package zp.com.zpbase.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Config;

import zp.com.zpbase.activity.ZpBaseActivity;

/**
 * Created by Administrator on 2017/12/11 0011.
 */

public class ExitAppReceiver extends BroadcastReceiver{

    // 退出app广播
    public static final String APP_EXIT = Config.class.getName() + "_APP_EXIT";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (context instanceof ZpBaseActivity) {
            ((ZpBaseActivity) context).finish();
        }
    }
}
