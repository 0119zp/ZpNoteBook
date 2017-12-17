package zp.com.zpnotebook.center.fragment;

import android.view.View;
import android.widget.LinearLayout;

import zp.com.zpbase.fragment.ZpBaseFragment;
import zp.com.zpbase.utils.ZpActivity;
import zp.com.zpnotebook.R;
import zp.com.zpnotebook.center.ZpSetGestureActivity;
import zp.com.zpnotebook.center.wight.ZpJisuanDialog;

/**
 * Created by Administrator on 2017/12/7 0007.
 */

public class CenterFragment extends ZpBaseFragment implements View.OnClickListener {

    private LinearLayout mGestureHind;
    private LinearLayout mGestureSet;

    @Override
    protected int exInitLayout() {
        return R.layout.fragment_center;
    }

    @Override
    protected void exInitView(View rootView) {
        super.exInitView(rootView);

        mGestureHind = (LinearLayout) rootView.findViewById(R.id.ll_center_gesture_hind);
        mGestureHind.setOnClickListener(this);
        mGestureSet = (LinearLayout) rootView.findViewById(R.id.ll_center_gesture_set);
        mGestureSet.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_center_gesture_hind:
                ZpJisuanDialog dialog = new ZpJisuanDialog(mActivity);
                dialog.show();
                break;
            case R.id.ll_center_gesture_set:
                ZpActivity.getInstance().start(mActivity, ZpSetGestureActivity.class);
                break;
        }

    }
}
