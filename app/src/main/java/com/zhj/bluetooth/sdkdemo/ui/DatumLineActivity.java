package com.zhj.bluetooth.sdkdemo.ui;

import android.view.View;
import android.widget.EditText;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.DatumLine;

import butterknife.BindView;

public class DatumLineActivity extends BaseActivity {

    @BindView(R.id.etVauleHeart)
    EditText etVauleHeart;
    @BindView(R.id.etVauleFz)
    EditText etVauleFz;
    @BindView(R.id.etVauleSs)
    EditText etVauleSs;
    @BindView(R.id.etVauleOxygen)
    EditText etVauleOxygen;

    @Override
    protected int getContentView() {
        return R.layout.activity_datumline;
    }


    @Override
    protected void initView() {
        super.initView();
        titleName.setText("心率基准线");
        rightText.setText("保存");
        rightText.setOnClickListener(view -> {
            DatumLine datumLine = new DatumLine();
            datumLine.setHeart(Integer.parseInt(etVauleHeart.getText().toString()));
            datumLine.setFz(Integer.parseInt(etVauleFz.getText().toString()));
            datumLine.setSs(Integer.parseInt(etVauleSs.getText().toString()));
            datumLine.setOxygen(Integer.parseInt(etVauleOxygen.getText().toString()));

        });
    }
}
