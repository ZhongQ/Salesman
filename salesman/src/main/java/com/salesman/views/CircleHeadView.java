package com.salesman.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.salesman.R;

/**
 * 圆形View
 * Created by LiHuai on 2016/8/4 0004.
 */
public class CircleHeadView extends View {
    private Context mContext;
    private int circleColor, textColor;
    private float textSize, rectAngle;
    private String textContent;
    private int centerX, centerY;
    private int radius;                     // 圆半径
    private Shape shape = Shape.SHAPE_ROUND;
    private int drawableId;

    private Paint mCircleBgPaint;
    private Paint mTextPaint;

    // 头像形状
    public enum Shape {
        SHAPE_ROUND, SHAPE_RECTANGLE    //圆形，矩形
    }

    public CircleHeadView(Context context) {
        super(context);
    }

    public CircleHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);
        initView();
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CircleHeadView);
        this.circleColor = typedArray.getColor(R.styleable.CircleHeadView_circle_color, Color.BLUE);
        this.textColor = typedArray.getColor(R.styleable.CircleHeadView_text_color_cir, Color.WHITE);
        this.textSize = typedArray.getDimension(R.styleable.CircleHeadView_text_size_cir, dipToPx(12));
        this.textContent = typedArray.getString(R.styleable.CircleHeadView_text_content);
        this.rectAngle = typedArray.getFloat(R.styleable.CircleHeadView_circle_angle, 5f);
        this.drawableId = typedArray.getResourceId(R.styleable.CircleHeadView_circle_src, -1);
        int shapeStyle = typedArray.getInt(R.styleable.CircleHeadView_shape, 0);
        switch (shapeStyle) {
            case 0:
                shape = Shape.SHAPE_ROUND;
                break;
            case 1:
                shape = Shape.SHAPE_RECTANGLE;
                break;
            default:
                shape = Shape.SHAPE_ROUND;
                break;
        }
        typedArray.recycle();
    }

    private void initView() {
        mCircleBgPaint = new Paint();
//        mCircleBgPaint.setStyle(Paint.Style.STROKE);
//        mCircleBgPaint.setStrokeCap(Paint.Cap.ROUND);
        mCircleBgPaint.setColor(circleColor);
        mCircleBgPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);            // 设置文字的基准（横向）
        mTextPaint.setAntiAlias(true);                          // 抗锯齿

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
        if ((centerX & centerY) == 0) {
            radius = getWidth() / 2;
            centerX = radius;
            centerY = radius;
        }
        if (shape == Shape.SHAPE_ROUND) {
            // 画圆
            canvas.drawCircle(centerX, centerY, radius, mCircleBgPaint);
        } else if (shape == Shape.SHAPE_RECTANGLE) {
            // 画圆角矩形
            RectF rectF = new RectF(0, 0, radius * 2f, radius * 2f);
            canvas.drawRoundRect(rectF, rectAngle, rectAngle, mCircleBgPaint);
        }
        // 画文字
        if (!TextUtils.isEmpty(textContent)) {
            canvas.drawText(textContent, centerX, centerY + (textSize / 3), mTextPaint);
        }
        // 画中心图
        if (drawableId != -1) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
            canvas.drawBitmap(bitmap, centerX - (bitmap.getWidth() / 2), centerY - (bitmap.getHeight() / 2), mCircleBgPaint);
        }
    }

    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        mCircleBgPaint.setColor(circleColor);
        postInvalidate();
    }

    public void setCircleColorResources(int colorResources) {
        this.circleColor = mContext.getResources().getColor(colorResources);
        mCircleBgPaint.setColor(circleColor);
        postInvalidate();
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
        postInvalidate();
    }
}
