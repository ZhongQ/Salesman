package com.salesman.adapter;

import android.content.Context;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.NoticeReleaseObj;

import java.util.List;

/**
 * 发公告指定发布对象列表适配器
 * Created by LiHuai on 2016/5/26 0026.
 */
public class NoticeReleaseObjAdapter extends CommonAdapter<NoticeReleaseObj> {
    public NoticeReleaseObjAdapter(Context context, List<NoticeReleaseObj> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, NoticeReleaseObj noticeReleaseObj) {
        TextView tvCheck = holder.getView(R.id.tv_release_obj);
        tvCheck.setText(noticeReleaseObj.name);
        if (noticeReleaseObj.isShowSingleBtn) {
            if (noticeReleaseObj.isSelect) {
                tvCheck.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.rb_check, 0);
            } else {
                tvCheck.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.rb_default, 0);
            }
        } else {
            tvCheck.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
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
        return R.layout.item_notice_release_obj;
    }
}
