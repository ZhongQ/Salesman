package com.salesman.activity.personal;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.adapter.LogListAdapter;
import com.salesman.common.BaseActivity;
import com.salesman.utils.AppLogUtil;
import com.salesman.utils.DateUtils;
import com.studio.jframework.widget.listview.UltimateListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志界面
 * Created by LiHuai on 2016/1/27.
 */
public class AppLogRecordActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = AppLogRecordActivity.class.getSimpleName();

    private UltimateListView listView;
    private List<String> mData = new ArrayList<>();
    private LogListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.common_layout_list);
    }

    @Override
    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText("日志");
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        listView = (UltimateListView) findViewById(R.id.lv_common);
        adapter = new LogListAdapter(this, mData);
        listView.setAdapter(adapter);

        tvBack.setOnClickListener(this);
        listView.setRefreshListener(this);
    }

    @Override
    protected void initData() {
        mData.clear();
        mData = AppLogUtil.getAppLogStrList(DateUtils.getYMD(System.currentTimeMillis()));
        adapter.setData(mData);
        listView.setRefreshing(false);
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
    public void onRefresh() {
        initData();
    }
}
