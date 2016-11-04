package com.salesman.entity;

import java.util.List;

/**
 * 趋势实体
 * Created by LiHuai on 2016/10/14 0014.
 */

public class TrendBean extends BaseBean {
    /**
     * zt_regStore : 0
     * zt_turnover : 0
     * jt_regStore : 0
     * by_zjturnover : 0
     * sy_zjturnover : 0
     * jt_activeStore : 352
     * sy_activeStore : 0
     * jt_turnover : 0
     * list : [{"zjturnover":0,"turnover":0,"dayTime":"2016-08-11"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-12"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-13"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-14"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-15"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-16"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-17"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-18"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-19"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-20"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-21"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-22"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-23"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-24"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-25"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-26"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-27"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-28"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-29"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-30"},{"zjturnover":0,"turnover":0,"dayTime":"2016-08-31"},{"zjturnover":0,"turnover":0,"dayTime":"2016-09-01"},{"zjturnover":0,"turnover":0,"dayTime":"2016-09-02"},{"zjturnover":0,"turnover":0,"dayTime":"2016-09-03"},{"zjturnover":0,"turnover":0,"dayTime":"2016-09-04"},{"zjturnover":0,"turnover":0,"dayTime":"2016-09-05"},{"zjturnover":0,"turnover":0,"dayTime":"2016-09-06"},{"zjturnover":0,"turnover":0,"dayTime":"2016-09-07"},{"zjturnover":0,"turnover":0,"dayTime":"2016-09-08"},{"zjturnover":0,"turnover":0,"dayTime":"2016-09-09"}]
     * by_turnover : 0
     * sy_turnover : 0
     * jt_zjturnover : 0
     * zt_zjturnover : 0
     * sy_regStore : 0
     * by_regStore : 0
     * zt_activeStore : 0
     * by_activeStore : 3795
     */

    public DataBean data;

    public static class DataBean {
        public int zt_regStore;
        public int zt_turnover;
        public int jt_regStore;
        public int by_zjturnover;
        public int sy_zjturnover;
        public int jt_activeStore;
        public int sy_activeStore;
        public int jt_turnover;
        public int by_turnover;
        public int sy_turnover;
        public int jt_zjturnover;
        public int zt_zjturnover;
        public int sy_regStore;
        public int by_regStore;
        public int zt_activeStore;
        public int by_activeStore;
        /**
         * zjturnover : 0
         * turnover : 0
         * dayTime : 2016-08-11
         */

        public List<PointBean> list;

        public static class PointBean {
            public float zjturnover;
            public float turnover;
            public String dayTime;
        }
    }
}
