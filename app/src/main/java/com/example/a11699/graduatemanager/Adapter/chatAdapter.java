package com.example.a11699.graduatemanager.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a11699.graduatemanager.ECApplication;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.circle.CirvleImageView;
import com.example.a11699.graduatemanager.lei.chatinformation;
import com.example.a11699.graduatemanager.utils.PopWindow_image;
import com.facebook.drawee.view.DraweeView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.util.ImageUtils;

import net.tsz.afinal.FinalBitmap;

import java.io.File;
import java.util.List;

public class chatAdapter extends RecyclerView.Adapter {
    private static final int TYPE_LEFT=0;//左边item
    private static final int TYPE_RIGHT=1;//右边item
    private Context context;
    List<EMMessage> emMessages;
    private FinalBitmap fb;//显示图片
    private String name;//当前聊天的人
    EMMessage message;
    int renidl;
    public chatAdapter(List<EMMessage> emMessages, Context context,String name,int renidl) {
        this.emMessages = emMessages;
        this.context = context;
        this.fb=FinalBitmap.create(context);
        this.name=name;
        this.renidl=renidl;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==TYPE_LEFT){
             View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leftchat,null);
            leftChatViewholder holder=new leftChatViewholder(view);
            return  holder;
         }else if(i==TYPE_RIGHT){
            View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rightchat,null);
            rightChatViewHolder holder=new rightChatViewHolder(view);
            return  holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
       final EMMessage message=emMessages.get(i);
        Log.i("zjc","消息总数"+emMessages.size());
        if(viewHolder instanceof leftChatViewholder){
            ((leftChatViewholder)viewHolder).lef_saq.setImageBitmap(ECApplication.list.get(renidl));
             if(message.getType()==EMMessage.Type.TXT){
                 String mess=((EMTextMessageBody)message.getBody()).getMessage();
                 Log.i("zjc","Adapter答应数据“："+mess);
                 ((leftChatViewholder)viewHolder).shoudaoMessage.setText(mess);
             }else if(message.getType()==EMMessage.Type.IMAGE){
                 Log.i("zjc","收到的是图片");
                 ((leftChatViewholder)viewHolder).shoudaoMessage.setVisibility(View.GONE);
                 ((leftChatViewholder)viewHolder).leftimage.setVisibility(View.VISIBLE);
                 String ThumbnailUrl = ((EMImageMessageBody) message.getBody()).getThumbnailUrl(); // 获取缩略图片地址
                 String thumbnailPath = ImageUtils.getScaledImage(context,ThumbnailUrl);
                 String imageRemoteUrl = ((EMImageMessageBody) message.getBody()).getRemoteUrl();// 获取远程原图片地址
                 fb.display( ((leftChatViewholder)viewHolder).leftimage,thumbnailPath);//显示图片
                 imageClick( ((leftChatViewholder)viewHolder).leftimage, imageRemoteUrl);//图片添加监听
             }else if(message.getType()==EMMessage.Type.VOICE){//语音消息
                 ((leftChatViewholder)viewHolder).shoudaoMessage.setVisibility(View.GONE);
                 ((leftChatViewholder)viewHolder).shoudaoMessage.setGravity(Gravity.TOP);
                 ((leftChatViewholder)viewHolder).leftimage.setVisibility(View.VISIBLE);
                 ((leftChatViewholder)viewHolder).shoudaoMessage.setText(((EMVoiceMessageBody) message.getBody()).getLength() + "”"); // 设置语音的时长
                 // 设置为语音的图片
                    ((leftChatViewholder)viewHolder).leftimage.setImageResource(R.drawable.chatfrom_voice_playing);
                 ((leftChatViewholder)viewHolder).leftimage.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View arg0) {
                         // 开始播放录音
                         startPlay(message,  ((leftChatViewholder)viewHolder).leftimage);
                     }
                 });
             }

         }else{
            ((rightChatViewHolder)viewHolder).right_saq.setImageBitmap(ECApplication.getPersonImage());
            if(message.getType()==EMMessage.Type.TXT) {
              //  ((rightChatViewHolder) viewHolder).fasongmessage.setVisibility(View.VISIBLE);
                String mess = ((EMTextMessageBody) message.getBody()).getMessage();
                Log.i("zjc","在Adapter显示发送的文字数据"+mess);
                ((rightChatViewHolder) viewHolder).fasongmessage.setText(mess);
            }else if(message.getType()==EMMessage.Type.IMAGE) {
                ((rightChatViewHolder)viewHolder).fasongmessage.setVisibility(View.GONE);
                ((rightChatViewHolder)viewHolder).rightImage.setVisibility(View.VISIBLE);
                // 自己发的消息
                String LocalUrl = ((EMImageMessageBody) message.getBody()).getLocalUrl(); // 获取本地图片地址
                Bitmap bm = ImageUtils.decodeScaleImage(LocalUrl,160,160);//获取缩略图
                ((rightChatViewHolder)viewHolder).rightImage.setImageBitmap(bm);//显示图片
                Log.e("zjc","bm="+bm+"==LocalUrl="+LocalUrl);
                imageClick(((rightChatViewHolder)viewHolder).rightImage, LocalUrl);//图片添加监听
            }
                else if(message.getType()==EMMessage.Type.VOICE){//语音消息
                Log.i("zjc","更新语音消息");
                ((rightChatViewHolder)viewHolder).fasongmessage.setVisibility(View.GONE);
                ((rightChatViewHolder)viewHolder).fasongmessage.setGravity(Gravity.TOP);
                ((rightChatViewHolder)viewHolder).rightImage.setVisibility(View.VISIBLE);
                ((rightChatViewHolder)viewHolder).fasongmessage.setText(((EMVoiceMessageBody) message.getBody()).getLength() + "”"); // 设置语音的时长
                    // 设置为语音的图片
                        ((rightChatViewHolder)viewHolder).rightImage.setImageResource(R.drawable.chatto_voice_playing);
                ((rightChatViewHolder)viewHolder).rightImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            // 开始播放录音
                            startPlay(message,  ((rightChatViewHolder)viewHolder).rightImage);
                        }
                    });
                }


         }
    }
