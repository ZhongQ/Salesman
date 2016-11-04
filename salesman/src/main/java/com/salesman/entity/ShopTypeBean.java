package com.salesman.entity;

/**
 * 商铺类型列表实体
 * Created by LiHuai on 2016/2/2 0002.
 */
public class ShopTypeBean {
    public String typeName;
    public boolean checked;

    public String label;
    public String value;

    public ShopTypeBean(String label, boolean checked) {
        this.label = label;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
