#include "GifskiJniApi.h"
#include <include/GifskiApi.h>

JNIEXPORT jlong JNICALL
JNI_FUNC(gifskiNew)(JNIEnv *env, jclass type, jint width, jint height) {
    GifskiSettings *set = new GifskiSettings();
    LOGI("new func width:%d", width);
    set->width = width;
    set->height = height;
    set->quality = 90;
    set->fast = false;
    set->repeat = false;
    gifski *instance = gifski_new(env, set);
    if (instance == nullptr) {
        LOGI("gifski obj is null");
    } else {
        LOGI("gifski obj valid");

//        FILE *fp = fopen("/storage/emulated/0/Android/data/com.lingyx.gifgski/files/output.gif", "wb");
//        fwrite("GIF89a", 6, 1, fp);
//        fwrite(&width, 2, 1, fp);
//        fwrite(&height, 2, 1, fp);
//        uint8_t gifFileTerminator = 0x3B;
//        fwrite(&gifFileTerminator, 1, 1, fp);
//        fclose(fp);
//        fp = NULL;
//        LOGI("next gifski api");

        GifskiError error = gifski_set_file_output(env, instance, "/storage/emulated/0/Android/data/com.lingyx.gifgski/files/3476/output.gif");
        LOGI("gifski set file output result: %d", error);
        if (error != GIFSKI_OK) return 2232;

        for (int i = 0; i <= 42; i++) {
//            string str1;
//            if (i > 99) {
//                str1 = "/storage/emulated/0/Android/data/com.lingyx.gifgski/files/3476/000";
//            } else if (i > 9) {
//                str1 = "/storage/emulated/0/Android/data/com.lingyx.gifgski/files/3476/0000";
//            } else {
//                str1 = "/storage/emulated/0/Android/data/com.lingyx.gifgski/files/3476/00000";
//            }
//            string filePath = str1 + std::to_string(i) + ".png";

//            LOGI("gifski file path: %s", filePath.c_str());
//            jclass jBitmapFactory = env->FindClass("android/graphics/BitmapFactory");
//            jmethodID mDecodeFile = env->GetStaticMethodID(jBitmapFactory, "decodeFile", "(Ljava/lang/String;)Landroid/graphics/Bitmap;");
//            jstring filePathJString = env->NewStringUTF(filePath.c_str());
//            jobject bitmap = (jobject) env->CallStaticObjectMethod(jBitmapFactory, mDecodeFile, filePathJString);
//
//            AndroidBitmapInfo info;
//            unsigned char *data = static_cast<unsigned char *>(lockAndroidBitmap(env, bitmap, info));
//            assert(data != nullptr);
//            assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888);
//            int res = gifski_add_frame_rgba(env, instance, i, width, height, data, 5);

            int res = gifski_add_frame_png_file(env, instance, i, "sdksdds", 5);
            LOGI("gifski file path: %s end:%d", "ddd", res);
            if (res != GIFSKI_OK) break;
        }

//        jsize count = env->GetArrayLength(frames);
//        for (int i = 0; i < count; i++) {
//            LOGI("gifski file path: %d", i);
//            AndroidBitmapInfo info;
//            unsigned char *data = lockAndroidBitmap(env, i[frames], info);
//            assert(data != nullptr);
//            assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888);
//            int res = gifski_add_frame_rgba(instance, i, width, height, data, 5);
//            LOGI("gifski file path: %d end:%d", i, res);
//            if (res != GIFSKI_OK) break;
//        }

        LOGI("loop finish before");
        int res = gifski_finish(instance);
        LOGI("loop finish:%d", res);
        if (res != GIFSKI_OK) return 9120;
    }
    return 21342;
}