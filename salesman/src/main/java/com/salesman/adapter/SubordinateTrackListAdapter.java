package com.salesman.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.SubordinateTrackListBean;

import java.util.List;

/**
 * 下属足迹列表适配器
 * Created by LiHuai on 2016/1/28 0028.
 */
public class SubordinateTrackListAdapter extends CommonAdapter<SubordinateTrackListBean.TrackBean> {

    public SubordinateTrackListAdapter(Context context, List<SubordinateTrackListBean.TrackBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, SubordinateTrackListBean.TrackBean trackBean) {
        holder.setTextByString(R.id.tv_signin_type, trackBean.userName);
        holder.setVisibility(R.id.tv_signin_num, View.GONE);
        ((TextView) holder.getView(R.id.tv_address_now)).setText(trackBean.positionName);
        ((TextView) holder.getView(R.id.tv_updata_time)).setText(trackBean.timePoint);
        TextView tvDate = holder.getView(R.id.tv_date_track);
        if (position == getDateFirstShowIndex(trackBean.createTime)) {
            tvDate.setVisibility(View.VISIBLE);
            tvDate.setText(trackBean.createTime + " " + trackBean.week);
        } else {
            tvDate.setVisibility(View.GONE);
        }
    }

    public int getDateFirstShowIndex(String date) {
        for (int i = 0; i < mData.size(); i++) {
            if (date.equals(mData.get(i).createTime)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_track_today;
    }
}
