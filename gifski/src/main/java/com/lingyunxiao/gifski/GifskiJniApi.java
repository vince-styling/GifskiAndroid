package com.lingyunxiao.gifski;

public class GifskiJniApi {

    public static native long gifskiNew(int width, int height);

    static {
        System.loadLibrary("gifskiad");
    }

    public static void logit(String msg) {
        MLog.info("gifski", msg);
    }
}
