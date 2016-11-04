package com.salesman.adapter;

import android.content.Context;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.ShopDetailsBean;

import java.util.List;

/**
 * 负责人列表适配器
 * Created by LiHuai on 2016/1/31 0031.
 */
public class PrincipalListAdapter extends CommonAdapter<ShopDetailsBean.ProprietorBean> {

    public PrincipalListAdapter(Context context, List<ShopDetailsBean.ProprietorBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, ShopDetailsBean.ProprietorBean proprietorBean) {
        ((TextView) holder.getView(R.id.tv_principal_shop)).setText(proprietorBean.name);

    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_client_detail;
    }
}
