package com.salesman.entity;

import java.util.List;

/**
 * 店铺坐标实体
 * Created by LiHuai on 2016/2/20 0020.
 */
public class ShopCoordinateListBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        /**
         * shopName : 苏宁易购588
         * shopAdderss : 长沙
         * longitude : 0
         * latitude : 0
         */

        public List<ShopCoordinate> list;


    }

    public static class ShopCoordinate {
        public String shopName;
        public String shopAdderss;
        public double longitude;
        public double latitude;
    }
}
