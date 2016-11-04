package com.salesman.entity;

import java.util.List;

/**
 * 业务员线路实体
 * Created by LiHuai on 2016/8/9 0009.
 */
public class SalesmanLineListBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        /**
         * lineList : {"lineId":"ALL","lineName":"全部线路"}
         * userName : 全部
         * userId : ALL
         */

        public List<SalesBean> list;
    }

    public static class SalesBean {
        /**
         * lineId : ALL
         * lineName : 全部线路
         */
        public List<LineBean> lineList;
        public String userName;
        public String userId;
    }

    public static class LineBean {
        public String lineId;
        public String lineName;
    }
}
