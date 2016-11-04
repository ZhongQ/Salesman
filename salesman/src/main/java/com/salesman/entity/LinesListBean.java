package com.salesman.entity;

import java.util.List;

/**
 * 业务员线路实体
 * Created by LiHuai on 2016/8/9 0009.
 */
public class LinesListBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        /**
         * shopTotal : 0
         * lineId : 1
         * lineName : 线路1
         */
        public List<LineBean> list;
    }

    public static class LineBean {
        public int shopTotal;
        public String lineId;
        public String lineName;
    }
}
