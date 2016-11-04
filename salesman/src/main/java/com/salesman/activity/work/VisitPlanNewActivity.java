package com.salesman.activity.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.salesman.R;
import com.salesman.activity.home.SingleSelection2Activity;
import com.salesman.activity.home.SingleSelectionActivity;
import com.salesman.adapter.viewholder.VisitPlanListHolder;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.SingleSelectionBean;
import com.salesman.entity.VisitPlanBean;
import com.salesman.network.BaseHelper;
import com.salesman.presenter.RequestDataPresenter;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserInfoPreference;
import com.salesman.utils.UserInfoUtil;
import com.salesman.utils.VisitLineUtil;
import com.salesman.view.OnCommonListener;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 拜访计划界面
 * Created by LiHuai on 2016/10/10.
 */
public class VisitPlanNewActivity extends BaseActivity implements View.OnClickListener, OnCommonListener, RecyclerArrayAdapter.OnItemClickListener, VisitLineUtil.VisitLineCallBack {
    private final String TAG = VisitPlanNewActivity.class.getSimpleName();
    private final int FLAG = 2008;
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private RequestDataPresenter mPresenter = new RequestDataPresenter(this);

    private TextView tvVisitNum, tvLineName, tvLineTotal, tvProgress;
    private SeekBar seekBar;
    private RecyclerView recyclerView;
    private RecyclerArrayAdapter<VisitPlanBean.DataBean.PlanBean> adapter;

    private String salesmanId = "", oldLineId = "", week = "";
    private VisitLineUtil visitLineUtil;
    private ArrayList<SingleSelectionBean> mLines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_visit_plan_new);
    }

    @Override
    protected void initView() {
        super.initView();
        TextView tvLeft = findView(R.id.tv_top_left);
        tvLeft.setOnClickListener(this);
        TextView tvTitle = findView(R.id.tv_top_title);
        tvTitle.setText("拜访计划");

        tvVisitNum = findView(R.id.tv_visit_num);
        tvLineName = findView(R.id.tv_line_name);
        tvLineTotal = findView(R.id.tv_line_total);
        tvProgress = findView(R.id.tv_progress);
        seekBar = findView(R.id.seekbar_plan);

        recyclerView = (RecyclerView) findViewById(R.id.rv_visit);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerArrayAdapter<VisitPlanBean.DataBean.PlanBean>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new VisitPlanListHolder(parent, R.layout.item_visit_week_list);
            }
        };
        recyclerView.setAdapter(adapter);
        if (UserInfoUtil.isAdministrator()) {
            adapter.setOnItemClickListener(this);
        }

        visitLineUtil = new VisitLineUtil();
        visitLineUtil.setVisitLineListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        salesmanId = getIntent().getStringExtra("salesmanId");
        mPresenter.getData();
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
    public Context getRequestContext() {
        return this;
    }

    @Override
    public String getRequestUrl() {
        return Constant.moduleVisitPlan;
    }

    @Override
    public Map<String, String> getRequestParam() {
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        if (TextUtils.isEmpty(salesmanId)) {
            map.put("salesmanId", mUserInfo.getUserId());
        } else {
            map.put("salesmanId", salesmanId);
        }
        return map;
    }

    @Override
    public void showLoading() {
        showProgressDialog(getString(R.string.loading1), false);
    }

    @Override
    public void hideLoading() {
        dismissProgressDialog();
    }

    @Override
    public void requestSuccess(String response) {
        LogUtils.d(TAG, response);
        VisitPlanBean visitPlanBean = GsonUtils.json2Bean(response, VisitPlanBean.class);
        if (null != visitPlanBean) {
            if (visitPlanBean.success && null != visitPlanBean.data) {
                tvVisitNum.setText(String.valueOf(visitPlanBean.data.visitTotal));
                tvLineName.setText(visitPlanBean.data.lineName);
                tvProgress.setText(visitPlanBean.data.week + "拜访进度");
                tvLineTotal.setText(String.valueOf(visitPlanBean.data.shopTotal));
                seekBar.setMax(visitPlanBean.data.shopTotal);
                seekBar.setProgress(visitPlanBean.data.visitTotal);
                seekBar.setEnabled(false);

                List<VisitPlanBean.DataBean.PlanBean> mData = visitPlanBean.data.list;
                if (null != mData && !mData.isEmpty()) {
                    adapter.clear();
                    adapter.addAll(mData);
                }
            }
        }
    }

    @Override
    public void requestFail() {

    }

    @Override
    public void onItemClick(int position) {
        VisitPlanBean.DataBean.PlanBean planBean = adapter.getItem(position);
        oldLineId = planBean.lineId;
        week = planBean.week;
        if (!TextUtils.isEmpty(salesmanId)) {
            if (mLines.isEmpty()) {
                showProgressDialog(getString(R.string.loading1), false);
                visitLineUtil.getVisitLineData(salesmanId);
            } else {
                VisitLineUtil.setSelectItem(mLines, oldLineId);
                Intent salesIntent = new Intent(VisitPlanNewActivity.this, SingleSelection2Activity.class);
                salesIntent.putParcelableArrayListExtra("data", mLines);
                salesIntent.putExtra(SingleSelectionActivity.TITLE, "线路选择");
                startActivityForResult(salesIntent, FLAG);
            }
        }
    }

    @Override
    public void onSuccess(ArrayList<SingleSelectionBean> data) {
        dismissProgressDialog();
        mLines.clear();
        mLines.addAll(data);
        VisitLineUtil.setSelectItem(mLines, oldLineId);
        Intent salesIntent = new Intent(VisitPlanNewActivity.this, SingleSelection2Activity.class);
        salesIntent.putParcelableArrayListExtra("data", mLines);
        salesIntent.putExtra(SingleSelectionActivity.TITLE, "线路选择");
        startActivityForResult(salesIntent, FLAG);
    }

    @Override
    public void onError() {
        dismissProgressDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FLAG:// 线路
                if (null != data) {
                    SingleSelectionBean bean = data.getParcelableExtra(SingleSelectionActivity.SELECT_BEAN);
                    if (null != bean) {
                        saveVisitPlan(bean);
                    }
                }
                break;
        }
    }

    private void saveVisitPlan(SingleSelectionBean bean) {
        if (TextUtils.isEmpty(salesmanId)) {
            return;
        }
        String lineStr = bean.id + "," + week;// 格式如：线路ID,星期一@@线路ID,星期二
//        showProgressDialog(getString(R.string.submitting), false);
        String url = Constant.moduleUpdateVisitPlans;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("salesmanId", salesmanId);
        map.put("lineStr", ReplaceSymbolUtil.transcodeToUTF8(lineStr));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean) {
                    if (baseBean.success) {
//                        EventBus.getDefault().post(EventBusConfig.VISIT_LIST_REFRESH);
                        mPresenter.getData();
                        ToastUtil.show(VisitPlanNewActivity.this, getString(R.string.save_success));
                    } else {
                        ToastUtil.show(VisitPlanNewActivity.this, baseBean.msg);
                    }
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
}
