package com.salesman.adapter;

import android.content.Context;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.MyTrackListBean;

import java.util.List;

/**
 * 我的足迹列表适配器
 * Created by LiHuai on 2016/1/28 0028.
 */
public class MyTrackListAdapter extends CommonAdapter<MyTrackListBean.TrackTimeBean> {
    public MyTrackListAdapter(Context context, List<MyTrackListBean.TrackTimeBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, MyTrackListBean.TrackTimeBean trackTimeBean) {
        ((TextView) holder.getView(R.id.tv_trace_date)).setText(trackTimeBean.createTime);
        ((TextView) holder.getView(R.id.tv_trace_week)).setText(trackTimeBean.week);
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_track_list;
    }
}
