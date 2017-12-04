package zp.com.zpbase.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by Administrator on 2017/9/24 0024.
 * 强大的ViewHolder
 */

public class ZpViewHolder {

    private ZpViewHolder() {
    }

    /**
     * 获取view
     *
     * @param view adapter view
     * @param id   控件id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (null == viewHolder) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (null == childView) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

}

// 使用
//ImageView brokerPhoto = ViewHolder.get(convertView, R.id.second_broker_photo_img);
