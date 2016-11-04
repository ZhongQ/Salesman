package com.salesman.entity;

import java.util.List;

/**
 * 个人拜访计划实体
 * Created by LiHuai on 2016/08/06 0027.
 */
public class VisitListBean extends BaseBean {
    public DataBase data;

    public class DataBase {
        public int pageNo;
        public int pageSize;
        public int count;
        public int firstResult;
        public int maxResults;
        public List<VisitPlanBean> list;
    }

    public class VisitPlanBean {
        /**
         * shopTotal : 2
         * salesmanName : 元宝
         * week : 星期四
         * createTime : 1470883250000
         * lineId : abb7f14551d344a28e67bba1e7059f1c
         * lineName : 2001
         * visitTotal : 1
         * status : 1
         */

        public int shopTotal;
        public String salesmanName;
        public String salesmanId;
        public String week;
        public String createTime;
        public String lineId;
        public String lineName;
        public int visitTotal;
        public String status;

        public int headColor;

        public int getHeadColor() {
            return headColor;
        }

        public void setHeadColor(int headColor) {
            this.headColor = headColor;
        }
    }
}
