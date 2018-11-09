package com.example.a11699.graduatemanager.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.a11699.graduatemanager.ECApplication;
import com.example.a11699.graduatemanager.R;

public class WelcomeActivity extends BaseActivity {
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplication());
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(pref.getString("stu_id",""))){
                    Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    ECApplication.setS_id(pref.getString("stu_id",""));
                     ECApplication.setS_name(pref.getString("stu_pa",""));
                    Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent);
                }

                finish();
            }
        },2000);
    }
}
