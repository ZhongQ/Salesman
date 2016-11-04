package com.salesman.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.PayWayBean;

import java.util.List;

/**
 * 支付方式列表适配器
 * Created by LiHuai on 2016/2/2 0002.
 */
public class PayWayListAdapter extends CommonAdapter<PayWayBean> {
    private boolean isSingle;// 是否单选

    public PayWayListAdapter(Context context, List<PayWayBean> data) {
        super(context, data);
    }

    public PayWayListAdapter(Context context, List<PayWayBean> data, boolean isSingle) {
        super(context, data);
        this.isSingle = isSingle;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, final int position, final PayWayBean payWayBean) {
        ((TextView) holder.getView(R.id.tv_name_pay)).setText(payWayBean.payName);
        ((ImageView) holder.getView(R.id.iv_pay_way)).setImageResource(payWayBean.imgId);
        ImageView ivLine = holder.getView(R.id.iv_line_pay_way);
        if (position == getData().size() - 1) {
            ivLine.setVisibility(View.GONE);
        } else {
            ivLine.setVisibility(View.VISIBLE);
        }
        final CheckBox cb = holder.getView(R.id.cb_pay_way);
        cb.setChecked(payWayBean.check);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payWayBean.isCheck()) {
                    cb.setChecked(false);
                    payWayBean.setCheck(false);
                } else {
                    cb.setChecked(true);
                    payWayBean.setCheck(true);
                }
            }
        });
    }

    public void setSingleSelect(int position) {
        List<PayWayBean> list = getData();
        for (int i = 0; i < list.size(); i++) {
            if (i != position) {
                list.get(i).setCheck(false);
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int setItemLayout(int type) {
        return R.layout.item_pay_way;
    }
}
