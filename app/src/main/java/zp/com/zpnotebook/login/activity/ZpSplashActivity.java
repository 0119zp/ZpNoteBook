package zp.com.zpnotebook.login.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import zp.com.zpbase.activity.ZpBaseActivity;
import zp.com.zpnotebook.R;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class ZpSplashActivity extends ZpBaseActivity{

    private TextView mSplashSkip;
    private CountDownTimer mTimer;

    @Override
    protected int exInitLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void exInitView() {
        super.exInitView();

        mSplashSkip = (TextView) findViewById(R.id.tv_spalsh_timer);
        mSplashSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ZpSplashActivity.this, ZpLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        initTimer();
    }

    private void initTimer() {
        mTimer = new CountDownTimer((long) (5 * 1000), 1) {

            @Override
            public void onTick(long millisUntilFinished) {
                int remainTime = (int) (millisUntilFinished / 1000L) + 1;
                mSplashSkip.setText(getResources().getString(R.string.zp_splash_skip, new Object[] {Integer.valueOf(remainTime)}));
            }

            @Override
            public void onFinish() {
                mSplashSkip.setText(getResources().getString(R.string.zp_splash_skip, new Object[] {Integer.valueOf(0)}));
                Intent intent = new Intent(ZpSplashActivity.this, ZpLoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        mTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

}
