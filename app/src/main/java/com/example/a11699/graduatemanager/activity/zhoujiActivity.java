package com.example.a11699.graduatemanager.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.askHttpDao.zhoujiDao;
import com.example.a11699.graduatemanager.askhttpDaoImp.zhoujiDaoImp;
import com.example.a11699.graduatemanager.utils.PopWindow_image;
import com.example.a11699.graduatemanager.utils.PopupMenuUtil;
import com.example.a11699.graduatemanager.utils.RealPathFromUriUtils;
import com.facebook.drawee.view.DraweeView;
import com.hyphenate.chat.EMClient;

import net.tsz.afinal.FinalBitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class zhoujiActivity extends BaseActivity implements View.OnClickListener{
    public static final int REQUEST_PICK_IMAGE = 11101;
    private String realPathFromUri;//图片的地址
    private TextView zhouji_time;
    private EditText zhouji_head,zhouji_body;
    private Button zhouji_subbmie;
    String  title,textbody;
    List<String> dizhi=new ArrayList<>();
  //  private DraweeView zhouji_tjdimg;//选的图片
    RelativeLayout diwuge;//查看选择按钮
    private ImageView iv_img;//选择控件里的那个十字架
    private FrameLayout xuanzetupain;//选择图片
    zhoujiDao zhoujiDo=new zhoujiDaoImp(this);
    private  String reqi="";
    ProgressBar processsdsd;
    private FinalBitmap fb;//显示图片
    String baseEncode = "";
    int i=0;
     String bianmadizhi ="";
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x22){
                processsdsd.setVisibility(View.GONE);
                Toast.makeText(zhoujiActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhouji);
        i=0;
        bianmadizhi="";
        dizhi=new ArrayList<>();
        View devorView = getWindow().getDecorView();
        View contentView=findViewById(Window.ID_ANDROID_CONTENT);
        devorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(devorView, contentView));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Intent intent=getIntent();
        reqi=intent.getStringExtra("reqi");
        initview();
    }
    void initview(){
        fb=FinalBitmap.create(this);
        processsdsd=findViewById(R.id.processsdsd);
        xuanzetupain=findViewById(R.id.xuanzetupain);//选择图片
      //  zhouji_tjdimg=findViewById(R.id.zhouji_tjdimg);//选择的图片
        diwuge=findViewById(R.id.diwuge);//查看选择图片按钮
        iv_img=findViewById(R.id.iv_img);
        zhouji_time=findViewById(R.id.zhouji_time);
        zhouji_body=findViewById(R.id.zhouji_body);
        zhouji_head=findViewById(R.id.zhouji_head);
        zhouji_subbmie=findViewById(R.id.zhouji_subbmie);
        zhouji_time.setText(reqi);
        zhouji_subbmie.setOnClickListener(this);
        xuanzetupain.setOnClickListener(this);
      //  zhouji_tjdimg.setOnClickListener(this);
        diwuge.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zhouji_subbmie:
                processsdsd.setVisibility(View.VISIBLE);
                tijiao();
                break;
            case R.id.xuanzetupain:
                xuanze();
                break;
      /*      case R.id.zhouji_tjdimg:
                new PopWindow_image(fb,this,realPathFromUri).showAtLocation(v,0,0,0);
                break;
                */
            case R.id.diwuge:
                PopupMenuUtil.getInstance()._show(this, iv_img,dizhi);
                break;
        }
    }
    //提交周记
    void tijiao(){
        title=zhouji_head.getText().toString();
        textbody=zhouji_body.getText().toString();
        //接口 
        String path = "http://i3c8x94lqb.51http.tech/TXGLwai/addChart";
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("s_sid", EMClient.getInstance().getCurrentUser())
                .add("s_title", title)
                .add("s_content",textbody)
                .add("image",baseEncode)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                handler.sendEmptyMessage(0x22);
            }
        });
      //  zhoujiDo.subbmitzhou(reqi,title,textbody);
    }
    void xuanze(){
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_IMAGE:
                    if (data != null) {
                        realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(this, data.getData());
                        dizhi.add(realPathFromUri);
                        if(i==8){
                            i=8;
                        }else{
                            i++;
                        }
//获取到图片地址/storage/emulated/0/zjcmcc/Image/logo.png
                        Log.i("zjc", realPathFromUri);
                        baseEncode += getImageStr(realPathFromUri)+",";

                        // 从相册返回的数据
                        if (data != null) {
                            // 得到图片的全路径
                            Uri uri = data.getData();
                       //      fb.display(zhouji_tjdimg ,realPathFromUri);//显示图片

                        }
                    } else {
                        Toast.makeText(this, "图片损坏，请重新选择", Toast.LENGTH_SHORT).show();
                    }break;
            }
        }
}

    public static String getImageStr(String imgFile){
        File param = new File(imgFile);
        Bitmap bitmap = BitmapFactory.decodeFile(param.getPath());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }
}
