package com.salesman.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.salesman.R;


/**
 * 自定义button
 * Created by LiHuai on 2016/8/8 0008.
 */
public class CustomButton extends View {
    private Context mContext;
    private int centerX, centerY;
    private int btnColor;
    private float btnAngle, textSize;
    private String btnText;
    private Paint btnPaint, textPaint;

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);
        initView();
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.CustomButton);
        this.btnColor = ta.getColor(R.styleable.CustomButton_btn_color, Color.BLACK);
        this.btnText = ta.getString(R.styleable.CustomButton_btn_text);
        this.textSize = ta.getDimension(R.styleable.CustomButton_btn_text_size, dipToPx(12));
        this.btnAngle = ta.getFloat(R.styleable.CustomButton_btn_angle, 10f);
        ta.recycle();
    }

    private void initView() {
        btnPaint = new Paint();
        btnPaint.setStyle(Paint.Style.STROKE);
        btnPaint.setColor(btnColor);
        btnPaint.setStrokeWidth(3);
        btnPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(btnColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((centerX & centerY) == 0) {
            centerX = getWidth() / 2;
            centerY = getHeight() / 2;
        }

        RectF rectF = new RectF(0, 0, centerX * 2f, centerY * 2f);
        canvas.drawRoundRect(rectF, btnAngle, btnAngle, btnPaint);

        if (!TextUtils.isEmpty(btnText)) {
            canvas.drawText(btnText, centerX, centerY + (textSize / 3), textPaint);
        }
    }

    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    public void setBtnColor(int btnColor) {
        this.btnColor = btnColor;
        btnPaint.setColor(btnColor);
        textPaint.setColor(btnColor);
        postInvalidate();
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
        postInvalidate();
    }

}
