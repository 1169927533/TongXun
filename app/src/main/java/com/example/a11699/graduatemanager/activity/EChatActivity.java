package com.example.a11699.graduatemanager.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a11699.graduatemanager.Adapter.chatAdapter;
import com.example.a11699.graduatemanager.ECApplication;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.gaoxiao;
import com.example.a11699.graduatemanager.lei.chatinformation;
import com.example.a11699.graduatemanager.utils.JsonParser;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.VoiceRecorder;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class EChatActivity extends BaseActivity implements EMMessageListener {
    //标题
    private TextView duixiang;
    // 聊天信息输入框
    private EditText mInputEdit;
    // 发送按钮
    private Button mSendBtn;
    private ImageView fasongtupian,fasongtalk,biaoqing;//图片按钮
    private RecyclerView ec_layout_input;
    //显示聊天界面适配器
    chatAdapter adapter;
    // 消息监听器
    private EMMessageListener mMessageListener;
    // 当前聊天的 ID
    private String mChatId;
    //账户人
    private String mzhuren = EMClient.getInstance().getCurrentUser();
    // 当前会话对象
    private EMConversation mConversation;
    private ImageView back_chat, backhistory, chat_yuying;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    protected static final int REQUEST_CODE_LOCAL = 3;
    List<EMMessage> listt = new ArrayList<>();//存放聊天信息的
    private VoiceRecorder voiceRecorder;// 环信封装的录音功能类
    private MediaPlayer mPlayer = null;// 播放语音的对象（播放器）
    private AnimationDrawable voiceAnimation = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echat);
        View devorView = getWindow().getDecorView();
        View contentView = findViewById(Window.ID_ANDROID_CONTENT);
        devorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(devorView, contentView));
        // 获取当前会话的username(如果是群聊就是群id)
        mChatId = getIntent().getStringExtra("ec_chat_id");
        mMessageListener = this;
        initConversation();
        initView();
        initSpeech();
    }

    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }

    /**
     * 初始化界面
     */
    private void initView() {
        Log.i("zjc", "对话人的id:" + mChatId);
        biaoqing=findViewById(R.id.biaoqing);//发表情
        fasongtalk=findViewById(R.id.fasongtalk);//发送长语音
        fasongtupian = findViewById(R.id.fasongtupian);//发送图片信息
        chat_yuying = findViewById(R.id.chat_yuying);//发送语音
        duixiang = findViewById(R.id.duixiang);//发送文字信息
        duixiang.setText(mChatId);//聊天界面的头部信息
        mInputEdit = (EditText) findViewById(R.id.ec_edit_message_input);
        mSendBtn = (Button) findViewById(R.id.ec_btn_send);
        ec_layout_input = (RecyclerView) findViewById(R.id.ec_text_content);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ec_layout_input.setLayoutManager(linearLayoutManager);
        /**
         * 这里可以设置进入聊天界面的时候展示多少条聊天信息
         */
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mChatId);
        listt = conversation.getAllMessages();


        adapter = new chatAdapter(listt, this, mChatId);
        ec_layout_input.scrollToPosition(adapter.getItemCount() - 1);
        ec_layout_input.setAdapter(adapter);
      /*  ec_layout_input.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(bottom<oldBottom){
                    ec_layout_input.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ec_layout_input.scrollToPosition(adapter.getItemCount()-1);
                        }
                    },100);
                }
            }
        });
        */
        fasongtupian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择图片
                choosepicture();
            }
        });
        //录音时的动画
        voiceRecorder=new VoiceRecorder(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //切换msg的动画
                fasongtalk.setBackgroundResource(R.drawable.record_animate_01);
            }
        });

        //
        fasongtalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送语音
                fasongtalk.setBackgroundResource(R.drawable.record_animate_01);
            }
        });
        fasongtalk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_DOWN == event.getAction()){
                    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                        Toast.makeText(getApplication(),"内存卡不存在",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    v.setPressed(true);
                    try {
                        voiceRecorder.startRecording(null,mChatId,getApplicationContext());
                    }catch (Exception e){
                        e.printStackTrace();
                        v.setPressed(false);
                        Toast.makeText(getApplication(),"录音失败，请重试",Toast.LENGTH_SHORT).show();
                        if (voiceRecorder!=null){
                            voiceRecorder.discardRecording();
                        }
                    }
                }else if (MotionEvent.ACTION_MOVE == event.getAction()){

                    if(event.getY()<0){
                        Toast.makeText(getApplication(),"松开手指,取消发送",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplication(),"手指上滑,取消发送",Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }else if (MotionEvent.ACTION_UP ==event.getAction()){

                    v.setPressed(false);
                    if (event.getY()<0){//如果已经上滑后
                        voiceRecorder.discardRecording();
                        return true;
                    }
                    try {
                        int length = voiceRecorder.stopRecoding(); // 停止录音
                        if (length > 0) {

                            EMMessage message = EMMessage.createVoiceSendMessage(voiceRecorder.getVoiceFilePath(), length, mChatId);
                            EMClient.getInstance().chatManager().sendMessage(message);
                            fasongtalk.setBackgroundResource(R.drawable.record_animate_01);

                            Message msg3 = mHandler.obtainMessage();
                            msg3.what = 3;
                            mHandler.sendMessage(msg3);

                        } else if (length == EMError.FILE_INVALID) {
                            Toast.makeText(getApplication(),"无录音权限",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplication(),"录音时间太短",Toast.LENGTH_SHORT).show();

                        }
                    }catch (Exception e){

                    }

                }

                return false;

            }
        });
        back_chat = findViewById(R.id.chat_back);
        backhistory = findViewById(R.id.chat_history);
        // 设置textview可滚动，需配合xml布局设置
        //   mContentText.setMovementMethod(new ScrollingMovementMethod());
        //查看聊天记录
        backhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentt = new Intent(EChatActivity.this, allHistory.class);
                intentt.putExtra("sa", mChatId);
                startActivity(intentt);
            }
        });
        //返回按钮
        back_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //语音
        chat_yuying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechDialog();
            }
        });
        // 设置发送按钮的点击事件
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mInputEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    mInputEdit.setText("");
                    // 创建一条新消息，第一个参数为消息内容，第二个为接受者username
                    EMMessage message = EMMessage.createTxtSendMessage(content, mChatId);
                    // 将新的消息内容和时间加入到下边
                    // 调用发送消息的方法
                    EMClient.getInstance().chatManager().sendMessage(message);
                    // 为消息设置回调
                    message.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            // 消息发送成功，打印下日志，正常操作应该去刷新ui
                            Log.i("lzan13", "send message on success");

                        }

                        @Override
                        public void onError(int i, String s) {
                            // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                            Log.i("lzan13", "send message on error " + i + " - " + s);
                        }

                        @Override
                        public void onProgress(int i, String s) {
                            // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
                        }
                    });
                    fasong(content);
                }
            }
        });

        biaoqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent intent=new Intent(EChatActivity.this, gaoxiao.class);
                //startActivity(intent);
            }
        });

    }

    private void initSpeech() {
        // 将“12345678”替换成您申请的 APPID，申请地址： http://www.xfyun.cn
        // 请勿在 “ =”与 appid 之间添加任务空字符或者转义符

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5bbd99d9");
    }

    /**
     * 初始化会话对象，并且根据需要加载更多消息
     */
    private void initConversation() {

        /**
         * 初始化会话对象，这里有三个参数么，
         * 第一个表示会话的当前聊天的 useranme 或者 groupid
         * 第二个是绘画类型可以为空
         * 第三个表示如果会话不存在是否创建
         */
        mConversation = EMClient.getInstance().chatManager().getConversation(mChatId, null, true);
        // 设置当前会话未读数为 0
        mConversation.markAllMessagesAsRead();
        int count = mConversation.getAllMessages().size();
        if (count < mConversation.getAllMsgCount() && count < 20) {
            // 获取已经在列表中的最上边的一条消息id
            String msgId = mConversation.getAllMessages().get(0).getMsgId();
            // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
            mConversation.loadMoreMsgFromDB(msgId, 20 - count);
        }

    }

    /**
     * 自定义实现Handler，主要用于刷新UI操作
     */

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    EMMessage message = (EMMessage) msg.obj;
                    // 这里只是简单的demo，也只是测试文字消息的收发，所以直接将body转为EMTextMessageBody去获取内容
                    EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                    // 将新的消息内容和时间加入到下边
                    shoudao();
                    break;
                case 2:
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mChatId);
                    listt.add(conversation.getLastMessage());
                    adapter.notifyItemRangeChanged(mConversation.getAllMessages().size(), 1);
                    break;
                case 3:
                    Log.i("zjc","发送语音");
                    EMConversation conversationn = EMClient.getInstance().chatManager().getConversation(mChatId);
                    listt.add(conversationn.getLastMessage());
                    adapter.notifyItemRangeChanged(mConversation.getAllMessages().size(), 1);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 移除消息监听
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }
    /**
     * --------------------------------- Message Listener -------------------------------------
     * 环信消息监听主要方法
     */
    /**
     * 收到新消息
     *
     * @param list 收到的新消息集合
     */
    @Override
    public void onMessageReceived(List<EMMessage> list) {
        // 循环遍历当前收到的消息
        for (EMMessage message : list) {
            Log.i("lzan13", "收到新消息:" + message);
            if (message.getFrom().equals(mChatId)) {
                // 设置消息为已读
                mConversation.markMessageAsRead(message.getMsgId());
                // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = message;
                mHandler.sendMessage(msg);
            } else {
                // TODO 如果消息不是当前会话的消息发送通知栏通知
            }
        }
    }

    /**
     * 收到新的 CMD 消息
     */
    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        for (int i = 0; i < list.size(); i++) {
            // 透传消息
            EMMessage cmdMessage = list.get(i);
            EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
            Log.i("lzan13", "收到 CMD 透传消息" + body.action());
        }
    }

    /**
     * 收到新的已读回执
     *
     * @param list 收到消息已读回执
     */
    @Override
    public void onMessageRead(List<EMMessage> list) {
    }

    /**
     * 收到新的发送回执
     * TODO 无效 暂时有bug
     *
     * @param list 收到发送回执的消息集合
     */
    @Override
    public void onMessageDelivered(List<EMMessage> list) {
    }

    /**
     * 消息撤回回调
     *
     * @param list 撤回的消息列表
     */
    @Override
    public void onMessageRecalled(List<EMMessage> list) {
    }

    /**
     * 消息的状态改变
     *
     * @param message 发生改变的消息
     * @param object  包含改变的消息
     */
    @Override
    public void onMessageChanged(EMMessage message, Object object) {
    }

    //更新数据
    void fasong(String xiao) {
        Log.i("zjc", xiao + "發送消息");
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mChatId);
        listt.add(conversation.getLastMessage());
        adapter.notifyItemRangeChanged(mConversation.getAllMessages().size(), 1);
    }

    void shoudao() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mChatId);
        listt.add(conversation.getLastMessage());
        adapter.notifyItemRangeChanged(mConversation.getAllMessages().size(), 1);
    }
