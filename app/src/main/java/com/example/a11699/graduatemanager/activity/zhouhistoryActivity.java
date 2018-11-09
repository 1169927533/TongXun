package com.example.a11699.graduatemanager.activity;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;

import com.example.a11699.graduatemanager.Adapter.MyPagerAdapter;
import com.example.a11699.graduatemanager.Fragment.searchFragment;
import com.example.a11699.graduatemanager.Fragment.zhouHistoryFragment;
import com.example.a11699.graduatemanager.Fragment.zhoujiyueFram;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.lei.TabEntity;
import com.example.a11699.graduatemanager.utils.ViewFindUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class zhouhistoryActivity extends BaseActivity {
    private ViewPager zhouji_history_viewtab;
    List<String> tabList;//存放标题
    List<Fragment> viewList;//存放视图
    Fragment view1;
    Fragment view2;
    Fragment  view3;
    View mDecorView;
    //存放图片
    private int[] mIconSelectIds= {
            R.drawable.lishiunsele, R.drawable.readunselect,R.drawable.searchunsele};
    private int[] mIconUnselectIds = {
            R.drawable.lishisele, R.drawable.readsele,
            R.drawable.serachsele};
    private CommonTabLayout mTabLayout_2;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhouhistory);
        mDecorView=getWindow().getDecorView();
        initview();
    }
    void initview(){
        zhouji_history_viewtab=ViewFindUtils.find(mDecorView, R.id.zhouji_history_viewtab);
        tabList=new ArrayList<>();
        tabList.add("评阅过的周记");
        tabList.add("历史周记");
        tabList.add("搜索");
        for (int i = 0; i < tabList.size(); i++) {
            mTabEntities.add(new TabEntity(tabList.get(i), mIconSelectIds[i], mIconUnselectIds[i]));
        }

                addShuju();


    }
    void addShuju(){
      //  zhouji_history_tab.setTabMode(TabLayout.MODE_FIXED);
      //  zhouji_history_tab.addTab(zhouji_history_tab.newTab().setText(tabList.get(0)));
       // zhouji_history_tab.addTab(zhouji_history_tab.newTab().setText(tabList.get(1)));
      //  zhouji_history_tab.addTab(zhouji_history_tab.newTab().setText(tabList.get(2)));

        view1=new zhouHistoryFragment();
        view2 = new zhoujiyueFram();
        view3=new searchFragment();
        viewList=new ArrayList<>();
        viewList.add(view2);
        viewList.add(view1);
        viewList.add(view3);

        //添加ViewPage的适配器
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), viewList );
        zhouji_history_viewtab.setAdapter(myPagerAdapter);
        mTabLayout_2 = ViewFindUtils.find(mDecorView, R.id.zhouji_history_tab);
        tl_2();

        //zhouji_history_tab.setupWithViewPager(zhouji_history_viewtab);
   }
    Random mRandom = new Random();
    private void tl_2() {
        /** with ViewPager */

      //  mTabLayout_2 = ViewFindUtils.find(zhouji_history_viewtab, R.id.zhouji_history_tab);
        mTabLayout_2.setTabData(mTabEntities);
        mTabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                zhouji_history_viewtab.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    mTabLayout_2.showMsg(0, mRandom.nextInt(100) + 1);
//                    UnreadMsgUtils.show(mTabLayout_2.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        });
        zhouji_history_viewtab.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout_2.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        zhouji_history_viewtab.setCurrentItem(1);
    }
    protected int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
