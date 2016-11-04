package com.salesman.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.salesman.R;
import com.salesman.common.Constant;

/**
 * Created by LiHuai on 2016/07/05.
 */
public class ArcProgressBar extends View {

    private final Context mContext;
    private int maxProgressbarAngle;                        //最大弧形角度
    private int startCircleAngle;                           //弧形开始的角度
    private int centerX;                                    //中心点X坐标
    private int centerY;                                    //中心点Y坐标
    // 属性
    private int mProgressBarColor;                          // 圆弧颜色
    private float mProgressbarWidth;
    private float textSize;
    private int textColor;
    private int mProgressbarUnreachedColor;
    private int maxValue;
    private float numSize;
    private int numColor;
    private String mTitleText;
    private String mTextUnit;
    private boolean mShowArrow;

    private Paint mCircleProgressBarPaint;                   // 圆弧画笔
    private Paint mCircleProgressBarUnreachedPaint;          // 底部圆弧画笔
    private Paint mTextPaint;                                // 文字画笔
    private Paint mNumPaint;                                 // 数字画笔
    private Paint mCentraCirclePaint;                        // 中心圆画笔
    private Paint mCentraArcPaint;                           // 中心圆弧画笔

    private RectF rectF;                    // 外部矩形
    private RectF rectF2;                   // 内部矩形

    private int radius;                     // 外部圆半径
    private int radius2;                    // 内部圆半径
    private int halfProgressbarWidth;       // 外圆弧宽度一半
    private int progress;                   //currentProgress变化的最大值
    private int currentProgress;            //随着动画变化
    private boolean animateFinish = false;
    private int animationDuring = 1000;

    public ArcProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);
        initView();
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ArcProgressBar);
        this.mProgressBarColor = typedArray.getColor(R.styleable.ArcProgressBar_progressbar_color, Color.BLUE);
        this.mProgressbarWidth = typedArray.getDimension(R.styleable.ArcProgressBar_progressbar_width, dipToPx(10));
        this.mTitleText = typedArray.getString(R.styleable.ArcProgressBar_title_text);
        this.textSize = typedArray.getDimension(R.styleable.ArcProgressBar_text_size, dipToPx(15));
        this.textColor = typedArray.getColor(R.styleable.ArcProgressBar_text_color, Color.BLACK);
        this.mProgressbarUnreachedColor = typedArray.getColor(R.styleable.ArcProgressBar_progressbar_unreached_color, Color.GRAY);
        this.maxValue = typedArray.getInteger(R.styleable.ArcProgressBar_max_value, 100);
        this.mTextUnit = typedArray.getString(R.styleable.ArcProgressBar_text_unit);
        this.numSize = typedArray.getDimension(R.styleable.ArcProgressBar_progress_num_size, dipToPx(80));
        this.numColor = typedArray.getColor(R.styleable.ArcProgressBar_progress_num_color, Color.BLACK);
        this.mShowArrow = typedArray.getBoolean(R.styleable.ArcProgressBar_show_arrow, true);
        setCircleAngle(typedArray.getInteger(R.styleable.ArcProgressBar_max_progressbar_angle, 300));
        typedArray.recycle();
    }

    private void initView() {
        mCircleProgressBarPaint = new Paint();
        mCircleProgressBarPaint.setStyle(Paint.Style.STROKE);
        mCircleProgressBarPaint.setColor(mProgressBarColor);
        mCircleProgressBarPaint.setStrokeWidth(mProgressbarWidth);        // 设置画笔的宽度
        mCircleProgressBarPaint.setStrokeCap(Paint.Cap.ROUND);            // 画笔末端的效果，我这里设置的是圆形
        mCircleProgressBarPaint.setAntiAlias(true);                       // 抗锯齿

        mCircleProgressBarUnreachedPaint = new Paint();
        mCircleProgressBarUnreachedPaint.setStyle(Paint.Style.STROKE);
        mCircleProgressBarUnreachedPaint.setColor(mProgressbarUnreachedColor);
        mCircleProgressBarUnreachedPaint.setStrokeCap(Paint.Cap.ROUND);
        mCircleProgressBarUnreachedPaint.setStrokeWidth(mProgressbarWidth);
        mCircleProgressBarUnreachedPaint.setAntiAlias(true);

        // 中心圆画笔
        mCentraCirclePaint = new Paint();
        mCentraCirclePaint.setColor(Color.WHITE);
        mCentraCirclePaint.setAntiAlias(true);
        // 中心圆弧画笔
        mCentraArcPaint = new Paint();
        mCentraArcPaint.setStyle(Paint.Style.STROKE);
        mCentraArcPaint.setStrokeWidth(2f);
        mCentraArcPaint.setColor(getResources().getColor(R.color.color_cccccc));
        mCentraArcPaint.setAntiAlias(true);
        //除了数字的其他文字
        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);                        // 设置文字的基准（横向）
        mTextPaint.setAntiAlias(true);
        //数字
        mNumPaint = new Paint();
        mNumPaint.setColor(numColor);
        mNumPaint.setTextSize(numSize);
        mNumPaint.setTextAlign(Paint.Align.CENTER);
        mNumPaint.setAntiAlias(true);
        AssetManager mgr = mContext.getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, Constant.CUSTOM_FONTS); // 自定义字体
        mNumPaint.setTypeface(tf);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 第一次draw的时候，构造一个`RectF`矩形，后面可以在这个矩形里面画圆弧
        if ((centerX & centerY) == 0) {
            halfProgressbarWidth = (int) (mProgressbarWidth / 2);
            radius = (int) (getWidth() / 2 - mProgressbarWidth / 2);        // 半径
            centerX = radius + (int) mProgressbarWidth / 2;                 // X轴
            centerY = radius + (int) mProgressbarWidth / 2;                 // Y轴
            // 外部矩形
            rectF = new RectF();
            rectF.left = mProgressbarWidth / 2;
            rectF.top = mProgressbarWidth / 2;
            rectF.right = getWidth() - mProgressbarWidth / 2;
            rectF.bottom = getHeight() - mProgressbarWidth / 2;
            // 内部矩形
            rectF2 = new RectF();
            rectF2.left = halfProgressbarWidth * 4;
            rectF2.top = halfProgressbarWidth * 4;
            rectF2.right = getWidth() - halfProgressbarWidth * 4;
            rectF2.bottom = getWidth() - halfProgressbarWidth * 4;
            radius2 = (getWidth() - halfProgressbarWidth * 8) / 2;
        }
        // 我的画笔，画中心圆饼
        canvas.drawOval(rectF2, mCentraCirclePaint);// 以一个矩形为边界画椭圆
