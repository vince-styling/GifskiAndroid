#ifndef GIFSKIANDROID_GIFSKIJNIAPI_H
#define GIFSKIANDROID_GIFSKIJNIAPI_H

#include "GifskiUtils.h"

#define JNI_FUNC(x) Java_com_lingyunxiao_gifski_GifskiJniApi_##x

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL
JNI_FUNC(gifskiNew)(JNIEnv *env, jclass type, jint width, jint height);

#ifdef __cplusplus
}
#endif

#endif //GIFSKIANDROID_GIFSKIJNIAPI_H