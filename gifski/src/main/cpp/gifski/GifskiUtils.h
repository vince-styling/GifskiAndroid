#ifndef GIFSKIANDROID_GIFSKI_H
#define GIFSKIANDROID_GIFSKI_H

#ifdef __cplusplus
extern "C" {
#endif

#include <jni.h>
#include <android/bitmap.h>

void *serveLoggerClass(JNIEnv *env);
void *releaseLoggerClass(JNIEnv *env);
void *logStrB(JNIEnv *env, const char *log, jboolean barg);
void *logStrI(JNIEnv *env, const char *log, jint iarg);
void *logStrS(JNIEnv *env, const char *log, jstring sarg);
void *logStrL(JNIEnv *env, const char *log, long iarg);
void *logStr(JNIEnv *env, const char *log);

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
