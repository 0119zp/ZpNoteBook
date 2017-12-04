package zp.com.zpbase.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by zpan on 2017/7/25 0025.
 */

public class ZpActivity {

    public static final String TAG = ZpActivity.class.getName();

    private CopyOnWriteArrayList<Activity> mActivities;
    private static HashMap<String, CopyOnWriteArrayList<Activity>> mTagActivities;

    /**
     * 创建者
     */
    private static class ActivityHolder {

        private static final ZpActivity mgr = new ZpActivity();
    }

    /**
     * 获取当前实例
     *
     * @return
     */
    public static ZpActivity getInstance() {

        return ActivityHolder.mgr;
    }

    public ZpActivity() {

        mActivities = new CopyOnWriteArrayList<>();
        mTagActivities = new HashMap<>();
    }

    /**
     * Method_添加 getActivityInstance 至管理队列
     *
     * @param activity 添加的 getActivityInstance
     */
    public void add(Activity activity) {

        add(TAG, activity);
    }

    /**
     * Method_添加 Activity 通过标识
     *
     * @param tag      标识
     * @param activity 对象
     */
    public void add(String tag, Activity activity) {

        if (mTagActivities == null) {
            mTagActivities = new HashMap<>();
        }

        CopyOnWriteArrayList<Activity> list;

        if (mTagActivities.containsKey(tag)) {
            list = mTagActivities.get(tag);
        } else {
            list = new CopyOnWriteArrayList<>();
        }

        list.add(activity);
        mTagActivities.put(tag, list);

        if (mActivities == null) {
            mActivities = new CopyOnWriteArrayList<>();
        }

        mActivities.add(activity);
    }

    /**
     * Method_移除 Activity
     *
     * @param activity 对象
     */
    public void remove(Activity activity) {

        remove(TAG, activity);
    }

    /**
     * Method_移除 Activity 通过标识
     *
     * @param tag 标识
     */
    public void remove(String tag) {

        remove(tag, null);
    }

    /**
     * Method_移除 Activity 通过对象与标识
     *
     * @param tag 标识
     * @param activity 对象
     */
    public void remove(String tag, Activity activity) {

        if (mTagActivities.containsKey(tag)) {
            if (activity == null) {
                mTagActivities.remove(tag);
            } else {
                CopyOnWriteArrayList<Activity> list = mTagActivities.get(tag);
                list.remove(activity);
                if (mActivities.contains(activity)) {
                    mActivities.remove(activity);
                }
            }
        }
    }

    /**
     * Method_获取当前 getActivityInstance
     *
     * @return getActivityInstance 对象
     */
    public Activity getCurrent() {

        Activity activity = mActivities.get(mActivities.size() - 1);

        return activity;
    }

    /**
     * Method_结束当前 getActivityInstance
     */
    public void finish() {

        Activity activity = getCurrent();

        if (activity != null) {
            remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * Method_结束指定 getActivityInstance
     *
     * @param activity 指定的 getActivityInstance
     */
    public void finish(Activity activity) {

        if (activity != null) {
            remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * Method_结束指定 Tag
     *
     * @param tag 标识
     */
    public void finish(String tag) {

        if (mTagActivities.containsKey(tag)) {
            CopyOnWriteArrayList<Activity> list = mTagActivities.get(tag);
            for (Activity activity : list) {
                finish(activity);
            }
            mTagActivities.remove(tag);
        }
    }

    /**
     * Method_结束指定类名的 getActivityInstance
     *
     * @param cls 类名信息
     */
    public void finish(Class<?> cls) {

        for (int i = 0; i < mActivities.size(); i++) {
            Activity activity = mActivities.get(i);

            if (activity != null && activity.getClass().equals(cls)) {
                finish(activity);
            }
        }
    }

    /**
     * Method_结束所有 getActivityInstance
     */
    public void finishAll() {

        for (int i = 0; i < mActivities.size(); i++) {
            Activity activity = mActivities.get(i);

            if (activity != null) {
                finish(activity);
            }
        }

        mActivities.clear();
        mTagActivities.clear();
    }

    /**
     * Method_结束非当前所有的 getActivityInstance
     */
    public void finishAllExceptLast() {

        for (int i = 0; i < mActivities.size(); i++) {
            Activity activity = mActivities.get(i);

            if (activity != null && !getCurrent().equals(activity)) {
                finish(activity);
            }
        }
    }

    /**
     * Method_系统退出
     */
    public void exit() {

        try {
            finishAll();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method_回到主页
     *
     * @param activity 当前 Activity
     */
    public void goHome(Activity activity) {

        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);

        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        activity.startActivity(mHomeIntent);
    }

    /**
     * Method_通过 Intent 启动 getActivityInstance
     *
     * @param activity 当前 Activity
     * @param intent   意图对象
     */
    public void start(Activity activity, Intent intent) {

        activity.startActivity(intent);
    }

    /**
     * Method_启动通过类名启动
     *
     * @param activity 当前 Activity
     * @param cls      类名信息
     */
    public void start(Activity activity, Class<?> cls) {

        start(activity, cls, null);
    }

    /**
     * Method_启动通过类名和数据启动
     *
     * @param activity 当前 Activity
     * @param cls      类名
     * @param bundle   参数数据
     */
    public void start(Activity activity, Class<?> cls, Bundle bundle) {

        Intent intent = new Intent(activity, cls);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        start(activity, intent);
    }

    /**
     * Method_启动通过类名和数据启动
     *
     * @param activity 当前 Activity
     * @param cls      类名
     * @param bundle   参数数据
     */
    public void start(Activity activity, Class<?> cls, String action, Bundle bundle) {

        Intent intent = new Intent(activity, cls);
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        start(activity, intent);
    }

    /**
     * Method_启动通过类名和数据启动
     *
     * @param activity 当前 Activity
     * @param cls      类名
     * @param bundle   参数数据
     */
    public void startForResult(Activity activity, Class<?> cls, String action, Bundle bundle, int requestCode) {

        Intent intent = new Intent(activity, cls);
        intent.setAction(action);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * Method_启动可以指定类名和返回码返回
     *
     * @param activity    当前 Activity
     * @param cls         类名
     * @param requestCode 返回码
     */
    public void startForResult(Activity activity, Class<?> cls, int requestCode) {

        startForResult(activity, cls, null, requestCode);
    }

    /**
     * Method_启动可以指定类名，数据和返回码返回
     *
     * @param activity    当前 Activity
     * @param cls         类名
     * @param bundle      参数数据
     * @param requestCode 返回码
     */
    public void startForResult(Activity activity, Class<?> cls, Bundle bundle, int requestCode) {

        Intent intent = new Intent(activity, cls);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Method_启动创建新的实例
     *
     * @param activity 当前 Activity
     * @param cls      指定类名启动
     */
    public void startNew(Activity activity, Class<?> cls) {

        startNew(activity, cls, null);
    }

    /**
     * Method_启动创建新的实例通过类名和数据
     *
     * @param activity 当前 Activity
     * @param cls      指定类名启动
     * @param bundle   参数数据
     */
    public void startNew(Activity activity, Class<?> cls, Bundle bundle) {

        Intent intent = new Intent(activity, cls);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        start(activity, intent);
    }

    /**
     * Method_启动创建新的实例通过类名和数据
     *
     * @param activity 当前 Activity
     * @param cls      指定类名启动
     * @param bundle   参数数据
     */
    public void startClearTop(Activity activity, Class<?> cls, Bundle bundle) {

        Intent intent = new Intent(activity, cls);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        start(activity, intent);
    }

}
