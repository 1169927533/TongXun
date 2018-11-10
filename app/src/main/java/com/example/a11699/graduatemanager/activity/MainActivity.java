package com.example.a11699.graduatemanager.activity;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a11699.graduatemanager.Adapter.MyPagerAdapter;
import com.example.a11699.graduatemanager.ECApplication;
import com.example.a11699.graduatemanager.Fragment.MineFragment;
import com.example.a11699.graduatemanager.Fragment.TalkFragment;
import com.example.a11699.graduatemanager.Fragment.WorkFragment;
import com.example.a11699.graduatemanager.MyApplication;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.askHttpDao.userImp;
import com.example.a11699.graduatemanager.askhttpDaoImp.userDaoImp;
import com.example.a11699.graduatemanager.circle.tools.DataCleanManager;
import com.example.a11699.graduatemanager.lei.userinformation;
import com.example.a11699.graduatemanager.utils.ExampleUtil;
import com.example.a11699.graduatemanager.utils.RealPathFromUriUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity {
    //声明控件
    private ViewPager view_pager;
    private TabLayout tab_layout;
    private List<String> tab_nameList;//标签栏的名字
    private List<Fragment> tab_viewList;//标签栏对应的页面
    private int[] tabIcon = {R.drawable.l1, R.drawable.m1, R.drawable.w1};
    private int[] tabClic={R.drawable.l2,R.drawable.m2,R.drawable.w2};
    Fragment view1;
    Fragment view2;
    Fragment view3;
    public static boolean isForeground = false;
    ImageView person;
    private Bitmap head;// 头像Bitma
    private SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View view;//头部视图
    private userImp userImpp=new userDaoImp(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref= PreferenceManager.getDefaultSharedPreferences( getApplication());
        editor=pref.edit();
        initView();//绑定控件
        initData();//初始化数据
        Log.i("zjc",EMClient.getInstance().getCurrentUser());
        JPushInterface.setAlias(this,1, EMClient.getInstance().getCurrentUser());
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                byte []Pi=(byte[])msg.obj;
                Bitmap bitmap= BitmapFactory.decodeByteArray(Pi,0,Pi.length);
                person=view.findViewById(R.id.ren_picturcc);
                ECApplication.setPersonImage(bitmap);
                person .setImageBitmap(bitmap);
            }
        }
    };
    private  void initView(){
        view_pager=findViewById(R.id.view_page);
        tab_layout=findViewById(R.id.tab_layout);
        drawerLayout=findViewById(R.id.activity_na);
        navigationView=findViewById(R.id.nav);
        view=navigationView.inflateHeaderView(R.layout.head);
        registerMessageReceiver();
        getin();
    }
    MenuItem ren_workname,ren_workplace,ren_workzhiwu;
    void getin(){
        userImpp.getUserInformation(EMClient.getInstance().getCurrentUser());
    }
    public void  caozuocehuacaidan( userinformation userinformationd)  {
        Menu menu=navigationView.getMenu();
          ren_workname=menu.findItem(R.id.ren_workname);
          ren_workplace=menu.findItem(R.id.ren_workplace);
          ren_workzhiwu=menu.findItem(R.id.ren_workzhiwu);
        MenuItem qiandaoo=menu.findItem(R.id.qiandaoo);
        MenuItem ren_clean=menu.findItem(R.id.ren_clean);
        MenuItem ren_teach=menu.findItem(R.id.ren_teach);
        MenuItem min_tel=menu.findItem(R.id.min_tel);
        final MenuItem ren_huancunbig=menu.findItem(R.id.ren_huancunbig);
        MenuItem ren_tuichu=menu.findItem(R.id.ren_tuichu);
        ren_workname.setTitle("  实习单位: "+userinformationd.getUnit());
        ren_workplace.setTitle("  实习地点: "+userinformationd.getCity());
        ren_workzhiwu.setTitle("  职务: "+userinformationd.getJob());
        qiandaoo.setTitle("  签到+记录: "+userinformationd.getQiandaoCount());
        ren_clean.setTitle("  清理缓存");
        ren_teach.setTitle("  指导老师："+userinformationd.getTutor());
        min_tel.setTitle("  电话：" +userinformationd.gettPhone());
        File file = new File(this.getCacheDir().getPath());
        try {
            ren_huancunbig.setTitle("  占用缓存: "+  DataCleanManager.getCacheSize(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ren_tuichu.setTitle("  退出登录");
        TextView textView=view.findViewById(R.id.ren_name);
        person=view.findViewById(R.id.ren_picturcc);
        textView.setText(userinformationd.getName());
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.qiandaoo:
                        qiandao();
                        break;
                    case R.id.ren_clean:
                        clean(ren_huancunbig);
                        break;
                    case R.id.ren_tuichu:
                        tuichu();
                        break;
                }
                return false;
            }
        });
        image(userinformationd.getPicture());
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zjc","sadaas");
                viugaiHead();
            }
        });
    }
    public void initData() {
        tab_nameList = new ArrayList<>();
        tab_nameList.add("讨论");
        tab_nameList.add("工作");
        // tab_nameList.add("我的");
        tab_layout.setTabMode(TabLayout.MODE_FIXED);
        tab_layout.addTab(tab_layout.newTab().setText(tab_nameList.get(0)));
        tab_layout.addTab(tab_layout.newTab().setText(tab_nameList.get(1)));
        //   tab_layout.addTab(tab_layout.newTab().setText(tab_nameList.get(2)));
        view1 = new TalkFragment();
        view2 = new WorkFragment();
        //  view3 = new MineFragment();
        tab_viewList = new ArrayList<>();
        tab_viewList.add(view1);
        tab_viewList.add(view2);
        //    tab_viewList.add(view3);
        //添加ViewPage的适配器
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), tab_viewList);
        view_pager.setAdapter(myPagerAdapter);
        tab_layout.setupWithViewPager(view_pager);
        tab_layout.getTabAt(0).setCustomView(getTabView(0));
        tab_layout.getTabAt(1).setCustomView(getTabView(1));

        LinearLayout linearLayout = (LinearLayout) tab_layout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.fengexian));

             // tab_layout.getTabAt(1).setCustomView(getTabView(1));
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view=tab.getCustomView();
                startShakeByPropertyAnim(view, 0.9f, 1.2f, 10f, 400);
                TextView textView=view.findViewById(R.id.teb_texsst);
                textView.setTextSize(18);
                ImageView imageView=view.findViewById(R.id.tab_immmmmg);
                imageView.setImageResource(tabClic[tab.getPosition()]);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view=tab.getCustomView();
                TextView textView=view.findViewById(R.id.teb_texsst);
                textView.setTextSize(12);
                ImageView imageView=view.findViewById(R.id.tab_immmmmg);
                imageView.setImageResource(tabIcon[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tab_layout.getTabAt(1).select();
    }
    void qiandao(){
        Intent intent=new Intent(this,qiandaoActivity.class);
        startActivity(intent);
    }
    void clean(final MenuItem ren_huancunbig){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        //设置参数
        builder.setTitle("是否清理缓存").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    DataCleanManager.cleanInternalCache(getApplicationContext());
                    findHuanSize(    ren_huancunbig);
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
    void findHuanSize(  MenuItem ren_huancunbig){
        //获得应用内部缓存(/data/data/com.example.androidclearcache/cache)
        File file = new File(this.getCacheDir().getPath());
        try {
            ren_huancunbig.setTitle(DataCleanManager.getCacheSize(file));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    void tuichu() {
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("lzan13", "logout success");
                // 调用退出成功，结束app
                exit();
                finish();
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
    public static void exit(){
        MyApplication.setS_id("");
        MyApplication.setS_name("");
        editor.clear();
        editor.apply();
        Intent intent=new Intent(ECApplication.getmContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ECApplication.getmContext().startActivity(intent);
    }
    public View getTabView(int position) {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_iteemm, null);
        TextView tab_title = view.findViewById(R.id.teb_texsst);
        ImageView imageView = view.findViewById(R.id.tab_immmmmg);
        tab_title.setText(tab_nameList.get(position));
        imageView.setImageResource(tabIcon[position]);
        return view;
    }
    private void startShakeByPropertyAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //TODO 验证参数的有效性

        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.a11699.graduatemanager.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private MessageReceiver mMessageReceiver;
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }

                }
            } catch (Exception e){
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void image(String url){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] Picture_bt=response.body().bytes();
                Message message=handler.obtainMessage();
                message.obj=Picture_bt;
                message.what=1;
                handler.sendMessage(message);
            }
        });
    }
    void viugaiHead(){
        Intent intent1 = new Intent(Intent.ACTION_PICK, null);
        //打开文件
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent1, 1);

    }
    Uri mUritempFile;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    //裁剪图片
                    crophoto(data.getData());
                }
                break;
            case 3:

                if (data != null) {
                    //将Uri图片转换为Bitmap
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream(mUritempFile));
                        //TODO，将裁剪的bitmap显示在imageview控件上
                        person.setImageBitmap(bitmap);// 用ImageButton显示出来
                       // setImageToHeadView(bitmap);
                        if (bitmap != null) {
                            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                            String realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(this, uri);
                            Log.i("zjc", realPathFromUri);
                            String baseEncode = getImageStr(realPathFromUri);
                            String dd[] = ren_workname.getTitle().toString().split(":| ");
                            String place[] = ren_workplace.getTitle().toString().split(":| ");
                            String d[] = ren_workzhiwu.getTitle().toString().split(":| ");
                             iinitData(EMClient.getInstance().getCurrentUser(),place[4],dd[4],d[4],baseEncode);
                             }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
              /*
                      //系统自带的方法
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    ECApplication.setPersonImage(head);
                    person.setImageBitmap(head);// 用ImageButton显示出来
                    if (head != null) {
                        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), head, null,null));
                     String  realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(this,uri);
                        Log.i("zjc", realPathFromUri);
                        String baseEncode = getImageStr(realPathFromUri);
                        String dd[]=ren_workname.getTitle().toString().split(":| ");;
                        String place[]=ren_workplace.getTitle().toString().split(":| ");
                        String d[]=ren_workzhiwu.getTitle().toString().split(":| ");
                         /**
                         * 上传服务器代码
                         iinitData(EMClient.getInstance().getCurrentUser(),place[4],dd[4],d[4],baseEncode);
                    }
                }
                */
                break;
        }
    }
    public static String getImageStr(String imgFile) {
        File param = new File(imgFile);
        Bitmap bitmap = BitmapFactory.decodeFile(param.getPath());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    //调用系统裁剪图片
    void crophoto(Uri uri){
       /*
           //系统自带的方法
      Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
        */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
//        intent.putExtra("return-data", true);      //原本的裁剪方式

        //uritempFile为Uri类变量，实例化uritempFile，转化为uri方式解决问题
         mUritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, 3);

    }
    //post请求网络数据
    private void iinitData(String xuehaoo,String workplacee,String gongsii,String zhuwuu,String baseEncode){
        //接口 
        String path = "http://i3c8x94lqb.51http.tech/TXGLwai/updateStudentInfo";
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("s_sid", xuehaoo)
                .add("s_city", workplacee)
                .add("s_company",gongsii)
                .add("s_job",zhuwuu)
                .add("image",baseEncode)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"网络连接失败,",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                try {
                    JSONObject jsonObject=new JSONObject(string);
                    if(jsonObject.getString("result").equals("1")){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"success,",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
