package com.salesman.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.salesman.R;
import com.salesman.entity.VisitPlanPersonalListBean;

import java.util.List;

/**
 * 个人拜访计划列表适配器
 * Created by LiHuai on 2016/8/6 0006.
 */
public class VisitPlanPersonalAdapter extends CommonAdapter<VisitPlanPersonalListBean.VisitPlanBean> {
    private boolean isMyPlan = false;

    public VisitPlanPersonalAdapter(Context context, List<VisitPlanPersonalListBean.VisitPlanBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, VisitPlanPersonalListBean.VisitPlanBean visitPlanBean) {
        holder.setTextByString(R.id.tv_time_visit, visitPlanBean.week + "：");
        if (!TextUtils.isEmpty(visitPlanBean.lineName)) {
            if (TextUtils.isEmpty(visitPlanBean.lineId)) {
                holder.setTextByString(R.id.tv_name_visit, mContext.getString(R.string.select_please));
            } else {
                holder.setTextByString(R.id.tv_name_visit, visitPlanBean.lineName + "，" + visitPlanBean.shopTotal + "家客户");
            }
        } else {
            if (isMyPlan) {
                holder.setTextByString(R.id.tv_name_visit, "暂无安排");
            } else {
                holder.setTextByString(R.id.tv_name_visit, mContext.getString(R.string.select_please));
            }
        }
        if (isMyPlan) {
            holder.setVisibility(R.id.iv_arrow_visit, View.GONE);
        } else {
            holder.setVisibility(R.id.iv_arrow_visit, View.VISIBLE);
        }
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_visit_plan;
    }

    public void setIsMyPlan(boolean isMyPlan) {
        this.isMyPlan = isMyPlan;
    }
}
