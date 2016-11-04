package com.salesman.utils;

import android.text.TextUtils;

import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.UploadImageBean;
import com.salesman.entity.UploadPicBean;
import com.studio.jframework.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 上传文件工具类
 * Created by Lihuai on 2016/6/16 0016.
 */
public class UploadFileUtil {
    public static final String TAG = UploadFileUtil.class.getSimpleName();

    private UploadFileListener mListener;

    public UploadFileUtil(UploadFileListener mListener) {
        this.mListener = mListener;
    }

    /**
     * OKHttp文件上传
     *
     * @param data
     */
    public void uploadFile(List<UploadImageBean> data) {
        if (null == data || data.size() <= 0) {
            if (null != mListener) {
                mListener.uploadFileFail();
            }
            return;
        }
        List<File> fileList = new ArrayList<>();
        for (UploadImageBean imageBean : data) {
            if (2 == imageBean.type && !TextUtils.isEmpty(imageBean.imgPath)) {
                File file = new File(imageBean.imgPath);
                fileList.add(file);
            }
        }
        if (fileList.isEmpty()) {
            if (null != mListener) {
                mListener.uploadFileFail();
            }
            return;
        }
        LogUtils.d(TAG, fileList.size() + "");

        PostFormBuilder postFormBuilder = OkHttpUtils.post();
        for (File file : fileList) {
            postFormBuilder.addFile(file.getName(), file.getName(), file);
        }
        postFormBuilder.url(Constant.moduleUploadPic)
                .build()
                .connTimeOut(20000) // 设置超时时间，必须在build()方法之后
                .readTimeOut(20000)
                .writeTimeOut(20000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d(TAG, e.toString());
                        if (null != mListener) {
                            mListener.uploadFileFail();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d(TAG, response);
                        UploadPicBean baseBean = GsonUtils.json2Bean(response, UploadPicBean.class);
                        if (null != baseBean) {
                            if (baseBean.success && baseBean.data != null) {
                                List<UploadPicBean.ImagePath> list = baseBean.data.list;
                                if (null != list && list.size() > 0) {
                                    if (null != mListener) {
                                        mListener.uploadFileSuccess(baseBean.data.list);
                                    }
                                } else {
                                    if (null != mListener) {
                                        mListener.uploadFileFail();
                                    }
                                }
                            } else {
                                if (null != mListener) {
                                    mListener.uploadFileFail();
                                }
                            }
                        }
                    }

                });
    }

    public interface UploadFileListener {
        void uploadFileFail();

        void uploadFileSuccess(List<UploadPicBean.ImagePath> picList);
    }
}
