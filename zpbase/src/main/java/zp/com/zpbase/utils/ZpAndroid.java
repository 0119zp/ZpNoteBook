package zp.com.zpbase.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.Locale;

/**
 * Created by zpan on 2017/7/26 0026.
 */

public class ZpAndroid {

    public static final String TAG = ZpAndroid.class.getName();

    /**
     * 创建者
     */
    private static class AndroidHolder {

        private static final ZpAndroid mgr = new ZpAndroid();
    }

    /**
     * 获取当前实例
     *
     * @return
     */
    public static ZpAndroid getInstance() {

        return AndroidHolder.mgr;
    }

    /**
     * Method_通过文件名称获取 Assests 内容
     *
     * @param context  上下文
     * @param fileName 文件名称
     * @return 字符内容
     */
//    public String getContentByAssetsFileName(Context context, String fileName) {
//
//        String result = "";
//        InputStream in = null;
//
//        try {
//            in = context.getResources().getAssets().open(fileName);
//
//            int length = in.available();
//            byte[] buffer = new byte[length];
//            in.read(buffer);
//            // Android API 22
//            result = EncodingUtils.getString(buffer, "UTF-8");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return result;
//    }

    /**
     * Method_通过名称和类型获取 ID
     *
     * @param context 上下文
     * @param name    名称
     * @param defType 类型
     * @return 资源 ID
     */
    private int getIdentifier(Context context, String name, String defType) {

        int result = 0;

        String skinPkgName = context.getPackageName();

        try {
            result = context.createPackageContext(skinPkgName, Context.CONTEXT_IGNORE_SECURITY).getResources().getIdentifier(name, defType, skinPkgName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Method_改变本地语言
     *
     * @param context 上下文
     * @param lanAtr  目标语言字段
     * @return 更改结果
     */
    public boolean changeLanguage(Context context, String lanAtr) {

        try {
            Resources resources = context.getResources();
            Configuration config = resources.getConfiguration();
            DisplayMetrics dm = resources.getDisplayMetrics();

            if (lanAtr.equals("en")) {
                config.locale = Locale.ENGLISH;
            } else {
                config.locale = Locale.SIMPLIFIED_CHINESE;
            }

            resources.updateConfiguration(config, dm);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Method_获取 View 辅助对象
     *
     * @param view 视图对象
     * @param id   编号管理
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getViewHolder(View view, int id) {

        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }

        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }

        return (T) childView;
    }

    /**
     * Method_发送广播
     *
     * @param context 上下文
     * @param action  定制
     * @param bundle  参数
     */
    public void sendBroadcast(Context context, String action, Bundle bundle) {

        if (action == null || action.length() <= 0) {
            return;
        }

        Intent intent = new Intent(action);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        context.sendBroadcast(intent);
    }

    /**
     * Method_Resources
     *
     * @param context 上下文
     * @return 对象
     */
    public Resources resources(Context context) {

        return context.getResources();
    }

    /**
     * Method_stirng
     *
     * @param context 上下文
     * @param name    资源名称
     * @return 资源 ID
     */
    public int string(Context context, String name) {

        return getIdentifier(context, name, "string");
    }

    /**
     * Method_stirng
     *
     * @param context 上下文
     * @param id      资源ID
     * @return 资源内容
     */
    public String string(Context context, int id) {

        return resources(context).getString(id);
    }

    /**
     * Method_anim
     *
     * @param context 上下文
     * @param name    资源名称
     * @return 资源 ID
     */
    public int anim(Context context, String name) {

        return getIdentifier(context, name, "anim");
    }

    /**
     * Method_attr
     *
     * @param context 上下文
     * @param name    资源名称
     * @return 资源 ID
     */
    public int attr(Context context, String name) {

        return getIdentifier(context, name, "attr");
    }

    /**
     * Method_dimen
     *
     * @param context 上下文
     * @param name    资源名称
     * @return 资源 ID
     */
    public int dimen(Context context, String name) {

        return getIdentifier(context, name, "dimen");
    }

    /**
     * Method_drawable
     *
     * @param context 上下文
     * @param name    资源名称
     * @return 资源 ID
     */
    public int drawable(Context context, String name) {

        return getIdentifier(context, name, "drawable");
    }

    /**
     * Method_id
     *
     * @param context 上下文
     * @param name    资源名称
     * @return 资源 ID
     */
    public int id(Context context, String name) {

        return getIdentifier(context, name, "id");
    }

    /**
     * Method_layout
     *
     * @param context 上下文
     * @param name    资源名称
     * @return 资源 ID
     */
    public int layout(Context context, String name) {

        return getIdentifier(context, name, "layout");
    }

    /**
     * Method_menu
     *
     * @param context 上下文
     * @param name    资源名称
     * @return 资源 ID
     */
    public int menu(Context context, String name) {

        return getIdentifier(context, name, "menu");
    }

    /**
     * Method_style
     *
     * @param context 上下文
     * @param name    资源名称
     * @return 资源 ID
     */
    public int style(Context context, String name) {

        return getIdentifier(context, name, "style");
    }

    /**
     * Method_xml
     *
     * @param context 上下文
     * @param name    资源名称
     * @return 资源 ID
     */
    public int xml(Context context, String name) {

        return getIdentifier(context, name, "xml");
    }

    /**
     * Method_设置 ListView 基础高度
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {

        if (listView == null) {

            return;
        }
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem == null) {
                continue;
            }

            listItem.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//            listItem.measure(0, 0);

            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * Method_设置 GridView 基础高度
     *
     * @param listView
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setGridViewHeightBasedOnChildren(GridView listView) {

        if (listView == null) {

            return;
        }
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int size = listAdapter.getCount() / listView.getNumColumns() + 1;

        for (int i = 0; i < size; i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem == null) {
                continue;
            }

            listItem.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getVerticalSpacing() * (listAdapter.getCount() - 1));
        params.height = totalHeight + listView.getVerticalSpacing() * size + listView.getVerticalSpacing();

        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
