package com.vbes.util.lis;

/**
 * Created by Vbe on 2020/12/17.
 */
public interface OnStickyScrollListener {
    void onScrollChanged(int l, int t, int oldl, int oldt);
    void onScrollStopped(boolean isStoped);
}
