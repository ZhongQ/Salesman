package com.salesman.activity.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.adapter.VisitPlanPersonalAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.VisitPlanPersonalListBean;
import com.salesman.global.HeadViewHolder;
import com.salesman.network.BaseHelper;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserInfoPreference;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.listview.UltimateListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 个人拜访计划界面
 * Created by LiHuai on 2016/08/10.
 */
public class MyVisitPlanActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = MyVisitPlanActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private UltimateListView listView;
    private VisitPlanPersonalAdapter adapter;
    private List<VisitPlanPersonalListBean.VisitPlanBean> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.common_layout_list);

        getVisitPlanListData();
    }

    @Override
    protected void initView() {
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.visit_jh);
        listView = (UltimateListView) findViewById(R.id.lv_common);
        listView.addOneOnlyHeader(HeadViewHolder.getHeadView(this), false);
        adapter = new VisitPlanPersonalAdapter(this, mData);
        adapter.setIsMyPlan(true);
        listView.setAdapter(adapter);

        tvBack.setOnClickListener(this);
    }

    /**
     * 获取拜访计划
     */
    private void getVisitPlanListData() {
        showProgressDialog(getString(R.string.loading1), false);
        String url = Constant.moduleMyVisitPlanList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("salesmanId", mUserInfo.getUserId());
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
                        ToastUtil.show(MyVisitPlanActivity.this, bean.msg);
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
        }
    }
}
