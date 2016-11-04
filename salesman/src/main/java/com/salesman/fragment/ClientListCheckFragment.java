package com.salesman.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.salesman.R;
import com.salesman.activity.client.ClientCheckDetailActivity;
import com.salesman.adapter.ClientCheckListAdapter;
import com.salesman.adapter.viewholder.ClientCheckListHolder;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseFragment;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.common.GsonUtils;
import com.salesman.entity.ClientCheckListBean;
import com.salesman.network.BaseHelper;
import com.salesman.presenter.RequestDataPresenter;
import com.salesman.umeng.UmengAnalyticsUtil;
import com.salesman.umeng.UmengConfig;
import com.salesman.utils.EmptyViewUtil;
import com.salesman.utils.StaticData;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserInfoPreference;
import com.salesman.view.OnCommonListener;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.listview.UltimateListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.greenrobot.event.EventBus;

/**
 * 客户审核列表界面
 * Created by LiHuai on 2016/6/22 0022.
 */
public class ClientListCheckFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnMoreListener, OnCommonListener, RecyclerArrayAdapter.OnItemClickListener {
    private static final String TAG = ClientListCheckFragment.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private String status = "";
    private EasyRecyclerView recyclerView;
    private RecyclerArrayAdapter<ClientCheckListBean.ClientCheckBean> mAdapter;
    private RequestDataPresenter mPresenter = new RequestDataPresenter(this);

    private UltimateListView listView;
    private ClientCheckListAdapter adapter;
    private List<ClientCheckListBean.ClientCheckBean> mData = new ArrayList<>();

    private List<Integer> circleList = StaticData.getCircleIdList();
    private int pageNo = 1;
    private int pageSize = 10;
    private boolean mEnableLoadMore = true;
    private boolean mIsRequesting = false;

    @Override
    protected int setLayout() {
        return R.layout.fragment_client_list_check;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void findViews(View view) {
        status = getArguments().getString("status");

        recyclerView = (EasyRecyclerView) view.findViewById(R.id.rv_client_check);
        initRecyclerView(recyclerView);
        EmptyViewUtil.initRecyclerEmptyView(recyclerView, EmptyViewUtil.CLIENT);
        mAdapter = new RecyclerArrayAdapter<ClientCheckListBean.ClientCheckBean>(mContext) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new ClientCheckListHolder(parent, R.layout.item_client_list_check);
            }
        };
        recyclerView.setAdapterWithProgress(mAdapter);
    }

    @Override
    protected void initialization() {
        onRefresh();
    }

    @Override
    protected void bindEvent() {
        recyclerView.setRefreshListener(this);
        mAdapter.setMore(R.layout.view_more, this);
        mAdapter.setNoMore(R.layout.view_nomore);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onCreateView() {

    }


    @Override
    public void onResume() {
        super.onResume();
        UmengAnalyticsUtil.umengOnPageStart(UmengConfig.CLIENT_CHECK_PAGE);
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengAnalyticsUtil.umengOnPageEnd(UmengConfig.CLIENT_CHECK_PAGE);
    }

    public void onEventMainThread(String action) {
        if (EventBusConfig.CLIENT_CHECK.equals(action)) {
            initialization();
        }
    }

    /**
     * 获取数据
     */
    private void getClientListDatas() {
        if (mIsRequesting) {
            return;
        }
        mIsRequesting = true;
        String url = Constant.moduleClientCheckList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("status", status);
        map.put("deptId", mUserInfo.getDeptId());
        map.put("userType", mUserInfo.getUserType());
        map.put("mobile", mUserInfo.getMobile());
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setListView();
                LogUtils.d(TAG, response);
                ClientCheckListBean clientCheckListBean = GsonUtils.json2Bean(response, ClientCheckListBean.class);
                if (null != clientCheckListBean) {
                    if (clientCheckListBean.success) {
                        if (null != clientCheckListBean.data && null != clientCheckListBean.data.list) {
                            setDatas(clientCheckListBean.data.list);
                        }
                    } else {
                        ToastUtil.show(mContext, clientCheckListBean.msg);
                        EmptyViewUtil.showEmptyViewNoData(EmptyViewUtil.CLIENT, false, adapter, listView);
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
        VolleyController.getInstance(mContext).addToQueue(post);
    }

    /**
     * 初始化列表数据
     */
    private void setDatas(List<ClientCheckListBean.ClientCheckBean> datas) {
        if (pageNo == 1) {
            mAdapter.clear();
        }
        for (ClientCheckListBean.ClientCheckBean data : datas) {
            Random random = new Random();
            int index = random.nextInt(circleList.size());
            data.setImgId(circleList.get(index));
        }
        mAdapter.addAll(datas);
        if (datas.size() < pageSize) {
            if (pageNo != 1) {
                mAdapter.stopMore();
            } else {
                mAdapter.pauseMore();
            }
        } else {
            mPresenter.setmEnableLoadMore(true);
            pageNo++;
        }
    }

    /**
     * 设置ListView属性
     */
    private void setListView() {
//        mIsRequesting = false;
//        listView.setRefreshing(false);
//        listView.setLoadingState(UltimateListView.FOOTER_NONE);
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        mPresenter.refreshData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public Context getRequestContext() {
        return mContext;
    }

    @Override
    public String getRequestUrl() {
        return Constant.moduleClientCheckList;
    }

    @Override
    public Map<String, String> getRequestParam() {
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("status", status);
        map.put("deptId", mUserInfo.getDeptId());
        map.put("userType", mUserInfo.getUserType());
        map.put("mobile", mUserInfo.getMobile());
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
        ClientCheckListBean clientCheckListBean = GsonUtils.json2Bean(response, ClientCheckListBean.class);
        if (null != clientCheckListBean) {
            if (clientCheckListBean.success) {
                if (null != clientCheckListBean.data && null != clientCheckListBean.data.list) {
                    setDatas(clientCheckListBean.data.list);
                }
            } else {
                ToastUtil.show(mContext, clientCheckListBean.msg);
            }
        }
    }

    @Override
    public void requestFail() {

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
        ClientCheckListBean.ClientCheckBean clientCheckBean = mAdapter.getItem(position);
        Intent intent = new Intent(mContext, ClientCheckDetailActivity.class);
        intent.putExtra("storeId", String.valueOf(clientCheckBean.storeId));
        if (status.equals("2")) {
            intent.putExtra("title", "待审核");
        } else {
            intent.putExtra("title", "已拒绝");
        }
        mContext.startActivity(intent);
    }
}
