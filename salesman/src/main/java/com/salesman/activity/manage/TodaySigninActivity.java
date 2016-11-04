package com.salesman.activity.manage;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.salesman.R;
import com.salesman.activity.personal.MySigninActivity;
import com.salesman.adapter.viewholder.TodaySigninListHolder;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.SigninListBean;
import com.salesman.presenter.RequestDataPresenter;
import com.salesman.utils.DateUtils;
import com.salesman.utils.EmptyViewUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserInfoPreference;
import com.salesman.view.OnCommonListener;
import com.studio.jframework.utils.DateUtil;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.listview.UltimateListView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 今日签到列表界面
 * Created by LiHuai on 2016/1/28.
 */
public class TodaySigninActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, UltimateListView.OnRetryClickListener, OnCommonListener, TodaySigninListHolder.TodaySigninListener {
    public final String TAG = TodaySigninActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private RequestDataPresenter mPresenter = new RequestDataPresenter(this);

    private TextView tvDate;
    private EasyRecyclerView recyclerView;
    private TodaySigninListHolder todaySigninListHolder;
    private RecyclerArrayAdapter<SigninListBean.SignGroupBean> adapter;

    private int pageNo = 1, pageSize = 10;
    private String createTime = DateUtils.getCurrentDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_today_signin_new);
        setTtileDate();
    }

    @Override
    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.today_signin);
        ImageView ivTimeFilter = (ImageView) findViewById(R.id.iv_top_right2);
        ivTimeFilter.setImageResource(R.drawable.calendar_icon);
        ImageView iv2 = (ImageView) findViewById(R.id.iv_top_right1);
        iv2.setVisibility(View.GONE);

        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        tvBack.setVisibility(View.VISIBLE);
        tvDate = (TextView) findViewById(R.id.tv_date_signin);

        recyclerView = (EasyRecyclerView) findViewById(R.id.rv_today_signin);
        initRecyclerView(recyclerView);
        EmptyViewUtil.initRecyclerEmptyView(recyclerView, EmptyViewUtil.SIGNIN);
        adapter = new RecyclerArrayAdapter<SigninListBean.SignGroupBean>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                todaySigninListHolder = new TodaySigninListHolder(parent, R.layout.item_today_signin_new);
                todaySigninListHolder.setTodaySigninListener(TodaySigninActivity.this);
                return todaySigninListHolder;
            }
        };
        recyclerView.setAdapterWithProgress(adapter);

        tvBack.setOnClickListener(this);
        ivTimeFilter.setOnClickListener(this);
        recyclerView.setRefreshListener(this);
    }

    @Override
    protected void initData() {
        onRefresh();
    }

    /**
     * 初始化列表数据
     */
    private void setDatas(List<SigninListBean.SignGroupBean> datas) {
        if (pageNo == 1) {
            adapter.clear();
        }
        adapter.addAll(datas);
        if (datas.size() < pageSize) {
//            adapter.stopMore();
        } else {
            mPresenter.setmEnableLoadMore(true);
            pageNo++;
        }
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
                    onRefresh();
                    setTtileDate();
                } else {
                    ToastUtil.show(TodaySigninActivity.this, getResources().getString(R.string.date_after_today));
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
            case R.id.iv_top_right2:
                showCalendar();
                break;
        }
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        mPresenter.refreshData();
    }

    @Override
    public void onRetryLoad() {
        initData();
    }

    @Override
    public Context getRequestContext() {
        return this;
    }

    @Override
    public String getRequestUrl() {
        return Constant.moduleTodaySigninList;
    }

    @Override
    public Map<String, String> getRequestParam() {
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", mUserInfo.getDeptId());
        map.put("createTime", createTime);
        return map;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void requestSuccess(String response) {
        LogUtils.d(TAG, response);
        SigninListBean signinListBean = GsonUtils.json2Bean(response, SigninListBean.class);
        if (null != signinListBean) {
            if (signinListBean.success && null != signinListBean.data && null != signinListBean.data.list) {
                setDatas(signinListBean.data.list);
            }
        }
    }

    @Override
    public void requestFail() {

    }

    @Override
    public void onInnerItemListener(SigninListBean.SignBean signBean) {
        if (null != signBean && !TextUtils.isEmpty(signBean.id) && !TextUtils.isEmpty(signBean.createTime)) {
            Intent intent = new Intent(this, MySigninActivity.class);
            intent.putExtra("userId", signBean.createBy);
            intent.putExtra("name", signBean.userName);
            intent.putExtra("createTime", signBean.createTime);
            intent.putExtra("type", String.valueOf(signBean.type));
            startActivity(intent);
        }
    }
}
