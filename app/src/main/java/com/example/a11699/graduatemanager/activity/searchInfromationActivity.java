package com.example.a11699.graduatemanager.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.askHttpDao.zhoujiDao;
import com.example.a11699.graduatemanager.askhttpDaoImp.zhoujiDaoImp;
import com.example.a11699.graduatemanager.lei.zhoujiinformation;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.transformer.ZoomOutSlideTransformer;

import java.util.ArrayList;
import java.util.List;

public class searchInfromationActivity extends BaseActivity {
    private ImageView acback;
    private TextView a_title,a_time,a_body,ping;
    zhoujiDao zhoujiDao=new zhoujiDaoImp(this);
    private Banner zhouji_banner;
    String aid;//活动id
    private  String tupianlianjie;
    private List<String> imgList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_infromation);
        aid=getIntent().getStringExtra("hid");
        initview();
    }
    void initview(){
        zhouji_banner=findViewById(R.id.zhouji_banner);
        acback=findViewById(R.id.acback);
        a_title=findViewById(R.id.a_title);
        a_time=findViewById(R.id.a_time);
        a_body=findViewById(R.id.a_body);
        ping=findViewById(R.id.ping);
        zhoujiDao.acinformation(aid);
        acback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void show(zhoujiinformation zhoujiinformation){
        tupianlianjie=zhoujiinformation.getImage();
        a_title.setText(zhoujiinformation.getTitle());
        a_time.setText(zhoujiinformation.getDate());
        a_body.setText(zhoujiinformation.getDetail());
        if(zhoujiinformation.getReviewDetail()!=null){
            ping.setText(zhoujiinformation.getReviewDetail());
        }
        initdata();
    }
    void initdata(){
        String[] ss = tupianlianjie.split(",");
        imgList=new ArrayList<>();
        for(int i=0;i<ss.length;i++){
            imgList.add(ss[i]);
        }
        zhouji_banner.setImages(imgList)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setBannerAnimation(Transformer.Default)
                .setImageLoader(new GlideImageLoader())
                .setBannerAnimation(ZoomOutSlideTransformer.class)
                .start();
    }
    //轮播图用来显示图片的
    class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);
        }
    }
}
