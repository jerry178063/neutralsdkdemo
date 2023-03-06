package com.zhj.bluetooth.sdkdemo.ui;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.ui.adapter.RateHistoryAdapter;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthHeartRateItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

public class RateHistoryActivity extends BaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @Override
    protected int getContentView() {
        return R.layout.activity_rate_history;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.heart_history_title));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        syncHeartHistory(true);
    }
    List<HealthHeartRateItem> healthHeartRateItemsAll = new ArrayList<>();
    Calendar calendar;
    //历史心率数据
    private void syncHeartHistory(boolean isFirst){
        if (isFirst) {
            calendar = Calendar.getInstance();
        }
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH)+1;
        final int day=calendar.get(Calendar.DATE);
    }
}
