package com.example.a11699.graduatemanager.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.askHttpDao.tongzhiDao;
import com.example.a11699.graduatemanager.askhttpDaoImp.tongzhiDaoImp;
import com.example.a11699.graduatemanager.lei.cunsuoyoudexiaoxi;
import com.example.a11699.graduatemanager.lei.tuisongInformation;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

public class messageActivity extends AppCompatActivity {
    RecyclerView talk_recycleview;
    tongzhiDao tongzhi=new tongzhiDaoImp(this);
    String content;
    String title;

    private String itemW;//你选择的选项
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initview( );
    }
    void initview( ){
        Intent integer=getIntent();
        itemW=integer.getStringExtra("typelei");
        talk_recycleview=findViewById(R.id.talk_recycleviewi);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        talk_recycleview.setLayoutManager(linearLayoutManager);
        tongzhi.findALlmessage(EMClient.getInstance().getCurrentUser(),"0","100",itemW);
    }
    public void showList( List<tuisongInformation> list){
       if(itemW.equals("1") ){
           myAdapter m=new myAdapter(this, cunsuoyoudexiaoxi.schoollist);
           talk_recycleview.setAdapter(m);
        }else if(itemW.equals("3")){
           myAdapter m=new myAdapter(this, cunsuoyoudexiaoxi.teacherlist);
           talk_recycleview.setAdapter(m);
       }else if(itemW.equals("4")){
           myAdapter m=new myAdapter(this, cunsuoyoudexiaoxi.pingjiazhouji);
           talk_recycleview.setAdapter(m);
       }else if(itemW.equals("5")){
           myAdapter m=new myAdapter(this, cunsuoyoudexiaoxi.dabainList);
           talk_recycleview.setAdapter(m);
       }

    }
    class myAdapter extends RecyclerView.Adapter{
        private Context context;
        private  List<tuisongInformation>list;
        private View view;
        public myAdapter(Context context,  List<tuisongInformation>list) {
            this.context = context;
            this.list = list;
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            view = LayoutInflater.from(context).inflate(R.layout.zhou_history_item,viewGroup,false);
            MyViewHolder myViewHolder=new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                ((MyViewHolder) viewHolder).textView.setText(list.get(i).getDetail());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textView=itemView.findViewById(R.id.hh);
            }
        }
    }
}
