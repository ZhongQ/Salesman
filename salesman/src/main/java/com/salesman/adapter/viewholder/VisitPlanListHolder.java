package com.salesman.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.salesman.R;
import com.salesman.entity.VisitPlanBean;
import com.salesman.utils.DateUtils;
import com.salesman.utils.UserInfoUtil;

/**
 * 拜访计划列表
 * Created by LiHuai on 2016/10/10 0010.
 */

public class VisitPlanListHolder extends BaseViewHolder<VisitPlanBean.DataBean.PlanBean> {
    private TextView tvWeek, tvLineName, tvClientNum, tvVisitNum, tvStatus;
    private ImageView ivArrow;

    public VisitPlanListHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        tvWeek = $(R.id.tv_week_plan);
        tvLineName = $(R.id.tv_line_name_plan);
        tvClientNum = $(R.id.tv_client_num_plan);
        tvVisitNum = $(R.id.tv_visit_num_plan);
        tvStatus = $(R.id.tv_status_plan);
    }

    @Override
    public void setData(VisitPlanBean.DataBean.PlanBean data) {
        super.setData(data);
        tvWeek.setText(data.week);
        tvLineName.setText(data.lineName);
        tvClientNum.setText(String.valueOf(data.shopTotal));
        tvVisitNum.setText(String.valueOf(data.visitTotal));
        Context mContext = getContext();
        switch (data.status) {
            case "0":
                tvStatus.setText("未开始");
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_999999));
                break;
            case "1":
                String currentTime = DateUtils.getCurrentDate();
                if (currentTime.equals(data.visitDate)) {
                    tvStatus.setText("进行中");
                    tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_0090ff));
                } else {
                    tvStatus.setText("未完成");
                    tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_ff3636));
                }
                break;
            case "2":
                tvStatus.setText("已完成");
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_58cc97));
                break;
            case "4":
                tvStatus.setText("未安排");
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_999999));
                break;
        }

        if (UserInfoUtil.isAdministrator()) {
            tvStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right_grey, 0);
        } else {
            tvStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }
}
