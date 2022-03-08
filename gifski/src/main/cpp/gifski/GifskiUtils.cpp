#include "GifskiUtils.h"

#define JNI_CLASS "com/lingyunxiao/gifski/GifskiJniApi"
#define METHOD_NAME "logit"

void *logStrB(JNIEnv *env, const char *log, jboolean barg) {
    jclass jjniApi = env->FindClass(JNI_CLASS);
    jmethodID mLogit = env->GetStaticMethodID(jjniApi, METHOD_NAME, "(Ljava/lang/String;Z)V");
    jstring jlog = env->NewStringUTF(log);
    env->CallStaticVoidMethod(jjniApi, mLogit, jlog, barg);
    return nullptr;
}

void *logStrI(JNIEnv *env, const char *log, jint iarg) {
    jclass jjniApi = env->FindClass(JNI_CLASS);
    jmethodID mLogit = env->GetStaticMethodID(jjniApi, METHOD_NAME, "(Ljava/lang/String;I)V");
    jstring jlog = env->NewStringUTF(log);
    env->CallStaticVoidMethod(jjniApi, mLogit, jlog, iarg);
    return nullptr;
}

void *logStrS(JNIEnv *env, const char *log, jstring sarg) {
    jclass jjniApi = env->FindClass(JNI_CLASS);
    jmethodID mLogit = env->GetStaticMethodID(jjniApi, METHOD_NAME, "(Ljava/lang/String;Ljava/lang/String;)V");
    jstring jlog = env->NewStringUTF(log);
    env->CallStaticVoidMethod(jjniApi, mLogit, jlog, sarg);
    return nullptr;
}

void *logStrL(JNIEnv *env, const char *log, long iarg) {
    jclass jjniApi = env->FindClass(JNI_CLASS);
    jmethodID mLogit = env->GetStaticMethodID(jjniApi, METHOD_NAME, "(Ljava/lang/String;J)V");
    jstring jlog = env->NewStringUTF(log);
    env->CallStaticVoidMethod(jjniApi, mLogit, jlog, iarg);
    return nullptr;
}

void *logStr(JNIEnv *env, const char *log) {
    jclass jjniApi = env->FindClass(JNI_CLASS);
    jmethodID mLogit = env->GetStaticMethodID(jjniApi, METHOD_NAME, "(Ljava/lang/String;)V");
    jstring jlog = env->NewStringUTF(log);
    env->CallStaticVoidMethod(jjniApi, mLogit, jlog);
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
