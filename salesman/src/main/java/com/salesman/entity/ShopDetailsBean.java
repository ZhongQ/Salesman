package com.salesman.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by LiHuai on 2016/1/31 0031.
 */
public class ShopDetailsBean extends BaseBean implements Serializable {
    public static final String      USERID             =        "userId";
    public static final String      REMARKS            =        "remarks";
    public static final String      CREATEBY           =        "createBy";
    public static final String      CREATETIME         =        "createTime";
    public static final String      UPDATEBY           =        "updateBy";
    public static final String      UPDATETIME         =        "updateTime";
    public static final String      SHOPID             =        "shopId";
    public static final String      GROUPID            =        "groupId";
    public static final String      SHOPNO             =        "shopNo";
    public static final String      SHOPNAME           =        "shopName";
    public static final String      TELEPHONE          =        "telephone";
    public static final String      PROVINCE           =        "province";
    public static final String      CITY               =        "city";
    public static final String      AREA               =        "area";
    public static final String      PROVINCEID         =        "provinceId";
    public static final String      AREAID             =        "areaId";
    public static final String      CITYID             =        "cityId";
    public static final String      SHOPADDRESS        =        "shopAddress";
    public static final String      SHOPTYPE           =        "shopType";
    public static final String      SHOPTYPENAME       =        "shopTypeName";
    public static final String      SHOPAREA           =        "shopArea";
    public static final String      SKU                =        "sku";
    public static final String      STAFFNUM           =        "staffNum";
    public static final String      DISTRIBUTIONNUM    =        "distributionNum";
    public static final String      TURNOVER           =        "turnover";
    public static final String      MAINPRODUCT        =        "mainProduct";
    public static final String      DCSHOP             =        "dcShop";
    public static final String      IPAY               =        "ipay";
    public static final String      OTHERPATFORM       =        "otherPatform";
    public static final String      STARTSHOPHOURS     =        "startShopHours";
    public static final String      ENDSHOPHOURS       =        "endShopHours";
    public static final String      BOSSNAME           =        "bossName";
    public static final String      BOSSTEL            =        "bossTel";
    public static final String      SALESMANID         =        "salesmanId";
    public static final String      SALESMANNAME       =        "salesmanName";
    public static final String      SALERATIO          =        "saleRatio";
    public static final String      ISMULTIPLESHOP     =        "isMultipleShop";
    public static final String      BACCYLICENCE       =        "baccyLicence";
    public static final String      ISPOS              =        "isPos";
    public static final String      ISCOMPUTER         =        "isComputer";
    public static final String      ISWIFI             =        "isWifi";
    public static final String      ISREGISTER         =        "isRegister";
    public static final String      REGISTERTEL        =        "registerTel";
    public static final String      SHOPLICENSE        =        "shopLicense";
    public static final String      LONGITUDE          =        "longitude";
    public static final String      LATITUDE           =        "latitude";
    public static final String      SPGROUPNAME        =        "spGroupName";
    public static final String      LINENAME           =        "lineName";

