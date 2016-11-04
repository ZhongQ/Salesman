package com.salesman.entity;

import java.util.List;

/**
 * 外勤线路和客户实体
 * Created by LiHuai on 2016/8/10 0010.
 */
public class OutLineClientListBean extends BaseBean {

    public DataBean data;

    public static class DataBean {
        /**
         * shopList : []
         * lineId : aabb95ccec5b4e7f9c3a9cd24f9545fe
         * lineName : 1001
         */

        public List<LineBean> list;
    }

    public static class LineBean {
        public String lineId;
        public String lineName;
        public List<ShopBean> shopList;
    }

    public static class ShopBean {
        public String shopNo;
        public String shopName;
    }
}
