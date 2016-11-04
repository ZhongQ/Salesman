package com.salesman.activity.manage;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.adapter.NoticeListAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.NoticeListBean;
import com.salesman.entity.NoticeReleaseObj;
import com.salesman.global.BeanListHolder;
import com.salesman.global.HeadViewHolder;
import com.salesman.listener.OnCommonPostListener;
import com.salesman.listener.OnDialogItemClickListener;
import com.salesman.network.BaseHelper;
import com.salesman.utils.DialogUtil;
import com.salesman.utils.EmptyViewUtil;
import com.salesman.utils.NoticeUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserInfoUtil;
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

/**
 * 公告列表界面
 * Created by LiHuai on 2016/1/26.
 */
public class NoticeListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener, UltimateListView.OnRetryClickListener, UltimateListView.OnItemClickListener, UltimateListView.OnItemLongClickListener, OnDialogItemClickListener, OnCommonPostListener {
    public final String TAG = NoticeListActivity.class.getSimpleName();

    private ImageView ivTimeFilter, ivRelease;
    private String createTime = "";

    private UltimateListView listView;
    private NoticeListAdapter adapter;
    private List<NoticeListBean.NoticeBean> mDatas = new ArrayList<>();

    private int pageNo = 1;
    private boolean mEnableLoadMore = true;
    private boolean mIsRequesting = false;
    private List<NoticeReleaseObj> mDialogData = BeanListHolder.getNoticeDialogItemList();
    private DialogUtil dialogUtil;
    private NoticeUtil noticeUtil;
    private NoticeListBean.NoticeBean noticeBean;
    private String come_from = "";

    @Override
    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.all_notice);
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        tvBack.setVisibility(View.VISIBLE);
        ivTimeFilter = (ImageView) findViewById(R.id.iv_top_right2);
        ivTimeFilter.setImageResource(R.drawable.calendar_icon);
        ivRelease = (ImageView) findViewById(R.id.iv_top_right1);
        ivRelease.setImageResource(R.drawable.release_notice_icon);
        if (UserInfoUtil.isAdministrator()) {
            ivRelease.setVisibility(View.VISIBLE);
        } else {
            ivRelease.setVisibility(View.GONE);
        }
        come_from = getIntent().getStringExtra("come_from");

        dialogUtil = new DialogUtil(this);
        dialogUtil.setOnDialogItemClickListener(this);
        noticeUtil = new NoticeUtil();
        noticeUtil.setOnCommonPostListener(this);

        listView = (UltimateListView) findViewById(R.id.lv_notice);
        listView.addOneOnlyHeader(HeadViewHolder.getHeadView(this), false);
        adapter = new NoticeListAdapter(this, mDatas);
        listView.setAdapter(adapter);
        tvBack.setOnClickListener(this);
        ivTimeFilter.setOnClickListener(this);
        ivRelease.setOnClickListener(this);
        listView.setRefreshListener(this);
        listView.setOnLoadMoreListener(this);
        listView.setOnRetryClickListener(this);
        listView.setOnItemClickListener(this);
        if (!TextUtils.isEmpty(come_from)) {
            listView.setOnItemLongClickListener(this);
            tvTitle.setText("我的公告");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_notice_list_new);
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
                getNoticeList();
            }
        });
    }

    /**
     * 获取公告列表
     */
    private void getNoticeList() {
        if (mIsRequesting) {
            return;
        }
        mIsRequesting = true;
        String url = Constant.moduleNoticeList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(10));
        map.put("createTime", createTime);
        if (!TextUtils.isEmpty(come_from)) {
            map.put("queryType", "PRI");
        } else {
            map.put("queryType", "ALL");
        }
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setListView();
                LogUtils.d(TAG, response);
                NoticeListBean noticeListBean = GsonUtils.json2Bean(response, NoticeListBean.class);
                if (null != noticeListBean && noticeListBean.success) {
                    if (null != noticeListBean.data && null != noticeListBean.data.list) {
                        setDatas(noticeListBean.data.list);
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
     * 初始化列表数据
     */
    private void setDatas(List<NoticeListBean.NoticeBean> datas) {
        if (pageNo == 1) {
            mDatas.clear();
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
        EmptyViewUtil.showEmptyViewNoData(EmptyViewUtil.NOTICE, false, adapter, listView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            initData();
        }
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
                    ToastUtil.show(NoticeListActivity.this, getResources().getString(R.string.date_after_today));
                }
            }

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.iv_top_right1:// 发公告
                Intent releaseIntent = new Intent(NoticeListActivity.this, ReleaseNoticeActivity.class);
                startActivityForResult(releaseIntent, 1001);
                break;
            case R.id.iv_top_right2:
                showCalendar();
                break;
        }
    }

    /**
     * 设置ListView
     */
    private void setListView() {
        mIsRequesting = false;
        listView.setRefreshing(false);
        listView.setLoadingState(UltimateListView.FOOTER_NONE);
    }

    @Override
    public void onRefresh() {
        mEnableLoadMore = false;
        pageNo = 1;
        getNoticeList();
    }

    @Override
    public void loadMore() {
        if (mEnableLoadMore) {
            listView.addLoadingFooter();
            listView.setLoadingState(UltimateListView.FOOTER_LOADING);
            getNoticeList();
        }
    }

    @Override
    public void onRetryLoad() {
        initData();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        NoticeListBean.NoticeBean noticeBean = adapter.getItem(position);
        Intent intent = new Intent(this, NoticeDetailActivity.class);
        intent.putExtra("noticeId", noticeBean.noticeId);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        this.noticeBean = adapter.getItem(position);
        dialogUtil.showListDialog(mDialogData);
    }

    @Override
    public void onDialogItemClick(NoticeReleaseObj itemBean, int position) {
        if (null != itemBean) {
            switch (itemBean.id) {
                case 1:// 编辑
                    if (null != noticeBean) {
                        Intent intent = new Intent(this, ReleaseNoticeActivity.class);
                        intent.putExtra("NoticeBean", noticeBean);
                        startActivity(intent);
                    }
                    break;
                case 2:// 删除
                    showProgressDialog(getString(R.string.deleting), false);
                    noticeUtil.delNotice(noticeBean.noticeId);
                    break;
            }
        }
    }

    @Override
    public void onSuccessListener() {
        dismissProgressDialog();
        mDatas.remove(noticeBean);
        adapter.setData(mDatas);
    }

    @Override
    public void onFailListener() {
        dismissProgressDialog();
    }
}
