package com.lingyunxiao.gifski;

public interface ILogger {
    void logit(String msg);

    void logit(String msg, boolean arg);

    void logit(String msg, int arg);
}
