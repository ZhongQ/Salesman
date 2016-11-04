package com.salesman.activity.client;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.salesman.R;
import com.salesman.activity.home.SigninMapActivity;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.ShopDetail;
import com.salesman.entity.ShopDetailsBean;
import com.salesman.network.BaseHelper;
import com.salesman.utils.CityUtil;
import com.salesman.utils.ClientTypeUtil;
import com.salesman.utils.PhoneNumberUtil;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.views.citypicker.CityPicker;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.dialog.DialogCreator;

import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 添加客户界面
 * 版本V1.4.0
 * Created by LiHuai on 2016/06/21.
 */
public class AddClientStep1Activity extends BaseActivity implements View.OnClickListener, CityUtil.OnAreaListener, ClientTypeUtil.GetClientTypeListener {
    public static final String TAG = AddClientStep1Activity.class.getSimpleName();
    public static final int FLAG = 2002;

    private TextView tvBack, tvRight;
    private LinearLayout layType, layQuyu;
    private TextView tvType, tvQuyu;
    private EditText etName, etTel, etAddress, etBoss, etBossTel, etBossRemark;
    private Button btnNext;
    private ImageView ivLocation;

    private Dialog mCityPickerDialog;
    private TextView tvSelectCity;
    private CityPicker cityPicker;
    private String province = "", city = "", district = "";
    private int provinceId = 0, cityId = 0, districtId = 0;
    // 百度地图地理编码
    private GeoCoder mSearch;
    private double lat;
    private double lng;
    private CityUtil cityUtil;
    private ClientTypeUtil clientTypeUtil;
    // 字段
    private String shopName, shopType, shopTypeId, shopTel, shopArea, shopAddress, bossName, bossTel, boosRemark;
    private boolean isSubmit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_add_client_step1);

        initEvents();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        tvBack = (TextView) findViewById(R.id.tv_top_left);
        TextView tvTitel = (TextView) findViewById(R.id.tv_top_title);
        tvTitel.setText(R.string.add_client);
        tvRight = (TextView) findViewById(R.id.tv_top_right);
        tvRight.setText(R.string.submit);
        tvRight.setVisibility(View.VISIBLE);
        layType = (LinearLayout) findViewById(R.id.lay_type);
        layQuyu = (LinearLayout) findViewById(R.id.lay_quyu);
        tvType = (TextView) findViewById(R.id.tv_type_client);
        tvQuyu = (TextView) findViewById(R.id.tv_quyu_client);
        btnNext = (Button) findViewById(R.id.btn_affirm);
        btnNext.setText("继续完善");
        etName = (EditText) findViewById(R.id.et_name_client);
        etTel = (EditText) findViewById(R.id.et_tel_client);
        etAddress = (EditText) findViewById(R.id.et_address_client);
        etBoss = (EditText) findViewById(R.id.et_name_boss);
        etBossTel = (EditText) findViewById(R.id.et_tel_boss);
        etBossRemark = (EditText) findViewById(R.id.et_remark_boss);
        ivLocation = (ImageView) findViewById(R.id.iv_location_client);

        cityUtil = new CityUtil();
        clientTypeUtil = new ClientTypeUtil();
        mCityPickerDialog = DialogCreator.createNormalDialog(this, R.layout.dialog_citypicker, DialogCreator.Position.BOTTOM);
        tvSelectCity = (TextView) mCityPickerDialog.findViewById(R.id.tv_selectcity);
        cityPicker = (CityPicker) mCityPickerDialog.findViewById(R.id.citypicker);
        CityUtil.initCityPicker(cityPicker);
    }

    private void initEvents() {
        tvBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        layType.setOnClickListener(this);
        layQuyu.setOnClickListener(this);
        ivLocation.setOnClickListener(this);
        cityUtil.setOnAreaListener(this);
        clientTypeUtil.setClientTypeListener(this);

        tvSelectCity.setOnClickListener(this);
    }

    /**
     * 下一步
     */
    private void judgeMessage() {
        shopName = etName.getText().toString().trim();
        shopType = tvType.getText().toString().trim();
        shopTel = etTel.getText().toString().trim();
        shopArea = tvQuyu.getText().toString().trim();
        shopAddress = etAddress.getText().toString().trim();
        bossName = etBoss.getText().toString().trim();
        bossTel = etBossTel.getText().toString().trim();
        boosRemark = etBossRemark.getText().toString().trim();

        if (TextUtils.isEmpty(shopName)) {
            ToastUtil.show(this, getString(R.string.input_shop_name_please));
            return;
        }
        if (TextUtils.isEmpty(shopType) || TextUtils.isEmpty(shopTypeId)) {
            ToastUtil.show(this, getString(R.string.select_shop_type_please));
            return;
        }
        if (TextUtils.isEmpty(shopTel)) {
            ToastUtil.show(this, getString(R.string.input_shop_tel_please));
            return;
        }
        if (!PhoneNumberUtil.isPhone(shopTel)) {
            ToastUtil.show(this, getString(R.string.input_right_tel_please));
            return;
        }
        if (TextUtils.isEmpty(shopArea)) {
            ToastUtil.show(this, getString(R.string.select_shop_area_please));
            return;
        }
        if (TextUtils.isEmpty(shopAddress)) {
            ToastUtil.show(this, getString(R.string.input_shop_address_please));
            return;
        }
        if (TextUtils.isEmpty(bossName)) {
            ToastUtil.show(this, getString(R.string.input_boss_name_please));
            return;
        }
        if (TextUtils.isEmpty(bossTel)) {
            ToastUtil.show(this, getString(R.string.input_boss_tel_please));
            return;
        }
        if (!PhoneNumberUtil.isMobileNO(bossTel)) {
            ToastUtil.show(this, getString(R.string.input_right_mobile_please));
            return;
        }

        showProgressDialog(getString(R.string.loading1), false);
        getGPRSLatLon(city, shopAddress);
    }

    /**
     * 百度地图获取填入地址的经纬度
     */
    private void getGPRSLatLon(String cityStr, String address) {
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(listener);
        mSearch.geocode(new GeoCodeOption().city(cityStr).address(address));
    }

    private OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                dismissProgressDialog();
                //没有检索到结果
                Toast.makeText(AddClientStep1Activity.this, "无法获取您选择地区信息，请填写正确的地址", Toast.LENGTH_SHORT).show();
                return;
            }
            //获取地理编码结果
            lat = result.getLocation().latitude;
            lng = result.getLocation().longitude;
            LogUtils.d(TAG, lat + "地理编码" + lng);
            saveMessage();
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            dismissProgressDialog();
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                Toast.makeText(AddClientStep1Activity.this, "抱歉，未能找到结果", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (null != mSearch) {
            mSearch.destroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ShopTypeActivity.FLAG:// 商铺类型
                tvType.setText(data.getStringExtra("shopType"));
                shopTypeId = data.getStringExtra("shopTypeId");
                break;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case FLAG:
                etAddress.setText(data.getStringExtra("address"));
                break;
        }

    }

    private void saveMessage() {
        ShopDetail shopDetail = new ShopDetail();
        shopDetail.shopName = shopName;
        shopDetail.shopType = shopTypeId;// 商铺类型id
        shopDetail.telephone = shopTel;
        shopDetail.province = province;
        shopDetail.city = city;
        shopDetail.area = district;
        shopDetail.provinceId = provinceId;
        shopDetail.cityId = cityId;
        shopDetail.areaId = districtId;
        shopDetail.shopAddress = shopAddress;
        shopDetail.bossName = bossName;
        shopDetail.bossTel = bossTel;
        shopDetail.remarks = boosRemark;
        shopDetail.longitude = lng;
        shopDetail.latitude = lat;

        if (isSubmit) {
            postMessage();
        } else {
            dismissProgressDialog();
            EventBus.getDefault().post(shopDetail);
            Intent intent = new Intent(this, AddClientStep2Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("ClientDetail", shopDetail);
            startActivity(intent);
        }
    }

    /**
     * 提交资料
     */
    private void postMessage() {
        String url = Constant.moduleAddClient;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put(ShopDetailsBean.SHOPNAME, ReplaceSymbolUtil.transcodeToUTF8(shopName));
        map.put(ShopDetailsBean.SHOPTYPE, shopTypeId);
        map.put(ShopDetailsBean.TELEPHONE, shopTel);
        map.put(ShopDetailsBean.PROVINCE, ReplaceSymbolUtil.transcodeToUTF8(province));
        map.put(ShopDetailsBean.CITY, ReplaceSymbolUtil.transcodeToUTF8(city));
        map.put(ShopDetailsBean.AREA, ReplaceSymbolUtil.transcodeToUTF8(district));
        map.put(ShopDetailsBean.PROVINCEID, String.valueOf(provinceId));
        map.put(ShopDetailsBean.CITYID, String.valueOf(cityId));
        map.put(ShopDetailsBean.AREAID, String.valueOf(districtId));
        map.put(ShopDetailsBean.LONGITUDE, String.valueOf(lng));
        map.put(ShopDetailsBean.LATITUDE, String.valueOf(lat));
        map.put(ShopDetailsBean.SHOPADDRESS, ReplaceSymbolUtil.transcodeToUTF8(shopAddress));
        map.put(ShopDetailsBean.BOSSNAME, ReplaceSymbolUtil.transcodeToUTF8(bossName));
        map.put(ShopDetailsBean.BOSSTEL, bossTel);
        map.put(ShopDetailsBean.REMARKS, ReplaceSymbolUtil.transcodeToUTF8(ReplaceSymbolUtil.replaceHuanHang(boosRemark)));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean) {
                    if (baseBean.success) {
                        ToastUtil.show(AddClientStep1Activity.this, getResources().getString(R.string.add_client_success));
                        EventBus.getDefault().post(EventBusConfig.ADD_CLIENT_FINISH);
                    } else {
                        ToastUtil.show(AddClientStep1Activity.this, baseBean.msg);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
                ToastUtil.show(AddClientStep1Activity.this, getResources().getString(R.string.add_client_fail));
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
    }

    public void onEventMainThread(String action) {
        if (EventBusConfig.ADD_CLIENT_FINISH.equals(action)) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(EventBusConfig.ADD_CLIENT_FINISH);
    }

    /**
     * 省市区
     *
     * @param shengShiQu
     * @return
     */
    public String initShengShiQu(String shengShiQu) {
        if (!TextUtils.isEmpty(shengShiQu)) {
            String[] diquArray = shengShiQu.split(";");
            for (int i = 0; i < diquArray.length; i++) {
                switch (i) {
                    case 0:
                        province = diquArray[0];
                        break;
                    case 1:
                        city = diquArray[1];
                        break;
                    case 2:
                        district = diquArray[2];
                        break;
                }
            }
        }
        return province + city + district;
    }

    /**
     * 省市区id
     *
     * @param shengShiQuCode
     */
    public void initShengShiQuCode(String shengShiQuCode) {
        if (!TextUtils.isEmpty(shengShiQuCode)) {
            String[] diquArray = shengShiQuCode.split(";");
            for (int i = 0; i < diquArray.length; i++) {
                switch (i) {
                    case 0:
                        provinceId = TextUtils.isEmpty(diquArray[0]) ? 0 : Integer.parseInt(diquArray[0]);
                        break;
                    case 1:
                        cityId = TextUtils.isEmpty(diquArray[1]) ? 0 : Integer.parseInt(diquArray[1]);
                        break;
                    case 2:
                        districtId = TextUtils.isEmpty(diquArray[2]) ? 0 : Integer.parseInt(diquArray[2]);
                        break;
                }
            }
            LogUtils.d(TAG, shengShiQuCode);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                onBackPressed();
                break;
            case R.id.tv_top_right:// 提交
                isSubmit = true;
                judgeMessage();
                break;
            case R.id.btn_affirm:// 继续完善
                isSubmit = false;
                judgeMessage();
                break;
            case R.id.lay_type:// 商铺类型
                if (ClientTypeUtil.isSecondRequest()) {
                    showProgressDialog(getString(R.string.loading1), false);
                    clientTypeUtil.getClientTypeData();
                } else {
                    Intent intentType = new Intent(this, ShopTypeActivity.class);
                    intentType.putExtra("shopType", tvType.getText().toString());
                    startActivityForResult(intentType, ShopTypeActivity.FLAG);
                }
                break;
            case R.id.lay_quyu:// 所属区域
                if (CityUtil.isSecondRequest()) {
                    showProgressDialog(getString(R.string.loading1), false);
                    cityUtil.getAllCityUtil();
                } else {
                    mCityPickerDialog.show();
                }
                break;
            case R.id.tv_selectcity:// 选择城市确定
                tvQuyu.setText(initShengShiQu(cityPicker.getCity_string()));
                initShengShiQuCode(cityPicker.getCity_Id());
                if (null != mCityPickerDialog) {
                    mCityPickerDialog.dismiss();
                }
                break;
            case R.id.iv_location_client:// 定位
                Intent mapIntent = new Intent(this, SigninMapActivity.class);
                mapIntent.putExtra(TAG, FLAG);
                startActivityForResult(mapIntent, FLAG);
                break;
        }
    }

    @Override
    public void onAreaSuccess() {
        dismissProgressDialog();
//        cityPicker.setDatas(CityUtil.getProvinceList(), CityUtil.getCityData(), CityUtil.getCountyData());
        CityUtil.initCityPicker(cityPicker);
        mCityPickerDialog.show();
    }

    @Override
    public void onAreaFail() {
        dismissProgressDialog();
    }

    @Override
    public void onGetClientTypeSuccess() {
        dismissProgressDialog();
        Intent intentType = new Intent(this, ShopTypeActivity.class);
        intentType.putExtra("shopType", tvType.getText().toString());
        startActivityForResult(intentType, ShopTypeActivity.FLAG);
    }

    @Override
    public void onGetClientTypeFail() {
        dismissProgressDialog();
    }
}
