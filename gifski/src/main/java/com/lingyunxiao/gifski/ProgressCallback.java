package com.lingyunxiao.gifski;

public interface ProgressCallback {
    /**
     * @param frameNumber start from 1
     */
    void onFrameWrited(int frameNumber, int taskKey);
}
