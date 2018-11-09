package com.example.a11699.graduatemanager.askhttpDaoImp;

import android.os.Handler;
import android.os.Message;

import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.activity.writezhouji;
import com.example.a11699.graduatemanager.lei.alldatime;
import com.example.a11699.graduatemanager.lei.zhoujiinformation;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class nadaoriqiDaoImp {

    writezhouji writezhouji;
List<alldatime>list=new ArrayList<>();
    public nadaoriqiDaoImp(com.example.a11699.graduatemanager.activity.writezhouji writezhouji) {
        this.writezhouji = writezhouji;
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x27){
                writezhouji.showll(list);
            }
        }
    };
   public void getAllDate(){
        String url="http://i3c8x94lqb.51http.tech/TXGLwai/chartList?s_sid="+ EMClient.getInstance().getCurrentUser()+"&page=0"+"&count="+10000;
       OkHttpClient okHttpClient=new OkHttpClient();
       Request request=new Request.Builder().url(url).build();
       Call call=okHttpClient.newCall(request);
       call.enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {

           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
                String sd=response.body().string();
               try {
                   JSONObject jsonObject = new JSONObject(sd);
                   JSONArray jsonArray = jsonObject.getJSONArray("chartList");
                   Gson gson = new Gson();
                   list = new ArrayList<>();
                   for (int i = 0; i < jsonArray.length(); i++) {
                       alldatime newInformation = gson.fromJson(String.valueOf(jsonArray.get(i)), alldatime.class);
                       list.add(newInformation);
                   }
                   Message message = new Message();
                   message.obj = list;
                   message.what = 0x27;
                   handler.sendMessage(message);
               }catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       });
    }



}
