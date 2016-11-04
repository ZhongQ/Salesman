package com.salesman.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.salesman.R;
import com.salesman.adapter.CommonAdapter;
import com.salesman.application.SalesManApplication;
import com.studio.jframework.widget.listview.UltimateListView;

/**
 * 无数据或无网络时工具类
 * Created by LiHuai on 2016/2/29 0029.
 */
public class EmptyViewUtil {
    public static final int NOTICE = 1;                 // 公告
    public static final int SIGNIN = 2;                 // 签到
    public static final int TRACK = 3;                  // 足迹
    public static final int CLIENT = 4;                 // 客户
    public static final int SUBORDINATE = 5;            // 下属
    public static final int DAILY = 6;                  // 日报
    public static final int MESSAGE = 7;                // 消息
    public static final int NO_DATA = 8;                // 无数据
    public static final int VISIT = 9;                  // 拜访

    /**
     * 无数据
     *
     * @param type          模块类型
     * @param isShowBtn     是否展示加载按钮
     * @param commonAdapter
     * @param listView
     */
    public static void showEmptyViewNoData(int type, boolean isShowBtn, CommonAdapter commonAdapter, UltimateListView listView) {
        if (null != commonAdapter && null != listView && commonAdapter.getCount() == 0) {
            switch (type) {
                case NOTICE:// 公告
                    listView.showEmptyImage(R.drawable.no_notice, SalesManApplication.getInstance().getString(R.string.no_notice_hint), isShowBtn);
                    break;
                case SIGNIN:// 签到
                    listView.showEmptyImage(R.drawable.no_signin, SalesManApplication.getInstance().getString(R.string.no_signin_hint), isShowBtn);
                    break;
                case TRACK:// 足迹
                    listView.showEmptyImage(R.drawable.no_track, SalesManApplication.getInstance().getString(R.string.no_track_hint), isShowBtn);
                    break;
                case CLIENT:// 客户
                    listView.showEmptyImage(R.drawable.no_client, SalesManApplication.getInstance().getString(R.string.no_client_hint), isShowBtn);
                    break;
                case SUBORDINATE:// 下属或发布对象
                    listView.showEmptyImage(R.drawable.no_data_pic, SalesManApplication.getInstance().getString(R.string.no_data_hint), isShowBtn);
                    break;
                case DAILY:// 日志
                    listView.showEmptyImage(R.drawable.no_data_pic, SalesManApplication.getInstance().getString(R.string.no_data_hint), isShowBtn);
                    break;
                case MESSAGE:// 消息
                    listView.showEmptyImage(R.drawable.no_message, SalesManApplication.getInstance().getString(R.string.no_message_hint), isShowBtn);
                    break;
                case NO_DATA:// 无数据
                    listView.showEmptyImage(R.drawable.no_data_pic, SalesManApplication.getInstance().getString(R.string.no_data_hint), isShowBtn);
                    break;
                case VISIT:// 无拜访
                    listView.showEmptyImage(R.drawable.no_data_pic, SalesManApplication.getInstance().getString(R.string.no_visit_plan), isShowBtn);
                    break;
            }
        }
    }

    /**
     * 无网络
     *
     * @param commonAdapter
     * @param listView
     * @param pageNo
     * @param isShowBtn     false 不展示加载按钮；true 展示加载按钮
     */
    public static void showEmptyView(CommonAdapter commonAdapter, UltimateListView listView, int pageNo, boolean isShowBtn) {
        if (null != commonAdapter && null != listView && 1 == pageNo && commonAdapter.getCount() == 0) {
            listView.showEmptyImage(R.drawable.no_networks, SalesManApplication.getInstance().getString(R.string.no_net_hint), isShowBtn);
        }
    }

    /**
     * 无数据(首页)
     *
     * @param commonAdapter
     * @param listView
     */
    public static void showEmptyViewHome(CommonAdapter commonAdapter, UltimateListView listView) {
        if (null != commonAdapter && null != listView && commonAdapter.getCount() == 0) {
            listView.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化EasyRecyclerView,无数据时情况
     *
     * @param recyclerView
     * @param type
     */
    public static void initRecyclerEmptyView(EasyRecyclerView recyclerView, int type) {
        if (null == recyclerView) {
            return;
        }
        View view = recyclerView.getEmptyView();
        ImageView iv = (ImageView) view.findViewById(R.id.iv_no_data);
        TextView tv = (TextView) view.findViewById(R.id.tv_no_data);
        switch (type) {
            case NOTICE:// 公告
                iv.setImageResource(R.drawable.no_notice);
                tv.setText(SalesManApplication.getInstance().getString(R.string.no_client_hint));
                break;
            case SIGNIN:// 签到
                iv.setImageResource(R.drawable.no_signin);
                tv.setText(SalesManApplication.getInstance().getString(R.string.no_signin_hint));
                break;
            case TRACK:// 足迹
                iv.setImageResource(R.drawable.no_track);
                tv.setText(SalesManApplication.getInstance().getString(R.string.no_track_hint));
                break;
            case CLIENT:// 客户
                iv.setImageResource(R.drawable.no_client);
                tv.setText(SalesManApplication.getInstance().getString(R.string.no_client_hint));
                break;
            case SUBORDINATE:// 下属或发布对象
                iv.setImageResource(R.drawable.no_data_pic);
                tv.setText(SalesManApplication.getInstance().getString(R.string.no_data_hint));
                break;
            case DAILY:// 日志
                iv.setImageResource(R.drawable.no_data_pic);
                tv.setText(SalesManApplication.getInstance().getString(R.string.no_data_hint));
                break;
            case MESSAGE:// 消息
                iv.setImageResource(R.drawable.no_message);
                tv.setText(SalesManApplication.getInstance().getString(R.string.no_message_hint));
                break;
            case NO_DATA:// 无数据
                iv.setImageResource(R.drawable.no_data_pic);
                tv.setText(SalesManApplication.getInstance().getString(R.string.no_data_hint));
                break;
            case VISIT:// 无拜访
                iv.setImageResource(R.drawable.no_data_pic);
                tv.setText(SalesManApplication.getInstance().getString(R.string.no_visit_plan));
                break;
        }
    }

}
