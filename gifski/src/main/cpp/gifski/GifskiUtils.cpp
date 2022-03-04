#include "GifskiUtils.h"

#define JNI_CLASS "com/lingyunxiao/gifski/GifskiJniApi"

void *logStrB(JNIEnv *env, const char *log, jboolean barg) {
    jclass jjniApi = env->FindClass(JNI_CLASS);
    jmethodID mLogit = env->GetStaticMethodID(jjniApi, "logit", "(Ljava/lang/String;Z)V");
    jstring jlog = env->NewStringUTF(log);
    env->CallStaticVoidMethod(jjniApi, mLogit, jlog, barg);
    return nullptr;
}

void *logStrI(JNIEnv *env, const char *log, jint iarg) {
    jclass jjniApi = env->FindClass(JNI_CLASS);
    jmethodID mLogit = env->GetStaticMethodID(jjniApi, "logit", "(Ljava/lang/String;I)V");
    jstring jlog = env->NewStringUTF(log);
    env->CallStaticVoidMethod(jjniApi, mLogit, jlog, iarg);
    return nullptr;
}

void *logStrL(JNIEnv *env, const char *log, long iarg) {
    jclass jjniApi = env->FindClass(JNI_CLASS);
    jmethodID mLogit = env->GetStaticMethodID(jjniApi, "logit", "(Ljava/lang/String;J)V");
    jstring jlog = env->NewStringUTF(log);
    env->CallStaticVoidMethod(jjniApi, mLogit, jlog, iarg);
    return nullptr;
}

void *logStr(JNIEnv *env, const char *log) {
    jclass jjniApi = env->FindClass(JNI_CLASS);
    jmethodID mLogit = env->GetStaticMethodID(jjniApi, "logit", "(Ljava/lang/String;)V");
    jstring jlog = env->NewStringUTF(log);
    env->CallStaticVoidMethod(jjniApi, mLogit, jlog);
    return nullptr;
}
