package com.example.a11699.graduatemanager.activity;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.transition.Explode;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.askhttpDaoImp.nadaoriqiDaoImp;
import com.example.a11699.graduatemanager.lei.alldatime;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
 import com.haibin.calendarview.CalendarView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class writezhouji extends jilei implements  CalendarView.OnCalendarSelectListener,  CalendarView.OnYearChangeListener, View.OnClickListener {

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;
    Button tijiao,write_back;

    CalendarView mCalendarView;
    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    String alltext="";
    nadaoriqiDaoImp nadao=new nadaoriqiDaoImp(this);
    @Override
    protected int getLayoutId() {
        return R.layout.activity_writezhouji;
    }

    @Override
    protected void initView() {
        initview();

    }

    @Override
    protected void initData() {
       /* int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
                getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
                getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
                getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
                getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
                getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
                getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用

        mCalendarView.setSchemeDate(map);
        */
    }
    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }

    void initview() {
        setStatusBarDarkMode();
        tijiao=findViewById(R.id.tijiao);
        write_back=findViewById(R.id.write_back);
        mCalendarView=findViewById(R.id.ib_calendarview);
        mTextMonthDay = (TextView) findViewById(R.id.tv_month_day);
        mTextYear = (TextView) findViewById(R.id.tv_year);
        mTextLunar = (TextView) findViewById(R.id.tv_lunar);
        mRelativeTool = (RelativeLayout) findViewById(R.id.rl_tool);
        mTextCurrentDay = (TextView) findViewById(R.id.tv_current_day);
        //这个是左上角的控件
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarView.showYearSelectLayout(mYear);////快速弹出年份选择月份
                    return;
                }
               mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        write_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();////滚动到当前日期
            }
        });
        tijiao.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Explode explode=new Explode();
                explode.setDuration(1000);
                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);
                Intent intent=new Intent(writezhouji.this,zhoujiActivity.class);
                intent.putExtra("reqi",alltext);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(writezhouji.this).toBundle());
            }
        });
        //这个是右上角的控件
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();////滚动到当前日期
            }
        });
        mCalendarLayout = (CalendarLayout) findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        alltext=mYear+"年"+mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日";
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
        nadao.getAllDate();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }
//日历被点击的时候
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        alltext=calendar.getYear()+"年"+calendar.getMonth() + "月" + calendar.getDay() + "日";
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }
    public void showll(List<alldatime> list){
        Map<String, Calendar> map = new HashMap<>();
        for(int i=0;i<list.size();i++){
            String[] parts = list.get(i).getDate().split("-");
            String nian=parts[0];
            String yue=parts[1];
            String re=parts[2];
            String [] arr = re.split("\\s+");
            Log.i("zjc",nian+"     "+yue+"   "+arr[0]);
            map.put(getSchemeCalendar(Integer.valueOf(nian), Integer.valueOf(yue), Integer.valueOf(arr[0]), 0xFFedc56d, "假").toString(),
                    getSchemeCalendar(Integer.valueOf(nian), Integer.valueOf(yue), Integer.valueOf(arr[0]), 0xFF40db25, "假"));

        }
        mCalendarView.setSchemeDate(map);
    }

}
