#ifndef GIFSKIANDROID_GIFSKIJNIAPI_H
#define GIFSKIANDROID_GIFSKIJNIAPI_H

#include "GifskiUtils.h"
#include <android/log.h>

#define JNI_FUNC(x) Java_com_lingyunxiao_skigifcore_SkigifJniApi_##x

#ifdef __cplusplus
extern "C" {
#endif

extern int taskKey;

JNIEXPORT long JNICALL
JNI_FUNC(skigifNew)(JNIEnv *env, jclass type,
                    jint width, jint height, jshort quality,
                    jboolean fast, jint repeat);

JNIEXPORT int JNICALL
JNI_FUNC(startProcess)(JNIEnv *env, jclass type,
                        jlong instancePtr, jstring filePath, jint key);

JNIEXPORT int JNICALL
JNI_FUNC(addFrameRgba)(JNIEnv *env, jclass type,
                       jlong instancePtr, jobject bitmap,
                       jint index, jint width, jint height, jdouble pts);

JNIEXPORT int JNICALL
JNI_FUNC(addFrameFile)(JNIEnv *env, jclass type,
                       jlong instancePtr, jstring framePath,
                       jint index, jdouble pts);

JNIEXPORT void JNICALL
JNI_FUNC(abort)(JNIEnv *env, jclass type, jint key);

JNIEXPORT int JNICALL
JNI_FUNC(finish)(JNIEnv *env, jclass type, jlong instancePtr);

#ifdef __cplusplus
}
#endif

#endif //GIFSKIANDROID_GIFSKIJNIAPI_H