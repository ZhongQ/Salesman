package com.salesman.activity.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.salesman.R;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.ClientListBean;
import com.salesman.global.BeanListHolder;
import com.salesman.listener.OnCommonPostListener;
import com.salesman.network.BaseHelper;
import com.salesman.utils.ClientTypeUtil;
import com.salesman.utils.LocationCoordinateUtil;
import com.salesman.utils.SalesmanLineUtil;
import com.salesman.utils.UserInfoPreference;
import com.salesman.utils.UserInfoUtil;
import com.salesman.views.ZoomControlView;
import com.salesman.views.popupwindow.ClientPopup;
import com.salesman.views.popupwindow.FilterItem;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 客户地图界面
 * Created by LiHuai on 2016/06/17.
 */
public class ClientMapActivity extends BaseActivity implements View.OnClickListener, ClientPopup.OnItemOnClickListener, ClientTypeUtil.GetClientTypeListener, BaiduMap.OnMarkerClickListener, OnCommonPostListener {
    public static final String TAG = ClientMapActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    // 筛选
    private OnFilterListener filterListener;
    private FrameLayout layfiltrate1, layfiltrate2, layfiltrate3, layfiltrate4, layfiltrate5;
    private TextView tvFiltrate1, tvFiltrate2, tvFiltrate3, tvFiltrate4, tvFiltrate5;
    private ImageView ivFiltrate1, ivFiltrate2, ivFiltrate3, ivFiltrate4, ivFiltrate5;
    // 地图
    private MapView bmapView = null;
    private BaiduMap mBaiduMap = null;
    private MapStatusUpdate msUpdate = null;
    private ZoomControlView mZoomControlView;// 客户坐标
    private List<MarkerOptions> clientOptionsList = new ArrayList<>();
    private BitmapDescriptor bmClientCoord1 = BitmapDescriptorFactory.fromResource(R.drawable.map_register_client);
    private BitmapDescriptor bmClientCoord2 = BitmapDescriptorFactory.fromResource(R.drawable.map_unregister_client);
    // 筛选数据
    private List<FilterItem> mSalesmans = new ArrayList<>();
    private List<FilterItem> mLines = new ArrayList<>();
    private List<FilterItem> mTypes = BeanListHolder.getClientZhiNengFilter();// V2.1.0版本之前为类型筛选，现在改为智能排序
    private List<FilterItem> mRegisters = BeanListHolder.getClientRegisterFilter();
    private List<FilterItem> mVips = BeanListHolder.getClientVipFilter();
    private String salesmanId = "", lineId = "", typeId = "", registerId = "", vipType = "";
    // 筛选工具
    private ClientTypeUtil clientTypeUtil;
    private SalesmanLineUtil salesmanLineUtil;
    // 筛选条件控件ID
    private int index = -1, indexTemp = -1;
    private ClientPopup clientPopup;
    private List<TextView> tvList = new ArrayList<>();
    private List<ImageView> ivList = new ArrayList<>();
    private ArrayList<Integer> filter_ids = new ArrayList<>(Arrays.asList(R.id.lay_filtrate1, R.id.lay_filtrate2, R.id.lay_filtrate3, R.id.lay_filtrate4, R.id.lay_filtrate5));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_map);

        initView();
        initFiltrate();
        initMap();
        initMapEvent();
        getClientList();
    }

    protected void initView() {
        TextView tvLeft = (TextView) findViewById(R.id.tv_top_left);
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setText("");
        tvLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.client_list_icon, 0, 0, 0);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.tab3);
        TextView tvRight = (TextView) findViewById(R.id.tv_top_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("");
        tvRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_client_icon, 0, 0, 0);
        tvRight.setVisibility(View.GONE);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    private void initFiltrate() {
        clientPopup = new ClientPopup(this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        clientPopup.setItemOnClickListener(this);
        clientTypeUtil = new ClientTypeUtil();
        clientTypeUtil.setClientTypeListener(this);
        salesmanLineUtil = new SalesmanLineUtil();
        salesmanLineUtil.setOnCommonPostListener(this);
        filterListener = new OnFilterListener();
        layfiltrate1 = (FrameLayout) findViewById(R.id.lay_filtrate1);
        layfiltrate2 = (FrameLayout) findViewById(R.id.lay_filtrate2);
        layfiltrate3 = (FrameLayout) findViewById(R.id.lay_filtrate3);
        layfiltrate4 = (FrameLayout) findViewById(R.id.lay_filtrate4);
        layfiltrate5 = (FrameLayout) findViewById(R.id.lay_filtrate5);
        tvFiltrate1 = (TextView) findViewById(R.id.tv_filtrate1);
        tvFiltrate2 = (TextView) findViewById(R.id.tv_filtrate2);
        tvFiltrate3 = (TextView) findViewById(R.id.tv_filtrate3);
        tvFiltrate4 = (TextView) findViewById(R.id.tv_filtrate4);
        tvFiltrate5 = (TextView) findViewById(R.id.tv_filtrate5);
        ivFiltrate1 = (ImageView) findViewById(R.id.iv_filtrate1);
        ivFiltrate2 = (ImageView) findViewById(R.id.iv_filtrate2);
        ivFiltrate3 = (ImageView) findViewById(R.id.iv_filtrate3);
        ivFiltrate4 = (ImageView) findViewById(R.id.iv_filtrate4);
        ivFiltrate5 = (ImageView) findViewById(R.id.iv_filtrate5);
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

    private void initMap() {
        // 初始化地图控件
        bmapView = (MapView) findViewById(R.id.bmapView_client);
        mBaiduMap = bmapView.getMap();
        bmapView.showZoomControls(false);// 设置缩放按钮
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//普通地图
        // 自定义缩放按钮
        mZoomControlView = (ZoomControlView) findViewById(R.id.ZoomControlView);
        mZoomControlView.setMapView(bmapView);

        mBaiduMap.setOnMarkerClickListener(this);
    }

    /**
     * 初始化地图监听
     */
    private void initMapEvent() {

        // 地图状态改变监听
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                refreshZoomControl();
            }
        });
    }

    /**
     * 获取客户数据
     */
    private void getClientList() {
        showProgressDialog(getString(R.string.loading1), false);
        String url = Constant.moduleShopSiteList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("salesmanId", salesmanId);
        map.put("lineId", lineId);
        map.put("shopType", typeId);
        map.put("isRegister", registerId);
        map.put("vipType", vipType);
        map.put("deptId", mUserInfo.getDeptId());
        map.put("userType", mUserInfo.getUserType());
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                ClientListBean clientListBean = GsonUtils.json2Bean(response, ClientListBean.class);
                if (null != clientListBean && clientListBean.success) {
                    if (null != clientListBean.data && null != clientListBean.data.list) {
                        clientCoordinateChange(clientListBean.data.list);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
            }
        });
        VolleyController.getInstance(this).addToQueue(post, TAG);
    }

    /**
     * 客户坐标转换
     *
     * @param list
     */
    private void clientCoordinateChange(List<ClientListBean.ShopBean> list) {
        mBaiduMap.clear();
        clientOptionsList.clear();
        if (null != list) {
            for (ClientListBean.ShopBean shopBean : list) {
                if (0d != shopBean.longitude && 0d != shopBean.latitude) {
                    LatLng latLng = new LatLng(shopBean.latitude, shopBean.longitude);
                    MarkerOptions marker = null;
                    if ("1".equals(shopBean.isRegister)) {// 已注册
                        marker = new MarkerOptions().position(latLng).icon(bmClientCoord1).zIndex(9).draggable(false).title(shopBean.shopId).animateType(MarkerOptions.MarkerAnimateType.drop);
                    } else {
                        marker = new MarkerOptions().position(latLng).icon(bmClientCoord2).zIndex(9).draggable(false).title(shopBean.shopId).animateType(MarkerOptions.MarkerAnimateType.drop);
                    }
                    clientOptionsList.add(marker);
                }
            }
            setMapStatus(clientOptionsList);
            if (null != clientOptionsList && !clientOptionsList.isEmpty()) {
                for (MarkerOptions options : clientOptionsList) {
                    if (null != options) {
                        if (mBaiduMap != null) {
                            mBaiduMap.addOverlay(options);
                        }
                    }
                }
            }
        }
    }

    /**
     * 设置地图状态
     *
     * @param list
     */
    private void setMapStatus(List<MarkerOptions> list) {
        if (null != list) {
            if (!list.isEmpty()) {
                int index = list.size() / 2;
                MarkerOptions options = list.get(index);
                LatLng llCentre = options.getPosition();
                msUpdate = MapStatusUpdateFactory.newLatLngZoom(llCentre, 18);
                if (null != mBaiduMap) {
                    mBaiduMap.setMapStatus(msUpdate);
                }
            } else {
                LatLng defaultLatLng = new LatLng(LocationCoordinateUtil.getLatitude(), LocationCoordinateUtil.getLongitude());
                msUpdate = MapStatusUpdateFactory.newLatLngZoom(defaultLatLng, 16);
                if (null != mBaiduMap && null != msUpdate) {
                    try {
                        mBaiduMap.setMapStatus(msUpdate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 地图缩放按钮状态控制
     */
    private void refreshZoomControl() {
        if (null != mZoomControlView && null != mBaiduMap) {
            //更新缩放按钮的状态
            mZoomControlView.refreshZoomButtonStatus(mBaiduMap.getMapStatus().zoom);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        bmapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bmapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleyController.getInstance(this).cancelPendingRequests(TAG);
        bmapView.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.tv_top_right:
                Intent addIntent = new Intent(this, AddClientStep1Activity.class);
                addIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(addIntent);
                break;
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
//                typeId = item.id;// V2.1.0之后不进行类型筛选，改为刷新
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
        getClientList();
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
    public boolean onMarkerClick(Marker marker) {
        if (null != marker) {
            Intent intent = new Intent(this, ClientDetailNewActivity.class);
            intent.putExtra("shopId", marker.getTitle());
            startActivity(intent);
        }
        return false;
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
//                    if (ClientTypeUtil.isSecondRequest()) {
//                        showProgressDialog(getString(R.string.loading1), false);
//                        clientTypeUtil.getClientTypeData();
//                        index = -1;
//                        return;
//                    } else {
//                        if (mTypes.isEmpty()) {
//                            mTypes = ClientTypeUtil.getTypeFilterList();
//                        }
//                        clientPopup.addActionList(mTypes);
//                    }
                    // V2.1.0修改后
                    clientPopup.addActionList(mTypes);
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
            tvList.get(index).setBackgroundColor(getResources().getColor(R.color.transparent));

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
