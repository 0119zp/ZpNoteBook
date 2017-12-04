package zp.com.zpbase.utils;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by zpan on 2017/7/25 0025.
 * Toast 工具类
 */

public class ZpToast {

    private Toast mToast; // 提示框
    private TextView mTextView;

    /**
     * 创建者
     */
    private static class ToastHolder {

        private static final ZpToast mgr = new ZpToast();
    }

    /**
     * 获取当前实例
     *
     * @return
     */
    public static ZpToast getInstance() {

        return ToastHolder.mgr;
    }

    /**
     * Method_显示内容
     *
     * @param content 内容
     */
    public void show(Context context, String content) {

        show(context, content, -1);
    }

    /**
     * Method_显示内容
     *
     * @param resId 资源编号
     */
    public void show(Context context, int resId) {

        show(context, context.getString(resId), -1);
    }

    /**
     * Method_显示内容
     *
     * @param content 内容
     * @param time    时间
     */
    public void show(Context context, String content, int time) {

        if (mToast == null) {
            mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
//            mToast = new Toast(mContext);
//            mTextView = new TextView(mContext);
//            mTextView.setText("");
//            mTextView.setTextColor(mContext.getResources().getColor(R.color.toast_font_color));
//            mTextView.setPadding(20, 20, 20, 20);
//            mTextView.setBackgroundResource(R.color.toast_bg_color);
//            mTextView.setText(content);
//            mToast.setView(mTextView);
        } else {
            mToast.setText(content);
//            mTextView.setText(content);
        }

        if (time == -1) {
            time = Toast.LENGTH_SHORT;
        } else if (time == 0) {
            time = Toast.LENGTH_LONG;
        }

        mToast.setDuration(time);

        mToast.show();
    }

    /**
     * Method_显示内容
     *
     * @param resId 资源编号
     * @param time  时间
     */
    public void show(Context context, int resId, int time) {

        show(context, context.getString(resId), time);
    }

    /**
     * Method_显示内容
     *
     * @param content 内容
     */
    public void showShort(Context context, String content) {

        show(context, content, -1);
    }

    /**
     * Method_显示内容
     *
     * @param resId 资源编号
     */
    public void showShort(Context context, int resId) {

        show(context, context.getString(resId), -1);
    }

    /**
     * Method_显示内容
     *
     * @param content 内容
     */
    public void showLong(Context context, String content) {

        show(context, content, 0);
    }

    /**
     * Method_显示内容
     *
     * @param resId 资源编号
     */
    public void showLong(Context context, int resId) {

        show(context, context.getString(resId), 0);
    }


}
