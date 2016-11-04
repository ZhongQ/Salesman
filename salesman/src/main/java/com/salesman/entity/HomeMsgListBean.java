package com.salesman.entity;

import java.util.List;

/**
 * 首页消息列表实体
 * Created by LiHuai on 2016/8/17 0017.
 */
public class HomeMsgListBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        /**
         * total : 0
         * createTime : 08月16日
         * subject : “婴儿”级台风明天或袭击，深圳未来三天有暴雨！雨！雨！
         * type : 1
         */

        public String warnTotal;
        public List<MsgBean> list;

    }

    public static class MsgBean {
        public String total;
        public String createTime;
        public String subject;
        public String type;
        public String typeName;
    }
}