private void imageClick(ImageView imageView,final String imageUrl){
    imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //new PopWindow_Image(Chat.this, imageUrl).showAtLocation(arg0, 0, 0, 0);
            new PopWindow_image(fb,context,imageUrl).showAtLocation(v,0,0,0);
        }
    });
}
    @Override
    public int getItemCount() {
        return emMessages.size();
    }
    public class leftChatViewholder extends RecyclerView.ViewHolder{
          TextView shoudaoMessage;
          DraweeView leftimage;
          CirvleImageView lef_saq;
        public leftChatViewholder(@NonNull View itemView) {
            super(itemView);
            shoudaoMessage=itemView.findViewById(R.id.shoudaoMessage);
            leftimage=itemView.findViewById(R.id.leftimage);
            lef_saq=itemView.findViewById(R.id.lef_saq);
        }
    }
    public class rightChatViewHolder extends RecyclerView.ViewHolder{
          TextView fasongmessage;
          DraweeView rightImage;
          CirvleImageView right_saq;
        public rightChatViewHolder(@NonNull View itemView) {
            super(itemView);
            fasongmessage=itemView.findViewById(R.id.fasongmessage);
            rightImage=itemView.findViewById(R.id.rightImage);
            right_saq=itemView.findViewById(R.id.right_saq);

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
          message = emMessages.get(position);//每条消息
        if(message.getFrom().equals(name)){
            //是自己发送的消息的时候
            return TYPE_LEFT;
        }else{
            return TYPE_RIGHT;
        }
    }
    // 开始播放
    private void startPlay(final EMMessage message, ImageView image) {

        EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) message.getBody();

        if (message.direct() == EMMessage.Direct.SEND) {
            // for sent msg, we will try to play the voice file directly
            playVoice(voiceBody.getLocalUrl(), message, image);
        } else {
            if (message.status() == EMMessage.Status.SUCCESS) {
                playVoice(voiceBody.getLocalUrl(), message, image);

            } else if (message.status() == EMMessage.Status.INPROGRESS) {
                //toast("信息还在发送中");
                Toast.makeText(context,"信息还在发送中",Toast.LENGTH_SHORT).show();
            } else if (message.status() == EMMessage.Status.FAIL) {
                // toast("接收失败");
                Toast.makeText(context,"接收失败",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String playMsgId = null;// 正在播放的语音信息id,用于判断播放的是哪一个语音信息
    private MediaPlayer mPlayer = null;// 播放语音的对象（播放器）
    public static boolean isPlaying = false;
    private ImageView playIv;
    private AnimationDrawable voiceAnimation = null;
    // 播放录音
    public void playVoice(String filePath, final EMMessage message, final ImageView image) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.err.println("file not exist");
            // toast("语音文件不存在");
            return;
        }
        playMsgId = message.getMsgId();
        mPlayer = new MediaPlayer();

        try {
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    if (mPlayer == null) // 表示因为要播放其他语音时已经被停止了,所以不需要再次调用停止
                        return;
                    stopPlayVoice(message); // stop animation
                }
            });
            isPlaying = true;
            mPlayer.start();
            playIv = image;
            showAnimation(message);

        } catch (Exception e) {
        }
    }
    public void stopPlayVoice(final EMMessage message) {
        voiceAnimation.stop();
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            playIv.setImageResource(R.drawable.chatfrom_voice_playing);
        } else {
            playIv.setImageResource(R.drawable.chatto_voice_playing);
        }
        // stop play voice
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        isPlaying = false;
        playMsgId = null;
         notifyDataSetChanged();
    }
    // show the voice playing animation
    private void showAnimation(final EMMessage message) {

        // play voice, and start animation
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            playIv.setImageResource(R.drawable.animalt);
        } else {
            playIv.setImageResource(R.drawable.animal);
        }
        voiceAnimation = (AnimationDrawable) playIv.getDrawable();
        voiceAnimation.start();
    }
}
