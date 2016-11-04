package com.salesman.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.salesman.R;
import com.salesman.activity.client.AddClientStep1Activity;
import com.salesman.activity.client.ClientCheckListActivity;
import com.salesman.activity.client.ClientInfoActivity;
import com.salesman.activity.client.ClientMapActivity;
import com.salesman.activity.guide.NewActionGuideActivity;
import com.salesman.adapter.viewholder.ClientListHolder;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseFragment;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.common.GsonUtils;
import com.salesman.entity.ClientListBean;
import com.salesman.global.BeanListHolder;
import com.salesman.listener.OnCommonPostListener;
import com.salesman.presenter.RequestDataPresenter;
import com.salesman.umeng.UmengAnalyticsUtil;
import com.salesman.umeng.UmengConfig;
import com.salesman.utils.ClientTypeUtil;
import com.salesman.utils.EmptyViewUtil;
import com.salesman.utils.SalesmanLineUtil;
import com.salesman.utils.StaticData;
import com.salesman.utils.StringUtil;
import com.salesman.utils.UserInfoPreference;
import com.salesman.utils.UserInfoUtil;
import com.salesman.view.OnCommonListener;
import com.salesman.views.popupwindow.ClientPopup;
import com.salesman.views.popupwindow.FilterItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 客户界面
 * Created by LiHuai on 2016/1/25.
 */
public class ClientFragment extends BaseFragment implements View.OnClickListener, ClientPopup.OnItemOnClickListener, ClientTypeUtil.GetClientTypeListener, OnCommonPostListener, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnMoreListener, OnCommonListener, RecyclerArrayAdapter.OnItemClickListener {
    public static final String TAG = ClientFragment.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private RequestDataPresenter mPresenter = new RequestDataPresenter(this);

