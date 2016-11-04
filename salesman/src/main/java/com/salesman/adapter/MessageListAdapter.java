package com.salesman.adapter;

import android.content.Context;

import com.salesman.R;
import com.salesman.entity.MessageListBean;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.StringUtil;

import java.util.List;

/**
 * 消息列表适配器(V2.0.0弃用)
 * Created by LiHuai on 2016/4/21 0021.
 */
public class MessageListAdapter extends CommonAdapter<MessageListBean.MessageBean> {

    public MessageListAdapter(Context context, List<MessageListBean.MessageBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, MessageListBean.MessageBean messageBean) {
        switch (getItemViewType(position)) {
            case 0:// 日志评论或回复消息
                holder.setTextByString(R.id.tv_name_short, ReplaceSymbolUtil.cutStringLastTwo(messageBean.replyBy));
                holder.setBackground(R.id.iv_bg_head, messageBean.getImgId());
                holder.setTextByString(R.id.tv_name_send, messageBean.replyBy);
                holder.setTextByString(R.id.tv_time_send, messageBean.createTime + messageBean.timePoint);
                holder.setTextByString(R.id.tv_name_log, messageBean.createBy + "的日志");
                holder.setTextByString(R.id.tv_content_log, StringUtil.getSpannStrMsgs(messageBean.postBy, messageBean.comment));
                break;
            case 1:// 公告或新日志消息
                if (1 == messageBean.type) {// 公告
                    holder.setTextByString(R.id.tv_time_msgs, messageBean.createTime + messageBean.timePoint);
//                    holder.setTextByString(R.id.tv_content_msgs, "您收到" + messageBean.total + "条新公告");
                    holder.setTextColor(R.id.tv_content_msgs, mContext.getResources().getColor(R.color.color_000000));
                } else if (2 == messageBean.type) {// 新日志消息
                    holder.setTextByString(R.id.tv_time_msgs, messageBean.createTime + messageBean.timePoint);
                    holder.setTextByString(R.id.tv_content_msgs, StringUtil.getSpannStrMsgs(messageBean.createBy));
                }
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type == 3 ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int setItemLayout(int type) {
        if (type == 0) {
            return R.layout.item_message_log;
        } else {
            return R.layout.item_message_system;
        }
    }
}
