package com.salesman.entity;

/**
 * 个人中心列表实体
 * Created by LiHuai on 2016/1/26 0026.
 */
public class PersonalListBean {

    public int id;
    public int imgId;
    public String itemText;
    public boolean isShowLine;// 是否展示下滑线
    public boolean isShowGreyBg;// 是否展示灰色背景
    public boolean isShowRedDot = false;// 是否展示小红点
    public int type;// 布局类型，默认0(暂时无用)

    public PersonalListBean(int id, int imgId, String itemText, boolean isShowLine, boolean isShowGreyBg) {
        this.id = id;
        this.imgId = imgId;
        this.itemText = itemText;
        this.isShowLine = isShowLine;
        this.isShowGreyBg = isShowGreyBg;
        this.isShowRedDot = false;
        this.type = 0;
    }

    public PersonalListBean(int id, int imgId, String itemText, boolean isShowLine, boolean isShowGreyBg, boolean isShowRedDot) {
        this(id, imgId, itemText, isShowLine, isShowGreyBg);
        this.isShowRedDot = isShowRedDot;
    }

    public PersonalListBean(int id, int imgId, String itemText, boolean isShowLine, boolean isShowGreyBg, int type) {
        this(id, imgId, itemText, isShowLine, isShowGreyBg);
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public boolean isShowLine() {
        return isShowLine;
    }

    public void setIsShowLine(boolean isShowLine) {
        this.isShowLine = isShowLine;
    }

    public boolean isShowGreyBg() {
        return isShowGreyBg;
    }

    public void setIsShowGreyBg(boolean isShowGreyBg) {
        this.isShowGreyBg = isShowGreyBg;
    }

    public boolean isShowRedDot() {
        return isShowRedDot;
    }

    public void setIsShowRedDot(boolean isShowRedDot) {
        this.isShowRedDot = isShowRedDot;
    }
}
