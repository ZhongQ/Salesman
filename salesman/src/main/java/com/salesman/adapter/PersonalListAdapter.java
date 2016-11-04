package com.salesman.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.PersonalListBean;

import java.util.List;

/**
 * 个人中心列表适配器
 * Created by LiHuai on 2016/1/26 0026.
 */
public class PersonalListAdapter extends CommonAdapter<PersonalListBean> {
    public PersonalListAdapter(Context context, List<PersonalListBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, PersonalListBean personalListBean) {
        ((ImageView) holder.getView(R.id.iv_personal_item)).setImageResource(personalListBean.imgId);
        ((TextView) holder.getView(R.id.tv_personal_list)).setText(personalListBean.itemText);
        View line = holder.getView(R.id.view_personal_line);
        View bg = holder.getView(R.id.view_personal_bg);
        TextView tvRedDot = holder.getView(R.id.tv_red_dot);
        if (personalListBean.isShowLine) {
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
        }
        if (personalListBean.isShowGreyBg) {
            bg.setVisibility(View.VISIBLE);
        } else {
            bg.setVisibility(View.GONE);
        }
        if (personalListBean.isShowRedDot()) {
            tvRedDot.setVisibility(View.VISIBLE);
        } else {
            tvRedDot.setVisibility(View.GONE);
        }
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_personal;
    }
}
