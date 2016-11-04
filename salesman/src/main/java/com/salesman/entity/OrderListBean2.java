package com.salesman.entity;

import java.util.List;

/**
 * 订单实体
 * Created by LiHuai on 2016/10/12 0012.
 */

public class OrderListBean2 extends BaseBean {
    public DataBean data;

    public static class DataBean {
        /**
         * orderNo : 20161008085404150242
         * addTime : 2016-10-08 08:54:04.0
         * mobile : 13076716104
         * orderPrice : 1003
         * storeName : 伍记士多
         * isZj : 2
         * storeId : 13333
         * dayTime : 2015-10-31
         * status : 4
         * storeCode : 粤A020390
         */

        public List<OrderBean> list;

        public static class OrderBean {
            public String orderNo;
            public String addTime;
            public String mobile;
            public float orderPrice;
            public String storeName;
            public int isZj;
            public int storeId;
            public String dayTime;
            public int status;
            public String storeCode;

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
