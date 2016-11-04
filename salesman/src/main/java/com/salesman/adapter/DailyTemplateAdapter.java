package com.salesman.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.salesman.R;
import com.salesman.activity.home.ReleaseDailyActivity;
import com.salesman.entity.DailyTemplateListBean;
import com.studio.jframework.adapter.recyclerview.CommonRvAdapter;
import com.studio.jframework.adapter.recyclerview.RvViewHolder;

import java.util.List;

/**
 * 日报模板列表适配器
 * Created by LiHuai on 2016/4/19 0019.
 */
public class DailyTemplateAdapter extends CommonRvAdapter<DailyTemplateListBean.DailyTemplateBean> {
    public DailyTemplateAdapter(Context context, List<DailyTemplateListBean.DailyTemplateBean> data) {
        super(context, data);
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_daily_template;
    }

    @Override
    public void inflateContent(RvViewHolder holder, final int position, final DailyTemplateListBean.DailyTemplateBean dailyTemplateBean) {
        holder.setTextByString(R.id.tv_name_tmpl, dailyTemplateBean.tmplName);
        switch (dailyTemplateBean.type) {
            case "0":
                holder.setImageResource(R.id.iv_tmpl, R.drawable.daily_common);
                break;
            case "1":
                holder.setImageResource(R.id.iv_tmpl, R.drawable.daily_day);
                break;
            case "2":
                holder.setImageResource(R.id.iv_tmpl, R.drawable.daily_week);
                break;
            case "3":
                holder.setImageResource(R.id.iv_tmpl, R.drawable.daily_month);
                break;
        }
        holder.setOnClickListener(R.id.lay_template, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReleaseDailyActivity.class);
                intent.putExtra("tmplId", dailyTemplateBean.tmplId);
                intent.putExtra("tmplName", dailyTemplateBean.tmplName);
                mContext.startActivity(intent);
            }
        });
    }
}
