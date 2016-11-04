package com.salesman.activity.client;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.work.ShopOrderListActivity;
import com.salesman.adapter.ClientDetailAdapter;
import com.salesman.adapter.ShopImageAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.common.GsonUtils;
import com.salesman.entity.ClientDetailBean;
import com.salesman.entity.ShopDetail;
import com.salesman.entity.ShopDetailsBean;
import com.salesman.entity.UploadImageBean;
import com.salesman.network.BaseHelper;
import com.salesman.utils.PictureUtil;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.StringUtil;
import com.salesman.utils.UserInfoPreference;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.InnerGridView;
import com.studio.jframework.widget.listview.InnerListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 客户详情界面
 * 版本V1.4.0
 * Created by LiHuai on 2016/06/20.
 */
public class ClientDetailNewActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String TAG = ClientDetailNewActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private Button btnLook;
    private InnerListView mListView;
    private ClientDetailAdapter adapter;
    private List<ClientDetailBean> mData = new ArrayList<>();
    private ShopDetail bean;
    private String storeId = "";

    // 图片部分
    private InnerGridView gridView;
    private List<UploadImageBean> uploadImgList = new ArrayList<>();
    private ShopImageAdapter imgAdpter;

    private String[] keyArray = new String[]{
            "业务员", "业务员手机号", "线路", "商铺编号", "商铺名称", "商铺类型", "固定电话", "所属区域", "定格", "详细地址",
            "负责人", "手机号码", "负责人备注",
            "是否注册店宝", "注册手机", "营业执照", "营业面积", "营业时间", "人员数量", "配送员数量", "配送合作商", "主营商品", "主营商品占比", "日均营业额",
            "支付平台", "SKU数", "其他合作平台", "是否连锁", "烟草许可证", "POS机", "电脑", "WiFi"
    };
    private String[] keyArray2 = new String[]{
            "业务员", "业务员手机号", "线路", "商铺编号", "商铺名称", "商铺类型", "固定电话", "所属区域", "定格", "详细地址",
            "负责人", "手机号码", "负责人备注",
            "是否注册店宝", "营业面积", "营业时间", "人员数量", "配送员数量", "配送合作商", "主营商品", "主营商品占比", "日均营业额",
            "支付平台", "SKU数", "其他合作平台", "是否连锁", "烟草许可证", "POS机", "电脑", "WiFi"
    };
    private String shopId = "", lineId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_client_detail_new);

        getClientDetailsDatas();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        shopId = getIntent().getStringExtra("shopId");
        lineId = getIntent().getStringExtra("lineId");
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        tvBack.setVisibility(View.VISIBLE);
        TextView tvTitel = (TextView) findViewById(R.id.tv_top_title);
        tvTitel.setText(R.string.client_detail);
        ImageView ivEdit = (ImageView) findViewById(R.id.iv_top_right2);
        ivEdit.setImageResource(R.drawable.client_edit_icon);
//        ImageView ivJiaoYi = (ImageView) findViewById(R.id.iv_top_right1);
//        ivJiaoYi.setImageResource(R.drawable.client_jiaoyi_icon);
        btnLook = (Button) findViewById(R.id.btn_affirm);
        btnLook.setText("查看交易记录");
        btnLook.setVisibility(View.GONE);

        mListView = (InnerListView) findViewById(R.id.lv_client_detail);
        adapter = new ClientDetailAdapter(this, mData);
        mListView.setAdapter(adapter);

        // 图片部分
        gridView = (InnerGridView) findViewById(R.id.gv_client_detail);
        imgAdpter = new ShopImageAdapter(this, uploadImgList);
        gridView.setAdapter(imgAdpter);

        tvBack.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
