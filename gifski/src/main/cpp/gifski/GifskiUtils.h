#ifndef GIFSKIANDROID_GIFSKI_H
#define GIFSKIANDROID_GIFSKI_H

#ifdef __cplusplus
extern "C" {
#endif

#include <jni.h>

void *logStrI(JNIEnv *env, const char *log, jint iarg);

void *logStr(JNIEnv *env, const char *log);

#ifdef __cplusplus
}
#endif

#endif //GIFSKIANDROID_GIFSKI_H
