#ifndef GIFSKIANDROID_GIFSKIJNIAPI_H
#define GIFSKIANDROID_GIFSKIJNIAPI_H

#include "GifskiUtils.h"

#define JNI_FUNC(x) Java_com_lingyunxiao_gifski_GifskiJniApi_##x

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT long JNICALL
JNI_FUNC(gifskiNew)(JNIEnv *env, jclass type,
                    jint width, jint height, jshort quality,
                    jboolean fast, jboolean repeat);

JNIEXPORT int JNICALL
JNI_FUNC(setFileOutput)(JNIEnv *env, jclass type,
                        jlong instancePtr, jstring filePath);

JNIEXPORT int JNICALL
JNI_FUNC(addFrameRgba)(JNIEnv *env, jclass type,
                       jlong instancePtr, jobject bitmap,
                       jint index, jint width, jint height, jint delay);

JNIEXPORT int JNICALL
JNI_FUNC(finish)(JNIEnv *env, jclass type, jlong instancePtr);

#ifdef __cplusplus
}
#endif

#endif //GIFSKIANDROID_GIFSKIJNIAPI_H