package com.example.a11699.graduatemanager.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.a11699.graduatemanager.Adapter.zhouHistoryAdapter;
import com.example.a11699.graduatemanager.Adapter.zhoureadAdapter;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.activity.searchInfromationActivity;
import com.example.a11699.graduatemanager.askHttpDao.zhoujiDao;
import com.example.a11699.graduatemanager.askhttpDaoImp.zhoujiDaoImp;
import com.example.a11699.graduatemanager.lei.zhoujiinformation;

import java.util.ArrayList;
import java.util.List;


public class zhoujiyueFram extends Fragment {
    View view;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    zhoujiDao zhoujiDao=new zhoujiDaoImp(this);
    private List<zhoujiinformation> list=new ArrayList<>();
    int lastVisibleItem;
    int page=0;
    int size=0;
    zhoureadAdapter zhoureadadapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_zhoujiyue_fram, container, false);
        initview();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == zhoureadadapter.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            page++;
                            news_loadMore();
                        }
                    }, 1000);
                }
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

            }
        });
        return view;
    }
    void initview(){
        recyclerView=view.findViewById(R.id.readzhoujirecycle);
        linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        zhoujiDao.chayidu(String.valueOf(page),false);
        size=this.list.size();
    }
    public void getNumber(List<zhoujiinformation> listt){
        this.list.addAll(listt);
        zhoureadadapter=new zhoureadAdapter(getContext(),list);
        recyclerView.setAdapter(zhoureadadapter);
        zhoureadadapter.setOnclickItem(new zhoureadAdapter.OnclickItem() {
            @Override
            public void OnItemClick(String position) {
                Intent intent=new Intent(getActivity(),searchInfromationActivity.class);
                intent.putExtra("hid", position);
                startActivity(intent);
            }
        });
    }
    void news_loadMore(){
        if(size==this.list.size()){
            Log.i("zjc",size+" "+this.list.size());
            zhoureadadapter.npshuju=-1;
        }else{
            size=this.list.size();
        }
        Log.i("zjc",size+"      "+this.list.size());
        zhoujiDao.chayidu(String.valueOf(page),true);
    }
    //news刷新加载更多的时候调用
    public void freshNews(List<zhoujiinformation> newInformations) {
        int sizez = this.list.size();
        this.list.addAll(newInformations);
        Log.i("zjc",size+"          "+this.list.size());
        zhoureadadapter.notifyItemRangeChanged(0, newInformations.size());//在两者之间添加数据
        zhoureadadapter.changeMoreStatus(zhoureadadapter.PULLUP_LOAD_MORE);
    }

}
