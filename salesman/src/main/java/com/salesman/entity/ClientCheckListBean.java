package com.salesman.entity;

import com.salesman.R;

import java.util.List;

/**
 * 客户列表实体
 * Created by LiHuai on 2016/6/23 0023.
 */
public class ClientCheckListBean extends BaseBean{
    /**
     * list : [{"id":15191,"name":"我在","provinceId":6,"cityId":77,"areaId":709,"address":"我在"},{"id":15190,"name":"jdjsjejf","provinceId":6,"cityId":77,"areaId":709,"address":"jsjdjfjjsdf"},{"id":15189,"name":"龙五","provinceId":6,"cityId":77,"areaId":709,"address":"龙五"},{"id":15188,"name":"名称","provinceId":6,"cityId":77,"areaId":709,"address":"地址"},{"id":15186,"name":"图太虚","provinceId":6,"cityId":77,"areaId":709,"address":"突突"},{"id":15185,"name":"葡萄酒了","provinceId":6,"cityId":77,"areaId":709,"address":"健健康康"},{"id":305,"name":"深V大杀四方","provinceId":6,"cityId":77,"areaId":709,"address":"洒出水电费"},{"id":243,"name":"咖啡屋","provinceId":6,"cityId":77,"areaId":709,"address":null},{"id":244,"name":null,"provinceId":6,"cityId":77,"areaId":709,"address":null}]
     * pageNo : 1
     * pageSize : 10
     * count : 9
     */

    public DataBean data;

    public static class DataBean {
        public int pageNo;
        public int pageSize;
        public int count;
        /**
         * id : 15191
         * name : 我在
         * provinceId : 6
         * cityId : 77
         * areaId : 709
         * address : 我在
         */

        public List<ClientCheckBean> list;

    }

    public static class ClientCheckBean {
        public int id;
        public String name;
        public int provinceId;
        public int cityId;
        public int areaId;
        public String address;
        public String storeName;
        public int storeId;

        public int imgId = R.drawable.circle_1;// 图标背景

        public int getImgId() {
            return imgId;
        }

        public void setImgId(int imgId) {
            this.imgId = imgId;
        }
    }
}
