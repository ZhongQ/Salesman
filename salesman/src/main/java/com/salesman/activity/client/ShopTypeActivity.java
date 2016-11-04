package com.salesman.activity.client;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.adapter.ShopTypeListAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.ShopTypeBean;
import com.salesman.network.BaseHelper;
import com.salesman.utils.ClientTypeUtil;
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
 * 修改商铺类型界面
 * Created by LiHuai on 2016/2/2.
 */
public class ShopTypeActivity extends BaseActivity implements View.OnClickListener, ClientTypeUtil.GetClientTypeListener, UltimateListView.OnItemClickListener {
    public static final String TAG = ShopTypeActivity.class.getSimpleName();
    public static final int FLAG = 1001;

    private TextView tvBack;
    private String shopType = "";
    // 商铺id
    private String shopId = "";
    private UltimateListView listView;
    private ShopTypeListAdapter adapter;
    private List<ShopTypeBean> mDatas = new ArrayList<>();
    private ClientTypeUtil clientTypeUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.common_layout_list_0);
    }

    @Override
    protected void initView() {
        tvBack = (TextView) findViewById(R.id.tv_top_left);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.shop_type);
        TextView tvRight = (TextView) findViewById(R.id.tv_top_right);
        tvRight.setVisibility(View.GONE);
        tvRight.setText(R.string.affirm);

        shopType = getIntent().getStringExtra("shopType");
        shopId = getIntent().getStringExtra("shopId");
        listView = (UltimateListView) findViewById(R.id.lv_common_0);

        adapter = new ShopTypeListAdapter(this, mDatas, true);
        listView.setAdapter(adapter);
        clientTypeUtil = new ClientTypeUtil();

        tvBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        clientTypeUtil.setClientTypeListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        mDatas.clear();
        if (ClientTypeUtil.isSecondRequest()) {
            showProgressDialog(getString(R.string.loading1), false);
            clientTypeUtil.getClientTypeData();
        }
        mDatas = ClientTypeUtil.getShopTypeList();
        if (!TextUtils.isEmpty(shopType)) {
            for (ShopTypeBean mData : mDatas) {
                if (mData.label.equals(shopType)) {
                    mData.setChecked(true);
                }
            }
        }
        adapter.setData(mDatas);
    }

    /**
     * 判读列表是否有选中类型
     */
    private boolean isSelected() {
        List<ShopTypeBean> list = adapter.getData();
        for (ShopTypeBean typeBean : list) {
            if (typeBean.isChecked()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取选中的对象
     */
    private ShopTypeBean getTypeBean() {
        List<ShopTypeBean> data = adapter.getData();
        for (ShopTypeBean typeBean : data) {
            if (typeBean.isChecked()) {
                return typeBean;
            }
        }
        return null;
    }

    private void saveMessage() {
        if (!isSelected()) {
            ToastUtil.show(this, "请选择商铺类型");
            return;
        }

        final ShopTypeBean bean = getTypeBean();
        if (null != bean) {
            if (!TextUtils.isEmpty(shopId)) {
                String url = Constant.moduleEditClient;
                Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
                map.put("userId", SalesManApplication.g_GlobalObject.getmUserInfo().getUserId());
                map.put("shopId", shopId);
                map.put("shopType", ReplaceSymbolUtil.transcodeToUTF8(bean.getTypeName()));
                LogUtils.d(TAG, url + BaseHelper.getParams(map));
                VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.d(TAG, response);
                        BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                        if (null != baseBean) {
                            if (baseBean.success) {
                                Intent intent = getIntent();
                                intent.putExtra("shopType", bean.getTypeName());
                                setResult(FLAG, intent);
                                finish();
                            } else {
                                ToastUtil.show(ShopTypeActivity.this, baseBean.msg);
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
                intent.putExtra("shopType", bean.label);
                intent.putExtra("shopTypeId", bean.value);
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

    @Override
    public void onGetClientTypeSuccess() {
        dismissProgressDialog();
    }

    @Override
    public void onGetClientTypeFail() {
        dismissProgressDialog();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        adapter.setSingleSelect(position);
        ShopTypeBean bean = mDatas.get(position);
        Intent intent = getIntent();
        intent.putExtra("shopType", bean.label);
        intent.putExtra("shopTypeId", bean.value);
        setResult(FLAG, intent);
        finish();
    }
}
