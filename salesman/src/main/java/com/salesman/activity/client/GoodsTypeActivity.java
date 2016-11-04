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
 * 修改商铺类型界面
 * Created by LiHuai on 2016/2/2.
 */
public class GoodsTypeActivity extends BaseActivity implements View.OnClickListener, UltimateListView.OnItemClickListener {
    public static final String TAG = GoodsTypeActivity.class.getSimpleName();
    public static final int FLAG = 1002;

    private TextView tvBack;
    private String goodsType = "";
    // 商铺id
    private String shopId = "";

    private UltimateListView listView;
    private ShopTypeListAdapter adapter;
    private List<ShopTypeBean> mDatas = BeanListHolder.getGoodsTypeList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.common_layout_list_0);
    }

    @Override
    protected void initView() {
        tvBack = (TextView) findViewById(R.id.tv_top_left);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.goods_type);
        TextView tvRight = (TextView) findViewById(R.id.tv_top_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.affirm);

        goodsType = getIntent().getStringExtra("goodsType");
        shopId = getIntent().getStringExtra("shopId");

        listView = (UltimateListView) findViewById(R.id.lv_common_0);
        if (!TextUtils.isEmpty(goodsType)) {
            String[] types = goodsType.split(",");
            for (String type : types) {
                for (ShopTypeBean data : mDatas) {
                    if (data.label.equals(type)) {
                        data.setChecked(true);
                    }
                }
            }
        }
        adapter = new ShopTypeListAdapter(this, mDatas, false);
        listView.setAdapter(adapter);

        tvBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        listView.setOnItemClickListener(this);
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
    private List<ShopTypeBean> getTypeBean() {
        List<ShopTypeBean> data = new ArrayList<>();
        for (ShopTypeBean mData : mDatas) {
            if (mData.isChecked()) {
                data.add(mData);
            }
        }
        return data;
    }

    private void saveMessage() {
        if (!isSelected()) {
            ToastUtil.show(this, "请选择主营商品类型");
            return;
        }

        List<ShopTypeBean> list = getTypeBean();
        final StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).label);
            if (i != list.size() - 1) {
                sb.append(",");
            }
        }
        if (!TextUtils.isEmpty(sb.toString())) {
            if (!TextUtils.isEmpty(shopId)) {
                String url = Constant.moduleEditClient;
                Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
                map.put("userId", SalesManApplication.g_GlobalObject.getmUserInfo().getUserId());
                map.put("shopId", shopId);
                map.put("mainProduct", ReplaceSymbolUtil.transcodeToUTF8(sb.toString()));
                LogUtils.d(TAG, url + BaseHelper.getParams(map));
                VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.d(TAG, response);
                        BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                        if (null != baseBean) {
                            if (baseBean.success) {
                                Intent intent = getIntent();
                                intent.putExtra("goodsType", sb.toString());
                                setResult(FLAG, intent);
                                finish();
                            } else {
                                ToastUtil.show(GoodsTypeActivity.this, baseBean.msg);
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
                intent.putExtra("goodsType", sb.toString());
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        ShopTypeBean bean = mDatas.get(position);
        if (bean.isChecked()) {
            bean.setChecked(false);
        } else {
            bean.setChecked(true);
        }
        adapter.notifyDataSetChanged();
    }
}
