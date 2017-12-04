package zp.com.zpbase.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import zp.com.zpbase.R;

/**
 * Created by zpan on 2017/7/26 0026.
 */

public class ZpApp {

    public static final String TAG = ZpApp.class.getName();

    /**
     * 创建者
     */
    private static class AppHolder {

        private static final ZpApp mgr = new ZpApp();
    }

    /**
     * 获取当前实例
     *
     * @return
     */
    public static ZpApp getInstance() {

        return AppHolder.mgr;
    }

    /**
     * Method_显示系统键盘
     *
     * @param activity 当前 Activity
     * @param v 操作对象
     */
    public void showSysKeyBord(Activity activity, View v) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }

    /**
     * Method_显示系统键盘
     *
     * @param activity 当前 Activity
     */
    public void toggleSysKeyBord(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Method_点击任何地方系统键盘
     *
     * @param activity 当前 Activity
     */
    public void hideNoThingSysKeyBord(Activity activity) {

        InputMethodManager imeManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imeManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    /**
     * Method_隐藏系统键盘
     *
     * @param activity 当前 Activity
     * @param v 操作对象
     */
    public void hideSysKeyBord(Activity activity,View v) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
    }

    /**
     * Method_发送邮件
     *
     * @param activity 当前 Activity
     * @param emailAddr 邮件地址
     */
    public void sendEmail(Activity activity,String[] emailAddr) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, emailAddr);
        intent.setType("plain/text");

        activity.startActivity(Intent.createChooser(intent, ZpAndroid.getInstance().string(activity, R.string.ex_str_content_please_install_email)));
    }

    /**
     * Method_发送短信通过系统
     *
     * @param activity 当前 Activity
     * @param phoneNumber 电话号码
     * @param content     内容
     */
    public void sendSMSMgr(Activity activity,String phoneNumber, String content) {

        SmsManager smsManager = SmsManager.getDefault();

        PendingIntent sentIntent = PendingIntent.getBroadcast(activity, 0, new Intent(), 0);

        if (content.length() > 70) {
            List<String> divideContents = smsManager.divideMessage(content);

            for (String text : divideContents) {
                smsManager.sendTextMessage(phoneNumber, null, text, sentIntent, null);
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
        }
    }

    /**
     * Method_发送短信
     *
     * @param activity 当前 Activity
     * @param phoneNumber 电话号码
     * @param content     内容
     */
    public void sendSMS(Activity activity,String phoneNumber, String content) {

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
        intent.putExtra("sms_body", content);

        activity.startActivity(intent);
    }

    /**
     * Method_在地图上面显示位置
     */
    public void showMapByWeb() {

    }

    /**
     * Method_显示 WEB 页面
     *
     * @param activity 当前 Activity
     * @param url
     */
    public void showPageByWeb(Activity activity,String url) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(url));

        activity.startActivity(intent);
    }

    /**
     * Method_打开 Excel
     *
     * @param activity 当前 Activity
     * @param path 路径
     */
    public void openExcel(Activity activity,String path) {

        openFile(activity, path, "application/vnd.ms-excel");
    }

    /**
     * Method_打开 PPT
     *
     * @param activity 当前 Activity
     * @param path 路径
     */
    public void openPPT(Activity activity,String path) {

        openFile(activity,path, "application/vnd.ms-powerpoint");
    }

    /**
     * Method_打开 Word
     *
     * @param activity 当前 Activity
     * @param path 路径
     */
    public void openWord(Activity activity,String path) {

        openFile(activity,path, "application/msword");
    }

    /**
     * Method_打开文件
     *
     * @param activity 当前 Activity
     * @param path 路径
     * @param type 文件路径
     */
    public void openFile(Activity activity,String path, String type) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(path)), type);

        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method_应用安装
     *
     * @param activity 当前 Activity
     * @param filename 名
     * @return 结果
     */
    @SuppressLint("WorldReadableFiles")
    @SuppressWarnings("deprecation")
    public boolean install(Activity activity,String filename) {

        if (ZpIs.getInstance().isEmpty(filename)) {

            return false;
        }

        int result = Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS, 0);

        if (result == 0) {
            Toast.makeText(activity, ZpAndroid.getInstance().string(activity, R.string.ex_str_content_please_not_mark_app), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();

            intent.setAction(Settings.ACTION_APPLICATION_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            activity.startActivity(intent);
        } else {

            if (!filename.contains("//sdcard")) {

                String oldname = filename;
                filename = "sharetemp_" + System.currentTimeMillis();

                try {
                    FileInputStream fis = new FileInputStream(oldname);
                    FileOutputStream fos = activity.openFileOutput(filename, Context.MODE_PRIVATE);

                    filename = activity.getFilesDir() + "/" + filename;
                    byte[] buffer = new byte[1024];
                    int len = 0;

                    while ((len = fis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }

                    buffer = null;
                    fis.close();
                    fis = null;
                    fos.flush();
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();

                    return false;
                }
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setDataAndType(Uri.parse("file://" + filename), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

            activity.startActivity(intent);

            return true;
        }

        return false;
    }

    /**
     * Method_卸载应用
     *
     * @param activity 当前 Activity
     * @param packageName 包名
     * @return 结果
     */
    public boolean uninstall(Activity activity,String packageName) {

        if (ZpIs.getInstance().isEmpty(packageName)) {

            return false;
        }

        Uri packageURI = Uri.parse("package:" + packageName);
        Intent i = new Intent(Intent.ACTION_DELETE, packageURI);

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivityForResult(i, 10);

        return true;

    }

    /**
     * Method_运行应用
     *
     * @param activity    当前 Activity
     * @param packageName 包名
     * @param bundle      参数
     * @return 结果
     */
    public boolean run(Activity activity, String packageName, Bundle bundle) {

        try {
            activity.getPackageManager().getPackageInfo(packageName, PackageManager.SIGNATURE_MATCH);

            try {
                Intent intent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (bundle != null) {
                    intent.putExtras(bundle);
                }

                activity.startActivity(intent);
            } catch (Exception e) {
                try {
                    Toast.makeText(activity, ZpAndroid.getInstance().string(activity, R.string.ex_str_content_please_check_app_type), Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                }
                e.printStackTrace();

                return false;
            }

            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Method_运行应用
     *
     * @param activity    当前 Activity
     * @param packageName 包名
     * @return 结果
     */
    public boolean run(Activity activity, String packageName) {

        return run(activity, packageName, null);
    }

}
