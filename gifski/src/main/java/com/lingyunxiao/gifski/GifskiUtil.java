package com.lingyunxiao.gifski;

public class GifskiUtil {
    public static String parseGifskiResult(int result) {
        switch (result) {
            case 0:
                return "ok";
            case 1:
                return "one of input arguments was NULL";
            case 2:
                return "a one-time function was called twice, or functions were called in wrong order";
            case 3:
                return "internal error related to palette quantization";
            case 4:
                return "internal error related to gif composing";
            case 5:
                return "internal error - unexpectedly aborted";
            case 6:
                return "I/O error: file or directory not found";
            case 7:
                return "I/O error: permission denied";
            case 8:
                return "I/O error: file already exists";
            case 9:
                return "invalid arguments passed to function";
            case 10:
                return "misc I/O error: timed out";
            case 11:
                return "misc I/O error: write zero";
            case 12:
                return "misc I/O error: interrupted";
            case 13:
                return "misc I/O error: unexpected eof";
            case 14:
                return "progress callback returned 0, writing aborted";
            case 15:
                return "should not happen, file a bug";
            default:
                return "unknow";
        }
    }
}
