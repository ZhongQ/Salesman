package com.salesman.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonRecyclerAdapter;
import com.salesman.R;
import com.salesman.entity.HomeMsgListBean;
import com.salesman.entity.NewestMesBean;
import com.salesman.utils.StringUtil;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 首页RecyclerView适配器
 * Created by LiHuai on 2016/9/18 0018.
 */

public class HomeMsgAdapter extends CommonRecyclerAdapter<HomeMsgListBean.MsgBean> {

    public HomeMsgAdapter(Context context, int layoutResId, List<HomeMsgListBean.MsgBean> data) {
        super(context, layoutResId, data);
    }

    @Override
    public void onUpdate(BaseAdapterHelper holder, HomeMsgListBean.MsgBean msgBean, int position) {
        holder.setText(R.id.tv_subject, msgBean.typeName);
        holder.setText(R.id.tv_content, msgBean.subject);
        holder.setText(R.id.tv_time, msgBean.createTime);
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
            case "7":// 今日拜访
                ivMsg.setImageResource(R.drawable.visit_plan_msgs_icon);
                break;
            case "8":// 今日足迹
                ivMsg.setImageResource(R.drawable.track_today_icon);
                break;
            case "9":// 今日签到
                ivMsg.setImageResource(R.drawable.signin_today_icon);
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
    }
}
