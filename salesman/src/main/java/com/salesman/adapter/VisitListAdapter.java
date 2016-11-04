package com.salesman.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.salesman.R;
import com.salesman.activity.work.VisitPlanNewActivity;
import com.salesman.entity.VisitListBean;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.views.CircleHeadView;

import java.util.List;

/**
 * 拜访列表适配器
 * Created by LiHuai on 2016/8/6 0006.
 */
public class VisitListAdapter extends CommonAdapter<VisitListBean.VisitPlanBean> {

    public VisitListAdapter(Context context, List<VisitListBean.VisitPlanBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, final VisitListBean.VisitPlanBean visitPlanBean) {
        CircleHeadView circleHeadView = holder.getView(R.id.hv_head_round);
        circleHeadView.setCircleColorResources(visitPlanBean.headColor);
        circleHeadView.setTextContent(ReplaceSymbolUtil.cutStringLastTwo(visitPlanBean.salesmanName));
        holder.setTextByString(R.id.tv_huaming, visitPlanBean.salesmanName);
        holder.setTextByString(R.id.tv_num_visit, String.valueOf(visitPlanBean.visitTotal));
        if (!TextUtils.isEmpty(visitPlanBean.lineName)) {
            holder.setTextByString(R.id.tv_name_line, visitPlanBean.lineName + " (共" + visitPlanBean.shopTotal + "个客户)");
        } else {
            holder.setTextByString(R.id.tv_name_line, "暂未安排");
        }
        ImageView ivState = holder.getView(R.id.iv_status);
        ivState.setVisibility(View.VISIBLE);
        switch (visitPlanBean.status) {
            case "0":
                ivState.setImageResource(R.drawable.un_start_icon);
                break;
            case "1":
                ivState.setImageResource(R.drawable.in_progress_icon);
                break;
            case "2":
                ivState.setImageResource(R.drawable.completed_icon);
                break;
            case "3":
                ivState.setImageResource(R.drawable.unfinished_icon);
                break;
            default:
                ivState.setImageResource(R.drawable.unfinished_icon);
                break;
        }

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VisitPlanNewActivity.class);
                intent.putExtra("salesmanId", visitPlanBean.salesmanId);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_visit_list_new;
    }
}
