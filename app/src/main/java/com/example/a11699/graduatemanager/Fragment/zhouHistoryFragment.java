package com.example.a11699.graduatemanager.Fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a11699.graduatemanager.Adapter.zhouHistoryAdapter;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.activity.searchInfromationActivity;
import com.example.a11699.graduatemanager.askHttpDao.zhoujiDao;
import com.example.a11699.graduatemanager.askhttpDaoImp.zhoujiDaoImp;
import com.example.a11699.graduatemanager.lei.zhoujiinformation;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class zhouHistoryFragment extends Fragment {
    private RecyclerView mehistory_recycle;
    private View view;
    private List<zhoujiinformation> list=new ArrayList<>();
    private zhoujiDao zhoujiDaro=new zhoujiDaoImp(this);
    int lastVisibleItem;
    int page=0;
    int size=0;

    LinearLayoutManager linearLayoutManager;
    zhouHistoryAdapter zhouHistoryAdapterr;

    public zhouHistoryFragment() {
        // Required empty public constructor
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.i("zjc","onstart被执行了");
        size=0;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    //    view=inflater.inflate(R.layout.fragment_zhou_history, container, false);
        if(view==null){
            view=inflater.inflate(R.layout.fragment_zhou_history, container, false);
            initview();
        }
            ViewGroup parent= (ViewGroup) view.getParent();
            if(parent!=null){
                parent.removeView(view);
            }
        mehistory_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == zhouHistoryAdapterr.getItemCount()) {
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
    private void initview(){

        // 5. 设置点击返回按键后的操作（通过回调接口）

        mehistory_recycle=view.findViewById(R.id.mehistory_recycle);
        linearLayoutManager=new LinearLayoutManager(getContext());
        mehistory_recycle.setLayoutManager(linearLayoutManager);
        zhoujiDaro.chartList(String.valueOf(page),false,1);
        size=this.list.size();

    }
    public void getNumber(List<zhoujiinformation> listt){

        this.list.addAll(listt);
        zhouHistoryAdapterr=new zhouHistoryAdapter(getContext(),list);
        mehistory_recycle.setAdapter(zhouHistoryAdapterr);
        zhouHistoryAdapterr.setOnclickItem(new zhouHistoryAdapter.OnclickItem() {
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
            zhouHistoryAdapterr.npshuju=-1;
        }else{
            size=this.list.size();
        }
        Log.i("zjc",size+"      "+this.list.size());
        zhoujiDaro.chartList(String.valueOf(page),true,1);
    }
    //news刷新加载更多的时候调用
    public void freshNews(List<zhoujiinformation> newInformations) {

        int sizez = this.list.size();
        this.list.addAll(newInformations);
        Log.i("zjc",size+"          "+this.list.size());
            zhouHistoryAdapterr.notifyItemRangeChanged(0, newInformations.size());//在两者之间添加数据
            zhouHistoryAdapterr.changeMoreStatus(zhouHistoryAdapterr.PULLUP_LOAD_MORE);
    }
    void search(String resultname){

    }

}
