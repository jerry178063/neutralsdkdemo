package com.zhj.bluetooth.sdkdemo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.util.CustomToggleButton;

/**
 */
public class ItemToggleLayout extends ItemLableValue {

    private TextView lableView;

    private CustomToggleButton toggleBtn;

    private ProgressBar progressBar;

    private OnToggleListener onToggleListener;

    private boolean oldStatus;
    public ImageView ivLeft;
    public ItemToggleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_item_toggle, this, true);
        lableView = (TextView) findViewById(R.id.lable);
        toggleBtn = (CustomToggleButton) findViewById(R.id.toggle);
        progressBar = (ProgressBar) findViewById(R.id.progress_circle);
        ivLeft = (ImageView) findViewById(R.id.left_drawable);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemLableValue);
        String lable = a.getString(R.styleable.ItemLableValue_lable_text);
        setHasBottomLine(a.getBoolean(R.styleable.ItemLableValue_has_bottom_line, true));
        if (isHasBottomLine()) {
            bottomLineColor = a.getColor(R.styleable.ItemLableValue_bottom_line_color, getResources().getColor(R.color.driver_color));
            initDraw();
        }
        Drawable leftDrawable = a.getDrawable(R.styleable.ItemLableValue_left_drawable);
        a.recycle();
        ivLeft.setImageDrawable(leftDrawable);
        lableView.setText(lable);
        toggleBtn.setOnSwitchListener(isSwitchOn -> {
            if (onToggleListener != null) {
                onToggleListener.onToggle(ItemToggleLayout.this,isSwitchOn);
            }
        });
        progressBar.setVisibility(View.GONE);
    }

    public void setImageBack(int resId){
        toggleBtn.setImageResource(resId, R.mipmap.toggle_off, R.mipmap.toggle_thumb);
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void cancelProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public boolean isOpen() {
        return toggleBtn.getSwitchState();
    }

    /**
     * 初始化开始值
     * @param isOpen
     */
    public void initStauts(boolean isOpen){
        oldStatus = isOpen;
        toggleBtn.setSwitchState(isOpen);
    }

    /**
     * 是否改变状态了
     * @return
     */
    public boolean isChange(){
        if(oldStatus != toggleBtn.getSwitchState()){
            return true;
        }
        return false;
    }

    public void setOpen(boolean isOpen) {
        toggleBtn.setSwitchState(isOpen);
    }
    public void setToggleButtonCallback(CustomToggleButton.Callback callback){
        toggleBtn.setCallback(callback);
    }
    public void setOnToggleListener(OnToggleListener listener) {
        this.onToggleListener = listener;
    }

    public void setLableText(String text) {
        lableView.setText(text);
    }
    public interface OnToggleListener {
        void onToggle(ItemToggleLayout layout, boolean isSwitchOn);
    }

}