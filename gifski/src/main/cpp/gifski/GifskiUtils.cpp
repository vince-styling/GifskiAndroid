#include <cstdio>
#include "GifskiUtils.h"

#define JNI_CLASS "com/lingyunxiao/gifski/GifskiJniApi"
#define METHOD_NAME "logit"

jclass jniApiClass;

void *serveLoggerClass(JNIEnv *env) {
    jclass jniClass = env->FindClass(JNI_CLASS);
    jniApiClass = (jclass) (env->NewGlobalRef(jniClass));
    env->DeleteLocalRef(jniClass);
    return nullptr;
}

void *releaseLoggerClass(JNIEnv *env) {
    if (jniApiClass != nullptr) {
        env->DeleteGlobalRef(jniApiClass);
        jniApiClass = nullptr;
    }
    return nullptr;
}

void *logStr(JNIEnv *env, const char *format, ...) {
    char buffer[256];
    va_list args;
    va_start(args, format);
    vsprintf(buffer, format, args);
    va_end(args);
    jmethodID mLogit = env->GetStaticMethodID(jniApiClass, METHOD_NAME, "(Ljava/lang/String;)V");
    jstring jlog = env->NewStringUTF(buffer);
    env->CallStaticVoidMethod(jniApiClass, mLogit, jlog);
    return nullptr;
}

void *progressCallback(JNIEnv *env, int writeCount, int taskKey) {
    jmethodID cb = env->GetStaticMethodID(jniApiClass, "onFrameWrited", "(II)V");
    env->CallStaticVoidMethod(jniApiClass, cb, writeCount, taskKey);
    return nullptr;
}

void *lockBitmapPixels(JNIEnv *env, jobject bitmap) {
    void *pixels = nullptr;
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
        logStr(env, "lockPixels error");
        return nullptr;
    }
    return pixels;
}
