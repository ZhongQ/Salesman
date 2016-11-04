package com.salesman.views.textview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.salesman.common.Constant;

/**
 * 自定义字体
 * Created by LiHuai on 2016/10/25 0025.
 */

public class BrandTextView extends TextView {

    public BrandTextView(Context context) {
        super(context);
    }

    public BrandTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BrandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Constant.CUSTOM_FONTS));
    }
}
