package com.salesman.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 客户详情
 * Created by LiHuai on 2016/7/14 0014.
 */
public class ShopDetail implements Parcelable {
    public String userId;
    public int storeId;
    public String remarks;
    public String createBy;
    public String createTime;
    public String updateBy;
    public String updateTime;
    public String shopId;
    public String groupId;
    public String shopNo;
    public String shopName;
    public String telephone;
    public String province;
    public String city;
    public String area;
    public int provinceId;
    public int areaId;
    public int cityId;
    public String shopAddress;
    public String shopType;// 店铺id
    public String shopTypeName;// 店铺类型名称
    public String shopArea;
    public String sku;
    public String staffNum;
    public String distributionNum;
    public String turnover;
    public String mainProduct;
    public String dcShop;
    public String ipay;
    public String otherPatform;
    public String startShopHours;
    public String endShopHours;
    public String bossName;
    public String bossTel;
    public String salesmanId;
    public String salesmanName;
    public String salesmanTel;
    public String saleRatio;
    public String isMultipleShop;
    public String baccyLicence;
    public String isPos;
    public String isComputer;
    public String isWifi;
    public String isRegister;
    public String registerTel;
    public String shopLicense;
    public String spGroupName;
    public String lineName;
    public double longitude;
    public double latitude;
    public List<String> picList;

    public ShopDetail() {
    }


    protected ShopDetail(Parcel in) {
        userId = in.readString();
        storeId = in.readInt();
        remarks = in.readString();
        createBy = in.readString();
        createTime = in.readString();
        updateBy = in.readString();
        updateTime = in.readString();
        shopId = in.readString();
        groupId = in.readString();
        shopNo = in.readString();
        shopName = in.readString();
        telephone = in.readString();
        province = in.readString();
        city = in.readString();
        area = in.readString();
        provinceId = in.readInt();
        areaId = in.readInt();
        cityId = in.readInt();
        shopAddress = in.readString();
        shopType = in.readString();
        shopTypeName = in.readString();
        shopArea = in.readString();
        sku = in.readString();
        staffNum = in.readString();
        distributionNum = in.readString();
        turnover = in.readString();
        mainProduct = in.readString();
        dcShop = in.readString();
        ipay = in.readString();
        otherPatform = in.readString();
        startShopHours = in.readString();
        endShopHours = in.readString();
        bossName = in.readString();
        bossTel = in.readString();
        salesmanId = in.readString();
        salesmanName = in.readString();
        salesmanTel = in.readString();
        saleRatio = in.readString();
        isMultipleShop = in.readString();
        baccyLicence = in.readString();
        isPos = in.readString();
        isComputer = in.readString();
        isWifi = in.readString();
        isRegister = in.readString();
        registerTel = in.readString();
        shopLicense = in.readString();
        spGroupName = in.readString();
        lineName = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        picList = in.createStringArrayList();
    }

    public static final Creator<ShopDetail> CREATOR = new Creator<ShopDetail>() {
        @Override
        public ShopDetail createFromParcel(Parcel in) {
            return new ShopDetail(in);
        }

        @Override
        public ShopDetail[] newArray(int size) {
            return new ShopDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeInt(storeId);
        dest.writeString(remarks);
        dest.writeString(createBy);
        dest.writeString(createTime);
        dest.writeString(updateBy);
        dest.writeString(updateTime);
        dest.writeString(shopId);
        dest.writeString(groupId);
        dest.writeString(shopNo);
        dest.writeString(shopName);
        dest.writeString(telephone);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(area);
        dest.writeInt(provinceId);
        dest.writeInt(areaId);
        dest.writeInt(cityId);
        dest.writeString(shopAddress);
        dest.writeString(shopType);
        dest.writeString(shopTypeName);
        dest.writeString(shopArea);
        dest.writeString(sku);
        dest.writeString(staffNum);
        dest.writeString(distributionNum);
        dest.writeString(turnover);
        dest.writeString(mainProduct);
        dest.writeString(dcShop);
        dest.writeString(ipay);
        dest.writeString(otherPatform);
        dest.writeString(startShopHours);
        dest.writeString(endShopHours);
        dest.writeString(bossName);
        dest.writeString(bossTel);
        dest.writeString(salesmanId);
        dest.writeString(salesmanName);
        dest.writeString(salesmanTel);
        dest.writeString(saleRatio);
        dest.writeString(isMultipleShop);
        dest.writeString(baccyLicence);
        dest.writeString(isPos);
        dest.writeString(isComputer);
        dest.writeString(isWifi);
        dest.writeString(isRegister);
        dest.writeString(registerTel);
        dest.writeString(shopLicense);
        dest.writeString(spGroupName);
        dest.writeString(lineName);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeStringList(picList);
    }
}