//更新图片
    void tu(){

    }

    private void startSpeechDialog() {
        //1. 创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener());
        //2. 设置accent、 language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");// 设置中文
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 若要将UI控件用于语义理解，必须添加以下参数设置，设置之后 onResult回调返回将是语义理解
        // 结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口

        mDialog.setListener(new MyRecognizerDialogListener());
        mDialog.show();

        //showTip(getString(R.string.te));
        TextView txt = mDialog.getWindow().getDecorView().findViewWithTag("textlink");
        txt.setText("");
        //4. 显示dialog，接收语音输入
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener {

        /**
         * @param results
         * @param isLast  是否说完了
         */
        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String result = results.getResultString(); //为解析的
            showTip(result);
            System.out.println(" 没有解析的 :" + result);

            String text = JsonParser.parseIatResult(result);//解析过后的
            System.out.println(" 解析后的 :" + text);

            String sn = null;
            // 读取json结果中的 sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);//没有得到一句，添加到

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }

            mInputEdit.setText(resultBuffer.toString());// 设置输入框的文本
            mInputEdit.setSelection(mInputEdit.length());//把光标定位末尾
        }

        @Override
        public void onError(SpeechError speechError) {

        }
    }

    class MyInitListener implements InitListener {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败 ");
            }

        }
    }

    private void showTip(String data) {
        //    Toast.makeText(this, data, Toast.LENGTH_SHORT).show();

    }

    private int ii = 6;

    //获取最新的五条数据
    void huoqu() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mChatId);
