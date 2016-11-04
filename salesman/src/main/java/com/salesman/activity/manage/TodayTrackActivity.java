package com.salesman.activity.manage;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.home.FootprintActivity;
import com.salesman.adapter.TodayTrackListAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.TodayTrackListBean;
import com.salesman.global.HeadViewHolder;
import com.salesman.network.BaseHelper;
import com.salesman.utils.DateUtils;
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
 * 今日足迹列表界面
 * Created by LiHuai on 2016/1/28.
 */
public class TodayTrackActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener, UltimateListView.OnItemClickListener, UltimateListView.OnRetryClickListener {
    public static final String TAG = TodayTrackActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private TextView tvDate;
    private UltimateListView listView;
    private TodayTrackListAdapter adapter;
    private List<TodayTrackListBean.EmployeeTrackBean> mDatas = new ArrayList<>();

    private int pageNo = 1;
    private boolean mEnableLoadMore = true;
    private boolean mIsRequesting = false;
    private String createTime = DateUtils.getCurrentDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_today_track);
        setTtileDate();
    }

    @Override
    protected void initView() {
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.today_track);
        TextView tvRight = (TextView) findViewById(R.id.tv_top_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("");
        tvRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar_icon, 0);

        tvDate = (TextView) findViewById(R.id.tv_date_signin);
        tvDate.setVisibility(View.GONE);
        listView = (UltimateListView) findViewById(R.id.lv_today_track);
        listView.addOneOnlyHeader(HeadViewHolder.getHeadView(this), false);
        adapter = new TodayTrackListAdapter(this, mDatas);
        listView.setAdapter(adapter);
        tvBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        listView.setOnItemClickListener(this);
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
                getTrackListDatas();
            }
        });
    }

    private void getTrackListDatas() {
        if (mIsRequesting) {
            return;
        }
        mIsRequesting = true;
        String url = Constant.moduleTodayTrackList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", mUserInfo.getDeptId());
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(10));
        map.put("createTime", createTime);
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setListView();
                LogUtils.d(TAG, response);
                TodayTrackListBean trackListBean = GsonUtils.json2Bean(response, TodayTrackListBean.class);
                if (null != trackListBean && trackListBean.success) {
                    if (null != trackListBean.data && null != trackListBean.data.list) {
                        setDatas(trackListBean.data.list);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (adapter.getCount() > 0) {
                    tvDate.setVisibility(View.VISIBLE);
                } else {
                    tvDate.setVisibility(View.GONE);
                }
                setListView();
                EmptyViewUtil.showEmptyView(adapter, listView, pageNo, true);
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
    }

    /**
     * 初始化列表数据
     */
    private void setDatas(List<TodayTrackListBean.EmployeeTrackBean> datas) {
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
        if (adapter.getCount() > 0) {
            tvDate.setVisibility(View.VISIBLE);
        } else {
            tvDate.setVisibility(View.GONE);
        }
        EmptyViewUtil.showEmptyViewNoData(EmptyViewUtil.TRACK, false, adapter, listView);
    }

    /**
     * 设置头部时间
     */
    private void setTtileDate() {
        if (!TextUtils.isEmpty(createTime)) {
            Date date = DateUtil.fmtStrToDate(createTime);
            if (null != date) {
                tvDate.setText(createTime + " " + DateUtil.getDayOfWeek(date));
            }
        } else {
            tvDate.setText(DateUtils.getCurrentDate() + " " + DateUtil.getDayOfWeek(new Date()));
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
                createTime = DateUtils.fmtTimeToStr(year + "-" + (month + 1) + "-" + dayOfMonth, "yyyy-MM-dd");
                Date selectDate = DateUtil.toShortDate(createTime);
                Date todayDate = DateUtil.toShortDate(new Date());
                if (selectDate.before(todayDate) || selectDate.equals(todayDate)) {
                    initData();
                    setTtileDate();
                } else {
                    ToastUtil.show(TodayTrackActivity.this, getResources().getString(R.string.date_after_today));
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
        getTrackListDatas();
    }

    @Override
    public void loadMore() {
        if (mEnableLoadMore) {
            listView.addLoadingFooter();
            listView.setLoadingState(UltimateListView.FOOTER_LOADING);
            getTrackListDatas();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        TodayTrackListBean.EmployeeTrackBean bean = adapter.getItem(position);
        Intent intent = new Intent(this, FootprintActivity.class);
        intent.putExtra("userId", bean.userId);
        intent.putExtra("createTime", bean.createTime);
        startActivity(intent);
    }

    @Override
    public void onRetryLoad() {
        initData();
    }
}
