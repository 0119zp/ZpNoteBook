package zp.com.zpnotebook.login.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class GestureLockManager {

    private static final String GESTURE_LOGIN_PWD_KEY = "gesture_login";
    private static final String GESTURE_TAG_KEY = "gesture_tag";

    // 动态手势密码数值
    public static String gesturePwd = "123456789";

    /**
     * 清空缓存手势密码
     */
    public static void clearGesturePwd(Context context) {
        saveSpGesturePwd(context, "");
    }

    /**
     * 是否已设置手势密码
     */
    public static boolean isHasGesturePwd(Context context) {
        String gesturePed = getSpGesturePwd(context);
        if (TextUtils.isEmpty(gesturePed)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 得到缓存手势密码
     */
    public static String getSpGesturePwd(Context context) {
        SharedPreferences sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        return sp.getString(GESTURE_LOGIN_PWD_KEY, "");
    }

    /**
     * 缓存手势密码
     */
    public static void saveSpGesturePwd(Context context, String gesturePwd) {
        SharedPreferences sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(GESTURE_LOGIN_PWD_KEY, gesturePwd);
        editor.commit();
    }

    /**
     * 得到缓存手势密码
     */
    public static boolean getSpGestureTag(Context context) {
        SharedPreferences sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        return sp.getBoolean(GESTURE_TAG_KEY, true);
    }

    /**
     * 缓存手势密码
     */
    public static void saveSpGestureTag(Context context, boolean isTag) {
        SharedPreferences sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(GESTURE_TAG_KEY, isTag);
        editor.commit();
    }

}
