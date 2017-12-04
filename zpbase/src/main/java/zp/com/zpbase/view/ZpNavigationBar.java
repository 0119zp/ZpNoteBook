package zp.com.zpbase.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import zp.com.zpbase.R;

/**
 * Created by zpan on 2017/7/25 0025.
 */

public class ZpNavigationBar extends LinearLayout{

    private Context mContext;
    private LinearLayout mTitleLeft;
    private TextView mTitleName;
    private TextView mTitleRight;
    private ImageView mTitleRightImg;

    public ZpNavigationBar(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public ZpNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.navigation_bar, this, true);
        mTitleLeft = (LinearLayout) findViewById(R.id.tv_left);
        mTitleName = (TextView) findViewById(R.id.tv_title);
        mTitleRight = (TextView) findViewById(R.id.tv_right);
        mTitleRightImg = (ImageView) findViewById(R.id.iv_right);
    }

    public TextView getmTitleName() {
        return mTitleName;
    }

    public void setmTitleName(TextView mTitleName) {
        this.mTitleName = mTitleName;
    }

    public TextView getmTitleRight() {
        return mTitleRight;
    }

    public void setmTitleRight(TextView mTitleRight) {
        this.mTitleRight = mTitleRight;
    }

    public ImageView getmTitleRightImg() {
        return mTitleRightImg;
    }

    public void setmTitleRightImg(ImageView mTitleRightImg) {
        this.mTitleRightImg = mTitleRightImg;
    }

    public LinearLayout getmTitleLeft() {
        return mTitleLeft;
    }

    public void setmTitleLeft(LinearLayout mTitleLeft) {
        this.mTitleLeft = mTitleLeft;
    }
}
