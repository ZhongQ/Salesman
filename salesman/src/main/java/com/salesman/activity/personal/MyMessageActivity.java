package com.salesman.activity.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.salesman.R;
import com.salesman.activity.home.DailyDetailsActivity;
import com.salesman.activity.manage.NoticeListActivity;
import com.salesman.adapter.viewholder.LogMsgsListHolder;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.common.GsonUtils;
import com.salesman.entity.MessageListBean;
import com.salesman.network.BaseHelper;
import com.salesman.presenter.RequestDataPresenter;
import com.salesman.utils.EmptyViewUtil;
import com.salesman.utils.StaticData;
import com.salesman.view.OnCommonListener;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.salesman.utils.DeviceUtil.dip2px;

/**
 * 我的消息界面
 * Created by LiHuai on 2016/10/19.
 */
public class MyMessageActivity extends BaseActivity implements OnCommonListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnMoreListener, RecyclerArrayAdapter.OnItemClickListener {
    public final String TAG = MyMessageActivity.class.getSimpleName();
    private RequestDataPresenter mPresenter = new RequestDataPresenter(this);

    private EasyRecyclerView recyclerView;
    private RecyclerArrayAdapter<MessageListBean.MessageBean> adapter;
    private List<Integer> imgIdList = StaticData.getCircleColorList();
    private int pageNo = 1;
    private int pageSize = 10;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.common_layout_rv);
    }

    @Override
    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.message_centre);
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        tvBack.setOnClickListener(this);

        recyclerView = findView(R.id.rv_common);
        initRecyclerView(recyclerView);
        EmptyViewUtil.initRecyclerEmptyView(recyclerView, EmptyViewUtil.MESSAGE);
        // item分割线
        DividerDecoration itemDecoration = new DividerDecoration(getResources().getColor(R.color.color_e5e5e5), dip2px(this, 0.5f), 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new RecyclerArrayAdapter<MessageListBean.MessageBean>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new LogMsgsListHolder(parent, R.layout.item_message_log);
            }
        };
        recyclerView.setAdapterWithProgress(adapter);

        recyclerView.setRefreshListener(this);
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        onRefresh();
    }

    /**
     * 最新消息列表数据
     */
    private void getMessageList() {
//        showProgressDialog(getString(R.string.loading1), false);
        String url = Constant.moduleNewestMsgsList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                MessageListBean messageListBean = GsonUtils.json2Bean(response, MessageListBean.class);
                if (null != messageListBean && messageListBean.success) {
                    if (null != messageListBean.data && null != messageListBean.data.list) {

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
     * 初始化历史消息
     *
     * @param list
     */
    private void setHistoryData(List<MessageListBean.MessageBean> list) {
        if (pageNo == 1) {
            adapter.clear();
        }
        setHeadBg(list);
        adapter.addAll(list);
        if (list.size() < pageSize) {
            if (pageNo != 1) {
                adapter.stopMore();
            } else {
                adapter.pauseMore();
            }
        } else {
            mPresenter.setmEnableLoadMore(true);
            pageNo++;
        }
    }

    private void setHeadBg(List<MessageListBean.MessageBean> list) {
        for (MessageListBean.MessageBean messageBean : list) {
            messageBean.setImgId(StaticData.getImageId(imgIdList));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(EventBusConfig.MSGS_VIEW_GONE);
        finish();
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        mPresenter.refreshData();
    }

    @Override
    public Context getRequestContext() {
        return this;
    }

    @Override
    public String getRequestUrl() {
        return Constant.moduleLogHistoryMessageList;
    }

    @Override
    public Map<String, String> getRequestParam() {
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        return map;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void requestSuccess(String response) {
        MessageListBean messageListBean = GsonUtils.json2Bean(response, MessageListBean.class);
        if (null != messageListBean) {
            if (messageListBean.success && null != messageListBean.data && null != messageListBean.data.list) {
                setHistoryData(messageListBean.data.list);
            }
        }
    }

    @Override
    public void requestFail() {

    }

    @Override
    public void onItemClick(int position) {
        MessageListBean.MessageBean messageBean = adapter.getItem(position);
        switch (messageBean.type) {
            case 3:
            case 2:
                Intent intentDetails = new Intent(this, DailyDetailsActivity.class);
                intentDetails.putExtra("reportId", messageBean.reportId);
                startActivity(intentDetails);
                break;
            case 1:
                startActivity(new Intent(this, NoticeListActivity.class));
                break;
        }
    }

    @Override
    public void onMoreShow() {
        mPresenter.loadMore();
    }

    @Override
    public void onMoreClick() {

    }
}
