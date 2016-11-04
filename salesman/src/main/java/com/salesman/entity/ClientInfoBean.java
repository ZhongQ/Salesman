package com.salesman.entity;

import java.util.List;

/**
 * 客户基础信息实体
 * Created by LiHuai on 2016/10/10 0010.
 */

public class ClientInfoBean extends BaseBean {
    /**
     * area : 凤岗镇
     * registerTel : 13790318335
     * salesmanName : 静静
     * salesmanId : ff8080815307dd760153080881150008
     * city : 东莞
     * bossName : 黄生
     * spGroupName : 东莞（东莞定格）组
     * shopName : 好佳便利
     * vipType : 0
     * lineId :
     * lineName :
     * spGroupId : 166
     * shopAddress : 林村新阳路中112号
     * list : [{"orderNo":"20160924151109150283","mobile":"13790318335","orderPrice":591,"storeName":"好佳便利","dayTime":"20160924","status":"已打印"}]
     * province : 广东省
     * bossTel : 13790318335
     * shopId : 0745e6d8f8cf444f827d773d404e4dcb
     * shopNo : 粤S190108
     * remarks :
     */

    public ClientInfo data;

    public static class ClientInfo {
        public String area;
        public String registerTel;
        public String salesmanName;
        public String salesmanId;
        public String city;
        public String bossName;
        public String spGroupName;
        public String shopName;
        public String vipType;
        public String lineId;
        public String lineName;
        public String spGroupId;
        public String shopAddress;
        public String province;
        public String bossTel;
        public String shopId;
        public String shopNo;
        public String remarks;
        public int storeId;
        /**
         * orderNo : 20160924151109150283
         * mobile : 13790318335
         * orderPrice : 591
         * storeName : 好佳便利
         * dayTime : 20160924
         * status : 已打印
         */

        public List<Order> list;

        public static class Order {
            public String orderNo;
            public String mobile;
            public float orderPrice;
            public String storeName;
            public String dayTime;
            public String addTime;
            public int status;
            public int isZj;
        }
    }
}
