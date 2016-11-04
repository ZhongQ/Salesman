package com.salesman.entity;

import java.util.List;

/**
 * 订单明细实体
 * Created by LiHuai on 2016/7/8 0008.
 */
public class OrderListBean extends BaseBean {

    /**
     * pageNo : 1
     * isUnion : 1
     * pageSize : 10
     * spGroupId : 50
     * list : [{"orderNo":"201601291621500078","mobile":"13076939019","orderPrice":1034,"dayTime":"2016-01-29 16:21:50.0","status":"已送达"},{"orderNo":"201601291620000166","mobile":"13699860178","orderPrice":1032,"dayTime":"2016-01-29 16:20:00.0","status":"已送达"},{"orderNo":"201601291618320066","mobile":"13316835069","orderPrice":1062,"dayTime":"2016-01-29 16:18:32.0","status":"已送达"},{"orderNo":"201601291617040058","mobile":"13692213577","orderPrice":1043.5,"dayTime":"2016-01-29 16:17:04.0","status":"已送达"},{"orderNo":"201601291615330120","mobile":"13530890276","orderPrice":1059,"dayTime":"2016-01-29 16:15:33.0","status":"已送达"},{"orderNo":"201601291612110796","mobile":"13691931617","orderPrice":1484,"dayTime":"2016-01-29 16:11:47.0","status":"已送达"},{"orderNo":"201601291446440795","mobile":"13670020887","orderPrice":1050.5,"dayTime":"2016-01-29 14:46:45.0","status":"已送达"},{"orderNo":"201601291446330078","mobile":"13662406416","orderPrice":1085,"dayTime":"2016-01-29 14:46:33.0","status":"已送达"},{"orderNo":"201601291444430904","mobile":"13530858292","orderPrice":1060,"dayTime":"2016-01-29 14:44:44.0","status":"已送达"},{"orderNo":"201601291442140391","mobile":"13923800761","orderPrice":1070,"dayTime":"2016-01-29 14:42:14.0","status":"已送达"}]
     */

    public DataBean data;

    public static class DataBean {
        public int pageNo;
        public String isUnion;
        public int pageSize;
        public String spGroupId;
        /**
         * orderNo : 201601291621500078
         * mobile : 13076939019
         * orderPrice : 1034
         * dayTime : 2016-01-29 16:21:50.0
         * status : 已送达
         */

        public List<OrderBean> list;

    }

    public static class OrderBean {
        public String orderNo;
        public String mobile;
        public float orderPrice;
        public String dayTime;
        public String status;
        public String storeName;

        public int imgId;

        public int getImgId() {
            return imgId;
        }

        public void setImgId(int imgId) {
            this.imgId = imgId;
        }
    }
}
