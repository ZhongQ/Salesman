package com.salesman.entity;

/**
 * 支付方式实体
 * Created by LiHuai on 2016/2/2 0002.
 */
public class PayWayBean {

    public int imgId;
    public String payName;
    public boolean check;

    public PayWayBean(int imgId, String payName, boolean check) {
        this.imgId = imgId;
        this.payName = payName;
        this.check = check;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
