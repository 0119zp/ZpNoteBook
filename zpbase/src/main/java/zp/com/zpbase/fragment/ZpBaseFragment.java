package zp.com.zpbase.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import zp.com.zpbase.R;
import zp.com.zpbase.utils.ZpIs;
import zp.com.zpbase.view.ZpNavigationBar;

/**
 * Created by zpan on 2017/7/26 0026.
 */

public class ZpBaseFragment extends ExFragment{

    private ZpBaseOperationFragment mOperationFragment;
    private ZpNavigationBar mTitle;
    private FrameLayout mContainer;
    private View mUserView;

    @Override
    protected void onCreateInitLayout(LayoutInflater inflater) {

        View rootView = inflater.inflate(R.layout.zp_base_container, null);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rootView.setLayoutParams(lp);

        mTitle = (ZpNavigationBar) rootView.findViewById(R.id.nb_title);
        mContainer = (FrameLayout) rootView.findViewById(R.id.fl_container);

        mTitle.setVisibility(View.GONE);

        mUserView = inflater.inflate(exInitLayout(), null);

        mContainer.addView(mUserView);

        setRootView(rootView);

        ZpOperationNotNetworkFragment.OperaNetworkCallback callback = isShowNotNetworkOfOnCreate();

        if (callback != null) {
            if (!ZpIs.getInstance().isConnected(mContext)) {
                operaShowNotNetwork(callback);
            } else {
                operaHide();
            }
        } else {
            operaHide();
        }

        initFNNavigationBar(mTitle);
    }

    // 公共方法实现: 可提供子类重写
    private void initFNNavigationBar(ZpNavigationBar mTitle) {

    }

    protected void operaHide() {

        operaHide(true);
    }

    protected void operaHide(boolean isInit) {

        if (mUserView != null) {
            mUserView.setVisibility(View.VISIBLE);
        }
        if (mOperationFragment != null) {
            ZpOperation.getInstance().hideOperaFragment(getChildFragmentManager(), mOperationFragment);
        }
        if (isInit) {
            onCreateInitStep();
        }
    }

    // 通用操作界面处理方案
    protected void operaShowNotNetwork(ZpOperationNotNetworkFragment.OperaNetworkCallback callback) {

        if (mUserView != null) {
            mUserView.setVisibility(View.GONE);
        }

        mOperationFragment = ZpOperation.getInstance().getOperation(ZpOperation.TYPE_NOT_NETWORK, getChildFragmentManager(), mOperationFragment);

        if (mOperationFragment instanceof ZpOperationNotNetworkFragment) {
            ((ZpOperationNotNetworkFragment) mOperationFragment).setOperaCallback(callback);
        }

        ZpOperation.getInstance().showOperaFragment(R.id.fl_container, getChildFragmentManager(), mOperationFragment);
    }

    public ZpOperationNotNetworkFragment.OperaNetworkCallback isShowNotNetworkOfOnCreate() {

        return null;
    }

    @Override
    protected int exInitLayout() {
        return 0;
    }

    @Override
    protected void exInitBundle() {

    }

    @Override
    protected void exInitView(View rootView) {

    }

    @Override
    protected void exInitAfter() {

    }
}
