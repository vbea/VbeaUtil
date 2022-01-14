package com.vbes.util.list;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DimenRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vbes.util.VbeUtil;

/**
 * 高度自定义的组列表浮动装饰
 * Created by Vbe on 2021/12/10.
 */
public class StickyDecoration extends RecyclerView.ItemDecoration {

    private DecorationCallback callback;
    private View groupView;
    private int groupViewHeight;

    public StickyDecoration(Context context, View view,  DecorationCallback decorationCallback) {
        this.callback = decorationCallback;
        groupView = view;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        if (pos == 0) {
            measureView(groupView, parent);
        }
        String groupId = callback.getGroupId(pos);
        if (groupId.equals("-1")) return;
        //只有是同一组的第一个才显示悬浮栏
        if (canShowGroup(pos)) {
            outRect.top = groupViewHeight;
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
        //int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            float top = view.getTop();
            int position = parent.getChildAdapterPosition(view);
            if (canShowGroup(position)) {
                String textLine = callback.getGroupId(position);//.toUpperCase();
                c.save();
                c.translate(left,top - groupViewHeight);//将画布起点移动到之前预留空间的左上角
                callback.onDrawGroup(groupView, textLine);
                measureView(groupView, parent);//因为内部控件设置了数据，所以需要重新测量View
                groupView.draw(c);
                c.restore();
            }
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
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
            c.save();
            int viewBottom = view.getBottom();
            float textY = Math.max(groupViewHeight, view.getTop());
            //下一个和当前不一样移动当前
            if (position + 1 < itemCount) {
                String nextGroupId = callback.getGroupId(position + 1);
                //组内最后一个view进入了header
                if (!nextGroupId.equals(groupId) && viewBottom < textY) {
                    textY = viewBottom;
                }
            }
            c.translate(left, textY - groupViewHeight);
            callback.onDrawGroup(groupView, groupId);
            measureView(groupView, parent);//因为内部控件设置了数据，所以需要重新测量View
            groupView.draw(c);
            c.restore();
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

    //定义一个接口方便外界的调用
    public interface DecorationCallback {
        String getGroupId(int position);
        void onDrawGroup(View v, String groupId);
    }

    /**
     * 测量View的大小和位置
     * @param view
     * @param parent
     */
    private void measureView(View view, View parent){
        if (view.getLayoutParams() == null) {
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int childWidth = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);

        int childHeight;
        if(view.getLayoutParams().height > 0){
            childHeight = View.MeasureSpec.makeMeasureSpec(view.getLayoutParams().height, View.MeasureSpec.EXACTLY);
        } else {
            childHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);//未指定
        }

        view.measure(childWidth, childHeight);
        view.layout(0,0,view.getMeasuredWidth(),view.getMeasuredHeight());

        groupViewHeight = view.getMeasuredHeight();
    }
}
