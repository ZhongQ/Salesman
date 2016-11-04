package com.salesman.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.salesman.R;
import com.salesman.entity.YeJiListBean;
import com.salesman.utils.StringUtil;
import com.salesman.views.CircleHeadView;

import java.util.List;

/**
 * 日业绩列表
 * Created by LiHuai on 2016/10/12 0012.
 */

public class DayYeJiListHolder extends BaseViewHolder<YeJiListBean.DataBean.YeJiBean> {
    private CircleHeadView hvYeJi;
    private TextView tvName, tvOrderNum, tvDianBao, tvZiYing, tvNewly, tvActive, tvTime;

    public DayYeJiListHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        hvYeJi = $(R.id.hv_head_round);
        tvName = $(R.id.tv_huaming);
        tvOrderNum = $(R.id.tv_order_num_yeji);
        tvDianBao = $(R.id.tv_dianbao_yeji);
        tvZiYing = $(R.id.tv_ziying_yeji);
        tvNewly = $(R.id.tv_newly_client_yeji);
        tvActive = $(R.id.tv_active_client_yeji);
        tvTime = $(R.id.tv_time_day_yeji);
    }

    @Override
    public void setData(YeJiListBean.DataBean.YeJiBean data) {
        super.setData(data);
        hvYeJi.setTextContent(StringUtil.cutStringLastTwo(data.salemanName));
        hvYeJi.setCircleColorResources(data.getImgId());
        tvName.setText(data.salemanName);
        tvOrderNum.setText(String.valueOf(data.orderCount));
        tvDianBao.setText(StringUtil.formatNumbers(data.turnover));
        tvZiYing.setText(StringUtil.formatNumbers(data.zjturnover));
        tvNewly.setText(String.valueOf(data.regStore));
        tvActive.setText(String.valueOf(data.activeStore));
        if (getAdapterPosition() == getfirstShowIndex(data.dayTime)) {
            tvTime.setVisibility(View.VISIBLE);
            tvTime.setText(data.dayTime);
        } else {
            tvTime.setVisibility(View.GONE);
        }
    }

    private int getfirstShowIndex(String daytime) {
        RecyclerArrayAdapter<YeJiListBean.DataBean.YeJiBean> adapter = getOwnerAdapter();
        List<YeJiListBean.DataBean.YeJiBean> data = adapter.getAllData();
        for (int i = 0; i < data.size(); i++) {
            YeJiListBean.DataBean.YeJiBean yeJiBean = data.get(i);
            if (yeJiBean.dayTime.equals(daytime)) {
                return i;
            }
        }
        return -1;
    }
}
