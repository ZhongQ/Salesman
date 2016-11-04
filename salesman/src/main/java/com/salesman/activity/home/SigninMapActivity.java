package com.salesman.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.salesman.R;
import com.salesman.activity.client.AddClientStep1Activity;
import com.salesman.adapter.ChooseAddressAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.EventBusConfig;
import com.salesman.utils.LocationCoordinateUtil;
import com.salesman.utils.LocationManagerUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserConfigPreference;
import com.salesman.views.ZoomControlView;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.listview.UltimateListView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 签到地图界面
 * Created by LiHuai on 2016/1/21.
 */
public class SigninMapActivity extends BaseActivity implements View.OnClickListener, UltimateListView.OnItemClickListener {
    private final String TAG = SigninMapActivity.class.getSimpleName();
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
    private TextView tvAddress;
    private ImageView ivLocation;
    private MapView bmapView = null;
    private BaiduMap mBaiduMap = null;
    private ZoomControlView mZoomControlView;

    private MapStatusUpdate msUpdate = null;
    // 明细覆盖标注
    private BitmapDescriptor bmZhong;
    private Marker marker;
    private LocationManagerUtil locationManagerUtil;
    private MySigninMapListener mySigninMapListener;
    private LocationClient locationClient;

    // 百度地图地理编码
    private GeoCoder mSearch;
    private UltimateListView listView;
    private ChooseAddressAdapter adapter;
    private List<PoiInfo> mData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_map);

        initViews();
        initMap();
        initDatas();
        initMapEvent();
    }

    private void initViews() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.address);
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        tvBack.setOnClickListener(this);
        TextView tvTopRight = (TextView) findViewById(R.id.tv_top_right);
        tvTopRight.setVisibility(View.GONE);
        listView = (UltimateListView) findViewById(R.id.lv_nearby_address);

        String address = getIntent().getStringExtra("address");
        tvAddress = (TextView) findViewById(R.id.tv_address_location);
        tvAddress.setText(address);
        ivLocation = (ImageView) findViewById(R.id.iv_location_hand);
        ivLocation.setOnClickListener(this);

        locationManagerUtil = LocationManagerUtil.getInstance(getApplicationContext());
        mySigninMapListener = new MySigninMapListener();

        adapter = new ChooseAddressAdapter(this, mData);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void initMap() {
        // 初始化控件
        bmapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = bmapView.getMap();
        bmapView.showZoomControls(false);// 设置缩放按钮
        // 自定义缩放按钮
        mZoomControlView = (ZoomControlView) findViewById(R.id.ZoomControlView);
        mZoomControlView.setMapView(bmapView);
    }

    private void initDatas() {
        double lng = LocationCoordinateUtil.getLongitude();
        double lat = LocationCoordinateUtil.getLatitude();
        LatLng latLng = new LatLng(lat, lng);
        msUpdate = MapStatusUpdateFactory.newLatLngZoom(latLng, 16);
        bmZhong = BitmapDescriptorFactory.fromResource(R.drawable.select_coord);
        OverlayOptions options = new MarkerOptions().position(latLng).icon(bmZhong).zIndex(9).draggable(false);
        if (null != msUpdate) {
            mBaiduMap.setMapStatus(msUpdate);
        }
        refreshZoomControl();
        //将marker添加到地图上
        marker = (Marker) (mBaiduMap.addOverlay(options));

        if (AddClientStep1Activity.FLAG == getIntent().getIntExtra(AddClientStep1Activity.TAG, 0)) {
            onClick(ivLocation);
        } else {
            getNearbyAddress(latLng);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        PoiInfo poiInfo = adapter.getItem(position);
        if (null != poiInfo) {
            mUserConfig.saveLatitude(String.valueOf(poiInfo.location.latitude))
                    .saveLongitude(String.valueOf(poiInfo.location.longitude))
                    .saveLocationAddress(poiInfo.name).apply();
            EventBus.getDefault().post(EventBusConfig.SIGNIN_ADDRESS);
            Intent intent = getIntent();
            intent.putExtra("address", poiInfo.name);
            intent.putExtra("latitude", poiInfo.location.latitude);
            intent.putExtra("longitude", poiInfo.location.longitude);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public class MySigninMapListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            ivLocation.setEnabled(true);
            locationManagerUtil.unRegisterLocationListener(locationClient, mySigninMapListener);
            LogUtils.d(TAG, "=========定位");
            if (bdLocation == null) {
                ToastUtil.show(SigninMapActivity.this, "定位失败");
                return;
            } else {
                double lat = bdLocation.getLatitude();
                double lng = bdLocation.getLongitude();
                String address = LocationCoordinateUtil.getLocationAddress(bdLocation);
                LogUtils.d(TAG, address);
                if (lat == 4.9E-324 || lng == 4.9E-324 || TextUtils.isEmpty(address)) {
                    ToastUtil.show(SigninMapActivity.this, "定位失败");
                    return;
                }
                tvAddress.setText(address);
                mUserConfig.saveLatitude(String.valueOf(lat))
                        .saveLongitude(String.valueOf(lng))
                        .saveLocationAddress(address).apply();
                EventBus.getDefault().post(EventBusConfig.SIGNIN_ADDRESS);
                if (marker != null) {
                    marker.remove();
                }
                LatLng latLng = new LatLng(lat, lng);
                OverlayOptions options = new MarkerOptions().position(latLng).icon(bmZhong).zIndex(9).draggable(false);
                marker = (Marker) (mBaiduMap.addOverlay(options));

                getNearbyAddress(latLng);
            }
        }
    }

    /**
     * 获取附近位置
     *
     * @param latLng
     */
    private void getNearbyAddress(LatLng latLng) {
        showProgressDialog(getString(R.string.searching), false);
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(listener);
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }

    private OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            dismissProgressDialog();
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
                return;
            }
            //获取地理编码结果
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            dismissProgressDialog();
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                Toast.makeText(SigninMapActivity.this, "抱歉，未能找到结果", Toast.LENGTH_SHORT).show();
                return;
            }
            //获取反向地理编码结果
            List<PoiInfo> list = result.getPoiList();
            mData.clear();
            mData.addAll(list);
            adapter.setData(mData);
//            for (PoiInfo poiInfo : list) {
//                LogUtils.d(TAG, poiInfo.name);
//                LogUtils.d(TAG, poiInfo.address);
//                LatLng latLng = poiInfo.location;
//                LogUtils.d(TAG, latLng.latitude + "===" + latLng.longitude);
//            }
        }
    };

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
                LatLng ll = mapStatus.target;
                LogUtils.d(TAG, ll.latitude + "=====" + ll.longitude);
                LogUtils.d(TAG, "=====" + mapStatus.zoom);
                refreshZoomControl();
            }
        });
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
        if (null != bmapView) {
            bmapView.onDestroy();
        }
        if (null != mSearch) {
            mSearch.destroy();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.iv_location_hand:
                ivLocation.setEnabled(false);
                locationClient = locationManagerUtil.initLocation(this, 0);
                locationManagerUtil.startLocationListener(locationClient, mySigninMapListener);
                break;
        }
    }
}
