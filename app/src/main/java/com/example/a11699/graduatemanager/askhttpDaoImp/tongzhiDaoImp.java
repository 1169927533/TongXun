package com.example.a11699.graduatemanager.askhttpDaoImp;


import android.os.Message;
import android.util.Log;

import com.example.a11699.graduatemanager.activity.TextActivity;
import com.example.a11699.graduatemanager.activity.messageActivity;
import com.example.a11699.graduatemanager.askHttpDao.tongzhiDao;
import com.example.a11699.graduatemanager.internet.Net;
import com.example.a11699.graduatemanager.lei.cunsuoyoudexiaoxi;
import com.example.a11699.graduatemanager.lei.tuisongInformation;
import com.example.a11699.graduatemanager.lei.zhoujiinformation;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class tongzhiDaoImp implements tongzhiDao {
    TextActivity textActivity;
    List<tuisongInformation> list=new ArrayList<>();
    public tongzhiDaoImp(TextActivity textActivity) {
        this.textActivity = textActivity;
    }
    messageActivity m;

    public tongzhiDaoImp(messageActivity m) {
        this.m = m;
    }

    Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg.what==0x32){
            m.showList(list);
        }else{
            m.showList(list);
        }
    }
};
    @Override
    public void findALlmessage(String id, String page, String count, final String itemm) {
        if(cunsuoyoudexiaoxi.s==1){
            handler.sendEmptyMessage(0x31);
        }else{
            cunsuoyoudexiaoxi.schoollist=new ArrayList<>();
            cunsuoyoudexiaoxi.teacherlist=new ArrayList<>();
            cunsuoyoudexiaoxi.pingjiazhouji=new ArrayList<>();
            cunsuoyoudexiaoxi.dabainList=new ArrayList<>();
         String url= Net.jPush+"?s_sid="+id+"&page="+page+"&count="+count;
         Log.i("zjc","请求通知+"+url);
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cunsuoyoudexiaoxi.s=0;
                  Log.i("zjc","网络访问失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                cunsuoyoudexiaoxi.s=1;
                    String aa=response.body().string();
                try {
                    JSONObject jsonObject=new JSONObject(aa);
                    JSONArray jsonArray=jsonObject.getJSONArray("jpushList");
                    list=new ArrayList<>();
                    Gson gson=new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1=(JSONObject) jsonArray.get(i);
                        if(jsonObject1.getString("type").equals("1")){
                            tuisongInformation newInformation = gson.fromJson(String.valueOf(jsonArray.get(i)), tuisongInformation.class);
                            cunsuoyoudexiaoxi.schoollist.add(newInformation);
                        }
                        if(jsonObject1.getString("type").equals("3")){
                            tuisongInformation newInformation = gson.fromJson(String.valueOf(jsonArray.get(i)), tuisongInformation.class);
                            cunsuoyoudexiaoxi.teacherlist.add(newInformation);
                        }
                        if(jsonObject1.getString("type").equals("4")){
                            tuisongInformation newInformation = gson.fromJson(String.valueOf(jsonArray.get(i)), tuisongInformation.class);
                            cunsuoyoudexiaoxi.pingjiazhouji.add(newInformation);
                        }
                        if(jsonObject1.getString("type").equals("5")){
                            tuisongInformation newInformation = gson.fromJson(String.valueOf(jsonArray.get(i)), tuisongInformation.class);
                            cunsuoyoudexiaoxi.dabainList.add(newInformation);
                        }
                        if(jsonObject1.getString("type").equals("100")){
                            tuisongInformation newInformation = gson.fromJson(String.valueOf(jsonArray.get(i)), tuisongInformation.class);
                            list.add(newInformation);
                        }
                        /*
                        if(itemm.equals(jsonObject1.getString("type"))){
                            tuisongInformation newInformation = gson.fromJson(String.valueOf(jsonArray.get(i)), tuisongInformation.class);
                            list.add(newInformation);
                        }else if(jsonObject1.getString("type").equals("100")){
                            tuisongInformation newInformation = gson.fromJson(String.valueOf(jsonArray.get(i)), tuisongInformation.class);
                            list.add(newInformation);
                        }
                        */
                    }
                    handler.sendEmptyMessage(0x32);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        }
    }
}
