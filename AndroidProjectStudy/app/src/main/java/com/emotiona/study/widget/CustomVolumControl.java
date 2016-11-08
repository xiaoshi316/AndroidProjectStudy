package com.emotiona.study.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.emotiona.study.R;

/**
 * @author sxshi on 2016/11/7.
 * @email:emotiona_xiaoshi@126.com
 * @describe:自定义音量控件
 */

public class CustomVolumControl extends View {
    private int mFirstColor;//第一种颜色
    private int mSecondColor;//第二种颜色
    private int mCircleWidth;//圆圈的宽度
    private Bitmap mBg;//背景图片
    private int mSplitSize;//块状的宽度
    private int mCount;//块状的个数

    private int defaultFirstColor = Color.GRAY;
    private int defaultSecondColor = Color.GREEN;

    private int currCount = 1;//当前音量
    private Paint mPaint;//画笔
    private Rect mRect;


    public CustomVolumControl(Context context) {
        this(context, null);
    }

    public CustomVolumControl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVolumControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomVolumControl);
        mFirstColor = typedArray.getColor(R.styleable.CustomVolumControl_oneColor, defaultFirstColor);
        mSecondColor = typedArray.getColor(R.styleable.CustomVolumControl_twoColor, defaultSecondColor);
        mCircleWidth = typedArray.getDimensionPixelSize(R.styleable.CustomVolumControl_circleWidth, 20);
        mSplitSize = typedArray.getDimensionPixelSize(R.styleable.CustomVolumControl_splitSize, 20);
        mBg = BitmapFactory.decodeResource(context.getResources(), typedArray.getResourceId(R.styleable.CustomVolumControl_bg, 0));
        mCount = typedArray.getInt(R.styleable.CustomVolumControl_doCount, 20);
        typedArray.recycle();

        mPaint = new Paint();
        mRect = new Rect();
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int specMode = MeasureSpec.getMode(widthMeasureSpec);
//        int specSize = MeasureSpec.getSize(widthMeasureSpec);
//        int mWidthSpec;
//        int mHeightSpec;
//
//        if (specMode == MeasureSpec.EXACTLY) {
//            mWidthSpec = specSize;
//        } else {
//            mWidthSpec =getPaddingLeft()+getPaddingRight()+ mBg.getWidth() + mCircleWidth;
//        }
//        specMode = MeasureSpec.getMode(heightMeasureSpec);
//        specSize = MeasureSpec.getSize(heightMeasureSpec);
//        if (specMode == MeasureSpec.EXACTLY) {
//            mHeightSpec = specSize;
//        } else {
//            mHeightSpec = getPaddingBottom()+getPaddingTop()+mBg.getHeight() + mCircleWidth;
//        }
//        setMeasuredDimension(mWidthSpec, mHeightSpec);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);//清楚锯齿
        mPaint.setStrokeWidth(mCircleWidth);//设置画笔大小
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置断开线头为圆头
        mPaint.setStyle(Paint.Style.STROKE);//设置空心

        int center = getWidth() / 2;//获取圆心的坐标
        int radius = center - mCircleWidth / 2;//圆的半径
        //画块状去
        drawOval(canvas, center, radius);
        //画出中间的图片
        drawCenterBitmp(canvas, radius);
    }

    /***
     * 画出中间的音量图片
     *
     * @param canvas
     * @param radius
     */
    private void drawCenterBitmp(Canvas canvas, int radius) {
        int relRadius = radius - mCircleWidth / 2;
        mRect.left = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;
        mRect.top = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;
        mRect.bottom = (int) (relRadius + Math.sqrt(2) * relRadius);
        mRect.right = (int) (relRadius + Math.sqrt(2) * relRadius);

        if (mBg.getWidth() < Math.sqrt(2) * relRadius) {
            mRect.left = (int) (mRect.left + Math.sqrt(2) * relRadius * 1.0f / 2 - mBg.getWidth() * 1.0f / 2);
            mRect.top = (int) (mRect.top + Math.sqrt(2) * relRadius * 1.0f / 2 - mBg.getHeight() * 1.0f / 2);
            mRect.bottom = (int) (mRect.left + mBg.getHeight());
            mRect.right = (int) (mRect.top + mBg.getWidth());
        }
        canvas.drawBitmap(mBg, null, mRect, mPaint);
    }

    /***
     * 画块状
     *
     * @param canvas
     * @param center
     * @param radius
     */
    private void drawOval(Canvas canvas, int center, int radius) {
        float itemSize = (360 * 1.0f - mSplitSize * mCount) / mCount;//计算出块之间的距离
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        mPaint.setColor(mFirstColor);
        for (int i = 0; i < mCount; i++) {
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint);
        }
        mPaint.setColor(mSecondColor);
        for (int i = 0; i < currCount; i++) {
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint);
        }
    }
    public void up(){
        if (currCount<=mCount){
            currCount++;
            postInvalidate();
        }
    }

    public void down(){
        if (currCount>1){
            currCount--;
            postInvalidate();
        }
    }
    private int xDown,xUp;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xDown=(int)event.getY();
                break;
            case MotionEvent.ACTION_UP:
                xUp=(int)event.getY();
                if (xUp>xDown){
                    down();
                }else {
                    up();
                }
                break;
        }
        return true;
    }
}
