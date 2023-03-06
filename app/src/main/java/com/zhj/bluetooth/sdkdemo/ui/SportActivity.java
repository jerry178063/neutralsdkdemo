package com.zhj.bluetooth.sdkdemo.ui;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class SportActivity extends BaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @Override
    protected int getContentView() {
        return R.layout.activity_sport;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.sport_info_title));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        synchActiviy();
    }
    //历史活动数据
    private void synchActiviy(){
    }
}
