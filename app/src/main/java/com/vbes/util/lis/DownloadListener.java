package com.vbes.util.lis;

public interface DownloadListener {
    void process(long total);
    void success();
    void failed();
}
