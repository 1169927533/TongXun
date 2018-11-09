package com.example.a11699.graduatemanager.askhttpDaoImp;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.a11699.graduatemanager.Fragment.MineFragment;
import com.example.a11699.graduatemanager.activity.MainActivity;
import com.example.a11699.graduatemanager.askHttpDao.userImp;
import com.example.a11699.graduatemanager.internet.Net;
import com.example.a11699.graduatemanager.lei.userinformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class userDaoImp implements userImp {
    private MineFragment mineFragment;
    private MainActivity mainActivityy;
    userinformation userinformationn;
    public userDaoImp(MineFragment mineFragment) {
        this.mineFragment = mineFragment;
    }

    public userDaoImp(MainActivity mainActivity) {
        this.mainActivityy = mainActivity;
    }

    Handler handler=new Handler() {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg.what==0x20){
            Toast.makeText(mineFragment.getActivity(),"网络请求失败",Toast.LENGTH_LONG).show();
        }
        if(msg.what==0x21){
         //   userinformation userinformation=(userinformation)msg.obj;
           // mineFragment.show(userinformationn);
           mainActivityy.caozuocehuacaidan(userinformationn);
        }
    }
};
    @Override
    public void getUserInformation(String s_sid) {
         String url= Net.getUser+"?s_sid="+s_sid;
         Log.i("zjcc",url);
        OkHttpClient okHttpClient=new OkHttpClient();
        final Request request=new Request.Builder().url(url).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zjc","getUser网络访问失败");
               // handler.sendEmptyMessage(0x20);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody=response.body().string();
                try {
                    JSONObject jsonObject=new JSONObject(responseBody);
                    JSONObject jsonObject1=jsonObject.getJSONObject("student");
                    Log.i("zjc",jsonObject.toString());
                      String unit =jsonObject1.getString("unit");//公司
                      String name =jsonObject1.getString("name");//
                      String job  =jsonObject1.getString("job");
                      String tutor =jsonObject1.getString("tutor");//导师
                      String qiandaoCount =jsonObject1.getString("qiandaoCount");//签到次数
                       String tPhone=jsonObject1.getString("tPhone");//指导教师电话
                    String picture=jsonObject1.getString("picture");//指导教师电话
                    String city=jsonObject1.getString("city");
                        userinformationn=new userinformation(unit,name,job,tutor,qiandaoCount,tPhone,picture,city);
                      Message message=new Message();
                      message.obj=userinformationn;
                      message.what=0x21;
                      handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
