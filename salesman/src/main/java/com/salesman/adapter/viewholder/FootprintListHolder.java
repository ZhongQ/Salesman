package com.salesman.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.salesman.R;
import com.salesman.entity.TrackDetailListBean;

/**
 * 足迹明细
 * Created by LiHuai on 2016/9/29 0029.
 */

public class FootprintListHolder extends BaseViewHolder<TrackDetailListBean.TrackDetailBean> {
    TextView tvTime, tvAddress, tvSignin;

    public FootprintListHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        tvTime = $(R.id.tv_time_track);
        tvAddress = $(R.id.tv_address_track);
        tvSignin = $(R.id.tv_signin_track);
    }

    @Override
    public void setData(TrackDetailListBean.TrackDetailBean data) {
        super.setData(data);
        tvTime.setText(data.timePoint);
        tvAddress.setText(data.positionName);
        switch (data.type) {
            case 0:
                tvSignin.setText("");
                tvSignin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right_grey, 0);
                break;
            case 1:
            case 2:
                tvSignin.setText("签到");
                tvSignin.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case 3:
                tvSignin.setText("外勤");
                tvSignin.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
        }
    }
}
