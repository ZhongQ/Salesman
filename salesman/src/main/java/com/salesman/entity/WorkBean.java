package com.salesman.entity;

/**
 * 工作实体
 * Created by LiHuai on 2016/7/4 0004.
 */
public class WorkBean {

    public int id;
    public String itemName;
    public int imgResource;

    public WorkBean(int id, String itemName, int imgResource) {
        this.id = id;
        this.itemName = itemName;
        this.imgResource = imgResource;
    }
}
