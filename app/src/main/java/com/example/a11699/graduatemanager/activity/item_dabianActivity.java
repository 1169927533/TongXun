package com.example.a11699.graduatemanager.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.askHttpDao.SavaEdittevtListener;
import com.example.a11699.graduatemanager.askHttpDao.dabianDao;
import com.example.a11699.graduatemanager.askhttpDaoImp.dabianDaoImp;
import com.example.a11699.graduatemanager.lei.dabianInformation;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

public class item_dabianActivity extends AppCompatActivity implements SavaEdittevtListener {
private TextView da_wquxiaoa,da_wenti;
private Button dabian_fa;
String neirong="";//所有回答的内容
dabianDao dabianDaeo=new dabianDaoImp(this);
private List<String> listt=new ArrayList<>();//保存题目数据
String name,iddd;//答辩标题
    itdabianAdapter  adapter;
    RecyclerView item_dabianrecy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_dabian);
        Intent intent=getIntent();
        dabianInformation a=(dabianInformation)intent.getSerializableExtra("lei");
        String timu=a.getQuession();
        String[] ss = timu.split("#");
        listt=new ArrayList<>();
        for(int i=0;i<ss.length;i++){
            listt.add(ss[i]);
        }
        name=a.getTitle();
        iddd=a.getRid();
        init();
    }
    void init(){
        item_dabianrecy=findViewById(R.id.item_dabianrecy);
        dabian_fa=findViewById(R.id.dabian_fa);
        da_wquxiaoa=findViewById(R.id.da_wquxiaoa);
        da_wenti=findViewById(R.id.da_wenti);
        da_wenti.setText(name);
        da_wquxiaoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dabian_fa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zuizhong=zuizhong+neirong;
                Log.i("zjc",zuizhong);
                dabianDaeo.putDabain(EMClient.getInstance().getCurrentUser(),zuizhong,iddd);
                neirong="";zuizhong="";
            }
        });
        adapter= new itdabianAdapter(this,listt);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        item_dabianrecy.setLayoutManager(linearLayoutManager);
        item_dabianrecy.setAdapter(adapter);

    }
    public void show(){
        Toast.makeText(item_dabianActivity.this,"发表成功",Toast.LENGTH_SHORT).show();
    }
      int sp=0;
    String zuizhong="";
    @Override
    public void SaveEdit(int position, String string) {
        if(sp==position){
            neirong=string;
        }else{
            zuizhong+=neirong+"#";
            sp=position;
        }
    }
    public static class itdabianAdapter extends RecyclerView.Adapter{
        private Context context;
        private List<String> list;
        private View view;
        public itdabianAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }
         class TextSwitcher  implements TextWatcher{
            private MyviewHolderr mHolder;

            public TextSwitcher (MyviewHolderr mHolder) {
                this.mHolder = mHolder;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                             SavaEdittevtListener listener= (SavaEdittevtListener) context;
                             if(s!=null){
                                 listener.SaveEdit(Integer.parseInt(mHolder.item_question.getTag().toString()),s.toString());
                             }
              /*  //用户输入完毕后，处理输入数据，回调给主界面处理
                SavaEdittevtListener listener= (SavaEdittevtListener) context;
                if(s!=null){
                    listener.SaveEdit(Integer.parseInt(mHolder.item_question.getTag().toString()),s.toString());
                }
*/
            }
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            view= LayoutInflater.from(context).inflate(R.layout.item_recy_lie,viewGroup,false);
            MyviewHolderr myViewHolder=new MyviewHolderr(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ((MyviewHolderr)viewHolder).item_timu.setText(list.get(i));
            //添加editText的监听事件
            ((MyviewHolderr)viewHolder).item_question.addTextChangedListener(new TextSwitcher(((MyviewHolderr)viewHolder)));
            //通过设置tag，防止position紊乱
            ((MyviewHolderr)viewHolder).item_question.setTag(i);
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
      public   class MyviewHolderr extends RecyclerView.ViewHolder{
              TextView   item_timu;
              EditText   item_question;
            public MyviewHolderr(@NonNull View itemView) {
                super(itemView);
                item_timu=itemView.findViewById(R.id.item_timu);
                item_question=itemView.findViewById(R.id.item_question);
            }
        }
    }
}
