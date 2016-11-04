package com.salesman.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.SingleSelectionBean;

import java.util.List;

/**
 * 单选列表适配器
 * Created by LiHuai on 2016/6/24 0026.
 */
public class SingleSelectionAdapter extends CommonAdapter<SingleSelectionBean> {
    public final int FIRST_TYPE = 0;
    public final int SECOND_TYPE = 1;

    public SingleSelectionAdapter(Context context, List<SingleSelectionBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).itemType == SECOND_TYPE) {
            return SECOND_TYPE;
        }
        return FIRST_TYPE;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, SingleSelectionBean singleSelectionBean) {
        switch (getItemViewType(position)) {
            case FIRST_TYPE:
                TextView tvCheck = holder.getView(R.id.tv_release_obj);
                tvCheck.setText(singleSelectionBean.name);
                if (singleSelectionBean.isSelect) {
                    tvCheck.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.rb_check, 0);
                } else {
                    tvCheck.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.rb_default, 0);
                }
                break;
            case SECOND_TYPE:
                holder.setTextByString(R.id.tv_name1_single_select, singleSelectionBean.name);
                if ("ALL".equals(singleSelectionBean.id) && "0".equals(singleSelectionBean.nameNd)) {
                    holder.setTextByString(R.id.tv_name2_single_select, "");
                } else {
                    holder.setTextByString(R.id.tv_name2_single_select, singleSelectionBean.nameNd + "家客户");
                }
                ImageView ivCheck = holder.getView(R.id.iv_single_select);
                if (singleSelectionBean.isSelect) {
                    ivCheck.setImageResource(R.drawable.rb_check);
                } else {
                    ivCheck.setImageResource(R.drawable.rb_default);
                }
                break;
        }
    }

    public void setCheckItem(int position) {
        for (int i = 0; i < mData.size(); i++) {
            if (i == position) {
                mData.get(i).setIsSelect(true);
            } else {
                mData.get(i).setIsSelect(false);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int setItemLayout(int type) {
        if (type == FIRST_TYPE) {
            return R.layout.item_notice_release_obj;
        } else if (type == SECOND_TYPE) {
            return R.layout.item_single_select_2;
        }
        return R.layout.item_notice_release_obj;
    }
}
