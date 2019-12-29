package com.vbes.util.lis;

public interface DownloadListner {
    void process(long total);
    void success();
    void failed();
}
