package com.salesman.views.popupwindow;

/**
 * 筛选实体
 * Created by LiHuai on 2016/6/27 0027.
 */
public class FilterItem {
    public String id;
    public String name;

    public String idNd;
    public String nameNd;

    public boolean isSelect = false;

    public FilterItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public FilterItem(String id, String name, String idNd, String nameNd) {
        this.id = id;
        this.name = name;
        this.idNd = idNd;
        this.nameNd = nameNd;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
