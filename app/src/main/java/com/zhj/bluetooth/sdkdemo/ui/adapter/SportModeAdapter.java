package com.zhj.bluetooth.sdkdemo.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseAdapter;
import com.zhj.bluetooth.sdkdemo.base.BaseViewHolder;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.SportModeBean;

import java.util.List;

import butterknife.BindView;

public class SportModeAdapter extends BaseAdapter<SportModeBean,SportModeAdapter.ViewHolder> {
    public SportModeAdapter(Context mContext, List<SportModeBean> mList) {
        super(mContext, mList);
    }

    @Override
    protected void onNormalBindViewHolder(SportModeAdapter.ViewHolder holder, SportModeBean itemBean, int position) {
        holder.sportModeName.setText(itemBean.getSportName());
        if(itemBean.isSportStaus()){
            holder.sportModeStatus.setImageResource(R.mipmap.sport_delete);
        }else{
            holder.sportModeStatus.setImageResource(R.mipmap.sport_add);
        }
        holder.itemView.setOnClickListener(v -> mClickListener.onStatusClick(holder.sportModeStatus,position,itemBean.isSportStaus()));
        holder.sportModeStatus.setOnClickListener(v -> mClickListener.onStatusClick(holder.sportModeStatus,position,itemBean.isSportStaus()));
        if(position == mList.size() - 1){
            holder.viewLine.setVisibility(View.GONE);
        }else{
            holder.viewLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_sport_mode,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.sportModeName)
        TextView sportModeName;
        @BindView(R.id.sportModeStatus)
        ImageView sportModeStatus;
        @BindView(R.id.viewLine)
        View viewLine;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private OnStatusClickListener mClickListener;
    public interface OnStatusClickListener {
        void onStatusClick(View view, int position, boolean status);
    }

    public OnStatusClickListener getmClickListener() {
        return mClickListener;
    }

    public void setmClickListener(OnStatusClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }
}
