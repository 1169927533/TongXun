package com.example.a11699.graduatemanager.Fragment;


import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.a11699.graduatemanager.Adapter.Adapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a11699.graduatemanager.Adapter.Adapter;
import com.example.a11699.graduatemanager.ECApplication;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.activity.EChatActivity;
import com.example.a11699.graduatemanager.activity.MainActivity;
import com.example.a11699.graduatemanager.askHttpDao.allFriendDao;
import com.example.a11699.graduatemanager.askhttpDaoImp.findAllFriendDaoImp;
import com.example.a11699.graduatemanager.lei.startInformation;
import com.example.a11699.graduatemanager.lei.telPeople;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TalkFragment extends Fragment implements View.OnClickListener,EMMessageListener{
    private ImageView addTel;//添加好友按钮
    private telPeople telman;//联系人
    SwipeRefreshLayout swipeRefreshLayout;//刷新
    private List<telPeople> telPeopleList=new ArrayList<>();
    private RecyclerView recyclerView;
    private ImageView message;
    Adapter adapter;
    List<String> list;//保存好友的

    String aname;//登陆者id
    // 消息监听器
    private EMMessageListener mMessageListener;
    String ridd="";//所有好友的id
    allFriendDao allFriendd=new findAllFriendDaoImp(this);
    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==12){
                //收到邀请后执行
                startInformation.message="5";
                message.setImageResource(R.drawable.message4);
                String username=(String)msg.obj;//邀请者的id
                startInformation.userNumber=username;
                startInformation.panduan=true;
                Log.i("zjc","请求者的id："+username);
            }
            else if(msg.what==0x13){
                //用来显示好友列表
                list= (List<String>) msg.obj;
                Log.i("friend","你的好友总数 ："+list.size());
                for(String l:list) {
                    ridd+=l+",";
                }
                Log.i("zjc",ridd);//从环信拿得所有好友的id
                //询问网络
                if(list.size()!=0){
                    allFriendd.findAllFriend(ridd);
                }
            }
            else if(msg.what==0x14){
                //接受方同意后
                telman=new telPeople(startInformation.userNumber,startInformation.userNumber);
                ridd+=telman.getName();
                Log.i("friend","同意后对方的id"+telman.getName());
                allFriendd.findAllFriend(ridd);

            }
            else if(msg.what==0x15){
                //发送方被同意后更新数据
                adapter.notifyItemRangeInserted(telPeopleList.size(),1);
            }
            else if(msg.what==0x16){
                message.setImageResource(R.drawable.message4);

            }else  if(msg.what==0x17){
                //被拒后后的消息提醒
                  startInformation.jujue="1";
            }
        }
    };
    public TalkFragment() {

    }
    private View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      if(rootview==null){
          rootview=inflater.inflate(R.layout.fragment_talk, container, false);

      }else{
          ViewGroup parent= (ViewGroup) rootview.getParent();
          if(parent!=null){
              parent.removeView(rootview);
          }
      }

        initview(rootview);
        return rootview;
    }
         //展示所有学生
    public void showALlFrinend(List<telPeople> list){
        for(telPeople l:list) {
            telman = new telPeople(l.getSid(),l.getName(),l.getPicture_url());
           //这个是第一次进来的时候的好友列表
            telPeopleList.add(telman);
            Log.i("friend","好友id"+l.getSid()+"好友名字："+l.getName()+"              ");
        }
        Log.i("friend","所有的好友id："+ridd);
        ridd="";
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EMClient.getInstance().contactManager().setContactListener(new MyContentListener());
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //拿到好友列表
                    List<String> list = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Message message=new Message();
                    message.obj=list;
                    message.what=0x13;
                    mhandler.sendMessage(message);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    void initview(View view){
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
        mMessageListener = this;
        addTel=view.findViewById(R.id.addTel);//添加好友
        addTel.setOnClickListener(this);
        recyclerView=view.findViewById(R.id.telphone);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout=view.findViewById(R.id.flash);
        adapter=new Adapter(getActivity(),telPeopleList);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);//开启刷新
                //重新获取数据
                //获取完成swipeRefreshLayout.setRefreshing(false);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zjccc","刷新的我被执行了");
                        swipeRefreshLayout.setRefreshing(false);//停止刷新
                        Toast.makeText(getContext(),"success",Toast.LENGTH_SHORT).show();
                        news_loadMore();//写入你要加载的数据的方法
                    }
                },2000);
            }
        });
        adapter.setOnLongClick(new Adapter.OnLongClick() {
            @Override
            public void onItemLongClick(View view, int position) {

                delete(position);
            }
        });
        adapter.setOnClick(new Adapter.OnCLick() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCLick(int position) {
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                View vieww = manager.getChildAt(position);
                Adapter.MyviewHolder holder = (Adapter.MyviewHolder) recyclerView.getChildViewHolder(vieww);
                holder.imageView.setImageResource(R.drawable.message3);
                //第一个参数是对方的id 后一个是自己的id
                chat(telPeopleList.get(position).getName(),EMClient.getInstance().getCurrentUser(), position,  telPeopleList.get(position).getSid());
            }
        });
        adapter.notifyItemRangeInserted(telPeopleList.size(),1);
        aname=EMClient.getInstance().getCurrentUser();//显示扥路者信息
        message=view.findViewById(R.id.message);//图片信息
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panduan();
            }
        });
    }
   void  news_loadMore(){

       telPeopleList=new ArrayList<>();
           new Thread() {
               @Override
               public void run() {
                   super.run();
                   try {
                       //拿到好友列表
                       List<String> list = EMClient.getInstance().contactManager().getAllContactsFromServer();

                       ridd="";
                       for(String l:list) {
                           ridd+=l+",";
                       }

                       //询问网络
                       allFriendd.findAllFriend(ridd);
                   } catch (HyphenateException e) {
                       e.printStackTrace();
                   }
               }
           }.start();


   }
    @Override public void onResume() {
        super.onResume();
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    @Override public void onStop() {
        super.onStop();
        // 移除消息监听
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }
    @Override
    public void onStart() {
        super.onStart();
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
        Log.i("zjc","onStart被执行了");
      //  adapter.notifyItemRangeInserted(telPeopleList.size(),1);
        if(startInformation.panduan){
            panduan();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addTel:
                addFriend();
                break;
        }
    }
    Handler messageHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x17){
                Log.i("zjc","one");
          //    changeImage(idd);
            }else if(msg.what==0x18){
                int s=(int)msg.obj;
                changeImage(s);
            }
        }
    };

    @Override
    public void onMessageReceived(List<EMMessage> listt) {
        String idd=listt.get(0).getMsgId();//524769218277677044
        String iidtwo=listt.get(0).getUserName();//发送消息人的id
        Log.i("zjcid",iidtwo);
        for(int i=0;i<list.size();i++){

            if(list.get(i).equals(iidtwo)){
                Message message=new Message();
                message.obj=i;
                message.what=0x18;

                messageHandler.sendMessage(message);
                break;
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    private class MyContentListener implements EMContactListener{

        @Override
        public void onContactAdded(String s) {
            Log.i("zjc","增加了联系人"+s);
            mhandler.sendEmptyMessage(0x14);
        }
        @Override
        public void onContactDeleted(String s) {
            Log.i("zjc","删除了联系人");
        }

        @Override
        public void onContactInvited(String s, String s1) {
            Log.i("zjc","收到"+s+"的好友邀请。"+"理由:"+s1);
            startInformation.whyy=s1;
            Message message=new Message();
            message.obj=s;
            message.what=12;
            mhandler.sendMessage(message);
        }
        @Override
        public void onFriendRequestAccepted(String s) {
            Log.i("zjc","你被同意了"+s);

            mhandler.sendEmptyMessage(0x16);
        }
        @Override
        public void onFriendRequestDeclined(String s) {
            Log.i("zjc","你被拒绝了");
            startInformation.jujue="1";
            mhandler.sendEmptyMessage(0x17);
        }
    }
    void addFriend(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);
        final AlertDialog dialog=builder.show();
        final EditText editText=view.findViewById(R.id.telNumber);//输入被加者的号码
        final EditText editText2=view.findViewById(R.id.why);//申请理由
        view.findViewById(R.id.telSure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(editText.getText().toString())){
                    try {
                        startInformation.userNumber=editText.getText().toString();
                        startInformation.userName=" ";
                        startInformation.whyy=editText2.getText().toString();
                        //添加好友
                        EMClient.getInstance().contactManager().addContact(startInformation.userNumber, startInformation.whyy);
                        Log.i("zjc","被加者的id"+startInformation.userNumber);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(getActivity(),"不能为空",Toast.LENGTH_LONG).show();
                }
            }
        });
        view.findViewById(R.id.canel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }
    void panduan(){
        message.setImageResource(R.drawable.message3);
        if(startInformation.jujue=="1"){
            startInformation.jujue="0";
            refuge();
        } else if (startInformation.message=="5"){
            startInformation.message="0";
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.panduan, null);
            builder.setView(view);
            final AlertDialog dialog = builder.show();
            final Button tongyi = view.findViewById(R.id.tongyi);
            final Button butongyi = view.findViewById(R.id.butongyi);
            final TextView askreson = view.findViewById(R.id.askreson);
            final TextView askpeopled=view.findViewById(R.id.askpeopled);
            askpeopled.setText(startInformation.userNumber);
            askreson.setText(startInformation.whyy.toString());
            tongyi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.i("zjc", startInformation.userNumber);
                        startInformation.panduan = false;
                        EMClient.getInstance().contactManager().acceptInvitation(startInformation.userNumber);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            });
            butongyi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startInformation.panduan = false;
                        EMClient.getInstance().contactManager().declineInvitation(startInformation.userNumber);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            });
        }
    }
    //加好友拒绝
    void refuge(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.refuge,null);
        builder.setView(view);
        final AlertDialog dialog=builder.show();
    }
    //是否要删除好友
    void delete(final int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        //设置参数
        builder.setTitle("是否删除好友").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    ECApplication.list.remove(position);
                    Log.i("zjc","recyclevewi点击了"+position+"");
                    Log.i("zjc","你是否删除好友："+telPeopleList.get(position).getName());
                     EMClient.getInstance().contactManager().deleteContact(telPeopleList.get(position).getName());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                telPeopleList.remove(position);
                adapter.notifyItemRemoved(position);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
    //开始聊天 第一个参数发起聊天这的id 第二个参数 当前用户的id
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void chat(String taNName, String meNumber,int position,String nameeeee){
        Log.i("friend","ECMPlication里的所有人的总数："+ECApplication.list.size()+"");
     for(int i=0;i<ECApplication.list.size();i++){
         Log.i("friend",ECApplication.list.get(i)+"        ");
     }
        Log.i("friend","聊天的人的id："+taNName+"  自己的id"+meNumber+"  刚才点的位置："+position);
        if(!TextUtils.isEmpty(taNName)){
            if(taNName.equals(meNumber)){
                Toast.makeText(getActivity(), "不能和自己聊天", Toast.LENGTH_SHORT).show();
                return;
            }
            Fade fade=new Fade();
            fade.setDuration(1000);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setExitTransition(null);
                Intent intent=new Intent(getActivity(), EChatActivity.class);
                intent.putExtra("ec_chat_id",taNName);
                intent.putExtra("beizhu",nameeeee);
                Log.i("friend","我来看看这个taNName是什么"+taNName+"");
                Bundle bundle=new Bundle();
                bundle.putInt("weizhi",position);
                Log.i("friend","我来看看这个position是什么"+position+"");
                intent.putExtras(bundle);
                ActivityOptions options =ActivityOptions.makeSceneTransitionAnimation(getActivity(),recyclerView,"one");
                startActivity(intent, options.toBundle());
            }
        } else {
            Toast.makeText(getActivity(), "Username 不能为空", Toast.LENGTH_LONG).show();
        }
    }
    //收好好友信息后改变图标样式
    void changeImage(int position){
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        View vieww = manager.getChildAt(position);
        Adapter.MyviewHolder holder = (Adapter.MyviewHolder) recyclerView.getChildViewHolder(vieww);
        holder.imageView.setImageResource(R.drawable.message4);
    }
}
