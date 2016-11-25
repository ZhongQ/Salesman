package com.salesman.global;

import com.salesman.R;
import com.salesman.entity.NoticeReleaseObj;
import com.salesman.entity.PayWayBean;
import com.salesman.entity.PersonalListBean;
import com.salesman.entity.ShopTypeBean;
import com.salesman.entity.UploadImageBean;
import com.salesman.entity.WorkBean;
import com.salesman.views.popupwindow.ActionItem;
import com.salesman.views.popupwindow.FilterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiHuai on 2016/1/26 0026.
 */
public class BeanListHolder {

    /**
     * 个人中心(管理者)
     */
    public static List<PersonalListBean> getPersonalBeanList() {
        List<PersonalListBean> data = new ArrayList<>();
        data.add(new PersonalListBean(9, R.drawable.my_daily_icon, "我的日志", true, true, false));
        data.add(new PersonalListBean(1, R.drawable.my_signin, "我的签到", true, false));
        data.add(new PersonalListBean(2, R.drawable.my_trace, "我的足迹", false, false));
        data.add(new PersonalListBean(10, R.drawable.my_notice, "我的公告", true, true));
//        data.add(new PersonalListBean(3, R.drawable.manage_xiashu, "管理下属", false, false));
        data.add(new PersonalListBean(4, R.drawable.my_xiashu, "我的下属", false, false));
//        data.add(new PersonalListBean(5, R.drawable.history_mission, "历史任务", true, false));
//        data.add(new PersonalListBean(6, R.drawable.history_trace, "历史足迹", true, false));
//        data.add(new PersonalListBean(7, R.drawable.history_signin, "历史签到", false, false));
//        data.add(new PersonalListBean(8, R.drawable.change_password_icon, "修改密码", false, true));

        return data;
    }

    /**
     * 个人中心(业务员)
     */
    public static List<PersonalListBean> getSalesmanPersonalBeanList() {
        List<PersonalListBean> data = new ArrayList<>();
        data.add(new PersonalListBean(9, R.drawable.my_daily_icon, "我的日志", true, true, false));
        data.add(new PersonalListBean(1, R.drawable.my_signin, "我的签到", true, false));
        data.add(new PersonalListBean(2, R.drawable.my_trace, "我的足迹", false, false));
//        data.add(new PersonalListBean(3, R.drawable.my_xiashu, "我的客户", false, true));
//        data.add(new PersonalListBean(4, R.drawable.change_password_icon, "修改密码", false, true));

        return data;
    }

    /**
     * 管理
     *
     * @return
     */
    public static List<PersonalListBean> getManageBeanList() {
        List<PersonalListBean> data = new ArrayList<>();
//        data.add(new PersonalListBean(1, R.drawable.notice_icon, "公告", false, true));
        data.add(new PersonalListBean(4, R.drawable.my_daily_icon, "今日日志", true, true));
        data.add(new PersonalListBean(2, R.drawable.signin_icon, "今日签到", true, false));
        data.add(new PersonalListBean(3, R.drawable.my_trace, "今日足迹", false, false));

        return data;
    }

    /**
     * 商铺类型
     *
     * @return
     */
    public static List<ShopTypeBean> getShopTypeList() {
        List<ShopTypeBean> data = new ArrayList<>();
        data.add(new ShopTypeBean("社区店", false));
        data.add(new ShopTypeBean("街边店", false));
        data.add(new ShopTypeBean("村口店", false));
        data.add(new ShopTypeBean("工业区士多店", false));
        data.add(new ShopTypeBean("小超市", false));

        return data;
    }

    /**
     * 商品类型
     *
     * @return
     */
    public static List<ShopTypeBean> getGoodsTypeList() {
        List<ShopTypeBean> data = new ArrayList<>();
        data.add(new ShopTypeBean("酒水", false));
        data.add(new ShopTypeBean("烟", false));
        data.add(new ShopTypeBean("饮料", false));
        data.add(new ShopTypeBean("零食", false));
        data.add(new ShopTypeBean("文具", false));

        return data;
    }

