package com.lingyunxiao.gifski;

public interface ProgressCallback {
    void onFrameWrited(int writeCount, int taskKey);
}
