package com.example.shuangxiang.qdkelenqdforphone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shuangxiang.qdkelenqdforphone.R;
import com.example.shuangxiang.qdkelenqdforphone.bean.AlarmInfo;

import java.text.SimpleDateFormat;

import java.util.List;

/**
 * Created by shuang.xiang on 2016/8/18.
 */
public class MyAlarmRecyclerViewAdapter extends RecyclerView.Adapter {
    private static final String TAG = "MyAlarmRecyclerViewAdapter";
    private List list;
    private Context context;

    public MyAlarmRecyclerViewAdapter(List<AlarmInfo.ListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.alarm_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        AlarmInfo.ListBean listBean = (AlarmInfo.ListBean) list.get(position);
        String createDate = listBean.getCreateDate();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Long time = new Long(createDate);
//        String d = format.format(time);
        //报警事件




        //当前值

        //界限值

        //描述
        String displayName = listBean.getDisplayName();

        holder.describe.setText(displayName);
        holder.time.setText(createDate);


    }

    @Override
    public int getItemCount() {
        return list.size() > 0 ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView cause;
        TextView now;
        TextView limit;
        TextView describe;

        public ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.tv_alarm_item_time);
            cause = (TextView) itemView.findViewById(R.id.tv_alarm_item_cause);
            now = (TextView) itemView.findViewById(R.id.tv_alarm_item_now);
            limit = (TextView) itemView.findViewById(R.id.tv_alarm_item_limit);
            describe = (TextView) itemView.findViewById(R.id.tv_alarm_item_describe);
        }
    }

    public void setData(List list) {
        this.list = list;
        notifyDataSetChanged();
    }
    //添加数据
    public void addItem(List<AlarmInfo.ListBean> newDatas) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        newDatas.addAll(list);
        list.removeAll(list);
        list.addAll(newDatas);
        notifyDataSetChanged();
    }
    public void addMoreItem(List<AlarmInfo.ListBean> newDatas) {
        list.addAll(newDatas);
        notifyDataSetChanged();
    }

}
