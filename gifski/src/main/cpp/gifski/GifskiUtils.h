#ifndef GIFSKIANDROID_GIFSKI_H
#define GIFSKIANDROID_GIFSKI_H

#ifdef __cplusplus
extern "C" {
#endif

#include <jni.h>
#include <android/bitmap.h>

void *serveLoggerClass(JNIEnv *env);
void *releaseLoggerClass(JNIEnv *env);
void *logStrB(JNIEnv *env, const char *log, bool barg);
void *logStrI(JNIEnv *env, const char *log, int iarg);
void *logStr(JNIEnv *env, const char *format, ...);

/**
 * lock android bitmap and return data and write info
 * @param env
 * @param bitmap
 * @param info
 * @return data
 */
void *lockBitmapPixels(JNIEnv *env, jobject bitmap);

#ifdef __cplusplus
}
#endif

#endif //GIFSKIANDROID_GIFSKI_H
