package com.salesman.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.HomeMsgListBean;
import com.salesman.entity.NewestMesBean;
import com.salesman.utils.StringUtil;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 首页消息列表和阿街提醒列表适配器
 * Created by LiHuai on 2016/8/11 0011.
 */
public class HomeMsgListAdapter extends CommonAdapter<HomeMsgListBean.MsgBean> {
    public static final int ITEM_TYPE_0 = 0;
    public static final int ITEM_TYPE_1 = 1;

    private int itemType = ITEM_TYPE_0;

    public HomeMsgListAdapter(Context context, List<HomeMsgListBean.MsgBean> data) {
        super(context, data);
        this.itemType = ITEM_TYPE_0;
    }

    public HomeMsgListAdapter(Context context, List<HomeMsgListBean.MsgBean> data, int itemType) {
        super(context, data);
        this.itemType = itemType;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return itemType;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, HomeMsgListBean.MsgBean msgBean) {
        switch (getItemViewType(position)) {
            case ITEM_TYPE_0:
                holder.setTextByString(R.id.tv_subject, msgBean.typeName);
                holder.setTextByString(R.id.tv_content, msgBean.subject);
                holder.setTextByString(R.id.tv_time, msgBean.createTime);
                ImageView ivMsg = holder.getView(R.id.iv_msg);
                switch (msgBean.type) {
                    case "1":// 公告
                        ivMsg.setImageResource(R.drawable.notice_msg_icon);
                        break;
                    case "2":// 日志
                        ivMsg.setImageResource(R.drawable.log_msg_icon);
                        break;
                    case "3":// 评论
                        ivMsg.setImageResource(R.drawable.reply_comment_msg_icon);
                        // 更新个人中心消息
                        NewestMesBean.NewsBean newsBean;
                        if (!TextUtils.isEmpty(msgBean.total)) {
                            newsBean = new NewestMesBean.NewsBean(Integer.parseInt(msgBean.total));
                        } else {
                            newsBean = new NewestMesBean.NewsBean(0);
                        }
                        EventBus.getDefault().post(newsBean);
                        break;
                    case "4":// 审核
                        ivMsg.setImageResource(R.drawable.check_msgs_icon);
                        break;
                    case "5":// 阿街提醒
                        ivMsg.setImageResource(R.drawable.remind_msg_icon);
                        break;
                    case "6":// 拜访计划
                        ivMsg.setImageResource(R.drawable.visit_plan_msgs_icon);
                        break;
                    default:
                        ivMsg.setImageResource(R.drawable.notice_msg_icon);
                        break;
                }
                TextView tvRedPoint = holder.getView(R.id.tv_red_point);
                String total = StringUtil.getMsgsNum(msgBean.total);
                if (!TextUtils.isEmpty(total)) {
                    tvRedPoint.setText(total);
                    tvRedPoint.setVisibility(View.VISIBLE);
                } else {
                    tvRedPoint.setVisibility(View.GONE);
                }
                break;
            case ITEM_TYPE_1:
                holder.setTextByString(R.id.tv_title_remind, msgBean.typeName);
                holder.setTextByString(R.id.tv_content_remind, msgBean.subject);
                holder.setTextByString(R.id.tv_time_remind, msgBean.createTime);
                break;
        }

    }

    @Override
    public int setItemLayout(int type) {
        if (ITEM_TYPE_0 == type) {
            return R.layout.item_home_msg;
        } else if (ITEM_TYPE_1 == type) {
            return R.layout.item_ajie_remind;
        } else {
            return R.layout.item_home_msg;
        }
    }
}
