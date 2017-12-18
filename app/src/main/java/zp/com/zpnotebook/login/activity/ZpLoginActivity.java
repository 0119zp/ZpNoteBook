package zp.com.zpnotebook.login.activity;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import zp.com.zpbase.activity.ZpBaseActivity;
import zp.com.zpbase.utils.ZpActivity;
import zp.com.zpnotebook.R;
import zp.com.zpnotebook.center.ZpSetGestureActivity;
import zp.com.zpnotebook.home.MainActivity;
import zp.com.zpnotebook.login.manager.GestureLockManager;
import zp.com.zpnotebook.login.wight.GestureLockView;

/**
 * Created by Administrator on 2017/12/4 0004.
 */
public class ZpLoginActivity extends ZpBaseActivity{

    private GestureLockView gestureLockView;
    private TextView errText;
    private TextView setGesture;
    private TranslateAnimation animation;

    @Override
    protected int exInitLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void exInitView() {
        super.exInitView();
        gestureLockView = (GestureLockView) findViewById(R.id.gv_login_gesture_view);
        errText = (TextView) findViewById(R.id.tv_login_err_text);
        setGesture = (TextView) findViewById(R.id.tv_login_set_gesture);

        if (GestureLockManager.isHasGesturePwd(this)) {
            setGesture.setVisibility(View.GONE);
        } else {
            setGesture.setVisibility(View.VISIBLE);
        }

        setGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZpActivity.getInstance().start(ZpLoginActivity.this, ZpSetGestureActivity.class);
            }
        });

        setGesture();
    }

    public void setGesture(){
        gestureLockView.setLimitNum(5);
        gestureLockView.setKey(GestureLockManager.getSpGesturePwd(this));
        gestureLockView.setIsGone(GestureLockManager.getSpGestureTag(this));

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
