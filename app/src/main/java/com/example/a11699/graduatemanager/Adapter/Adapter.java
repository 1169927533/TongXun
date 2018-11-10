package com.example.a11699.graduatemanager.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a11699.graduatemanager.ECApplication;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.circle.CirvleImageView;
import com.example.a11699.graduatemanager.lei.telPeople;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.adapter.EMACallManagerListener;

import java.util.List;

public class Adapter extends RecyclerView.Adapter{
    private Context context;
    private List<telPeople> list;
    // 当前会话对象
    private EMConversation mConversation;
    public Adapter(Context context, List<telPeople> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.tel_item,viewGroup,false);
        MyviewHolder holder=new MyviewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        ((MyviewHolder)viewHolder).textView.setText(list.get(i).getSid());
        ECApplication.list.clear();
        ((MyviewHolder)viewHolder).liaotiantouxoang.setImageURL(list.get(i).getPicture_url());
        ViewCompat.setTransitionName(  ((MyviewHolder)viewHolder).talk_o, "one");
        mConversation = EMClient.getInstance().chatManager().getConversation(list.get(i).getName(), null, true);
        // 设置当前会话未读数为 0
        mConversation.markAllMessagesAsRead();
        // 打开聊天界面获取最后一条消息内容并显示
        if (mConversation.getAllMessages().size() > 0) {
            EMMessage messge = mConversation.getLastMessage();
            if(messge.getBody() instanceof EMImageMessageBody){
                ((MyviewHolder)viewHolder).tel_itemmessage.setText("一张图片信息");
            }else if(messge.getType()==EMMessage.Type.TXT){
                EMTextMessageBody body = (EMTextMessageBody) messge.getBody();
                // 将消息内容和时间显示出来
                ((MyviewHolder)viewHolder).tel_itemmessage.setText(body.getMessage());
            }else{
                ((MyviewHolder)viewHolder).tel_itemmessage.setText("一条语音消息");
            }
        }
        ((MyviewHolder) viewHolder).talk_o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onCLick(i);

            }
        });
        ((MyviewHolder) viewHolder).talk_o.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClick.onItemLongClick(v,i);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyviewHolder extends RecyclerView.ViewHolder{
        public LinearLayout talk_o;
        public TextView textView;
         public ImageView imageView;
        private TextView tel_itemmessage;//聊天
        private CirvleImageView liaotiantouxoang;//人的照片
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.re_tel_name);
            imageView=itemView.findViewById(R.id.tel_message);
            tel_itemmessage=itemView.findViewById(R.id.tel_itemmessage);
            talk_o=itemView.findViewById(R.id.talk_o);
            liaotiantouxoang=itemView.findViewById(R.id.liaotiantouxoang);
        }
    }
    public interface  OnCLick{
        public void onCLick(int position);
    }
    OnCLick onClick;

    public void setOnClick(OnCLick onClick) {
        this.onClick = onClick;
    }

    public interface OnLongClick{
       public void onItemLongClick(View view, int position);
    }
    OnLongClick onLongClick;

    public void setOnLongClick(OnLongClick onLongClick) {
        this.onLongClick = onLongClick;
    }
}
