package com.emotiona.study.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.emotiona.study.R;

/**
 * @author sxshi on 2016/10/31.
 * @email:emotiona_xiaoshi@126.com
 * @describe:Describe the function  of the current class
 */

public class CustomProgressBar extends View{
    private int firstColor;
    private int secondColor;
    private int progress;

    private int defaultFirstColor= Color.RED;
    private int defaultSecondColor=Color.GREEN;

    private Paint mPaint;

    private boolean isNext=false;

    private int mSpeed=20;//速度
    private int mCycleWidth;//圆圈的宽度
    public CustomProgressBar(Context context) {
        this(context,null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar);
        firstColor=typedArray.getColor(R.styleable.CustomProgressBar_firstColor,defaultFirstColor);
        secondColor=typedArray.getColor(R.styleable.CustomProgressBar_secondColor,defaultSecondColor);
        progress=typedArray.getInt(R.styleable.CustomProgressBar_mProgress,20);
        mCycleWidth=typedArray.getDimensionPixelSize(R.styleable.CustomProgressBar_cyclerWidth,20);
        typedArray.recycle();
        mPaint=new Paint();
        new ProgressThread().start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center=getWidth()/2;//圆心的坐标
        int radius=center-mCycleWidth/2;
        mPaint.setStrokeWidth(mCycleWidth);//設置画笔的宽度
        mPaint.setStyle(Paint.Style.STROKE);//设置空心
        mPaint.setAntiAlias(true);//清楚锯齿
        //四个顶点确定圆弧的范围。
        RectF rectF=new RectF(center-radius,center-radius,center+radius,center+radius);

        if (isNext){
            mPaint.setColor(firstColor);
            canvas.drawCircle(center,center,radius,mPaint);
            mPaint.setColor(secondColor);
            canvas.drawArc(rectF,-90,progress,false,mPaint);
        }else {//交换颜色
            mPaint.setColor(secondColor);
            canvas.drawCircle(center,center,radius,mPaint);
            mPaint.setColor(firstColor);
            canvas.drawArc(rectF,-90,progress,false,mPaint);
        }
    }

    /***
     * 另起一个线程控制是否绘制进度
     */
    private class ProgressThread extends Thread{
        @Override
        public void run() {
            while (true){
                progress++;
                if (progress==360){
                    progress=0;
                    if (!isNext) isNext = true;
                    else isNext=false;
                }
                postInvalidate();
                try {
                    Thread.sleep(mSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
