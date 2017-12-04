package zp.com.zpbase.activity;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import zp.com.zpbase.R;
import zp.com.zpbase.fragment.ZpBaseOperationFragment;
import zp.com.zpbase.fragment.ZpOperation;
import zp.com.zpbase.fragment.ZpOperationNotNetworkFragment;
import zp.com.zpbase.utils.ZpIs;
import zp.com.zpbase.view.ZpNavigationBar;

/**
 * Created by zpan on 2017/7/25 0025.
 */

public class ZpBaseActivity extends ExBaseActivity{

    private ZpNavigationBar mTitle;
    private FrameLayout mContainer;
    private View mUserView;
    private ZpBaseOperationFragment mOperationFragment;

    @Override
    protected void onCreateInitLayout() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = layoutInflater.inflate(R.layout.zp_base_container, null);

        mTitle = (ZpNavigationBar) rootView.findViewById(R.id.nb_title);
        mContainer = (FrameLayout) rootView.findViewById(R.id.fl_container);

        mTitle.setVisibility(View.GONE);
        mTitle.getmTitleLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                back();
            }
        });

        mUserView = LayoutInflater.from(mActivity).inflate(exInitLayout(), null);
        mContainer.addView(mUserView);
        setContentView(rootView);

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

    protected void operaHide() {
        operaHide(true);
    }

    protected void operaHide(boolean isInit) {

        if (mUserView != null) {
            mUserView.setVisibility(View.VISIBLE);
        }

        if (mOperationFragment != null) {
            ZpOperation.getInstance().hideOperaFragment(getSupportFragmentManager(), mOperationFragment);
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

        mOperationFragment = ZpOperation.getInstance().getOperation(ZpOperation.TYPE_NOT_NETWORK, getSupportFragmentManager(), mOperationFragment);

        if (mOperationFragment instanceof ZpOperationNotNetworkFragment) {
            ((ZpOperationNotNetworkFragment) mOperationFragment).setOperaCallback(callback);
        }

        ZpOperation.getInstance().showOperaFragment(R.id.fl_container, getSupportFragmentManager(), mOperationFragment);
    }

    // 公共方法实现: 可提供子类重写
    public void initFNNavigationBar(ZpNavigationBar mTitle) {

    }

    // 得到头部
    protected ZpNavigationBar getFNNavigationBar() {
        return mTitle;
    }

    // 隐藏头部
    protected void hideFNNavigationBar() {
        mTitle.setVisibility(View.GONE);
    }

    // 显示头部
    protected void showFNNavigationBar() {
        mTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void back() {
        View view = getWindow().peekDecorView();
        if (null != view) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        finish();
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
    protected void exInitView() {

    }

    @Override
    protected void exInitAfter() {

    }

    @Override
    protected void exMessage(int what, Message msg) {

    }
}
