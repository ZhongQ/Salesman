package com.salesman.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.adapter.SingleSelectionAdapter;
import com.salesman.common.BaseActivity;
import com.salesman.entity.SingleSelectionBean;
import com.salesman.utils.EmptyViewUtil;
import com.studio.jframework.widget.listview.UltimateListView;

import java.util.ArrayList;

/**
 * 单选列表界面
 * Created by LiHuai on 2016/06/17.
 */
public class SingleSelectionActivity extends BaseActivity implements View.OnClickListener, UltimateListView.OnItemClickListener {
    public static final int FLAG = 2001;
    public static final String SELECT_BEAN = "select_bean";
    public static final String TITLE = "title";

    private String title = "单选";
    private UltimateListView listView;
    private SingleSelectionAdapter adapter;
    private ArrayList<SingleSelectionBean> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.common_layout_list_0);
    }

    @Override
    protected void initView() {
        title = getIntent().getStringExtra(TITLE);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(title);
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        listView = (UltimateListView) findViewById(R.id.lv_common_0);

        tvBack.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        mData = getIntent().getParcelableArrayListExtra("data");
        if (null != mData) {
            adapter = new SingleSelectionAdapter(this, mData);
            listView.setAdapter(adapter);
            listView.removeEmptyView();
        }
        EmptyViewUtil.showEmptyViewNoData(EmptyViewUtil.NO_DATA, false, adapter, listView);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        adapter.setCheckItem(position);
        Intent intent = getIntent();
        intent.putExtra(SELECT_BEAN, adapter.getItem(position));
        setResult(FLAG, intent);
        finish();
    }
}
