package com.salesman.adapter;

import android.content.Context;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.DailyDetailsBean;

import java.util.List;

/**
 * 工作日志详情列表适配器
 * version V1.2.0
 * Created by LiHuai on 2016/4/20 0029.
 */
public class DailyDetailsListAdapter2 extends CommonAdapter<DailyDetailsBean.FieldBean> {

    public DailyDetailsListAdapter2(Context context, List<DailyDetailsBean.FieldBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, DailyDetailsBean.FieldBean fieldBean) {
        ((TextView) holder.getView(R.id.tv_filename)).setText(fieldBean.key);
        ((TextView) holder.getView(R.id.tv_filevalue)).setText(fieldBean.value);
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_daily_details;
    }
}
