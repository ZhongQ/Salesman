package com.salesman.entity;

import com.salesman.utils.ReplaceSymbolUtil;

import java.util.List;

/**
 * 个人拜访计划实体
 * Created by LiHuai on 2016/08/06 0027.
 */
public class VisitPlanPersonalListBean extends BaseBean {
    public VisitPlanPersonalBean data;

    public class VisitPlanPersonalBean {
        public List<VisitPlanBean> list;
    }

    public class VisitPlanBean {
        /**
         * shopTotal : 0
         * week : 星期二
         * lineId :
         * lineName :
         */

        public int shopTotal;
        public String week;
        public String lineId;
        public String lineName;

        @Override
        public String toString() {
            return this.lineId + "," + this.week + ReplaceSymbolUtil.AITE;
        }
    }
}
