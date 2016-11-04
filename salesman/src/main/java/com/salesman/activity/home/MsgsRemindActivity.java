package com.salesman.activity.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.adapter.HomeMsgListAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.HomeMsgListBean;
import com.salesman.global.HeadViewHolder;
import com.salesman.network.BaseHelper;
import com.salesman.utils.EmptyViewUtil;
import com.salesman.utils.ToastUtil;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.listview.UltimateListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 阿街提醒界面
 * Created by LiHuai on 2016/08/22.
 */
public class MsgsRemindActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = MsgsRemindActivity.class.getSimpleName();

    private UltimateListView listView;
    private List<HomeMsgListBean.MsgBean> mData = new ArrayList<>();
    private HomeMsgListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.common_layout_list_0);
    }

    @Override
    protected void initView() {
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.ajie_remind);
        listView = (UltimateListView) findViewById(R.id.lv_common_0);
        listView.addOneOnlyHeader(HeadViewHolder.getHeadView(this), false);
        adapter = new HomeMsgListAdapter(this, mData, HomeMsgListAdapter.ITEM_TYPE_1);
        listView.setAdapter(adapter);

        tvBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        listView.postRefresh(new Runnable() {
            @Override
            public void run() {
                listView.removeEmptyView();
                listView.setRefreshing(true);
                listView.setVisibility(View.VISIBLE);
                getMsgsRemindData();
            }
        });
    }

    /**
     * 获取消息提醒列表数据
     */
    private void getMsgsRemindData() {
        String url = Constant.moduleAJieMsgsRemindList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listView.setRefreshing(false);
                LogUtils.d(TAG, response);
                HomeMsgListBean homeMsgListBean = GsonUtils.json2Bean(response, HomeMsgListBean.class);
                if (null != homeMsgListBean) {
                    if (homeMsgListBean.success && null != homeMsgListBean.data && null != homeMsgListBean.data.list) {
                        mData.clear();
                        mData.addAll(homeMsgListBean.data.list);
                        adapter.setData(mData);
                        EmptyViewUtil.showEmptyViewNoData(EmptyViewUtil.MESSAGE, false, adapter, listView);
                    } else {
                        ToastUtil.show(MsgsRemindActivity.this, homeMsgListBean.msg);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listView.setRefreshing(false);
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
