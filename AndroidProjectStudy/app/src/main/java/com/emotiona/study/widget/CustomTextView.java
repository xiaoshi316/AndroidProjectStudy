package com.emotiona.study.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.emotiona.study.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author sxshi on 2016/10/26.
 * @email:emotiona_xiaoshi@126.com
 * @describe:学习自定义控件第一步
 */

public class CustomTextView extends View {
    private  final String TAG=this.getClass().getSimpleName();
    private String mTextString;
    private int mTextColor;
    private float mTextSize;
    private int defaultTextColor = 0xFF000000;
    private float defaultTextSize = 14f;

    private Paint mPaint;
    private Rect bgRect;
    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /***
         * 获取自定义的属性
         */
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        mTextString = typedArray.getString(R.styleable.CustomTextView_textText);
        mTextColor = typedArray.getColor(R.styleable.CustomTextView_textColor, defaultTextColor);
        mTextSize = typedArray.getDimension(R.styleable.CustomTextView_textSize, defaultTextSize);
        typedArray.recycle();

        mPaint=new Paint();
        mPaint.setTextSize(mTextSize);
        bgRect=new Rect();
        mPaint.getTextBounds(mTextString,0,mTextString.length(),bgRect);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextString=randomText();
                postInvalidate();
            }
        });
    }

    /***
     * 随机产生数字
     * @return
     */
    private String randomText() {
        Random random=new Random();
        Set<Integer>set=new HashSet<>();
        while (set.size()<4){
            int randomInt=random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb=new StringBuffer();
        for (Integer i:set){
            sb.append(""+i);
        }
        return sb.toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode==MeasureSpec.EXACTLY){//Match_parent
            width=widthSize;
        }else {
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mTextString,0,mTextString.length(),bgRect);
            float textWidth=bgRect.width();
            int desired=(int)(getPaddingLeft()+textWidth+getPaddingRight());
            width=desired;
        }
        if (heightMode==MeasureSpec.EXACTLY){//Match_parent
            height=heightSize;
        }else {
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mTextString,0,mTextString.length(),bgRect);
            float textHeight=bgRect.height();
            int desired=(int)(getPaddingTop()+textHeight+getPaddingBottom());
            height=desired;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //设置背景为透明色
        mPaint.setColor(Color.TRANSPARENT);
        //先画背景
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        //绘制文字
        mPaint.setColor(mTextColor);
        canvas.drawText(mTextString,getWidth()/2-bgRect.width()/2,getHeight()/2+bgRect.height()/2,mPaint);
    }

}
