package com.salesman.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.salesman.R;
import com.salesman.activity.manage.TodaySigninActivity;
import com.salesman.activity.manage.TodayTrackActivity;
import com.salesman.activity.personal.DailyListActivity;
import com.salesman.activity.work.VisitListActivity;
import com.salesman.activity.work.ZhanJiActivity;
import com.salesman.application.SalesManApplication;
import com.salesman.entity.WorkBean;
import com.salesman.fragment.WorkFragment;
import com.salesman.umeng.UmengAnalyticsUtil;
import com.salesman.umeng.UmengConfig;
import com.salesman.utils.DateUtils;
import com.studio.jframework.adapter.recyclerview.CommonRvAdapter;
import com.studio.jframework.adapter.recyclerview.RvViewHolder;

import java.util.List;

/**
 * 工作适配器
 * Created by LiHuai on 2016/7/4 0004.
 */
public class WorkAdapter extends CommonRvAdapter<WorkBean> {

    public WorkAdapter(Context context, List<WorkBean> data) {
        super(context, data);
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_work;
    }

    @Override
    public void inflateContent(RvViewHolder holder, int position, final WorkBean workBean) {
        ImageView ivWork = holder.getView(R.id.iv_work);
        if (0 != workBean.imgResource) {
            ivWork.setVisibility(View.VISIBLE);
            ivWork.setImageResource(workBean.imgResource);
        } else {
            ivWork.setVisibility(View.GONE);
        }
        holder.setTextByString(R.id.tv_name_work, workBean.itemName);

        holder.setOnClickListener(R.id.lay_work, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (workBean.id) {
                    case 1: //今日战绩
                        UmengAnalyticsUtil.onEvent(mContext, UmengConfig.TODAY_ZHANJI);
                        mContext.startActivity(new Intent(mContext, ZhanJiActivity.class));
                        break;
                    case 2: //今日足迹
                        UmengAnalyticsUtil.onEvent(mContext, UmengConfig.TODAY_TRACK);
                        mContext.startActivity(new Intent(mContext, TodayTrackActivity.class));
                        break;
                    case 3: //今日签到
                        UmengAnalyticsUtil.onEvent(mContext, UmengConfig.TODAY_SIGNIN);
                        mContext.startActivity(new Intent(mContext, TodaySigninActivity.class));
                        break;
                    case 4: //今日日志
                        UmengAnalyticsUtil.onEvent(mContext, UmengConfig.TODAY_LOG);
                        Intent todayDaily = new Intent(mContext, DailyListActivity.class);
                        todayDaily.putExtra("deptId", SalesManApplication.g_GlobalObject.getmUserInfo().getDeptId());
                        todayDaily.putExtra("createTime", DateUtils.getCurrentDate());
                        todayDaily.putExtra("come_from", WorkFragment.TAG);
                        mContext.startActivity(todayDaily);
                        break;
                    case 5:// 今日拜访
                        UmengAnalyticsUtil.onEvent(mContext, UmengConfig.TODAY_VISIT);
                        Intent visitIntent = new Intent(mContext, VisitListActivity.class);
                        mContext.startActivity(visitIntent);
                        break;
                    case 6:
//                        mContext.startActivity(new Intent(mContext, MyVisitPlanActivity.class));
                        break;
                }
            }
        });
    }
}
