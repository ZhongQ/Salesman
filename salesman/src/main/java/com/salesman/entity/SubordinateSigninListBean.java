package com.salesman.entity;

import java.util.List;

/**
 * 下属签到列表实体
 * Created by LiHuai on 2016/2/23 0023.
 */
public class SubordinateSigninListBean extends BaseBean {

    /**
     * pageNo : 1
     * pageSize : 5
     * count : 5
     * list : [{"id":"a0ba840490c5426aaf9fca311b8d6ca3","userName":"李坏坏","createBy":"ff80808151e6527a01521f64daf80ea6","createTime":"2016-02-23","signTime":"09:38:07","type":1,"typeName":"上班签到","week":"星期二"},{"id":"9e803de7165f4b90b1ba09f8b9f03013","userName":"李坏坏","createBy":"ff80808151e6527a01521f64daf80ea6","createTime":"2016-02-19","signTime":"18:24:12","type":1,"typeName":"上班签到","week":"星期一"},{"id":"017331f8b7884e038272256f60b66e0a","userName":"李坏坏","createBy":"ff80808151e6527a01521f64daf80ea6","createTime":"2016-02-19","signTime":"19:23:19","type":2,"typeName":"下班签到","week":"星期四"},{"id":"2333e3096cc043c8b5a1b65815105a48","userName":"李坏坏","createBy":"ff80808151e6527a01521f64daf80ea6","createTime":"2016-02-18","signTime":"10:26:36","type":1,"typeName":"上班签到","week":"星期三"},{"id":"a7a78930182b43ebb65a123319596969","userName":"李坏坏","createBy":"ff80808151e6527a01521f64daf80ea6","createTime":"2016-02-18","signTime":"12:52:52","type":2,"typeName":"下班签到","week":"星期三"}]
     * firstResult : 0
     * maxResults : 5
     */

    public DataBean data;

    public static class DataBean {
        public int pageNo;
        public int pageSize;
        public int count;
        public int firstResult;
        public int maxResults;
        /**
         * id : a0ba840490c5426aaf9fca311b8d6ca3
         * userName : 李坏坏
         * createBy : ff80808151e6527a01521f64daf80ea6
         * createTime : 2016-02-23
         * signTime : 09:38:07
         * type : 1
         * typeName : 上班签到
         * week : 星期二
         */

        public List<SigninBean> list;
    }

    public static class SigninBean {
        public String id;
        public String userName;
        public String createBy;
        public String createTime;
        public String signTime;
        public int type;
        public String typeName;
        public String week;
        public String outWorkStart;// 外勤开始时间(版本V1.3.0增加)
        public String outWorkEnd;// 外勤结束时间(版本V1.3.0增加)
    }
}
