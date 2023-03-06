package com.zhj.bluetooth.sdkdemo.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseAdapter;
import com.zhj.bluetooth.sdkdemo.base.BaseViewHolder;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthSleepItem;

import java.util.List;

import butterknife.BindView;

public class SleepsHistoryAdapter extends BaseAdapter<HealthSleepItem, SleepsHistoryAdapter.ViewHolder> {
    public SleepsHistoryAdapter(Context mContext, List<HealthSleepItem> mList) {
        super(mContext, mList);
    }

    @Override
    protected void onNormalBindViewHolder(SleepsHistoryAdapter.ViewHolder holder, HealthSleepItem itemBean, int position) {
        holder.tvDate.setText(mContext.getResources().getString(R.string.heart_history_time)+"\n"+itemBean.getYear()+"-"+itemBean.getMonth()+"-"+itemBean.getDay() + "  "+itemBean.getHour()+":"+itemBean.getMinuter());
        //0x01:开始入睡 0x02:浅睡 0x03:深睡 0x04:清醒 0x05:快速眼动睡眠
        holder.tvSleepState.setText(mContext.getResources().getString(R.string.sleep_history_status)+"\n"+itemBean.getSleepStatus());
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_sleep, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvSleepState)
        TextView tvSleepState;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
