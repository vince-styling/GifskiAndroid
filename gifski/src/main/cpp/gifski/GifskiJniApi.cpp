#include "GifskiJniApi.h"
#include <include/GifskiApi.h>

JNIEXPORT long JNICALL
JNI_FUNC(gifskiNew)(JNIEnv *env, jclass type,
                    jint width, jint height, jshort quality,
                    jboolean fast, jboolean repeat) {
    logStr(env, "new func invoked");

    auto *set = new GifskiSettings();
    set->width = width;
    logStrI(env, "width:%d", width);
    set->height = height;
    logStrI(env, "height:%d", height);
    set->quality = quality;
    logStrI(env, "quality:%d", quality);
    set->fast = fast;
    logStrB(env, "fast:%s", fast);
    set->repeat = repeat;
    logStrB(env, "repeat:%s", repeat);

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
JNI_FUNC(setFileOutput)(JNIEnv *env, jclass type,
                        jlong instancePtr, jstring filePath) {
    logStrL(env, "set file output:%d", instancePtr);
    auto *instance = (gifski *) instancePtr;
    const char *ntvFilePath = env->GetStringUTFChars(filePath, nullptr);
    GifskiError result = gifski_set_file_output(env, instance, ntvFilePath);
    logStrI(env, "set file output result:%d", result);
    env->ReleaseStringUTFChars(filePath, ntvFilePath);
    return result;
}

JNIEXPORT int JNICALL
JNI_FUNC(addFrameRgba)(JNIEnv *env, jclass type,
                        jlong instancePtr, jobject bitmap,
                        jint index, jint width, jint height, jint delay) {
    auto *instance = (gifski *) instancePtr;
    auto *data = static_cast<unsigned char *>(lockBitmapPixels(env, bitmap));
    if (data != nullptr) {
        return gifski_add_frame_rgba(env, instance, index, width, height, data, delay);
    }
    // 扩展自 GifskiError 的错误索引
    return 16;
}

JNIEXPORT int JNICALL
JNI_FUNC(finish)(JNIEnv *env, jclass type, jlong instancePtr) {
    auto *instance = (gifski *) instancePtr;
    logStr(env, "start to finish");
    int result = gifski_finish(instance);
    logStrI(env, "finish result:%d", result);
    return result;
}
