package com.salesman.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 单选实体
 * Created by LiHuai on 2016/6/24 0024.
 */
public class SingleSelectionBean implements Parcelable {
    public String id;
    public String idSecond;
    public String idThird;
    public String name;
    /*线路选择*/
    public String nameNd;
    /*item布局类型*/
    public int itemType;

    public boolean isSelect;

    public SingleSelectionBean(String id, String name) {
        this.id = id;
        this.name = name;
        this.isSelect = false;
    }

    public SingleSelectionBean(String id, String idSecond, String idThird, String name) {
        this(id, name);
        this.idSecond = idSecond;
        this.idThird = idThird;
    }

    public SingleSelectionBean(String id, String name, String nameNd, int itemType) {
        this(id, name);
        this.nameNd = nameNd;
        this.itemType = itemType;
    }

    public SingleSelectionBean(String id, String name, String nameNd) {
        this(id, name);
        this.nameNd = nameNd;
    }

    protected SingleSelectionBean(Parcel in) {
        id = in.readString();
        idSecond = in.readString();
        idThird = in.readString();
        name = in.readString();
        nameNd = in.readString();
        itemType = in.readInt();
        isSelect = in.readByte() != 0;
    }

    public static final Creator<SingleSelectionBean> CREATOR = new Creator<SingleSelectionBean>() {
        @Override
        public SingleSelectionBean createFromParcel(Parcel in) {
            return new SingleSelectionBean(in);
        }

        @Override
        public SingleSelectionBean[] newArray(int size) {
            return new SingleSelectionBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(idSecond);
        dest.writeString(idThird);
        dest.writeString(name);
        dest.writeString(nameNd);
        dest.writeInt(itemType);
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }
}
