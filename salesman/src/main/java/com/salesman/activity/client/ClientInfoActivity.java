package com.salesman.activity.client;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.guide.NewActionGuideActivity;
import com.salesman.activity.home.SingleSelection2Activity;
import com.salesman.activity.home.SingleSelectionActivity;
import com.salesman.activity.work.ShopOrderListActivity;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.ClientInfoBean;
import com.salesman.entity.SingleSelectionBean;
import com.salesman.network.BaseHelper;
import com.salesman.presenter.RequestDataPresenter;
import com.salesman.utils.StaticData;
import com.salesman.utils.StringUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.VisitLineUtil;
import com.salesman.view.OnCommonListener;
import com.salesman.views.CircleHeadView;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 客户资料界面
 * Created by LiHuai on 2016/10/09.
 */
public class ClientInfoActivity extends BaseActivity implements View.OnClickListener, OnCommonListener, VisitLineUtil.VisitLineCallBack {
    public final String TAG = ClientInfoActivity.class.getSimpleName();
    private final int FLAG = 2006;

    private String shopId = "", lineId = "", salesmanId = "", registerTel = "", vipType = "";
    private String shopNo = "", oldLineId = "", storeId = "";
    private RequestDataPresenter mPresenter = new RequestDataPresenter(this);
    private List<Integer> circleList = StaticData.getCircleColorList();

