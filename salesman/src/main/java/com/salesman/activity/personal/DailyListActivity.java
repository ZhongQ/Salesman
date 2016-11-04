package com.salesman.activity.personal;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.home.DailyDetailsActivity;
import com.salesman.adapter.DailyListAdapter3;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.common.GsonUtils;
import com.salesman.entity.CommonResponseBean;
import com.salesman.entity.DailyCommentListBean;
import com.salesman.entity.DailyListBean;
import com.salesman.fragment.PersonalFragment;
import com.salesman.fragment.WorkFragment;
import com.salesman.global.HeadViewHolder;
import com.salesman.network.BaseHelper;
import com.salesman.utils.DailyUtil;
import com.salesman.utils.DialogUtil;
import com.salesman.utils.EmptyViewUtil;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.StaticData;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserInfoPreference;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.DateUtil;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.listview.LoadMoreListView;
import com.studio.jframework.widget.listview.UltimateListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 日报列表（适用我的日报、下属日报和今日日报）
 * Created by LiHuai on 2016/3/31.
 */
public class DailyListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener, UltimateListView.OnItemClickListener, UltimateListView.OnRetryClickListener, LoadMoreListView.OnScrollChangedListener, DialogUtil.DialogOnClickListener, DailyUtil.OnDeleteListener {
    public static final String TAG = DailyListActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private List<Integer> imgIdList = StaticData.getCircleColorList();
    private TextView tvBack;
    private UltimateListView listView;
    private DailyListAdapter3 adapter;
    private List<DailyListBean.DailyBean> mDatas = new ArrayList<>();
    private int pageNo = 1;
    private int pageSize = 10;
    private boolean mEnableLoadMore = true;
    private boolean mIsRequesting = false;

