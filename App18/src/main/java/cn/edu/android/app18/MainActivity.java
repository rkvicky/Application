package cn.edu.android.app18;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;
    // 记录ImageView当前的位置
    private float curX = 0;
    private float curY = 0;
    // 记录ImageView下一个位置的座标
    float nextX = 0;
    //屏幕的宽度
    int width = 0;

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                // 横向上一直向右飞出，直至图片消失
                if (nextX > 2 * width) {
                    imageView.setVisibility(View.GONE);
                } else {
                    nextX += 100;
                }
                // 设置显示ImageView发生位移改变
                TranslateAnimation anim = new TranslateAnimation(curX, nextX, curY, curY);

                curX = nextX;
                anim.setDuration(20);
                // 开始位移动画
                imageView.startAnimation(anim);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        startTweenedAnim();
        startFrameAnim();
    }

    private void startTweenedAnim() {
        // 旋转动画
        RotateAnimation animRotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animRotate.setDuration(1000);// 动画时间
        animRotate.setFillAfter(true);// 保持动画结束状态

        // 缩放动画
        ScaleAnimation animScale = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animScale.setInterpolator(new BounceInterpolator());
        animScale.setDuration(1000);
        animScale.setFillAfter(true);// 保持动画结束状态

        // 渐变动画
        AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
        animAlpha.setDuration(2000);// 动画时间
        animAlpha.setFillAfter(true);// 保持动画结束状态

        // 动画集合
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animRotate);
        set.addAnimation(animScale);
        set.addAnimation(animAlpha);

        // 启动动画
        imageView.startAnimation(set);
    }

    private void startFrameAnim() {
        // 设置动画
        AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
        anim.start();
    }

    public void skip(View v) {
        v.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        width = metrics.widthPixels;
        curY = imageView.getY();
        // 通过定制器控制每0.02秒运行一次TranslateAnimation动画
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x123);
            }
        }, 0, 20);
    }
}
