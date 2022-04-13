package com.lingyunxiao.skigifcore;

public interface ProgressCallback {
    void onFrameWrited(int writeCount, int taskKey);
}