//        ivJiaoYi.setOnClickListener(this);
        btnLook.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
    }

    private void setData(String[] value, boolean isRegister) {
        mData.clear();
        int l2 = value.length;
        if (isRegister) {
            int l1 = keyArray.length;
            int l3 = Math.min(l1, l2);
            for (int i = 0; i < l3; i++) {
                mData.add(new ClientDetailBean(keyArray[i], value[i]));
            }
        } else {
            int l1 = keyArray2.length;
            int l3 = Math.min(l1, l2);
            for (int i = 0; i < l3; i++) {
                mData.add(new ClientDetailBean(keyArray2[i], value[i]));
            }
        }
        adapter.setData(mData);
    }

    public void onEventMainThread(String action) {
        if (EventBusConfig.CLIENT_DETAIL_REFRESH.equals(action)) {
            getClientDetailsDatas();
        }
    }

    /**
     * 获取店铺详情数据
     */
    private void getClientDetailsDatas() {
        showProgressDialog(getString(R.string.loading1), false);
        String url = Constant.moduleClientDetails;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        if (!TextUtils.isEmpty(shopId)) {
            map.put("shopId", shopId);
        }
        if (!TextUtils.isEmpty(lineId)) {
            map.put("lineId", lineId);
        }
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                ShopDetailsBean shopDetailsBean = GsonUtils.json2Bean(response, ShopDetailsBean.class);
                if (null != shopDetailsBean && shopDetailsBean.success) {
                    if (null != shopDetailsBean.data && null != shopDetailsBean.data.resultObj) {
                        bean = shopDetailsBean.data.resultObj;
                        storeId = String.valueOf(bean.storeId);
                        initPic(bean.picList);
                        // 是否注册店宝
                        if ("1".equals(bean.isRegister)) {
                            String[] valueArray = new String[]{
                                    bean.salesmanName, bean.salesmanTel, bean.lineName, bean.shopNo, bean.shopName, bean.shopTypeName, bean.telephone, (bean.province + bean.city + bean.area), bean.spGroupName, bean.shopAddress,
                                    bean.bossName, bean.bossTel, ReplaceSymbolUtil.reverseReplaceHuanHang(bean.remarks),
                                    StringUtil.judgeStr(bean.isRegister), bean.registerTel, bean.shopLicense, bean.shopArea, yingYeTime(bean), bean.staffNum, bean.distributionNum, bean.dcShop, bean.mainProduct, bean.saleRatio, bean.turnover,
                                    bean.ipay, bean.sku, judgeStr(bean.otherPatform), judgeStr(bean.isMultipleShop), judgeStr(bean.baccyLicence), judgeStr(bean.isPos), judgeStr(bean.isComputer), judgeStr(bean.isWifi)
                            };
                            setData(valueArray, true);
                            btnLook.setVisibility(View.GONE);
                        } else {
                            btnLook.setVisibility(View.GONE);
                            String[] valueArray2 = new String[]{
                                    bean.salesmanName, bean.salesmanTel, bean.lineName, bean.shopNo, bean.shopName, bean.shopTypeName, bean.telephone, (bean.province + bean.city + bean.area), bean.spGroupName, bean.shopAddress,
                                    bean.bossName, bean.bossTel, ReplaceSymbolUtil.reverseReplaceHuanHang(bean.remarks),
                                    StringUtil.judgeStr(bean.isRegister), bean.shopArea, yingYeTime(bean), bean.staffNum, bean.distributionNum, bean.dcShop, bean.mainProduct, bean.saleRatio, bean.turnover,
                                    bean.ipay, bean.sku, judgeStr(bean.otherPatform), judgeStr(bean.isMultipleShop), judgeStr(bean.baccyLicence), judgeStr(bean.isPos), judgeStr(bean.isComputer), judgeStr(bean.isWifi)
                            };
                            setData(valueArray2, false);
                        }
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
     * 初始化图片
     */
    private void initPic(List<String> strList) {
        uploadImgList.clear();
        if (strList == null || strList.isEmpty()) {
            gridView.setVisibility(View.GONE);
            return;
        }
        gridView.setVisibility(View.VISIBLE);
        for (String s : strList) {
            UploadImageBean uploadImageBean = new UploadImageBean(-1);
            uploadImageBean.setPicUrl(s);
            uploadImgList.add(uploadImageBean);
        }
        imgAdpter.setData(uploadImgList);
    }

    private String yingYeTime(ShopDetail detail) {
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(detail.startShopHours)) {
            sb.append(detail.startShopHours);
            sb.append("-");
        }
        if (!TextUtils.isEmpty(detail.endShopHours)) {
            sb.append(detail.endShopHours);
        }
        return sb.toString();
    }

    /**
     * 是否字符判断
     */
    private String judgeStr(String str) {
        if ("1".equals(str)) {
            return "有";
        } else {
            return "无";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.iv_top_right2:
                Intent intent = new Intent(this, ClientCompileActivity.class);
                intent.putExtra("clientDetail", bean);
                startActivity(intent);
                break;
            case R.id.iv_top_right1:// 查看交易记录
                Intent orderIntent = new Intent(this, ShopOrderListActivity.class);
                orderIntent.putExtra("storeId", storeId);
                startActivity(orderIntent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PictureUtil.lookBigPicFromNet(this, position, imgAdpter.getData());
    }

}
