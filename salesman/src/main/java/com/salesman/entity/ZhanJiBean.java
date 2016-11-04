package com.salesman.entity;

import java.util.List;

/**
 * 战绩首页实体
 * Created by LiHuai on 2016/10/11 0011.
 */

public class ZhanJiBean extends BaseBean {
    /**
     * zjturnover : 2300
     * newRegStore : 14
     * kpiTurnover : 1700
     * raseTurnover : 0
     * activeStore : 0
     * raseZjturnover : 0
     * orderCount : 20
     * list : [{"salemanId":"1","salemanName":"a1","raseNumDay":2,"raseNumMonth":-3,"dayTime":20160909},{"salemanId":"1","salemanName":"a1","raseNumDay":-1,"raseNumMonth":1,"dayTime":20160910},{"salemanId":"1","salemanName":"b2","raseNumDay":-3,"raseNumMonth":-2,"dayTime":20160910}]
     * turnover : 3000
     */

    public DataBean data;

    public static class DataBean {
        public int zjturnover;
        public int regStore;
        public int kpiTurnover;
        public int raseTurnover;
        public int activeStore;
        public int raseZjturnover;
        public int orderCount;
        public int turnover;
        /**
         * salemanId : 1
         * salemanName : a1
         * raseNumDay : 2
         * raseNumMonth : -3
         * dayTime : 20160909
         */

        public List<RankingBean> list;

        public static class RankingBean {
            /**
             * seqNo : 1
             * raseNum : 4
             * salemanId : 8aad22685365860d0153659e659a0007
             * salemanName : 铁战
             * updateTime : 16:03
             * turnover : 3136.5
             * dayTime : 2016-10-18
             */

            public int seqNo;
            public int raseNum;
            public String salemanId;
            public String salemanName;
            public String updateTime;
            public float turnover;
            public String dayTime;
        }
    }
}
