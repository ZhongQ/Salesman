package com.salesman.entity;

/**
 * 用户实体
 * Created by LiHuai on 2016/2/3 0003.
 */
public class UserBean extends BaseBean {

    /**
     * id : ff80808151e6527a01521f64daf80ea6
     * userName : 李坏
     * nickName : 李坏
     * gender : 1
     * password :
     * mobile : 13267188047
     * email :
     * deviceUUID : 355848067676240
     * sessionid : E94BFE417FBBF79A5D337BF0E4AC614B
     * token : 7fed42a09f1f46a584b9b944d781ce20
     */

    public DataBean data;

    public class DataBean {
        public String userId;
        public String userName;
        public String nickName;
        public int gender;
        public String password;
        public String mobile;
        public String email;
        public String deviceUUID;
        public String sessionid;
        public String token;
        public String userType;
        public String postName;
        public String timeHz;// 足迹上传频率（单位s）
        public String deptId;
        public String deptName;

        public int signStart;// 上班签到标记; 0：未签到；1：已签到
        public int signEnd;// 下班签到标记; 0：未签到；1：已签到
        public String areaId; // 区域id
        public String bdType; // 定格代表
    }
}
