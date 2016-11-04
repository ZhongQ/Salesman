package com.salesman.entity;

/**
 * 客户详情ListView实体
 * Created by LiHuai on 2016/6/20 0020.
 */
public class ClientDetailBean {
    public String key;
    public String value;
    public boolean isShowBg;

    public ClientDetailBean(String key, String value) {
        this.key = key;
        this.value = value;
        this.isShowBg = false;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isShowBg() {
        return isShowBg;
    }

    public void setIsShowBg(boolean isShowBg) {
        this.isShowBg = isShowBg;
    }
}
