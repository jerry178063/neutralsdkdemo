package com.zhj.bluetooth.sdkdemo.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.util.ScreenUtil;


/**
 */
public class ItemLableValue extends RelativeLayout {

    private TextView lableView;

    public ValueStateTextView valueView;

    private String lable, value;

    private String targetActivty;

    protected Paint paint;

    private boolean hasBottomLine;

    protected int bottomLineColor;

    private boolean hasTopLine;

    private boolean has_all_line;

    protected int topLineColor;
    public View rightIv;
    private Context context;

    public ItemLableValue(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemLableValue(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_item_lable_value, this, true);
        lableView = findViewById(R.id.lable);
        valueView = findViewById(R.id.value);
        ImageView imageView = (ImageView) findViewById(R.id.left_drawable);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemLableValue);

        lable = a.getString(R.styleable.ItemLableValue_lable_text);
        value = a.getString(R.styleable.ItemLableValue_value_text);
        targetActivty = a.getString(R.styleable.ItemLableValue_target_activty);
        // boolean isOpen = a.getBoolean(R.styleable.ItemLableValue_opened,
        // true);
        // boolean isEnable =
        // a.getBoolean(R.styleable.ItemLableValue_android_enabled, true);
        int valueTextColor = a.getColor(R.styleable.ItemLableValue_valueTextColor, 0);
        int valueTextSize = a.getInteger(R.styleable.ItemLableValue_mvalueTextSize, 12);
        has_all_line = a.getBoolean(R.styleable.ItemLableValue_has_all_line, false);
        setHasBottomLine(a.getBoolean(R.styleable.ItemLableValue_has_bottom_line, true));
        setHasTopLine(a.getBoolean(R.styleable.ItemLableValue_has_top_line, false));
        if (isHasBottomLine()) {
            bottomLineColor = a.getColor(R.styleable.ItemLableValue_bottom_line_color, getResources().getColor(R.color.driver_color));
            initDraw();
        }

        if (isHasTopLine()) {
            topLineColor = a.getColor(R.styleable.ItemLableValue_top_line_color, getResources().getColor(R.color.driver_color));
            initTopDraw();
        }

        Drawable rightDrawable = a.getDrawable(R.styleable.ItemLableValue_right_arrow);
        Drawable leftDrawable = a.getDrawable(R.styleable.ItemLableValue_left_drawable);
        if (leftDrawable == null) {
            imageView.setVisibility(View.GONE);
        }
        imageView.setImageDrawable(leftDrawable);
        a.recycle();

        if (rightDrawable != null) {
            rightDrawable.setBounds(0, 0, rightDrawable.getIntrinsicWidth(), rightDrawable.getIntrinsicHeight());
            valueView.setCompoundDrawables(null, null, rightDrawable, null);
        }
        if (valueTextColor != 0) {
            valueView.setTextColor(valueTextColor);
        }

        lableView.setText(lable);
        valueView.setText(value);
        valueView.setTextSize(valueTextSize);
        if (targetActivty != null) {
            setOnClickListener(onClick);
        }
    }

    protected void initDraw() {
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(bottomLineColor);
        paint.setStrokeWidth(2);
    }


    protected void initTopDraw() {
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(topLineColor);
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isHasBottomLine()) {
            if (has_all_line) {
                canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), paint);
            } else {
                canvas.drawLine(ScreenUtil.dp2px(35), getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), paint);
            }
        }
        if (isHasTopLine()) {
            if (has_all_line) {
                canvas.drawLine(0, 0, getMeasuredWidth(), 0, paint);
            } else {
                canvas.drawLine(ScreenUtil.dp2px(35), 0, getMeasuredWidth(), 0, paint);
            }
        }
    }

    private OnClickListener onClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (targetActivty != null && valueView.isEnabled()) {
                Intent intent = new Intent();
                intent.setClassName(getContext(), targetActivty);
                getContext().startActivity(intent);
            }
//            switch (v.getId()) {
//                case R.id.item_ranking_list:
//                    StatService.onEvent(context, "K1", "今日排行榜");
//                    break;
//                case R.id.item_target_setting:
//                    StatService.onEvent(context, "L1", "目标设置");
//                    break;
//                case R.id.item_person_info:
//                    StatService.onEvent(context, "M1", "个人资料");
//                    break;
//                case R.id.item_sys_systems:
//                    StatService.onEvent(context, "N1", "系统设置");
//                    break;
//            }
        }
    };


    public void setEnable(boolean enable) {
        super.setEnabled(enable);
        valueView.setEnabled(enable);
//		if(!enable){
//		}
    }

    public boolean isEnable() {
        return valueView.isEnabled();
    }

    public String getValue() {
        return valueView.getText().toString().trim();
    }

    public void setValue(String value) {
        valueView.setText(value);
    }

    public boolean isOpen() {
        return valueView.isOpen();
    }

    public void setOpen(boolean isOpen) {
        valueView.setOpen(isOpen);
        valueView.setText(isOpen ? "已开启" :"已关闭");
    }

    public void setValueState(boolean isOpen, int strId) {
        setEnable(isOpen);
        setOpen(isOpen);
        valueView.setText(strId);
    }

    public void setValueState(boolean isOpen, String str) {
        setEnable(isOpen);
        setOpen(isOpen);
        valueView.setText(str);
    }

    public void setValueColor(int colorId) {
        valueView.setTextColor(colorId);
    }

    public boolean isHasBottomLine() {
        return hasBottomLine;
    }

    public void setHasBottomLine(boolean hasBottomLine) {
        this.hasBottomLine = hasBottomLine;
        invalidate();
    }

    public boolean isHasTopLine() {
        return hasTopLine;
    }

    public void setHasTopLine(boolean hasTopLine) {
        this.hasTopLine = hasTopLine;
        invalidate();
    }

    public View getTextView() {
        return lableView;
    }

    public void setLable(String lable) {
        lableView.setText(lable);
    }

    public ValueStateTextView getValueView() {
        return valueView;
    }

    public void setLableColor(int color) {
        lableView.setTextColor(color);
    }
}