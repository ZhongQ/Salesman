package com.salesman.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.classic.adapter.CommonRecyclerAdapter;
import com.salesman.R;
import com.salesman.activity.client.ClientCheckListActivity;
import com.salesman.activity.home.DailyTemplateActivity;
import com.salesman.activity.home.FootprintActivity;
import com.salesman.activity.home.MsgsRemindActivity;
import com.salesman.activity.home.OutsideSignInActivity;
import com.salesman.activity.home.SignInActivity;
import com.salesman.activity.manage.NoticeListActivity;
import com.salesman.activity.manage.ReleaseNoticeActivity;
import com.salesman.activity.manage.TodaySigninActivity;
import com.salesman.activity.manage.TodayTrackActivity;
import com.salesman.activity.personal.DailyListActivity;
import com.salesman.activity.personal.MyMessageActivity;
import com.salesman.activity.personal.MyVisitPlanActivity;
import com.salesman.activity.work.VisitListActivity;
import com.salesman.activity.work.VisitPlanActivity;
import com.salesman.activity.work.VisitPlanNewActivity;
import com.salesman.adapter.HomeMsgAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseFragment;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.common.GsonUtils;
import com.salesman.entity.HomeMsgListBean;
import com.salesman.global.BeanListHolder;
import com.salesman.network.BaseHelper;
import com.salesman.presenter.RequestDataPresenter;
import com.salesman.umeng.UmengAnalyticsUtil;
import com.salesman.umeng.UmengConfig;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserConfigPreference;
import com.salesman.utils.UserInfoPreference;
import com.salesman.utils.UserInfoUtil;
import com.salesman.view.OnCommonListener;
import com.salesman.views.popupwindow.ActionItem;
import com.salesman.views.popupwindow.TitlePopup;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.listview.UltimateListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 首页界面
 * 版本V1.6.0
 * Created by LiHuai on 2016/08/11.
 */
public class HomeFragment3 extends BaseFragment implements View.OnClickListener, UltimateListView.OnRetryClickListener, TitlePopup.OnItemOnClickListener, CommonRecyclerAdapter.OnItemClickListener, OnCommonListener {
    public static final String TAG = HomeFragment3.class.getSimpleName();
    public UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();

    private ImageView ivSignin, ivOutsideSign, ivMsgRemind;
    // 标题栏弹窗
    private TitlePopup titlePopup;

    private RecyclerView lvHome;
    private HomeMsgAdapter homeMsgAdapter;
    private List<HomeMsgListBean.MsgBean> mData = new ArrayList<>();
    // Mvp
    private RequestDataPresenter requestDataPresenter = new RequestDataPresenter(this);

    @Override
    protected int setLayout() {
        return R.layout.fragment_home3;
    }

    @Override
    protected void findViews(View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.app_name_simple);
        ImageView ivTopR2 = (ImageView) view.findViewById(R.id.iv_top_right2);
        ivTopR2.setImageResource(R.drawable.more_home_icon);
        ivTopR2.setOnClickListener(this);
        ivMsgRemind = (ImageView) view.findViewById(R.id.iv_top_left);
        ivMsgRemind.setVisibility(View.VISIBLE);
        ivMsgRemind.setImageResource(R.drawable.msg_default_home);
        ivMsgRemind.setOnClickListener(this);

