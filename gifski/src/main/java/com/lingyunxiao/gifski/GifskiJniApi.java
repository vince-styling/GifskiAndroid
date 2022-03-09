package com.lingyunxiao.gifski;

import android.graphics.Bitmap;

public class GifskiJniApi {

    public static native long gifskiNew(
            int width, int height, short quality, boolean fast, boolean repeat);

    public static native int startProcess(long instancePtr, String filePath, int key);

    public static native int addFrameRgba(
            long instancePtr, Bitmap bitmap,
            int index, int width, int height, int delay);

    public static native int finish(long instancePtr);

    public static native void abort(int key);

    static {
        System.loadLibrary("gifskiad");
    }

    public static void onFrameWrited(int frameNumber, int taskKey) {
        InstanceKeeper.progressCallback.onFrameWrited(frameNumber, taskKey);
    }

    public static void logit(String msg) {
        InstanceKeeper.logger.logit(msg);
    }

    public static void logit(String msg, boolean arg) {
        InstanceKeeper.logger.logit(msg, arg);
    }

    public static void logit(String msg, int arg) {
        InstanceKeeper.logger.logit(msg, arg);
    }
}
