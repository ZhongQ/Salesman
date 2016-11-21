package com.salesman.common;

/**
 * 接口公共类
 * Created by LiHuai on 2016/1/21
 */
public class Constant {

    // 测试服务
//    public static final String BASE_URL = "http://jfun360.f3322.net:6077";
//    public static final String BASE_URL = "http://192.168.1.11:8077";
    public static final String BASE_URL = "http://192.168.1.16:6077";// 内网
//    public static final String BASE_URL = "http://112.74.196.61:8077";
    // 正式服务
//    public static final String BASE_URL = "http://www.izjjf.cn";
    // 元宝服务
//    public static final String BASE_URL = "http://192.168.31.59:8080";
//    public static final String BASE_URL = "http://192.168.1.148:8080";
    // 公测服务
//    private static final String BASE_URL = "http://120.76.208.143:6077";

    // 公共模块
    private static final String COMMON_URL = "/salesman-provide/mobile/";
    // 文件夹名称
    public static final String DOWNLOADDIR = "/salesman/";
    public static final String APP_FOLDER = "salesman/imgcache";

    // 模块
    private static final String module_user = COMMON_URL + "user/";// 用户
    private static final String module_notice = COMMON_URL + "notice/";// 公告
    private static final String module_signin = COMMON_URL + "sign/";// 签到
    private static final String module_track = COMMON_URL + "track/";// 足迹
    private static final String module_shop = COMMON_URL + "shop/";// 门店
    private static final String module_proprietor = COMMON_URL + "proprietor/";// 负责人
    private static final String module_upload = COMMON_URL + "fileUpload/";// 文件上传
    private static final String module_version = COMMON_URL + "version/";// 版本升级
    private static final String module_log = COMMON_URL + "log/";// 日志
    private static final String module_subordinate = COMMON_URL + "underling/";// 下属
    private static final String module_tmpl = COMMON_URL + "tmpl/";// 日报模板
    private static final String module_daily = COMMON_URL + "daily/";// 日报
    private static final String module_monitor = COMMON_URL + "monitor/";// 监听器
    private static final String module_report = COMMON_URL + "report/";// 工作日志
    private static final String module_spgLine = COMMON_URL + "spgLine/";// 定格线路
    private static final String module_region = COMMON_URL + "region/";// 省市区
    private static final String module_kpi = COMMON_URL + "kpi/";// 战绩
    private static final String module_line = COMMON_URL + "line/";// 线路
    private static final String module_audit = COMMON_URL + "audit/";// 审核
    private static final String module_work = COMMON_URL + "work/";// 工作

    /*首页*/
    // 公告列表
    public static final String moduleNoticeList = BASE_URL + module_notice + "getNoticePageList.do";
    // 签到接口
    public static final String moduleSignIn = BASE_URL + module_signin + "addSignInfo.do";
    // 签到接口(版本V1.3.0)
    public static final String moduleSignData = BASE_URL + module_signin + "addSignData.do";
    // 当天足迹明细列表
    public static final String moduleTrackList = BASE_URL + module_track + "getMyTrackList.do";
    // 上传足迹接口
    public static final String moduleUploadTrack = BASE_URL + module_track + "addTrackRecord.do";
    // 图片上传接口
    public static final String moduleUploadPic = BASE_URL + module_upload + "upload.do";
    // 足迹查询接口
    public static final String moduleAllTrack = BASE_URL + module_track + "getMyAllDateTrackList.do";
    // 批量上传足迹接口
    public static final String moduleBatchUploadTrack = BASE_URL + module_track + "addBatchTrackRecord.do";
    // 获取最新公告
    public static final String moduleNewestNotice = BASE_URL + module_notice + "getNewNotice.do";
    // 首页日报列表
    public static final String moduleHomeDailyList = BASE_URL + module_daily + "getDailyList.do";
    // 首页新消息提醒
    public static final String moduleHomeMessageRemind = BASE_URL + module_monitor + "getNewInfo.do";
    // 外勤签到线路及客户
    public static final String moduleOutLineAndClient = BASE_URL + module_shop + "getMyVisitLineList.do";
    // 首页消息列表
    public static final String moduleHomeMsgList = BASE_URL + module_monitor + "getMyNewsList.do";
    // 阿街提醒列表
    public static final String moduleAJieMsgsRemindList = BASE_URL + module_monitor + "getWarnMsgList.do";


