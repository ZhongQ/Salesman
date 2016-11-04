package com.salesman.entity;

import java.util.List;

/**
 * 业绩实体
 * Created by LiHuai on 2016/10/12 0012.
 */

public class YeJiListBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        /**
         * zjturnover : 1500
         * kpiTurnover : 1100
         * isEffect : 1
         * orderCount : 10
         * updateTime : 1473487902000
         * regStore : 8
         * raseNumDay : -1
         * dayTime : 2016-09-10
         * createTime : 1473350400
         * salemanId : 1
         * activeStore : 6
         * salemanName : a1
         * id : 2
         * zjKpiTurnover : 2
         * raseNumMonth : 1
         * turnover : 2000
         */

        public List<YeJiBean> list;

        public static class YeJiBean {
            public float zjturnover;
            public float kpiTurnover;
            public int isEffect;
            public int orderCount;
            public int regStore;
            public int raseNumDay;
            public String dayTime;
            public String salemanId;
            public int activeStore;
            public String salemanName;
            public int id;
            public float zjKpiTurnover;
            public int raseNumMonth;
            public float turnover;

            public int imgId;

            public int getImgId() {
                return imgId;
            }

            public void setImgId(int imgId) {
                this.imgId = imgId;
            }
        }
    }
}
