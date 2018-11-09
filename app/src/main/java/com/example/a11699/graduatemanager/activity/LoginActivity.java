package com.example.a11699.graduatemanager.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import  android.support.design.widget.TabLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.a11699.graduatemanager.ECApplication;
import com.example.a11699.graduatemanager.MyApplication;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.askhttpDaoImp.userlogin;
import com.example.a11699.graduatemanager.utils.MathTools;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {
    private ProgressDialog mDialog;
    private EditText ed_username ,ed_password;
    private Button subbmit;
    private TextView zhuce;
    private Context context;
    userlogin userlogin=new userlogin(this);
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    String nam,passworddd;

    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref= PreferenceManager.getDefaultSharedPreferences(getApplication());
        editor=pref.edit();
        initView();
    }
    void initView(){
        fab=findViewById(R.id.fabbb);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                /**
                 *  共享一个元素的时候
                 *
                 * 第一个参数  activity
                 * 第二个参数  View sharedElement 共享的元素
                 * 第三个参数  sharedElementName  在xml中指定的共享元素的名字
                 */
                getWindow().setExitTransition(null);//Activity过渡动画
                getWindow().setEnterTransition(null);//Activity过渡动画
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //这个是另外一种形式的过度动画
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, fab, fab.getTransitionName());
                    startActivity(new Intent(LoginActivity.this, adduserActivity.class), options.toBundle());

                } else {
                    startActivity(new Intent(LoginActivity.this, adduserActivity.class));

                }
            }
        });
        ed_username=findViewById(R.id.et_username);
        ed_password=findViewById(R.id.et_password);
        subbmit=findViewById(R.id.bt_go);
        zhuce=findViewById(R.id.zhuce);
        subbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog=new ProgressDialog(LoginActivity.this);
                mDialog.setMessage("正在登录，请稍等");
                mDialog.show();
                String username=ed_username.getText().toString().trim();
                String password=ed_password.getText().toString().trim();
                nam=username;
                passworddd=password;
                String token= MathTools.getMd5(password);//将密码加密
                //都一个自己的服务器 第二个环信的
                 userlogin.loginValidate(username,token);
                //singnIn();
            }
        });
        zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //singup(nam,passworddd);
                Intent intent=new Intent(LoginActivity.this,adduserActivity.class);
                startActivity(intent);

            }
        });
    }
    void singup(final String name, final String password){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String username=ed_username.getText().toString().trim();
                    final String password=ed_password.getText().toString().trim();
                    //注册用户
                    EMClient.getInstance().createAccount(username,password);
                    runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run() {
                          //  Toast.makeText(LoginActivity.this, "稍", Toast.LENGTH_LONG).show();
                            if (!LoginActivity.this.isFinishing()) {
                                mDialog.dismiss();
                               // tianxieinformation(name,password);
                            }
                            tianxieinformation(name,password);
                        }
                    });

                } catch (final HyphenateException e) {
                    e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override public void run() {
                                    if (!LoginActivity.this.isFinishing()) {
                                        // mDialog.dismiss();
                                    }
                                    /**
                                     * 关于错误码可以参考官方api详细说明
                                     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                                     */
                                    int errorCode = e.getErrorCode();
                                    String message = e.getMessage();
                                    Log.d("lzan13",
                                            String.format("sign up - errorCode:%d, errorMsg:%s", errorCode,
                                            e.getMessage()));
                            switch (errorCode) {
                                // 网络错误
                                case EMError.NETWORK_ERROR:
                                    Toast.makeText(LoginActivity.this,
                                            "网络错误 code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                                // 用户已存在
                                case EMError.USER_ALREADY_EXIST:
                                denglu(name,password);
                                    break;
                                // 参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册
                                case EMError.USER_ILLEGAL_ARGUMENT:
                                    Toast.makeText(LoginActivity.this,
                                            "参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册 code: "
                                                    + errorCode
                                                    + ", message:"
                                                    + message, Toast.LENGTH_LONG).show();
                                    break;
                                // 服务器未知错误
                                case EMError.SERVER_UNKNOWN_ERROR:
                                    Toast.makeText(LoginActivity.this,
                                            "服务器未知错误 code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                                case EMError.USER_REG_FAILED:
                                    Toast.makeText(LoginActivity.this,
                                            "账户注册失败 code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(LoginActivity.this,
                                            "ml_sign_up_failed code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    });
                }
            }
        }).start();

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void tianxieinformation(String name, String password){
        fab.performClick();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==10){
            //
            Log.i("zjc","返回主函数");
            denglu(nam,passworddd);
        }
    }
/*
   void singnIn(){
        mDialog=new ProgressDialog(this);
        mDialog.setMessage("正在登录，请稍等");
        mDialog.show();
        final String username=ed_username.getText().toString().trim();
        final String password=ed_password.getText().toString().trim();
        //判断账户 密码是否为空
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        //进行登录
        EMClient.getInstance().login(username, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("zjc","登录成功");
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        mDialog.dismiss();//取消提示
                        //可以确保群 聊天消息都被加载完
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        ECApplication.setS_id(username);
                        ECApplication.setS_name(password);
                        editor.putString("stu_id",username);
                        editor.putString("stu_pa",password);
                        editor.apply();



                        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
            }

            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        mDialog.dismiss();
                        Log.d("lzan13", "登录失败 Error code:" + i + ", message:" + s);

                         * 关于错误码可以参考官方api详细说明
                         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html

                        switch (i) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                Toast.makeText(LoginActivity.this,
                                        "网络错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                Toast.makeText(LoginActivity.this,
                                        "无效的用户名 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                Toast.makeText(LoginActivity.this,
                                        "无效的密码 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                Toast.makeText(LoginActivity.this,
                                        "用户认证失败，用户名或密码错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG)
                                        .show();
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                Toast.makeText(LoginActivity.this,
                                        "用户不存在 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                Toast.makeText(LoginActivity.this,
                                        "无法访问到服务器 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                Toast.makeText(LoginActivity.this,
                                        "等待服务器响应超时 code: " + i + ", message:" + s, Toast.LENGTH_LONG)
                                        .show();
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                Toast.makeText(LoginActivity.this,
                                        "服务器繁忙 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                Toast.makeText(LoginActivity.this,
                                        "未知的服务器异常 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(LoginActivity.this,
                                        "ml_sign_in_failed code: " + i + ", message:" + s,
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
     */
    void denglu(final String name, final String password){
        //进行登录
        EMClient.getInstance().login(ed_username.getText().toString().trim(), ed_password.getText().toString(), new EMCallBack() {
            @Override
            public void onSuccess() {

                Log.i("zjc","登录成功");
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        ECApplication.setS_id(nam);
                        ECApplication.setS_name(password);
                        editor.putString("stu_id",name);
                        editor.putString("stu_pa",password);
                        editor.apply();




                         Log.i("zjc","登录成功二次检查");
                        //可以确保群 聊天消息都被加载完
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
            }

            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        mDialog.dismiss();
                        Log.d("lzan13", "登录失败 Error code:" + i + ", message:" + s);
                        /**
                         * 关于错误码可以参考官方api详细说明
                         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                         */
                        switch (i) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                Toast.makeText(LoginActivity.this,
                                        "网络错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                Toast.makeText(LoginActivity.this,
                                        "无效的用户名 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                Toast.makeText(LoginActivity.this,
                                        "无效的密码 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                Toast.makeText(LoginActivity.this,
                                        "用户认证失败，用户名或密码错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG)
                                        .show();
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                Toast.makeText(LoginActivity.this,
                                        "用户不存在 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                Toast.makeText(LoginActivity.this,
                                        "无法访问到服务器 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                Toast.makeText(LoginActivity.this,
                                        "等待服务器响应超时 code: " + i + ", message:" + s, Toast.LENGTH_LONG)
                                        .show();
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                Toast.makeText(LoginActivity.this,
                                        "服务器繁忙 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                Toast.makeText(LoginActivity.this,
                                        "未知的服务器异常 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(LoginActivity.this,
                                        "ml_sign_in_failed code: " + i + ", message:" + s,
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
    //成功的回调函数
    public void loginCallback(String name,String password){
        Log.i("zjc","dsadsada");
        singup(name,password);
    }
    //失败的回调函数
    public void loginBack(){
Looper.prepare();
        Toast.makeText(LoginActivity.this,"用户名/密码错误",Toast.LENGTH_LONG).show();
Looper.loop();
    }
    public  void shibai(){
        Looper.prepare();///
        Toast.makeText(LoginActivity.this,"网络访问失败",Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

}
