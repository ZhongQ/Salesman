package com.salesman.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.SubordinateSigninListBean;

import java.util.List;

/**
 * 下属签到列表适配器
 * Created by LiHuai on 2016/2/23 0023.
 */
public class SubordinateSigninListAdapter extends CommonAdapter<SubordinateSigninListBean.SigninBean> {

    public SubordinateSigninListAdapter(Context context, List<SubordinateSigninListBean.SigninBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, SubordinateSigninListBean.SigninBean signinBean) {
        holder.setTextByString(R.id.tv_signin_type, signinBean.typeName);
        holder.setTextByString(R.id.tv_name_signin, signinBean.userName);
        holder.getView(R.id.tv_signin_num).setVisibility(View.GONE);
        if (3 == signinBean.type) {// 外勤签到
            StringBuffer sb = new StringBuffer();
            sb.append("开始：");
            sb.append(signinBean.outWorkStart);
            sb.append("  ");
            sb.append("结束：");
            sb.append(signinBean.outWorkEnd);
            holder.setTextByString(R.id.tv_time_signin, sb);
        } else {
            holder.setTextByString(R.id.tv_time_signin, signinBean.signTime);
        }

        TextView tvDateSignin = holder.getView(R.id.tv_date_signin);
        ImageView ivLine = holder.getView(R.id.iv_line_sub_signin);
        if (position == getDateFirstShowIndex(signinBean.createTime)) {
            tvDateSignin.setVisibility(View.VISIBLE);
            ivLine.setVisibility(View.GONE);
            tvDateSignin.setText(signinBean.createTime + " " + signinBean.week);
        } else {
            tvDateSignin.setVisibility(View.GONE);
            ivLine.setVisibility(View.VISIBLE);
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
        return R.layout.item_subordinate_signin;
    }
}
