package com.salesman.entity;

import java.util.List;

/**
 * 英雄榜实体
 * Created by LiHuai on 2016/10/13 0013.
 */

public class HeroRankListBean extends BaseBean {
    /**
     * createTime : 2016-09-10
     * myRank : {"seqNo":8,"raseNum":"-2","salemanId":"ff8080815307dd760153080881150008","salemanName":"","updateTime":"19:10","dayTime":"2016-09-10"}
     * list : [{"seqNo":1,"raseNum":"-2","salemanId":"1","salemanName":"a1","updateTime":"14:09","dayTime":"2016-09-10"},{"seqNo":2,"raseNum":"-2","salemanId":"3","salemanName":"c3","updateTime":"10:09","dayTime":"2016-09-10"},{"seqNo":3,"raseNum":"-2","salemanId":"2","salemanName":"b2","updateTime":"14:09","dayTime":"2016-09-10"},{"seqNo":4,"raseNum":"-2","salemanId":"ff80808151e6527a01521f64daf80ea2","salemanName":"","updateTime":"19:10","dayTime":"2016-09-10"},{"seqNo":5,"raseNum":"-2","salemanId":"ff8080815307dd760153080881150008","salemanName":"","updateTime":"19:10","dayTime":"2016-09-10"},{"seqNo":6,"raseNum":"-2","salemanId":"1","salemanName":"","updateTime":"19:10","dayTime":"2016-09-10"},{"seqNo":7,"raseNum":"-2","salemanId":"ff80808151e6527a01521f64daf80ea2","salemanName":"","updateTime":"19:10","dayTime":"2016-09-10"},{"seqNo":8,"raseNum":"-2","salemanId":"ff8080815307dd760153080881150008","salemanName":"","updateTime":"19:10","dayTime":"2016-09-10"},{"seqNo":9,"raseNum":"-2","salemanId":"1","salemanName":"","updateTime":"19:10","dayTime":"2016-09-10"}]
     */

    public DataBean data;

    public static class DataBean {
        public String createTime;
        /**
         * seqNo : 8
         * raseNum : -2
         * salemanId : ff8080815307dd760153080881150008
         * salemanName :
         * updateTime : 19:10
         * dayTime : 2016-09-10
         */

        public MyRankBean myRank;
        /**
         * seqNo : 1
         * raseNum : -2
         * salemanId : 1
         * salemanName : a1
         * updateTime : 14:09
         * dayTime : 2016-09-10
         */

        public List<RankBean> list;

        public static class MyRankBean {
            public int seqNo;
            public int raseNum;
            public String salemanId;
            public String salemanName;
            public String updateTime;
            public String dayTime;
        }

        public static class RankBean {
            public int seqNo;
            public int raseNum;
            public String salemanId;
            public String salemanName;
            public String updateTime;
            public String dayTime;
        }
    }
}
