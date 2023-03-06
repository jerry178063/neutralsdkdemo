package com.zhj.bluetooth.sdkdemo.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.util.PermissionUtil;
import com.zhj.bluetooth.sdkdemo.util.ScreenUtil;
import com.zhj.bluetooth.sdkdemo.util.StatusBarUtil;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.BluetoothLe;
import com.zhj.bluetooth.zhjbluetoothsdk.util.ToastUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;

/**
 * 所有Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity implements PermissionUtil.RequsetResult{
    /**
     * 日志输出标志
     */
    protected final String TAG = this.getClass().getSimpleName();
    protected FrameLayout mFlContent; //中间的主界面
    protected View mRoodView;
    protected ImageView titleBack; //返回键
    protected TextView titleName; //标题
    protected RelativeLayout titleBg; //标题背景颜色
    protected TextView rightText; //右侧文字
    protected RelativeLayout bar_bg;
    protected RelativeLayout layoutTitle;
    protected ImageView rightImg;
    protected View bgView;
    private Bundle bundle;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
        }
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
        initActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("WrongConstant")
    private boolean fixOrientation(){
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo)field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }

    private boolean isTranslucentOrFloating(){
        boolean isTranslucentOrFloating = false;
        try {
            int [] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean)m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    public void initActivity(){
        setContentView(R.layout.layout_base_title);
        titleBack = (ImageView) findViewById(R.id.base_title_back);
        titleName = (TextView) findViewById(R.id.base_title_name);
        rightText = (TextView) findViewById(R.id.base_right_text);
        titleBg = (RelativeLayout) findViewById(R.id.base_title_bg);
        mFlContent = (FrameLayout) findViewById(R.id.base_content);
        layoutTitle = (RelativeLayout) findViewById(R.id.layoutTitle);
        bgView = findViewById(R.id.bgView);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.this.onBackPressed();
            }
        });
        bar_bg = (RelativeLayout) findViewById(R.id.bar_bg);
        rightImg = (ImageView) findViewById(R.id.rightImg);
        onViewCreate();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bar_bg.getLayoutParams();
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        params.height = ScreenUtil.getStatusHeight(this);
        bar_bg.setLayoutParams(params);
        StatusBarUtil.setTranslucentForImageView(this, 0,titleBg);
        initView();
        permissionUtil=new PermissionUtil();
        permissionUtil.setRequsetResult(this);
        layoutTitle.setBackgroundColor(getResources().getColor(R.color.white));
        titleBack.setImageResource(R.mipmap.back_black);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
    protected void onViewCreate() {
        mRoodView = LayoutInflater.from(this).inflate(getContentView(), null);
        ButterKnife.bind(this, mRoodView);
        mFlContent.addView(mRoodView);
    }
    /**
     * 得到Activity布局ID
     *
     * @return
     */
    protected abstract int getContentView();

    /**
     * 初始化控件
     */
    protected void initView() {

    }

    protected void showToast(final String content) {
        runOnUiThread(() -> ToastUtil.showToast(this,content));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothLe.getDefault().cancelAllTag();
    }

    private PermissionUtil permissionUtil;
    /**
     * 检测权限，如果返回true,有权限 false 无权限
     * @param permissions 权限
     * @return 是否有权限
     */
    public boolean checkSelfPermission(String... permissions){
        return PermissionUtil.checkSelfPermission(permissions);
    }
    public void requestPermissions(int requestCode,String... permissions){
        PermissionUtil.requestPermissions(this,requestCode,permissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtil.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
    public void requestPermissionsSuccess(int requestCode){

    }
    public void requestPermissionsFail(int requestCode){

    }
}
