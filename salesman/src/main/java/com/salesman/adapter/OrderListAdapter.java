package com.salesman.adapter;

import android.content.Context;

import com.salesman.R;
import com.salesman.entity.OrderListBean;

import java.util.List;

/**
 * 订单列表适配器
 * Created by LiHuai on 2016/6/16 0016.
 */
public class OrderListAdapter extends CommonAdapter<OrderListBean.OrderBean> {

    public OrderListAdapter(Context context, List<OrderListBean.OrderBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, OrderListBean.OrderBean orderBean) {
        holder.setTextByString(R.id.tv_name_store, orderBean.storeName);
        holder.setTextByString(R.id.tv_mobile, orderBean.mobile);
        holder.setTextByString(R.id.tv_time_order, orderBean.dayTime);
        holder.setTextByString(R.id.tv_no_order, orderBean.orderNo);
        holder.setTextByString(R.id.tv_money_order, String.valueOf(orderBean.orderPrice));
        holder.setTextByString(R.id.tv_state_order, orderBean.status);
//        holder.setBackground(R.id.iv_bg_head, orderBean.getImgId());

    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_order_list;
    }
}
