package com.salesman.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.guide.NewActionGuideActivity;
import com.salesman.adapter.DepartmentListAdapter;
import com.salesman.adapter.SubordinateListAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.SubordinateListBean;
import com.salesman.network.BaseHelper;
import com.salesman.utils.StaticData;
import com.salesman.utils.StringUtil;
import com.salesman.utils.ViewUtil;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.listview.InnerListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 我的下属列表界面
 * 版本V1.1.0
 * Created by LiHuai on 2016/3/25.
 */
public class MySubordinateActivity11 extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String TAG = MySubordinateActivity11.class.getSimpleName();

    private TextView tvBack;

    private InnerListView lvSub;// 下属列表
    private InnerListView lvDep;// 部门列表
    private SubordinateListAdapter subAdapter;// 下属列表适配器
    private DepartmentListAdapter depAdapter;// 部门列表适配器
    private List<SubordinateListBean.XiaShuBean> mDatasSub = new ArrayList<>();
    private List<SubordinateListBean.DepartmentBean> mDatasDep = new ArrayList<>();
    private List<Integer> circle = StaticData.getCircleColorList();
    // 导航
    private LinearLayout layoutGuide;
    private ScrollView scrollView;
    // 无内容
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_subordinate);
    }

    @Override
    protected void initView() {
        tvBack = (TextView) findViewById(R.id.tv_top_left);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.my_xiashu);
        layoutGuide = (LinearLayout) findViewById(R.id.layout_guide);
        scrollView = (ScrollView) findViewById(R.id.sv_subordinate);
        emptyView = findViewById(R.id.view_empty_sub);
        lvSub = (InnerListView) findViewById(R.id.lv_subordinate);
        lvDep = (InnerListView) findViewById(R.id.lv_department);
        subAdapter = new SubordinateListAdapter(this, mDatasSub);
        depAdapter = new DepartmentListAdapter(this, mDatasDep);
        lvSub.setAdapter(subAdapter);
        lvDep.setAdapter(depAdapter);

        tvBack.setOnClickListener(this);
        lvDep.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        getListDatas(SalesManApplication.g_GlobalObject.getmUserInfo().getDeptId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!StringUtil.isOpenGuide(TAG)) {
            Intent intent = new Intent(this, NewActionGuideActivity.class);
            intent.putExtra("come_from", TAG);
            startActivity(intent);
        }
    }

    private void getListDatas(String depId) {
        showProgressDialog(getString(R.string.loading1), false);
        String url = Constant.moduleMySubordinateList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", depId);
        map.put("userId", SalesManApplication.g_GlobalObject.getmUserInfo().getUserId());
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                SubordinateListBean subordinateListBean = GsonUtils.json2Bean(response, SubordinateListBean.class);
                if (null != subordinateListBean && subordinateListBean.success) {
                    if (null != subordinateListBean.data) {
                        setNoContent(subordinateListBean.data);
                        if (null != subordinateListBean.data.navList) {
                            setGuideDatas(subordinateListBean.data.navList);
                        }
                        if (null != subordinateListBean.data.userList) {
                            setDatas(subordinateListBean.data.userList);
                        }
                        setDepDatas(subordinateListBean.data.deptList);
                        slideTop();
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
     * 处理下属数据
     */
    private void setDatas(List<SubordinateListBean.XiaShuBean> datas) {
        Random random = new Random();
        for (SubordinateListBean.XiaShuBean data : datas) {
            int index = random.nextInt(circle.size());
            data.setImgId(circle.get(index));
        }
        mDatasSub.clear();
        mDatasSub.addAll(datas);
        subAdapter.setData(mDatasSub);
        if (datas.size() > 0) {
            lvSub.setVisibility(View.VISIBLE);
        } else {
            lvSub.setVisibility(View.GONE);
        }
    }

    /**
     * 处理部门数据
     */
    private void setDepDatas(List<SubordinateListBean.DepartmentBean> datas) {
        if (null != datas) {
            mDatasDep.clear();
            mDatasDep.addAll(datas);
            depAdapter.setData(mDatasDep);
            if (datas.size() > 0) {
                lvDep.setVisibility(View.VISIBLE);
            } else {
                lvDep.setVisibility(View.GONE);
            }
        } else {
            lvDep.setVisibility(View.GONE);
        }
    }

    /**
     * 处理导航数据
     *
     * @param datas
     */
    private void setGuideDatas(List<SubordinateListBean.GuideBean> datas) {
        if (datas.size() > 0) {
            layoutGuide.removeAllViews();
            for (int i = 0; i < datas.size(); i++) {
                SubordinateListBean.GuideBean guideBean = datas.get(i);
                View view = View.inflate(this, R.layout.view_dept_guide, null);
                TextView tvName = (TextView) view.findViewById(R.id.tv_name_dept);
                ImageView ivArrow = (ImageView) view.findViewById(R.id.iv_arrow_dept);
                tvName.setText(guideBean.deptName);
                if (i == datas.size() - 1) {
                    if (1 != datas.size()) {
                        tvName.setTextColor(getResources().getColor(R.color.color_666666));
                    }
                    ivArrow.setVisibility(View.GONE);
                } else {
                    tvName.setOnClickListener(new MyGuideClick(guideBean));
                    ivArrow.setVisibility(View.VISIBLE);
                }
                ViewUtil.scaleContentView((ViewGroup) view);
                layoutGuide.addView(view);
            }
        }
    }

    /**
     * 无数据处理
     *
     * @param data
     */
    private void setNoContent(SubordinateListBean.DataBean data) {
        ImageView iv = (ImageView) findViewById(R.id.iv_no_data);
        iv.setImageResource(R.drawable.no_data_pic);
        TextView tvHint = (TextView) findViewById(R.id.tv_no_data);
        tvHint.setText(getString(R.string.no_subordinate_hint));
        TextView tvRefresh = (TextView) findViewById(R.id.tv_refresh);
        tvRefresh.setVisibility(View.GONE);
        if (null != data.deptList && null != data.userList) {
            if (data.deptList.size() > 0 || data.userList.size() > 0) {
                scrollView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            } else {
                scrollView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 滑动到顶部
     */
    private void slideTop() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_department:// 部门列表
                String deptId = depAdapter.getItem(position).deptId;
                getListDatas(deptId);
                break;
        }
    }

    private class MyGuideClick implements View.OnClickListener {
        private SubordinateListBean.GuideBean guideBean;

        public MyGuideClick(SubordinateListBean.GuideBean guideBean) {
            this.guideBean = guideBean;
        }

        @Override
        public void onClick(View v) {
            if (null != guideBean) {
                getListDatas(guideBean.deptId);
            }
        }
    }
}
