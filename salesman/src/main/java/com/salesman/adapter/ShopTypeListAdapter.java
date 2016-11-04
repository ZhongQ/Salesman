package com.salesman.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.ShopTypeBean;

import java.util.List;

/**
 * 商铺类型列表适配器
 * Created by LiHuai on 2016/2/2 0002.
 */
public class ShopTypeListAdapter extends CommonAdapter<ShopTypeBean> {
    private boolean isSingle;// 是否单选

    public ShopTypeListAdapter(Context context, List<ShopTypeBean> data) {
        super(context, data);
    }

    public ShopTypeListAdapter(Context context, List<ShopTypeBean> data, boolean isSingle) {
        super(context, data);
        this.isSingle = isSingle;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, final int position, final ShopTypeBean shopTypeBean) {
        ((TextView) holder.getView(R.id.tv_type_shop)).setText(shopTypeBean.label);
        ImageView ivLine = holder.getView(R.id.iv_line_shop);
        if (position == getData().size() - 1) {
            ivLine.setVisibility(View.GONE);
        } else {
            ivLine.setVisibility(View.VISIBLE);
        }
        if (shopTypeBean.isChecked()) {
            holder.setBackground(R.id.iv_type_shop, R.drawable.rb_check);
        } else {
            holder.setBackground(R.id.iv_type_shop, R.drawable.rb_default);
        }

//        final CheckBox cb = holder.getView(R.id.cb_type_shop);
//        cb.setChecked(shopTypeBean.checked);
//        cb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (shopTypeBean.isChecked()) {
//                    cb.setChecked(false);
//                    shopTypeBean.setChecked(false);
//                } else {
//                    cb.setChecked(true);
//                    shopTypeBean.setChecked(true);
//                }
//                if (isSingle) {
//                    setSingleSelect(position);
//                }
//            }
//        });
    }

    public void setSingleSelect(int position) {
        List<ShopTypeBean> list = getData();
        for (int i = 0; i < list.size(); i++) {
            if (i != position) {
                list.get(i).setChecked(false);
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int setItemLayout(int type) {
        return R.layout.item_shop_type;
    }
}
