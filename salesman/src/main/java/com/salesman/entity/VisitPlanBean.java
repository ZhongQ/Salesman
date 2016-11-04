package com.salesman.entity;

import java.util.List;

/**
 * 拜访计划实体
 * Created by LiHuai on 2016/10/12 0012.
 */

public class VisitPlanBean extends BaseBean {
    /**
     * shopTotal : 0
     * salesmanId : ff8080815307dd760153080881150008
     * period : 10.10--10.16
     * week : 星期三
     * lineName : 暂无安排
     * list : [{"shopTotal":2,"week":"星期一","lineId":"4e302b1ef4584860a2febf8df0821972","lineName":"九一八","visitDate":"2016-10-10","visitTotal":0,"status":"0"},{"shopTotal":0,"week":"星期二","lineId":"","lineName":"","visitDate":"","visitTotal":0,"status":"4"},{"shopTotal":0,"week":"星期三","lineId":"","lineName":"","visitDate":"","visitTotal":0,"status":"4"},{"shopTotal":0,"week":"星期四","lineId":"","lineName":"","visitDate":"","visitTotal":0,"status":"4"},{"shopTotal":0,"week":"星期五","lineId":"","lineName":"","visitDate":"","visitTotal":0,"status":"4"},{"shopTotal":0,"week":"星期六","lineId":"","lineName":"","visitDate":"","visitTotal":0,"status":"4"},{"shopTotal":0,"week":"星期日","lineId":"","lineName":"","visitDate":"","visitTotal":0,"status":"4"}]
     * visitTotal : 0
     */

    public DataBean data;

    public static class DataBean {
        public int shopTotal;
        public String salesmanId;
        public String period;
        public String week;
        public String lineName;
        public int visitTotal;
        /**
         * shopTotal : 2
         * week : 星期一
         * lineId : 4e302b1ef4584860a2febf8df0821972
         * lineName : 九一八
         * visitDate : 2016-10-10
         * visitTotal : 0
         * status : 0
         */

        public List<PlanBean> list;

        public static class PlanBean {
            public int shopTotal;
            public String week;
            public String lineId;
            public String lineName;
            public String visitDate;
            public int visitTotal;
            public String status;
        }
    }
}
