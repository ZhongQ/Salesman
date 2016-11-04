package com.salesman.activity.client;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.adapter.PayWayListAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.PayWayBean;
import com.salesman.global.BeanListHolder;
import com.salesman.network.BaseHelper;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.ToastUtil;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.listview.UltimateListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 修改支付方式界面
 * Created by LiHuai on 2016/2/2.
 */
public class PayWayActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = PayWayActivity.class.getSimpleName();
    public static final int FLAG = 1003;

    private TextView tvBack;
    private String payWay = "";
    // 商铺id
    private String shopId = "";

    private UltimateListView listView;
    private PayWayListAdapter adapter;
    private List<PayWayBean> mDatas = BeanListHolder.getPayWayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.common_layout_list_0);
    }

    @Override
    protected void initView() {
        tvBack = (TextView) findViewById(R.id.tv_top_left);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.pay_way);
        TextView tvRight = (TextView) findViewById(R.id.tv_top_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.affirm);

        payWay = getIntent().getStringExtra("payWay");
        shopId = getIntent().getStringExtra("shopId");

        listView = (UltimateListView) findViewById(R.id.lv_common_0);
        if (!TextUtils.isEmpty(payWay)) {
            String[] types = payWay.split(",");
            for (String type : types) {
                for (PayWayBean data : mDatas) {
                    if (data.payName.equals(type)) {
                        data.setCheck(true);
                    }
                }
            }
        }
        adapter = new PayWayListAdapter(this, mDatas);
        listView.setAdapter(adapter);

        tvBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    /**
     * 判读列表是否有选中类型
     */
    private boolean isSelected() {
        List<PayWayBean> list = adapter.getData();
        for (PayWayBean payWayBean : list) {
            if (payWayBean.isCheck()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取选中的对象
     */
    private List<PayWayBean> getTypeBean() {
        List<PayWayBean> data = new ArrayList<>();
        for (PayWayBean mData : mDatas) {
            if (mData.isCheck()) {
                data.add(mData);
            }
        }
        return data;
    }

    private void saveMessage() {
        if (!isSelected()) {
            ToastUtil.show(this, "请选择支付方式");
            return;
        }

        List<PayWayBean> list = getTypeBean();
        final StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).payName);
            if (i != list.size() - 1){
                sb.append(",");
            }
        }
        if (!TextUtils.isEmpty(sb.toString())) {
            if (!TextUtils.isEmpty(shopId)) {
                String url = Constant.moduleEditClient;
                Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
                map.put("userId", SalesManApplication.g_GlobalObject.getmUserInfo().getUserId());
                map.put("shopId", shopId);
                map.put("ipay", ReplaceSymbolUtil.transcodeToUTF8(sb.toString()));
                LogUtils.d(TAG, url + BaseHelper.getParams(map));
                VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.d(TAG, response);
                        BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                        if (null != baseBean) {
                            if (baseBean.success) {
                                Intent intent = getIntent();
                                intent.putExtra("payWay", sb.toString());
                                setResult(FLAG, intent);
                                finish();
                            } else {
                                ToastUtil.show(PayWayActivity.this, baseBean.msg);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                VolleyController.getInstance(this).addToQueue(post);
            } else {
                Intent intent = getIntent();
                intent.putExtra("payWay", sb.toString());
                setResult(FLAG, intent);
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.tv_top_right:
                saveMessage();
                break;
        }
    }
}
