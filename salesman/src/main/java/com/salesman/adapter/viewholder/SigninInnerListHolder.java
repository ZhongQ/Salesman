package com.salesman.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.salesman.R;
import com.salesman.entity.SigninListBean;

/**
 * 今日签到列表
 * Created by LiHuai on 2016/10/26 0026.
 */

public class SigninInnerListHolder extends BaseViewHolder<SigninListBean.SignBean> {
    private TextView tvName, tvTime;

    public SigninInnerListHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        tvName = $(R.id.tv_name_signin);
        tvTime = $(R.id.tv_time_signin);
    }

    @Override
    public void setData(SigninListBean.SignBean data) {
        super.setData(data);
        tvName.setText(data.userName);
        if (3 == data.type) {// 外勤签到
            StringBuffer sb = new StringBuffer();
            sb.append("开始：");
            sb.append(data.outWorkStart);
            sb.append("  ");
            sb.append("结束：");
            sb.append(data.outWorkEnd);
            tvTime.setText(sb.toString());
        } else {
            tvTime.setText(data.signTime);
        }
    }
}
