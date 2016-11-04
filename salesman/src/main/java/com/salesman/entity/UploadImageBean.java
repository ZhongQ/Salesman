package com.salesman.entity;

import android.graphics.Bitmap;

/**
 * 上传图片实体
 * Created by LiHuai on 2016/1/26 0026.
 */
public class UploadImageBean {

    public long createTime;// 创建时间
    public Bitmap imgBitmap;

    public int type = 1;// -1有图填充不展示删除按钮;1: 无图片填充；2：有图填充展示删除按钮
    public String picUrl;// 网络图片链接
    public String cameraPath;// 拍照图片缓存路径（原图）
    public String imgPath;// 本地图片保存路径（压缩后）

    public UploadImageBean() {
    }

    public UploadImageBean(int type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getCameraPath() {
        return cameraPath;
    }

    public void setCameraPath(String cameraPath) {
        this.cameraPath = cameraPath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void clear() {
        this.cameraPath = "";
        this.imgPath = "";
    }
}
