package com.salesman.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.salesman.R;
import com.salesman.activity.picture.PhotoReviewActivity;
import com.salesman.entity.SigninRecordListBean;
import com.salesman.entity.UploadImageBean;
import com.salesman.utils.ReplaceSymbolUtil;
import com.studio.jframework.widget.InnerGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 签到记录列表适配器
 * Created by LiHuai on 2016/1/27 0027.
 */
public class SigninRecordListAdapter extends CommonAdapter<SigninRecordListBean.SigninBean> {

    private DisplayImageOptions options;

    public SigninRecordListAdapter(Context context, List<SigninRecordListBean.SigninBean> data) {
        super(context, data);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_image)
                .showImageForEmptyUri(R.drawable.default_image)
                .showImageOnFail(R.drawable.default_image)
                .cacheInMemory(true)// 开启内存缓存
                .cacheOnDisk(true) // 开启硬盘缓存
                .resetViewBeforeLoading(false).build();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, final SigninRecordListBean.SigninBean signinBean) {
        ImageView ivSignin = holder.getView(R.id.iv_signin);
        final String picUrl = signinBean.picUrl;
        ImageLoader.getInstance().displayImage(picUrl, ivSignin, options);
        // 图片
        InnerGridView gridView = holder.getView(R.id.gv_signin_record);
        final List<UploadImageBean> imgList = new ArrayList<>();
        ShopImageAdapter adapter = new ShopImageAdapter(mContext, imgList);
        for (String s : signinBean.picList) {
            UploadImageBean uploadImageBean = new UploadImageBean(-1);
            uploadImageBean.setPicUrl(s);
            imgList.add(uploadImageBean);
        }
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (imgList.size() > 0) {
                    ArrayList<String> imgs = new ArrayList<>();
                    for (UploadImageBean upBean : imgList) {
                        if (!TextUtils.isEmpty(upBean.picUrl)) {
                            imgs.add(upBean.picUrl);
                        }
                    }
                    Intent photoIntent = new Intent(mContext, PhotoReviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("Imgs", imgs);
                    bundle.putInt("Position", position);
                    photoIntent.putExtras(bundle);
                    mContext.startActivity(photoIntent);
                }
            }
        });

        ((TextView) holder.getView(R.id.tv_signin_type)).setText(signinBean.typeName);
        ((TextView) holder.getView(R.id.tv_signin_address)).setText(signinBean.address);
        ((TextView) holder.getView(R.id.tv_signin_time)).setText(signinBean.createTime + " " + signinBean.signTime);
        TextView tvRemark = holder.getView(R.id.tv_signin_remark);
        if (TextUtils.isEmpty(signinBean.remarks)) {
            tvRemark.setVisibility(View.GONE);
        } else {
            tvRemark.setVisibility(View.VISIBLE);
            tvRemark.setText(ReplaceSymbolUtil.reverseReplaceHuanHang(signinBean.remarks));
        }
        if (3 == signinBean.type) {
            StringBuffer sb = new StringBuffer();
            if (!TextUtils.isEmpty(signinBean.visitLine)) {
                sb.append(signinBean.visitLine);
                sb.append("，");
            }
            if (!TextUtils.isEmpty(signinBean.visitCust)) {
                sb.append(signinBean.visitCust);
            }
            if (!TextUtils.isEmpty(sb)) {
                holder.setVisibility(R.id.tv_client_line_signin, View.VISIBLE);
                holder.setTextByString(R.id.tv_client_line_signin, sb);
            } else {
                holder.setVisibility(R.id.tv_client_line_signin, View.GONE);
            }
            if (!TextUtils.isEmpty(signinBean.markType)) {
                holder.setVisibility(R.id.tv_enter_leave_store, View.VISIBLE);
                holder.setVisibility(R.id.tv_enter_leave_store, View.VISIBLE);
                holder.setTextByString(R.id.tv_enter_leave_store, "(" + signinBean.markType + ")");
            } else {
                holder.setVisibility(R.id.tv_enter_leave_store, View.GONE);
            }
        } else {
            holder.setVisibility(R.id.tv_client_line_signin, View.GONE);
            holder.setVisibility(R.id.tv_enter_leave_store, View.GONE);
        }

        ivSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> imgsList = new ArrayList<>();
                imgsList.add(picUrl);
                Intent intent = new Intent(mContext, PhotoReviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("Imgs", imgsList);
                bundle.putInt("Position", 0);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_signin;
    }
}
