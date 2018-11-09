package com.example.a11699.graduatemanager.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.a11699.graduatemanager.Adapter.chatAdapter;
import com.example.a11699.graduatemanager.Adapter.historyAdapter;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.lei.chatinformation;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

public class allHistory extends BaseActivity implements View.OnClickListener{
    private ImageView history_back, history_laji;
    private RecyclerView history_recycle;

    private List<EMMessage> lists;
    private String talkname;
    //适配器
    historyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_history);
        initview();
    }
    void initview(){
        history_back=findViewById(R.id.history_back);history_back.setOnClickListener(this);
        history_laji=findViewById(R.id.history_laji);history_laji.setOnClickListener(this);
        history_recycle=findViewById(R.id.history_recycle);
        talkname= (String) getIntent().getStringExtra("sa");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        history_recycle.setLayoutManager(linearLayoutManager);
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(talkname);
        lists = conversation.getAllMessages();
        adapter = new historyAdapter(lists, this, talkname);
        Log.i("zjc","运行下来了");
        history_recycle.setAdapter(adapter);
     }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.history_back: finish(); break;
            case R.id.history_laji: break;
        }
    }
}