//        int radius2 = radius - (int) mProgressbarWidth;
//        canvas.drawCircle(centerX, centerY, radius2, mPaint);// 以中心坐标画半径的圆
        // 画中心圆弧
        canvas.drawArc(rectF2, 0f, 360f, false, mCentraArcPaint);// 以一个矩形为边界画圆弧
        // 画三角图片
        if (mShowArrow) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_down_grey);
            canvas.drawBitmap(bitmap, centerX - (bitmap.getWidth() / 2), centerY + (radius2 / 4 * 3), mCentraCirclePaint);
        }
        // 画后面灰色的圆弧
        canvas.drawArc(rectF, startCircleAngle, maxProgressbarAngle, false, mCircleProgressBarUnreachedPaint);
        if (currentProgress >= progress) {
            animateFinish = true;
        }
        // 画progressbar的圆弧
        canvas.drawArc(rectF, startCircleAngle, getProgressAngle(), false, mCircleProgressBarPaint);
        // 把文字画上去
        if (!TextUtils.isEmpty(mTitleText)) {
//            canvas.drawText(mTitleText, centerX, centerY - 5 * (numSize / 3), mTextPaint);
//            canvas.drawText(String.valueOf(currentProgress), centerX, centerY + (numSize / 3), mNumPaint);
//            canvas.drawText(mTextUnit, centerX, centerY + 4 * (numSize / 3 + textSize / 3), mTextPaint);
            canvas.drawText(mTitleText, centerX, centerY - radius2 / 2, mTextPaint);
        }
        canvas.drawText(String.valueOf(maxValue), centerX, centerY + (numSize / 3), mNumPaint);
        if (!TextUtils.isEmpty(mTextUnit)) {
            canvas.drawText(mTextUnit, centerX, centerY + radius2 / 3 * 2, mTextPaint);
        }
        if (!animateFinish) {
            invalidate();
        }
    }

    public void setProgress(int value) {
        if (value > maxValue) {
            value = maxValue;
        }
        if (value < 0) {
            value = 0;
        }
        this.progress = value;
        setAnimation(0, value, animationDuring);
    }

    private void setAnimation(int start, int end, int during) {
        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(during);
        animator.setIntValues(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentProgress = (int) animation.getAnimatedValue();
            }
        });
        animateFinish = false;
        postInvalidate();
        animator.start();
    }

    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    private float getProgressAngle() {
        float angle = (currentProgress * 1f) / maxValue * maxProgressbarAngle;
        return angle == 0 ? 0.001f : angle;
    }

    private void setCircleAngle(int angle) {
        if (angle > 360) {
            this.maxProgressbarAngle = angle % 360;// 取余
        } else {
            this.maxProgressbarAngle = angle;
            startCircleAngle = -maxProgressbarAngle / 2 - 90;
        }
    }

    public void setTitleText(String mTitleText) {
        this.mTitleText = mTitleText;
        postInvalidate();
    }

    public void setUnitText(String text) {
        this.mTextUnit = text;
        postInvalidate();
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        postInvalidate();
    }

    public void setShowArrow(boolean mShowArrow) {
        this.mShowArrow = mShowArrow;
        postInvalidate();
    }
}
