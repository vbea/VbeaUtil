package com.vbes.util.interfaces;

public interface DownloadListener {
    void process(long total);
    void success();
    void failed();
}
