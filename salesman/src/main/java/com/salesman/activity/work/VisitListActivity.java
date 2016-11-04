package com.salesman.activity.work;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.guide.NewActionGuideActivity;
import com.salesman.adapter.VisitListAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.common.GsonUtils;
import com.salesman.entity.VisitListBean;
import com.salesman.global.HeadViewHolder;
import com.salesman.network.BaseHelper;
import com.salesman.utils.DateUtils;
import com.salesman.utils.EmptyViewUtil;
import com.salesman.utils.StaticData;
import com.salesman.utils.StringUtil;
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
 * 今日拜访列表界面
 * Created by LiHuai on 2016/08/04.
 */
public class VisitListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener {
    public final String TAG = VisitListActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private String createTime = DateUtils.getCurrentDate();
    private TextView tvDate;
    private UltimateListView listView;
    private VisitListAdapter adapter;
    private List<VisitListBean.VisitPlanBean> mData = new ArrayList<>();
    private List<Integer> colors = StaticData.getCircleColorList();
    private int pageNo = 1;
    private int pageSize = 10;
    private boolean mEnableLoadMore = true;
    private boolean mIsRequesting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_today_signin);

        setTtileDate();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        tvBack.setVisibility(View.VISIBLE);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.today_visit);
        ImageView ivTimeFilter = (ImageView) findViewById(R.id.iv_top_right2);
        ivTimeFilter.setImageResource(R.drawable.calendar_icon);
        ImageView visitPlan = (ImageView) findViewById(R.id.iv_top_right1);
        visitPlan.setImageResource(R.drawable.visit_plan_icon);
        tvDate = (TextView) findViewById(R.id.tv_date_signin);
        listView = (UltimateListView) findViewById(R.id.lv_today_signin);
        listView.addOneOnlyHeader(HeadViewHolder.getHeadView(this), false);
        adapter = new VisitListAdapter(this, mData);
        listView.setAdapter(adapter);

        tvBack.setOnClickListener(this);
        ivTimeFilter.setOnClickListener(this);
        visitPlan.setOnClickListener(this);
        listView.setRefreshListener(this);
        listView.setOnLoadMoreListener(this);
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
                getVisitData();
            }
        });
    }

    /**
     * 获取今日拜访数据
     */
    private void getVisitData() {
        if (mIsRequesting) {
            return;
        }
        mIsRequesting = true;
        String url = Constant.moduleVisitPlansList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", mUserInfo.getDeptId());
        map.put("createTime", createTime);
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setListView();
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                VisitListBean visitListBean = GsonUtils.json2Bean(response, VisitListBean.class);
                if (null != visitListBean && visitListBean.success) {
                    if (null != visitListBean.data && null != visitListBean.data.list) {
                        setDatas(visitListBean.data.list);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setListView();
                dismissProgressDialog();
                EmptyViewUtil.showEmptyView(adapter, listView, pageNo, false);
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
    }

    /**
     * 初始化列表数据
     */
    private void setDatas(List<VisitListBean.VisitPlanBean> datas) {
        if (pageNo == 1) {
            mData.clear();
        }
        for (VisitListBean.VisitPlanBean bean : datas) {
            bean.setHeadColor(StaticData.getImageId(colors));
        }
        mData.addAll(datas);
        adapter.setData(mData);
        if (datas.size() < pageSize) {
            mEnableLoadMore = false;
            if (pageNo != 1) {
                listView.addLoadingFooter();
                listView.setLoadingState(UltimateListView.FOOTER_NOMORE);
            }
        } else {
            mEnableLoadMore = true;
            pageNo++;
        }
        EmptyViewUtil.showEmptyViewNoData(EmptyViewUtil.VISIT, false, adapter, listView);
    }

    /**
     * 设置ListView属性
     */
    private void setListView() {
        mIsRequesting = false;
        listView.setRefreshing(false);
        listView.setLoadingState(UltimateListView.FOOTER_NONE);
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
                    setTtileDate();
                    initData();
                } else {
                    ToastUtil.show(VisitListActivity.this, getResources().getString(R.string.date_after_today));
                }
            }

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
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

    public void onEventMainThread(String action) {
        if (EventBusConfig.VISIT_LIST_REFRESH.equals(action)) {
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
            case R.id.iv_top_right2:
                showCalendar();
                break;
            case R.id.iv_top_right1:// 计划安排
                Intent intent = new Intent(this, VisitPlanActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        mEnableLoadMore = false;
        pageNo = 1;
        getVisitData();
    }

    @Override
    public void loadMore() {
        if (mEnableLoadMore) {
            listView.addLoadingFooter();
            listView.setLoadingState(UltimateListView.FOOTER_LOADING);
            getVisitData();
        }
    }
}
