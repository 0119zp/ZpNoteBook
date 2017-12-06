package zp.com.zpnotebook.login.activity;

import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import zp.com.zpbase.activity.ZpBaseActivity;
import zp.com.zpnotebook.R;
import zp.com.zpnotebook.home.MainActivity;
import zp.com.zpnotebook.login.wight.GestureLockView;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class ZpLoginActivity extends ZpBaseActivity{

    private GestureLockView gestureLockView;
    private TextView errText;
    private TranslateAnimation animation;

    @Override
    protected int exInitLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void exInitView() {
        super.exInitView();
        gestureLockView = findViewById(R.id.login_gestureLockView);
        errText = findViewById(R.id.lock_login_errornum_tv);

        setGesture();
    }

    public void setGesture(){
        gestureLockView.setLimitNum(5);
        gestureLockView.setKey("12358");

        // 错误提示动画
        animation = new TranslateAnimation(-30, 30, 0, 0);
        animation.setDuration(50);
        animation.setRepeatCount(3);
        animation.setRepeatMode(Animation.REVERSE);

        gestureLockView.setOnGestureFinishListener(new GestureLockView.OnGestureFinishListener() {
            @Override
            public void OnGestureFinish(boolean success, String key, String adapterNum) {
                if (key.length() >= gestureLockView.getLimitNum()) {
                    if (success) {
                        Intent intent = new Intent(ZpLoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        verificaCancleBack();
                        finish();
                    } else {
                        errText.setText("手势密码错误");
                        verificaCancleBack();
                    }
                } else {
                    verificaCancleBack();
                    errText.setText("至少连接5个点");
                    errText.startAnimation(animation);
                }
            }
        });
    }

    protected void verificaCancleBack() {
        //清除绘制图案
        if(null != gestureLockView){
            gestureLockView.clearPath();
            gestureLockView.drawPath(true);
            gestureLockView.setPsw();
        }
    }
}
