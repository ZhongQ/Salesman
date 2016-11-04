package com.salesman.adapter;

import android.content.Context;

import com.salesman.R;

import java.util.List;

/**
 * 日志列表适配器
 * Created by LiHuai on 2016/5/4 0004.
 */
public class LogListAdapter extends CommonAdapter<String> {
    public LogListAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, String s) {
        holder.setTextByString(R.id.tv_log, s);
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_log;
    }
}
