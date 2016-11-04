package com.salesman.global;

import android.content.Context;
import android.view.View;

import com.salesman.R;

/**
 * Created by Administrator on 2016/2/24 0024.
 */
public class HeadViewHolder {

    private static View HEADVIEW;

    public static View getHeadView(Context context) {
        if (null == HEADVIEW) {
            synchronized (HeadViewHolder.class) {
                if (null == HEADVIEW) {
                    HEADVIEW = View.inflate(context, R.layout.view_head_lv, null);
                }
            }
        }
        return HEADVIEW;
    }
}
