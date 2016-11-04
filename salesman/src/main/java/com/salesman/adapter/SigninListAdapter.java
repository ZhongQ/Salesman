package com.salesman.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.salesman.R;
import com.salesman.activity.personal.MySigninActivity;
import com.salesman.entity.SigninListBean;
import com.studio.jframework.widget.listview.InnerListView;

import java.util.List;

/**
 * 今日签到和历史签到列表适配器
 * Created by LiHuai on 2016/1/29 0029.
 */
public class SigninListAdapter extends CommonAdapter<SigninListBean.SignGroupBean> {
    private int type = 1;

    public SigninListAdapter(Context context, List<SigninListBean.SignGroupBean> data) {
        super(context, data);
    }

    public SigninListAdapter(Context context, List<SigninListBean.SignGroupBean> data, int type) {
        super(context, data);
        this.type = type;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, SigninListBean.SignGroupBean signGroupBean) {
        holder.setTextByString(R.id.tv_signin_type, signGroupBean.typeName);
        holder.setTextByString(R.id.tv_signin_num, signGroupBean.signList.size() + "/" + signGroupBean.staffTotal);

        ImageView ivLine = holder.getView(R.id.iv_line_signin);
        if (0 == position) {
            ivLine.setVisibility(View.GONE);
        } else {
            ivLine.setVisibility(View.VISIBLE);
        }
        InnerListView listView = (InnerListView) holder.getView(R.id.lv_inner_signin);
        final SigninInnerListAdapter mAdapter = new SigninInnerListAdapter(mContext, signGroupBean.signList, type);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SigninListBean.SignBean signBean = mAdapter.getItem(position);
                if (null != signBean && !TextUtils.isEmpty(signBean.id) && !TextUtils.isEmpty(signBean.createTime)) {
                    Intent intent = new Intent(mContext, MySigninActivity.class);
                    intent.putExtra("userId", signBean.createBy);
                    intent.putExtra("name", signBean.userName);
                    intent.putExtra("createTime", signBean.createTime);
                    intent.putExtra("type", String.valueOf(signBean.type));
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_today_signin;
    }
}
