package com.example.a11699.graduatemanager.askhttpDaoImp;

import android.os.Handler;
import android.os.Message;

import com.example.a11699.graduatemanager.activity.qiandaoActivity;
import com.example.a11699.graduatemanager.askHttpDao.qiandaoImp;
import com.example.a11699.graduatemanager.internet.Net;
import com.example.a11699.graduatemanager.lei.stationInformation;
import com.example.a11699.graduatemanager.lei.zhoujiinformation;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class qiandaoDaoImp implements qiandaoImp {
    private qiandaoActivity qiandaoActivityy;
    List<stationInformation> list;
    public qiandaoDaoImp(qiandaoActivity qiandaoActivityy) {
        this.qiandaoActivityy = qiandaoActivityy;
    }
Handler handler=new Handler() {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg.what==0x33){
          // // if(list.get(list.size()-1).getQiandao().equals("1")){
                //qiandaoActivity.nodiandao="1";
           // }
            qiandaoActivityy.show(list);
        }
    }
};
    @Override
    public void qian(String shi, String qu, String wei,String jin) {
        String url= Net.qianDao+"?s_sid="+ EMClient.getInstance().getCurrentUser()+"&s_latitude="+wei+"&s_longitude="+jin+"&s_city="+shi+"&s_district="+qu;
        OkHttpClient okHttpClient=new OkHttpClient();
        final Request request=new Request.Builder().url(url).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 String b=response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(b);
                    JSONArray jsonArray = jsonObject.getJSONArray("qiandaoList");
                    Gson gson = new Gson();
                     list=new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        stationInformation newInformation = gson.fromJson(String.valueOf(jsonArray.get(i)), stationInformation.class);
                            list.add(newInformation);
                    }
                    handler.sendEmptyMessage(0x33);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
