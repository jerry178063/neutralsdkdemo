package com.zhj.bluetooth.sdkdemo.ui;

import android.widget.EditText;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.views.ItemToggleLayout;

import butterknife.BindView;

public class TempWaringActivity extends BaseActivity {
    @BindView(R.id.itTempWarn)
    ItemToggleLayout itTempWarn;
    @BindView(R.id.etVaule)
    EditText etVaule;

    @Override
    protected int getContentView() {
        return R.layout.activity_temp_waring;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.main_temp_waring));
        //获取体温报警开关
        rightText.setText(getResources().getString(R.string.temp_warm_set));
    }
}
