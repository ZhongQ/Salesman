package com.salesman.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 客户列表
 * Created by LiHuai on 2016/1/28 0028.
 */
public class ClientListBean extends BaseBean {

    /**
     * pageNo : 1
     * pageSize : 10
     * count : 2
     * list : [{"userId":"ff80808151e6527a01521f64daf80ea7","userName":"元宝","shopId":"fd621e426cae41a4b91313e5ba350a24","shopName":"苏宁易购","shopAdderss":"长沙"},{"userId":"ff80808151e6527a01521f64daf80ea7","userName":"元宝","shopId":"5628e8d71f074a6aabd4a74b27feb2df","shopName":"转角街坊","shopAdderss":"长沙"}]
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
         * userId : ff80808151e6527a01521f64daf80ea7
         * userName : 元宝
         * shopId : fd621e426cae41a4b91313e5ba350a24
         * shopName : 苏宁易购
         * shopAdderss : 长沙
         */

        public List<ShopBean> list;
    }

    public static class ShopBean implements Serializable {
        public String shopId;
        public String shopNo;
        public String shopName;
        public String shopAddress;
        public String status;
        public String lineId;
        public String lineName;
        public String optType;

        public String shopType;
        public String isRegister;
        public double longitude;
        public double latitude;

        public String salesmanId;
        public String salesmanName;
        public String proprietor;

        public String distance;// V2.1.0

        public int imgId;// 图标背景

        public int getImgId() {
            return imgId;
        }

        public void setImgId(int imgId) {
            this.imgId = imgId;
        }
    }
}
