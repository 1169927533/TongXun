package com.example.a11699.graduatemanager.askhttpDaoImp;

import android.os.Message;
import android.util.Log;

import com.example.a11699.graduatemanager.Fragment.TalkFragment;
import com.example.a11699.graduatemanager.askHttpDao.allFriendDao;
import com.example.a11699.graduatemanager.internet.Net;
import com.example.a11699.graduatemanager.lei.dabianInformation;
import com.example.a11699.graduatemanager.lei.telPeople;
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

public class findAllFriendDaoImp implements allFriendDao {
    private TalkFragment talkFragment;
private List<telPeople> list=new ArrayList<>();
    public findAllFriendDaoImp(TalkFragment talkFragment) {
        this.talkFragment = talkFragment;
    }
Handler handler=new Handler() {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg.what==20){
            talkFragment.showALlFrinend(list);
        }
    }
};
    @Override
    public void findAllFriend(String id) {
        String url= Net.getAllStudnet+"?id="+id;
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zjc","查找所有好友失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resBody=response.body().string();
                try {
                    JSONObject jsonObject=new JSONObject(resBody);
                    JSONArray jsonArray=jsonObject.getJSONArray("getUserName");
                    Gson gson=new Gson();
                    list=new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++){
                        telPeople finfor = gson.fromJson(String.valueOf(jsonArray.get(i)),telPeople.class);
                        Log.i("zjc",finfor.getId()+" "+finfor.getName());
                        list.add(finfor);
                    }
                    handler.sendEmptyMessage(20);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
