package com.salesman.entity;

import java.util.List;

/**
 * 下属足迹列表实体
 * Created by LiHuai on 2016/2/23 0023.
 */
public class SubordinateTrackListBean extends BaseBean {
    /**
     * pageNo : 1
     * pageSize : 10
     * count : 15
     * list : [{"userId":"ff80808151e6527a01521f64daf80ea6","userName":"李坏坏","createTime":"2016-02-23","timePoint":"10:52:47","positionName":"中国广东省深圳市南山区粤兴二道6","week":"星期二"},{"userId":"ff80808151e6527a01521f64daf80ea6","userName":"李坏坏","createTime":"2016-02-19","timePoint":"19:52:16","positionName":"中国广东省深圳市南山区粤兴二道6","week":"星期五"},{"userId":"ff80808151e6527a01521f64daf80ea6","userName":"李坏坏","createTime":"2016-02-18","timePoint":"20:49:18","positionName":"中国广东省深圳市南山区粤兴二道6","week":"星期四"},{"userId":"ff80808151e6527a01521f64daf80ea6","userName":"李坏坏","createTime":"2016-02-17","timePoint":"21:23:56","positionName":"中国广东省深圳市南山区粤兴二道6","week":"星期三"},{"userId":"ff80808151e6527a01521f64daf80ea6","userName":"李坏坏","createTime":"2016-02-16","timePoint":"20:08:33","positionName":"中国广东省深圳市南山区粤兴二道6","week":"星期二"},{"userId":"ff80808151e6527a01521f64daf80ea6","userName":"李坏坏","createTime":"2016-02-15","timePoint":"18:22:50","positionName":"中国广东省深圳市南山区粤兴二道6","week":"星期一"},{"userId":"ff80808151e6527a01521f64daf80ea6","userName":"李坏坏","createTime":"2016-01-27","timePoint":"16:02:11","positionName":"深圳南山科技园科苑武汉大学城","week":"星期三"},{"userId":"ff80808151e6527a01521f64daf80ea6","userName":"李坏坏","createTime":"2016-01-26","timePoint":"16:02:11","positionName":"深圳南山科技园科苑武汉大学城","week":"星期三"},{"userId":"ff80808151e6527a01521f64daf80ea6","userName":"李坏坏","createTime":"2016-01-25","timePoint":"16:02:11","positionName":"深圳南山科技园科苑武汉大学城","week":"星期二"},{"userId":"ff80808151e6527a01521f64daf80ea6","userName":"李坏坏","createTime":"2016-01-24","timePoint":"16:02:11","positionName":"深圳南山科技园科苑武汉大学城","week":"星期二"}]
     * firstResult : 0
     * maxResults : 10
     */

    public DataBean data;

    public static class DataBean {
        public int pageNo;
        public int pageSize;
        public int count;
        public int firstResult;
        public int maxResults;
        /**
         * userId : ff80808151e6527a01521f64daf80ea6
         * userName : 李坏坏
         * createTime : 2016-02-23
         * timePoint : 10:52:47
         * positionName : 中国广东省深圳市南山区粤兴二道6
         * week : 星期二
         */

        public List<TrackBean> list;

    }

    public static class TrackBean {
        public String userId;
        public String userName;
        public String createTime;
        public String timePoint;
        public String positionName;
        public String week;
    }
}
