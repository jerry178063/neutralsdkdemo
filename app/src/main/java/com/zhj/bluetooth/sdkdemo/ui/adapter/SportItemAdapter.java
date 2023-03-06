package com.zhj.bluetooth.sdkdemo.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseAdapter;
import com.zhj.bluetooth.sdkdemo.base.BaseViewHolder;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.ActivityItem;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthActivity;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class SportItemAdapter extends BaseAdapter<HealthActivity,SportItemAdapter.ViewHolder> {
    public SportItemAdapter(Context mContext, List<HealthActivity> mList) {
        super(mContext, mList);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onNormalBindViewHolder(ViewHolder holder, HealthActivity itemBean, int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.tvTime.setText(mContext.getResources().getString(R.string.sport_info_time)+simpleDateFormat.format(itemBean.getDate()));
        holder.tvDurations.setText(mContext.getResources().getString(R.string.sport_info_duration)+String.format("%02d",itemBean.getDurations()/3600)+":"+String.format("%02d",itemBean.getDurations()%3600/60)+":"+String.format("%02d",itemBean.getDurations()%60));
        //0步行1 跑步2 游泳3 骑行
        //4 室内跑步5 自由训练6 足球7 篮球
        //8 羽毛球9 跳绳10 俯卧撑11 仰卧起坐12 登山13 其他
        holder.tvType.setText(mContext.getResources().getString(R.string.sport_info_type)+itemBean.getType());

        holder.tvCalories.setText(mContext.getResources().getString(R.string.sport_info_cal)+itemBean.getCalories());
        holder.tvSteps.setText(mContext.getResources().getString(R.string.sport_info_steps)+itemBean.getStep());
        holder.tvDistance.setText(mContext.getResources().getString(R.string.sport_info_dis)+itemBean.getDistance());
        holder.tvAvgSpeed.setText(mContext.getResources().getString(R.string.sport_info_speed)+itemBean.getAvgSpeed());
        StringBuffer sb = new StringBuffer();
        //心率原始数据为5秒一组  如果需要以分钟为单位，可以每12组数据取平均值
        List<ActivityItem>  heartItem = itemBean.getHritems();
        if(heartItem != null){
            for (ActivityItem item : heartItem){
                sb.append(item.hr+",");
            }
            holder.tvHeart.setText(mContext.getResources().getString(R.string.sport_info_heart)+sb.toString());
        }else{
            holder.tvHeart.setText(mContext.getResources().getString(R.string.sport_info_no_heart));
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_sport_list,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvDurations)
        TextView tvDurations;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvCalories)
        TextView tvCalories;
        @BindView(R.id.tvDistance)
        TextView tvDistance;
        @BindView(R.id.tvSteps)
        TextView tvSteps;
        @BindView(R.id.tvAvgSpeed)
        TextView tvAvgSpeed;
        @BindView(R.id.tvHeart)
        TextView tvHeart;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
