package com.zhj.bluetooth.sdkdemo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.zhj.bluetooth.sdkdemo.R;


/**
 */
public class ValueStateTextView extends AppCompatTextView {

    private boolean isOpen = false;

    private Drawable d;

    private static final int[] OPEN_STATE_SET = { R.attr.state_open };
    private int drawableW=0;
    private int width=0;
    public ValueStateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ValueStateTextView);
        d = a.getDrawable(R.styleable.ValueStateTextView_checkBackground);
        a.recycle();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width=getMeasuredWidth();
//		drawableW=d.getIntrinsicWidth();
    }
    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
        refreshDrawableState();

    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isOpen()) {
            mergeDrawableStates(drawableState, OPEN_STATE_SET);
//            drawableW=d.getIntrinsicWidth();
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (d != null && d.isStateful()) {
            d.setState(getDrawableState());
            drawableW=d.getIntrinsicWidth();
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (d != null) {
//            int h = getHeight();
//            int w = h/2;
//            int size = Math.min(w, getHeight());
//            d.setBounds(w / 2 - size / 2, h / 2 - size / 2, w / 2 + size / 2, h / 2 + size / 2);
            int h = getHeight();
            int w = h/2-3;
            int size = Math.min(w, getHeight());
            int left=(width-drawableW)/2;
//			int left=w / 2 - size / 2;
//			int top=h / 2 - size / 2;
            int top=h / 2 - drawableW / 2;
//			int right=w / 2 + size / 2;
            int right=drawableW+left;
//			int bottom=h / 2 + size / 2;
            int bottom=drawableW+top;
//            DebugLog.d("left:" + left + ",top:" + top + ",right:" + right + ",bottom:" + bottom + ",width:" + width + ",drawableW:" + drawableW);
            d.setBounds(left, top, right, bottom);
            d.draw(canvas);
        }
        super.onDraw(canvas);
    }

}