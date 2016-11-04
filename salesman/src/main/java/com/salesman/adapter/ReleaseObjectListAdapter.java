package com.salesman.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.ReleaseObjectListBean;

import java.util.List;

/**
 * 发布对象列表适配器
 * Created by LiHuai on 2016/2/1 0001.
 */
public class ReleaseObjectListAdapter extends CommonAdapter<ReleaseObjectListBean.ReleaseObjectBean> {

    private CheckBoxListener listener;
    private boolean isSingle;// 是否单选;true单选，false全选

    public ReleaseObjectListAdapter(Context context, List<ReleaseObjectListBean.ReleaseObjectBean> data) {
        super(context, data);
    }

    public ReleaseObjectListAdapter(Context context, List<ReleaseObjectListBean.ReleaseObjectBean> data, Boolean isSingle, CheckBoxListener listener) {
        this(context, data);
        this.isSingle = isSingle;
        this.listener = listener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, final int position, final ReleaseObjectListBean.ReleaseObjectBean releaseObjectBean) {
        ((TextView) holder.getView(R.id.tv_name_obj)).setText(releaseObjectBean.userName);
        ((ImageView) holder.getView(R.id.iv_background_obj)).setImageResource(releaseObjectBean.imgId);

        TextView tvNick = holder.getView(R.id.tv_nick_obj);
        int size = releaseObjectBean.userName.length();
        if (size > 2) {
            tvNick.setText(releaseObjectBean.userName.substring(size - 2, size))
            ;
        } else {
            tvNick.setText(releaseObjectBean.userName);
        }

        final CheckBox cb = holder.getView(R.id.checkbox_obj);
        cb.setChecked(releaseObjectBean.isChecked());
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (releaseObjectBean.isChecked()) {
                    cb.setChecked(false);
                    releaseObjectBean.setChecked(false);
                } else {
                    cb.setChecked(true);
                    releaseObjectBean.setChecked(true);
                }
                if (isSingle) {
                    setSingleSelect(position);
                    if (isChooseOne() && null != listener) {
                        listener.singleListener();
                    }
                }
                if (null != listener && !isSingle) {
                    listener.checkListener();
                }
            }
        });
    }

    /**
     * 单选
     */
    public void setSingleSelect(int position) {
        List<ReleaseObjectListBean.ReleaseObjectBean> list = getData();
        for (int i = 0; i < list.size(); i++) {
            if (i != position) {
                list.get(i).setChecked(false);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 是否有选中
     */
    private boolean isChooseOne() {
        for (ReleaseObjectListBean.ReleaseObjectBean bean : mData) {
            if (bean.checked) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_release_object;
    }

    public interface CheckBoxListener {
        void checkListener();

        void singleListener();
    }

}