//获取此会话的所有消息
        List<EMMessage> messages = conversation.getAllMessages();
        List<chatinformation> list1 = new ArrayList<>();
        int sa = messages.size();
        while (ii != 0) {
            ii--;
            if (sa > ii) {
                EMTextMessageBody body = (EMTextMessageBody) messages.get(sa - ii - 1).getBody();
                if (messages.get(sa - ii - 1).getFrom().equals(mChatId)) {
                    chatinformation chatinformationnb = new chatinformation(body.getMessage(), true);
                    //  list.add(chatinformationnb);
                } else {
                    chatinformation chatinformationnb = new chatinformation(body.getMessage(), false);
                    //   list.add(chatinformationnb);
                }

            } else {
                break;
            }
        }
        ii = 6;
        adapter.notifyDataSetChanged();
    }

    //选择发送的图片
    void choosepicture() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_LOCAL) {//发送图片
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);//发送图片
                    }
                }

            }

        }
    }

    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getApplication().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(getApplication(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(getApplication(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendImageMessage(file.getAbsolutePath());
        }

    }

    /**
     * 发送图片
     *
     * @param imagePath
     */
    protected void sendImageMessage(String imagePath) {
        Log.i("zjc", "发送了一张图片数据");
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, mChatId);
        if (message == null) {
            return;
        }
        EMClient.getInstance().chatManager().sendMessage(message);
        Message msg2 = mHandler.obtainMessage();
        msg2.what = 2;
        mHandler.sendMessage(msg2);
    }
    void talk(){

    }
}
