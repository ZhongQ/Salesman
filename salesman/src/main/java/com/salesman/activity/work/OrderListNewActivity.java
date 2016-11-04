package com.salesman.activity.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.salesman.R;
import com.salesman.adapter.viewholder.OrderListHolder;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.OrderListBean2;
import com.salesman.presenter.RequestDataPresenter;
import com.salesman.utils.DateUtils;
import com.salesman.utils.EmptyViewUtil;
import com.salesman.utils.StaticData;
import com.salesman.view.OnCommonListener;
import com.studio.jframework.utils.LogUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 订单明细界面
 * Created by LiHuai on 2016/10/12.
 * v2.0.0
 */
public class OrderListNewActivity extends BaseActivity implements View.OnClickListener, OnCommonListener, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnMoreListener, RecyclerArrayAdapter.OnItemClickListener {
    private final String TAG = OrderListNewActivity.class.getSimpleName();
    private RequestDataPresenter mPresenter = new RequestDataPresenter(this);
    private List<Integer> circleList = StaticData.getCircleColorList();

    private EasyRecyclerView recyclerView;
    private RecyclerArrayAdapter<OrderListBean2.DataBean.OrderBean> adapter;
    private int pageNo = 1, pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_order_list_new);
    }

    @Override
    protected void initView() {
        super.initView();
        TextView tvTitle = findView(R.id.tv_top_title);
        tvTitle.setText("今日订单明细");
        TextView tvBack = findView(R.id.tv_top_left);
        tvBack.setOnClickListener(this);

        recyclerView = findView(R.id.rv_order_list);
        initRecyclerView(recyclerView);
        EmptyViewUtil.initRecyclerEmptyView(recyclerView, EmptyViewUtil.NO_DATA);
        adapter = new RecyclerArrayAdapter<OrderListBean2.DataBean.OrderBean>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new OrderListHolder(parent, R.layout.item_order_list);
            }
        };
        recyclerView.setAdapterWithProgress(adapter);


        recyclerView.setRefreshListener(this);
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        onRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
        }
    }

    @Override
    public Context getRequestContext() {
        return this;
    }

    @Override
    public String getRequestUrl() {
        return Constant.moduleOrderList;
    }

    @Override
    public Map<String, String> getRequestParam() {
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("createTime", DateUtils.getCurrentDate());
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
        OrderListBean2 orderListBean2 = GsonUtils.json2Bean(response, OrderListBean2.class);
        if (null != orderListBean2) {
            if (orderListBean2.success && null != orderListBean2.data && null != orderListBean2.data.list) {
                List<OrderListBean2.DataBean.OrderBean> datas = orderListBean2.data.list;
                if (pageNo == 1) {
                    adapter.clear();
                }
                for (OrderListBean2.DataBean.OrderBean data : datas) {
                    Random random = new Random();
                    int index = random.nextInt(circleList.size());
                    data.setImgId(circleList.get(index));
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

    @Override
    public void onItemClick(int position) {
        OrderListBean2.DataBean.OrderBean orderBean = adapter.getItem(position);
        Intent intent = new Intent(this, ShopOrderListActivity.class);
        intent.putExtra("storeId", String.valueOf(orderBean.storeId));
        startActivity(intent);
    }
}
