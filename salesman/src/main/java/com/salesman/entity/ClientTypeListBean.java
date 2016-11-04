package com.salesman.entity;

import java.util.List;

/**
 * 客户类型列表实体
 * Created by LiHuai on 2016/6/27 0027.
 */
public class ClientTypeListBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        /**
         * label : 村口店
         * value : 1
         */

        public List<ShopTypeBean> list;
    }
}
