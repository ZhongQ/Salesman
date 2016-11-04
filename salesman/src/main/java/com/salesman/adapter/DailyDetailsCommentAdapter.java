package com.salesman.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.DailyCommentListBean;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.StringUtil;
import com.salesman.views.CircleHeadView;

import java.util.List;

/**
 * 日报详情评论列表适配器
 * Created by LiHuai on 2016/3/30 0030.
 */
public class DailyDetailsCommentAdapter extends CommonAdapter<DailyCommentListBean.CommentBean> {

    public DailyDetailsCommentAdapter(Context context, List<DailyCommentListBean.CommentBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, DailyCommentListBean.CommentBean commentBean) {
        CircleHeadView circleHeadView = holder.getView(R.id.cir_head_daily_comment);
        circleHeadView.setCircleColorResources(commentBean.imgId);
        circleHeadView.setTextContent(ReplaceSymbolUtil.cutStringLastTwo(commentBean.replyBy));
        ((TextView) holder.getView(R.id.tv_time_comment)).setText(commentBean.createTime);
        ((TextView) holder.getView(R.id.tv_content_comment)).setText(ReplaceSymbolUtil.reverseReplaceHuanHang(commentBean.comment));

        TextView tvName = holder.getView(R.id.tv_name_comment);
        tvName.setText(StringUtil.getSpannable(commentBean.replyBy, commentBean.postBy));
        ImageView ivLine = holder.getView(R.id.iv_line_daily_comment);
        if (position == getCount() - 1) {
            ivLine.setVisibility(View.GONE);
        } else {
            ivLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_comment_daily;
    }
}
