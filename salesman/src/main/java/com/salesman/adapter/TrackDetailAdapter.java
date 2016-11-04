package com.salesman.adapter;

import android.content.Context;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.TrackDetailListBean;

import java.util.List;

/**
 * 足迹明细列表适配器
 * Created by LiHuai on 2016/1/28 0028.
 */
public class TrackDetailAdapter extends CommonAdapter<TrackDetailListBean.TrackDetailBean> {

    public TrackDetailAdapter(Context context, List<TrackDetailListBean.TrackDetailBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, TrackDetailListBean.TrackDetailBean trackDetailBean) {
        ((TextView) holder.getView(R.id.tv_time_track)).setText(trackDetailBean.timePoint);
        ((TextView) holder.getView(R.id.tv_address_track)).setText(trackDetailBean.positionName);

        TextView tvSignFlag = (TextView) holder.getView(R.id.tv_signin_track);
        switch (trackDetailBean.type) {
            case 0:
                tvSignFlag.setText("");
                tvSignFlag.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right_grey, 0);
                break;
            case 1:
            case 2:
                tvSignFlag.setText("签到");
                tvSignFlag.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case 3:
                tvSignFlag.setText("外勤");
                tvSignFlag.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
        }
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_track_detail;
    }
}
