package com.example.a11699.graduatemanager.askhttpDaoImp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.a11699.graduatemanager.activity.dabianListActivity;
import com.example.a11699.graduatemanager.activity.item_dabianActivity;
import com.example.a11699.graduatemanager.askHttpDao.dabianDao;
import com.example.a11699.graduatemanager.internet.Net;
import com.example.a11699.graduatemanager.lei.dabianInformation;
import com.google.gson.Gson;
import com.google.gson.JsonStreamParser;

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

public class dabianDaoImp implements dabianDao {
    private dabianListActivity activity;
    private item_dabianActivity item_dabianActivity;
private List<dabianInformation> list;

    public dabianDaoImp(com.example.a11699.graduatemanager.activity.item_dabianActivity item_dabianActivity) {
        this.item_dabianActivity = item_dabianActivity;
    }

    public dabianDaoImp(dabianListActivity activity) {
        this.activity = activity;
    }
Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg.what==0x34){
            Toast.makeText(activity,"网络访问失败",Toast.LENGTH_LONG).show();
        }
        else if(msg.what==0x35){
            Log.i("zjc","答辩列表的大小"+list.size());
            activity.showList(list);
        }
        else if(msg.what==0x36){
            item_dabianActivity.show();
        }
    }
};
    @Override
    public void getDabainInfor(String s_sid) {
         String url= Net.rejoinList+"?s_sid="+s_sid;
         Log.i("zjc","答辩列表请求地址："+url);
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zjc","答辩网络访问失败");
                handler.sendEmptyMessage(0x34);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody=response.body().string();
                list=new ArrayList<>();
                try {
                    JSONObject jsonObject=new JSONObject(responseBody);
                    JSONArray jsonArray=jsonObject.getJSONArray("rejoinList");
                    Gson gson=new Gson();
                    for(int i=0;i<jsonArray.length();i++){
                        dabianInformation finfor = gson.fromJson(String.valueOf(jsonArray.get(i)), dabianInformation.class);
                       list.add(finfor);
                    }
                    handler.sendEmptyMessage(0x35);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void putDabain(String s_sid, String neirong, String r_id) {
        String url= Net.studentRejoin+"?s_sid="+s_sid+"&s_detail="+neirong+"&s_rid="+r_id;
        Log.i("zjc","答辩列表请求地址："+url);
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zjc","答辩网络访问失败");
                handler.sendEmptyMessage(0x34);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    handler.sendEmptyMessage(0x36);
            }
        });
    }
}
