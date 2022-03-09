#include "GifskiUtils.h"

#define JNI_CLASS "com/lingyunxiao/gifski/GifskiJniApi"
#define METHOD_NAME "logit"

jclass loggerClass;

void *serveLoggerClass(JNIEnv *env) {
    jclass jniClass = env->FindClass(JNI_CLASS);
    loggerClass = (jclass) (env->NewGlobalRef(jniClass));
    env->DeleteLocalRef(jniClass);
    return nullptr;
}

void *releaseLoggerClass(JNIEnv *env) {
    if (loggerClass != nullptr) {
        env->DeleteGlobalRef(loggerClass);
        loggerClass = nullptr;
    }
    return nullptr;
}

void *logStrB(JNIEnv *env, const char *log, jboolean barg) {
    jmethodID mLogit = env->GetStaticMethodID(loggerClass, METHOD_NAME, "(Ljava/lang/String;Z)V");
    jstring jlog = env->NewStringUTF(log);
    env->CallStaticVoidMethod(loggerClass, mLogit, jlog, barg);
    return nullptr;
}

void *logStrI(JNIEnv *env, const char *log, jint iarg) {
    jmethodID mLogit = env->GetStaticMethodID(loggerClass, METHOD_NAME, "(Ljava/lang/String;I)V");
    jstring jlog = env->NewStringUTF(log);
    env->CallStaticVoidMethod(loggerClass, mLogit, jlog, iarg);
    return nullptr;
}

void *logStrS(JNIEnv *env, const char *log, jstring sarg) {
    jmethodID mLogit = env->GetStaticMethodID(loggerClass, METHOD_NAME, "(Ljava/lang/String;Ljava/lang/String;)V");
    jstring jlog = env->NewStringUTF(log);
    env->CallStaticVoidMethod(loggerClass, mLogit, jlog, sarg);
    return nullptr;
}

void *logStrL(JNIEnv *env, const char *log, long iarg) {
    jmethodID mLogit = env->GetStaticMethodID(loggerClass, METHOD_NAME, "(Ljava/lang/String;J)V");
    jstring jlog = env->NewStringUTF(log);
    env->CallStaticVoidMethod(loggerClass, mLogit, jlog, iarg);
    return nullptr;
}

void *logStr(JNIEnv *env, const char *log) {
    jmethodID mLogit = env->GetStaticMethodID(loggerClass, METHOD_NAME, "(Ljava/lang/String;)V");
    jstring jlog = env->NewStringUTF(log);
    env->CallStaticVoidMethod(loggerClass, mLogit, jlog);
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
