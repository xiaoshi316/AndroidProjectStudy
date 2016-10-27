package com.emotiona.study.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.emotiona.study.R;

/**
 * @author sxshi on 2016/10/27.
 * @email:emotiona_xiaoshi@126.com
 * @describe:实现一个ImageView能够添加文字描述，也方法缩小。
 */

public class CustomImageView extends View {
    private String mImageTitle;
    private int mImageTitleColor;
    private float mImageTitleSize;
    private Bitmap mBitmap;
    private int mImageScale;

    private final int IMAGE_SCALE_FITXY=1;
    private final int IMAGE_SCALE=0;

    private Paint mPaint;
    private Rect mTextBound;//文字背景矩形
    private Rect rect;//背景矩形

    private int widthSpec;//最终测量的宽度
    private int heightSpec;//最终测量的高度

    private int defaultTitleColor=0xFF000000;
    private float defaultTitleSize=16f;
    public CustomImageView(Context context) {
        this(context,null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        mImageTitle=typedArray.getString(R.styleable.CustomImageView_title);
        mImageTitleSize=typedArray.getDimension(R.styleable.CustomImageView_titleSize,defaultTitleSize);
        mImageTitleColor=typedArray.getColor(R.styleable.CustomImageView_titleColor,defaultTitleColor);
        mImageScale=typedArray.getInt(R.styleable.CustomImageView_imageScaleType,IMAGE_SCALE_FITXY);
        mBitmap= BitmapFactory.decodeResource(context.getResources(),typedArray.getResourceId(R.styleable.CustomImageView_image,0));
        typedArray.recycle();
        mPaint=new Paint();
        rect=new Rect();
        mTextBound=new Rect();
        mPaint.getTextBounds(mImageTitle,0,mImageTitle.length(),mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specMode=MeasureSpec.getMode(widthMeasureSpec);
        int specSize=MeasureSpec.getSize(widthMeasureSpec);
        //测量宽度
        if (specMode==MeasureSpec.EXACTLY){
            widthSpec=specSize;
        }else {
            //计算图片的宽度
            int desiredForImage=getPaddingLeft()+getPaddingRight()+mBitmap.getWidth();
            //计算文字的宽度
            int desiredForText=getPaddingLeft()+getPaddingRight()+mTextBound.width();
            if (specMode==MeasureSpec.AT_MOST){
                //取图片和文字最宽的一个
                int desire=Math.max(desiredForImage,desiredForText);
                //取 测量值和Macth_parent时候宽度的最小值
                widthSpec=Math.min(desire,specSize);
            }
        }
        //测量高度
        specMode=MeasureSpec.getMode(heightMeasureSpec);
        specSize=MeasureSpec.getSize(heightMeasureSpec);
        if (specMode==MeasureSpec.EXACTLY){
            heightSpec=specSize;
        }else {
            //计算图片+文字的高度  这里将文字放在图片下面
            int desire=getPaddingBottom()+getPaddingTop()+mBitmap.getHeight()+mTextBound.height();
            //计算文字的高度
            if (specMode==MeasureSpec.AT_MOST){
                //取 测量值和Macth_parent时候高度的最小值
                heightSpec=Math.min(desire,specSize);
            }
        }
        setMeasuredDimension(widthSpec,heightSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //首先绘制背景边框
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        //初始化图片背景矩形
        rect.left=getPaddingLeft();
        rect.right=widthSpec-getPaddingRight();
        rect.bottom=heightSpec-getPaddingBottom()-mTextBound.height();//图片的的高度应该减去文字的高度
        rect.top=getPaddingTop();

        mPaint.setColor(mImageTitleColor);
        mPaint.setTextSize(mImageTitleSize);
        mPaint.setStyle(Paint.Style.FILL);
        /***
         * 当文字的宽度大于图片的宽度，将文字多余部分省略
         */
        if (mTextBound.width()>widthSpec){
            TextPaint textPaint=new TextPaint(mPaint);
            String afterText= TextUtils.ellipsize(mImageTitle,textPaint,(float)(widthSpec-getPaddingLeft()-getPaddingRight()), TextUtils.TruncateAt.END).toString();
            canvas.drawText(afterText,getPaddingLeft(),heightSpec-getPaddingBottom(),mPaint);
        }else {
            //将字体水平居中 放于图片下面
            canvas.drawText(mImageTitle,widthSpec/2-mTextBound.width()/2,heightSpec-getPaddingBottom(),mPaint);
        }

        if (mImageScale==IMAGE_SCALE_FITXY){
            canvas.drawBitmap(mBitmap,null,rect,mPaint);
        }else {
            rect.left=widthSpec/2-mBitmap.getWidth()/2;
            rect.right=widthSpec/2+mBitmap.getWidth()/2;
            rect.bottom=(heightSpec-mTextBound.height())/2+mBitmap.getHeight()/2;
            rect.top=(heightSpec-mTextBound.height())/2-mBitmap.getHeight()/2;
            canvas.drawBitmap(mBitmap,null,rect,mPaint);
        }
    }
}
