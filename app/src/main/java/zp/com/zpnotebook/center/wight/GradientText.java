package zp.com.zpnotebook.center.wight;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

public class GradientText extends android.support.v7.widget.AppCompatTextView{

    private Paint mTextPaint;
    private Matrix matrix;

    private int mVx;
    private Shader shader;

    public GradientText(Context context) {
        super(context);
        initText();
    }

    public GradientText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initText();
    }

    public GradientText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initText();
    }

    private void initText() {
        // 得到控件本身的paint
        mTextPaint = getPaint();
        // 矩阵
        matrix = new Matrix();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 我们设置一个LgShader，从左边[距离文字显示2倍宽度的距离]
        shader = new LinearGradient(-2 * w, 0, -w, 0, new int[]{getCurrentTextColor(), Color.RED, Color.YELLOW, Color.BLUE, getCurrentTextColor(),}, null, Shader.TileMode.CLAMP);
        mTextPaint.setShader(shader);

        initAnimtor(w);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 重置矩阵为单位矩阵。
        matrix.reset();
        // 设置平移效果，参数分别是x，y上的平移量。
        matrix.preTranslate(mVx, 0);
        shader.setLocalMatrix(matrix);

    }

    public void initAnimtor(int width){
        ValueAnimator animator = ValueAnimator.ofInt(0, width * 3);  //我们设置value的值为0-getMeasureWidth的3 倍
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mVx = (Integer) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.setRepeatMode(ValueAnimator.RESTART);   //重新播放
        animator.setRepeatCount(-1);                    //无限循环
        animator.setDuration(3000);
        animator.start();
    }

}
