package com.lingyunxiao.skigifcore;

import android.graphics.Bitmap;

public class SkigifJniApi {

    public static native long skigifNew(
            int width, int height, short quality, boolean fast, int repeat);

    public static native int startProcess(long instancePtr, String filePath, int key);

    public static native int addFrameRgba(
            long instancePtr, Bitmap bitmap,
            int index, int width, int height, double pts);

    public static native int finish(long instancePtr);

    public static native void abort(int key);

    static {
        System.loadLibrary("skigifcore");
    }

    public static void onFrameWrited(int writeCount, int taskKey) {
        final ProgressCallback callback = InstanceKeeper.progressCallback;
        if (callback != null) callback.onFrameWrited(writeCount, taskKey);
    }

    public static void logit(String msg) {
        final ILogger logger = InstanceKeeper.logger;
        if (logger != null) logger.logit(msg);
    }

    public static void logit(String msg, boolean arg) {
        final ILogger logger = InstanceKeeper.logger;
        if (logger != null) logger.logit(msg, arg);
    }

    public static void logit(String msg, int arg) {
        final ILogger logger = InstanceKeeper.logger;
        if (logger != null) logger.logit(msg, arg);
    }
}
