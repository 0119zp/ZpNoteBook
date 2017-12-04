package zp.com.zpbase.utils;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/7/25 0025.
 */

public class ZpIs {

    public static final String TAG = ZpIs.class.getName();

    private long mLastClickTime;

    /**
     * 创建者
     */
    private static class IsHolder {

        private static final ZpIs mgr = new ZpIs();
    }

    /**
     * 获取当前实例
     *
     * @return
     */
    public static ZpIs getInstance() {

        return IsHolder.mgr;
    }

    public boolean notNullOrEmpty(String str) {

        return !ZpIs.getInstance().isEmpty(str);
    }

    public String getNotNullString(String str) {

        if (isEmpty(str)) {
            return "";
        }

        return str;
    }

    /**
     * Method_判断对象是否为空
     *
     * @param obj 对象
     * @return 结果
     */
    public boolean isEmpty(Object obj) {

        if(obj == null){
            return true;
        }
        if (obj instanceof String) {
            return isEmpty((String) obj);
        }
        if (obj instanceof List<?>) {
            List<?> list = (List<?>) obj;
            return list == null || list.size() == 0;
        }
        if (obj instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) obj;
            return map == null || map.size() == 0;
        }
        if (obj instanceof int[]) {
            int[] array = (int[]) obj;
            return array == null || array.length == 0;
        }
        if (obj instanceof float[]) {
            float[] array = (float[]) obj;
            return array == null || array.length == 0;
        }
        if (obj instanceof long[]) {
            long[] array = (long[]) obj;
            return array == null || array.length == 0;
        }
        if (obj instanceof double[]) {
            double[] array = (double[]) obj;
            return array == null || array.length == 0;
        }
        if (obj instanceof Object[]) {
            Object[] array = (Object[]) obj;
            return array == null || array.length == 0;
        }
        if (obj instanceof Object) {
            return obj == null;
        }
        return false;
    }

    /**
     * Method_是否为空
     *
     * @param strs 字符数组
     * @return 结果
     */
    public boolean isEmpty(String[] strs) {

        if (strs == null || strs.length == 0) {
            return true;
        }

        int size = strs.length;

        for (int i = 0; i < size; i++) {
            String str = strs[i];

            if (isEmpty(str)) {

                return true;
            }
        }

        return false;
    }

    /**
     * Method_是否为空
     *
     * @param map Map对象
     * @return 结果
     */
    public boolean isEmpty(Map map) {

        if (map == null || map.size() == 0 || map.isEmpty()) {

            return true;
        }

        return false;
    }

    /**
     * Method_是否为空
     *
     * @param list 列表集合
     * @return 结果
     */
    public boolean isEmpty(List list) {

        if (list == null || list.isEmpty()) {

            return true;
        }

        int size = list.size();

        for (int i = 0; i < size; i++) {
            Object obj = list.get(i);

            if (obj == null || obj.toString().length() == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method_是否为空
     *
     * @param input 输入内容
     * @return 结果
     */
    public boolean isEmpty(String input) {
        if (TextUtils.isEmpty(input)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method_是否点击太快
     *
     * @param time 时间
     * @return 结果
     */
    public boolean isFastDoubleClick(long time) {

        long timeC = System.currentTimeMillis();
        long timeD = timeC - mLastClickTime;

        if (0 < timeD && timeD < time) {
            return true;
        }

        mLastClickTime = timeC;

        return false;
    }

    /**
     * Method_是否包含表情
     *
     * @param source 内容
     * @return 结果
     */
    public boolean isContainsEmoji(String source) {

        if (isEmpty(source)) {
            return false;
        }

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (!isEmojiCharacter(codePoint)) {
                //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }

        return false;
    }

    /**
     * Method_是否存在 Emoji 表情
     *
     * @param codePoint 字符
     * @return 结果
     */
    private boolean isEmojiCharacter(char codePoint) {

        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * Method_是否是数字
     *
     * @param str 字符串
     * @return 结果
     */
    public boolean isNumber(String str) {

        try {
            Integer.parseInt(str.toString());
        } catch (Exception e) {

            return false;
        }

        return true;
    }

    /**
     * Method_是否是今天
     *
     * @param todayStr 字符串
     * @return 结果
     */
    public boolean isToday(String todayStr) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date toDayDate = sdf.parse(todayStr);
            Date nowDate = new Date();

            if (nowDate.getYear() == toDayDate.getYear() && nowDate.getMonth() == toDayDate.getMonth() && nowDate.getDate() == toDayDate.getDate()) {

                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Method_是否有空格
     *
     * @param input 输入字符
     * @return 结果
     */
    public boolean isWhiteSpace(String input) {

        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(input);

        boolean found = matcher.find();

        return found;
    }

    /**
     * Method_是否满足正则表达式
     *
     * @param patternStr 匹配字符串
     * @param regStr     正则表达式
     * @return 结果
     */
    public boolean isPattern(String patternStr, String regStr) {

        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(patternStr);

        if (matcher.matches()) {

            return true;
        }

        return false;
    }

    /**
     * Method_是否是 Email
     *
     * @param email 邮箱地址
     * @return 结果
     */
    public boolean isEmail(String email) {

        if (email == null || email.trim().length() == 0) {
            return false;
        }

        return Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*").matcher(email).matches();
    }

    /**
     * Method_是否是 JPEG
     *
     * @param b 字节流
     * @return 结果
     */
    public boolean isJPEG(byte[] b) {

        if (b.length < 2) {
            return false;
        }

        return (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
    }

    /**
     * Method_是否是 GIF
     *
     * @param b 字节流
     * @return 结果
     */
    public boolean isGIF(byte[] b) {

        if (b.length < 6) {
            return false;
        }

        return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8'
                && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
    }

    /**
     * Method_是否是 PNG
     *
     * @param b 字节流
     * @return 结果
     */
    public boolean isPNG(byte[] b) {

        if (b.length < 8) {
            return false;
        }

        return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78
                && b[3] == (byte) 71 && b[4] == (byte) 13 && b[5] == (byte) 10
                && b[6] == (byte) 26 && b[7] == (byte) 10);
    }

    /**
     * Method_是否是 BMP
     *
     * @param b 字节流
     * @return 结果
     */
    public boolean isBMP(byte[] b) {

        if (b.length < 2) {
            return false;
        }

        return (b[0] == 0x42) && (b[1] == 0x4d);
    }

    /**
     * Method_是否在后台
     *
     * @param context 上下文
     * @return 结果
     */
    public boolean isBackground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    // 后台运行
                    return true;
                } else {
                    // 前台运行
                    return false;
                }
            }
        }

        return false;
    }

    /**
     * Method_是否在休眠
     *
     * @param context 上下文
     * @return 结果
     */
    public boolean isSleeping(Context context) {

        KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();

        return isSleeping;
    }

    /**
     * Method_是否存在某个应用
     *
     * @param context 上下文
     * @param pkgName 包名
     * @return 结果
     */
    public boolean isExitsApp(Context context, String pkgName) {

        if (ZpIs.getInstance().isEmpty(pkgName)) {

            return false;
        }

        try {
            context.getPackageManager().getPackageInfo(pkgName, 0);

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Method_是否是系统 APP
     *
     * @param context 上下文
     * @param pkgName 包名
     * @return 结果
     */
    public boolean isSysApp(Context context, String pkgName) {

        if (ZpIs.getInstance().isEmpty(pkgName)) {

            return false;
        }

        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(pkgName, 0);

            if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {

                return false;
            } else if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {

                return false;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Method_网络是否连接
     *
     * @return 结果
     */
    public boolean isConnected(Context context) {

        if (context == null) {
            return false;
        }
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {

            return false;
        } else {
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {

                return true;
            }
        }

        return false;
    }

    /**
     * Method_判断WIFI网络是否可用
     *
     * @return 结果
     */
    public boolean isConnectedByWifi(Context context) {

        return isConnecteByType(context, ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Method_判断MOBILE网络是否可用
     *
     * @return 结果
     */
    public boolean isConnectedByMobile(Context context) {

        return isConnecteByType(context, ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Method_根据网络类型判断是否可用
     *
     * @param networkType 网络类型
     * @return 结果
     */
    public boolean isConnecteByType(Context context, int networkType) {

        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(networkType);

            if (mWiFiNetworkInfo != null) {

                return mWiFiNetworkInfo.isConnected();
            }
        }

        return false;
    }

    /**
     * Method_文件是否存在
     *
     * @param file 文件
     * @return 结果
     */
    public boolean isFileExists(File file) {

        if (file == null) {

            return false;
        }

        return file.exists();
    }

    /**
     * Method_文件是否存在
     *
     * @param filePath 文件路径
     * @return 结果
     */
    public boolean isFileExists(String filePath) {

        if (isEmpty(filePath)) {

            return false;
        }

        return isFileExists(new File(filePath));
    }

}