    /**
     * 支付方式
     *
     * @return
     */
    public static List<PayWayBean> getPayWayList() {
        List<PayWayBean> data = new ArrayList<>();
        data.add(new PayWayBean(R.drawable.alipay_icon, "支付宝", false));
        data.add(new PayWayBean(R.drawable.wechat_icon, "微信", false));
        data.add(new PayWayBean(R.drawable.unionpay_icon, "银联", false));

        return data;
    }

    /**
     * 设置
     */
//    public static List<PersonalListBean> getSettingBeanList() {
//        List<PersonalListBean> data = new ArrayList<>();
//        data.add(new PersonalListBean(1, R.drawable.setting_msgs_icon, "消息提示音", true, true, 1));
//        data.add(new PersonalListBean(2, R.drawable.change_password_icon, "修改密码", false, false));
//
//        return data;
//    }
    public static List<UploadImageBean> getUploadImageBeanList() {
        List<UploadImageBean> data = new ArrayList<>();
        data.add(new UploadImageBean());

        return data;
    }

    /**
     * 首页popwindow
     *
     * @param type 1:表示管理者；2表示业务员
     * @return
     */
    public static List<ActionItem> getActionItemBeanList(int type) {
        List<ActionItem> data = new ArrayList<>();
        switch (type) {
            case 1:
                data.add(new ActionItem(1, "发公告", R.drawable.notice_pop_icon));
                data.add(new ActionItem(2, "发日志", R.drawable.log_pop_icon));
                data.add(new ActionItem(3, "足迹", R.drawable.track_pop_icon));
                break;
            case 2:
                data.add(new ActionItem(2, "发日志", R.drawable.log_pop_icon));
                data.add(new ActionItem(3, "足迹", R.drawable.track_pop_icon));
                break;
        }

        return data;
    }

    /**
     * 发公告指定发布对象
     *
     * @return
     */
    public static List<NoticeReleaseObj> getReleaseObjList() {
        List<NoticeReleaseObj> data = new ArrayList<>();
        data.add(new NoticeReleaseObj(1, "全体成员"));
        data.add(new NoticeReleaseObj(2, "我的部门"));
        data.add(new NoticeReleaseObj(3, "所有管理者"));
        data.add(new NoticeReleaseObj(4, "运营中心全体成员"));
        return data;
    }

    /**
     * 公告列表Dialog数据源
     *
     * @return
     */
    public static List<NoticeReleaseObj> getNoticeDialogItemList() {
        List<NoticeReleaseObj> data = new ArrayList<>();
        data.add(new NoticeReleaseObj(1, "编辑", false));
        data.add(new NoticeReleaseObj(2, "删除", false));
        return data;
    }


    /**
     * 客户筛选之注册状态
     *
     * @return
     */
    public static List<FilterItem> getClientRegisterFilter() {
        List<FilterItem> data = new ArrayList<>();
        data.add(new FilterItem("ALL", "全部"));
        data.add(new FilterItem("1", "已注册"));
        data.add(new FilterItem("0", "未注册"));
        return data;
    }

    /**
     * 工作模块
     *
     * @return
     */
    public static List<WorkBean> getWorkBeanList() {
        List<WorkBean> data = new ArrayList<>();
        data.add(new WorkBean(1, "今日战绩", R.drawable.today_zhanji_icon));
        data.add(new WorkBean(5, "今日拜访", R.drawable.today_visit_icon));
        data.add(new WorkBean(2, "今日足迹", R.drawable.today_track_icon));
        data.add(new WorkBean(3, "今日签到", R.drawable.today_signin_icon));
        data.add(new WorkBean(4, "今日日志", R.drawable.today_log_icon));
        data.add(new WorkBean(6, "", 0));
        return data;
    }

    /**
     * 客户筛选之重点客户
     *
     * @return
     */
    public static List<FilterItem> getClientVipFilter() {
        List<FilterItem> data = new ArrayList<>();
        data.add(new FilterItem("ALL", "全部"));
        data.add(new FilterItem("1", "重点客户"));
        data.add(new FilterItem("0", "普通客户"));
        return data;
    }

    /**
     * 客户筛选之智能排序
     *
     * @return
     */
    public static List<FilterItem> getClientZhiNengFilter() {
        List<FilterItem> data = new ArrayList<>();
        data.add(new FilterItem("Distance", "离我最近"));
        return data;
    }

}
