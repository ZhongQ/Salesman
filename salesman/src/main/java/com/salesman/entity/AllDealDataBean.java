package com.salesman.entity;

/**
 * 总交易数据实体
 * Created by LiHuai on 2016/7/7 0007.
 */
public class AllDealDataBean extends BaseBean{
    /**
     * saleMoney : 0.00
     * buyMoney : 0.00
     * monthMoney : 0.00
     */

    public DataBean data;

    public static class DataBean {
        public String saleMoney;
        public String buyMoney;
        public String monthMoney;
        public String deptName;
    }
}
