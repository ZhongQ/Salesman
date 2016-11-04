package com.salesman.activity.personal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.adapter.SigninRecordListAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.SigninRecordListBean;
import com.salesman.global.HeadViewHolder;
import com.salesman.network.BaseHelper;
import com.salesman.utils.EmptyViewUtil;
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

/**
 * 我的签到列表界面
 * Created by LiHuai on 2016/1/25.
 */
public class MySigninActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener, UltimateListView.OnRetryClickListener {
    public static final String TAG = MySigninActivity.class.getSimpleName();

    private TextView tvBack;
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private UltimateListView listView;
    private List<SigninRecordListBean.SigninBean> mDatas = new ArrayList<>();
    private SigninRecordListAdapter adapter;

    private String name = "";
    private String userId = "";
    private String createTime = "";
    private String type = "";

    private int pageNo = 1;
    private boolean mEnableLoadMore = true;
    private boolean mIsRequesting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.common_layout_list_20);
    }

    @Override
    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        TextView tvRight = (TextView) findViewById(R.id.tv_top_right);
        tvRight.setText("");
        tvRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar_icon, 0);

        name = getIntent().getStringExtra("name");
        userId = getIntent().getStringExtra("userId");
        createTime = getIntent().getStringExtra("createTime");
        type = getIntent().getStringExtra("type");
        if (TextUtils.isEmpty(userId)) {
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(name)) {
            tvTitle.setText(R.string.my_signin);
        } else {
            tvTitle.setText(name);
        }


        tvBack = (TextView) findViewById(R.id.tv_top_left);
        listView = (UltimateListView) findViewById(R.id.lv_common_20);
        listView.addOneOnlyHeader(HeadViewHolder.getHeadView(this), false);
        adapter = new SigninRecordListAdapter(this, mDatas);
        listView.setAdapter(adapter);
        tvBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        listView.setRefreshListener(this);
        listView.setOnLoadMoreListener(this);
        listView.setOnRetryClickListener(this);
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
                getSigninListData();
            }
        });
    }

    /**
     * 获取签到记录列表数据
     */
    private void getSigninListData() {
        if (mIsRequesting) {
            return;
        }
        mIsRequesting = true;
        String url = Constant.moduleSignInRecordList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        if (TextUtils.isEmpty(userId)) {
            map.put("userId", mUserInfo.getUserId());
        } else {
            map.put("userId", userId);
        }
        if (TextUtils.isEmpty(createTime)) {
            map.put("createTime", "");
        } else {
            map.put("createTime", createTime);
        }
        if (TextUtils.isEmpty(type)) {
            map.put("type", "");
        } else {
            map.put("type", type);
        }
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(10));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setListView();
                LogUtils.d(TAG, response);
                SigninRecordListBean recordBean = GsonUtils.json2Bean(response, SigninRecordListBean.class);
                if (null != recordBean && recordBean.success) {
                    if (null != recordBean.data && null != recordBean.data.list) {
                        setDatas(recordBean.data.list);
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
    private void setDatas(List<SigninRecordListBean.SigninBean> datas) {
        if (pageNo == 1) {
            mDatas.clear();
        }
        mDatas.addAll(datas);
        adapter.setData(mDatas);
        if (datas.size() < 10) {
            mEnableLoadMore = false;
            if (1 != pageNo) {
                listView.addLoadingFooter();
                listView.setLoadingState(UltimateListView.FOOTER_NOMORE);
            }
        } else {
            mEnableLoadMore = true;
            pageNo++;
        }
        EmptyViewUtil.showEmptyViewNoData(EmptyViewUtil.SIGNIN, false, adapter, listView);
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
                    ToastUtil.show(MySigninActivity.this, getResources().getString(R.string.date_after_today));
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
            case R.id.tv_top_right:
                showCalendar();
                break;
        }
    }

    /**
     * 设置ListView属性
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
        getSigninListData();
    }

    @Override
    public void loadMore() {
        if (mEnableLoadMore) {
            listView.addLoadingFooter();
            listView.setLoadingState(UltimateListView.FOOTER_LOADING);
            getSigninListData();
        }
    }

    @Override
    public void onRetryLoad() {
        initData();
    }
}
