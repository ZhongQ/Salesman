package com.salesman.activity.work;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.home.SingleSelectionActivity;
import com.salesman.adapter.VisitPlanPersonalAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.LinesListBean;
import com.salesman.entity.SingleSelectionBean;
import com.salesman.entity.VisitPlanPersonalListBean;
import com.salesman.listener.OnCommonPostListener;
import com.salesman.network.BaseHelper;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.SalesmanLineUtil;
import com.salesman.utils.ToastUtil;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.listview.InnerListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 拜访安排界面
 * Created by LiHuai on 2016/08/03.
 */
public class VisitPlanActivity extends BaseActivity implements View.OnClickListener, OnCommonPostListener, AdapterView.OnItemClickListener {
    public final String TAG = VisitPlanActivity.class.getSimpleName();
    private final int FLAG = 2004;
    private final int FLAG2 = 2005;

    private InnerListView listView;
    private VisitPlanPersonalAdapter adapter;
    private List<VisitPlanPersonalListBean.VisitPlanBean> mData = new ArrayList<>();
    private LinearLayout laySalesSelect;
    private TextView tvSalesName;
    private ArrayList<SingleSelectionBean> mSalesmans = new ArrayList<>();
    private ArrayList<SingleSelectionBean> mLines = new ArrayList<>();
    private SalesmanLineUtil salesmanLineUtil;
    private String salesmanId = "";
    private int clickPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_visit_plan);
    }

    @Override
    protected void initView() {
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        TextView tvTitel = (TextView) findViewById(R.id.tv_top_title);
        tvTitel.setText(R.string.visit_plan);
        TextView tvSave = (TextView) findViewById(R.id.tv_top_right);
        tvSave.setVisibility(View.VISIBLE);
        tvSave.setText(R.string.save);
        laySalesSelect = (LinearLayout) findViewById(R.id.lay_sales_select);
        tvSalesName = (TextView) findViewById(R.id.tv_name_visit);
        listView = (InnerListView) findViewById(R.id.lv_visit_week_plan);
        adapter = new VisitPlanPersonalAdapter(this, mData);
        listView.setAdapter(adapter);
        salesmanLineUtil = new SalesmanLineUtil();

        tvBack.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        laySalesSelect.setOnClickListener(this);
        salesmanLineUtil.setOnCommonPostListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        getVisitPlanListData();
    }

    /**
     * 获取拜访计划
     */
    private void getVisitPlanListData() {
        showProgressDialog(getString(R.string.loading1), false);
        String url = Constant.moduleMyVisitPlanList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("salesmanId", salesmanId);
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                VisitPlanPersonalListBean bean = GsonUtils.json2Bean(response, VisitPlanPersonalListBean.class);
                if (null != bean) {
                    if (bean.success && null != bean.data && null != bean.data.list) {
                        mData.clear();
                        mData.addAll(bean.data.list);
                        adapter.setData(mData);
                    } else {
                        ToastUtil.show(VisitPlanActivity.this, bean.msg);
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

    /**
     * 获取业务员线路
     */
    private void getSalesmanLinesData() {
        showProgressDialog(getString(R.string.loading1), false);
        String url = Constant.moduleSalesLinesList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("salesmanId", salesmanId);
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                LinesListBean bean = GsonUtils.json2Bean(response, LinesListBean.class);
                if (null != bean) {
                    if (bean.success && null != bean.data && null != bean.data.list) {
                        mLines.clear();
                        mLines = changeToSingleBean(bean.data.list);
                        Intent salesIntent = new Intent(VisitPlanActivity.this, SingleSelectionActivity.class);
                        salesIntent.putParcelableArrayListExtra("data", mLines);
                        salesIntent.putExtra(SingleSelectionActivity.TITLE, "线路选择");
                        startActivityForResult(salesIntent, FLAG2);
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

    private ArrayList<SingleSelectionBean> changeToSingleBean(List<LinesListBean.LineBean> list) {
        ArrayList<SingleSelectionBean> data = new ArrayList<>();
        for (LinesListBean.LineBean lineBean : list) {
            data.add(new SingleSelectionBean(lineBean.lineId, lineBean.lineName, String.valueOf(lineBean.shopTotal), 1));
        }
        return data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FLAG:// 业务员
                mLines.clear();
                if (null != data) {
                    SingleSelectionBean bean = data.getParcelableExtra(SingleSelectionActivity.SELECT_BEAN);
                    if (null != bean) {
                        salesmanId = bean.id;
                        tvSalesName.setText(bean.name);
                        setSelectItem(bean);
                        getVisitPlanListData();
                    }
                }
                break;
            case FLAG2:// 线路
                if (null != data) {
                    SingleSelectionBean bean = data.getParcelableExtra(SingleSelectionActivity.SELECT_BEAN);
                    if (null != bean) {
                        upDataVisitPlan(bean);
                    }
                }
                break;
        }
    }

    private void setSelectItem(SingleSelectionBean bean) {
        for (SingleSelectionBean singleSelectionBean : mSalesmans) {
            if (bean.id.equals(singleSelectionBean.id)) {
                singleSelectionBean.setIsSelect(true);
            } else {
                singleSelectionBean.setIsSelect(false);
            }
        }
    }

    /**
     * 更新拜访安排列表
     *
     * @param bean
     */
    private void upDataVisitPlan(SingleSelectionBean bean) {
        if (-1 != clickPosition) {
            VisitPlanPersonalListBean.VisitPlanBean visitPlanBean = mData.get(clickPosition);
            if ("ALL".equals(bean.id)) {
                visitPlanBean.lineId = "";
            } else {
                visitPlanBean.lineId = bean.id;
            }
            visitPlanBean.lineName = bean.name;
            if (!TextUtils.isEmpty(bean.nameNd)) {
                visitPlanBean.shopTotal = Integer.parseInt(bean.nameNd);
            } else {
                visitPlanBean.shopTotal = 0;
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void saveVisitPlan() {
        if (TextUtils.isEmpty(salesmanId)) {
            ToastUtil.show(this, "请先选择业务员");
            return;
        }
        StringBuffer lineStr = new StringBuffer();
        for (VisitPlanPersonalListBean.VisitPlanBean bean : mData) {
            lineStr.append(bean.toString());
        }
        showProgressDialog(getString(R.string.submitting), false);
        String url = Constant.moduleUpdateVisitPlans;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("salesmanId", salesmanId);
        map.put("lineStr", ReplaceSymbolUtil.transcodeToUTF8(lineStr.toString()));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean) {
                    if (baseBean.success) {
                        EventBus.getDefault().post(EventBusConfig.VISIT_LIST_REFRESH);
                        ToastUtil.show(VisitPlanActivity.this, getString(R.string.save_success));
                        finish();
                    } else {
                        ToastUtil.show(VisitPlanActivity.this, baseBean.msg);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.tv_top_right:// 保存
                saveVisitPlan();
                break;
            case R.id.lay_sales_select:// 业务员选择
                if (SalesmanLineUtil.isSecondRequest()) {
                    showProgressDialog(getString(R.string.loading1), false);
                    salesmanLineUtil.getSalesmanAndLineData(false);
                } else {
                    if (mSalesmans.isEmpty()) {
                        mSalesmans = SalesmanLineUtil.getAllLineSingSelectionData();
                    }
                    Intent salesIntent = new Intent(this, SingleSelectionActivity.class);
                    salesIntent.putParcelableArrayListExtra("data", mSalesmans);
                    salesIntent.putExtra(SingleSelectionActivity.TITLE, "业务员选择");
                    startActivityForResult(salesIntent, FLAG);
                }
                break;
        }
    }

    @Override
    public void onSuccessListener() {
        dismissProgressDialog();
        mSalesmans = SalesmanLineUtil.getAllLineSingSelectionData();
        onClick(laySalesSelect);
    }

    @Override
    public void onFailListener() {
        dismissProgressDialog();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (TextUtils.isEmpty(salesmanId)) {
            ToastUtil.show(this, "请先选择业务员");
            return;
        }
        clickPosition = position;
        if (mLines.isEmpty()) {
            getSalesmanLinesData();
        } else {
            Intent salesIntent = new Intent(VisitPlanActivity.this, SingleSelectionActivity.class);
            salesIntent.putParcelableArrayListExtra("data", mLines);
            salesIntent.putExtra(SingleSelectionActivity.TITLE, "线路选择");
            startActivityForResult(salesIntent, FLAG2);
        }
    }
}
