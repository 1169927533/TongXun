package com.example.a11699.graduatemanager.askhttpDaoImp;

import android.os.Message;
import android.util.Log;

import com.example.a11699.graduatemanager.activity.adduserActivity;
import com.example.a11699.graduatemanager.askHttpDao.addStudnet;
import com.example.a11699.graduatemanager.internet.Net;
import com.example.a11699.graduatemanager.lei.dabianInformation;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import android.os.Handler;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class addStudentDaoImp implements addStudnet {
    private adduserActivity adduserActivity;

    public addStudentDaoImp(com.example.a11699.graduatemanager.activity.adduserActivity adduserActivity) {
        this.adduserActivity = adduserActivity;
    }
Handler handler=new Handler() {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg.what==0x34){
            Toast.makeText(adduserActivity,"网络请求失败，请检查网络",Toast.LENGTH_SHORT).show();
        }else if(msg.what==0x35){
            Toast.makeText(adduserActivity,"注册成功",Toast.LENGTH_SHORT).show();
            adduserActivity.show();
        }else {
            Toast.makeText(adduserActivity,"注册失败",Toast.LENGTH_SHORT).show();
        }
    }
};
    @Override
    public void addStudent(String xuehao, String workplace, String gongsi, String zhuwu,String baseEncode) {
       String url= Net.updateStudentInfo+"?s_sid="+xuehao+"&s_city="+workplace+"&s_company="+gongsi+"&s_job="+zhuwu+"&image="+baseEncode;
       Log.i("zjc","修改学生的地址"+url);
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zjc","学生数据网络访问失败");
                handler.sendEmptyMessage(0x34);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody=response.body().string();
                try {
                    JSONObject jsonObject=new JSONObject(responseBody);
                    if(jsonObject.getString("result").equals("1")){
                        handler.sendEmptyMessage(0x35);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
