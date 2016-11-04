package com.salesman.entity;

import java.util.List;

/**
 * 定格和线路实体
 * Created by LiHuai on 2016/6/27 0027.
 */
public class DingGeListBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        /**
         * spGroupId : ALL
         * spGroupName : 全部
         * lineList : []
         */

        public List<DingGeBean> spgList;

    }

    public static class DingGeBean {
        public String spGroupId;
        public String spGroupName;
        public List<LineBean> lineList;
    }

    public static class LineBean {
        public String lineName;
        public String lineId;
    }
}
