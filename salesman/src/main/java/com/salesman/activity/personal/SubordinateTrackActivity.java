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
import com.salesman.activity.home.FootprintActivity;
import com.salesman.adapter.SubordinateTrackListAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.SubordinateTrackListBean;
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
 * 下属足迹列表界面
 * Created by LiHuai on 2016/2/2.
 */
public class SubordinateTrackActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener, UltimateListView.OnItemClickListener, UltimateListView.OnRetryClickListener {
    public static final String TAG = SubordinateTrackActivity.class.getSimpleName();

    private TextView tvBack;
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private UltimateListView listView;
    private SubordinateTrackListAdapter adapter;
    private List<SubordinateTrackListBean.TrackBean> mDatas = new ArrayList<>();

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
        adapter = new SubordinateTrackListAdapter(this, mDatas);
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
        String url = Constant.modulePersonalTrackRecordList;
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
                SubordinateTrackListBean trackListBean = GsonUtils.json2Bean(response, SubordinateTrackListBean.class);
                if (null != trackListBean && trackListBean.success) {
                    if (null != trackListBean.data && null != trackListBean.data.list) {
                        setDatas(trackListBean.data.list);
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
    private void setDatas(List<SubordinateTrackListBean.TrackBean> datas) {
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
        EmptyViewUtil.showEmptyViewNoData(EmptyViewUtil.TRACK, false, adapter, listView);
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
                    ToastUtil.show(SubordinateTrackActivity.this, getResources().getString(R.string.date_after_today));
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
        SubordinateTrackListBean.TrackBean trackBean = mDatas.get(position);
        Intent intent = new Intent(this, FootprintActivity.class);
        intent.putExtra("userId", trackBean.userId);
        intent.putExtra("createTime", trackBean.createTime);
        startActivity(intent);
    }

    @Override
    public void onRetryLoad() {
        initData();
    }
}
