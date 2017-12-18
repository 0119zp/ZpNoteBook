package zp.com.zpnotebook.center.wight;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import zp.com.zpnotebook.R;


/**
 * Created by Administrator on 2017/9/24 0024.
 */

public class IosSwitchView extends View {

    private static final long commonDuration = 300L;
    private static final int intrinsicWidth = 0;
    private static final int intrinsicHeight = 0;
    //控件内部动画
    private ObjectAnimator innerContentAnimator;
    private ObjectAnimator knobExpandAnimator;
    private ObjectAnimator knobMoveAnimator;
    private GestureDetector gestureDetector;
    private int width;
    private int height;

    private int centerX;
    private int centerY;

    private float cornerRadius;

    private int shadowSpace;//阴影大小
    private int outerStrokeWidth;//外部椭圆边大小

    private Drawable shadowDrawable;

    private RectF knobBound;
    private float knobMaxExpandWidth;
    private float intrinsicKnobWidth;
    private float knobExpandRate;
    private float knobMoveRate;

    private boolean knobState;
    private boolean isOn;
    private boolean preIsOn;
    private boolean isFullcolor = false; //颜色显示完全非半透明的

    private RectF innerContentBound;
    private float innerContentRate = 1.0F;
    private float intrinsicInnerWidth;
    private float intrinsicInnerHeight;

    private int openColor;

    private int tempTintColor;


    //外部椭圆颜色
    private int backgroundColor = 0xFFCCCCCC;
    //内部颜色
    private int closeColor = 0xFFEFEFEF;
    //当该控件为不可点击时将closeColor暂时存在tempcloseColor中
    private int tempcloseColor = closeColor;
    //内部椭圆knob颜色
    private int knobgroundColor = 0xFFEFEFEF;//默认内部颜色和内部内容颜色一致

    private int colorStep = backgroundColor;

    private Paint paint;
    private RectF tempForRoundRect;

