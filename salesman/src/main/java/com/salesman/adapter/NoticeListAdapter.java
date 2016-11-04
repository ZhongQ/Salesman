package com.salesman.adapter;

import android.content.Context;

import com.salesman.R;
import com.salesman.entity.NoticeListBean;
import com.salesman.utils.ReplaceSymbolUtil;

import java.util.List;

/**
 * 公告列表适配器
 * Created by LiHuai on 2016/1/31 0031.
 */
public class NoticeListAdapter extends CommonAdapter<NoticeListBean.NoticeBean> {

    public NoticeListAdapter(Context context, List<NoticeListBean.NoticeBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, NoticeListBean.NoticeBean noticeBean) {
        holder.setTextByString(R.id.tv_title_notice, noticeBean.subject);
        holder.setTextByString(R.id.tv_sender_notice, noticeBean.userName + "   " + noticeBean.deptName);
        holder.setTextByString(R.id.tv_time_notice, noticeBean.createTime);
        holder.setTextByString(R.id.tv_notice_content, ReplaceSymbolUtil.reverseReplaceHuanHang(noticeBean.content));
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_notice_list_new;
    }
}
