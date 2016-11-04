package com.salesman.activity.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.adapter.SubordinateListAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.ReleaseObjectListBean;
import com.salesman.global.HeadViewHolder;
import com.salesman.network.BaseHelper;
import com.salesman.utils.EmptyViewUtil;
import com.salesman.utils.StaticData;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.listview.UltimateListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 我的下属列表界面
 * Created by LiHuai on 2016/2/1.
 */
public class MySubordinateActivity extends BaseActivity implements View.OnClickListener, UltimateListView.OnRetryClickListener {
    public static final String TAG = MySubordinateActivity.class.getSimpleName();

    private TextView tvBack;

    private UltimateListView listView;
    private SubordinateListAdapter adapter;
    private List<ReleaseObjectListBean.ReleaseObjectBean> mDatas = new ArrayList<>();
    private List<Integer> circle = StaticData.getCircleIdList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.common_layout_list);

        initViews();
        initDatas();
    }

    private void initViews() {
        tvBack = (TextView) findViewById(R.id.tv_top_left);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.my_xiashu);

        listView = (UltimateListView) findViewById(R.id.lv_common);
        listView.addOneOnlyHeader(HeadViewHolder.getHeadView(this), false);
        adapter = new SubordinateListAdapter(this, mDatas);
        listView.setAdapter(adapter);

        tvBack.setOnClickListener(this);
        listView.setOnRetryClickListener(this);
    }

    private void initDatas() {
        listView.postRefresh(new Runnable() {
            @Override
            public void run() {
                listView.removeEmptyView();
                listView.setRefreshing(true);
                getListDatas();
            }
        });
    }


    private void getListDatas() {
        String url = Constant.moduleRelaseObjectList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("userId", SalesManApplication.g_GlobalObject.getmUserInfo().getUserId());
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listView.setRefreshing(false);
                LogUtils.d(TAG, response);
                ReleaseObjectListBean releaseObjectListBean = GsonUtils.json2Bean(response, ReleaseObjectListBean.class);
                if (null != releaseObjectListBean && releaseObjectListBean.success) {
                    if (null != releaseObjectListBean.data && null != releaseObjectListBean.data.list) {
                        setDatas(releaseObjectListBean.data.list);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listView.setRefreshing(false);
                EmptyViewUtil.showEmptyView(adapter, listView, 1, true);
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
    }

    /**
     * 处理数据
     */
    private void setDatas(List<ReleaseObjectListBean.ReleaseObjectBean> datas) {
        Random random = new Random();
        for (ReleaseObjectListBean.ReleaseObjectBean data : datas) {
            int index = random.nextInt(circle.size());
            data.setImgId(circle.get(index));
        }
        mDatas.clear();
        mDatas.addAll(datas);
        adapter.setData(mDatas);
        EmptyViewUtil.showEmptyViewNoData(EmptyViewUtil.SUBORDINATE, false, adapter, listView);
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
    public void onRetryLoad() {
        initDatas();
    }
}
