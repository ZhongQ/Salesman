package com.salesman.entity;

import java.util.List;

/**
 * 战绩筛选条件实体
 * Created by LiHuai on 2016/7/8 0008.
 */
public class ZhanJiFilterListBean extends BaseBean {
    /**
     * deptName : 技术部
     * areaId : 707
     * level : 1
     * deptId : YFB001
     * pid : C_1
     * cityId : 77
     * provinceId : 6
     */

    public List<ZhanJiFilterBean> list;

    public static class ZhanJiFilterBean {
        public String deptName;
        public String areaId;
        public String level;
        public String deptId;
        public String pid;
        public String spGroupId;// 1.5.0
        public String salesmanId;// V1.6.0
        public String cityId;
        public String provinceId;
        public String userType;// V2.0.0
    }
}
