package com.example.a11699.graduatemanager.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a11699.graduatemanager.MyApplication;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.lei.zhoujiinformation;

import java.util.List;

public class zhouHistoryAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<zhoujiinformation> list;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //底部FootView
    private int load_more_status = 0; //上拉加载更多状态-默认为0
    public static final int PULLUP_LOAD_MORE = 0; //上拉加载更多
    public static final int LOADING_MORE = 1; //正在加载中
    public static   int npshuju=0;
    public zhouHistoryAdapter(Context context, List<zhoujiinformation> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        if (i == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_historyzhou, viewGroup, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        } else if (i == TYPE_FOOTER) {
            View vieww = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_jiazai, viewGroup, false);
            FooterHolder footViewHolder = new FooterHolder(vieww);
            return footViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof MyViewHolder) {
            ((MyViewHolder) viewHolder).zhou_history_time.setText(list.get(i).getDate());
            ((MyViewHolder) viewHolder).zhou_history_title.setText(list.get(i).getTitle());
            ((MyViewHolder) viewHolder).zhou_history_body.setText(list.get(i).getDetail());
            ((MyViewHolder) viewHolder).red_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickItem.OnItemClick( list.get(i).getCid());
                }
            });
        } else if (viewHolder instanceof FooterHolder) {
            FooterHolder footViewHolder = (FooterHolder) viewHolder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    if(npshuju==-1){
                        footViewHolder.jiazaidonghua.setIndeterminate(false);
                        footViewHolder.jiazia.setText("没数据了...");
                        npshuju=0;
                    }else{
                        footViewHolder.jiazaidonghua.setIndeterminate(true);
                        footViewHolder.jiazia.setText("上拉加载更多...");
                    }
                    break;
                case LOADING_MORE:
                    footViewHolder.jiazaidonghua.setIndeterminate(true);
                    footViewHolder.jiazia.setText("正在加载更多数据...");
                    break;
            }
        }
    }
//((AnnounceAdapter.LoadingHolder) holder).loading_pb.setIndeterminate(false);
    @Override
    public int getItemCount() {
        return list.size()+1;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView zhou_history_title,zhou_history_time,zhou_history_body;
        private LinearLayout red_layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            zhou_history_title=itemView.findViewById(R.id.zhou_history_title);
            zhou_history_time=itemView.findViewById(R.id.zhou_history_time);
            zhou_history_body=itemView.findViewById(R.id.zhou_history_body);
            red_layout=itemView.findViewById(R.id.red_layout);
        }
    }
    public class FooterHolder extends RecyclerView.ViewHolder{
        private ProgressBar jiazaidonghua;
        private TextView jiazia;
        public FooterHolder(@NonNull View itemView) {
            super(itemView);
            jiazaidonghua=itemView.findViewById(R.id.jizaidonghua);
            jiazia=itemView.findViewById(R.id.jiazai);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position+1==getItemCount()){
            return  TYPE_FOOTER;
        }else{
            return TYPE_ITEM;
        }
     }
    public void changeMoreStatus(int status){
        load_more_status=status;
        notifyDataSetChanged();
    }
    public interface  OnclickItem{
        void OnItemClick(String position);
    }
    OnclickItem onclickItem;

    public void setOnclickItem(OnclickItem onclickItem) {
        this.onclickItem = onclickItem;
    }
}
