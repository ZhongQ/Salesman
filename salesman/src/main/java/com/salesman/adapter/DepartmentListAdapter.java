package com.salesman.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.SubordinateListBean;

import java.util.List;

/**
 * 部门列表适配器
 * Created by LiHuai on 2016/3/25 0025.
 */
public class DepartmentListAdapter extends CommonAdapter<SubordinateListBean.DepartmentBean> {

    public DepartmentListAdapter(Context context, List<SubordinateListBean.DepartmentBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, SubordinateListBean.DepartmentBean departmentBean) {
        ((TextView) holder.getView(R.id.tv_name_dep)).setText(departmentBean.deptName);
        ((TextView) holder.getView(R.id.tv_dep_renshu)).setText(String.valueOf(departmentBean.total));
        ImageView ivLine = holder.getView(R.id.iv_line_dept);
        if (position == getCount() - 1) {
            ivLine.setVisibility(View.GONE);
        } else {
            ivLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_department;
    }
}
