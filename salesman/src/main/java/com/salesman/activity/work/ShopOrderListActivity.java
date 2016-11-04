package com.salesman.activity.work;

import android.content.Context;
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
import com.salesman.utils.StaticData;
import com.salesman.view.OnCommonListener;
import com.studio.jframework.utils.LogUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 店铺订单明细界面
 * Created by LiHuai on 2016/10/13.
 * v2.0.0
 */
public class ShopOrderListActivity extends BaseActivity implements View.OnClickListener, OnCommonListener, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnMoreListener {
    private final String TAG = ShopOrderListActivity.class.getSimpleName();
    private RequestDataPresenter mPresenter = new RequestDataPresenter(this);
    private List<Integer> circleList = StaticData.getCircleColorList();

    private EasyRecyclerView recyclerView;
    private RecyclerArrayAdapter<OrderListBean2.DataBean.OrderBean> adapter;
    private String storeId = "";
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
        tvTitle.setText("历史订单");
        TextView tvBack = findView(R.id.tv_top_left);
        tvBack.setOnClickListener(this);

        recyclerView = findView(R.id.rv_order_list);
        initRecyclerView(recyclerView);
        adapter = new RecyclerArrayAdapter<OrderListBean2.DataBean.OrderBean>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new OrderListHolder(parent, R.layout.item_order_list, true);
            }
        };
        recyclerView.setAdapterWithProgress(adapter);


        recyclerView.setRefreshListener(this);
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
    }

    @Override
    protected void initData() {
        super.initData();
        storeId = getIntent().getStringExtra("storeId");
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
        return Constant.moduleShopOrderList;
    }

    @Override
    public Map<String, String> getRequestParam() {
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("storeId", storeId);
//        map.put("createTime", DateUtils.getCurrentDate());
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
}
