package com.zhj.bluetooth.sdkdemo.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseAdapter;
import com.zhj.bluetooth.sdkdemo.base.BaseViewHolder;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthHeartRateItem;

import java.util.List;

import butterknife.BindView;

public class RateHistoryAdapter extends BaseAdapter<HealthHeartRateItem, RateHistoryAdapter.ViewHolder> {
    public RateHistoryAdapter(Context mContext, List<HealthHeartRateItem> mList) {
        super(mContext, mList);
    }

    @Override
    protected void onNormalBindViewHolder(RateHistoryAdapter.ViewHolder holder, HealthHeartRateItem itemBean, int position) {
        holder.tvDate.setText(mContext.getResources().getString(R.string.heart_history_time)+"\n"+itemBean.getYear()+"-"+itemBean.getMonth()+"-"+itemBean.getDay() + "  "+itemBean.getHour()+":"+itemBean.getMinuter());
        holder.tvRate.setText(mContext.getResources().getString(R.string.heart_history_rate)+"\n"+itemBean.getHeartRaveValue());
        holder.tvFz.setText(mContext.getResources().getString(R.string.heart_history_diastolic_pressure)+itemBean.getFz());
        holder.tvSs.setText(mContext.getResources().getString(R.string.heart_history_systolic_pressure)+itemBean.getSs());
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_rate, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvRate)
        TextView tvRate;
        @BindView(R.id.tvFz)
        TextView tvFz;
        @BindView(R.id.tvSs)
        TextView tvSs;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
