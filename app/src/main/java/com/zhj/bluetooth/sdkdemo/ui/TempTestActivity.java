package com.zhj.bluetooth.sdkdemo.ui;

import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.util.CustomToggleButton;

import butterknife.BindView;

public class TempTestActivity extends BaseActivity {

    @BindView(R.id.mSwitch)
    CustomToggleButton mSwitch;
    @BindView(R.id.tvTag)
    TextView tvTag;
    @Override
    protected int getContentView() {
        return R.layout.activity_heart_test;
    }

    @Override
    protected void initView() {
        super.initView();
        tvTag.setText(getResources().getString(R.string.temp_test_swith));
        titleName.setText(getResources().getString(R.string.main_set_temp_test));
        //获取温度检测开关
        //设置心率开关
    }
}
