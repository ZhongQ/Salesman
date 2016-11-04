package com.salesman.entity;

import java.util.List;

/**
 * 客户审核详情实体
 * Created by LiHuai on 2016/6/23 0023.
 */
public class ClientCheckDetailBean extends BaseBean{
    /**
     * id : 236
     * name : 丽莎小卖铺
     * contact : lisa
     * mobile : 15209823113
     * areaStr : 广东省深圳龙岗区
     * address : 市政府
     * licenseNum : null
     * idCardUpPic : group1/M00/00/0D/oYYBAFVl13-AItkIAAAa5ngPH7U990.jpg
     * idCardDownPic : group1/M00/00/0D/oYYBAFVl15GAWMe4AAAax9y6glM913.jpg
     * licensePic : group1/M00/00/0D/oYYBAFVl19GAXm8vAAAhM4pXSQs613.jpg
     * spGroupList : [{"id":21,"name":"龙岗区(龙岗佰丽批发部)组","code":"DA9EFF159D4046FD8FFF3AE5D39ABC7B","provinceId":6,"cityId":77,"areaId":709,"status":1,"isDelete":false},{"id":22,"name":"龙岗区(永胜批发部)组","code":"12C09D5B5FFB40938A3D1C1D35497ADE","provinceId":6,"cityId":77,"areaId":709,"status":1,"isDelete":false},{"id":25,"name":"龙岗区(龙西振兴批发部)组","code":"FC0157C6B9244E13B0460505B6095611","provinceId":6,"cityId":77,"areaId":709,"status":1,"isDelete":false},{"id":30,"name":"龙岗区(聚鑫批发部)组","code":"5","provinceId":6,"cityId":77,"areaId":709,"status":1,"isDelete":false},{"id":31,"name":"龙岗区(加顺发批发部)组","code":"7","provinceId":6,"cityId":77,"areaId":709,"status":1,"isDelete":false},{"id":35,"name":"龙岗区(龙岗合盛批发部)组","code":"475FF7ED16F14CC18B1ECF07FABF2C1C","provinceId":6,"cityId":77,"areaId":709,"status":1,"isDelete":false},{"id":53,"name":"龙岗区(龙岗顺龙达批发商行)组","code":null,"provinceId":6,"cityId":77,"areaId":709,"status":1,"isDelete":false},{"id":57,"name":"龙岗(顺意达批发部)组","code":null,"provinceId":6,"cityId":77,"areaId":709,"status":1,"isDelete":false},{"id":62,"name":"龙岗区（隆辉伟达批发部）组","code":null,"provinceId":6,"cityId":77,"areaId":709,"status":1,"isDelete":false}]
     */

    public CheckDetailBean data;

    public static class CheckDetailBean {
        public int id;
        public String name;
        public String contact;
        public String mobile;
        public String areaStr;
        public String address;
        public String licenseNum;
        public String idCardUpPic;
        public String idCardDownPic;
        public String licensePic;
        /**
         * id : 21
         * name : 龙岗区(龙岗佰丽批发部)组
         * code : DA9EFF159D4046FD8FFF3AE5D39ABC7B
         * provinceId : 6
         * cityId : 77
         * areaId : 709
         * status : 1
         * isDelete : false
         */

        public List<DingGeBean> spGroupList;

    }

    public static class DingGeBean {
        public int id;
        public String name;
        public String code;
        public int provinceId;
        public int cityId;
        public int areaId;
        public int status;
        public boolean isDelete;
    }
}
