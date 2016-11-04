package com.salesman.adapter;

import android.content.Context;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.SigninListBean;

import java.util.List;

/**
 * 今日签到列表内部列表适配器
 * Created by LiHuai on 2016/1/29 0029.
 */
public class SigninInnerListAdapter extends CommonAdapter<SigninListBean.SignBean> {
    private int type = 1;// 时间加上年月日

    public SigninInnerListAdapter(Context context, List<SigninListBean.SignBean> data) {
        super(context, data);
    }

    public SigninInnerListAdapter(Context context, List<SigninListBean.SignBean> data, int type) {
        super(context, data);
        this.type = type;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, SigninListBean.SignBean signBean) {
        ((TextView) holder.getView(R.id.tv_name_signin)).setText(signBean.userName);

        if (3 == signBean.type) {// 外勤签到
            StringBuffer sb = new StringBuffer();
            sb.append("开始：");
            sb.append(signBean.outWorkStart);
            sb.append("  ");
            sb.append("结束：");
            sb.append(signBean.outWorkEnd);
            holder.setTextByString(R.id.tv_time_signin, sb);
        } else {
            if (type == 1) {// 时间是否加上年月日
                holder.setTextByString(R.id.tv_time_signin, signBean.signTime);
            } else {
                holder.setTextByString(R.id.tv_time_signin, signBean.createTime + " " + signBean.signTime);
            }
        }
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_signin_today;
    }
}
