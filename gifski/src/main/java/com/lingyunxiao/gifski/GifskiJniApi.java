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

    public static void logit(String msg) {
        MLog.info("gifski", msg);
    }

    public static void logit(String msg, boolean barg) {
        MLog.info("gifski", msg, barg);
    }

    public static void logit(String msg, int argi) {
        MLog.info("gifski", msg, argi);
    }
}
