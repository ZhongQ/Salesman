package com.salesman.activity.manage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.picture.PhotoReviewActivity;
import com.salesman.adapter.ShopImageAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.entity.NoticeDetailBean;
import com.salesman.entity.UploadImageBean;
import com.salesman.network.BaseHelper;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.ToastUtil;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.JsonUtils;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.InnerGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 公告详情界面
 * Created by LiHuai on 2016/5/25.
 */
public class NoticeDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final String TAG = NoticeDetailActivity.class.getSimpleName();

    private TextView tvSender, tvPost, tvTime, tvSubject;
    private TextView tvContent;
    private String noticeId = "";
    // 图片
    private InnerGridView gridView;
    private List<UploadImageBean> imgList = new ArrayList<>();
    private ShopImageAdapter imgAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_notice_detail);

        getNoticeDetaiil();
    }

    @Override
    protected void initView() {
        noticeId = getIntent().getStringExtra("noticeId");
        TextView ivBack = (TextView) findViewById(R.id.tv_top_left);
        ivBack.setOnClickListener(this);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.notice_detail);

        tvSender = (TextView) findViewById(R.id.tv_title_notice);
        tvPost = (TextView) findViewById(R.id.tv_sender_notice);
        tvTime = (TextView) findViewById(R.id.tv_time_notice);
        tvSubject = (TextView) findViewById(R.id.tv_title_notice_detail);
        tvContent = (TextView) findViewById(R.id.tv_content_notice_detail);
        gridView = (InnerGridView) findViewById(R.id.gv_notice_detail);
        imgAdpter = new ShopImageAdapter(this, imgList);
        gridView.setAdapter(imgAdpter);
        gridView.setOnItemClickListener(this);
    }

    /**
     * 获取公告详情
     */
    private void getNoticeDetaiil() {
        showProgressDialog(getString(R.string.loading1), false);
        String url = Constant.moduleNoticeDetail;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("noticeId", noticeId);
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                NoticeDetailBean noticeDetailBean = JsonUtils.parseToObject(NoticeDetailBean.class, response);
                if (null != noticeDetailBean) {
                    if (noticeDetailBean.success && noticeDetailBean.data != null && null != noticeDetailBean.data.resultObj) {
                        initPic(noticeDetailBean.data.resultObj.picList);
                        tvSender.setText(noticeDetailBean.data.resultObj.userName);
                        tvPost.setText(noticeDetailBean.data.resultObj.deptName + "     " + noticeDetailBean.data.resultObj.postName);
                        tvTime.setText(noticeDetailBean.data.resultObj.createTime);
                        tvSubject.setText(noticeDetailBean.data.resultObj.subject);
                        tvContent.setText(ReplaceSymbolUtil.reverseReplaceHuanHang(noticeDetailBean.data.resultObj.content));
                    } else {
                        ToastUtil.show(NoticeDetailActivity.this, noticeDetailBean.msg);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
    }

    /**
     * 初始化图片
     */
    private void initPic(List<String> strList) {
        if (strList == null || strList.size() < 1) {
            gridView.setVisibility(View.GONE);
            return;
        }
        gridView.setVisibility(View.VISIBLE);
        imgList.clear();
        for (String s : strList) {
            UploadImageBean uploadImageBean = new UploadImageBean(-1);
            uploadImageBean.setPicUrl(s);
            imgList.add(uploadImageBean);
        }
        imgAdpter.setData(imgList);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != imgList && imgList.size() > 0) {
            ArrayList<String> imgs = new ArrayList<>();
            for (UploadImageBean upBean : imgList) {
                if (!TextUtils.isEmpty(upBean.picUrl)) {
                    imgs.add(upBean.picUrl);
                }
            }
            Intent photoIntent = new Intent(this, PhotoReviewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("Imgs", imgs);
            bundle.putInt("Position", position);
            photoIntent.putExtras(bundle);
            startActivity(photoIntent);
        }
    }
}
