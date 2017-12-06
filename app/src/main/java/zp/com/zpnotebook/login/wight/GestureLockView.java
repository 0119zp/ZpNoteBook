package zp.com.zpnotebook.login.wight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import zp.com.zpnotebook.login.manager.GestureLockManager;

/**
 * Created by zpan
 * data on 2016/7/26.
 */
public class GestureLockView extends View {

    /** 解锁密码key */
    private String key="";

    private OnGestureFinishListener onGestureFinishListener;

    /** 解锁圆点数组 */
    private LockCircle[] cycles;

    /** 存储触碰圆的序列 */
    private List<Integer> linedCycles = new ArrayList<Integer>();
    private List<String> linedCyclesPsw = new ArrayList<String>();

    //画笔
    /** 空心外圆 */
    private Paint paintNormal;
    /** 点击后内部圆 */
    private Paint paintInnerCycle;
    /** 画路径 */
    private Paint paintLines;
    private Path linePath = new Path();

    /** 当前手指X,Y位置 */
    private int eventX, eventY;
    /** 能否操控界面绘画 */
    private boolean canContinue = true;

    /** 验证结果 */
    private boolean result;

    /** 外圈最大虚线范围 */
    private float maxOutCicleErrorLine;

    private Timer timer =new Timer();

    /** 未选中内圈颜色 */
    private final int NORMAL_COLOR = Color.parseColor("#AACEF4");//内圈点
    /** 选中内圈颜色 */
    private final int TOUCH_NORMAL_COLOR = Color.parseColor("#157CFB");//内圈点
    /** 错误颜色 */
    private  int LINE_ERROE_COLOR = Color.parseColor("#fdeaef"); // 错误线
    private  int TOUCH_NORMAL_LINE_COLOR = Color.parseColor("#0078FF"); // 选中线颜色
    private  int TOUCH_ERROR_COLOR = Color.parseColor("#e92d5f"); // 选中圈颜色
    /** 未选中颜色,选中时颜色 */
    private  int TOUCH_COLOR = Color.parseColor("#78A8D4"); // 正常滑动外层圆颜色，连接线的颜色

    /** 设置圈圈的数量限制 */
    private int limitNum=4;
    private boolean isGone = true;//绘制手势密码不显示线条轨迹标志

    private Paint paintNorma2;
    private Path mArrowPath;//绘制三角形曲线的类
    private Context context;

    public GestureLockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public GestureLockView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GestureLockView(Context context) {
        this(context, null);
    }

