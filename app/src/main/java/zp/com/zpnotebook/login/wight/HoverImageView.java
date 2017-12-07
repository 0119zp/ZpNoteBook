package zp.com.zpnotebook.login.wight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import zp.com.zpnotebook.R;


public class HoverImageView extends ImageView {

	// Scaletype决定了图片在View上显示时的样子，如进行何种比例的缩放，及显示图片的整体还是部分，等等
	private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
	// 设置两张图片相交时的模式。
	private static final PorterDuffXfermode duffMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
	
	private boolean pressed;
	private int borderColor = 0x00ff00;
	private int hoverColor = 0xff0000;
	// 画笔
	private Paint borderPaint;
	private Path boundPath;
	private Path borderPath;
	/**
	 * 用于表示坐标系中的一块矩形区域，并可以对其做一些简单操作。这块矩形区域，需要用左上和右下两个坐标点表示。
	 * 精度不一样。Rect是使用int类型作为数值，RectF是使用float类型作为数值。
	 */
	private RectF rect = new RectF();
	private float borderWidth = 4f;
	
	public HoverImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup(attrs);
	}

	public HoverImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup(attrs);
	}

	public HoverImageView(Context context) {
		super(context);
		setup(null);
	}

	// 自定义属性的使用
	private void setup(AttributeSet attrs) {
		if(attrs != null){
			TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RoundImageView);
			borderColor = typedArray.getColor(R.styleable.RoundImageView_borderColor, borderColor);
			hoverColor = typedArray.getColor(R.styleable.RoundImageView_hoverColor, hoverColor);
			borderWidth = typedArray.getDimension(R.styleable.RoundImageView_borderWidth, borderWidth);
			typedArray.recycle();
		}
		
		borderPath = new Path();
		boundPath = new Path();
		borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// 用于设置画笔的空心线宽。该方法在矩形、圆形等图形上有明显的效果。
		borderPaint.setStrokeWidth(borderWidth);
		
		super.setScaleType(SCALE_TYPE);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		setImageDrawable(null);
		super.onDetachedFromWindow();
	}
	
	protected void drawBorder(Canvas canvas) {
		// 设置填充的颜色
		borderPaint.setStyle(Style.STROKE);
		// 设置画笔的颜色
		borderPaint.setColor(borderColor);
		canvas.drawPath(borderPath, borderPaint);
	}

	protected void drawHover(Canvas canvas) {
		if(this.isClickable() && pressed){
			borderPaint.setStyle(Style.FILL);
			borderPaint.setColor(hoverColor);
			canvas.drawPath(boundPath, borderPaint);
		}
	}
	
	public void buildBorderPath(Path borderPath) {
		borderPath.reset();
		final float halfBorderWidth = borderWidth * 0.5f;
		boundPath.addRect(halfBorderWidth, halfBorderWidth,
				getWidth() - halfBorderWidth, getHeight() - halfBorderWidth, Direction.CW);
	}
	
	public void buildBoundPath(Path boundPath){
		boundPath.reset();
		boundPath.addRect(0, 0, getWidth(), getHeight(), Direction.CW);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		
		if(changed){
			buildBoundPath(boundPath);
			buildBorderPath(borderPath);
		}
	}


	@Override
	protected void onDraw(Canvas canvas) {
		
		Drawable maiDrawable = getDrawable();
		if (!isInEditMode() && maiDrawable instanceof BitmapDrawable) {
			Paint paint = ((BitmapDrawable) maiDrawable).getPaint();
	        
	        Rect bitmapBounds = maiDrawable.getBounds();
	        rect.set(bitmapBounds);
	        
			int saveCount = canvas.saveLayer(rect, null,
                    Canvas.MATRIX_SAVE_FLAG |
                    Canvas.CLIP_SAVE_FLAG |
                    Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                    Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                    Canvas.CLIP_TO_LAYER_SAVE_FLAG);
			getImageMatrix().mapRect(rect);

			// 抗锯齿方法两种（其一：paint.setAntiAlias(ture);paint.setBitmapFilter(true))
	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        final int color = 0xffffffff;
	        paint.setColor(color);
	        canvas.drawPath(boundPath, paint);
 
			Xfermode oldMode = paint.getXfermode();
			paint.setXfermode(duffMode);
	        super.onDraw(canvas);
	        paint.setXfermode(oldMode);
			// save 与 restoreToCount用来保存和回复Canvas的状态（Maxtrix等属性）
	        canvas.restoreToCount(saveCount);
	        
	        drawHover(canvas);
			drawBorder(canvas);
		} else {
			super.onDraw(canvas);
		}
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final boolean touched = super.onTouchEvent(event); 
		if(touched){
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				pressed = true;
				postInvalidate();
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				pressed = false;
				postInvalidate();
				break;
			default:
				break;
			}
		}
		return touched;
	}
	
	public float getBorderWidth() {
		return borderWidth;
	}
}
