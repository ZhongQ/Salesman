package com.salesman.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.salesman.R;
import com.studio.jframework.utils.LogUtils;

/**
 * 百度地图自定义缩放按钮
 * Created by LiHuai on 2016/3/4 0004.
 */
public class ZoomControlView extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = ZoomControlView.class.getSimpleName();
    private ImageView mButtonZoomin;
    private ImageView mButtonZoomout;
    private MapView mapView;
    private float maxZoomLevel;
    private float minZoomLevel;

//    public ZoomControlView(Context context) {
//        super(context);
//    }

    public ZoomControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.zoom_controls_layout, null);
        mButtonZoomin = (ImageView) view.findViewById(R.id.zoomin);
        mButtonZoomout = (ImageView) view.findViewById(R.id.zoomout);
        mButtonZoomin.setOnClickListener(this);
        mButtonZoomout.setOnClickListener(this);
        addView(view);
    }

    @Override
    public void onClick(View v) {
        if (mapView == null) {
            throw new NullPointerException("you can call setMapView(MapView mapView) at first");
        }
        switch (v.getId()) {
            case R.id.zoomin: {
                MapStatusUpdate msu = MapStatusUpdateFactory.zoomIn();
                mapView.getMap().setMapStatus(msu);
                refreshZoomButtonStatus(mapView.getMap().getMapStatus().zoom);
                break;
            }
            case R.id.zoomout: {
                MapStatusUpdate msu = MapStatusUpdateFactory.zoomOut();
                mapView.getMap().setMapStatus(msu);
                refreshZoomButtonStatus(mapView.getMap().getMapStatus().zoom);
                break;
            }
        }
    }

    /**
     * 与MapView设置关联
     *
     * @param mapView
     */
    public void setMapView(MapView mapView) {
        this.mapView = mapView;
        // 获取最大的缩放级别
        maxZoomLevel = mapView.getMap().getMaxZoomLevel();
        // 获取最大的缩放级别
        minZoomLevel = mapView.getMap().getMinZoomLevel();
        LogUtils.d(TAG, "maxZoom===" + maxZoomLevel + "minZoom===" + minZoomLevel);
    }

    /**
     * 根据MapView的缩放级别更新缩放按钮的状态，当达到最大缩放级别，设置mButtonZoomin
     * 为不能点击，反之设置mButtonZoomout
     *
     * @param level
     */
    public void refreshZoomButtonStatus(float level) {
        if (mapView == null) {
            throw new NullPointerException("you can call setMapView(MapView mapView) at first");
        }
        LogUtils.d(TAG, "level===" + level);
        if (level > minZoomLevel && level < maxZoomLevel) {
            if (!mButtonZoomout.isEnabled()) {
                mButtonZoomout.setEnabled(true);
            }
            if (!mButtonZoomin.isEnabled()) {
                mButtonZoomin.setEnabled(true);
            }
        } else if (level == minZoomLevel) {
            mButtonZoomout.setEnabled(false);
        } else if (level == maxZoomLevel) {
            mButtonZoomin.setEnabled(false);
        }
    }
}
