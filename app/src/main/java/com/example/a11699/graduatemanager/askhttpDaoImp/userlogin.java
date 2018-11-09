package com.example.a11699.graduatemanager.askhttpDaoImp;

import com.example.a11699.graduatemanager.activity.LoginActivity;
import com.example.a11699.graduatemanager.askHttpDao.userloginImp;
import com.example.a11699.graduatemanager.internet.Net;

import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//登陆请求网络
public class userlogin implements userloginImp {
    private LoginActivity loginActivity;

    public userlogin(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    @Override
    public void loginValidate(final String username, final String password) {
        Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                String url= Net.firstLogin+"?s_sid="+username+"&s_pwd="+password;
                Log.i("zjc",url);
                OkHttpClient okHttpClient=new OkHttpClient();
                final Request request=new Request.Builder().url(url).build();
                Call call=okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("internet","网络请求失败");
                        loginActivity.shibai();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i("ZJC","请求成功");
                        String responseData=response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            Log.i("zjc",responseData);
                            //
                            // *
                            // 学校端返回的数据是：
                            // 没数据的时候{"Firstlogin":{}}
                            // 有数据的时候：
                            //{"Firstlogin":{"stu_id":"201604070101","token":"a3c5f24c062c4b0d24b03a3d4efe1919"}}
                            //
                            JSONObject jsonObject1=jsonObject.getJSONObject("firstLogin");
                            try{
                                String stu_id=jsonObject1.getString("sid");//拿到登录着的id
                                String password=jsonObject1.getString("token");
                                loginActivity.loginCallback(stu_id,password);//判断登录情况
                            }catch(Exception e){
                                loginActivity.loginBack();
                                Log.i("zjc","没这项值");
                            }
                    /*
                   可以解析json里的值
                   JSONObject jsonArray=jsonObject.getJSONObject("data");//学校端返回的数据是{"data":{}}
                    String token=jsonArray.getString("loginname");
                    */
                        } catch (JSONException e) {
                            Log.i("zjc","失败");
                        }
                    }
                });
            }
        });
    }
}