    /**
     * resultObj : {"userId":"ff80808151e6527a01521f64daf80ea7","userName":"元宝","remarks":"备注","createBy":"ff80808151e6527a01521f64daf80ea7","createTime":"2016-01-27 19:30:25.0","updateBy":"ff80808151e6527a01521f64daf80ea7","updateTime":"2016-01-29 14:30:46.0","shopId":"fd621e426cae41a4b91313e5ba350a24","shopNo":"龙贤","shopName":"苏宁易购-8889","telephone":"18129897639","provinceId":"广东","cityId":"深圳","areaId":"南山区","shopAdderss":"科技园腾讯大厦101","shopLicense":"老板","shopType":"街边店铺","isMultipleShop":1,"shopArea":120,"sku":1,"staffNum":1,"distributionNum":56,"turnover":888,"mainProduct":"水果、酒水","saleRatio":0.5,"dcShop":"5","baccyLicence":1,"isPos":1,"isComputer":1,"isWifi":0,"ipay":"微信、支付宝、财付通","otherPatform":"1","startShopHours":"9：30","endShopHours":"22：30","proprietorList":[{"id":"7b9700eb9d834153aa72daaa0bf3aacf","createTime":"2016-01-28 14:49:00.0","name":"张三","mobile":"13510288098","sex":1,"age":28,"nativePlace":"中国","post":"老板","shopId":"fd621e426cae41a4b91313e5ba350a24"},{"id":"9fe23415ea844f028d6264e74c94738a","createTime":"2016-01-28 14:49:00.0","name":"李四","mobile":"1234567","sex":2,"age":33,"nativePlace":"美国","post":"投资人","shopId":"fd621e426cae41a4b91313e5ba350a24"}]}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * userId : ff80808151e6527a01521f64daf80ea7
         * userName : 元宝
         * remarks : 备注
         * createBy : ff80808151e6527a01521f64daf80ea7
         * createTime : 2016-01-27 19:30:25.0
         * updateBy : ff80808151e6527a01521f64daf80ea7
         * updateTime : 2016-01-29 14:30:46.0
         * shopId : fd621e426cae41a4b91313e5ba350a24
         * shopNo : 龙贤
         * shopName : 苏宁易购-8889
         * telephone : 18129897639
         * provinceId : 广东
         * cityId : 深圳
         * areaId : 南山区
         * shopAdderss : 科技园腾讯大厦101
         * shopLicense : 老板
         * shopType : 街边店铺
         * isMultipleShop : 1
         * shopArea : 120
         * sku : 1
         * staffNum : 1
         * distributionNum : 56
         * turnover : 888
         * mainProduct : 水果、酒水
         * saleRatio : 0.5
         * dcShop : 5
         * baccyLicence : 1
         * isPos : 1
         * isComputer : 1
         * isWifi : 0
         * ipay : 微信、支付宝、财付通
         * otherPatform : 1
         * startShopHours : 9：30
         * endShopHours : 22：30
         * proprietorList : [{"id":"7b9700eb9d834153aa72daaa0bf3aacf","createTime":"2016-01-28 14:49:00.0","name":"张三","mobile":"13510288098","sex":1,"age":28,"nativePlace":"中国","post":"老板","shopId":"fd621e426cae41a4b91313e5ba350a24"},{"id":"9fe23415ea844f028d6264e74c94738a","createTime":"2016-01-28 14:49:00.0","name":"李四","mobile":"1234567","sex":2,"age":33,"nativePlace":"美国","post":"投资人","shopId":"fd621e426cae41a4b91313e5ba350a24"}]
         */

        public ShopDetail resultObj;
    }

    public static class ProprietorBean implements Parcelable {
        public String id;
        public String createTime;
        public String name;
        public String mobile;
        public int sex;
        public int age;
        public String nativePlace;
        public String post;
        public String shopId;

        public ProprietorBean(Parcel in) {
            id = in.readString();
            createTime = in.readString();
            name = in.readString();
            mobile = in.readString();
            sex = in.readInt();
            age = in.readInt();
            nativePlace = in.readString();
            post = in.readString();
            shopId = in.readString();
        }

        public static final Creator<ProprietorBean> CREATOR = new Creator<ProprietorBean>() {
            @Override
            public ProprietorBean createFromParcel(Parcel in) {
                return new ProprietorBean(in);
            }

            @Override
            public ProprietorBean[] newArray(int size) {
                return new ProprietorBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(createTime);
            dest.writeString(name);
            dest.writeString(mobile);
            dest.writeInt(sex);
            dest.writeInt(age);
            dest.writeString(nativePlace);
            dest.writeString(post);
            dest.writeString(shopId);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getNativePlace() {
            return nativePlace;
        }

        public void setNativePlace(String nativePlace) {
            this.nativePlace = nativePlace;
        }

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String toString() {
            return name + "," + mobile + "," + sex + "," + age + "," + nativePlace + "," + post + "]";
        }
    }
}
