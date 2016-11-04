package com.salesman.entity;

import java.util.List;

/**
 * 足迹坐标点
 * Created by LiHuai on 2016/2/26 0026.
 */
public class TrackCoordListBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        /**
         * longitude : 113.951347
         * latitude : 22.535719
         * localtimes : 1456467406
         */

        public List<TrackCoord> list;

    }

    public static class TrackCoord {
        public double longitude;
        public double latitude;
        public String localtimes;
    }
}