    private TextView tvShopName, tvShopNo, tvBossName, tvTel1, tvTel2, tvArea, tvDingge, tvAddress, tvSalesman, tvLine, tvChangeLine;
    private TextView tvStoreName, tvTimeOrder, tvMobile, tvNoOrder, tvMoney, tvStatus;
    private CircleHeadView hvOrder, hvShop;
    private View layDetail;
    private CardView cardView;
    private ImageView ivHeart, ivCall, ivZiYing;
    private VisitLineUtil visitLineUtil;
    private ArrayList<SingleSelectionBean> mLines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_client_info);
    }

    @Override
    protected void initView() {
        super.initView();
        TextView tvLeft = findView(R.id.tv_top_left);
        tvLeft.setOnClickListener(this);
        TextView tvTitle = findView(R.id.tv_top_title);
        tvTitle.setText("客户资料");

        tvShopName = findView(R.id.tv_shop_name);
        tvShopNo = findView(R.id.tv_shop_no);
        tvBossName = findView(R.id.tv_boss_name);
        tvTel1 = findView(R.id.tv_tel1);
        tvTel2 = findView(R.id.tv_tel2);
        tvArea = findView(R.id.tv_area);
        tvDingge = findView(R.id.tv_dingge);
        tvAddress = findView(R.id.tv_address);
        tvSalesman = findView(R.id.tv_salesman);
        tvLine = findView(R.id.tv_line);
        tvChangeLine = findView(R.id.tv_change_line);

        tvStoreName = findView(R.id.tv_name_store);
        tvTimeOrder = findView(R.id.tv_time_order);
        tvMobile = findView(R.id.tv_mobile);
        tvNoOrder = findView(R.id.tv_no_order);
        tvMoney = findView(R.id.tv_money_order);
        tvStatus = findView(R.id.tv_state_order);
        hvOrder = findView(R.id.hv_order);
        hvShop = findView(R.id.hv_shop);
        cardView = findView(R.id.cd_info);
        ivHeart = findView(R.id.iv_heart);
        ivCall = findView(R.id.iv_call);
        ivZiYing = findView(R.id.iv_ziying);
        layDetail = findView(R.id.lay_client_detail);

        visitLineUtil = new VisitLineUtil();

        ivHeart.setOnClickListener(this);
        ivCall.setOnClickListener(this);
        layDetail.setOnClickListener(this);
        tvChangeLine.setOnClickListener(this);
        visitLineUtil.setVisitLineListener(this);
        cardView.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        shopId = getIntent().getStringExtra("shopId");
        lineId = getIntent().getStringExtra("lineId");
        mPresenter.getData();

        hvShop.setCircleColorResources(StaticData.getImageId(circleList));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.iv_call:// 打电话
                if (!TextUtils.isEmpty(registerTel)) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + registerTel);
                    intent.setData(data);
                    startActivity(intent);
                }
                break;
            case R.id.iv_heart:// 重点客户
                markVipType();
                break;
            case R.id.lay_client_detail:// 详细资料
                Intent intent = new Intent(this, ClientDetailNewActivity.class);
                intent.putExtra("shopId", shopId);
                intent.putExtra("lineId", lineId);
                startActivity(intent);
                break;
            case R.id.tv_change_line:// 更改线路
                if (!TextUtils.isEmpty(salesmanId)) {
//                    if (mLines.isEmpty()) {
                        showProgressDialog(getString(R.string.loading1), false);
                        visitLineUtil.getVisitLineData(salesmanId);
//                    } else {
//                        VisitLineUtil.setSelectItem(mLines, oldLineId);
//                        Intent salesIntent = new Intent(ClientInfoActivity.this, SingleSelection2Activity.class);
//                        salesIntent.putParcelableArrayListExtra("data", mLines);
//                        salesIntent.putExtra(SingleSelectionActivity.TITLE, "线路选择");
//                        startActivityForResult(salesIntent, FLAG);
//                    }
                }
                break;
            case R.id.cd_info:// 历史订单
                Intent orderIntent = new Intent(this, ShopOrderListActivity.class);
                orderIntent.putExtra("storeId", storeId);
                startActivity(orderIntent);
                break;

        }
    }

    @Override
    public Context getRequestContext() {
        return this;
    }

    @Override
    public String getRequestUrl() {
        return Constant.moduleClientInfo;
    }

    @Override
    public Map<String, String> getRequestParam() {
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("shopId", shopId);
        map.put("lineId", lineId);
        return map;
    }

    @Override
    public void showLoading() {
        showProgressDialog(getString(R.string.loading1), false);
    }

    @Override
    public void hideLoading() {
        dismissProgressDialog();
    }

    @Override
    public void requestSuccess(String response) {
        LogUtils.d(TAG, response);
        ClientInfoBean clientInfoBean = GsonUtils.json2Bean(response, ClientInfoBean.class);
        if (clientInfoBean != null) {
            if (clientInfoBean.success && clientInfoBean.data != null) {
                ClientInfoBean.ClientInfo clientInfo = clientInfoBean.data;
                salesmanId = clientInfo.salesmanId;
                oldLineId = clientInfo.lineId;
                shopNo = clientInfo.shopNo;
                storeId = String.valueOf(clientInfo.storeId);
                vipType = clientInfo.vipType;
                registerTel = clientInfo.registerTel;
                tvShopName.setText(clientInfo.shopName);
                tvShopNo.setText(clientInfo.shopNo);
                tvBossName.setText(clientInfo.bossName);
                tvTel1.setText(clientInfo.registerTel);
                tvTel2.setText(clientInfo.bossTel);
                tvArea.setText(clientInfo.province + clientInfo.city + clientInfo.area);
                tvDingge.setText(clientInfo.spGroupName);
                tvAddress.setText(clientInfo.shopAddress);
                tvSalesman.setText(clientInfo.salesmanName);
                if (TextUtils.isEmpty(clientInfo.lineId)) {
                    tvLine.setText("暂无安排");
                } else {
                    tvLine.setText(clientInfo.lineName);
                }
                if ("1".equals(clientInfo.vipType)) {
                    ivHeart.setImageResource(R.drawable.heart_red);
                } else {
                    ivHeart.setImageResource(R.drawable.heart_grey);
                }

                List<ClientInfoBean.ClientInfo.Order> list = clientInfo.list;
                if (list != null && !list.isEmpty()) {
                    cardView.setVisibility(View.VISIBLE);
                    ClientInfoBean.ClientInfo.Order order = list.get(0);
                    hvOrder.setVisibility(View.GONE);
                    tvStoreName.setText("最新交易记录");
                    tvTimeOrder.setText(order.addTime);
                    tvMobile.setText(order.mobile);
                    tvNoOrder.setText(order.orderNo);
                    tvMoney.setText(String.valueOf(order.orderPrice));
                    StringUtil.setTextAndColor(this, tvStatus, order.status);
                    if (1 == order.isZj) {
                        ivZiYing.setVisibility(View.VISIBLE);
                    } else {
                        ivZiYing.setVisibility(View.GONE);
                    }
                } else {
                    cardView.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void requestFail() {

    }

    /**
     * 标为重点客户
     */
    private void markVipType() {
        if (TextUtils.isEmpty(vipType)) {
            return;
        }
        String url = Constant.moduleEditClient;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("shopId", shopId);
        if ("0".equals(vipType)) {
            map.put("vipType", "1");
        } else {
            map.put("vipType", "0");
        }
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean) {
                    if (baseBean.success) {
                        mPresenter.getData();
                    } else {
                        ToastUtil.show(ClientInfoActivity.this, baseBean.msg);
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
     * 保存客户线路
     *
     * @param nowLineId
     * @param oldLineId
     */
    private void saveLineForClient(String nowLineId, String oldLineId) {
        String url = Constant.moduleSaveClientLine;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("salesmanId", salesmanId);
        map.put("shopNo", shopNo);
        if ("ALL".equals(nowLineId)) {
            map.put("lineId", "");
        } else {
            map.put("lineId", nowLineId);
        }
        map.put("oldLineId", oldLineId);
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean) {
                    if (baseBean.success) {
                        mPresenter.getData();
                    } else {
                        ToastUtil.show(ClientInfoActivity.this, baseBean.msg);
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
    public void onSuccess(ArrayList<SingleSelectionBean> data) {
        dismissProgressDialog();
        mLines.clear();
        mLines.addAll(data);
        VisitLineUtil.setSelectItem(mLines, oldLineId);
        Intent salesIntent = new Intent(ClientInfoActivity.this, SingleSelection2Activity.class);
        salesIntent.putParcelableArrayListExtra("data", mLines);
        salesIntent.putExtra(SingleSelectionActivity.TITLE, "线路选择");
        startActivityForResult(salesIntent, FLAG);
    }

    @Override
    public void onError() {
        dismissProgressDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FLAG:// 线路
                if (null != data) {
                    SingleSelectionBean bean = data.getParcelableExtra(SingleSelectionActivity.SELECT_BEAN);
                    if (null != bean) {
                        // 保存线路
                        saveLineForClient(bean.id, oldLineId);
                    }
                }
                break;
        }
    }
}
