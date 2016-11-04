package com.salesman.entity;

import java.util.List;

/**
 * 消息列表实体
 * Created by LiHuai on 2016/4/21 0021.
 */
public class MessageListBean extends BaseBean {

    public DataBean data;

    public static class DataBean {
        /**
         * type : 3
         * createTime : 04月22日
         * comment : 为什么
         * postBy : 十四郎
         * replyBy : 明明
         * createBy : 何丹
         * reportId : 9520e0fa902445688dcc2b5778030ad6
         * timePoint : 13:39
         */

        public List<MessageBean> list;// 1:公告；2：日志；3：日志消息
    }


    public static class MessageBean {

        public int imgId;
        /**
         * deptName :
         * type : 3
         * reportId : 36cb203aa5e1470db810fe56b5548110
         * comment : 慢慢来
         * postBy : 静静
         * replyBy : 萧别离
         * createBy : 萧别离
         * createTime : 10月19日
         * timePoint : 15:23
         */

        public String deptName;
        public int type;
        public String reportId;
        public String comment;
        public String postBy;
        public String replyBy;
        public String createBy;
        public String createTime;
        public String timePoint;

        public int getImgId() {
            return imgId;
        }

        public void setImgId(int imgId) {
            this.imgId = imgId;
        }
    }
}
