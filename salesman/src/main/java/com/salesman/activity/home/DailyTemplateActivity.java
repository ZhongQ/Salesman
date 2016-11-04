package com.salesman.activity.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.adapter.DailyTemplateAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.DailyTemplateListBean;
import com.salesman.network.BaseHelper;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.itemdecoration.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 日志模板界面
 * Created by LiHuai on 2016/4/19.
 */
public class DailyTemplateActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = DailyTemplateActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private List<DailyTemplateListBean.DailyTemplateBean> mData = new ArrayList<>();
    private DailyTemplateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_daily_template);
    }

    @Override
    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.report_template);
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_daily);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        adapter = new DailyTemplateAdapter(this, mData);
        recyclerView.setAdapter(adapter);

        tvBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        getDailyTemplate();
    }

    /**
     * 获取日报模板
     */
    private void getDailyTemplate() {
        showProgressDialog(getString(R.string.loading1), false);
        String url = Constant.moduleDailyTemplateList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
//        map.put("type", "salesman_report");
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                DailyTemplateListBean dailyTemplateListBean = GsonUtils.json2Bean(response, DailyTemplateListBean.class);
                if (null != dailyTemplateListBean && dailyTemplateListBean.success && null != dailyTemplateListBean.data.list) {
                    mData.clear();
                    mData.addAll(dailyTemplateListBean.data.list);
                    adapter.setData(mData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
        }
    }
}
