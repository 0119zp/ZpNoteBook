package zp.com.zpnotebook.center;

import android.widget.TextView;
import android.widget.Toast;

import zp.com.zpbase.activity.ZpBaseActivity;
import zp.com.zpnotebook.R;
import zp.com.zpnotebook.login.manager.GestureLockManager;
import zp.com.zpnotebook.login.wight.GestureLockView;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class ZpSetGestureActivity extends ZpBaseActivity{

    private static final int SET_GESTURE_SUCCESS = 1;

    private TextView mSetDec;
    private GestureLockView mSetGesture;
    // 是否第一次绘制
    private boolean isFristSet = true;
    private String firstKey = "";

    @Override
    protected int exInitLayout() {
        return R.layout.activity_set_gesture;
    }

    @Override
    protected void exInitView() {
        super.exInitView();
        mSetDec = (TextView) findViewById(R.id.tv_set_gesture_dec);
        mSetGesture = (GestureLockView) findViewById(R.id.glv_set_gesture_view);

        initGestureView();
    }

    private void initGestureView() {
        mSetGesture.setLimitNum(4);
        mSetGesture.setOnGestureFinishListener(new GestureLockView.OnGestureFinishListener() {

            @Override
            public void OnGestureFinish(boolean success, String key, String adapterNum) {
                if (isFristSet) {
                    // 首次绘制
                    if (key.length() < mSetGesture.getLimitNum() ) {
                        Toast.makeText(ZpSetGestureActivity.this, "至少连接5个点", Toast.LENGTH_SHORT).show();
                    } else {
                        firstKey = key;
                        isFristSet = false;
                        mSetGesture.setKey(firstKey);
                        mSetDec.setText("请再次绘制解锁图案");
                    }
                } else {
                    // 再次绘制
                    if (key.length() < mSetGesture.getLimitNum()) {
                        Toast.makeText(ZpSetGestureActivity.this, "至少连接5个点", Toast.LENGTH_SHORT).show();
                    } else {
                        if (success) {
                            GestureLockManager.saveSpGesturePwd(ZpSetGestureActivity.this, firstKey);
                            Toast.makeText(ZpSetGestureActivity.this, "手势密码设置成功", Toast.LENGTH_SHORT).show();
                            setResult(SET_GESTURE_SUCCESS);
                            finish();
                        } else {
                            Toast.makeText(ZpSetGestureActivity.this, "两次绘制图案不一致", Toast.LENGTH_SHORT).show();
                            firstKey = "";
                            isFristSet = true;
                            mSetDec.setText("绘制解锁图案\n至少4个连接点");
                        }
                    }
                }
                clearGesturePath();
            }
        });
    }

    // 清除绘制轨迹
    protected void clearGesturePath() {
        mSetGesture.clearPath();
        mSetGesture.drawPath(true);
        mSetGesture.setPsw();
    }
}
