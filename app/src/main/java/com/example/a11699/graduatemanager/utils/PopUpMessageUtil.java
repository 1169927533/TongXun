package com.example.a11699.graduatemanager.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.activity.messageActivity;
import com.facebook.drawee.view.DraweeView;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

public class PopUpMessageUtil {
    View rootview;
    private PopupWindow popupWindow;
    float animatorProperty[] = null;
    LinearLayout mpic1,mpic2,mpic3,mpic4  ;
    private ImageView ivBtn;//那个十字架
    private RelativeLayout mp_rl_click;
    /**
     * 第一排图 距离屏幕底部的距离
     */
    int top = 0;
    /**
     * 第二排图 距离屏幕底部的距离
     */
    int bottom = 0;
    public static PopUpMessageUtil getInstance() {
        return MenuUtilHolder.INSTANCE;
    }
    private static class MenuUtilHolder {
        public static PopUpMessageUtil INSTANCE = new PopUpMessageUtil();
    }
    public void _show(Context context, View parent ){
        //fb= FinalBitmap.create(context);

       // this.dizhi=dizhi;
         _createView(context);
         if (popupWindow != null && !popupWindow.isShowing()) {
             popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
              _openPopupWindowAction();
         }
    }
    /**
     * 刚打开popupWindow 执行的动画
     */
    private void _openPopupWindowAction() {

        //属性动画中旋转动画中rotation,rotationX和rotationY的区别
        //第一个参数是执行动画的参数 第二个是指定控件属性 第三个是被拉升的长度
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivBtn, "rotation", 0f, 135f);
        objectAnimator.setDuration(200);
        objectAnimator.start();

        _startAnimation(mpic1, 500, animatorProperty);
        _startAnimation(mpic2, 500, animatorProperty);
        _startAnimation(mpic3, 500, animatorProperty);
        _startAnimation(mpic4, 500, animatorProperty);

    }
    private void _createView(Context context){
        rootview= LayoutInflater.from(context).inflate(R.layout.xiaoxiitem,null);
        popupWindow=new PopupWindow(rootview, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(false);//设置失去焦点，方便监听返回按键
        // 如果想要popupWindow 遮挡住状态栏可以加上这句代码
        //popupWindow.setClippingEnabled(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        if (animatorProperty == null) {
            top = dip2px(context, 310);
            bottom = dip2px(context, 210);
            animatorProperty = new float[]{bottom, 60, -30, -20 - 10, 0};
        }

        initLayout(context);
    }
    //初始化view
    private void initLayout(Context context){
        ivBtn = (ImageView) rootview.findViewById(R.id.xmcpop_iv_img);//那个十字架
        mp_rl_click = (RelativeLayout) rootview.findViewById(R.id.mp_rl_click);
        mpic1=rootview.findViewById(R.id.mp1);
        mpic2=rootview.findViewById(R.id.mp2);
        mpic3=rootview.findViewById(R.id.mp3);
        mpic4=rootview.findViewById(R.id.mp4);


        mp_rl_click.setOnClickListener(new MViewClick(0, context));
        mpic1.setOnClickListener(new MViewClick(1, context));
        mpic2.setOnClickListener(new MViewClick(3, context));
        mpic3.setOnClickListener(new MViewClick(4, context));
        mpic4.setOnClickListener(new MViewClick(5, context));


    }
    /**
     * 点击事件
     */
    private class MViewClick implements View.OnClickListener {
        public int index;
        public Context context;

        public MViewClick(int index, Context context) {
            this.index = index;
            this.context = context;
        }
        @Override
        public void onClick(View v) {
            if (index == 0) {
                //加号按钮点击之后的执行
                _rlClickAction();
            } else {
                Intent intent=new Intent(context,messageActivity.class);
                intent.putExtra("typelei",String.valueOf(index));
                context.startActivity(intent);
            }
        }
    }
    /**
     * 关闭 popupWindow执行的动画
     */
    public void _rlClickAction() {
        if (ivBtn != null && mp_rl_click != null) {

            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivBtn, "rotation", 135f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.start();

            _closeAnimation(mpic1, 300, top);
            _closeAnimation(mpic2, 300, bottom);
            _closeAnimation(mpic3, 300, top);
            _closeAnimation(mpic4, 300, bottom);



            mp_rl_click.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _close();
                }
            }, 300);

        }
    }
    public  void _close(){
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
    /**
     * 关闭 popupWindow 时的动画
     *
     * @param view     mView
     * @param duration 动画执行时长
     * @param next     平移量
     */
    private void _closeAnimation(View view, int duration, int next) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", 0f, next);
        anim.setDuration(duration);
        anim.start();
    }
    private void _startAnimation(View view, int duration, float[] distance) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", distance);
        anim.setDuration(duration);
        anim.start();
    }
    /**
     * dp转化为px
     *
     * @param context  context
     * @param dipValue dp value
     * @return 转换之后的px值
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
