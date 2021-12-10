package com.vbes.util.list;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import android.os.Build;
import android.view.View;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.content.Context;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;

import com.vbes.util.R;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class DividerDecoration extends ItemDecoration {

    private Drawable drawable;
    private final Rect mBounds = new Rect();

    public DividerDecoration(Context context) {
        this(context, R.drawable.ic_divider);
    }

    public DividerDecoration(Context context, @DrawableRes int id) {
        drawable = ContextCompat.getDrawable(context, id);
		/*TypedArray a = context.obtainStyledAttributes(new int[]{R.attr.divider});
		drawable = a.getDrawable(0);
		a.recycle();*/
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        canvas.save();
        final int left;
        final int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - drawable.getIntrinsicHeight();
            drawable.setBounds(left, top, right, bottom);
            drawable.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.set(0, 0, 0, drawable.getIntrinsicHeight());
    }
}
