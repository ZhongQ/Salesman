package com.salesman.entity;

import java.util.List;

/**
 * 足迹明细列表实体
 * Created by LiHuai on 2016/1/28 0028.
 */
public class TrackDetailListBean extends BaseBean {

    /**
     * pageNo : 1
     * pageSize : 10
     * count : 4
     * list : [{"timePoint":"13:09","longitude":22.2123234,"latitude":333.23345,"type":0,"positionName":"深圳南山科技园科苑武汉大学城"},{"timePoint":"13:42","longitude":21.22,"latitude":38.98733,"type":0,"positionName":"深圳南山科技园科苑武汉大学城"},{"timePoint":"13:59","longitude":38.98733,"latitude":333,"type":0,"positionName":"深圳南山科技园科苑武汉大学城"},{"timePoint":"16:02","longitude":222.65,"latitude":38.98733,"type":0,"positionName":"深圳南山科技园科苑武汉大学城"}]
     * firstResult : 0
     * maxResults : 10
     */

    public DataBean data;

    public static class DataBean {
        public int pageNo;
        public int pageSize;
        public int count;
        public int firstResult;
        public int maxResults;
        /**
         * timePoint : 13:09
         * longitude : 22.2123234
         * latitude : 333.23345
         * type : 0
         * positionName : 深圳南山科技园科苑武汉大学城
         */

        public List<TrackDetailBean> list;
    }

    public static class TrackDetailBean {
        public String timePoint;
        public double longitude;
        public double latitude;
        public int type;
        public String positionName;
    }
}
