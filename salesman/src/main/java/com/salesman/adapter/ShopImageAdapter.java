package com.salesman.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.salesman.R;
import com.salesman.activity.home.BigImageActivity;
import com.salesman.activity.picture.PhotoReviewActivity;
import com.salesman.entity.UploadImageBean;
import com.salesman.utils.FileSizeUtil;
import com.studio.jframework.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户图片适配器
 * Created by LiHuai on 2016/2/19 0019.
 */
public class ShopImageAdapter extends CommonAdapter<UploadImageBean> {
    private DisplayImageOptions options;
    private DeletePicListener mListener;

    public ShopImageAdapter(Context context, List<UploadImageBean> data) {
        super(context, data);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_image)
                .showImageForEmptyUri(R.drawable.default_image)
                .showImageOnFail(R.drawable.default_image)
                .cacheInMemory(true)// 开启内存缓存
                .cacheOnDisk(true) // 开启硬盘缓存
                .resetViewBeforeLoading(false).build();
    }

    public ShopImageAdapter(Context context, List<UploadImageBean> data, DeletePicListener mListener) {
        this(context, data);
        this.mListener = mListener;
    }

    @Override
    public int getCount() {
        return mData.size() < 4 ? mData.size() : 4;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, final int position, final UploadImageBean uploadImageBean) {
        ImageView ivImage = holder.getView(R.id.iv_shop_img);
        ImageView ivDel = holder.getView(R.id.iv_del_pic);
        if (uploadImageBean.type == 2) {
            ivDel.setVisibility(View.VISIBLE);
            showImage(ivImage, uploadImageBean);
        } else if (uploadImageBean.type == 1) {
            ivDel.setVisibility(View.GONE);
            ivImage.setImageResource(R.drawable.select_pic_icon);
        } else if (uploadImageBean.type == -1) {
            ivDel.setVisibility(View.GONE);
            showImage(ivImage, uploadImageBean);
        }
        // 删除按钮
        ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.deletePic(uploadImageBean);
                } else {
                    List<UploadImageBean> tempList = new ArrayList<>();
                    mData.remove(uploadImageBean);
                    for (UploadImageBean bean : mData) {
                        if (bean.type == 2) {
                            tempList.add(bean);
                        }
                    }
                    if (tempList.size() < 4) {
                        tempList.add(new UploadImageBean(1));
                    }
                    mData.clear();
                    mData.addAll(tempList);
                    notifyDataSetChanged();
                    FileSizeUtil.deleteFile(uploadImageBean.imgPath);
                }
            }
        });
    }

    /**
     * 展示图片
     *
     * @param ivImage
     * @param uploadImageBean
     */
    private void showImage(ImageView ivImage, UploadImageBean uploadImageBean) {
        if (!TextUtils.isEmpty(uploadImageBean.picUrl)) {
            ImageLoader.getInstance().displayImage(uploadImageBean.picUrl, ivImage, options);
        } else if (!TextUtils.isEmpty(uploadImageBean.cameraPath)) {
            Bitmap ivBitmap = BitmapUtils.getPicBitmap(uploadImageBean.cameraPath, 100, 100);
            ivImage.setImageBitmap(ivBitmap);
        }
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_image_shop;
    }

    public interface DeletePicListener {
        void deletePic(UploadImageBean imageBean);
    }
}
