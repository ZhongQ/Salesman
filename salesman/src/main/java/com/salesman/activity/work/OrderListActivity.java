package com.salesman.activity.work;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.adapter.OrderListAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.OrderListBean;
import com.salesman.global.HeadViewHolder;
import com.salesman.network.BaseHelper;
import com.salesman.utils.DateUtils;
import com.salesman.utils.EmptyViewUtil;
import com.salesman.utils.StaticData;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserInfoPreference;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.DateUtil;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.listview.LoadMoreListView;
import com.studio.jframework.widget.listview.UltimateListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 订单列表界面
 * Created by LiHuai on 2016/6/16.
 */
public class OrderListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener, UltimateListView.OnItemClickListener, UltimateListView.OnRetryClickListener {
    private static final String TAG = OrderListActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private UltimateListView listView;
    private OrderListAdapter adapter;
    private List<OrderListBean.OrderBean> mData = new ArrayList<>();
    private List<Integer> circleList = StaticData.getCircleIdList();
    private int pageNo = 1;
    private int pageSize = 10;
    private boolean mEnableLoadMore = true;
    private boolean mIsRequesting = false;
    private String createTime = "", isUnion = "", shopNo = "", title = "", salesmanId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_order_list);
    }

    @Override
    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText("订单明细");
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        TextView tvRight = (TextView) findViewById(R.id.tv_top_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("");
        tvRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar_icon, 0);
        createTime = getIntent().getStringExtra("createTime");
        salesmanId = getIntent().getStringExtra("salesmanId");
        isUnion = getIntent().getStringExtra("isUnion");
        shopNo = getIntent().getStringExtra("shopNo");
        title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

        listView = (UltimateListView) findViewById(R.id.lv_order_list);
        listView.addOneOnlyHeader(HeadViewHolder.getHeadView(this), false);
        adapter = new OrderListAdapter(this, mData);
        listView.setAdapter(adapter);

        tvBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        listView.setRefreshListener(this);
        listView.setOnLoadMoreListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnRetryClickListener(this);
    }

    @Override
    protected void initData() {
        listView.postRefresh(new Runnable() {
            @Override
            public void run() {
                pageNo = 1;
                mEnableLoadMore = false;
                listView.setRefreshing(true);
                listView.removeEmptyView();
                getOrderListData();
            }
        });
    }

    private void getOrderListData() {
        if (mIsRequesting) {
            return;
        }
        mIsRequesting = true;
        String url = Constant.moduleZhanJiOrderList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("isUnion", isUnion);
        if (!TextUtils.isEmpty(shopNo)) {
            map.put("shopNo", shopNo);
        }
        if (!TextUtils.isEmpty(createTime)) {
            map.put("createTime", createTime);
        }
        if (!TextUtils.isEmpty(salesmanId)) {
            map.put("salesmanId", salesmanId);
        }
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setListView();
                LogUtils.d(TAG, response);
                OrderListBean orderListBean = GsonUtils.json2Bean(response, OrderListBean.class);
                if (null != orderListBean) {
                    if (orderListBean.success && null != orderListBean.data && null != orderListBean.data.list) {
                        setDatas(orderListBean.data.list);
                    } else {
                        ToastUtil.show(OrderListActivity.this, orderListBean.msg);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setListView();
                EmptyViewUtil.showEmptyView(adapter, listView, pageNo, false);
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
    }

    /**
     * 初始化列表数据
     */
    private void setDatas(List<OrderListBean.OrderBean> data) {
        if (pageNo == 1) {
            mData.clear();
        }
        for (OrderListBean.OrderBean orderBean : data) {
            Random random = new Random();
            int index = random.nextInt(circleList.size());
            orderBean.setImgId(circleList.get(index));
        }
        mData.addAll(data);
        adapter.setData(mData);
        if (data.size() < pageSize) {
            mEnableLoadMore = false;
            if (pageNo != 1) {
                listView.addLoadingFooter();
                listView.setLoadingState(UltimateListView.FOOTER_NOMORE);
            }
        } else {
            mEnableLoadMore = true;
            pageNo++;
        }
        EmptyViewUtil.showEmptyViewNoData(EmptyViewUtil.SUBORDINATE, false, adapter, listView);
    }

    /**
     * 设置ListView属性
     */
    private void setListView() {
        mIsRequesting = false;
        listView.setRefreshing(false);
        listView.setLoadingState(UltimateListView.FOOTER_NONE);
    }

    /**
     * 日期选择器
     */
    private void showCalendar() {
        Calendar c = Calendar.getInstance();
        // THEME_HOLO_LIGHT
        new DatePickerDialog(this, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                createTime = DateUtils.fmtTimeToStr((year + "-" + (month + 1) + "-" + dayOfMonth), "yyyy-MM-dd");
                Date selectDate = DateUtil.toShortDate(createTime);
                Date todayDate = DateUtil.toShortDate(new Date());
                if (selectDate.before(todayDate) || selectDate.equals(todayDate)) {
                    initData();
                } else {
                    ToastUtil.show(OrderListActivity.this, getResources().getString(R.string.date_after_today));
                }
            }

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.tv_top_right:
                showCalendar();
                break;
        }
    }

    @Override
    public void onRefresh() {
        mEnableLoadMore = false;
        pageNo = 1;
        getOrderListData();

    }

    @Override
    public void loadMore() {
        if (mEnableLoadMore) {
            listView.addLoadingFooter();
            listView.setLoadingState(UltimateListView.FOOTER_LOADING);
            getOrderListData();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

    }

    @Override
    public void onRetryLoad() {
        initData();
    }
}