    /*管理*/
    // 今日足迹和历史足迹列表
    public static final String moduleTodayTrackList = BASE_URL + module_track + "getGroupTrackList.do";
    // 今日签到和历史签到列表
    public static final String moduleTodaySigninList = BASE_URL + module_signin + "getGroupSignList.do";
    // 发公告
    public static final String moduleRelaseNotice = BASE_URL + module_notice + "addNotice.do";
    // 发布对象
    public static final String moduleRelaseObjectList = BASE_URL + module_subordinate + "getUserList.do";
    // 日报模板
    public static final String moduleDailyList = BASE_URL + module_tmpl + "getTmplFieldList.do";
    // 发日报
    public static final String moduleReleaseDaily = BASE_URL + module_daily + "addDaily.do";
    // 日报详情
    public static final String moduleDailyDetails = BASE_URL + module_daily + "getDailyById.do";
    // 评论日报
    public static final String moduleCommentDaily = BASE_URL + module_daily + "addComment.do";
    // 日报评论列表
    public static final String moduleDailyCommentList = BASE_URL + module_daily + "getCommentList.do";
    // 发公告(版本V1.3.0)
    public static final String moduleRelaseNoticeV130 = BASE_URL + module_notice + "addNoticeV130.do";
    // 公告详情
    public static final String moduleNoticeDetail = BASE_URL + module_notice + "getNoticeDetail.do";


    /*工作日志（版本V1.2.0）*/
    // 日报模板（版本V1.2.0）
    public static final String moduleDailyTemplateList = BASE_URL + module_tmpl + "getTmplList.do";
    // 日报模板字段（版本V1.2.0）
    public static final String moduleDailyFieldList = BASE_URL + module_tmpl + "getTmplAttrList.do";
    // 发工作日志（版本V1.2.0）
    public static final String moduleAddReport = BASE_URL + module_report + "addReport.do";
    // 工作日志明细（版本V1.2.0）
    public static final String moduleReportDetails = BASE_URL + module_report + "getReportById.do";
    // 工作日志列表（版本V1.2.0；首页）
    public static final String moduleReportList = BASE_URL + module_report + "getReportList.do";
    // 工作日志列表（版本V1.2.0；我的日报，下属日报，今日日报）
    public static final String moduleMyReportList = BASE_URL + module_report + "getMyReportList.do";
    // 日志消息列表
    public static final String moduleLogMessageList = BASE_URL + module_monitor + "getNewsList.do";
    // 日志历史消息列表
    public static final String moduleLogHistoryMessageList = BASE_URL + module_daily + "getHisCommentList.do";
    // 最新消息列表（V1.6.0）
    public static final String moduleNewestMsgsList = BASE_URL + module_daily + "getUnreadCommentList.do";


    /*客户*/
    // 客户列表(V1.5.0修改)
    public static final String moduleClientList = BASE_URL + module_shop + "getMyShopList.do";
    // 客户详情
    public static final String moduleClientDetails = BASE_URL + module_shop + "getShopInfoById.do";
    // 添加客户
    public static final String moduleAddClient = BASE_URL + module_shop + "addShopInfo.do";
    // 修改客户
    public static final String moduleEditClient = BASE_URL + module_shop + "updateShopInfo.do";
    // 添加修改负责人
    public static final String moduleAddPrincipal = BASE_URL + module_proprietor + "updateProprietor.do";
    // 添加修改负责人
    public static final String moduleShopCoordinate = BASE_URL + module_shop + "getSoordinateList.do";
    // 定格线路（V1.5.0）
    public static final String moduleDingGeLine = BASE_URL + module_spgLine + "getMySpGroupList.do";
    // 客户坐标（V1.5.0）
    public static final String moduleClientSiteList = BASE_URL + module_spgLine + "getShopSiteList.do";
    // 客户坐标（V1.6.0）
    public static final String moduleShopSiteList = BASE_URL + module_shop + "getShopSiteList.do";
    // 客户类型（V1.5.0）
    public static final String moduleClientType = BASE_URL + module_shop + "getShopType.do";
    // 外勤客户列表（V1.5.0）
    public static final String moduleOutsideClientList = BASE_URL + module_spgLine + "getCustByLineId.do";
    // 省市区（V1.5.0）
    public static final String moduleProvinceCityDistrict = BASE_URL + module_region + "getAllRegionList.do";
    // 业务员与线路接口（V1.6.0）
    public static final String moduleSalesmanLineList = BASE_URL + module_shop + "getMyShopLineList.do";
    // 客户审核列表（V1.6.5）
    public static final String moduleClientCheckList = BASE_URL + module_audit + "getAuditCustList.do";
    // 客户审核详情（V1.6.5）
    public static final String moduleClientCheckDetail = BASE_URL + module_audit + "getAuditCustDetail.do";
    // 客户审核（V1.6.5）
    public static final String moduleClientCheck = BASE_URL + module_audit + "updateStoreStatus.do";
    // 客户基础信息（V2.0.0）
    public static final String moduleClientInfo = BASE_URL + module_shop + "getShopBaseInfoById.do";
    // 客户线路保存（V2.0.0）
    public static final String moduleSaveClientLine = BASE_URL + module_line + "switchLine.do";

