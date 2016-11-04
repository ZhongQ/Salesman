package com.salesman.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.home.FootprintActivity;
import com.salesman.adapter.MyTrackListAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.MyTrackListBean;
import com.salesman.global.HeadViewHolder;
import com.salesman.network.BaseHelper;
import com.salesman.utils.EmptyViewUtil;
import com.salesman.utils.UserInfoPreference;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.listview.LoadMoreListView;
import com.studio.jframework.widget.listview.UltimateListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 我的足迹列表界面
 * Created by LiHuai on 2016/1/28.
 */
public class MyTrackActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener, UltimateListView.OnItemClickListener, UltimateListView.OnRetryClickListener {
    public static final String TAG = MyTrackActivity.class.getSimpleName();

    private TextView tvBack;
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private UltimateListView listView;
    private MyTrackListAdapter adapter;
    private List<MyTrackListBean.TrackTimeBean> mDatas = new ArrayList<>();
    private int pageNo = 1;
    private int pageSize = 15;
    private boolean mEnableLoadMore = true;
    private boolean mIsRequesting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.common_layout_list_0);
    }

    @Override
    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.my_trace);

        tvBack = (TextView) findViewById(R.id.tv_top_left);
        listView = (UltimateListView) findViewById(R.id.lv_common_0);
        listView.addOneOnlyHeader(HeadViewHolder.getHeadView(this), false);
        adapter = new MyTrackListAdapter(this, mDatas);
        listView.setAdapter(adapter);
        tvBack.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setRefreshListener(this);
        listView.setOnLoadMoreListener(this);
        listView.setOnRetryClickListener(this);
    }

    @Override
    protected void initData() {
        listView.postRefresh(new Runnable() {
            @Override
            public void run() {
                pageNo = 1;
                mEnableLoadMore = false;
                listView.removeEmptyView();
                listView.setRefreshing(true);
                getTrackListDatas();
            }
        });
    }

    /**
     * 获取我的足迹列表数据
     */
    private void getTrackListDatas() {
        if (mIsRequesting) {
            return;
        }
        mIsRequesting = true;
        String url = Constant.moduleMyTrackList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("userId", mUserInfo.getUserId());
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                setListView();
                LogUtils.d(TAG, response);
                MyTrackListBean trackListBean = GsonUtils.json2Bean(response, MyTrackListBean.class);
                if (null != trackListBean && trackListBean.success) {
                    if (null != trackListBean.data && null != trackListBean.data.list) {
                        setDatas(trackListBean.data.list);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setListView();
                EmptyViewUtil.showEmptyView(adapter, listView, pageNo, true);
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
    }

    /**
     * 初始化列表数据
     */
    private void setDatas(List<MyTrackListBean.TrackTimeBean> datas) {
        if (pageNo == 1) {
            mDatas.clear();
        }
        mDatas.addAll(datas);
        adapter.setData(mDatas);
        if (datas.size() < pageSize) {
            mEnableLoadMore = false;
            if (pageNo != 1) {
                listView.addLoadingFooter();
                listView.setLoadingState(UltimateListView.FOOTER_NOMORE);
            }
        } else {
            mEnableLoadMore = true;
            pageNo++;
        }
        EmptyViewUtil.showEmptyViewNoData(EmptyViewUtil.TRACK, false, adapter, listView);
    }

    /**
     * 设置ListView属性
     */
    private void setListView() {
        mIsRequesting = false;
        listView.setRefreshing(false);
        listView.setLoadingState(UltimateListView.FOOTER_NONE);
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
        mEnableLoadMore = false;
        pageNo = 1;
        getTrackListDatas();
    }

    @Override
    public void loadMore() {
        if (mEnableLoadMore) {
            listView.addLoadingFooter();
            listView.setLoadingState(UltimateListView.FOOTER_LOADING);
            getTrackListDatas();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        MyTrackListBean.TrackTimeBean bean = adapter.getItem(position);
        Intent intent = new Intent(this, FootprintActivity.class);
        intent.putExtra("userId", mUserInfo.getUserId());
        intent.putExtra("createTime", bean.createTime);
        startActivity(intent);
    }

    @Override
    public void onRetryLoad() {
        initData();
    }
}
