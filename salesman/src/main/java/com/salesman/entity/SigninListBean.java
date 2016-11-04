package com.salesman.entity;

import java.util.List;

/**
 * 签到列表实体
 * Created by LiHuai on 2016/1/29 0029.
 */
public class SigninListBean extends BaseBean {

    public DataBean data;

    public static class DataBean {
        /**
         * staffTotal : 3
         * signList : [{"id":"21ba279adf0d4980aa048dc4bec0c7b0","userName":"元宝","createBy":"ff80808151e6527a01521f64daf80ea7","createTime":"2016-01-27","signTime":"15:14:27","type":2,"typeName":"下班签到","week":"星期一"}]
         */

        public List<SignGroupBean> list;
    }

    public static class SignGroupBean {
        public int staffTotal;
        public String typeName;
        public List<SignBean> signList;

    }

    public static class SignBean {
        /**
         * id : 21ba279adf0d4980aa048dc4bec0c7b0
         * userName : 元宝
         * createBy : ff80808151e6527a01521f64daf80ea7
         * createTime : 2016-01-27
         * signTime : 15:14:27
         * type : 2
         * typeName : 下班签到
         * week : 星期一
         */
        public String id;
        public String userName;
        public String createBy;
        public String createTime;
        public String signTime;
        public int type;
        public String typeName;
        public String week;

        public String outWorkStart; // 外勤开始时间（版本V1.3.0增加）
        public String outWorkEnd;   // 外勤结束时间（版本V1.3.0增加）
    }
}
