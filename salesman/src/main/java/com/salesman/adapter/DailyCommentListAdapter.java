package com.salesman.adapter;

import android.content.Context;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.DailyCommentListBean;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.StringUtil;

import java.util.List;

/**
 * 日报列表之评论列表适配器
 * Created by LiHuai on 2016/3/30 0030.
 */
public class DailyCommentListAdapter extends CommonAdapter<DailyCommentListBean.CommentBean> {

    public DailyCommentListAdapter(Context context, List<DailyCommentListBean.CommentBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, DailyCommentListBean.CommentBean commentBean) {
        ((TextView) holder.getView(R.id.tv_comment_daily_list)).setText(StringUtil.getSpannable(commentBean.replyBy, commentBean.postBy, ReplaceSymbolUtil.reverseReplaceHuanHang(commentBean.comment)));

    }

    public void addCommentData(DailyCommentListBean.CommentBean commentBean){
        mData.add(commentBean);
        notifyDataSetChanged();
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_comment_daily_list;
    }
}
