package zp.com.zpnotebook.center.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import zp.com.zpbase.fragment.ZpBaseFragment;
import zp.com.zpbase.utils.ZpActivity;
import zp.com.zpnotebook.R;
import zp.com.zpnotebook.center.ZpSetGestureActivity;
import zp.com.zpnotebook.center.wight.IosSwitchView;
import zp.com.zpnotebook.login.manager.GestureLockManager;

/**
 * Created by Administrator on 2017/12/7 0007.
 */
public class CenterFragment extends ZpBaseFragment implements View.OnClickListener {

    private RelativeLayout mGestureSet;
    private IosSwitchView switchView;

    @Override
    protected int exInitLayout() {
        return R.layout.fragment_center;
    }

    @Override
    protected void exInitView(View rootView) {
        super.exInitView(rootView);

        mGestureSet = (RelativeLayout) rootView.findViewById(R.id.ll_center_gesture_set);
        mGestureSet.setOnClickListener(this);

        initSwitch(rootView);
    }

    private void initSwitch(View rootView) {
        switchView = (IosSwitchView) rootView.findViewById(R.id.isv_center_switch);
        switchView.setOn(true);
        switchView.setOnSwitchStateChangeListener(new IosSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                if (isOn) {
                    GestureLockManager.saveSpGestureTag(mContext, true);
                } else {
                    GestureLockManager.saveSpGestureTag(mContext, false);
                }
            }
        });
        switchView.setTintColor(Color.RED);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_center_gesture_set:
                ZpActivity.getInstance().start(mActivity, ZpSetGestureActivity.class);
                break;
        }

    }
}