    private String userId = "";
    private String deptId = "";// 部门Id
    private String createTime = "";
    private String reportId = "";// 日报Id
    private String postId = "";// 被回复人Id
    private String postName = "";// 被回复人名称
    private String come_from = "";
    private String name = "";
    // 评论布局
    private RelativeLayout layoutComment;
    private EditText etComment;
    private TextView tvSend;
    private InputMethodManager inputManager;
    private DailyListBean.DailyBean dailyBeanTemp = null;           // 被操作的日报实体
    private DailyCommentListBean.CommentBean commentBean = null;    // 被操作的评论实体
    // Dialog
    private DialogUtil dialogUtil;
    private DailyUtil dailyUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_daily_list);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText("日志列表");
        TextView tvRight = (TextView) findViewById(R.id.tv_top_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("");
        tvRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar_icon, 0);

        userId = getIntent().getStringExtra("userId");
        deptId = getIntent().getStringExtra("deptId");
        createTime = getIntent().getStringExtra("createTime");
        come_from = getIntent().getStringExtra("come_from");
        name = getIntent().getStringExtra("name");
        tvBack = (TextView) findViewById(R.id.tv_top_left);
        layoutComment = (RelativeLayout) findViewById(R.id.compose_comment_bar);
        layoutComment.setVisibility(View.GONE);
        etComment = (EditText) findViewById(R.id.et_comment);
        tvSend = (TextView) findViewById(R.id.tv_send);
        inputManager = (InputMethodManager) etComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        listView = (UltimateListView) findViewById(R.id.lv_common_0);
        listView.addOneOnlyHeader(HeadViewHolder.getHeadView(this), false);
        adapter = new DailyListAdapter3(this, mDatas, new MyCommentListener());
        listView.setAdapter(adapter);
        tvBack.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setRefreshListener(this);
        listView.setOnLoadMoreListener(this);
        listView.setOnRetryClickListener(this);
        listView.setOnScrollChangedListener(this);
        if (WorkFragment.TAG.equals(come_from)) {
            tvTitle.setText(getString(R.string.today_report));
        } else if (PersonalFragment.TAG.equals(come_from)) {
            tvTitle.setText(getString(R.string.my_report));
        } else if (MySubordinateActivity11.TAG.equals(come_from)) {
            tvTitle.setText(name);
        }

        dialogUtil = new DialogUtil(this);
        dialogUtil.setDialogListener(this);
        dailyUtil = new DailyUtil();
        dailyUtil.setmListener(this);
    }

    @Override
    protected void initData() {
        listView.postRefresh(new Runnable() {
            @Override
            public void run() {
                pageNo = 1;
                mEnableLoadMore = false;
                listView.removeEmptyView();
                listView.setRefreshing(true);
                getListData();
            }
        });
    }

    /**
     * 获取列表数据
     */
    private void getListData() {
        if (mIsRequesting) {
            return;
        }
        mIsRequesting = true;
        String url = Constant.moduleMyReportList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        if (TextUtils.isEmpty(userId)) {
            map.put("userId", mUserInfo.getUserId());
        } else {
            map.put("userId", userId);
        }
        if (!TextUtils.isEmpty(deptId)) {
            map.put("deptId", deptId);// 场景一：部门ID为空时，查询用户个人日报信息；场景二、部门ID不为空时，查询所在部门用户的日报信息；
        } else {
            map.put("deptId", "");
        }

        if (!TextUtils.isEmpty(createTime)) {
            map.put("createTime", createTime);
        }
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setListView();
                LogUtils.d(TAG, response);
                DailyListBean dailyListBean = GsonUtils.json2Bean(response, DailyListBean.class);
                if (null != dailyListBean && dailyListBean.success) {
                    if (null != dailyListBean.data && null != dailyListBean.data.list) {
                        setDailyData(dailyListBean.data.list);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setListView();
                EmptyViewUtil.showEmptyView(adapter, listView, pageNo, true);
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
    }

    /**
     * 初始化日报列表数据
     */
    private void setDailyData(List<DailyListBean.DailyBean> datas) {
        if (pageNo == 1) {
            mDatas.clear();
        }
        for (DailyListBean.DailyBean data : datas) {
            data.setImgId(StaticData.getImageId(imgIdList));
        }
        mDatas.addAll(datas);
        adapter.setData(mDatas);
        if (datas.size() < 10) {
            mEnableLoadMore = false;
            if (pageNo != 1) {
                listView.addLoadingFooter();
                listView.setLoadingState(UltimateListView.FOOTER_NOMORE);
            }
        } else {
            mEnableLoadMore = true;
            pageNo++;
        }
        EmptyViewUtil.showEmptyViewNoData(EmptyViewUtil.DAILY, false, adapter, listView);
    }


    private void setListView() {
        mIsRequesting = false;
        listView.setRefreshing(false);
        listView.setLoadingState(UltimateListView.FOOTER_NONE);
    }


    @Override
    public void onRefresh() {
        mEnableLoadMore = false;
        pageNo = 1;
        getListData();
    }

    @Override
    public void loadMore() {
        if (mEnableLoadMore) {
            listView.addLoadingFooter();
            listView.setLoadingState(UltimateListView.FOOTER_LOADING);
            getListData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1100 && resultCode == 1100) {
            initData();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        hideCommentView();
        DailyListBean.DailyBean dailyBean = adapter.getItem(position);
        Intent dailyDetails = new Intent(this, DailyDetailsActivity.class);
        dailyDetails.putExtra("reportId", dailyBean.reportId);
        startActivityForResult(dailyDetails, 1100);
    }

    @Override
    public void onRetryLoad() {
        initData();
    }

    /**
     * 日期选择器
     */
    private void showCalendar() {
        Calendar c = Calendar.getInstance();
        // THEME_HOLO_LIGHT
        new DatePickerDialog(this, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                createTime = year + "-" + (month + 1) + "-" + dayOfMonth;
                Date selectDate = DateUtil.toShortDate(createTime);
                Date todayDate = DateUtil.toShortDate(new Date());
                if (selectDate.before(todayDate) || selectDate.equals(todayDate)) {
                    initData();
                } else {
                    ToastUtil.show(DailyListActivity.this, getResources().getString(R.string.date_after_today));
                }
            }

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void scrollChanged() {
        if (layoutComment.getVisibility() != View.GONE) {
            hideCommentView();
        }
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
        if (null != dailyBeanTemp && null != dailyBeanTemp.replyList && null != commentBean) {
            dailyBeanTemp.replyList.remove(commentBean);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDeleteFail() {
        dismissProgressDialog();
    }

    /**
     * 评论和回复监听
     */
    private class MyCommentListener implements DailyListAdapter3.CommentListener {

        @Override
        public void commentListener(DailyListBean.DailyBean dailyBean) {
            setReplyEditFocusable();
            DailyListActivity.this.dailyBeanTemp = dailyBean;
            reportId = dailyBean.reportId;
            postId = "";// 给被回复人Id赋值
            postName = "";// 给被回复人名称赋值
            etComment.setHint(getString(R.string.comment) + dailyBean.userName);
        }

        @Override
        public void replayListener(DailyListBean.DailyBean dailyBean, DailyCommentListBean.CommentBean commentBean) {
            DailyListActivity.this.dailyBeanTemp = dailyBean;
            if (!mUserInfo.getUserId().equals(commentBean.createBy)) {
                setReplyEditFocusable();
                reportId = dailyBean.reportId;
                postId = commentBean.createBy;// 给被回复人Id赋值
                postName = commentBean.replyBy;// 给被回复人名称赋值
                etComment.setHint(getString(R.string.reply_please) + commentBean.replyBy);
            } else {
                hideCommentView();
                DailyListActivity.this.commentBean = commentBean;
                dialogUtil.showDeleteDialog();
//                ToastUtil.show(DailyListActivity.this, DailyListActivity.this.getString(R.string.cannot_comment));
            }

        }
    }

    /**
     * 使评论框获取焦点并展示
     */
    private void setReplyEditFocusable() {
        etComment.setFocusable(true);
        etComment.setFocusableInTouchMode(true);
        etComment.requestFocus();
        etComment.setText("");
        layoutComment.setVisibility(View.VISIBLE);
        inputManager.showSoftInput(etComment, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏评论框和输入键盘
     */
    private void hideCommentView() {
        layoutComment.setVisibility(View.GONE);
        inputManager.hideSoftInputFromWindow(etComment.getWindowToken(), 0);
//        postId = "";// 给被回复人Id赋值
//        postName = "";// 给被回复人名称赋值
//        etComment.setText("");
//        etComment.setHint(getString(R.string.comment_please));
    }

    /**
     * 发送评论或回复
     */
    private void commentDaily() {
        final String content = etComment.getText().toString().trim();
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
                CommonResponseBean bean = GsonUtils.json2Bean(response, CommonResponseBean.class);
                if (null != bean) {
                    if (bean.success && null != bean.data && !TextUtils.isEmpty(bean.data.commentId)) {
                        createComment(reportId, postName, ReplaceSymbolUtil.replaceHuanHang(content), bean.data.commentId);
                        hideCommentView();
                    } else {
                        ToastUtil.show(DailyListActivity.this, bean.msg);
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
     * 创建评论数据
     *
     * @param reportId 日报id
     * @param postBy   被回复人id
     * @param content  内容
     */
    private void createComment(String reportId, String postBy, String content, String commentId) {
        DailyCommentListBean.CommentBean commentBean = new DailyCommentListBean.CommentBean(mUserInfo.getUserName(), mUserInfo.getUserId(), reportId, postBy, content, commentId);
        if (null != dailyBeanTemp && null != dailyBeanTemp.replyList) {
            dailyBeanTemp.replyList.add(commentBean);
            adapter.notifyDataSetChanged();
        }
    }

    public void onEventMainThread(String action) {
        if (EventBusConfig.LOG_LIST_REFRESH.equals(action)) {
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.tv_top_right:
                showCalendar();
                break;
            case R.id.tv_send:// 发送评论
                commentDaily();
                break;
        }
    }
}
