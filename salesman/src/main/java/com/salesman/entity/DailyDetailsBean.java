package com.salesman.entity;

import java.util.List;

/**
 * 日报详情实体
 * Created by LiHuai on 2016/3/29 0029.
 */
public class DailyDetailsBean extends BaseBean {

    /**
     * userName : 李坏坏
     * remark : 我是备注！
     * picList : ["http://www.izjjf.cn/group1/M00/00/87/cEpChlb5-JGAXN67AALsptw4s5U929.jpg"]
     * tmplList : [{"type":"salesman_report","fieldCnName":"今日拜访路线","fieldEnName":"visitLine","fieldType":2,"isRequired":0,"fieldVal":"1"},{"type":"salesman_report","fieldCnName":"配送商","fieldEnName":"distributionCenter","fieldType":1,"isRequired":1,"fieldVal":"2"},{"type":"salesman_report","fieldCnName":"本线路便利店数","fieldEnName":"lineStoreNum","fieldType":2,"isRequired":1,"fieldVal":"3"},{"type":"salesman_report","fieldCnName":"实际拜访便利店数","fieldEnName":"visitStoreNum","fieldType":2,"isRequired":0,"fieldVal":"4"},{"type":"salesman_report","fieldCnName":"有效拜访便利店数","fieldEnName":"visitStoreReapNum","fieldType":2,"isRequired":0,"fieldVal":"5"},{"type":"salesman_report","fieldCnName":"计划开发注册店","fieldEnName":"planRegisterNum","fieldType":2,"isRequired":0,"fieldVal":"6"},{"type":"salesman_report","fieldCnName":"实际开发注册店","fieldEnName":"actualRegisterlNum","fieldType":3,"isRequired":1,"fieldVal":"7"},{"type":"salesman_report","fieldCnName":"本线路已开发店数","fieldEnName":"yetDevelopStoreNum","fieldType":3,"isRequired":1,"fieldVal":"8"},{"type":"salesman_report","fieldCnName":"本线路注册店占比","fieldEnName":"registerlStoreRatio","fieldType":1,"isRequired":1,"fieldVal":"9"},{"type":"salesman_report","fieldCnName":"计划开发活跃店数","fieldEnName":"planActiveStoreNum","fieldType":1,"isRequired":1,"fieldVal":"10"},{"type":"salesman_report","fieldCnName":"实际开发活跃店数","fieldEnName":"actualActiveStoreNum","fieldType":1,"isRequired":1,"fieldVal":"11"},{"type":"salesman_report","fieldCnName":"本线路已开发活跃店数","fieldEnName":"ownActiveStoreNum","fieldType":1,"isRequired":1,"fieldVal":"12"},{"type":"salesman_report","fieldCnName":"本线路活跃店占比","fieldEnName":"lineActiveStoreRatio","fieldType":1,"isRequired":1,"fieldVal":"13"},{"type":"salesman_report","fieldCnName":"今日计划开发高频店","fieldEnName":"planDevelopHF","fieldType":1,"isRequired":1,"fieldVal":"14"},{"type":"salesman_report","fieldCnName":"今日实际开发高频店","fieldEnName":"actualDevelopHF","fieldType":1,"isRequired":1,"fieldVal":"15"},{"type":"salesman_report","fieldCnName":"本线路已开发高频店","fieldEnName":"lineYetStoreHFNum","fieldType":1,"isRequired":1,"fieldVal":"16"},{"type":"salesman_report","fieldCnName":"本线路高频店占比","fieldEnName":"lineStoreHFRatio","fieldType":1,"isRequired":1,"fieldVal":"17"}]
     */

    public DataBean data;

    public static class DataBean {
        public String userName;
        public String remark;
        public String createTime;
        public String deptName;         // V1.6.0
        public String postName;         // V1.6.0
        public String participant;      // V1.6.0
        public String tmplName;
        public List<String> picList;
        /**
         * type : salesman_report
         * fieldCnName : 今日拜访路线
         * fieldEnName : visitLine
         * fieldType : 2
         * isRequired : 0
         * fieldVal : 1
         */

        public List<ReleaseDailyBean.ReleaseListBean> tmplList;

        // 版本V1.2.0
        public List<FieldBean> fieldList;
    }

    public static class FieldBean {

        /**
         * key : 今日拜访路线
         * value : 输入内容
         */

        public String key;
        public String value;
    }
}
