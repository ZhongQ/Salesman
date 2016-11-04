package com.salesman.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.salesman.R;
import com.salesman.entity.DailyCommentListBean;
import com.salesman.entity.DailyDetailsBean;
import com.salesman.entity.DailyListBean;
import com.salesman.utils.PictureUtil;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.StringUtil;
import com.salesman.utils.ViewUtil;
import com.salesman.views.CircleHeadView;
import com.studio.jframework.widget.listview.InnerListView;

import java.util.List;

/**
 * 日报列表适配器（我的日报和下属日报）
 * Created by LiHuai on 2016/08/04 0030.
 */
public class DailyListAdapter3 extends CommonAdapter<DailyListBean.DailyBean> {
    private CommentListener commentListener;
    private DisplayImageOptions options;

    public DailyListAdapter3(Context context, List<DailyListBean.DailyBean> data, CommentListener commentListener) {
        super(context, data);
        this.commentListener = commentListener;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_image)
                .showImageForEmptyUri(R.drawable.default_image)
                .showImageOnFail(R.drawable.default_image)
                .cacheInMemory(true)// 开启内存缓存
                .cacheOnDisk(true) // 开启硬盘缓存
                .resetViewBeforeLoading(false).build();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, final int position, final DailyListBean.DailyBean dailyBean) {
        // 图片
        ImageView iv = holder.getView(R.id.iv_log);
        if (null != dailyBean.picList && !dailyBean.picList.isEmpty()) {
            iv.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(dailyBean.picList.get(0), iv, options);
        } else {
            iv.setVisibility(View.GONE);
        }
        // 头像
        CircleHeadView cirView = holder.getView(R.id.cir_head_daily);
        cirView.setCircleColorResources(dailyBean.imgId);
        cirView.setTextContent(ReplaceSymbolUtil.cutStringLastTwo(dailyBean.userName));
        holder.setTextByString(R.id.tv_creater, dailyBean.userName);
        holder.setTextByString(R.id.tv_dept_post, dailyBean.deptName + "    " + dailyBean.postName);
        holder.setTextByString(R.id.tv_time_log, dailyBean.createTime + "  " + dailyBean.timePoint);
        holder.setTextByString(R.id.tv_type_log, dailyBean.tmplName);

        // 已阅
        if (TextUtils.isEmpty(dailyBean.participant)) {
            holder.setVisibility(R.id.tv_read, View.GONE);
        } else {
            holder.setVisibility(R.id.tv_read, View.VISIBLE);
            holder.setTextByString(R.id.tv_read, StringUtil.setImgToText(mContext, 0, 1, R.drawable.read_eye, dailyBean.participant));
        }
        // 日志内容
        LinearLayout layout = holder.getView(R.id.lay_report_list);
        layout.removeAllViews();
        if (null != dailyBean.fieldList && dailyBean.fieldList.size() > 0) {
            for (DailyDetailsBean.FieldBean fieldBean : dailyBean.fieldList) {
                View itemView = View.inflate(mContext, R.layout.item_report_list_public, null);
                TextView tvKey = (TextView) itemView.findViewById(R.id.tv_key_report);
                TextView tvValue = (TextView) itemView.findViewById(R.id.tv_value_report);
                tvKey.setText(fieldBean.key + "：");
                tvValue.setText(fieldBean.value);
                ViewUtil.scaleContentView((ViewGroup) itemView);
                layout.addView(itemView);
            }
        }
        // 评论和回复
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
        // 评论按钮
        TextView tvComment = holder.getView(R.id.tv_comment_log);
        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != commentListener) {
                    commentListener.commentListener(dailyBean);
                }
            }
        });
        // 查看大图
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureUtil.lookBigPicFromNet2(mContext, 0, dailyBean.picList);
            }
        });
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_daily_list3;
    }

    public interface CommentListener {
        void commentListener(DailyListBean.DailyBean dailyBean);

        void replayListener(DailyListBean.DailyBean dailyBean, DailyCommentListBean.CommentBean commentBean);
    }
}