    /*个人中心*/
    // 我的足迹列表
    public static final String moduleMyTrackList = BASE_URL + module_track + "getMyEverydayTrack.do";
    // 我的足迹列表
    public static final String moduleChangePassword = BASE_URL + module_user + "updateUserPwd.do";
    // 登录
    public static final String moduleLogIn = BASE_URL + module_user + "login.do";
    // 注销登录
    public static final String moduleLogOut = BASE_URL + module_user + "logout.do";
    // 签到记录接口
    public static final String moduleSignInRecordList = BASE_URL + module_signin + "getMySignDetailList.do";
    // 下属签到接口
    public static final String modulePersonalSignRecordList = BASE_URL + module_signin + "getMySignList.do";
    // 下属足迹接口
    public static final String modulePersonalTrackRecordList = BASE_URL + module_track + "getMyTrackDetailList.do";
    // 我的下属接口
    public static final String moduleMySubordinateList = BASE_URL + module_subordinate + "getMyUnderlingList.do";
    // 我的日报和下属日报
    public static final String moduleMyDailyList = BASE_URL + module_daily + "getMyDailyList.do";

    /*工作*/
    // 总交易数据
    public static final String moduleWorkDealData = BASE_URL + module_kpi + "getKPIBaseInfo.do";
    // 战绩明细
    public static final String moduleZhanJiDetail = BASE_URL + module_kpi + "getKPIDetailInfo.do";
    // 战绩筛选条件
    public static final String moduleZhanJiFilter = BASE_URL + module_user + "getDeptLevelList.do";
    // 店宝或联合采购明细
    public static final String moduleZhanJiOrderList = BASE_URL + module_kpi + "getSpgroupOrderList.do";
    // 日志评论删除
    public static final String moduleDelLogComment = BASE_URL + module_report + "delDailyCommnent.do";
    // 公告删除
    public static final String moduleDelNotice = BASE_URL + module_notice + "delNoticeById.do";
    // 拜访计划
    public static final String moduleMyVisitPlanList = BASE_URL + module_line + "getMyVisitPlansList.do";
    // 业务员线路
    public static final String moduleSalesLinesList = BASE_URL + module_line + "getVisitLineByUserId.do";
    // 业务拜访计划保存
    public static final String moduleUpdateVisitPlans = BASE_URL + module_line + "updateVisitPlans.do";
    // 今日拜访计划
    public static final String moduleVisitPlansList = BASE_URL + module_line + "getTodayVisitPlansList.do";
    // 战绩首页（V2.0.0）
    public static final String moduleZhanJiData = BASE_URL + module_work + "getKPIBaseInfo.do";
    // 拜访计划（V2.0.0）
    public static final String moduleVisitPlan = BASE_URL + module_line + "getMyVisitReportList.do";
    // 业绩列表（V2.0.0）
    public static final String moduleYeJiList = BASE_URL + module_work + "getHisResultList.do";
    // 订单明细列表（V2.0.0）
    public static final String moduleOrderList = BASE_URL + module_work + "getOrderDetail.do";
    // 商铺历史订单列表（V2.0.0）
    public static final String moduleShopOrderList = BASE_URL + module_work + "getOrderDetailList.do";
    // 英雄榜列表（V2.0.0）
    public static final String moduleHeroRankingList = BASE_URL + module_work + "getKPIRankList.do";
    // 业绩趋势（V2.0.0）
    public static final String moduleYeJiTrend = BASE_URL + module_work + "getChartReport.do";


    /*版本更新*/
    public static final String moduleVersionUpdate = BASE_URL + module_version + "getKDBVersionInfo.do";
    /*日志上传*/
    public static final String moduleUploadCrash = BASE_URL + module_log + "addErrorLog.do";
    /*app运行日志*/
    public static final String moduleUploadAppLog = BASE_URL + module_log + "addAppRuningLog.do";


    /**
     * UI设计的基准宽度.
     */
    public static int UI_WIDTH = 720;

    /**
     * UI设计的基准高度.
     */
    public static int UI_HEIGHT = 1280;

    /**
     * UI设计的密度.
     */
    public static int UI_DENSITY = 320;

    /**
     * 上传图片宽度.
     */
    public static int UPLOAD_WIDTH = 200;
    /**
     * 上传图片高度.
     */
    public static int UPLOAD_HEIGHT = 200;
    /**
     * 自定义字体路径
     * (造字工房悦黑字体)
     */
//    public static String CUSTOM_FONTS = "fonts/RTWSYueGoTrial-ExLightCom.ttf";
    public static String CUSTOM_FONTS = "fonts/DINMittelschrift-Alternate.ttf";
}
