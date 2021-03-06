#include "GifskiJniApi.h"
#include <include/GifskiApi.h>
#include <sys/types.h>
#include <pthread.h>

static JavaVM *gJvm;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *pjvm, void *reserved) {
    gJvm = pjvm;

    JNIEnv *env;
    if (pjvm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    // https://zhuanlan.zhihu.com/p/362225059
    serveLoggerClass(env);
    logStr(env, "JNI_OnLoad");

    return JNI_VERSION_1_6;
}

/**
 * https://stackoverflow.com/questions/30026030/what-is-the-best-way-to-save-jnienv/30026231#30026231
 * Get a JNIEnv* valid for this thread, regardless of whether
 * we're on a native thread or a Java thread.
 * If the calling thread is not currently attached to the JVM
 * it will be attached, and then automatically detached when the
 * thread is destroyed.
 */
JNIEnv *GetJniEnv() {
    JNIEnv *env = nullptr;
    // We still call GetEnv first to detect if the thread already
    // is attached. This is done to avoid setting up a DetachCurrentThread
    // call on a Java thread.

    // gJvm is a global.
    auto get_env_result = gJvm->GetEnv((void**)&env, JNI_VERSION_1_6);
    if (get_env_result == JNI_EDETACHED) {
        if (gJvm->AttachCurrentThread(&env, NULL) == JNI_OK) {
            // defer thread detach
            static pthread_key_t thread_key;

            // Set up a Thread Specific Data key, and a callback that
            // will be executed when a thread is destroyed.
            // This is only done once, across all threads, and the value
            // associated with the key for any given thread will initially
            // be NULL.
            static auto run_once = [] {
                const auto err = pthread_key_create(&thread_key, [] (void *ts_env) {
                    if (ts_env) {
                        gJvm->DetachCurrentThread();
                    }
                });
                if (err) {
                    // Failed to create TSD key. Throw an exception if you want to.
                }
                return 0;
            }();

            // For the callback to actually be executed when a thread exits
            // we need to associate a non-NULL value with the key on that thread.
            // We can use the JNIEnv* as that value.
            const auto ts_env = pthread_getspecific(thread_key);
            if (!ts_env) {
                if (pthread_setspecific(thread_key, env)) {
                    // Failed to set thread-specific value for key. Throw an exception if you want to.
                }
            }
        } else {
            // Failed to attach thread. Throw an exception if you want to.
        }
    } else if (get_env_result == JNI_EVERSION) {
        // Unsupported JNI version. Throw an exception if you want to.
    }
    return env;
}

int taskKey = 0;

JNIEXPORT long JNICALL
JNI_FUNC(skigifNew)(JNIEnv *env, jclass type,
                    jint width, jint height, jshort quality,
                    jboolean fast, jint repeat) {
    auto *set = new GifskiSettings();
    set->width = width;
    set->height = height;
    set->quality = quality;
    set->fast = fast;
    set->repeat = repeat;

    gifski *instance = gifski_new(env, set);
    if (instance == nullptr) {
        logStr(env, "obj is null");
        return 0;
    } else {
        logStr(env, "obj valid");
        return (long) instance;
    }
}

JNIEXPORT int JNICALL
JNI_FUNC(startProcess)(JNIEnv *env, jclass type,
                        jlong instancePtr, jstring filePath, jint key) {
    logStr(env, "set file output:%d", instancePtr);
    auto *instance = (gifski *) instancePtr;

    taskKey = key;
    logStr(env, "start process task key:%d", taskKey);
    struct ProgressCallback {
        static int onFrameWrited(int user_data, int write_count, int frame_number) {
            auto env = GetJniEnv();
            logStr(env, "frame writed index:%d writed_count:%d key:%d vs %d", frame_number, write_count, taskKey, user_data);
            progressCallback(env, write_count, user_data);
            return user_data == taskKey ? 1 : 0;
        }
    };
    gifski_set_progress_callback(instance, ProgressCallback::onFrameWrited, taskKey);

    const char *ntvFilePath = env->GetStringUTFChars(filePath, nullptr);
    GifskiError result = gifski_set_file_output(env, instance, ntvFilePath);
    logStr(env, "set file output result:%d", result);
    env->ReleaseStringUTFChars(filePath, ntvFilePath);
    return result;
}

JNIEXPORT int JNICALL
JNI_FUNC(addFrameRgba)(JNIEnv *env, jclass type,
                        jlong instancePtr, jbyteArray pixels,
                        jint index, jint width, jint height, jdouble pts) {
    auto *instance = (gifski *) instancePtr;
    jbyte *bBuffer = env->GetByteArrayElements(pixels, 0);
    auto *data = (unsigned char *) bBuffer;
    if (data != nullptr) {
        GifskiError result = gifski_add_frame_rgba(env, instance, index, width, height, data, pts);
        env->ReleaseByteArrayElements(pixels, bBuffer, 0);
        return result;
    }
    // ????????? GifskiError ???????????????
    return 16;
}

JNIEXPORT int JNICALL
JNI_FUNC(addFrameArgb)(JNIEnv *env, jclass type,
                       jlong instancePtr, jobject bitmap,
                       jint index, jint width, jint height, jint rowBytes, jdouble pts) {
    auto *instance = (gifski *) instancePtr;
    auto *data = static_cast<unsigned char *>(lockBitmapPixels(env, bitmap));
    if (data != nullptr) {
        GifskiError result = gifski_add_frame_argb(env, instance, index, width, rowBytes, height, data, pts);
        AndroidBitmap_unlockPixels(env, bitmap);
        return result;
    }
    // ????????? GifskiError ???????????????
    return 16;
}

JNIEXPORT int JNICALL
JNI_FUNC(addFrameFile)(JNIEnv *env, jclass type,
                        jlong instancePtr, jstring framePath,
                        jint index, jdouble pts) {
    auto *instance = (gifski *) instancePtr;
    const char *ntvFramePath = env->GetStringUTFChars(framePath, nullptr);
    GifskiError result = gifski_add_frame_png_file(env, instance, index, ntvFramePath, pts);
    env->ReleaseStringUTFChars(framePath, ntvFramePath);
    return result;
}

JNIEXPORT int JNICALL
JNI_FUNC(finish)(JNIEnv *env, jclass type, jlong instancePtr) {
    auto *instance = (gifski *) instancePtr;
    logStr(env, "start to finish");
    int result = gifski_finish(env, instance);
    logStr(env, "finish result:%d", result);
    taskKey = 0;
    return result;
}

JNIEXPORT void JNICALL
JNI_FUNC(abort)(JNIEnv *env, jclass type, jint key) {
    logStr(env, "abort: %d", taskKey);
    if (key == taskKey) {
        taskKey = 0;
    }
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved) {
    JNIEnv *env;
    if (vm->GetEnv((void **)(&env), JNI_VERSION_1_6) != JNI_OK) {
        return;
    }
    gJvm = nullptr;
    releaseLoggerClass(env);
}