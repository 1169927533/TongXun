package com.example.a11699.graduatemanager.Fragment;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.activity.dabianListActivity;
import com.example.a11699.graduatemanager.activity.writezhouji;
import com.example.a11699.graduatemanager.activity.zhouhistoryActivity;
import com.example.a11699.graduatemanager.utils.MyScrollView;
import com.example.a11699.graduatemanager.utils.PopUpMessageUtil;
import com.haibin.calendarview.CalendarView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkFragment extends Fragment implements View.OnClickListener,MyScrollView.OnScrollListener {
    private RelativeLayout work_zhuanxie;//撰写周记
    private RelativeLayout work_readhistory;//查看历史
    private RelativeLayout work_dabian;//答辩
    private RelativeLayout work_xiaoxi;//消息
    private Banner banner;
    private List list = new ArrayList();
    private View view;//总的视图
    Animation shake;
    Handler handler=new Handler();
    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    static ImageView imageView;
    private MyScrollView myScrollView;//自定义scrollview控件
    private LinearLayout mBuyLayout;
    private FrameLayout mTopBuyLayout;
    private ImageView messageImage;


    public WorkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_work, container, false);
         list.add(R.drawable.er);
         list.add(R.drawable.err);
        list.add(R.drawable.errr);
         list.add(R.drawable.errrrr);
        list.add(R.drawable.errrrrr);
        initview();
        return view;
    }

    void initview() {

        pref =getContext().getSharedPreferences("jackie", Context.MODE_PRIVATE);
        editor=pref.edit();
        //   imageView=view.findViewById(R.id.talk_xiao);
        myScrollView = (MyScrollView) view.findViewById(R.id.scrollView);
        mBuyLayout = (LinearLayout) view. findViewById(R.id.buy);
        mTopBuyLayout = (FrameLayout) view.findViewById(R.id.top_buy_layout);
        myScrollView.setOnScrollListener(this);
//当布局的状态或者控件的可见性发生改变回调的接口
        view. findViewById(R.id.parent_layout).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //这一步很重要，使得上面的购买布局和下面的购买布局重合
                onScroll(myScrollView.getScrollY());

            }
        });

        work_dabian = view.findViewById(R.id.work_dabian);
        work_readhistory = view.findViewById(R.id.work_readhistory);
        work_zhuanxie = view.findViewById(R.id.work_zhuanxie);
        work_xiaoxi = view.findViewById(R.id.work_xiaoxi);
        messageImage=view.findViewById(R.id.messageImage);
        banner = view.findViewById(R.id.home_banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setBannerAnimation(Transformer.Default);
        banner.setImages(list);
        banner.setImageLoader(new GlideImageLoader());
        banner.setDelayTime(3000);
        banner.isAutoPlay(true);
        banner.start();


        shake=AnimationUtils.loadAnimation(getContext(),R.anim.shake);//加载动画资源文件??

        work_zhuanxie.setOnClickListener(this);
        work_readhistory.setOnClickListener(this);
        work_dabian.setOnClickListener(this);
        work_xiaoxi.setOnClickListener(this);
      /*  if(pref.getString("getMessage","").equals("onon")){
            imageView.setImageResource(R.drawable.shoudaomessafe);
        }else{
            imageView.setImageResource(R.drawable.mess);

        }
        */
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.work_zhuanxie:
                //  work_zhuanxie.startAnimation(shake);
                //  handler.postDelayed(new Runnable() {
                //      @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                //     @Override
                //     public void run() {
                Explode e=new Explode();
                e.setDuration(600);
                getActivity().getWindow().setExitTransition(e);
                getActivity().getWindow().setEnterTransition(e);
                Intent intent = new Intent(getActivity(), writezhouji.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                //     }
                //   },300);
                break;
            case R.id.work_readhistory:
                //  work_readhistory.startAnimation(shake);
                //  handler.postDelayed(new Runnable() {
                //      @Override
                //      public void run() {
                Slide s= new Slide();
                s.setDuration(600);
                getActivity().getWindow().setExitTransition(s);
                getActivity().getWindow().setEnterTransition(s);
                Intent intent1 = new Intent(getActivity(), zhouhistoryActivity.class);
                startActivity(intent1,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                //     }
                ////   },300);
                break;
            case R.id.work_xiaoxi:
                //  work_xiaoxi.startAnimation(shake);
                editor.putString("getMessage","0");
                editor.commit();
                //  imageView.setImageResource(R.drawable.mess);
                //   handler.postDelayed(new Runnable() {
                //      @Override
                //      public void run() {
                //   Fade f= new Fade();
                //    f.setDuration(600);
                //    getActivity().getWindow().setExitTransition(f);
                //   getActivity().getWindow().setEnterTransition(f);
                //    Intent intent2 = new Intent(getActivity(), messageActivity.class);
                //   startActivity(intent2,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());

                PopUpMessageUtil.getInstance()._show(getActivity(), messageImage);


                //     }
                //   },300);
                break;
            case R.id.work_dabian:
                Slide si= new Slide();
                si.setDuration(600);
                getActivity().getWindow().setExitTransition(si);
                getActivity().getWindow().setEnterTransition(si);
                Intent intentt=new Intent(getActivity(),dabianListActivity.class);
                startActivity(intentt,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                break;
        }
    }



    @Override
    public void onScroll(int scrollY) {
        int mBuyLayout2ParentTop = Math.max(scrollY, mBuyLayout.getTop());
        mTopBuyLayout.layout(0, mBuyLayout2ParentTop, mTopBuyLayout.getWidth(), mBuyLayout2ParentTop + mTopBuyLayout.getHeight());

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
    public   static    class   wozijiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("myBroadCast")) {
                //接收到广播，取出里面携带的数据
                String str = intent.getStringExtra("data");
                Log.i("zjc","接收到的广播的数据：" + str);
                // imageView.setImageResource(R.drawable.shoudaomessafe);
            }
        }
    }



}
