package com.salesman.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.salesman.R;
import com.salesman.entity.ClientCheckListBean;

/**
 * 客户审核列表
 * Created by LiHuai on 2016/9/29 0029.
 */
public class ClientCheckListHolder extends BaseViewHolder<ClientCheckListBean.ClientCheckBean> {
    TextView tvName, tvAddr;
    ImageView ivBgHead;

    public ClientCheckListHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        tvName = $(R.id.tv_name_client);
        tvAddr = $(R.id.tv_addr_client);
        ivBgHead = $(R.id.iv_bg_head);
    }

    @Override
    public void setData(ClientCheckListBean.ClientCheckBean data) {
        super.setData(data);
        tvName.setText(data.storeName);
        tvAddr.setText(data.address);
        ivBgHead.setBackgroundResource(data.getImgId());
    }
}
