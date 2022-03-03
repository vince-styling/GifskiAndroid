#ifndef GIFSKIANDROID_GIFSKIJNIAPI_H
#define GIFSKIANDROID_GIFSKIJNIAPI_H

#define JNI_FUNC(x) Java_com_lingyunxiao_gifski_GifskiJniApi_##x

#ifdef __cplusplus
extern "C" {
#endif

#include <jni.h>

#ifdef NDEBUG
#define LOGD(...) do{}while(0)
#define LOGI(...) do{}while(0)
#define LOGW(...) do{}while(0)
#define LOGE(...) do{}while(0)
#define LOGF(...) do{}while(0)
#else
#define LOG_TAG "GifskiJni"

#include <android/log.h>

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,LOG_TAG,__VA_ARGS__)
#endif

JNIEXPORT jlong JNICALL
JNI_FUNC(gifskiNew)(JNIEnv *env, jclass type, jint width, jint height);

#ifdef __cplusplus
}
#endif

#endif //GIFSKIANDROID_GIFSKIJNIAPI_H