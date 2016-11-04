package com.salesman.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.picture.PhotoReviewActivity;
import com.salesman.adapter.DailyDetailsCommentAdapter;
import com.salesman.adapter.DailyDetailsListAdapter2;
import com.salesman.adapter.ShopImageAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.DailyCommentListBean;
import com.salesman.entity.DailyDetailsBean;
import com.salesman.entity.UploadImageBean;
import com.salesman.network.BaseHelper;
import com.salesman.utils.DailyUtil;
import com.salesman.utils.DialogUtil;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.StaticData;
import com.salesman.utils.StringUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserInfoPreference;
import com.salesman.views.CircleHeadView;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.InnerGridView;
import com.studio.jframework.widget.listview.InnerListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 日报详情界面
 * Created by LiHuai on 2016/3/25.
 */
public class DailyDetailsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, DialogUtil.DialogOnClickListener, DailyUtil.OnDeleteListener {
    public static final String TAG = DailyDetailsActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private TextView tvBack, tvTitle;

    private TextView tvLogName;
    private List<Integer> circleList = StaticData.getCircleColorList();
    //    private ImageView ivHead;
//    private TextView tvNameShort;
    private CircleHeadView circleHeadView;
    private TextView tvNameFull, tvTime, tvDeptPost, tvTypeLog;
    private TextView tvRemark;
    private TextView tvCommentHead;
    private ScrollView scrollView;
    private InnerListView lvDaily;
    private InnerListView lvComment;
    private DailyDetailsListAdapter2 adapterDaily;
    private DailyDetailsCommentAdapter adapterComment;
    private List<DailyDetailsBean.FieldBean> mDatasDaily = new ArrayList<>();
    private List<DailyCommentListBean.CommentBean> mDatasComment = new ArrayList<>();
    // 图片
    private InnerGridView gridView;
    private List<UploadImageBean> uploadImgList = new ArrayList<>();
    private ShopImageAdapter imgAdpter;
    // 评论
    private EditText etComment;
    private TextView tvSend;
    private String reportId = "";// 日报Id
    private String postId = "";// 被回复人Id
    //    private boolean isCommentSuccess = false;
    private DailyCommentListBean.CommentBean commentBean = null;    // 被操作的评论实体
    // Dialog
    private DialogUtil dialogUtil;
    private DailyUtil dailyUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_daily_details);
    }

    @Override
    protected void initView() {
        reportId = getIntent().getStringExtra("reportId");
        tvBack = (TextView) findViewById(R.id.tv_top_left);
        tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.report_details);
        TextView tvRight = (TextView) findViewById(R.id.tv_top_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("");
        tvRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.more_home_icon, 0);
        tvLogName = (TextView) findViewById(R.id.tv_log_name);
        circleHeadView = (CircleHeadView) findViewById(R.id.cir_head_daily_detail);
        tvNameFull = (TextView) findViewById(R.id.tv_name_full);
        tvTime = (TextView) findViewById(R.id.tv_time_daily);
        tvTypeLog = (TextView) findViewById(R.id.tv_type_log);
        tvRemark = (TextView) findViewById(R.id.tv_remark_daily);
        tvCommentHead = (TextView) findViewById(R.id.tv_comment_haed);
        tvDeptPost = (TextView) findViewById(R.id.tv_dept_post);
        lvDaily = (InnerListView) findViewById(R.id.lv_daily_details);
        lvComment = (InnerListView) findViewById(R.id.lv_comment_daily);
        gridView = (InnerGridView) findViewById(R.id.gv_daily_details);
        etComment = (EditText) findViewById(R.id.et_comment);
        tvSend = (TextView) findViewById(R.id.tv_send);
        scrollView = (ScrollView) findViewById(R.id.scroll_daily_detail);
        circleHeadView.setCircleColorResources(StaticData.getImageId(circleList));

        adapterDaily = new DailyDetailsListAdapter2(this, mDatasDaily);
        lvDaily.setAdapter(adapterDaily);
        imgAdpter = new ShopImageAdapter(this, uploadImgList);
        gridView.setAdapter(imgAdpter);
        adapterComment = new DailyDetailsCommentAdapter(this, mDatasComment);
        lvComment.setAdapter(adapterComment);
        dialogUtil = new DialogUtil(this);
        dialogUtil.setDialogListener(this);
        dailyUtil = new DailyUtil();
        dailyUtil.setmListener(this);

        tvBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
        lvComment.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        getDailyDatas();
        getCommentDatas();
    }

    /**
     * 获取日报详情
     */
    private void getDailyDatas() {
        showProgressDialog(getString(R.string.loading1), false);
        String url = Constant.moduleReportDetails;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("reportId", reportId);
        map.put("userId", mUserInfo.getUserId());
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                DailyDetailsBean dailyDetailsBean = GsonUtils.json2Bean(response, DailyDetailsBean.class);
                if (null != dailyDetailsBean && dailyDetailsBean.success && dailyDetailsBean.data != null) {
                    setDailyContent(dailyDetailsBean.data);
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
     * 获取评论列表
     */
    private void getCommentDatas() {
        String url = Constant.moduleDailyCommentList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("reportId", reportId);
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                DailyCommentListBean commentListBean = GsonUtils.json2Bean(response, DailyCommentListBean.class);
                if (null != commentListBean && commentListBean.success && null != commentListBean.data.list) {
                    setCommentDatas(commentListBean.data.list);
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
     * 日报数据初始化
     *
     * @param dataBean
     */
    private void setDailyContent(DailyDetailsBean.DataBean dataBean) {
        tvNameFull.setText(dataBean.userName);
        tvTime.setText(dataBean.createTime);
        if (!TextUtils.isEmpty(dataBean.tmplName)) {
            tvTypeLog.setText(dataBean.tmplName);
        }
        if (!TextUtils.isEmpty(dataBean.remark)) {
            tvRemark.setVisibility(View.VISIBLE);
            tvRemark.setText(ReplaceSymbolUtil.reverseReplaceHuanHang(dataBean.remark));
        } else {
            tvRemark.setVisibility(View.GONE);
        }
        circleHeadView.setTextContent(ReplaceSymbolUtil.cutStringLastTwo(dataBean.userName));
        if (!TextUtils.isEmpty(dataBean.deptName)) {
            tvDeptPost.setText(dataBean.deptName + "     " + dataBean.postName);
        } else {
            tvDeptPost.setText(dataBean.postName);
        }
        if (!TextUtils.isEmpty(dataBean.participant)) {
            tvCommentHead.setVisibility(View.VISIBLE);
            tvCommentHead.setText(StringUtil.setImgToText(this, 0, 1, R.drawable.read_eye, dataBean.participant));
        } else {
            tvCommentHead.setVisibility(View.GONE);
        }


        if (null != dataBean.fieldList) {
            mDatasDaily.clear();
            mDatasDaily.addAll(dataBean.fieldList);
            adapterDaily.setData(mDatasDaily);
        }

        initPic(dataBean.picList);
    }

    /**
     * 日报评论列表初始化
     *
     * @param list
     */
    private void setCommentDatas(List<DailyCommentListBean.CommentBean> list) {
        List<Integer> listInt = StaticData.getCircleColorList();
        for (DailyCommentListBean.CommentBean commentBean : list) {
            commentBean.setImgId(StaticData.getImageId(listInt));
        }
        mDatasComment.clear();
        mDatasComment.addAll(list);
        adapterComment.setData(mDatasComment);
//        if (adapterComment.getCount() < 1) {
//            lvComment.setVisibility(View.GONE);
//            tvCommentHead.setVisibility(View.GONE);
//        } else {
//            lvComment.setVisibility(View.VISIBLE);
//            tvCommentHead.setVisibility(View.VISIBLE);
//        }
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
        uploadImgList.clear();
        for (String s : strList) {
            UploadImageBean uploadImageBean = new UploadImageBean(-1);
            uploadImageBean.setPicUrl(s);
            uploadImgList.add(uploadImageBean);
        }
        imgAdpter.setData(uploadImgList);
    }

    /**
     * 评论
     */
    private void commentDaily() {
        String content = etComment.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show(this, getString(R.string.comment_daily_hint));
            return;
        }
        showProgressDialog(getString(R.string.submitting), false);
        String url = Constant.moduleCommentDaily;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("reportId", reportId);
        map.put("postBy", postId);
        map.put("userId", mUserInfo.getUserId());
        map.put("comment", ReplaceSymbolUtil.transcodeToUTF8(ReplaceSymbolUtil.replaceHuanHang(content)));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        final VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean) {
                    if (baseBean.success) {
                        EventBus.getDefault().post(EventBusConfig.LOG_LIST_REFRESH);
                        getCommentDatas();
                        postId = "";
                        etComment.setText("");
                        etComment.setHint(getString(R.string.comment_please));
//                        ToastUtil.show(DailyDetailsActivity.this, getString(R.string.comment_success));
                    } else {
                        ToastUtil.show(DailyDetailsActivity.this, baseBean.msg);
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
     * 滑动到顶部
     */
    private void slideDown() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.tv_send:// 评论
                commentDaily();
                break;
            case R.id.tv_top_right:// 日志模板
                startActivity(new Intent(this, DailyTemplateActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.gv_daily_details:// 图片
                if (null != uploadImgList && uploadImgList.size() > 0) {
                    ArrayList<String> imgs = new ArrayList<>();
                    for (UploadImageBean upBean : uploadImgList) {
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
                break;
            case R.id.lv_comment_daily:// 评论列表
                commentBean = adapterComment.getItem(position);
                if (null != commentBean) {
                    if (mUserInfo.getUserId().equals(commentBean.createBy)) {
                        etComment.setHint(getString(R.string.comment_please));
                        postId = "";
                        hideKeyboard();
                        dialogUtil.showDeleteDialog();
                    } else {
                        etComment.setHint(getString(R.string.reply_please) + commentBean.replyBy);
                        showKeyboard(etComment);
                        postId = commentBean.createBy;// 给被回复人Id赋值
                    }
                }
                break;
        }
    }

    /**
     * 展示键盘
     *
     * @param view
     */
    protected void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED);
        imm.showSoftInput(view, 0);
    }

    /**
     * 隐藏评论框和输入键盘
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etComment.getWindowToken(), 0);
    }

    @Override
    public void confirmDialog() {
        if (null != commentBean) {
            showProgressDialog(getString(R.string.deleting), false);
            dailyUtil.delLogComment(commentBean.commentId);
        }
    }

    @Override
    public void onDeleteSuccess() {
        dismissProgressDialog();
        EventBus.getDefault().post(EventBusConfig.LOG_LIST_REFRESH);
        if (null != adapterComment) {
            mDatasComment.remove(commentBean);
            adapterComment.notifyDataSetChanged();
        }
    }

    @Override
    public void onDeleteFail() {
        dismissProgressDialog();
    }
}
