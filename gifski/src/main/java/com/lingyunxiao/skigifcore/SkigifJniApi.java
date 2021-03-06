package com.lingyunxiao.skigifcore;

import android.graphics.Bitmap;

public class SkigifJniApi {

    public static native long skigifNew(
            int width, int height, short quality, boolean fast, int repeat);

    public static native int startProcess(long instancePtr, String filePath, int key);

    public static native int addFrameRgba(
            long instancePtr, byte[] pixels,
            int index, int width, int height, double pts);

    public static native int addFrameArgb(
            long instancePtr, Bitmap bitmap,
            int index, int width, int height, int rowBytes, double pts);

    public static native int addFrameFile(long instancePtr, String framePath, int index, double pts);

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
}
