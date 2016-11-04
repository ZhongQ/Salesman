package com.salesman.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.salesman.activity.home.BigImageActivity;
import com.salesman.activity.picture.PhotoReviewActivity;
import com.salesman.adapter.ShopImageAdapter;
import com.salesman.common.Constant;
import com.salesman.entity.UploadImageBean;
import com.salesman.entity.UploadPicBean;
import com.studio.jframework.utils.BitmapUtils;
import com.studio.jframework.utils.FileUtils;
import com.studio.jframework.utils.LogUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 图片工具类
 * Created by LiHuai on 2016/6/12 0012.
 */
public class PictureUtil {
    private static final String TAG = PictureUtil.class.getSimpleName();
    private Context mContext;
    private List<UploadImageBean> uploadImgList;
    private ShopImageAdapter imgAdpter;

    public PictureUtil(Context mContext, List<UploadImageBean> uploadImgList, ShopImageAdapter imgAdpter) {
        this.mContext = mContext;
        this.uploadImgList = uploadImgList;
        this.imgAdpter = imgAdpter;
    }

    /**
     * 拍照后图片处理
     */
    public void picDispose(String imgPath) {
        LogUtils.d(TAG, imgPath);
        if (TextUtils.isEmpty(imgPath)) {
            Toast.makeText(mContext, "图片获取失败", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(imgPath);
        if (file.exists()) {
            Bitmap ivBitmap = BitmapUtils.getPicBitmap(imgPath, Constant.UPLOAD_WIDTH, Constant.UPLOAD_HEIGHT);
            FileUtils fileUtils = new FileUtils(mContext, 2, Constant.APP_FOLDER);
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            // 照片命名
            String picName = timeStamp + ".jpg";
            if (ivBitmap == null) {
                Toast.makeText(mContext, "图片获取失败", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!fileUtils.saveBitmap(picName, ivBitmap)) {
                ivBitmap.recycle();
                Toast.makeText(mContext, "图片获取失败", Toast.LENGTH_SHORT).show();
                return;
            }
            ivBitmap.recycle();
            UploadImageBean imgBean = new UploadImageBean(2);
            imgBean.setCameraPath(imgPath);
            imgBean.setImgPath(fileUtils.getAppFolderPath() + picName);
            imgBean.setCreateTime(System.currentTimeMillis());
            listSort(imgBean);
        } else {
            Toast.makeText(mContext, "图片获取失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 相册选择照片后处理
     *
     * @param uri
     */
    public void pickPhoto(Uri uri) {
        if (TextUtils.isEmpty(uri.toString())) {
            return;
        }
        String[] proj = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,};// 指定所要查询的字段
        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
        String imgPath = "";
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);// 字段所对应的列数
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                imgPath = cursor.getString(column_index);
            }
            cursor.close();
        }
        picDispose(imgPath);
    }

    /**
     * 图片集合排序
     */
    public void listSort(UploadImageBean bean) {
        if (null == uploadImgList || null == imgAdpter) {
            Toast.makeText(mContext, "图片获取失败", Toast.LENGTH_SHORT).show();
            return;
        }
        int size = uploadImgList.size();
        if (1 == size) {
            uploadImgList.clear();
            uploadImgList.add(bean);
            uploadImgList.add(new UploadImageBean(1));
        } else if (size > 1 && size <= 3) {
            uploadImgList.remove(size - 1);
            uploadImgList.add(bean);
            uploadImgList.add(new UploadImageBean(1));
        } else if (size == 4) {
            uploadImgList.remove(size - 1);
            uploadImgList.add(bean);
        }
        imgAdpter.setData(uploadImgList);
    }

    /**
     * 图片是否失效
     *
     * @param list
     */
    public boolean isPictureUsable(List<UploadImageBean> list) {
        if (null == list || list.size() <= 0) {
            ToastUtil.show(mContext, "请拍照");
            return false;
        }
        boolean flag = false;
        for (UploadImageBean imageBean : list) {
            if (2 == imageBean.type) {
                flag = true;
            }
        }
        if (!flag) {
            ToastUtil.show(mContext, "请拍照");
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            UploadImageBean bean = list.get(i);
            if (2 == bean.type) {
                long nowTime = System.currentTimeMillis();
                long timeInterval = nowTime - bean.createTime;
                if (timeInterval > 1000 * 60 * 5) {
                    ToastUtil.show(mContext, "图片已失效请重新拍摄");
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 是否需要上传图片
     *
     * @param list
     * @return
     */
    public static boolean isPictureUpload(List<UploadImageBean> list) {
        if (null == list || list.size() <= 0) {
            return false;
        }
        boolean flag = false;
        for (UploadImageBean imageBean : list) {
            if (2 == imageBean.type) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 拼接上传图片链接
     *
     * @param data
     * @return
     */
    public static String sliceUploadPicString(List<UploadPicBean.ImagePath> data) {
        if (null == data || data.isEmpty()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (UploadPicBean.ImagePath imagePath : data) {
            sb.append(imagePath.toString());
        }
        return sb.toString();
    }

    /**
     * 获取网络图片的url
     *
     * @param list
     * @return
     */
    public static List<String> getPicUrlList(List<UploadImageBean> list) {
        List<String> data = new ArrayList<>();
        if (null == list || list.isEmpty()) {
            return data;
        }
        for (UploadImageBean imageBean : list) {
            if (imageBean.type == 2 && !TextUtils.isEmpty(imageBean.picUrl)) {
                data.add(imageBean.picUrl);
            }
        }
        return data;
    }

    /**
     * 拼接所有网络图片url字符串
     *
     * @param list
     * @return
     */
    public static String sliceNetPicString(List<UploadImageBean> list) {
        StringBuffer sb = new StringBuffer();
        if (null == list || list.isEmpty()) {
            return sb.toString();
        }
        for (UploadImageBean imageBean : list) {
            if (imageBean.type == 2 && !TextUtils.isEmpty(imageBean.picUrl)) {
                sb.append(imageBean.picUrl);
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * 查看网络大图
     *
     * @param context
     * @param position
     * @param list
     */
    public static void lookBigPicFromNet(Context context, int position, List<UploadImageBean> list) {
        if (null != list && !list.isEmpty()) {
            ArrayList<String> imgs = new ArrayList<>();
            for (UploadImageBean upBean : list) {
                if (!TextUtils.isEmpty(upBean.picUrl)) {
                    imgs.add(upBean.picUrl);
                }
            }
            Intent photoIntent = new Intent(context, PhotoReviewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("Imgs", imgs);
            bundle.putInt("Position", position);
            photoIntent.putExtras(bundle);
            context.startActivity(photoIntent);
        }
    }

    public static void lookBigPicFromNet2(Context context, int position, List<String> list) {
        if (null != list && !list.isEmpty()) {
            ArrayList<String> imgs = new ArrayList<>();
            for (String s : list) {
                if (!TextUtils.isEmpty(s)) {
                    imgs.add(s);
                }
            }
            Intent photoIntent = new Intent(context, PhotoReviewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("Imgs", imgs);
            bundle.putInt("Position", position);
            photoIntent.putExtras(bundle);
            context.startActivity(photoIntent);
        }
    }

    /**
     * 查看本地大图
     *
     * @param context
     * @param position
     * @param list
     */
    public static void lookBigPicFromLocal(Context context, int position, List<UploadImageBean> list) {
        if (null != list && !list.isEmpty()) {
            ArrayList<String> imgs = new ArrayList<>();
            for (UploadImageBean upBean : list) {
                if (upBean.type == 2 && !TextUtils.isEmpty(upBean.cameraPath)) {
                    imgs.add(upBean.getCameraPath());
                }
            }
            Intent bigIntent = new Intent(context, BigImageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("Imgs", imgs);
            bundle.putInt("Position", position);
            bigIntent.putExtras(bundle);
            context.startActivity(bigIntent);
        }
    }
}
