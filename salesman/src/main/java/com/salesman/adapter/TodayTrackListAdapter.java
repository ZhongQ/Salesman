package com.salesman.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.TodayTrackListBean;

import java.util.List;

/**
 * 今日足迹和历史足迹列表适配器
 * Created by LiHuai on 2016/1/28 0028.
 */
public class TodayTrackListAdapter extends CommonAdapter<TodayTrackListBean.EmployeeTrackBean> {
    private int type = 1;

    public TodayTrackListAdapter(Context context, List<TodayTrackListBean.EmployeeTrackBean> data) {
        super(context, data);
    }

    public TodayTrackListAdapter(Context context, List<TodayTrackListBean.EmployeeTrackBean> data, int type) {
        super(context, data);
        this.type = type;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, TodayTrackListBean.EmployeeTrackBean employeeTrackBean) {
        holder.setTextByString(R.id.tv_signin_type, employeeTrackBean.userName);
        holder.setVisibility(R.id.tv_signin_num, View.GONE);
        ((TextView) holder.getView(R.id.tv_address_now)).setText(employeeTrackBean.positionName);
        if (type == 1) {
            ((TextView) holder.getView(R.id.tv_updata_time)).setText(employeeTrackBean.timePoint);
        } else {
            ((TextView) holder.getView(R.id.tv_updata_time)).setText(employeeTrackBean.createTime + " " + employeeTrackBean.timePoint);
        }
        if (position == 0) {
            holder.setVisibility(R.id.iv_line_bg_signin, View.GONE);
        } else {
            holder.setVisibility(R.id.iv_line_bg_signin, View.VISIBLE);
        }

    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_track_today;
    }
}
