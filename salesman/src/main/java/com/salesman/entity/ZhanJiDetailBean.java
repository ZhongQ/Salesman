package com.salesman.entity;

/**
 * 战绩详情实体
 * Created by LiHuai on 2016/7/7 0007.
 */
public class ZhanJiDetailBean extends BaseBean {
    /**
     * saleMoneyAll : 0.00
     * visitSaleMoney : 0.00
     * buyMoneyAll : 0.00
     * dbUser : 静静
     * buyMoney : 0.00
     */

    public DataBean data;

    public static class DataBean {
        public String saleMoneyAll;
        public String visitSaleMoney;
        public String buyMoneyAll;
        public String dbUser;
        public String buyMoney;
        public String createTime;
    }
}
