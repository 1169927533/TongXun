package com.example.a11699.graduatemanager.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.askhttpDaoImp.dabianDaoImp;
import com.example.a11699.graduatemanager.askhttpDaoImp.qiandaoDaoImp;
import com.example.a11699.graduatemanager.lei.dabianInformation;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

public class dabianListActivity extends BaseActivity {
     TextView da_laoshi;//指导老师
     RecyclerView dblistRec;//答辩显示列表
    List<dabianInformation> dabianlist=new ArrayList<>();//答辩列表
    MyAdapter myAdapter;
    dabianDaoImp dabianDaoImpp=new dabianDaoImp(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dabian_list);
        initView();
    }
    void  initView(){
        da_laoshi=findViewById(R.id.da_laoshi);
        dblistRec=findViewById(R.id.dblistRec);
        dabianDaoImpp.getDabainInfor(EMClient.getInstance().getCurrentUser());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        dblistRec.setLayoutManager(linearLayoutManager);
        myAdapter=new MyAdapter(dabianlist,this);
        myAdapter.setOnclickListener(new MyAdapter.OnclickListener() {
            @Override
            public void OnClick(dabianInformation din) {
                Intent intent=new Intent(dabianListActivity.this,item_dabianActivity.class);
                intent.putExtra("lei",din);
                Log.i("zjc",din+"");
                startActivity(intent);
              //  Toast.makeText(dabianListActivity.this,position+"",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void showList(List<dabianInformation> list){
        dabianlist.addAll(list);
        if(list.size()==0){
            da_laoshi.setText("暂无数据");
        }else{
            da_laoshi.setText(list.get(0).gettName());
        }
        dblistRec.setAdapter(myAdapter);
    }
    static class MyAdapter extends RecyclerView.Adapter{
        List<dabianInformation> list;
        Context context;
        View view;
        public MyAdapter(List<dabianInformation> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            view = LayoutInflater.from(context).inflate(R.layout.item_dabian,viewGroup,false);
            MyViewHolder myViewHolder=new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            ((MyViewHolder)viewHolder).da_biao.setText(list.get(i).getTitle());
            if(list.get(i).getReview().equals("1")){
                ((MyViewHolder)viewHolder).da_dumei.setText("以回答");
            }else{
                ((MyViewHolder)viewHolder).da_dumei.setText("未回答");
            }
            ((MyViewHolder)viewHolder).da_lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickListener.OnClick(list.get(i));
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder{
            public TextView da_biao,da_dumei;
            public LinearLayout da_lin;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                da_biao=itemView.findViewById(R.id.da_biao);
                da_dumei=itemView.findViewById(R.id.da_dumei);
                da_lin=itemView.findViewById(R.id.da_lin);
            }
        }
        public interface OnclickListener{
            void OnClick(dabianInformation d);
        }
        OnclickListener onclickListener;
        public void setOnclickListener(OnclickListener onclickListener) {
            this.onclickListener = onclickListener;
        }
    }

}
