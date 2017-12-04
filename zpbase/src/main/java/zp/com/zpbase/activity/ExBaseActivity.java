package zp.com.zpbase.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

/**
 * Created by zpan on 2017/7/25 0025.
 */

public abstract class ExBaseActivity extends FragmentActivity {

    protected FragmentActivity mActivity;
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化全局变量
        mActivity = this;
        mContext = this.getApplicationContext();

        // 绑定布局
        onCreateInitLayout();

    }

    protected void onCreateInitLayout() {

        setContentView(exInitLayout());

        // 初始化步骤
        onCreateInitStep();
    }

    protected void onCreateInitStep() {

        // 实现类处理传入数据
        exInitBundle();
        // 实现类处理控件数据绑定
        exInitView();
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
     */
    protected abstract void exInitView();

    /**
     * Method_初始化之后： 在基础数据初始化完成之后可以使用该方法
     */
    protected abstract void exInitAfter();

    /**
     * Method_获取消息结果回调 只包括手动发送消息
     *
     * @param what 状态操作码_其值需大于1000
     * @param msg  消息传递对象
     */
    protected abstract void exMessage(int what, Message msg);


}
