package com.salesman.entity;

import java.util.List;

/**
 * 日报模板列表实体
 * Created by LiHuai on 2016/4/19 0019.
 */
public class DailyTemplateListBean extends BaseBean {

    public DataBean data;

    public static class DataBean {
        /**
         * tmplId : 8a81814f54295a650154295a71f40004
         * tmplName : 日报模板信息
         * type : 1
         */

        public List<DailyTemplateBean> list;
    }


    public static class DailyTemplateBean {
        public String tmplId;
        public String tmplName;
        public String type;
    }
}
