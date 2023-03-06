package com.zhj.bluetooth.sdkdemo.ui;

import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;

import butterknife.BindView;

public class TempDayDataActivity extends BaseActivity {
    @BindView(R.id.tvTemp)
    TextView tvTemp;

    @Override
    protected int getContentView() {
        return R.layout.activity_current_temp;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.main_current_temp));
        //体温单位为0.01度   例显示3600代表36度
    }
}
