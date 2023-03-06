package com.zhj.bluetooth.sdkdemo.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseAdapter;
import com.zhj.bluetooth.sdkdemo.base.BaseViewHolder;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.TempInfo;

import java.util.List;

import butterknife.BindView;

public class TempHistoryAdapter extends BaseAdapter<TempInfo, TempHistoryAdapter.ViewHolder> {
    public TempHistoryAdapter(Context mContext, List<TempInfo> mList) {
        super(mContext, mList);
    }

    @Override
    protected void onNormalBindViewHolder(TempHistoryAdapter.ViewHolder holder, TempInfo itemBean, int position) {
        holder.tvDate.setText(mContext.getResources().getString(R.string.heart_history_time)+"\n"+itemBean.getYear()+"-"+itemBean.getMonth()+"-"+itemBean.getDay() + "  "+itemBean.getHour()+":"+itemBean.getMinute());
        holder.tvTemp.setText(mContext.getResources().getString(R.string.temp_current)+"\n"+itemBean.getTmpHandler());
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_temp, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvTemp)
        TextView tvTemp;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
