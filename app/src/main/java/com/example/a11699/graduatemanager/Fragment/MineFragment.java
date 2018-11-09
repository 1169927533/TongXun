package com.example.a11699.graduatemanager.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a11699.graduatemanager.ECApplication;
import com.example.a11699.graduatemanager.MyApplication;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.activity.LoginActivity;
import com.example.a11699.graduatemanager.activity.MainActivity;
import com.example.a11699.graduatemanager.activity.qiandaoActivity;
import com.example.a11699.graduatemanager.askHttpDao.userImp;
import com.example.a11699.graduatemanager.askhttpDaoImp.userDaoImp;
import com.example.a11699.graduatemanager.circle.tools.DataCleanManager;
import com.example.a11699.graduatemanager.lei.startInformation;
import com.example.a11699.graduatemanager.lei.userinformation;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private TextView ren_workname, ren_workplace, ren_workzhiwu, ren_qiandao, ren_clean, ren_teach, ren_huancunbig;
    private TextView ren_name,min_tel;
    private Button ren_tuichu;
    private LinearLayout qiandaoo;
    private View view;
    private SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorr;
    private userImp userImp=new userDaoImp(this);
    public MineFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        pref= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());
        editor=pref.edit();
        sharedPreferences =this.getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
        editorr=sharedPreferences.edit();
        init();
        getUserInformation();
        findHuanSize();
        return view;
    }
    void getUserInformation(){
        userImp.getUserInformation(EMClient.getInstance().getCurrentUser());
    }
    public void show(userinformation userinformation){
        ren_workname.setText(userinformation.getUnit());
       // ren_workplace.setText(userinformation.getUnit());
        ren_workzhiwu.setText(userinformation.getJob());
        ren_teach.setText(userinformation.getTutor());
        ren_qiandao.setText(userinformation.getQiandaoCount());
        min_tel.setText(userinformation.gettPhone());
    }
    void init() {
        ren_name = view.findViewById(R.id.ren_name);//账户人名子
        if(this.getActivity().getSharedPreferences("user",Context.MODE_PRIVATE).getString(EMClient.getInstance().getCurrentUser(),"").equals("")){
            ren_name.setText(EMClient.getInstance().getCurrentUser());
        }else{
            ren_name.setText(this.getActivity().getSharedPreferences("user",Context.MODE_PRIVATE).getString(EMClient.getInstance().getCurrentUser(),""));
        }
        min_tel=view.findViewById(R.id.min_tel);//老师电话
        qiandaoo=view.findViewById(R.id.qiandaoo);//签到按钮
        ren_workname = view.findViewById(R.id.ren_workname);//实习单位
        ren_workplace = view.findViewById(R.id.ren_workplace);//实习地点
        ren_workzhiwu = view.findViewById(R.id.ren_workzhiwu);//职务
        ren_qiandao = view.findViewById(R.id.ren_qiandao);//签到
        ren_clean = view.findViewById(R.id.ren_clean);//清理缓存
        ren_teach = view.findViewById(R.id.ren_teach);//指导老师
        ren_tuichu = view.findViewById(R.id.ren_tuichu);//退出登录
        ren_huancunbig = view.findViewById(R.id.ren_huancunbig);
        qiandaoo.setOnClickListener(this);//签到
        ren_clean.setOnClickListener(this);//清理垃圾
        ren_tuichu.setOnClickListener(this);//退出登录
        ren_name.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qiandaoo:
                qiandao();
                break;
            case R.id.ren_clean:
                 clean();
                break;
            case R.id.ren_tuichu:
                tuichu();
                break;
            case R.id.ren_name:
                xiugainame();
                break;
        }
    }
    void tuichu() {
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("lzan13", "logout success");
                // 调用退出成功，结束app
                exit();
                getActivity().finish();
            }

            @Override
            public void onError(int i, String s) {
                Log.i("lzan13", "logout error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
    void clean(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        //设置参数
        builder.setTitle("是否清理缓存").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    DataCleanManager.cleanInternalCache(getActivity().getApplicationContext());
                    findHuanSize();
                }catch (Exception e){
                    e.printStackTrace();

                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
    void findHuanSize(){
        //获得应用内部缓存(/data/data/com.example.androidclearcache/cache)
        File file = new File(getActivity().getCacheDir().getPath());
        try {
            ren_huancunbig.setText(DataCleanManager.getCacheSize(file));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    void xiugainame(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.xiugainame,null);
        builder.setView(view);
        final AlertDialog dialog=builder.show();
        final EditText editText=view.findViewById(R.id.ren_beizhu);//输入被加者的号码
        final Button button=view.findViewById(R.id.ren_queding);//输入被加者的备注
        final Button button1=view.findViewById(R.id.ren_quxiao);//申请理由
      button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(editText.getText().toString())){
                    String name=editText.getText().toString().trim();
                    Log.i("name",name);
                    editor.putString(EMClient.getInstance().getCurrentUser(),name);
                    editor.commit();
                    ren_name.setText(name);
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(getActivity(),"不能为空",Toast.LENGTH_LONG).show();
                }
            }
        });
      button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    void qiandao(){
         Intent intent=new Intent(getActivity(),qiandaoActivity.class);
        startActivity(intent);
    }
    public static void exit(){
        MyApplication.setS_id("");
        MyApplication.setS_name("");

        editor.clear();
        editor.apply();

        Intent intent=new Intent(ECApplication.getmContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ECApplication.getmContext().startActivity(intent);
    }
}
