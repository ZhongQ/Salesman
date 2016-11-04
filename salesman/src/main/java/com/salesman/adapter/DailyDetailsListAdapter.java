package com.salesman.adapter;

import android.content.Context;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.ReleaseDailyBean;

import java.util.List;

/**
 * 日报详情列表适配器
 * Created by LiHuai on 2016/3/29 0029.
 */
public class DailyDetailsListAdapter extends CommonAdapter<ReleaseDailyBean.ReleaseListBean> {

    public DailyDetailsListAdapter(Context context, List<ReleaseDailyBean.ReleaseListBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, ReleaseDailyBean.ReleaseListBean releaseListBean) {
        ((TextView) holder.getView(R.id.tv_filename)).setText(releaseListBean.fieldCnName);
        ((TextView) holder.getView(R.id.tv_filevalue)).setText(releaseListBean.fieldVal);
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_daily_details;
    }
}
