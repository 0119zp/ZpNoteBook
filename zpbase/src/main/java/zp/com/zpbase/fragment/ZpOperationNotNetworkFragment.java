package zp.com.zpbase.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import zp.com.zpbase.R;
import zp.com.zpbase.utils.ZpIs;
import zp.com.zpbase.utils.ZpLog;

/**
 * Created by zpan on 2017/7/25 0025.
 * 无网络页面
 */

public class ZpOperationNotNetworkFragment extends ZpBaseOperationFragment{

    private TextView mTvRefresh;
    private OperaNetworkCallback mCallback;

    public static ZpOperationNotNetworkFragment newInstance() {
        ZpOperationNotNetworkFragment fragment = new ZpOperationNotNetworkFragment();

        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected int exInitLayout() {
        return R.layout.fragment_operation_not_network;
    }

    @Override
    protected void exInitView(View rootView) {
        super.exInitView(rootView);
        mTvRefresh = (TextView) rootView.findViewById(R.id.tv_not_work_refresh);

        mTvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null && ZpIs.getInstance().isConnected(mContext)) {
                    mCallback.onClickRefresh();
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();

        if (mCallback != null) {
            mCallback.show();
        }
    }

    @Override
    public void hide() {
        super.hide();

        if (mCallback != null) {
            mCallback.hide();
        }
    }

    public void setOperaCallback(OperaNetworkCallback callback) {

        mCallback = callback;
    }

    public interface OperaNetworkCallback extends ZpOperation.OperaCallBack{

        void onClickRefresh();
    }


}
