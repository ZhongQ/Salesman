package com.salesman.utils;

import com.salesman.application.SalesManApplication;

/**
 * 用户信息工具类
 * Created by LiHuai on 2016/7/8 0008.
 */
public class UserInfoUtil {

    /**
     * 是否管理员
     *
     * @return
     */
    public static boolean isAdministrator() {
        UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
        if ("1".equals(mUserInfo.getUserType())) {
            return true;
        }
        return false;
    }

    /**
     * 是否定格代表
     *
     * @return
     */
    public static boolean isBDRepresentative() {
        UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
        if ("1".equals(mUserInfo.getBDType())) {
            return true;
        }
        return false;
    }

}
