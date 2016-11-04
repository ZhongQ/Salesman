package com.salesman.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.salesman.R;
import com.salesman.entity.ClientDetailBean;

import java.util.List;

/**
 * 客户详情适配器
 * Created by LiHuai on 2016/6/20 0020.
 */
public class ClientDetailAdapter extends CommonAdapter<ClientDetailBean> {
    public ClientDetailAdapter(Context context, List<ClientDetailBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, ClientDetailBean clientDetailBean) {
        holder.setTextByString(R.id.tv_key_client, clientDetailBean.key);
        holder.setTextByString(R.id.tv_value_client, clientDetailBean.value);
        ImageView ivBg = holder.getView(R.id.iv_client_detail_bg);
        if (7 == position || 10 == position || 21 == position) {
            ivBg.setVisibility(View.VISIBLE);
        } else {
            ivBg.setVisibility(View.GONE);
        }
        if (position == mData.size() - 1) {
            holder.setVisibility(R.id.iv_line_client_detail, View.GONE);
        } else {
            holder.setVisibility(R.id.iv_line_client_detail, View.VISIBLE);
        }
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_client_detail_new;
    }
}
