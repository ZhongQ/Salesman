package com.salesman.entity;

/**
 * 公告发布指定对象实体
 * Created by LiHuai on 2016/5/26 0026.
 */
public class NoticeReleaseObj {
    public int id;
    public String name;
    public boolean isSelect;
    public boolean isShowSingleBtn;

    public NoticeReleaseObj(int id, String name) {
        this.id = id;
        this.name = name;
        this.isSelect = false;
        this.isShowSingleBtn = true;
    }

    public NoticeReleaseObj(int id, String name, boolean isShowSingleBtn) {
        this.id = id;
        this.name = name;
        this.isShowSingleBtn = isShowSingleBtn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
