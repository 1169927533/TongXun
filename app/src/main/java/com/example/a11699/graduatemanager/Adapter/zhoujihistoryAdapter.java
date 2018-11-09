package com.example.a11699.graduatemanager.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.lei.studentInfromation;

import java.util.List;

public class zhoujihistoryAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<studentInfromation>list;

    public zhoujihistoryAdapter(Context context, List<studentInfromation> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.zhou_history_item,viewGroup,false);
        MyviewHolder holder=new MyviewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ((MyviewHolder)viewHolder).textView.setText(list.get(i).getName());
        ((MyviewHolder)viewHolder).textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickListener.OnClick(Integer.valueOf(list.get(i).getCid().toString()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private class MyviewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.hh);
        }

    }
    public interface OnclickListener{
        void OnClick(int position);
    }
    OnclickListener onclickListener;

    public void setOnclickListener(OnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }
}
