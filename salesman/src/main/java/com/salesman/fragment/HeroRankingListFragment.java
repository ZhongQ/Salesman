package com.salesman.fragment;


import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.salesman.R;
import com.salesman.adapter.viewholder.HeroRankingList2Holder;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseFragment;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.HeroRankListBean;
import com.salesman.presenter.RequestDataPresenter;
import com.salesman.utils.DateUtils;
import com.salesman.utils.EmptyViewUtil;
import com.salesman.view.OnCommonListener;
import com.studio.jframework.utils.LogUtils;

import java.util.List;
import java.util.Map;

/**
 * 英雄榜列表界面
 * Created by LiHuai on 2016/10/21 0022.
 */
public class HeroRankingListFragment extends BaseFragment implements OnCommonListener, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnMoreListener {
    private final String TAG = HeroRankingListFragment.class.getSimpleName();
    private RequestDataPresenter mPresenter = new RequestDataPresenter(this);

    private EasyRecyclerView recyclerView;
    private RecyclerArrayAdapter<HeroRankListBean.DataBean.RankBean> adapter;
    private int pageNo = 1, pageSize = 10;
    private String timeType = "";
    private TextView tvRank, tvName, tvRankDetail, tvDate;
    private View headView, layRankSelf;

    @Override
    protected int setLayout() {
        return R.layout.fragment_hero_rank_list;
    }

    @Override
    protected void findViews(View view) {
        recyclerView = (EasyRecyclerView) view.findViewById(R.id.rv_yeji_rank);
        initRecyclerView(recyclerView);
        EmptyViewUtil.initRecyclerEmptyView(recyclerView, EmptyViewUtil.NO_DATA);
        adapter = new RecyclerArrayAdapter<HeroRankListBean.DataBean.RankBean>(mContext) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new HeroRankingList2Holder(parent, R.layout.item_ranking_list2);
            }
        };
        recyclerView.setAdapterWithProgress(adapter);
        timeType = getArguments().getString("timeType");

        layRankSelf = view.findViewById(R.id.lay_rank_self);
        tvRank = (TextView) view.findViewById(R.id.tv_rank);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvRankDetail = (TextView) view.findViewById(R.id.tv_rank_detail);


        headView = View.inflate(mContext, R.layout.view_hero_date, null);
        tvDate = (TextView) headView.findViewById(R.id.tv_hero_rank_date);
    }

    @Override
    protected void initialization() {
        onRefresh();
    }

    @Override
    protected void bindEvent() {
        recyclerView.setRefreshListener(this);
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);

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
    protected void onCreateView() {

    }

    @Override
    public Context getRequestContext() {
        return mContext;
    }

    @Override
    public String getRequestUrl() {
        return Constant.moduleHeroRankingList;
    }

    @Override
    public Map<String, String> getRequestParam() {
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("createTime", DateUtils.getCurrentDate());
        map.put("timeType", timeType);
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        return map;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void requestSuccess(String response) {
        LogUtils.d(TAG, response);
        HeroRankListBean heroRankListBean = GsonUtils.json2Bean(response, HeroRankListBean.class);
        if (null != heroRankListBean) {
            if (heroRankListBean.success && null != heroRankListBean.data && null != heroRankListBean.data.list) {
                // 日期
                tvDate.setText(heroRankListBean.data.createTime);
                // 本人排名
                if (null != heroRankListBean.data.myRank) {
                    if (TextUtils.isEmpty(heroRankListBean.data.myRank.salemanId)) {
                        layRankSelf.setVisibility(View.GONE);
                    } else {
                        layRankSelf.setVisibility(View.VISIBLE);
                    }
                    tvRank.setText(String.valueOf(heroRankListBean.data.myRank.seqNo));
                    tvName.setText(String.valueOf(heroRankListBean.data.myRank.salemanName));
                    if (heroRankListBean.data.myRank.raseNum < 0) {
                        tvRankDetail.setVisibility(View.VISIBLE);
                        tvRankDetail.setText("排名下降：" + Math.abs(heroRankListBean.data.myRank.raseNum));
                    } else if (heroRankListBean.data.myRank.raseNum > 0) {
                        tvRankDetail.setVisibility(View.VISIBLE);
                        tvRankDetail.setText("排名上升：" + Math.abs(heroRankListBean.data.myRank.raseNum));
                    } else {
                        tvRankDetail.setVisibility(View.VISIBLE);
                        tvRankDetail.setText("排名不变");
                    }
                }

                List<HeroRankListBean.DataBean.RankBean> datas = heroRankListBean.data.list;
                if (pageNo == 1) {
                    adapter.clear();
                }
                adapter.addAll(datas);
                if (datas.size() < pageSize) {
                    if (pageNo != 1) {
                        adapter.stopMore();
                    } else {
                        adapter.pauseMore();
                    }
                } else {
                    mPresenter.setmEnableLoadMore(true);
                    pageNo++;
                }
            }
        }
    }

    @Override
    public void requestFail() {

    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        mPresenter.refreshData();
    }

    @Override
    public void onMoreShow() {
        mPresenter.loadMore();
    }

    @Override
    public void onMoreClick() {

    }
}
