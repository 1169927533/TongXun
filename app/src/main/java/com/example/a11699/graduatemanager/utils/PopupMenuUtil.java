package com.example.a11699.graduatemanager.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.a11699.graduatemanager.R;
import com.facebook.drawee.view.DraweeView;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;

//执行伸缩菜单的工具类
public class PopupMenuUtil {
    private FinalBitmap fb;//显示图片
    private static final String TAG = "PopupMenuUtil";
    private View rootview;
    private PopupWindow popupWindow;
    private ImageView ivBtn;//那个十字架
    private RelativeLayout rlClick;
    DraweeView pic1,pic2,pic3,pic4,pic5,pic6 ;
    DraweeView s[]={pic1,pic2,pic3,pic4,pic5,pic6};
    /**
     * 动画执行的 属性值数组
     */
    float animatorProperty[] = null;
    List<String> dizhi=new ArrayList<>();
    /**
     * 第一排图 距离屏幕底部的距离
     */
    int top = 0;
    /**
     * 第二排图 距离屏幕底部的距离
     */
    int bottom = 0;

    public static PopupMenuUtil getInstance() {
        return MenuUtilHolder.INSTANCE;
    }
    private static class MenuUtilHolder {
        public static PopupMenuUtil INSTANCE = new PopupMenuUtil();
    }
    public void _show(Context context, View parent,List<String> dizhi){
        fb=FinalBitmap.create(context);

        this.dizhi=dizhi;
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

        _startAnimation(pic1, 500, animatorProperty);
        _startAnimation(pic2, 430, animatorProperty);
        _startAnimation(pic3, 500, animatorProperty);
        _startAnimation(pic4, 500, animatorProperty);
        _startAnimation(pic5, 430, animatorProperty);
        _startAnimation(pic6, 500, animatorProperty);
    }
    /**
     * 启动动画
     *
     * @param view     view
     * @param duration 执行时长
     * @param distance 执行的轨迹数组
     */
    private void _startAnimation(View view, int duration, float[] distance) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", distance);
        anim.setDuration(duration);
        anim.start();
    }


    private void _createView(Context context){
        rootview= LayoutInflater.from(context).inflate(R.layout.tupianzhanshi,null);
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
        ivBtn = (ImageView) rootview.findViewById(R.id.pop_iv_img);//那个十字架
        rlClick = (RelativeLayout) rootview.findViewById(R.id.pop_rl_click);
        s[0]=rootview.findViewById(R.id.pic1);
        s[1]=rootview.findViewById(R.id.pic2);
        s[2]=rootview.findViewById(R.id.pic3);
        s[3]=rootview.findViewById(R.id.pic4);
        s[4]=rootview.findViewById(R.id.pic5);
        s[5]=rootview.findViewById(R.id.pic6);

        rlClick.setOnClickListener(new MViewClick(0, context));
        s[0].setOnClickListener(new MViewClick(1, context));
        s[1].setOnClickListener(new MViewClick(2, context));
        s[2].setOnClickListener(new MViewClick(2, context));
        s[3].setOnClickListener(new MViewClick(3, context));
        s[4].setOnClickListener(new MViewClick(4, context));
        s[5].setOnClickListener(new MViewClick(5, context));
        if(dizhi.size()!=0){
                for(int i=0;i<dizhi.size();i++){
                    if(i>=6){
                        fb.display(s[5],dizhi.get(i));
                    }else{
                        fb.display(s[i],dizhi.get(i));
                    }
                }
        }
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
              //  new PopWindow_image(fb,context,dizhi.get(index-1)).showAtLocation(v,0,0,0);

            }
        }
    }
    /**
     * 关闭 popupWindow执行的动画
     */
    public void _rlClickAction() {
        if (ivBtn != null && rlClick != null) {

            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivBtn, "rotation", 135f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.start();

            _closeAnimation(pic1, 300, top);
            _closeAnimation(pic2, 200, top);
            _closeAnimation(pic3, 300, top);
            _closeAnimation(pic4, 300, bottom);
            _closeAnimation(pic5, 200, bottom);
            _closeAnimation(pic6, 300, bottom);


            rlClick.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _close();
                }
            }, 300);

        }
    }
    /**
     * 关闭popupWindow
     */

    public void _close() {
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
