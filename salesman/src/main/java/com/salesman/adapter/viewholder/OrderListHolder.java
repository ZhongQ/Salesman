package com.salesman.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.salesman.R;
import com.salesman.entity.OrderListBean2;
import com.salesman.utils.StringUtil;
import com.salesman.views.CircleHeadView;

import java.util.List;

/**
 * 订单列表
 * Created by LiHuai on 2016/10/12 0012.
 */

public class OrderListHolder extends BaseViewHolder<OrderListBean2.DataBean.OrderBean> {
    private CircleHeadView hvOrder;
    private TextView tvName, tvOrderTime, tvTel, tvOrderNo, tvOrderMoney, tvStatus, tvLookMore, tvTimeDayTime;
    private ImageView ivLine, ivZjLogo;
    private boolean isShopOrder = false;// 是否是店铺订单

    public OrderListHolder(ViewGroup parent, @LayoutRes int res, boolean isShopOrder) {
        this(parent, res);
        this.isShopOrder = isShopOrder;
    }

    public OrderListHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        hvOrder = $(R.id.hv_order);
        tvName = $(R.id.tv_name_store);
        tvOrderTime = $(R.id.tv_time_order);
        tvTel = $(R.id.tv_mobile);
        tvOrderNo = $(R.id.tv_no_order);
        tvOrderMoney = $(R.id.tv_money_order);
        tvStatus = $(R.id.tv_state_order);
        tvLookMore = $(R.id.tv_look_more_order);
        tvTimeDayTime = $(R.id.tv_time_shop_order);
        ivLine = $(R.id.iv_line_order);
        ivZjLogo = $(R.id.iv_zj_logo);
    }

    @Override
    public void setData(OrderListBean2.DataBean.OrderBean data) {
        super.setData(data);
        hvOrder.setCircleColorResources(data.getImgId());
        tvName.setText(data.storeName);
        tvOrderTime.setText(data.addTime);
        tvTel.setText(data.mobile);
        tvOrderNo.setText(data.orderNo);
        tvOrderMoney.setText(String.valueOf(data.orderPrice));
        StringUtil.setTextAndColor(getContext(), tvStatus, data.status);
        // 是否自营
        if (1 == data.isZj) {
            ivZjLogo.setVisibility(View.VISIBLE);
        } else {
            ivZjLogo.setVisibility(View.GONE);
        }

        if (isShopOrder) {
            ivLine.setVisibility(View.GONE);
            tvLookMore.setVisibility(View.GONE);
            if (getAdapterPosition() == getfirstShowIndex(data.dayTime)) {
                tvTimeDayTime.setVisibility(View.VISIBLE);
                tvTimeDayTime.setText(data.dayTime);
            } else {
                tvTimeDayTime.setVisibility(View.GONE);
            }
        } else {
            tvTimeDayTime.setVisibility(View.GONE);
            ivLine.setVisibility(View.VISIBLE);
            tvLookMore.setVisibility(View.VISIBLE);
        }
    }

    private int getfirstShowIndex(String daytime) {
        RecyclerArrayAdapter<OrderListBean2.DataBean.OrderBean> adapter = getOwnerAdapter();
        List<OrderListBean2.DataBean.OrderBean> data = adapter.getAllData();
        for (int i = 0; i < data.size(); i++) {
            OrderListBean2.DataBean.OrderBean orderBean = data.get(i);
            if (orderBean.dayTime.equals(daytime)) {
                return i;
            }
        }
        return -1;
    }
}
