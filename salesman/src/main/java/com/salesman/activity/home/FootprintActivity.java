package com.salesman.activity.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.salesman.R;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.ShopCoordinateListBean;
import com.salesman.entity.TrackCoordListBean;
import com.salesman.network.BaseHelper;
import com.salesman.utils.DateUtils;
import com.salesman.utils.LocationCoordinateUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserInfoPreference;
import com.salesman.views.ZoomControlView;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 足迹界面
 * Created by LiHuai on 2016/1/25.
 */
public class FootprintActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = FootprintActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private Context mContext;
    private TextView ivBack;
    private TextView tvTopRight;
    private ZoomControlView mZoomControlView;
    private String userId = "";
    private String createTime = "";

    private MapView bmapView = null;
    private BaiduMap mBaiduMap = null;

    // 起点图标
    private BitmapDescriptor bmStart;
    // 终点图标
    private BitmapDescriptor bmEnd;
    // 起点图标覆盖物
    private MarkerOptions startMarker = null;
    // 终点图标覆盖物
    private MarkerOptions endMarker = null;
    // 路线覆盖物
    private PolylineOptions polyline = null;
    private MapStatusUpdate msUpdate = null;
    // 明细覆盖标注
    private Marker marker;
    // 店铺坐标
    private List<MarkerOptions> shopOptionsList = new ArrayList<>();
    // 是否Destroy
    private boolean isDestroy = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprint);
        mContext = getApplicationContext();

        initComponent();
        initView();
        initMapEvent();
        getAllCoordinateData();
        getShopCoordinate();
    }

    @Override
    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.footprint);
        ivBack = (TextView) findViewById(R.id.tv_top_left);
        ivBack.setOnClickListener(this);
        tvTopRight = (TextView) findViewById(R.id.tv_top_right);
        tvTopRight.setVisibility(View.VISIBLE);
        tvTopRight.setText(R.string.mingxi);
        tvTopRight.setOnClickListener(this);

        userId = getIntent().getStringExtra("userId");
        createTime = getIntent().getStringExtra("createTime");
    }

    /**
     * 初始化组件
     */
    private void initComponent() {
        // 初始化地图控件
        bmapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = bmapView.getMap();
        bmapView.showZoomControls(false);// 设置缩放按钮
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//普通地图
        // 自定义缩放按钮
        mZoomControlView = (ZoomControlView) findViewById(R.id.ZoomControlView);
        mZoomControlView.setMapView(bmapView);
        mBaiduMap.clear();
    }

    /**
     * 添加覆盖物
     */
    protected void addMarker() {
        if (null != mBaiduMap && null != msUpdate) {
            mBaiduMap.setMapStatus(msUpdate);
        }

        if (null != mBaiduMap && null != startMarker) {
            mBaiduMap.addOverlay(startMarker);
        }

        if (null != mBaiduMap && null != endMarker) {
            mBaiduMap.addOverlay(endMarker);
        }

        if (null != mBaiduMap && null != polyline) {
            mBaiduMap.addOverlay(polyline);
        }
    }

    /**
     * 重置覆盖物
     */
    private void resetMarker() {
        startMarker = null;
        endMarker = null;
        polyline = null;
    }

    /**
     * 获取当天所有足迹坐标点
     */
    private void getAllCoordinateData() {
        String url = Constant.moduleAllTrack;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        if (!TextUtils.isEmpty(userId)) {
            map.put("userId", userId);
        }
        if (TextUtils.isEmpty(createTime)) {
            map.put("createTime", DateUtils.getCurrentDate());
        } else {
            map.put("createTime", createTime);
        }
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                TrackCoordListBean trackCoordListBean = GsonUtils.json2Bean(response, TrackCoordListBean.class);
                if (null != trackCoordListBean) {
                    if (trackCoordListBean.success && null != trackCoordListBean.data) {
                        trackCoordChange(trackCoordListBean.data.list);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleyController.getInstance(this).addToQueue(post, TAG);
    }

    /**
     * 获取店铺坐标
     */
    private void getShopCoordinate() {
        String url = Constant.moduleShopSiteList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        if (!TextUtils.isEmpty(userId)) {
            map.put("userId", userId);
        }
        map.put("deptId", mUserInfo.getDeptId());
        map.put("userType", mUserInfo.getUserType());
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                ShopCoordinateListBean coordinateListBean = GsonUtils.json2Bean(response, ShopCoordinateListBean.class);
                if (null != coordinateListBean) {
                    if (coordinateListBean.success) {
                        if (null != coordinateListBean.data) {
                            shopCoordinateChange(coordinateListBean.data.list);
                        }
                    } else {
                        ToastUtil.show(FootprintActivity.this, coordinateListBean.msg);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleyController.getInstance(this).addToQueue(post, TAG);
    }

    /**
     * 足迹坐标点转换
     */
    private void trackCoordChange(List<TrackCoordListBean.TrackCoord> trackCoordList) {
        List<LatLng> latLngList = new ArrayList<>();
        latLngList.clear();
        for (TrackCoordListBean.TrackCoord trackCoord : trackCoordList) {
            if (0d != trackCoord.latitude && 0d != trackCoord.longitude) {
                LatLng latLng = new LatLng(trackCoord.latitude, trackCoord.longitude);
                latLngList.add(latLng);
            }
        }
        showTrack(latLngList);
    }

    /**
     * 店铺坐标转换
     */
    private void shopCoordinateChange(List<ShopCoordinateListBean.ShopCoordinate> coordinateList) {
        if (null != coordinateList) {
            shopOptionsList.clear();
            BitmapDescriptor bmShopCoord = BitmapDescriptorFactory.fromResource(R.drawable.shop_coord);
            for (ShopCoordinateListBean.ShopCoordinate bean : coordinateList) {
                if (bean.latitude != 0d && bean.longitude != 0d) {
                    LatLng latlng = new LatLng(bean.latitude, bean.longitude);
                    MarkerOptions marker = new MarkerOptions().position(latlng).icon(bmShopCoord).zIndex(9).draggable(false);
                    shopOptionsList.add(marker);
                }
            }
            // 添加轨迹中的图形标注(李坏)
            if (null != shopOptionsList) {
                for (MarkerOptions shopOptions : shopOptionsList) {
                    if (null != shopOptions) {
                        if (mBaiduMap != null) {
                            mBaiduMap.addOverlay(shopOptions);
                        }
                    }
                }
            }
        }
    }

    /**
     * 展示足迹
     */
    private void showTrack(List<LatLng> points) {
        if (isDestroy) {
            return;
        }
        if (points == null || points.size() == 0) {
//            Toast.makeText(FootprintActivity.this, "当前查询无轨迹点", Toast.LENGTH_SHORT).show();
            resetMarker();
            LatLng defaultLatLng = new LatLng(LocationCoordinateUtil.getLatitude(), LocationCoordinateUtil.getLongitude());
            msUpdate = MapStatusUpdateFactory.newLatLngZoom(defaultLatLng, 16);
            if (null != mBaiduMap && null != msUpdate) {
                try {
                    mBaiduMap.setMapStatus(msUpdate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (points.size() == 1) {
            LatLng llCentre = points.get(0);
            msUpdate = MapStatusUpdateFactory.newLatLngZoom(llCentre, 16);
            bmStart = BitmapDescriptorFactory.fromResource(R.drawable.track_start);
            OverlayOptions options = new MarkerOptions().position(llCentre).icon(bmStart).zIndex(9).draggable(false);
            if (null != mBaiduMap && null != msUpdate) {
                mBaiduMap.addOverlay(options);
                try {
                    mBaiduMap.setMapStatus(msUpdate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (points.size() > 1) {
            LatLng llC = points.get(0);
            LatLng llD = points.get(points.size() - 1);
//            LatLngBounds bounds = new LatLngBounds.Builder().include(llC).include(llD).build();
//            msUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);
            LatLng llCentre = points.get(points.size() / 2);
            msUpdate = MapStatusUpdateFactory.newLatLngZoom(llCentre, 16);
            bmStart = BitmapDescriptorFactory.fromResource(R.drawable.track_start);
            bmEnd = BitmapDescriptorFactory.fromResource(R.drawable.track_end);
            // 添加起点图标
            startMarker = new MarkerOptions().position(points.get(points.size() - 1)).icon(bmStart).zIndex(9).draggable(false);
            // 添加终点图标
            endMarker = new MarkerOptions().position(points.get(0)).icon(bmEnd).zIndex(9).draggable(false);
            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10).color(Color.RED).points(points);

            addMarker();
        }
        refreshZoomControl();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (null != marker) {
                marker.remove();
            }
            double longitude = data.getDoubleExtra("longitude", 0);
            double latitude = data.getDoubleExtra("latitude", 0);
            if (longitude != 0 && latitude != 0) {
                LatLng latLng = new LatLng(latitude, longitude);
                BitmapDescriptor bmZhong = BitmapDescriptorFactory.fromResource(R.drawable.select_coord);
                OverlayOptions options = new MarkerOptions().position(latLng).icon(bmZhong).zIndex(9).draggable(false).title("地址");
                //将marker添加到地图上
                marker = (Marker) (mBaiduMap.addOverlay(options));
                msUpdate = MapStatusUpdateFactory.newLatLngZoom(latLng, 16);
                if (null != mBaiduMap && null != msUpdate) {
                    try {
                        mBaiduMap.setMapStatus(msUpdate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
//                mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//                    @Override
//                    public boolean onMarkerClick(Marker marker) {
//                        Button button = new Button(getApplicationContext());
//                        button.setText(marker.getTitle());
//                        button.setBackgroundResource(R.drawable.applogo);
//                        LatLng pt = marker.getPosition();
//                        InfoWindow infoWindow = new InfoWindow(button, pt, -100);
//                        mBaiduMap.showInfoWindow(infoWindow);
//                        return false;
//                    }
//                });
            }
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
        isDestroy = true;
        VolleyController.getInstance(this).cancelPendingRequests(TAG);
        bmapView.onDestroy();
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
                LatLng ll = mapStatus.target;
                LogUtils.d(TAG, ll.latitude + "=====" + ll.longitude);
                LogUtils.d(TAG, "=====" + mapStatus.zoom);
                refreshZoomControl();
            }
        });
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
            case R.id.tv_top_right:
                Intent intent = new Intent(this, FootprintListActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("createTime", createTime);
                startActivityForResult(intent, 1);
                break;
        }
    }
}
