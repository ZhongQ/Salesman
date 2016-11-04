package com.salesman.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.salesman.R;
import com.salesman.entity.SingleSelectionBean;

/**
 * 单选列表
 * Created by LiHuai on 2016/10/13 0013.
 */

public class SingleSelectionHolder extends BaseViewHolder<SingleSelectionBean> {
    private TextView tvNo;
    private ImageView ivBiaoZhi;

    public SingleSelectionHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        tvNo = $(R.id.tv_no);
        ivBiaoZhi = $(R.id.iv_biaozhi);
    }

    @Override
    public void setData(SingleSelectionBean data) {
        super.setData(data);
        if (!TextUtils.isEmpty(data.nameNd) && !"ALL".equals(data.id)) {
            tvNo.setText(data.name + "，" + data.nameNd + "家客户");
        } else {
            tvNo.setText(data.name);
        }
        Context context = getContext();
        if (data.isSelect) {
            ivBiaoZhi.setVisibility(View.VISIBLE);
            tvNo.setTextColor(context.getResources().getColor(R.color.color_0090ff));
        } else {
            ivBiaoZhi.setVisibility(View.GONE);
            tvNo.setTextColor(context.getResources().getColor(R.color.color_666666));
        }
    }
}
