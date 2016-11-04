package com.salesman.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.DailyCommentListBean;
import com.salesman.entity.DailyDetailsBean;
import com.salesman.entity.DailyListBean;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.ViewUtil;
import com.studio.jframework.widget.listview.InnerListView;

import java.util.List;

/**
 * 日报列表适配器（我的日报和下属日报）
 * Created by LiHuai on 2016/3/30 0030.
 */
public class DailyListAdapter2 extends CommonAdapter<DailyListBean.DailyBean> {
    private CommentListener commentListener;
    private boolean isShowTime = true;// 是否展示日期和星期

    public DailyListAdapter2(Context context, List<DailyListBean.DailyBean> data) {
        super(context, data);
    }

    public DailyListAdapter2(Context context, List<DailyListBean.DailyBean> data, CommentListener commentListener) {
        super(context, data);
        this.commentListener = commentListener;
        this.isShowTime = true;
    }

    public void setIsShowTime(boolean isShowTime) {
        this.isShowTime = isShowTime;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, final DailyListBean.DailyBean dailyBean) {
        ((ImageView) holder.getView(R.id.iv_bg_head_comment)).setImageResource(dailyBean.getImgId());
        ((TextView) holder.getView(R.id.tv_name_short_comment)).setText(ReplaceSymbolUtil.cutStringLastTwo(dailyBean.userName));
        ((TextView) holder.getView(R.id.tv_creater)).setText(dailyBean.userName);
//        ((TextView) holder.getView(R.id.tv_dept_post)).setText(dailyBean.deptName + " " + dailyBean.postName);
        holder.setTextByString(R.id.tv_dept_post, dailyBean.postName);
        if (TextUtils.isEmpty(dailyBean.participant)) {
            holder.setVisibility(R.id.tv_read, View.GONE);
        } else {
            holder.setVisibility(R.id.tv_read, View.VISIBLE);
            holder.setTextByString(R.id.tv_read, "已阅：" + dailyBean.participant);
        }
        // 日志内容
        LinearLayout layout = (LinearLayout) holder.getView(R.id.lay_report_list);
        layout.removeAllViews();
        if (null != dailyBean.fieldList && dailyBean.fieldList.size() > 0) {
            for (DailyDetailsBean.FieldBean fieldBean : dailyBean.fieldList) {
                View itemView = View.inflate(mContext, R.layout.item_report_list_public, null);
                TextView tvKey = (TextView) itemView.findViewById(R.id.tv_key_report);
                TextView tvValue = (TextView) itemView.findViewById(R.id.tv_value_report);
                tvKey.setText(fieldBean.key);
                tvValue.setText(fieldBean.value);
                ViewUtil.scaleContentView((ViewGroup) itemView);
                layout.addView(itemView);
            }
        }

        TextView tvTimeTitle = holder.getView(R.id.tv_time_title_daily);
        TextView tvTime = holder.getView(R.id.tv_time_daily);
        View viewSplit = holder.getView(R.id.view_split_bg);

        if (position == getTimeIndex(dailyBean.createTime) && isShowTime) {
            tvTimeTitle.setVisibility(View.VISIBLE);
            viewSplit.setVisibility(View.GONE);
            tvTimeTitle.setText(dailyBean.createTime + " " + dailyBean.week);
        } else {
            tvTimeTitle.setVisibility(View.GONE);
            viewSplit.setVisibility(View.VISIBLE);
        }

        tvTime.setText(dailyBean.timePoint);

        InnerListView listView = holder.getView(R.id.lv_comment_daily_list);
        DailyCommentListAdapter commentAdapter = new DailyCommentListAdapter(mContext, dailyBean.replyList);
        dailyBean.setCommentListAdapter(commentAdapter);
        listView.setAdapter(commentAdapter);
        if (dailyBean.replyList.size() > 0) {
            listView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.GONE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DailyCommentListBean.CommentBean commentBean = dailyBean.replyList.get(position);
                if (null != commentListener) {
                    commentListener.replayListener(dailyBean, commentBean);
                }
            }
        });

        ImageView ivComment = holder.getView(R.id.iv_comment);
        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != commentListener) {
                    commentListener.commentListener(dailyBean);
                }
            }
        });
    }

    private int getTimeIndex(String date) {
        for (int i = 0; i < mData.size(); i++) {
            if (date.equals(mData.get(i).createTime)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_daily_list;
    }

    public interface CommentListener {
        void commentListener(DailyListBean.DailyBean dailyBean);

        void replayListener(DailyListBean.DailyBean dailyBean, DailyCommentListBean.CommentBean commentBean);
    }
}
