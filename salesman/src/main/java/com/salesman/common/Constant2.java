package com.salesman.common;

/**
 * 店宝接口公共类
 * Created by LiHuai on 2016/6/23
 */
public class Constant2 {

    // 开泰服务
//    public static final String BASE_URL = "http://192.168.1.144:9090";
    // 测试服务器
//    public static final String BASE_URL = "http://jfun360.f3322.net:8077";
//    public static final String BASE_URL = "http://192.168.1.16:8077";
    // 正式服务
    public static final String BASE_URL = "http://www.izjjf.cn";

    // 公共模块
    public static final String COMMON_URL = "/zjjf-kefu/mobile/";

    // 模块
    public static final String module_store = COMMON_URL + "store/";// 商店

    /*客户*/
    // 客户审核列表
    public static final String moduleClientCheckList = BASE_URL + module_store + "getStoreList.do";
    // 客户审核详情
    public static final String moduleClientDetailInfo = BASE_URL + module_store + "getStoreDetailInfo.do";
    // 客户审核接口
    public static final String moduleClientApproval = BASE_URL + module_store + "approvalStore.do";

}
