package com.salesman.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.salesman.R;
import com.salesman.adapter.viewholder.FootprintListHolder;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.TrackDetailListBean;
import com.salesman.network.BaseHelper;
import com.salesman.presenter.RequestDataPresenter;
import com.salesman.utils.DateUtils;
import com.salesman.utils.EmptyViewUtil;
import com.salesman.utils.UserInfoPreference;
import com.salesman.view.OnCommonListener;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.salesman.utils.DeviceUtil.dip2px;

/**
 * 足迹明细列表界面
 * Created by LiHuai on 2016/1/28.
 */
public class FootprintListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnMoreListener, RecyclerArrayAdapter.OnItemClickListener, OnCommonListener {
    public static final String TAG = FootprintListActivity.class.getSimpleName();

    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private TextView tvBack;

    private EasyRecyclerView recyclerView;
    private RecyclerArrayAdapter<TrackDetailListBean.TrackDetailBean> adapter;
    private List<TrackDetailListBean.TrackDetailBean> mDatas = new ArrayList<>();
    private RequestDataPresenter mPresenter = new RequestDataPresenter(this);

    private int pageNo = 1;
    private int pageSize = 20;
    private boolean mIsRequesting = false;
    private String userId = "";
    private String createTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_footprint_list);
    }

    @Override
    protected void initView() {
        tvBack = (TextView) findViewById(R.id.tv_top_left);
        TextView tvTitel = (TextView) findViewById(R.id.tv_top_title);
        tvTitel.setText(R.string.footprint);
        userId = getIntent().getStringExtra("userId");
        createTime = getIntent().getStringExtra("createTime");

        recyclerView = (EasyRecyclerView) findViewById(R.id.rv_footprint);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initRecyclerView(recyclerView);
        EmptyViewUtil.initRecyclerEmptyView(recyclerView, EmptyViewUtil.TRACK);
        DividerDecoration itemDecoration = new DividerDecoration(getResources().getColor(R.color.color_e5e5e5), dip2px(this, 0.5f), 0, 0);
        itemDecoration.setDrawLastItem(true);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new RecyclerArrayAdapter<TrackDetailListBean.TrackDetailBean>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new FootprintListHolder(parent, R.layout.item_track_detail);
            }
        };
        recyclerView.setAdapterWithProgress(adapter);

        tvBack.setOnClickListener(this);
        recyclerView.setRefreshListener(this);
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        onRefresh();
    }

    /**
     * 获取足迹列表数据
     */
    private void getFootListData() {
        if (mIsRequesting) {
            return;
        }
        mIsRequesting = true;
        String url = Constant.moduleTrackList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        if (TextUtils.isEmpty(userId)) {
            map.put("userId", mUserInfo.getUserId());
        } else {
            map.put("userId", userId);
        }
        if (TextUtils.isEmpty(createTime)) {
            map.put("createTime", DateUtils.getCurrentDate());
        } else {
            map.put("createTime", createTime);
        }
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mIsRequesting = false;
                LogUtils.d(TAG, response);
                TrackDetailListBean bean = GsonUtils.json2Bean(response, TrackDetailListBean.class);
                if (null != bean && bean.success) {
                    if (null != bean.data && null != bean.data.list) {
                        setListDatas(bean.data.list);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mIsRequesting = false;
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
    }

    /**
     * 初始化列表数据
     */
    private void setListDatas(List<TrackDetailListBean.TrackDetailBean> datas) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        mPresenter.refreshData();
    }

    @Override
    public void onMoreShow() {
        LogUtils.d(TAG, "onMoreShow");
        mPresenter.loadMore();
    }

    @Override
    public void onMoreClick() {

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = getIntent();
        TrackDetailListBean.TrackDetailBean bean = adapter.getItem(position);
        intent.putExtra("longitude", bean.longitude);
        intent.putExtra("latitude", bean.latitude);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public Context getRequestContext() {
        return this;
    }

    @Override
    public String getRequestUrl() {
        return Constant.moduleTrackList;
    }

    @Override
    public Map<String, String> getRequestParam() {
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        if (TextUtils.isEmpty(userId)) {
            map.put("userId", mUserInfo.getUserId());
        } else {
            map.put("userId", userId);
        }
        if (TextUtils.isEmpty(createTime)) {
            map.put("createTime", DateUtils.getCurrentDate());
        } else {
            map.put("createTime", createTime);
        }
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
        TrackDetailListBean bean = GsonUtils.json2Bean(response, TrackDetailListBean.class);
        if (null != bean && bean.success) {
            if (null != bean.data && null != bean.data.list) {
                setListDatas(bean.data.list);
            }
        }
    }

    @Override
    public void requestFail() {

    }
}
