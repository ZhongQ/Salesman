package com.salesman.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 公告列表实体
 * Created by LiHuai on 2016/1/27 0027.
 */
public class NoticeListBean extends BaseBean {
    public NoticeDataBean data;

    public class NoticeDataBean {
        public int pageNo;
        public int pageSize;
        public int count;
        public int firstResult;
        public int maxResults;
        public List<NoticeBean> list;
    }

    public class NoticeBean implements Serializable{
        public String id;
        public String userName;
        public String createTime;
        public String subject;
        public String content;
        public String deptName;

        public int total;// 2016/3/30增加
        public String noticeId;// 2016/5/25增加
    }
}
