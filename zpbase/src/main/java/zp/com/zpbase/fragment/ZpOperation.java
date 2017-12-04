package zp.com.zpbase.fragment;

import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import zp.com.zpbase.utils.ZpIs;

/**
 * Created by zpan on 2017/7/25 0025.
 * 操作管理类
 */

public class ZpOperation {

    public static final int TYPE_NOT_NETWORK = 1;
    public static final int TYPE_NOT_CONTENT = 2;
    public static final int TYPE_FAIL_NETWORK = 3;

    /**
     * 创建者
     */
    private static class Holder {

        private static final ZpOperation mgr = new ZpOperation();
    }

    /**
     * 获取当前实例
     *
     * @return
     */
    public static ZpOperation getInstance() {

        return Holder.mgr;
    }

    public ZpBaseOperationFragment getOperation(int type) {

        switch (type) {
            case TYPE_NOT_CONTENT:

                return ZpOperationNotNetworkFragment.newInstance();
            case TYPE_NOT_NETWORK:

                return ZpOperationNotNetworkFragment.newInstance();
            case TYPE_FAIL_NETWORK:

                return ZpOperationNotNetworkFragment.newInstance();
        }

        return null;
    }

    public ZpBaseOperationFragment getOperation(int type, FragmentManager fragmentManager, ZpBaseOperationFragment fragment) {

        if (fragment != null) {
            if (!ZpOperation.getInstance().removeOperaFragment(type, fragmentManager, fragment)) {
                return fragment;
            }
        }

        return ZpOperation.getInstance().getOperation(type);
    }

    public boolean removeOperaFragment(int type, FragmentManager fragmentManager, ZpBaseOperationFragment fragment) {

        switch (type) {
            case TYPE_NOT_CONTENT:
                if (fragment instanceof ZpOperationNotNetworkFragment) {
                    return false;
                }
            case TYPE_NOT_NETWORK:
                if (fragment instanceof ZpOperationNotNetworkFragment) {
                    return false;
                }
            case TYPE_FAIL_NETWORK:
                if (fragment instanceof ZpOperationNotNetworkFragment) {
                    return false;
                }
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (fragment.isAdded()) {
            transaction.remove(fragment);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                transaction.commitAllowingStateLoss();
            } else {
                transaction.commit();
            }

            return true;
        }

        return false;
    }

    public void showOperaFragment(int resId, FragmentManager fragmentManager, ZpBaseOperationFragment fragment) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (fragment.isAdded() || !ZpIs.getInstance().isEmpty(fragment.getFragmentManager())) {
            transaction.show(fragment);
        } else {
            transaction.add(resId, fragment);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

    public void hideOperaFragment(FragmentManager fragmentManager, ZpBaseOperationFragment fragment) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (fragment.isAdded() || !ZpIs.getInstance().isEmpty(fragment.getFragmentManager())) {
            transaction.hide(fragment);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                transaction.commitAllowingStateLoss();
            } else {
                transaction.commit();
            }
        }
    }

    public interface OperaCallBack {

        void show();

        void hide();

    }

}
