package com.hzy.face.morphme.activity;

import android.util.Log;

public class MLog {
    private static final String TAG = "MLog";

    public static void verbose(Object obj, String format, Object... args) {
        if (obj == null || format == null || args == null) {
            Log.w(TAG, "param is null error!!!");
            return;
        }
        Log.v(String.valueOf(obj), String.format(format, args));
    }

    public static void verbose(String tag, String message) {
        if (tag == null || message == null) {
            Log.w(TAG, "param is null error!!!");
            return;
        }
        Log.v(tag, message);
    }

    public static void debug(Object obj, String format, Object... args) {
        if (obj == null || format == null || args == null) {
            Log.w(TAG, "param is null error!!!");
            return;
        }
        Log.d(String.valueOf(obj), String.format(format, args));
    }

    public static void debug(String tag, String message) {
        if (tag == null || message == null) {
            Log.w(TAG, "param is null error!!!");
            return;
        }
        Log.d(tag, message);
    }

    public static void info(Object obj, String format, Object... args) {
        if (obj == null || format == null || args == null) {
            Log.w(TAG, "param is null error!!!");
            return;
        }
        Log.i(String.valueOf(obj), String.format(format, args));
    }

    public static void info(String tag, String message) {
        if (tag == null || message == null) {
            Log.w(TAG, "param is null error!!!");
            return;
        }
        Log.i(tag, message);
    }

    public static void warn(Object obj, String format, Object... args) {
        if (obj == null || format == null || args == null) {
            Log.w(TAG, "param is null error!!!");
            return;
        }
        Log.w(String.valueOf(obj), String.format(format, args));
    }

    public static void warn(String tag, String message) {
        if (tag == null || message == null) {
            Log.w(TAG, "param is null error!!!");
            return;
        }
        Log.w(tag, message);
    }

    public static void error(Object obj, String format, Object... args) {
        if (obj == null || format == null || args == null) {
            Log.w(TAG, "param is null error!!!");
            return;
        }
        Log.e(String.valueOf(obj), String.format(format, args), null);
    }

    public static void error(String tag, String message) {
        if (tag == null || message == null) {
            Log.w(TAG, "param is null error!!!");
            return;
        }
        Log.e(tag, message);
    }

    public static void error(Object obj, Throwable t) {
        error(obj, "", t);
    }

    public static void error(Object obj, String message, Throwable t) {
        if (obj == null) {
            Log.w(TAG, "param is null error!!!");
            return;
        }
        Log.e(String.valueOf(obj), message, t);
    }

    public static boolean isLogLevelAboveDebug() {
        return true;
    }
}
