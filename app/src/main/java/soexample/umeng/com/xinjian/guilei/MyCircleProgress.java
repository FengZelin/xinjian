package soexample.umeng.com.xinjian.guilei;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import soexample.umeng.com.xinjian.R;


/**
 * date:2018/11/21
 * author:李晓亮:Shinelon
 * function:
 */
public class MyCircleProgress extends View {
    public int progress  = 0;//进度实际值,当前进度
    /**
     * 自定义控件属性，可灵活的设置圆形进度条的大小、颜色、类型等
     */
    private int mR;//圆半径，决定圆大小
    private int bgColor;//圆或弧的背景颜色
    private int fgColor;//圆或弧的前景颜色，即绘制时的颜色
    private int drawStyle; //绘制类型 FILL画圆形进度条，STROKE绘制弧形进度条
    private int strokeWidth;//STROKE绘制弧形的弧线的宽度
    private int max=100;//最大值，设置进度的最大值
    private Paint mPaint;
    private boolean opt;

    public MyCircleProgress(Context context) {
        this(context,null);
    }

    public MyCircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public MyCircleProgress(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        mR=tArray.getInteger(R.styleable.CircleProgressBar_r,30);
        bgColor=tArray.getColor(R.styleable.CircleProgressBar_bgColor, Color.GRAY);
        fgColor=tArray.getColor(R.styleable.CircleProgressBar_fgColor, Color.RED);
        drawStyle=tArray.getInt(R.styleable.CircleProgressBar_drawStyle, 0);
        strokeWidth=tArray.getInteger(R.styleable.CircleProgressBar_strokeWidth, 10);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //mPaint.setStyle(Paint.Style.STROKE);
        initProperty(attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2; // 圆心位置
        int height = getHeight();
        mPaint.setColor(bgColor);
        mPaint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(center, center, mR, mPaint);
        // 绘制圆环
        mPaint.setColor(fgColor);
        if(drawStyle==0){
            mPaint.setStyle(Paint.Style.STROKE);
            opt=false;
        }else{
            mPaint.setStyle(Paint.Style.STROKE);
            opt=true;
        }
        int top = (center - mR);
        int bottom = (center + mR);
        RectF oval = new RectF(top, top, bottom, bottom);
        canvas.drawArc(oval, 270, 360*progress/max, opt, mPaint);
        mPaint.setColor(Color.BLACK);
        String text=progress+"%";
        float stringWidth = mPaint.measureText(text);
        float x =(getWidth()-stringWidth)/2;
        //Log.e("Lixiaoliang", "小亮亮"+progress);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(70f);
        canvas.drawText(text,x,height/2,mPaint);
    }

    private void initProperty(AttributeSet attrs) {

    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     */
    public synchronized void setProgress(int progress) {
        if(progress<0){
            progress=0;
        }else if(progress>max){
            progress=max;
        }else{
            this.progress = progress;
        }
    }
    public int getMax() {
        return max;    }
}