    /**初始化*/
    public void init() {
        paintNormal = new Paint();
        paintNormal.setColor(NORMAL_COLOR);
        paintNormal.setAntiAlias(true);
        paintNormal.setStrokeWidth(3);
        paintNormal.setStyle(Paint.Style.FILL);

        paintNorma2 = new Paint();
        paintNorma2.setAntiAlias(true);
        paintNorma2.setStrokeWidth(3);
        paintNorma2.setStyle(Paint.Style.STROKE);

        paintInnerCycle=new Paint();
        paintInnerCycle.setAntiAlias(true);
        paintInnerCycle.setStyle(Paint.Style.FILL);


        paintLines = new Paint();
        paintLines.setAntiAlias(true);
        paintLines.setStyle(Paint.Style.STROKE);
        paintLines.setStrokeWidth(dip2px(context,10));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specMode= MeasureSpec.getMode(widthMeasureSpec);
        int spceSize= MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec= MeasureSpec.makeMeasureSpec((int) (spceSize*0.8+0.5f), specMode);
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    private float[] oXs = new float[9];
    private float[] oYs = new float[9];


    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i("TAG","手势密码区域数值left==="+left);
        Log.i("TAG","手势密码区域数值top==="+top);
        Log.i("TAG","手势密码区域数值right==="+right);
        Log.i("TAG","手势密码区域数值bottom==="+bottom);
//        int perWidthSize= getWidth() / 7;
//        int perHeightSize=getHeight()/7;
        oXs[1]=oXs[7]= oXs[4] = (float)(getWidth() / 2);
        Log.i("TAG","个手势密码区域第二排坐标Ox==="+(float)(getWidth() / 2));
        oXs[0]=oXs[3]= oXs[6] = ((float)(getWidth() / 2- dip2px(context,90)));
        Log.i("TAG","个手势密码区域第一排坐标Ox==="+(float)(getWidth() / 2- dip2px(context,90)));
        oXs[2]=oXs[5]= oXs[8] = ((float)(getWidth() / 2+ dip2px(context,90)));
        Log.i("TAG","个手势密码区域第三排坐标Ox==="+(float)(getWidth() / 2+ dip2px(context,90)));

        oYs[3]=oYs[4]= oYs[5] = (float)(getHeight() / 2);
        Log.i("TAG","个手势密码区域第二排坐标Oy==="+(float)(getHeight() / 2));
        oYs[0]=oYs[1]= oYs[2] = ((float)(getHeight() / 2- dip2px(context,90)));
        Log.i("TAG","个手势密码区域第一排坐标Oy==="+(float)(getHeight() / 2- dip2px(context,90)));
        oYs[6]=oYs[7]= oYs[8] = ((float)(getHeight() / 2+ dip2px(context,90)));
        Log.i("TAG","个手势密码区域第三排坐标Oy==="+(float)(getHeight() / 2+ dip2px(context,90)));
        maxOutCicleErrorLine = dip2px(context,33.5f);
        /**初始化圆的参数*/
        if(cycles==null&&(getWidth()>0)&&(getHeight()>0)){
            cycles=new LockCircle[9];
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++)
                {
                    LockCircle lockCircle=new LockCircle();
                    lockCircle.setSequence(i*3+j);//设置圈圈对应的数字
                    lockCircle.setNum(String.valueOf(GestureLockManager.gesturePwd.charAt(i*3+j)));//设置圈圈对应的数字
                    lockCircle.setOx(oXs[i*3+j]);
//                    AresLog.i("TAG",lockCircle.getSequence()+"个手势密码区域数值Ox==="+lockCircle.getOx());
                    lockCircle.setOy(oYs[i*3+j]);
//                    AresLog.i("TAG",lockCircle.getSequence()+"个手势密码区域数值Oy==="+lockCircle.getOy());
                    lockCircle.setR(dip2px(context,32.5f));
//                    AresLog.i("TAG",lockCircle.getSequence()+"个手势密码区域数值R==="+lockCircle.getR());
                    //设置每个手势需要的小三角坐标
                    lockCircle.setOxOne(lockCircle.getOx()+lockCircle.getR()+dip2px(context,6f));
                    lockCircle.setOyOne(lockCircle.getOy());
                    lockCircle.setOxTwo(lockCircle.getOx()+lockCircle.getR());
                    lockCircle.setOyTwo(lockCircle.getOy()+dip2px(context,5f));
                    lockCircle.setOxThree(lockCircle.getOx()+lockCircle.getR());
                    lockCircle.setOyThree(lockCircle.getOy()-dip2px(context,5f));
                    cycles[i*3+j]=lockCircle;
                }
            }
        }

    }

    public void setPsw(){
        for(int i=0;i<3;i++) {
            for(int j=0;j<3;j++) {
                cycles[i*3+j].setNum(String.valueOf(GestureLockManager.gesturePwd.charAt(i*3+j)));//设置圈圈对应的数字
            }
        }
    }

    public int getLimitNum() {
        return limitNum;
    }

    /**
     * 设置手势密码连接的数量值
     * @param limitNum
     */
    public void setLimitNum(int limitNum){
        this.limitNum=limitNum;
    }

    public void setIsGone(boolean isGone) {
        this.isGone = isGone;
    }
    /**
     * 设置key值留验证用，要是没有设置就代表是设置手势密码
     * @param key
     */
    public void setKey(String key){
        this.key=key;
    }

    public void setOnGestureFinishListener(OnGestureFinishListener onGestureFinishListener) {
        this.onGestureFinishListener = onGestureFinishListener;
    }

    /**手势输入完成后回调接口*/
    public interface OnGestureFinishListener
    {
        /**手势输入完成后回调函数*/
        public void OnGestureFinish(boolean success, String key, String adapterNum);
    }

    private List<LockCircle> mChoice = new ArrayList<LockCircle>();

    /**监听手势*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(canContinue){
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    eventX=(int) event.getX();
                    eventY=(int) event.getY();
                    for(int i=0;i<cycles.length;i++){
                        if(cycles[i]!=null&&cycles[i].isPointIn(eventX, eventY)){
                            cycles[i].setOnTouch(true);
                            if(!linedCycles.contains(cycles[i].getSequence())){
                                linedCycles.add(cycles[i].getSequence());
                                linedCyclesPsw.add(cycles[i].getNum());
                                mChoice.add(cycles[i]);
                            }
                            if(isGone){
                                for (int j = 0; j < mChoice.size() - 1; j++){
                                    LockCircle firstLockCircle = mChoice.get(j);
                                    LockCircle nextLockCircle = mChoice.get(j + 1);
                                    int dx = (int)(nextLockCircle.getOx() - firstLockCircle.getOx());
                                    int dy = (int)(nextLockCircle.getOy() - firstLockCircle.getOy());
                                    int angle = (int) Math.toDegrees(Math.atan2(dy, dx));
                                    firstLockCircle.setArrowDegree(angle);
                                }
                            }

                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //手指离开暂停触碰
                    canContinue=false;
                    StringBuffer stringBuffer=new StringBuffer();
                    StringBuffer stringBufferAdapter=new StringBuffer();
                    for(int i=0;i<linedCycles.size();i++){
                        stringBufferAdapter.append(linedCycles.get(i));
                    }
                    for(int i=0;i<linedCyclesPsw.size();i++){
                        stringBuffer.append(linedCyclesPsw.get(i));
                    }

                    if(!TextUtils.isEmpty(key)) {
                        result = key.equals(stringBuffer.toString());
                    }else
                        result=true;
                    if(linedCycles.size()<limitNum){
                        result=false;
                    }

                    if(onGestureFinishListener!=null&&linedCyclesPsw.size()>0){
                        Log.i("TAG","手势密码===="+stringBuffer.toString());
                        onGestureFinishListener.OnGestureFinish(result, stringBuffer.toString(),stringBufferAdapter.toString());
                    }
                    if(!canContinue && !result){
                        if(!isGone){
                            eventX = eventY = 0;
                            for(int i=0;i<9;i++)
                            {
                                cycles[i].setOnTouch(false);
                            }
                            mChoice.clear();
                            linedCycles.clear();
                            linedCyclesPsw.clear();
                            linePath.reset();
                            canContinue = true;
                            invalidate();
                        }
                    }

                    break;
            }
            if(isGone){
                //显示轨迹的情况下才做界面刷新
                invalidate();
            }

        }
        return true;
    }

    /**
     * @param normal
     * 根据接口返回的接口，是否绘制错误还是正常图案
     */
    public void drawPath(boolean normal){
        if(normal){
            //绘制最原始界面
            eventX = eventY = 0;
            for(int i=0;i<9;i++)
            {
                cycles[i].setOnTouch(false);
                cycles[i].setArrowDegree(-1);
            }
            mChoice.clear();
            linedCycles.clear();
            linedCyclesPsw.clear();
            linePath.reset();
            canContinue = true;
            errorRadio = 0.0f;
            alpha = 255;
            paintNorma2.setPathEffect(null);
            postInvalidate();//在非ui线程刷新界面
        }else{
            canContinue = false;
            result = false;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(errorRadio<maxOutCicleErrorLine){
                        errorRadio += errorRadio*0.01f;
                        alpha -= 75;
                        if(alpha<40){
                            alpha = 40;
                        }
                        postInvalidate();//在非ui线程刷新界面
                    }
                }
            }, 0);
        }
    }

    private int alpha = 255;

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("TAG","onDraw");
        super.onDraw(canvas);
        if(isGone){
            Log.i("TAG","手势密码显示轨迹");
            if (!canContinue && !result) {
                drawLine(canvas, LINE_ERROE_COLOR);
            } else {
                drawLine(canvas, TOUCH_NORMAL_LINE_COLOR);
            }
            int cycleSize = cycles.length;
            for (int i = 0; i < cycleSize; i++) {

                // 画完并且错误
                if (!canContinue && !result)
                {
                    if(errorRadio == 0.0f){
                        errorRadio = cycles[i].getR();
                    }
                    if (cycles[i].isOnTouch()) {
                        drawOutsideErrorCycle(cycles[i], canvas, alpha,errorRadio);
                        drawInnerCycle(cycles[i], canvas, TOUCH_ERROR_COLOR);
                    } else{
                        drawOutsideCycle(cycles[i], canvas, NORMAL_COLOR);
                    }
                }
                //绘画中
                else {
                    if(null!=cycles[i]){
                        if (cycles[i].isOnTouch()) {
                            if(isGone){
                                Paint paint=new Paint();
	                    /*去锯齿*/
                                paint.setAntiAlias(true);
	                    /*设置paint的颜色*/
                                paint.setColor(TOUCH_NORMAL_COLOR);
	                    /*设置paint的 style 为STROKE：空心*/
                                paint.setStyle(Paint.Style.STROKE);
	                    /*设置paint的外框宽度*/
                                paint.setStrokeWidth(2);
                                //外圈外面绘制小三角形
                                mArrowPath = new Path();
                                mArrowPath.moveTo(cycles[i].getOxOne(), cycles[i].getOyOne());
                                mArrowPath.lineTo(cycles[i].getOxTwo(), cycles[i].getOyTwo());
                                mArrowPath.lineTo(cycles[i].getOxThree(), cycles[i].getOyThree());
                                mArrowPath.close();
                                mArrowPath.setFillType(Path.FillType.WINDING);
                                if (cycles[i].getArrowDegree() != -1) {
                                    paint.setStyle(Paint.Style.FILL);
                                    canvas.save();
                                    Log.i("TAG","小三角角标数值===="+cycles[i].getArrowDegree());
                                    canvas.rotate(cycles[i].getArrowDegree(), cycles[i].getOx(), cycles[i].getOy()); // 旋转的不是画布,而是画布的坐标系
                                    canvas.drawPath(mArrowPath, paint);
                                    canvas.restore();
                                }
                                drawOutsideCycle02(cycles[i], canvas, TOUCH_COLOR);
                                drawInnerCycle(cycles[i], canvas, TOUCH_NORMAL_COLOR);
                            }

                        } else
                            drawOutsideCycle(cycles[i], canvas, NORMAL_COLOR);
                    }
                }
            }
            //每次for循环完刷新一遍徐线图
            if(!canContinue && !result){
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(errorRadio<maxOutCicleErrorLine){
                            errorRadio += errorRadio*0.01f;
                            alpha -= 75;
                            if(alpha<40){
                                alpha = 40;
                            }
                            postInvalidate();//在非ui线程刷新界面
                        }else{
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    eventX = eventY = 0;
                                    for(int i=0;i<9;i++)
                                    {
                                        cycles[i].setOnTouch(false);
                                        cycles[i].setArrowDegree(-1);
                                    }
                                    mChoice.clear();
                                    linedCycles.clear();
                                    linedCyclesPsw.clear();
                                    linePath.reset();
                                    canContinue = true;
                                    errorRadio = 0.0f;
                                    alpha = 255;
                                    paintNorma2.setPathEffect(null);
                                    postInvalidate();//在非ui线程刷新界面
                                }
                            }, 0);
                        }

                    }
                }, 20);
            }

        }else{
            Log.i("TAG","手势密码不显示轨迹");
            //只绘制初始化内圈
            eventX = eventY = 0;
            for(int i=0;i<9;i++)
            {
                cycles[i].setOnTouch(false);
            }
            mChoice.clear();
            linedCycles.clear();
            linedCyclesPsw.clear();
            linePath.reset();
            canContinue = true;
            int cycleSize = cycles.length;
            for (int i = 0; i < cycleSize; i++){
                drawOutsideCycle(cycles[i], canvas, NORMAL_COLOR);
            }
        }
    }

    /**
     * 擦除痕迹
     */
    public void clearPath(){
        mChoice.clear();
        linedCycles.clear();
        linedCyclesPsw.clear();
        linePath.reset();
        canContinue = true;
        errorRadio = 0.0f;
        alpha = 255;
        paintNorma2.setPathEffect(null);
        postInvalidate();//在非ui线程刷新界面
    }

    /**画空心圆*/
    private void drawOutsideCycle(LockCircle lockCircle, Canvas canvas, int color)
    {
        canvas.drawCircle(lockCircle.getOx(), lockCircle.getOy(), dip2px(context,5), paintNormal);
    }

    float errorRadio = 0.0f;

    /**绘制外层扩散错误虚线框
     * @param lockCircle
     * @param canvas
     * @param alpha
     * @param circleErrorRadio
     */
    private void drawOutsideErrorCycle(final LockCircle lockCircle, final Canvas canvas, int alpha, float circleErrorRadio){
        paintNorma2.setARGB(alpha,229,49,97);
        PathEffect effects = new DashPathEffect(new float[]{9,9,9,9},1);
        paintNorma2.setPathEffect(effects);
        canvas.drawCircle(lockCircle.getOx(), lockCircle.getOy(),
                circleErrorRadio, paintNorma2);

    }

    /**画外层边圆*/
    private void drawOutsideCycle02(final LockCircle lockCircle, final Canvas canvas, int color)
    {
        paintNorma2.setColor(color);
        canvas.drawCircle(lockCircle.getOx(), lockCircle.getOy(),
                lockCircle.getR(), paintNorma2);
    }

    /**画中心圆圆*/
    private void drawInnerCycle(LockCircle myCycle, Canvas canvas, int color) {
        paintInnerCycle.setColor(color);
        canvas.drawCircle(myCycle.getOx(), myCycle.getOy(), dip2px(context,5),
                paintInnerCycle);
    }

    /**画横线*/
    private void drawLine(Canvas canvas, int color)
    {
        //构建路径
        linePath.reset();
        if (linedCycles.size() > 0) {
            int size=linedCycles.size();
            for (int i = 0; i < size; i++) {
                int index = linedCycles.get(i);
                float x = cycles[index].getOx();
                float y = cycles[index].getOy();
                if (i == 0) {
                    linePath.moveTo(x,y);
                } else {
                    linePath.lineTo(x,y);
                }
            }
            if (canContinue) {
                linePath.lineTo(eventX, eventY);
            }else {
                linePath.lineTo(cycles[linedCycles.get(linedCycles.size()-1)].getOx(), cycles[linedCycles.get(linedCycles.size()-1)].getOy());
            }
            paintLines.setColor(color);
            paintLines.setAlpha(15);
            canvas.drawPath(linePath, paintLines);
        }
    }

    /**
     * 每个圆点类
     *
     * @author rxx
     *
     * 2014年12月12日  上午10:05:48
     */
    class LockCircle {
        /**圆心横坐标*/
        private float ox;
        /**圆心纵坐标*/
        private float oy;
        /**半径长度*/
        private float r;
        /**代表数值*/
        private String num;
        /**是否选择:false=未选中*/
        private boolean onTouch;

        /**
         * 设置小三角的角度
         * @param degree
         */

        private int mArrowDegree = -1;

        public void setArrowDegree(int degree) {
            this.mArrowDegree = degree;
        }

        public int getArrowDegree() {
            return this.mArrowDegree;
        }

        public Integer getSequence() {
            return sequence;
        }

        public void setSequence(Integer sequence) {
            this.sequence = sequence;
        }

        private Integer sequence;

        private float oyOne;
        private float oyTwo;

        public float getOyThree() {
            return oyThree;
        }

        public void setOyThree(float oyThree) {
            this.oyThree = oyThree;
        }

        public float getOyOne() {
            return oyOne;
        }

        public void setOyOne(float oyOne) {
            this.oyOne = oyOne;
        }

        public float getOyTwo() {
            return oyTwo;
        }

        public void setOyTwo(float oyTwo) {
            this.oyTwo = oyTwo;
        }

        private float oyThree;

        public float getOxOne() {
            return oxOne;
        }

        public void setOxOne(float oxOne) {
            this.oxOne = oxOne;
        }

        private float oxOne;

        public float getOxTwo() {
            return oxTwo;
        }

        public void setOxTwo(float oxTwo) {
            this.oxTwo = oxTwo;
        }

        public float getOxThree() {
            return oxThree;
        }

        public void setOxThree(float oxThree) {
            this.oxThree = oxThree;
        }

        private float oxTwo;
        private float oxThree;

        public float getOx() {
            return ox;
        }
        public void setOx(float ox) {
            this.ox = ox;
        }
        public float getOy() {
            return oy;
        }
        public void setOy(float oy) {
            this.oy = oy;
        }
        public void setOy(int oy) {
            this.oy = oy;
        }
        public float getR() {
            return r;
        }
        public void setR(float r) {
            this.r = r;
        }
        public String getNum() {
            return num;
        }
        public void setNum(String num) {
            this.num = num;
        }
        public boolean isOnTouch() {
            return onTouch;
        }
        public void setOnTouch(boolean onTouch) {
            this.onTouch = onTouch;
        }

        /**判读传入位置是否在圆心内部*/
        public boolean isPointIn(int x, int y) {
            double distance = Math.sqrt((x - ox) * (x - ox) + (y - oy) * (y - oy));
            return distance < r;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if(null!=context){
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }else{
            return 0;
        }
    }
}
