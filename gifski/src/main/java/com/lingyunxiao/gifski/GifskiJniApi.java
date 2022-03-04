package com.lingyunxiao.gifski;

public class GifskiJniApi {

    public static native long gifskiNew(
            int width, int height, short quality, boolean fast, boolean repeat);

    public static native int setFileOutput(long instancePtr, String filePath);

    static {
        System.loadLibrary("gifskiad");
    }

    public static void logit(String msg) {
        MLog.info("gifski", msg);
    }

    public static void logit(String msg, long larg) {
        MLog.info("gifski", msg, larg);
    }

    public static void logit(String msg, boolean barg) {
        MLog.info("gifski", msg, barg);
    }

    public static void logit(String msg, int argi) {
        MLog.info("gifski", msg, argi);
    }
}