    private EasyRecyclerView recyclerView;
    private RecyclerArrayAdapter<ClientListBean.ShopBean> adapter;
    private List<Integer> circleList = StaticData.getCircleColorList();
    private int pageNo = 1;
    private int pageSize = 10;
    // 筛选
    private OnFilterListener filterListener;
    private FrameLayout layfiltrate1, layfiltrate2, layfiltrate3, layfiltrate4, layfiltrate5;
    private TextView tvFiltrate1, tvFiltrate2, tvFiltrate3, tvFiltrate4, tvFiltrate5;
    private ImageView ivFiltrate1, ivFiltrate2, ivFiltrate3, ivFiltrate4, ivFiltrate5;
    // 筛选条件控件ID
    private ArrayList<Integer> filter_ids = new ArrayList<>(Arrays.asList(R.id.lay_filtrate1, R.id.lay_filtrate2, R.id.lay_filtrate3, R.id.lay_filtrate4, R.id.lay_filtrate5));
    private int index = -1, indexTemp = -1;
    private List<TextView> tvList = new ArrayList<>();
    private List<ImageView> ivList = new ArrayList<>();
    private ClientPopup clientPopup;
    // 筛选数据
    private List<FilterItem> mSalesmans = new ArrayList<>();
    private List<FilterItem> mLines = new ArrayList<>();
    private List<FilterItem> mTypes = new ArrayList<>();
    private List<FilterItem> mRegisters = BeanListHolder.getClientRegisterFilter();
    private List<FilterItem> mVips = BeanListHolder.getClientVipFilter();
    private String salesmanId = "";
    private String lineId = "";
    private String typeId = "";
    private String registerId = "";
    private String vipType = "";
    // 筛选工具
    private ClientTypeUtil clientTypeUtil;
    private SalesmanLineUtil salesmanLineUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_client;
    }

    @Override
    protected void findViews(View view) {
        ImageView ivLeft = (ImageView) view.findViewById(R.id.iv_top_left);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(R.drawable.map_client_icon);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.tab3);
        ImageView ivClientCheck = (ImageView) view.findViewById(R.id.iv_top_right2);
        ivClientCheck.setImageResource(R.drawable.client_check_icon);
        ivClientCheck.setVisibility(View.VISIBLE);
        ImageView ivAddClient = (ImageView) view.findViewById(R.id.iv_top_right1);
        ivAddClient.setImageResource(R.drawable.add_client_icon);
        ivAddClient.setVisibility(View.GONE);
        ivLeft.setOnClickListener(this);
        ivAddClient.setOnClickListener(this);
        ivClientCheck.setOnClickListener(this);

        recyclerView = (EasyRecyclerView) view.findViewById(R.id.rv_client);
        initRecyclerView(recyclerView);
        EmptyViewUtil.initRecyclerEmptyView(recyclerView, EmptyViewUtil.CLIENT);
        adapter = new RecyclerArrayAdapter<ClientListBean.ShopBean>(mContext) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new ClientListHolder(parent, R.layout.item_client_list_new);
            }
        };
        recyclerView.setAdapterWithProgress(adapter);

        initFiltrate(view);
    }

    /**
     * 初始化筛选条件
     *
     * @param view
     */
    private void initFiltrate(View view) {
        clientPopup = new ClientPopup(mContext, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        clientPopup.setItemOnClickListener(this);
        clientTypeUtil = new ClientTypeUtil();
        clientTypeUtil.setClientTypeListener(this);
        salesmanLineUtil = new SalesmanLineUtil();
        salesmanLineUtil.setOnCommonPostListener(this);
        filterListener = new OnFilterListener();
        layfiltrate1 = (FrameLayout) view.findViewById(R.id.lay_filtrate1);
        layfiltrate2 = (FrameLayout) view.findViewById(R.id.lay_filtrate2);
        layfiltrate3 = (FrameLayout) view.findViewById(R.id.lay_filtrate3);
        layfiltrate4 = (FrameLayout) view.findViewById(R.id.lay_filtrate4);
        layfiltrate5 = (FrameLayout) view.findViewById(R.id.lay_filtrate5);
        tvFiltrate1 = (TextView) view.findViewById(R.id.tv_filtrate1);
        tvFiltrate2 = (TextView) view.findViewById(R.id.tv_filtrate2);
        tvFiltrate3 = (TextView) view.findViewById(R.id.tv_filtrate3);
        tvFiltrate4 = (TextView) view.findViewById(R.id.tv_filtrate4);
        tvFiltrate5 = (TextView) view.findViewById(R.id.tv_filtrate5);
        ivFiltrate1 = (ImageView) view.findViewById(R.id.iv_filtrate1);
        ivFiltrate2 = (ImageView) view.findViewById(R.id.iv_filtrate2);
        ivFiltrate3 = (ImageView) view.findViewById(R.id.iv_filtrate3);
        ivFiltrate4 = (ImageView) view.findViewById(R.id.iv_filtrate4);
        ivFiltrate5 = (ImageView) view.findViewById(R.id.iv_filtrate5);
        layfiltrate1.setOnClickListener(filterListener);
        layfiltrate2.setOnClickListener(filterListener);
        layfiltrate3.setOnClickListener(filterListener);
        layfiltrate4.setOnClickListener(filterListener);
        layfiltrate5.setOnClickListener(filterListener);
        tvList.add(tvFiltrate1);
        tvList.add(tvFiltrate2);
        tvList.add(tvFiltrate3);
        tvList.add(tvFiltrate4);
        tvList.add(tvFiltrate5);
        ivList.add(ivFiltrate1);
        ivList.add(ivFiltrate2);
        ivList.add(ivFiltrate3);
        ivList.add(ivFiltrate4);
        ivList.add(ivFiltrate5);
        if (UserInfoUtil.isAdministrator()) {
            layfiltrate1.setVisibility(View.VISIBLE);
            layfiltrate4.setVisibility(View.GONE);
        } else {
            layfiltrate1.setVisibility(View.GONE);
            layfiltrate4.setVisibility(View.VISIBLE);
        }
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
        adapter.setOnItemClickListener(this);
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
    public void onResume() {
        super.onResume();
        UmengAnalyticsUtil.umengOnPageStart(UmengConfig.CLIENT_PAGE);
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengAnalyticsUtil.umengOnPageEnd(UmengConfig.CLIENT_PAGE);
    }

    /**
     * 初始化列表数据
     */
    private void setDatas(List<ClientListBean.ShopBean> datas) {
        if (pageNo == 1) {
            adapter.clear();
        }
        for (ClientListBean.ShopBean data : datas) {
            data.setImgId(StaticData.getImageId(circleList));
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
    public void onRefresh() {
        pageNo = 1;
        mPresenter.refreshData();
    }

    public void onEventMainThread(String action) {
        if (EventBusConfig.CLIENT_LIST_REFRESH.equals(action)) {
            initialization();
        }
    }

    @Override
    public void onItemClick(FilterItem item, int position) {
        switch (indexTemp) {
            case 0:// 定格
                tvFiltrate1.setText(item.name);
                salesmanId = item.id;
                mLines = SalesmanLineUtil.getLineFilterItemList(item.id);
                tvFiltrate2.setText("全部线路");
                lineId = "";
                break;
            case 1:// 线路
                tvFiltrate2.setText(item.name);
                lineId = item.id;
                break;
            case 2:// 类型
                tvFiltrate3.setText(item.name);
                typeId = item.id;
                break;
            case 3:// 注册
                tvFiltrate4.setText(item.name);
                registerId = item.id;
                break;
            case 4:// 重点客户
                tvFiltrate5.setText(item.name);
                vipType = item.id;
                break;
        }
        initialization();
    }

    @Override
    public void onGetClientTypeSuccess() {
        dismissProgressDialog();
        filterListener.onClick(layfiltrate3);
    }

    @Override
    public void onGetClientTypeFail() {
        dismissProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_right1:
                Intent addIntent = new Intent(getActivity(), AddClientStep1Activity.class);
                addIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(addIntent);
                break;
            case R.id.iv_top_left:
                startActivity(new Intent(getActivity(), ClientMapActivity.class));// 客户地图
                break;
            case R.id.iv_top_right2:
                startActivity(new Intent(getActivity(), ClientCheckListActivity.class));// 客户审核
                break;
        }
    }

    @Override
    public void onSuccessListener() {
        dismissProgressDialog();
        switch (indexTemp) {
            case 0:
                filterListener.onClick(layfiltrate1);
                break;
            case 1:
                filterListener.onClick(layfiltrate2);
                break;
        }
    }

    @Override
    public void onFailListener() {
        dismissProgressDialog();
    }

    @Override
    public Context getRequestContext() {
        return mContext;
    }

    @Override
    public String getRequestUrl() {
        return Constant.moduleClientList;
    }

    @Override
    public Map<String, String> getRequestParam() {
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("salesmanId", salesmanId);
        map.put("lineId", lineId);
        map.put("shopType", typeId);
        map.put("isRegister", registerId);
        map.put("vipType", vipType);
        map.put("deptId", mUserInfo.getDeptId());
        map.put("userType", mUserInfo.getUserType());
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
        ClientListBean clientListBean = GsonUtils.json2Bean(response, ClientListBean.class);
        if (null != clientListBean) {
            if (clientListBean.success && null != clientListBean.data && null != clientListBean.data.list) {
                setDatas(clientListBean.data.list);
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
        ClientListBean.ShopBean shopBean = adapter.getItem(position);
        Intent intent = new Intent(mContext, ClientInfoActivity.class);
        intent.putExtra("shopId", shopBean.shopId);
        intent.putExtra("lineId", shopBean.lineId);
        mContext.startActivity(intent);
    }


    public class OnFilterListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (index == filter_ids.indexOf(v.getId()) && null != clientPopup) {
                clientPopup.dismiss();
                index = -1;
                return;
            } else {
                index = filter_ids.indexOf(v.getId());
                indexTemp = index;
            }
            switch (index) {
                case 0:// 定格
                    if (SalesmanLineUtil.isSecondRequest()) {
                        showProgressDialog(getString(R.string.loading1), false);
                        salesmanLineUtil.getSalesmanAndLineData(false);
                        index = -1;
                        return;
                    } else {
                        if (mSalesmans.isEmpty()) {
                            mSalesmans = SalesmanLineUtil.getSalesmanFilterItemList(true);
                        }
                        clientPopup.addActionList(mSalesmans);
                    }
                    break;
                case 1:// 线路
                    if (SalesmanLineUtil.isSecondRequest()) {
                        showProgressDialog(getString(R.string.loading1), false);
                        salesmanLineUtil.getSalesmanAndLineData(false);
                        index = -1;
                        return;
                    } else {
                        if (mLines.isEmpty()) {
                            if (UserInfoUtil.isAdministrator()) {
                                mLines = SalesmanLineUtil.getLineFilterItemList(salesmanId);
                            } else {
                                mLines = SalesmanLineUtil.getAllLineFilterItemList(true);
                            }
                        }
                        clientPopup.addActionList(mLines);
                    }
                    break;
                case 2:// 类型
                    if (ClientTypeUtil.isSecondRequest()) {
                        showProgressDialog(getString(R.string.loading1), false);
                        clientTypeUtil.getClientTypeData();
                        index = -1;
                        return;
                    } else {
                        if (mTypes.isEmpty()) {
                            mTypes = ClientTypeUtil.getTypeFilterList();
                        }
                        clientPopup.addActionList(mTypes);
                    }
                    break;
                case 3:// 注册
                    clientPopup.addActionList(mRegisters);
                    break;
                case 4:// 重点客户
                    clientPopup.addActionList(mVips);
                    break;
            }
            clientPopup.show(v);
            ivList.get(index).setVisibility(View.VISIBLE);
            tvList.get(index).setBackgroundColor(mContext.getResources().getColor(R.color.transparent));

            clientPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    ivList.get(index).setVisibility(View.GONE);
                    tvList.get(index).setBackgroundResource(R.drawable.tv_grey_bg_shape);
                    index = -1;
                }
            });
        }
    }
}


