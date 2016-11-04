package com.salesman.utils;

import com.salesman.application.SalesManApplication;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 本地图片工具类
 * Created by LiHuai on 2016/2/21.
 */
public class LocalImageHelper {
    private static LocalImageHelper instance;
    private final SalesManApplication context;
    //拍照时指定保存图片的路径
    private String CameraImgPath;

    public static LocalImageHelper getInstance() {
        return instance;
    }

    private LocalImageHelper(SalesManApplication context) {
        this.context = context;
    }

    public static void init(SalesManApplication context) {
        instance = new LocalImageHelper(context);
    }

    public String setCameraImgPath() {
        String folder = SalesManApplication.getInstance().getCachePath() + "/PostPicture/";
        File savedir = new File(folder);
        if (!savedir.exists()) {
            savedir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // 照片命名
        String picName = timeStamp + ".jpg";
        //  裁剪头像的绝对路径
        CameraImgPath = folder + picName;
        return CameraImgPath;
    }

    public String getCameraImgPath() {
        return CameraImgPath;
    }
}
