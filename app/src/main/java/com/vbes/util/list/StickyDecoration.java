package com.vbes.util.list;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vbes.util.DensityUtil;
import com.vbes.util.VbeUtil;

/**
 * Created by Vbe on 2021/12/10.
 */
public class StickyDecoration extends RecyclerView.ItemDecoration {

    private DecorationCallback callback;
    private TextPaint textPaint;
    private Paint paint;
    private int topGap;
    private int alignBottom;
    private Paint.FontMetrics fontMetrics;

    public StickyDecoration(Context context, StickyOptions options, DecorationCallback decorationCallback) {
        Resources res = context.getResources();
        this.callback = decorationCallback;
        //设置悬浮栏的画笔---paint
        paint = new Paint();
        paint.setColor(res.getColor(options.stickyBackground));

        //设置悬浮栏中文本的画笔
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DensityUtil.sp2px(context, options.textSize));
        textPaint.setColor(res.getColor(options.textColor));
        textPaint.setTextAlign(Paint.Align.LEFT);
        fontMetrics = new Paint.FontMetrics();
        //决定悬浮栏的高度等
        topGap = res.getDimensionPixelSize(options.stickyHeight);
        //决定文本的显示位置等
        alignBottom = res.getDimensionPixelSize(options.stickyPadding);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        String groupId = callback.getGroupId(pos);
        if (groupId.equals("-1")) return;
        //只有是同一组的第一个才显示悬浮栏
        if (canShowGroup(pos)) {
            outRect.top = topGap;
            if (callback.getGroupId(pos).equals(""))
                outRect.top = 0;
        } else {
            outRect.top = 0;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            if (canShowGroup(position)) {
                String textLine = callback.getGroupId(position);//.toUpperCase();
                float top = view.getTop() - topGap;
                float bottom = view.getTop();
                //绘制悬浮栏
                c.drawRect(left, top - topGap, right, bottom, paint);
                //绘制文本
                c.drawText(textLine, left, bottom, textPaint);
            } else {
                float top = view.getTop();
                float bottom = view.getTop();
                c.drawRect(left, top, right, bottom, paint);
                return;
            }
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        //float lineHeight = textPaint.getTextSize() + fontMetrics.descent;

        String preGroupId = "";
        String groupId = "-1";
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            preGroupId = groupId;
            groupId = callback.getGroupId(position);
            if (groupId.equals("") || groupId.equals(preGroupId)) continue;

            //String textLine = callback.getGroupId(position);//.toUpperCase();
            //if (textLine.equals("")) continue;

            int viewBottom = view.getBottom();
            float textY = Math.max(topGap, view.getTop());
            //下一个和当前不一样移动当前
            if (position + 1 < itemCount) {
                String nextGroupId = callback.getGroupId(position + 1);
                //组内最后一个view进入了header
                if (!nextGroupId.equals(groupId) && viewBottom < textY)
                    textY = viewBottom;
            }
            //textY - topGap决定了悬浮栏绘制的高度和位置
            c.drawRect(left, textY - topGap, right, textY, paint);
            //left+2*alignBottom 决定了文本往左偏移的多少（加-->向左移）
            //textY-alignBottom  决定了文本往右偏移的多少  (减-->向上移)
            c.drawText(groupId, left + 2 * alignBottom, textY - alignBottom, textPaint);
        }
    }

    /**
     * 判断是否需要显示组
     *
     * @param pos 下标
     * @return 是否需要显示
     */
    private boolean canShowGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            // 因为是根据 字符串内容的相同与否 来判断是不是同一组的，所以此处的标记id 要是String类型
            // 如果你只是做联系人列表，悬浮框里显示的只是一个字母，则标记id直接用 int 类型就行了
            String groupId = callback.getGroupId(pos);
            if (VbeUtil.isNullOrEmpty(groupId))
                return false;
            String prevGroupId = callback.getGroupId(pos - 1);
            //判断前一个字符串 与 当前字符串 是否相同
            return !prevGroupId.equals(groupId);
        }
    }

    public static class StickyOptions {
        private int textColor;
        private int textSize;
        private int stickyHeight;
        private int stickyPadding;
        private int stickyBackground;

        /**
         * 设置悬浮头组件的文本颜色
         * @param textColor 文本颜色
         */
        public void setTextColor(@ColorRes int textColor) {
            this.textColor = textColor;
        }

        /**
         * 设置悬浮头组件的文字大小
         * @param textSize 文字大小sp值，例如14
         */
        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        /**
         * 设置悬浮头组件的高度
         * @param stickyHeight dimen高度
         */
        public void setStickyHeight(@DimenRes int stickyHeight) {
            this.stickyHeight = stickyHeight;
        }

        /**
         * 设置悬浮头组件的文字和边缘间距
         * @param stickyPadding dimen间距
         */
        public void setStickyPadding(@DimenRes int stickyPadding) {
            this.stickyPadding = stickyPadding;
        }

        /**
         * 设置悬浮头组件的背景颜色
         * @param stickyBackground 背景颜色
         */
        public void setStickyBackground(@ColorRes int stickyBackground) {
            this.stickyBackground = stickyBackground;
        }
    }

    //定义一个接口方便外界的调用
    public interface DecorationCallback {
        String getGroupId(int position);
    }
}
