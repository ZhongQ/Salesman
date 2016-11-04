package com.salesman.entity;

import java.io.Serializable;

/**
 * 首页最新消息实体
 * Created by LiHuai on 2016/4/6 0006.
 */
public class NewestMesBean extends BaseBean {
    /**
     * notice : {"total":"0","update":false}
     * daily : {"total":"0","update":false}
     */

    public NewsBean data;

    public static class NewsBean implements Serializable {
        public NewsBean(int msgTotal) {
            this.msgTotal = msgTotal;
        }

        /**
         * total : 0
         * update : false
         */

        public MessageBean notice;
        /**
         * total : 0
         * update : false
         */

        public MessageBean daily;
        /**
         * total : 0
         * update : false
         */

        public MessageBean comment;
        public int msgTotal;

        public int getMsgTotal() {
            return msgTotal;
        }

        public void setMsgTotal(int msgTotal) {
            this.msgTotal = msgTotal;
        }
    }

    public static class MessageBean implements Serializable {
        public String total;
        public boolean update;
    }
}
