package com.salesman.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.salesman.R;
import com.salesman.adapter.viewholder.SingleSelectionHolder;
import com.salesman.common.BaseActivity;
import com.salesman.entity.SingleSelectionBean;

import java.util.ArrayList;

/**
 * 单选列表界面
 * Created by LiHuai on 2016/10/13.
 * V2.0.0
 */
public class SingleSelection2Activity extends BaseActivity implements View.OnClickListener, RecyclerArrayAdapter.OnItemClickListener {
    public static final int FLAG = 2007;
    public static final String SELECT_BEAN = "select_bean";
    public static final String TITLE = "title";

    private String title = "单选";
    private RecyclerView recyclerView;
    private RecyclerArrayAdapter<SingleSelectionBean> adapter;
    private ArrayList<SingleSelectionBean> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.common_layout_rv2);
    }

    @Override
    protected void initView() {
        title = getIntent().getStringExtra(TITLE);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(title);
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        tvBack.setOnClickListener(this);

        recyclerView = findView(R.id.rv_common_0);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerArrayAdapter<SingleSelectionBean>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new SingleSelectionHolder(parent, R.layout.item_single_select);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        mData = getIntent().getParcelableArrayListExtra("data");
        if (null != mData) {
            adapter.addAll(mData);
        }
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
    public void onItemClick(int position) {
        SingleSelectionBean bean = adapter.getItem(position);
        Intent intent = getIntent();
        intent.putExtra(SELECT_BEAN, bean);
        setResult(FLAG, intent);
        finish();
    }
}
