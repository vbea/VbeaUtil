package com.vbes.util.sticky;

import android.support.annotation.StyleableRes;

import com.vbes.util.sticky.lis.IResourceProvider;
import com.vbes.util.sticky.lis.IScreenInfoProvider;

/**
 * Created by Vbe on 2020/12/17.
 */
public class StickyScrollPresenter {

    private final IResourceProvider mTypedArrayResourceProvider;
    private StickyScrollPresentation mStickyScrollPresentation;
    private int mDeviceHeight;

    private int mStickyFooterHeight;
    private int mStickyFooterInitialTranslation;
    private int mStickyFooterInitialLocation;

    private int mStickyHeaderInitialLocation;
    private boolean mIsFooterSticky;
    private boolean mIsHeaderSticky;
    public boolean mScrolled;

    public StickyScrollPresenter(StickyScrollPresentation stickyScrollPresentation, IScreenInfoProvider screenInfoProvider, IResourceProvider typedArrayResourceProvider) {
        mDeviceHeight = screenInfoProvider.getScreenHeight();
        mTypedArrayResourceProvider = typedArrayResourceProvider;
        mStickyScrollPresentation = stickyScrollPresentation;
    }

    public void onGlobalLayoutChange(@StyleableRes int headerRes, @StyleableRes int footerRes) {
        int headerId = mTypedArrayResourceProvider.getResourceId(headerRes);
        if (headerId != 0) {
            mStickyScrollPresentation.initHeaderView(headerId);
        }
        int footerId = mTypedArrayResourceProvider.getResourceId(footerRes);
        if (footerId != 0) {
            mStickyScrollPresentation.initFooterView(footerId);
        }
        mTypedArrayResourceProvider.recycle();
    }

    public void initStickyFooter(int measuredHeight, int initialStickyFooterLocation) {
        mStickyFooterHeight = measuredHeight;
        mStickyFooterInitialLocation = initialStickyFooterLocation;
        mStickyFooterInitialTranslation = mDeviceHeight - initialStickyFooterLocation - mStickyFooterHeight;
        if (mStickyFooterInitialLocation > mDeviceHeight - mStickyFooterHeight) {
            mStickyScrollPresentation.stickFooter(mStickyFooterInitialTranslation);
            mIsFooterSticky = true;
        }
    }

    public void initStickyHeader(int headerTop) {
        mStickyHeaderInitialLocation = headerTop;
    }

    public void onScroll(int scrollY) {
        mScrolled = true;
        handleFooterStickiness(scrollY);
        handleHeaderStickiness(scrollY);
    }

    private void handleFooterStickiness(int scrollY) {
        if (scrollY > mStickyFooterInitialLocation - mDeviceHeight + mStickyFooterHeight) {
            mStickyScrollPresentation.freeFooter();
            mIsFooterSticky = false;
        } else {
            mStickyScrollPresentation.stickFooter(mStickyFooterInitialTranslation + scrollY);
            mIsFooterSticky = true;
        }
    }

    private void handleHeaderStickiness(int scrollY) {
        if (scrollY > mStickyHeaderInitialLocation) {
            mStickyScrollPresentation.stickHeader(scrollY - mStickyHeaderInitialLocation);
            mIsHeaderSticky = true;
        } else {
            mStickyScrollPresentation.freeHeader();
            mIsHeaderSticky = false;
        }
    }

    public boolean isFooterSticky() {
        return mIsFooterSticky;
    }

    public boolean isHeaderSticky() {
        return mIsHeaderSticky;
    }

    public void recomputeFooterLocation(int footerTop, int footerLocation) {
        if (mScrolled) {
            mStickyFooterInitialTranslation = mDeviceHeight - footerTop - mStickyFooterHeight;
            mStickyFooterInitialLocation = footerTop;
            if (footerLocation > mDeviceHeight - mStickyFooterHeight) {
                mStickyScrollPresentation.stickFooter(mStickyFooterInitialTranslation);
                mIsFooterSticky = true;
            } else {
                mStickyScrollPresentation.freeFooter();
                mIsFooterSticky = false;
            }
        } else {
            initStickyFooter(mStickyFooterHeight, footerTop);
        }
    }
}
