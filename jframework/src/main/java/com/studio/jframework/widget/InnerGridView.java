package com.studio.jframework.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * A sub class of {@link GridView} which can be a child view of scrollable view,
 * such as {@link android.widget.ScrollView} and {@link android.widget.ListView}. Just use as
 * normal GridView
 */
public class InnerGridView extends GridView {

    private OnTouchBlankPositionListener mTouchBlankPosListener;
    public static final int BLANK_POSITION = -1;

    public InnerGridView(Context context) {
        super(context);
    }

    public InnerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public interface OnTouchBlankPositionListener {
        /**
         * 1、处理时onTouchBlankPosition要返回true以终止事件的传递
         * 2、要捕捉ACTION_UP事件，如果使用ACTION_DOWN会在实际中出现点击两次的问题
         *
         * @return 是否要终止事件的传递
         */
        boolean onTouchBlankPosition();
    }

    public void setOnTouchBlankPositionListener(OnTouchBlankPositionListener listener) {
        mTouchBlankPosListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mTouchBlankPosListener != null) {
            if (!isEnabled()) {
                return isClickable() || isLongClickable();
            }

            if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                final int motionPosition = pointToPosition((int) event.getX(), (int) event.getY());
                if (motionPosition == BLANK_POSITION) {
                    return mTouchBlankPosListener.onTouchBlankPosition();
                }
            }
        }
        return super.onTouchEvent(event);
    }

}
