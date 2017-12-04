package zp.com.zpbase.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zpan on 2017/7/25 0025.
 */

public abstract class ExFragment extends DialogFragment{

    protected FragmentActivity mActivity;
    protected Context mContext;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化全局对象
        mActivity = (FragmentActivity) getActivity();
        mContext = getActivity().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 获取当前操作 View
        if (rootView == null) {
            onCreateInitLayout(inflater);
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    protected void onCreateInitLayout(LayoutInflater inflater){
        setRootView(inflater.inflate(exInitLayout(), null));

        // 初始化步骤
        onCreateInitStep();
    }

    public void setRootView(View view) {
        this.rootView = view;
    }

    protected void onCreateInitStep() {

        // 实现类处理传入数据
        exInitBundle();
        // 实现类处理控件数据绑定
        exInitView(rootView);
        // 实现处理初始化完成之后
        exInitAfter();
    }

    /**
     * Method_初始化布局 ：对展示布局进行设置
     *
     * @return 布局资源 ID
     */
    protected abstract int exInitLayout();

    /**
     * Method_初始化传入参数 ：处理进入之前传入的数据
     */
    protected abstract void exInitBundle();

    /**
     * Method_初始化控件参数： 在该方法中，可以对已绑定的控件数据初始化
     *
     * @param rootView 布局视图
     */
    protected abstract void exInitView(View rootView);

    /**
     * Method_初始化之后： 在基础数据初始化完成之后可以使用该方法
     */
    protected abstract void exInitAfter();


}
