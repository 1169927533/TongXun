package com.example.a11699.graduatemanager.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.a11699.graduatemanager.Adapter.qian_itemAdapter;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.askHttpDao.qiandaoImp;
import com.example.a11699.graduatemanager.askhttpDaoImp.qiandaoDaoImp;
import com.example.a11699.graduatemanager.lei.stationInformation;


import java.util.ArrayList;
import java.util.List;

public class qiandaoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView qian_zuo;
    private Button qiandaoa;
    public AMapLocationClient mLocationClient = null;
    stationInformation stationInformation;//放经纬度的类
    private RecyclerView recyclerView;
    qian_itemAdapter qianAdapter;
    private List<stationInformation> list = new ArrayList<>();
    String shiii = "", quii = "", jingii = "如果这里没出数据，请重新签到一次，如果还没数据，那就是你没有打开定位权限", weiii = "请前往设置中心，打开定位权限";
    //声明定位回调监听器
    public AMapLocationClientOption mLocationOption = null;
    qiandaoImp qiandaoImpp = new qiandaoDaoImp(this);  //签到
    public static String nodiandao = "0";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x11) {
                //进行签到
                //stationInformation = (stationInformation) msg.obj;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qiandao);
        initview();
    }

    void initview() {
        qiandaoa = findViewById(R.id.qiandaoa);
        qian_zuo = findViewById(R.id.qian_zuo);
        recyclerView = findViewById(R.id.qian_recycleqw);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(qiandaoActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        qianAdapter = new qian_itemAdapter(this, list);
        recyclerView.setAdapter(qianAdapter);
        qian_zuo.setOnClickListener(this);
        qiandaoa.setOnClickListener(this);
        geiPosition();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qian_zuo:
                finish();
                break;
            case R.id.qiandaoa:
                sureQiandoa(shiii,quii,jingii,weiii);
                break;
        }
    }

    void geiPosition() {
        //初始化定位
        mLocationClient = new AMapLocationClient(qiandaoActivity.this);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，AMapLocationMode.Battery_Saving为低功耗模式，AMapLocationMode.Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    weiii=String.valueOf(aMapLocation.getLatitude());//获取纬度
                    jingii=String.valueOf(aMapLocation.getLongitude());//获取经度
                    aMapLocation.getAccuracy();//获取精度信息
                    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    aMapLocation.getCountry();//国家信息
                    aMapLocation.getProvince();//省信息
                    shiii=aMapLocation.getCity();//城市信息
                   quii= aMapLocation.getDistrict();//城区信息
                    aMapLocation.getStreet();//街道信息
                    aMapLocation.getStreetNum();//街道门牌号信息
                    aMapLocation.getCityCode();//城市编码
                    aMapLocation.getAdCode();//地区编码
                   // Message message = new Message();
                   // stationInformation = new stationInformation(aMapLocation.getCity(), aMapLocation.getDistrict(), String.valueOf(aMapLocation.getLongitude()), String.valueOf(aMapLocation.getLatitude()));
                 //   message.what = 0x11;
                  //  message.obj = stationInformation;
                 //   handler.sendMessage(message);
                    mLocationClient.stopLocation();//停止定位
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("info", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    //签到提示界面
    void sureQiandoa(final String shii, final String qui, final String jingi, final String weii) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.qiandao, null);
        builder.setView(view);
        final AlertDialog dialog = builder.show();
        final TextView qian_jindu = view.findViewById(R.id.qian_jindu);
        qian_jindu.setText(jingi);
        final TextView qian_weidu = view.findViewById(R.id.qian_weidu);
        qian_weidu.setText(weii);
        final TextView qian_shi = view.findViewById(R.id.qian_shi);
        qian_shi.setText(shii);
        final TextView qian_qu = view.findViewById(R.id.qian_qu);
        qian_qu.setText(qui);
        final Button button = view.findViewById(R.id.qian_queding);//输入被加者的备注
        final Button button1 = view.findViewById(R.id.qian_quxiao);//申请理由
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // stationInformation=new stationInformation(shii,qui, jingi, weii);
                // //向后台发送经纬度 并拿到历史记录存到list里面
                qiandaoImpp.qian(shii, qui, jingi, weii);
                dialog.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void show(List<stationInformation> listt) {
        if (nodiandao.equals("1")) {
            Toast.makeText(qiandaoActivity.this, "今天已签到", Toast.LENGTH_LONG).show();
            nodiandao = "0";
        }
        nodiandao = "1";
        qianAdapter = new qian_itemAdapter(qiandaoActivity.this, listt);
        recyclerView.setAdapter(qianAdapter);
    }

}
