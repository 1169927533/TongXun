package com.example.a11699.graduatemanager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a11699.graduatemanager.R;


import com.example.a11699.graduatemanager.askhttpDaoImp.dabianDaoImp;
import com.example.a11699.graduatemanager.askhttpDaoImp.qiandaoDaoImp;
import com.example.a11699.graduatemanager.lei.stationInformation;


import java.util.List;

public class qian_itemAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<stationInformation> listt;

    public qian_itemAdapter(Context context, List<stationInformation> list) {
        this.context = context;
        this.listt = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.qianrecycleitem,viewGroup,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        String name="第"+ Integer.valueOf(i+1)+"次签到：位置："+listt.get(i).getCity()+"_"+listt.get(i).getDistrict()+"_经度："+listt.get(i).getLongitude()+" 纬度："+listt.get(i).getLatitude();
        ((MyViewHolder)viewHolder).qian_item_information.setText(name);
    }

    @Override
    public int getItemCount() {
        return listt.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView qian_item_information;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            qian_item_information=itemView.findViewById(R.id.qian_item_information);
        }
    }
}
