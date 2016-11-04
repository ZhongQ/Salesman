package com.salesman.activity.personal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.adapter.SubordinateSigninListAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.SubordinateSigninListBean;
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
 * 下属签到列表界面
 * Created by LiHuai on 2016/2/2.
 */
public class SubordinateSigninActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener, UltimateListView.OnItemClickListener, UltimateListView.OnRetryClickListener {
    public static final String TAG = SubordinateSigninActivity.class.getSimpleName();

    private TextView tvBack;
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private UltimateListView listView;
    private SubordinateSigninListAdapter adapter;
    private List<SubordinateSigninListBean.SigninBean> mDatas = new ArrayList<>();

    private String name = "";
    private String id = "";
    private int pageNo = 1;
    private int pageSize = 10;
    private boolean mEnableLoadMore = true;
    private boolean mIsRequesting = false;

    private String createTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.common_layout_list_0);
    }

    @Override
    protected void initView() {
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(name);
        TextView tvRight = (TextView) findViewById(R.id.tv_top_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("");
        tvRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar_icon, 0);

        tvBack = (TextView) findViewById(R.id.tv_top_left);
        listView = (UltimateListView) findViewById(R.id.lv_common_0);
        listView.addOneOnlyHeader(HeadViewHolder.getHeadView(this), false);
        adapter = new SubordinateSigninListAdapter(this, mDatas);
        listView.setAdapter(adapter);
        tvBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        listView.setRefreshListener(this);
        listView.setOnLoadMoreListener(this);
        listView.setOnItemClickListener(this);
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
                getListDatas();
            }
        });
    }

    /**
     * 获取列表数据
     */
    private void getListDatas() {
        if (mIsRequesting) {
            return;
        }
        mIsRequesting = true;
        String url = Constant.modulePersonalSignRecordList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("userId", id);
        map.put("createTime", createTime);
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setListView();
                LogUtils.d(TAG, response);
                SubordinateSigninListBean signinListBean = GsonUtils.json2Bean(response, SubordinateSigninListBean.class);
                if (null != signinListBean && signinListBean.success) {
                    if (null != signinListBean.data && null != signinListBean.data.list) {
                        setDatas(signinListBean.data.list);
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
    private void setDatas(List<SubordinateSigninListBean.SigninBean> datas) {
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
                    ToastUtil.show(SubordinateSigninActivity.this, getResources().getString(R.string.date_after_today));
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

    private void setListView() {
        mIsRequesting = false;
        listView.setRefreshing(false);
        listView.setLoadingState(UltimateListView.FOOTER_NONE);
    }


    @Override
    public void onRefresh() {
        mEnableLoadMore = false;
        pageNo = 1;
        getListDatas();
    }

    @Override
    public void loadMore() {
        if (mEnableLoadMore) {
            listView.addLoadingFooter();
            listView.setLoadingState(UltimateListView.FOOTER_LOADING);
            getListDatas();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        SubordinateSigninListBean.SigninBean signinBean = mDatas.get(position);
        Intent intent = new Intent(this, MySigninActivity.class);
        intent.putExtra("createTime", signinBean.createTime);
        intent.putExtra("name", name);
        intent.putExtra("userId", signinBean.createBy);
        intent.putExtra("type", String.valueOf(signinBean.type));
        startActivity(intent);
    }

    @Override
    public void onRetryLoad() {
        initData();
    }
}
