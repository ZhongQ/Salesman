package com.salesman.fragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.salesman.R;
import com.salesman.activity.guide.NewActionGuideActivity;
import com.salesman.activity.work.HeroRankingActivity;
import com.salesman.activity.work.OrderListNewActivity;
import com.salesman.activity.work.YeJiTrendActivity;
import com.salesman.adapter.viewholder.HeroRankingListHolder;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseFragment;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.ZhanJiBean;
import com.salesman.presenter.RequestDataPresenter;
import com.salesman.utils.DateUtils;
import com.salesman.utils.StringUtil;
import com.salesman.utils.UserInfoPreference;
import com.salesman.utils.ViewUtil;
import com.salesman.view.OnCommonListener;
import com.studio.jframework.utils.LogUtils;

import java.util.List;
import java.util.Map;

/**
 * 战绩界面
 * Created by LiHuai on 2016/9/22 0022.
 */

public class ZhanJiFragmentNew extends BaseFragment implements View.OnClickListener, OnCommonListener, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = ZhanJiFragmentNew.class.getSimpleName();
    private RequestDataPresenter mPersenter = new RequestDataPresenter(this);
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private TextView tvYeJiTrend, tvAllHero, tvOrderDetail;
    private TextView tvDianBao, tvZiYing, tvNumOrder, tvActiveClient, tvNewClient;
    private EasyRecyclerView recyclerView;
    private RecyclerArrayAdapter<ZhanJiBean.DataBean.RankingBean> adapter;

    @Override
    protected int setLayout() {
        return R.layout.fragment_zhanji_new;
    }

    @Override
    protected void findViews(View view) {
        recyclerView = (EasyRecyclerView) view.findViewById(R.id.rv_ranking);
        initRecyclerView(recyclerView);
        adapter = new RecyclerArrayAdapter<ZhanJiBean.DataBean.RankingBean>(mContext) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new HeroRankingListHolder(parent, R.layout.item_ranking_list);
            }
        };
        recyclerView.setAdapter(adapter);
        final View headView = View.inflate(mContext, R.layout.view_zhanji_first_part, null);
        tvYeJiTrend = (TextView) headView.findViewById(R.id.tv_yeji_trend);
        tvAllHero = (TextView) headView.findViewById(R.id.tv_all_hero);
        tvOrderDetail = (TextView) headView.findViewById(R.id.tv_order_detail);

        tvDianBao = (TextView) headView.findViewById(R.id.tv_dianbao_deal);
        tvZiYing = (TextView) headView.findViewById(R.id.tv_ziying_deal);
        tvNumOrder = (TextView) headView.findViewById(R.id.tv_num_order);
        tvActiveClient = (TextView) headView.findViewById(R.id.tv_active_client);
        tvNewClient = (TextView) headView.findViewById(R.id.tv_new_client);
        ViewUtil.scaleContentView((ViewGroup) headView);
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return headView;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
    }

    @Override
    protected void initialization() {
        mPersenter.getData();
    }

    @Override
    protected void bindEvent() {
        recyclerView.setRefreshListener(this);
        tvYeJiTrend.setOnClickListener(this);
        tvAllHero.setOnClickListener(this);
        tvOrderDetail.setOnClickListener(this);
    }

    @Override
    protected void onCreateView() {

    }

    @Override
    protected void onVisible(boolean prepared) {
        super.onVisible(prepared);
        if (prepared && !StringUtil.isOpenGuide(TAG)) {
            Intent intent = new Intent(mContext, NewActionGuideActivity.class);
            intent.putExtra("come_from", TAG);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_yeji_trend:// 业绩趋势
                startActivity(new Intent(mContext, YeJiTrendActivity.class));
                break;
            case R.id.tv_all_hero:// 全部榜单
                startActivity(new Intent(mContext, HeroRankingActivity.class));
                break;
            case R.id.tv_order_detail:// 订单明细
                startActivity(new Intent(mContext, OrderListNewActivity.class));
                break;
        }
    }

    @Override
    public Context getRequestContext() {
        return mContext;
    }

    @Override
    public String getRequestUrl() {
        return Constant.moduleZhanJiData;
    }

    @Override
    public Map<String, String> getRequestParam() {
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("createTime", DateUtils.getCurrentDate());
        map.put("deptId", mUserInfo.getDeptId());
        map.put("userType", mUserInfo.getUserType());
        return map;
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
        recyclerView.setRefreshing(false);
    }

    @Override
    public void requestSuccess(String response) {
        LogUtils.d(TAG, response);
        ZhanJiBean zhanJiBean = GsonUtils.json2Bean(response, ZhanJiBean.class);
        if (zhanJiBean != null) {
            if (zhanJiBean.success && zhanJiBean.data != null) {
                setAnimation(0, zhanJiBean.data.turnover, 1000, tvDianBao);
                setAnimation(0, zhanJiBean.data.zjturnover, 1000, tvZiYing);
                tvNumOrder.setText(String.valueOf(zhanJiBean.data.orderCount));
                tvActiveClient.setText(String.valueOf(zhanJiBean.data.activeStore));
                tvNewClient.setText(String.valueOf(zhanJiBean.data.regStore));

                List<ZhanJiBean.DataBean.RankingBean> mData = zhanJiBean.data.list;
                if (mData != null && !mData.isEmpty()) {
                    adapter.clear();
                    adapter.addAll(mData);
                }
            }
        }
    }

    @Override
    public void requestFail() {

    }

    @Override
    public void onRefresh() {
        mPersenter.getData();
    }

    private void setAnimation(int start, int end, int during, final TextView tvText) {
        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(during);
        animator.setIntValues(start, end);
        animator.setInterpolator(new LinearInterpolator());// 时间插值;LinearInterpolator：表示以常量速率改变
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                tvText.setText(String.valueOf((int) animation.getAnimatedValue()));
                tvText.setText(StringUtil.formatNumbers((int) animation.getAnimatedValue()));
            }
        });
        animator.start();
    }
}
