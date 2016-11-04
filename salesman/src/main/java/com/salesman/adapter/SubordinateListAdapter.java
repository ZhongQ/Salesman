package com.salesman.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.activity.personal.DailyListActivity;
import com.salesman.activity.personal.MySubordinateActivity11;
import com.salesman.activity.personal.SubordinateSigninActivity;
import com.salesman.activity.personal.SubordinateTrackActivity;
import com.salesman.activity.work.YeJiTrendActivity;
import com.salesman.entity.SubordinateListBean;
import com.salesman.utils.StringUtil;
import com.salesman.views.CircleHeadView;

import java.util.List;

/**
 * 我的下属列表适配器
 * Created by LiHuai on 2016/2/2 0002.
 */
public class SubordinateListAdapter extends CommonAdapter<SubordinateListBean.XiaShuBean> {

    public SubordinateListAdapter(Context context, List<SubordinateListBean.XiaShuBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, final SubordinateListBean.XiaShuBean xiaShuBean) {
        CircleHeadView hv = holder.getView(R.id.hv_head_round);
        hv.setTextContent(StringUtil.cutStringLastTwo(xiaShuBean.userName));
        hv.setCircleColorResources(xiaShuBean.imgId);
        holder.setTextByString(R.id.tv_huaming, xiaShuBean.userName);

        TextView tvTrack = holder.getView(R.id.tv_track_sub);
        TextView tvSign = holder.getView(R.id.tv_sign_sub);
        TextView tvDaily = holder.getView(R.id.tv_daily_sub);
        TextView tvYeJi = holder.getView(R.id.tv_yeji_sub);
        // 足迹
        tvTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SubordinateTrackActivity.class);
                intent.putExtra("name", xiaShuBean.userName);
                intent.putExtra("id", xiaShuBean.userId);
                mContext.startActivity(intent);

            }
        });
        // 签到
        tvSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SubordinateSigninActivity.class);
                intent.putExtra("name", xiaShuBean.userName);
                intent.putExtra("id", xiaShuBean.userId);
                mContext.startActivity(intent);
            }
        });
        // 日报
        tvDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DailyListActivity.class);
                intent.putExtra("userId", xiaShuBean.userId);
                intent.putExtra("name", xiaShuBean.userName);
                intent.putExtra("come_from", MySubordinateActivity11.TAG);
                mContext.startActivity(intent);
            }
        });
        // 业绩
        tvYeJi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YeJiTrendActivity.class);
//                intent.putExtra("userId", xiaShuBean.userId);
//                intent.putExtra("deptId", xiaShuBean.deptId);
//                intent.putExtra("userType", xiaShuBean.userType);
                intent.putExtra("title", xiaShuBean.userName);
                intent.putExtra("XiaShu", xiaShuBean);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_my_xiashu;
    }
}
