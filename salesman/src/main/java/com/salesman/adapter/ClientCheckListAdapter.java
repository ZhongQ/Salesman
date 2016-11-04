package com.salesman.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.salesman.R;
import com.salesman.activity.client.ClientCheckDetailActivity;
import com.salesman.entity.ClientCheckListBean;

import java.util.List;

/**
 * 客户审核列表适配器
 * Created by LiHuai on 2016/6/23 0023.
 */
public class ClientCheckListAdapter extends CommonAdapter<ClientCheckListBean.ClientCheckBean> {
    private String status = "";

    public ClientCheckListAdapter(Context context, List<ClientCheckListBean.ClientCheckBean> data) {
        super(context, data);
    }

    public ClientCheckListAdapter(Context context, List<ClientCheckListBean.ClientCheckBean> data, String status) {
        super(context, data);
        this.status = status;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, final ClientCheckListBean.ClientCheckBean clientCheckBean) {
        holder.setTextByString(R.id.tv_name_client, clientCheckBean.storeName);
        holder.setTextByString(R.id.tv_addr_client, clientCheckBean.address);
        holder.setBackground(R.id.iv_bg_head, clientCheckBean.getImgId());

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClientCheckDetailActivity.class);
                intent.putExtra("storeId", String.valueOf(clientCheckBean.storeId));
                if (status.equals("2")) {
                    intent.putExtra("title", "待审核");
                } else {
                    intent.putExtra("title", "已拒绝");
                }
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_client_list_check;
    }
}
