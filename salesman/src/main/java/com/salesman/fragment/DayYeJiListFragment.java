package com.salesman.fragment;


import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.salesman.R;
import com.salesman.adapter.viewholder.DayYeJiListHolder;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseFragment;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.SubordinateListBean;
import com.salesman.entity.YeJiListBean;
import com.salesman.presenter.RequestDataPresenter;
import com.salesman.utils.DateUtils;
import com.salesman.utils.EmptyViewUtil;
import com.salesman.utils.StaticData;
import com.salesman.utils.UserInfoPreference;
import com.salesman.view.OnCommonListener;
import com.studio.jframework.utils.LogUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 日业绩列表界面
 * Created by LiHuai on 2016/09/28 0022.
 */
public class DayYeJiListFragment extends BaseFragment implements OnCommonListener, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnMoreListener {
    private final String TAG = DayYeJiListFragment.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private RequestDataPresenter mPresenter = new RequestDataPresenter(this);
    private List<Integer> circleList = StaticData.getCircleColorList();

    private EasyRecyclerView recyclerView;
    private RecyclerArrayAdapter<YeJiListBean.DataBean.YeJiBean> adapter;
    private int pageNo = 1, pageSize = 10;
    private SubordinateListBean.XiaShuBean xiaShuBean;

    @Override
    protected int setLayout() {
        return R.layout.fragment_yeji_list;
    }

    @Override
    protected void findViews(View view) {
        recyclerView = (EasyRecyclerView) view.findViewById(R.id.rv_yeji_rank);
        initRecyclerView(recyclerView);
        EmptyViewUtil.initRecyclerEmptyView(recyclerView, EmptyViewUtil.NO_DATA);
        adapter = new RecyclerArrayAdapter<YeJiListBean.DataBean.YeJiBean>(mContext) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new DayYeJiListHolder(parent, R.layout.item_yeji_day_list);
            }
        };
        recyclerView.setAdapterWithProgress(adapter);
    }

    @Override
    protected void initialization() {
        xiaShuBean = (SubordinateListBean.XiaShuBean) getArguments().getSerializable("XiaShu");
        onRefresh();
    }

    @Override
    protected void bindEvent() {
        recyclerView.setRefreshListener(this);
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
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
        return Constant.moduleYeJiList;
    }

    @Override
    public Map<String, String> getRequestParam() {
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("createTime", DateUtils.getCurrentDate());
        map.put("timeType", "");
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        map.put("deptName", mUserInfo.getDeptName());
        if (null != xiaShuBean) {
            map.put("userId", xiaShuBean.userId);
            map.put("deptId", xiaShuBean.deptId);
            map.put("deptName", xiaShuBean.deptName);
            map.put("userType", xiaShuBean.userType);
        }
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
        YeJiListBean yeJiListBean = GsonUtils.json2Bean(response, YeJiListBean.class);
        if (null != yeJiListBean) {
            if (yeJiListBean.success && null != yeJiListBean.data && null != yeJiListBean.data.list) {
                List<YeJiListBean.DataBean.YeJiBean> datas = yeJiListBean.data.list;
                if (pageNo == 1) {
                    adapter.clear();
                }
                for (YeJiListBean.DataBean.YeJiBean data : datas) {
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