    private boolean dirtyAnimation = false;
    private boolean isAttachedToWindow = false;
    private OnSwitchStateChangeListener onSwitchStateChangeListener;
    private Property<IosSwitchView, Float> innerContentProperty = new Property<IosSwitchView, Float>(Float.class, "innerBound") {
        @Override
        public void set(IosSwitchView sv, Float innerContentRate) {
            sv.setInnerContentRate(innerContentRate);
        }

        @Override
        public Float get(IosSwitchView sv) {
            return sv.getInnerContentRate();
        }
    };
    private Property<IosSwitchView, Float> knobExpandProperty = new Property<IosSwitchView, Float>(Float.class, "knobExpand") {
        @Override
        public void set(IosSwitchView sv, Float knobExpandRate) {
            sv.setKnobExpandRate(knobExpandRate);
        }

        @Override
        public Float get(IosSwitchView sv) {
            return sv.getKnobExpandRate();
        }
    };
    private Property<IosSwitchView, Float> knobMoveProperty = new Property<IosSwitchView, Float>(Float.class, "knobMove") {
        @Override
        public void set(IosSwitchView sv, Float knobMoveRate) {
            sv.setKnobMoveRate(knobMoveRate);
        }

        @Override
        public Float get(IosSwitchView sv) {
            return sv.getKnobMoveRate();
        }
    };
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent event) {

            if (!isEnabled()) {
                return false;
            }

            preIsOn = isOn;

            innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
            innerContentAnimator.start();

            knobExpandAnimator.setFloatValues(knobExpandRate, 1.0F);
            knobExpandAnimator.start();

            return true;
        }

        @Override
        public void onShowPress(MotionEvent event) {


        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            isOn = knobState;

            if (preIsOn == isOn) {
                isOn = !isOn;
                knobState = !knobState;
            }

            if (knobState) {
                knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                knobMoveAnimator.start();

                innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
                innerContentAnimator.start();
            } else {
                knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                knobMoveAnimator.start();

                innerContentAnimator.setFloatValues(innerContentRate, 1.0F);
                innerContentAnimator.start();
            }

            knobExpandAnimator.setFloatValues(knobExpandRate, 0.0F);
            knobExpandAnimator.start();

            if (IosSwitchView.this.onSwitchStateChangeListener != null && isOn != preIsOn) {
                IosSwitchView.this.onSwitchStateChangeListener.onSwitchStateChange(isOn);
            }

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (e2.getX() > centerX) {
                if (!knobState) {
                    knobState = !knobState;

                    knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                    knobMoveAnimator.start();

                    innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
                    innerContentAnimator.start();
                }
            } else {
                if (knobState) {
                    knobState = !knobState;

                    knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                    knobMoveAnimator.start();
                }
            }
            return true;
        }
    };

    public IosSwitchView(Context context) {
        this(context, null);
    }


    public IosSwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public IosSwitchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IosSwitchView);

        openColor = ta.getColor(R.styleable.IosSwitchView_openColor, 0xFF9CE949);
        closeColor = ta.getColor(R.styleable.IosSwitchView_closeColor, 0xFFEFEFEF);
        knobgroundColor = ta.getColor(R.styleable.IosSwitchView_knobColor, 0xFFEFEFEF);
        backgroundColor = ta.getColor(R.styleable.IosSwitchView_backgroundColor, 0xFFCCCCCC);
        tempTintColor = openColor;

        //默认的外部椭圆边的宽度
        //转换成标准的dip单位
        int defaultOuterStrokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5F, context.getResources().getDisplayMetrics());
        //阴影大小
        int defaultShadowSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
        outerStrokeWidth = ta.getDimensionPixelOffset(R.styleable.IosSwitchView_outerStrokeWidth, defaultOuterStrokeWidth);
        shadowSpace = ta.getDimensionPixelOffset(R.styleable.IosSwitchView_shadowSpace, defaultShadowSpace);
        ta.recycle();

        knobBound = new RectF();
        innerContentBound = new RectF();
        tempForRoundRect = new RectF();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gestureDetector = new GestureDetector(context, gestureListener);
        gestureDetector.setIsLongpressEnabled(false);

        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        innerContentAnimator = ObjectAnimator.ofFloat(IosSwitchView.this, innerContentProperty, innerContentRate, 1.0F);
        innerContentAnimator.setDuration(commonDuration);
        innerContentAnimator.setInterpolator(new DecelerateInterpolator());

        knobExpandAnimator = ObjectAnimator.ofFloat(IosSwitchView.this, knobExpandProperty, knobExpandRate, 1.0F);
        knobExpandAnimator.setDuration(commonDuration);
        knobExpandAnimator.setInterpolator(new DecelerateInterpolator());

        knobMoveAnimator = ObjectAnimator.ofFloat(IosSwitchView.this, knobMoveProperty, knobMoveRate, 1.0F);
        knobMoveAnimator.setDuration(commonDuration);
        knobMoveAnimator.setInterpolator(new DecelerateInterpolator());

        shadowDrawable = context.getResources().getDrawable(R.mipmap.ic_launcher);
    }

    public OnSwitchStateChangeListener getOnSwitchStateChangeListener() {
        return this.onSwitchStateChangeListener;
    }

    public void setOnSwitchStateChangeListener(OnSwitchStateChangeListener onSwitchStateChangeListener) {
        this.onSwitchStateChangeListener = onSwitchStateChangeListener;
    }

    float getInnerContentRate() {
        return this.innerContentRate;
    }

    public void setInnerContentRate(float rate) {
        this.innerContentRate = rate;
        invalidate();
    }

    float getKnobExpandRate() {
        return this.knobExpandRate;
    }

    public void setKnobExpandRate(float rate) {
        this.knobExpandRate = rate;
        invalidate();
    }

    public float getKnobMoveRate() {
        return knobMoveRate;
    }

    public void setKnobMoveRate(float rate) {
        this.knobMoveRate = rate;
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;

        if (dirtyAnimation) {
            knobState = this.isOn;
            if (knobState) {
                knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                knobMoveAnimator.start();

                innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
                innerContentAnimator.start();
            } else {
                knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                knobMoveAnimator.start();

                innerContentAnimator.setFloatValues(innerContentRate, 1.0F);
                innerContentAnimator.start();
            }
            knobExpandAnimator.setFloatValues(knobExpandRate, 0.0F);
            knobExpandAnimator.start();

            if (IosSwitchView.this.onSwitchStateChangeListener != null && isOn != preIsOn) {
                IosSwitchView.this.onSwitchStateChangeListener.onSwitchStateChange(isOn);
            }
            dirtyAnimation = false;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec 这个两个参数包含了两个值:模式和尺寸,参数前两位代表模式后30为代表大小
     *                          int mode = MeasureSpec.getMode(widthMeasureSpec);
     *                          int size = MeasureSpec.getSize(widthMeasureSpec);
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        //make sure widget remain in a good appearance
        if ((float) height / (float) width < 0.33333F) {
            height = (int) ((float) width * 0.33333F);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec));
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec));
            super.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }

        centerX = width / 2;
        centerY = height / 2;

        cornerRadius = centerY - shadowSpace;

        innerContentBound.left = outerStrokeWidth + shadowSpace;
        innerContentBound.top = outerStrokeWidth + shadowSpace;
        innerContentBound.right = width - outerStrokeWidth - shadowSpace;
        innerContentBound.bottom = height - outerStrokeWidth - shadowSpace;

        intrinsicInnerWidth = innerContentBound.width();
        intrinsicInnerHeight = innerContentBound.height();

        knobBound.left = outerStrokeWidth + shadowSpace;
        knobBound.top = outerStrokeWidth + shadowSpace;
        knobBound.right = height - outerStrokeWidth - shadowSpace;
        knobBound.bottom = height - outerStrokeWidth - shadowSpace;

        intrinsicKnobWidth = knobBound.height();
        knobMaxExpandWidth = (float) width * 0.7F;
        if (knobMaxExpandWidth > knobBound.width() * 1.25F) {
            knobMaxExpandWidth = knobBound.width() * 1.25F;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //innerContentCalculation begin
        float w = intrinsicInnerWidth / 2.0F * innerContentRate;
        float h = intrinsicInnerHeight / 2.0F * innerContentRate;

        this.innerContentBound.left = centerX - w;
        this.innerContentBound.top = centerY - h;
        this.innerContentBound.right = centerX + w;
        this.innerContentBound.bottom = centerY + h;
        //innerContentCalculation end

        //knobExpandCalculation begin
        w = intrinsicKnobWidth + (knobMaxExpandWidth - intrinsicKnobWidth) * knobExpandRate;

        boolean left = knobBound.left + knobBound.width() / 2 > centerX;

        if (left) {
            knobBound.left = knobBound.right - w;
        } else {
            knobBound.right = knobBound.left + w;
        }
        //knobExpandCalculation end

        //knobMoveCalculation begin
        float kw = knobBound.width();
        w = (width - kw - ((shadowSpace + outerStrokeWidth) * 2)) * knobMoveRate;

        this.colorStep = RGBColorTransform(knobMoveRate, backgroundColor, openColor);


        knobBound.left = shadowSpace + outerStrokeWidth + w;
        knobBound.right = knobBound.left + kw;
        //knobMoveCalculation end

        //background
        paint.setColor(colorStep);
        paint.setStyle(Paint.Style.FILL);

        drawRoundRect(shadowSpace, shadowSpace, width - shadowSpace, height - shadowSpace, cornerRadius, canvas, paint);

        //innerContent
        paint.setColor(closeColor);
        canvas.drawRoundRect(innerContentBound, innerContentBound.height() / 2, innerContentBound.height() / 2, paint);

        //knob
        //shadowDrawable.setBounds((int) (knobBound.left - shadowSpace), (int) (knobBound.top - shadowSpace), (int) (knobBound.right + shadowSpace), (int) (knobBound.bottom + shadowSpace));
        //shadowDrawable.draw(canvas);
        paint.setShadowLayer(2, 0, shadowSpace / 2, isEnabled() ? 0x20000000 : 0x10000000);

        //paint.setColor(isEnabled() ? 0x20000000 : 0x10000000);
        //drawRoundRect(knobBound.left, knobBound.top + shadowSpace / 2, knobBound.right, knobBound.bottom + shadowSpace / 2, cornerRadius - outerStrokeWidth, canvas, paint);
        paint.setColor(knobgroundColor);
        canvas.drawRoundRect(knobBound, cornerRadius - outerStrokeWidth, cornerRadius - outerStrokeWidth, paint);
        paint.setShadowLayer(0, 0, 0, 0);

        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);

        canvas.drawRoundRect(knobBound, cornerRadius - outerStrokeWidth, cornerRadius - outerStrokeWidth, paint);
    }

    public boolean isOn() {
        return this.isOn;
    }

    public void setOn(boolean on) {
        setOn(on, false);
    }

    public void setOn(boolean on, boolean animated) {

        if (this.isOn == on) {
            return;
        }

        if (!isAttachedToWindow && animated) {
            dirtyAnimation = true;
            this.isOn = on;

            return;
        }

        this.isOn = on;
        knobState = this.isOn;

        if (!animated) {

            if (on) {
                setKnobMoveRate(1.0F);
                setInnerContentRate(0.0F);
            } else {
                setKnobMoveRate(0.0F);
                setInnerContentRate(1.0F);
            }

            setKnobExpandRate(0.0F);

        } else {
            if (knobState) {

                knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                knobMoveAnimator.start();

                innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
                innerContentAnimator.start();
            } else {

                knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                knobMoveAnimator.start();

                innerContentAnimator.setFloatValues(innerContentRate, 1.0F);
                innerContentAnimator.start();
            }

            knobExpandAnimator.setFloatValues(knobExpandRate, 0.0F);
            knobExpandAnimator.start();
        }

        if (IosSwitchView.this.onSwitchStateChangeListener != null && isOn != preIsOn) {
            IosSwitchView.this.onSwitchStateChangeListener.onSwitchStateChange(isOn);
        }
    }

    public int getTintColor() {
        return this.openColor;
    }

    public void setTintColor(int tintColor) {
        this.openColor = tintColor;
        tempTintColor = this.openColor;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!knobState) {
                    innerContentAnimator = ObjectAnimator.ofFloat(IosSwitchView.this, innerContentProperty, innerContentRate, 1.0F);
                    innerContentAnimator.setDuration(300L);
                    innerContentAnimator.setInterpolator(new DecelerateInterpolator());

                    innerContentAnimator.start();
                }

                knobExpandAnimator = ObjectAnimator.ofFloat(IosSwitchView.this, knobExpandProperty, knobExpandRate, 0.0F);
                knobExpandAnimator.setDuration(300L);
                knobExpandAnimator.setInterpolator(new DecelerateInterpolator());

                knobExpandAnimator.start();

                isOn = knobState;

                if (IosSwitchView.this.onSwitchStateChangeListener != null && isOn != preIsOn) {
                    IosSwitchView.this.onSwitchStateChangeListener.onSwitchStateChange(isOn);
                }

                break;
        }
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (enabled || isFullcolor) {
            this.openColor = tempTintColor;
            this.closeColor = tempcloseColor;
        } else {
            this.openColor = this.RGBColorTransform(0.5F, tempTintColor, 0xFFFFFFFF);
            this.closeColor = 0xFFEFEFEF;
        }
    }

    private void drawRoundRect(float left, float top, float right, float bottom, float radius, Canvas canvas, Paint paint) {
        tempForRoundRect.left = left;
        tempForRoundRect.top = top;
        tempForRoundRect.right = right;
        tempForRoundRect.bottom = bottom;
        canvas.drawRoundRect(tempForRoundRect, radius, radius, paint);
    }

    //seperate RGB channels and calculate new value for each channel
    //ignore alpha channel
    private int RGBColorTransform(float progress, int fromColor, int toColor) {
        int or = (fromColor >> 16) & 0xFF;
        int og = (fromColor >> 8) & 0xFF;
        int ob = fromColor & 0xFF;

        int nr = (toColor >> 16) & 0xFF;
        int ng = (toColor >> 8) & 0xFF;
        int nb = toColor & 0xFF;

        int rGap = (int) ((float) (nr - or) * progress);
        int gGap = (int) ((float) (ng - og) * progress);
        int bGap = (int) ((float) (nb - ob) * progress);
        return 0xFF000000 | ((or + rGap) << 16) | ((og + gGap) << 8) | (ob + bGap);
    }

    public IosSwitchView setIsisFullcolor(boolean isFullcolor) {
        this.isFullcolor = isFullcolor;
        return this;
    }

    public interface OnSwitchStateChangeListener {
        public void onSwitchStateChange(boolean isOn);
    }
}

