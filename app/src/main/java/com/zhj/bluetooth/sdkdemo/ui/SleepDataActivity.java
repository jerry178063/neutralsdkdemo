package com.zhj.bluetooth.sdkdemo.ui;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.ui.adapter.SleepsHistoryAdapter;
import com.zhj.bluetooth.sdkdemo.ui.adapter.StepsHistoryAdapter;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthSleepItem;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthSportItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SleepDataActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_sleep;
    }

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.step_and_sleep_history_title));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        syncStepHistory(true);
    }
    List<HealthSportItem> sportItemsAll = new ArrayList<>();//历史步数数据
    List<HealthSleepItem> sleepItemsAll = new ArrayList<>();//历史睡眠
    Calendar calendar;
    StepsHistoryAdapter stepsHistoryAdapter;
    SleepsHistoryAdapter sleepsHistoryAdapter;

    //历史睡眠数据 (正常计算卡路里数据)
    private void syncStepHistory(boolean isFirst){
        if (isFirst) {
            calendar = Calendar.getInstance();
        }
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH)+1;
        final int day=calendar.get(Calendar.DATE);
    }

    @OnClick(R.id.tvShowSteps)
    void showHistorySteps(){
        if(stepsHistoryAdapter != null){
            mRecyclerView.setAdapter(stepsHistoryAdapter);
        }else{
            showToast(getResources().getString(R.string.step_history_no_data));
        }
    }

    @OnClick(R.id.tvShowSleeps)
    void showHistorySleeps(){
        if(sleepsHistoryAdapter != null){
            mRecyclerView.setAdapter(sleepsHistoryAdapter);
        }else{
            showToast(getResources().getString(R.string.sleep_history_no_data));
        }
    }

}
