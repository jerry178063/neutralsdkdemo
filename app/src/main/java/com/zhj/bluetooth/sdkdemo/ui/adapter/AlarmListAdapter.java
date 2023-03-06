package com.zhj.bluetooth.sdkdemo.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseAdapter;
import com.zhj.bluetooth.sdkdemo.base.BaseViewHolder;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.Alarm;

import java.util.List;

import butterknife.BindView;


/**
 * Created by Administrator on 2019/7/11.
 */

public class AlarmListAdapter extends BaseAdapter<Alarm,AlarmListAdapter.ViewHolder> {

    public AlarmListAdapter(Context mContext, List<Alarm> mList) {
        super(mContext, mList);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onNormalBindViewHolder(AlarmListAdapter.ViewHolder holder, Alarm itemBean, int position) {
        holder.tvOpen.setText(mContext.getResources().getString(R.string.alarm_item_swith)+itemBean.getOn_off());
        //闹钟周期从星期一开始  每一个Boolean值对应当天是否选中
        StringBuilder sb = new StringBuilder();
        for (boolean b : itemBean.getWeekRepeat()){
            sb.append(b);
            sb.append(",");
        }
        holder.tvCycle.setText(mContext.getResources().getString(R.string.alarm_item_cycle)+sb.toString());

        holder.tvTime.setText(mContext.getResources().getString(R.string.alarm_item_time)+itemBean.getAlarmHour()+":"+ itemBean.getAlarmMinute());
        //0x00:其他 0x01:喝水 0x02:吃药 0x03:吃饭 0x04:运动 0x05:睡觉 0x06:起床 0x07:约会 0x08:聚会 0x09:会议
        holder.tvType.setText(mContext.getResources().getString(R.string.alarm_item_type)+itemBean.getAlarmType());
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_alarm_list,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvOpen)
        TextView tvOpen;
        @BindView(R.id.tvCycle)
        TextView tvCycle;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvType)
        TextView tvType;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
