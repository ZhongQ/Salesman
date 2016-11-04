package com.salesman.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.salesman.R;
import com.salesman.entity.MessageListBean;
import com.salesman.utils.StringUtil;
import com.salesman.views.CircleHeadView;

/**
 * 日志点评/回复
 * Created by LiHuai on 2016/10/19 0019.
 */

public class LogMsgsListHolder extends BaseViewHolder<MessageListBean.MessageBean> {
    private TextView tvNameSend, tvContent, tvTime, tvNameLog;
    private CircleHeadView hv;

    public LogMsgsListHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        hv = $(R.id.hv_head_log_msgs);
        tvNameSend = $(R.id.tv_name_send);
        tvContent = $(R.id.tv_content_log);
        tvTime = $(R.id.tv_time_send);
        tvNameLog = $(R.id.tv_name_log);
    }

    @Override
    public void setData(MessageListBean.MessageBean data) {
        super.setData(data);
        hv.setCircleColorResources(data.imgId);
        hv.setTextContent(StringUtil.cutStringLastTwo(data.replyBy));
        tvNameSend.setText(data.replyBy);
        tvContent.setText(StringUtil.getSpannStrMsgs(data.postBy, data.comment));
        tvTime.setText(data.createTime +"  "+ data.timePoint);
        tvNameLog.setText(data.createBy + "的日志");
    }
}