        // 实例化标题栏弹窗
        titlePopup = new TitlePopup(mContext, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (UserInfoUtil.isAdministrator()) {// 管理者
            titlePopup.addActionList(BeanListHolder.getActionItemBeanList(1));
        } else {
            titlePopup.addActionList(BeanListHolder.getActionItemBeanList(2));
        }

        ivSignin = (ImageView) view.findViewById(R.id.iv_signin_home);
        ivOutsideSign = (ImageView) view.findViewById(R.id.iv_outside_signin);
        // RecyclerView
        lvHome = (RecyclerView) view.findViewById(R.id.recyclerview_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        lvHome.setLayoutManager(layoutManager);
        lvHome.setHasFixedSize(true);
        homeMsgAdapter = new HomeMsgAdapter(mContext, R.layout.item_home_msg, mData);
        lvHome.setAdapter(homeMsgAdapter);
    }

    @Override
    protected void initialization() {
    }

    @Override
    protected void bindEvent() {
        ivSignin.setOnClickListener(this);
        ivOutsideSign.setOnClickListener(this);
        homeMsgAdapter.setOnItemClickListener(this);
        titlePopup.setItemOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onCreateView() {
    }

    /**
     * 获取数据
     */
    private void getHomeMsgData() {
        String url = Constant.moduleHomeMsgList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", mUserInfo.getDeptId());
        map.put("userType", mUserInfo.getUserType());
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                HomeMsgListBean homeMsgListBean = GsonUtils.json2Bean(response, HomeMsgListBean.class);
                if (null != homeMsgListBean) {
                    if (homeMsgListBean.success && null != homeMsgListBean.data && null != homeMsgListBean.data.list) {
                        mData.clear();
                        mData.addAll(homeMsgListBean.data.list);
                        homeMsgAdapter.clear();
                        homeMsgAdapter.addAll(mData);
                    } else {
                        ToastUtil.show(mContext, homeMsgListBean.msg);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleyController.getInstance(mContext).addToQueue(post);
    }

    public void onEventMainThread(String action) {
        if (EventBusConfig.HOME_REFRESH.equals(action)) {
            requestDataPresenter.getData();
        }
    }

    @Override
    public void onRetryLoad() {
//        requestDataPresenter.getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume");
        requestDataPresenter.getData();
        UmengAnalyticsUtil.umengOnPageStart(UmengConfig.HOME_PAGE);
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengAnalyticsUtil.umengOnPageEnd(UmengConfig.HOME_PAGE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_signin_home:// 签到（打卡）
                UmengAnalyticsUtil.onEvent(mContext, UmengConfig.WORK_SIGNIN_CLICK);
                startActivity(new Intent(mContext, SignInActivity.class));
                break;
            case R.id.iv_outside_signin:// 外勤签到
                if (mUserConfig.getGotoWork()) {
                    startActivity(new Intent(mContext, OutsideSignInActivity.class));
                } else {
                    ToastUtil.show(mContext, mContext.getString(R.string.first_signin_please));
                }
                break;
            case R.id.iv_top_right2:
                titlePopup.show(v);
                break;
            case R.id.iv_top_left:// 阿街消息提醒
                startActivity(new Intent(mContext, MsgsRemindActivity.class));
                break;
        }
    }

    @Override
    public Context getRequestContext() {
        return mContext;
    }

    @Override
    public String getRequestUrl() {
        return Constant.moduleHomeMsgList;
    }

    @Override
    public Map<String, String> getRequestParam() {
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", mUserInfo.getDeptId());
        map.put("userType", mUserInfo.getUserType());
        return map;
    }

    @Override
    public void showLoading() {
//        showProgressDialog(getString(R.string.loading1), false);
    }

    @Override
    public void hideLoading() {
        dismissProgressDialog();
    }

    @Override
    public void requestSuccess(String response) {
        HomeMsgListBean homeMsgListBean = GsonUtils.json2Bean(response, HomeMsgListBean.class);
        if (null != homeMsgListBean) {
            if (homeMsgListBean.success && null != homeMsgListBean.data && null != homeMsgListBean.data.list) {
                mData.clear();
                mData.addAll(homeMsgListBean.data.list);
                homeMsgAdapter.clear();
                homeMsgAdapter.addAll(mData);
                if (!TextUtils.isEmpty(homeMsgListBean.data.warnTotal) && Integer.parseInt(homeMsgListBean.data.warnTotal) > 0) {
                    ivMsgRemind.setImageResource(R.drawable.msg_new_home);
                } else {
                    ivMsgRemind.setImageResource(R.drawable.msg_default_home);
                }
            } else {
                ToastUtil.show(mContext, homeMsgListBean.msg);
            }
        }
    }

    @Override
    public void requestFail() {
        ToastUtil.show(mContext, "网络异常，请检查网络设置");
    }

    @Override
    public void onItemClick(ActionItem item, int position) {
        if (null != item) {
            int id = item.id;
            switch (id) {
                case 1:// 发公告
                    UmengAnalyticsUtil.onEvent(mContext, UmengConfig.HOME_NOTICE);
                    startActivity(new Intent(mContext, ReleaseNoticeActivity.class));
                    break;
                case 2:// 发日志
                    UmengAnalyticsUtil.onEvent(mContext, UmengConfig.HOME_LOG);
                    startActivity(new Intent(mContext, DailyTemplateActivity.class));
                    break;
                case 3:// 足迹
                    Intent footIntent = new Intent(mContext, FootprintActivity.class);
                    footIntent.putExtra("userId", mUserInfo.getUserId());
                    startActivity(footIntent);
                    break;
            }
        }
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
        HomeMsgListBean.MsgBean msgBean = homeMsgAdapter.getItem(position);
        if (null != msgBean) {
            switch (msgBean.type) {
                case "1":// 公告
                    Intent noticeIntent = new Intent(mContext, NoticeListActivity.class);
                    startActivity(noticeIntent);
                    break;
                case "2":// 日志
                    Intent logIntent = new Intent(new Intent(mContext, DailyListActivity.class));
                    logIntent.putExtra("deptId", mUserInfo.getDeptId());
                    startActivity(logIntent);
                    break;
                case "3":// 评论
                    startActivity(new Intent(mContext, MyMessageActivity.class));
                    break;
                case "4":// 客户审核
                    startActivity(new Intent(mContext, ClientCheckListActivity.class));
                    break;
                case "5":// 阿街提醒
                    startActivity(new Intent(mContext, MsgsRemindActivity.class));
                    break;
                case "6":// 拜访计划
                    if (UserInfoUtil.isAdministrator()) {
                        startActivity(new Intent(mContext, VisitPlanActivity.class));
                    } else {
                        startActivity(new Intent(mContext, MyVisitPlanActivity.class));
                    }
                    break;
                case "7":// 今日拜访
                    if (UserInfoUtil.isAdministrator()) {
                        startActivity(new Intent(mContext, VisitListActivity.class));
                    } else {
                        startActivity(new Intent(mContext, VisitPlanNewActivity.class));
                    }
                    break;
                case "8":// 今日足迹
                    UmengAnalyticsUtil.onEvent(mContext, UmengConfig.TODAY_TRACK);
                    mContext.startActivity(new Intent(mContext, TodayTrackActivity.class));
                    break;
                case "9":// 今日签到
                    UmengAnalyticsUtil.onEvent(mContext, UmengConfig.TODAY_SIGNIN);
                    mContext.startActivity(new Intent(mContext, TodaySigninActivity.class));
                    break;
                default:// 其他
                    break;
            }
        }
    }
}
