package zp.com.zpbase.utils;

import android.util.Log;

/**
 * Created by zpan on 2017/7/25 0025.
 */

public class ZpLog {

    public static final String TAG = ZpLog.class.getName();

    public String mTag = "Ex Log"; // 便签
    public boolean mDebug = true; // 是否开启调试

    /**
     * 创建者
     */
    private static class LogHolder {

        private static final ZpLog mgr = new ZpLog();
    }

    /**
     * 获取当前实例
     *
     * @return
     */
    public static ZpLog getInstance() {

        return LogHolder.mgr;
    }

    /**
     * Method_设置是否开启调试
     *
     * @param debug 是否开启调试
     */
    public void setDebug(boolean debug) {

        mDebug = debug;
    }

    /**
     * Method_这是标签
     *
     * @param tag 标签
     */
    public void setTag(String tag) {

        mTag = tag;
    }

    /**
     * Method_d
     *
     * @param msg 消息
     */
    public void d(String msg) {

        if (mDebug) {
            Log.d(mTag, msg);
        }
    }

    /**
     * Method_w
     *
     * @param msg 消息
     */
    public void w(String msg) {

        if (mDebug) {
            Log.w(mTag, msg);
        }
    }

    /**
     * Method_i
     *
     * @param msg 消息
     */
    public void i(String msg) {

        if (mDebug) {
            Log.i(mTag, msg);
        }
    }

    /**
     * Method_e
     *
     * @param msg 消息
     */
    public void e(String msg) {

        if (mDebug) {
            Log.e(mTag, msg);
        }
    }

    /**
     * Method_e
     *
     * @param msg 消息
     */
    public void e(String msg, Throwable e) {

        if (mDebug) {
            Log.e(mTag, msg, e);
        }
    }

    /**
     * Method_d
     *
     * @param msg 消息
     */
    public void d(String tag, String msg) {

        if (mDebug) {
            Log.d(tag, msg);
        }
    }

    /**
     * Method_w
     *
     * @param msg 消息
     */
    public void w(String tag, String msg) {

        if (mDebug) {
            Log.w(tag, msg);
        }
    }

    /**
     * Method_i
     *
     * @param msg 消息
     */
    public void i(String tag, String msg) {

        if (mDebug) {
            Log.i(tag, msg);
        }
    }

    /**
     * Method_e
     *
     * @param msg 消息
     */
    public void e(String tag, String msg) {

        if (mDebug) {
            Log.e(tag, msg);
        }
    }

    /**
     * Method_e
     *
     * @param msg 消息
     */
    public void e(String tag, String msg, Throwable e) {

        if (mDebug) {
            Log.e(tag, msg, e);
        }
    }

    /**
     * Method_exE
     *
     * @param msg 消息
     */
    public void exE(String msg) {

        Log.e(TAG, "Ex library 4.0 >>>> msg = " + msg);
    }

    /**
     * Method_exD
     *
     * @param msg 消息
     */
    public void exD(String msg) {

        if (mDebug) {
            Log.d(TAG, "Ex library 4.0 >>>> msg = " + msg);
        }
    }


}
