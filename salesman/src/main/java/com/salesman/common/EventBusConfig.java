package com.salesman.common;

/**
 * EventBus配置类
 * Created by LiHuai on 2016/4/22 0022.
 */
public class EventBusConfig {
    /*app注销*/
    public static final String APP_EXIT = "app_exit";

    /*首页*/
    // 首页刷新
    public static final String HOME_REFRESH = "home_refresh";
    // 首页消息提醒消失
    public static final String MSGS_VIEW_GONE = "msgs_view_gone";
    // 日志列表
    public static final String LOG_LIST_REFRESH = "log_list_refresh";

    /*签到*/
    public static final String SIGNIN_ADDRESS = "signin_address";

    /*客户*/
    // 客户审核
    public static final String CLIENT_CHECK = "client_check";
    // 添加客户
    public static final String ADD_CLIENT_FINISH = "add_client_finish";
    // 客户详情
    public static final String CLIENT_DETAIL_REFRESH = "client_detail_refresh";
    // 客户列表
    public static final String CLIENT_LIST_REFRESH = "client_list_refresh";

    /*工作*/
    // 拜访列表
    public static final String VISIT_LIST_REFRESH = "visit_list_refresh";

    /*我的*/
    //

}
