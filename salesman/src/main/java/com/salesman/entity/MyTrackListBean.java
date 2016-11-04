package com.salesman.entity;

import java.util.List;

/**
 * 我的足迹列表实体
 * Created by LiHuai on 2016/1/28 0028.
 */
public class MyTrackListBean extends BaseBean {

    /**
     * pageNo : 1
     * pageSize : 10
     * count : 1
     * list : [{"createTime":"2016-01-27","week":"星期三"}]
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
         * createTime : 2016-01-27
         * week : 星期三
         */

        public List<TrackTimeBean> list;
    }

    public class TrackTimeBean {
        public String createTime;
        public String week;
    }
}
