package com.example.a11699.graduatemanager.askhttpDaoImp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.a11699.graduatemanager.Fragment.searchFragment;
import com.example.a11699.graduatemanager.Fragment.zhouHistoryFragment;
import com.example.a11699.graduatemanager.Fragment.zhoujiyueFram;
import com.example.a11699.graduatemanager.activity.searchInfromationActivity;
import com.example.a11699.graduatemanager.activity.zhoujiActivity;
import com.example.a11699.graduatemanager.askHttpDao.zhoujiDao;
import com.example.a11699.graduatemanager.internet.Net;
import com.example.a11699.graduatemanager.lei.zhoujiinformation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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


public class zhoujiDaoImp implements zhoujiDao {
    private zhoujiActivity zhoujiActivity;
    private List<zhoujiinformation> list = new ArrayList<>();
    private zhouHistoryFragment zhouHistoryFragment;
    private zhoujiyueFram fragment;
    private searchFragment searchFragment;
    private searchInfromationActivity sActivity;
    private Boolean fra = false;
    private int who = 0;

    public zhoujiDaoImp(searchInfromationActivity sActivity) {
        this.sActivity = sActivity;
    }

    public zhoujiDaoImp(com.example.a11699.graduatemanager.activity.zhoujiActivity zhoujiActivity) {
        this.zhoujiActivity = zhoujiActivity;
    }

    public zhoujiDaoImp(com.example.a11699.graduatemanager.Fragment.zhouHistoryFragment zhouHistoryFragment) {
        this.zhouHistoryFragment = zhouHistoryFragment;
    }
    public zhoujiDaoImp(com.example.a11699.graduatemanager.Fragment.searchFragment searchFragment) {
        this.searchFragment = searchFragment;
    }
    public zhoujiDaoImp(zhoujiyueFram fragment) {
        this.fragment = fragment;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x24) {
                Toast.makeText(zhoujiActivity, "新建成功", Toast.LENGTH_LONG).show();
            } else if (msg.what == 0x25) {
                Toast.makeText(zhoujiActivity, "新建失败", Toast.LENGTH_LONG).show();
            } else if (msg.what == 0x26) {

                try {
                    Toast.makeText(zhoujiActivity, "网络出现问题", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (msg.what == 0x27) {
                List<zhoujiinformation> listt = (List<zhoujiinformation>) msg.obj;
                    if (fra) {
                        zhouHistoryFragment.freshNews(listt);
                    } else {
                        zhouHistoryFragment.getNumber(listt);
                    }
                }
                else if(msg.what==0x28){
                List<zhoujiinformation> listt = (List<zhoujiinformation>) msg.obj;
                if (fra) {
                    fragment.freshNews(listt);
                } else {
                    fragment.getNumber(listt);
                }
            }
            else if(msg.what==0x29){
                List<zhoujiinformation> listt = (List<zhoujiinformation>) msg.obj;
                Log.i("zjc","传递过来后的数据："+listt.size());
                    searchFragment.show(listt);
            }
            else if(msg.what==0x30){
                zhoujiinformation zhoujiinformation=(zhoujiinformation)msg.obj;
                if(zhoujiinformation!=null){
                    sActivity.show(zhoujiinformation);
                }

            }
            }

    };

    @Override
    public void subbmitzhou(final String time, final String title, final String body) {
        String url = Net.addChart + "?s_sid=" + EMClient.getInstance().getCurrentUser() + "&s_title=" + title + "&s_content=" + body;
        Log.i("zjc", "新建周记url：" + url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zjx", "新建周记访问失败");
                handler.sendEmptyMessage(0x38);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String re = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(re);
                    if (jsonObject.getString("result").equals("1")) {
                        handler.sendEmptyMessage(0x24);
                    } else {
                        handler.sendEmptyMessage(0x25);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void chartList(String page, Boolean fresh, int aaa) {
        who = aaa;
        fra = fresh;
        String url = Net.chartList + "?s_sid=" + EMClient.getInstance().getCurrentUser() + "&page=" + page + "&count=8";
        Log.i("zjcc", "查看周记urll：" + url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zjx", "查看周记访问失败");
                handler.sendEmptyMessage(0x30);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String re = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(re);
                    JSONArray jsonArray = jsonObject.getJSONArray("chartList");
                    Gson gson = new Gson();
                    list = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        zhoujiinformation newInformation = gson.fromJson(String.valueOf(jsonArray.get(i)), zhoujiinformation.class);

                        list.add(newInformation);
                    }
                    Message message = new Message();
                    message.obj = list;
                    message.what = 0x27;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void chayidu(String page, Boolean a) {
        fra = a;
        String url = Net.chartList + "?s_sid=" + EMClient.getInstance().getCurrentUser() + "&page=" + page + "&count=1000";
        Log.i("zjc", "查看已经被阅读的周记url：" + url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zjx", "查看周记访问失败");
                handler.sendEmptyMessage(0x31);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String re = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(re);
                    JSONArray jsonArray = jsonObject.getJSONArray("chartList");
                    Gson gson = new Gson();
                    list = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        zhoujiinformation newInformation = gson.fromJson(String.valueOf(jsonArray.get(i)), zhoujiinformation.class);
                         if(newInformation.getReview().equals("1")){
                            list.add(newInformation);
                         }
                    }

                    Message message = new Message();
                    message.obj = list;
                    message.what = 0x28;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void quanbuzhouji() {
        String url = Net.chartList + "?s_sid=" + EMClient.getInstance().getCurrentUser() + "&page=" + 0 + "&count=1000";
        Log.i("zjc", "查看周记url：" + url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zjx", "查看周记访问失败");
                handler.sendEmptyMessage(0x32);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String re = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(re);
                    JSONArray jsonArray = jsonObject.getJSONArray("chartList");
                    Gson gson = new Gson();
                    list = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        zhoujiinformation newInformation = gson.fromJson(String.valueOf(jsonArray.get(i)), zhoujiinformation.class);
                            list.add(newInformation);
                    }

                    Message message = new Message();
                    message.obj = list;
                    message.what = 0x29;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void acinformation(String idd) {
        String url=Net.infor+"?c_cid="+idd;
        Log.i("zjc", "url：" + url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            Log.i("zjc","网络访问失败");
                handler.sendEmptyMessage(0x26);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
               String resb=response.body().string();
                try {
                    JSONObject jsonObject=new JSONObject(resb);
                    JSONObject jsonObject1=jsonObject.getJSONObject("tChartDetail");
                    Gson gson=new Gson();
                    zhoujiinformation zhoujiinformation=gson.fromJson(String.valueOf(jsonObject1), com.example.a11699.graduatemanager.lei.zhoujiinformation.class);

                    Message message=new Message();
                    message.obj=zhoujiinformation;
                    message.what=0x30;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
