package com.salesman.entity;

import java.util.List;

/**
 * 公告详情实体
 * Created by LiHuai on 2016/5/25 0025.
 */
public class NoticeDetailBean extends BaseBean {
    /**
     * resultObj : {"userName":"叶开","createTime":"03月16日","subject":"培训通知","content":"今晚阿街足迹产品，需要进行培训。","total":0}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * userName : 叶开
         * createTime : 03月16日
         * subject : 培训通知
         * content : 今晚阿街足迹产品，需要进行培训。
         * total : 0
         */

        public NoticeDetail resultObj;
    }

    public static class NoticeDetail {
        public String userName;
        public String createTime;
        public String subject;
        public String content;
        public int total;
        public String deptName;
        public String postName;         // V1.6.0
        public List<String> picList;
    }
}
