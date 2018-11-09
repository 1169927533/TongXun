package com.example.a11699.graduatemanager.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.lei.tuisongInformation;

import cn.jpush.android.api.JPushInterface;

public class tongzhiActivity extends BaseActivity {
    String title = "";
    String content = "";
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tongggg);
        textView=findViewById(R.id.te);
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
              title = null;
              content = null;
            if(bundle!=null){
                title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                content = bundle.getString(JPushInterface.EXTRA_ALERT);
            }
            if(content==null){
                content="";
            }
            tuisongInformation.list.add(content);
            textView.setText(content);
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("通知信息").setMessage("内容："+content).setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

            @Override
            public void onClick(DialogInterface dialog,
                                int which) {

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

            @Override
            public void onClick(DialogInterface dialog,
                                int which) {

            }
        });
        builder.create().show();
    }

}
