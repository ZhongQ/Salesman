package com.salesman.entity;

import java.util.List;

/**
 * 今日足迹列表实体
 * Created by LiHuai on 2016/1/28 0028.
 */
public class TodayTrackListBean extends BaseBean {
    /**
     * pageNo : 1
     * pageSize : 10
     * count : 1
     * list : [{"userName":"元宝","createTime":"2016-01-27","positionName":"深圳南山科技园科苑武汉大学城"}]
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
         * userName : 元宝
         * createTime : 2016-01-27
         * positionName : 深圳南山科技园科苑武汉大学城
         */

        public List<EmployeeTrackBean> list;
    }

    public class EmployeeTrackBean {
        public String userId;
        public String userName;
        public String createTime;
        public String positionName;
        public String timePoint;
    }
}
