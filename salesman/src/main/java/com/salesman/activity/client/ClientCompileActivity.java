package com.salesman.activity.client;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.salesman.adapter.ShopImageAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.ShopDetail;
import com.salesman.entity.ShopDetailsBean;
import com.salesman.entity.UploadImageBean;
import com.salesman.entity.UploadPicBean;
import com.salesman.network.BaseHelper;
import com.salesman.utils.CityUtil;
import com.salesman.utils.ClientTypeUtil;
import com.salesman.utils.DialogUtil;
import com.salesman.utils.LocalImageHelper;
import com.salesman.utils.PhoneNumberUtil;
import com.salesman.utils.PictureUtil;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UploadFileUtil;
import com.salesman.views.citypicker.CityPicker;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.IntentHelper;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.dialog.DialogCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 编辑客户界面
 * 版本V1.4.0
 * Created by LiHuai on 2016/06/21.
 */
public class ClientCompileActivity extends BaseActivity implements View.OnClickListener, DialogUtil.PickPhotoOnClickListener, UploadFileUtil.UploadFileListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, CityUtil.OnAreaListener, ClientTypeUtil.GetClientTypeListener {
    public static final String TAG = ClientCompileActivity.class.getSimpleName();
    public static final int FLAG = 2003;

    private TextView tvBack;
    private TextView tvRight;
    private ShopDetail clientDetail;
    // 第一部分
    private LinearLayout layType, layQuyu;
    private TextView tvType, tvQuyu;
    private EditText etName, etTel, etAddress, etBoss, etBossTel, etBossRemark;
    private ImageView ivLocation;
    // 第二部分
    private LinearLayout layTime, layZhuYing, layZhiFu;
    private TextView tvTime, tvZhuYing, tvZhiFu;
    private EditText etMianJi, etRenShu, etPeiSong, etPeiSongShang, etZhanBi, etYingYeE, etSKU;
    // 开关
    private ToggleButton tbHeZuo, tbLianSuo, tbYanCao, tbPos, tbPc, tbWifi;
    private TextView tvHeZuo, tvLianSuo, tvYanCao, tvPos, tvPc, tvWifi;
    // 变化布局
    private LinearLayout layNameStore, layQuyuStroe;

    private Dialog mCityPickerDialog;
    private TextView tvSelectCity;
    private CityPicker cityPicker;
    private String province = "", city = "", district = "";
    private int provinceId = 0, cityId = 0, areaId = 0;
    // 百度地图地理编码
    private GeoCoder mSearch;
    private double lat;
    private double lng;
    private CityUtil cityUtil;
    private ClientTypeUtil clientTypeUtil;
    // 图片
    private GridView gridView;
    private List<UploadImageBean> mData = new ArrayList<>();
    private ShopImageAdapter adapter;
    private UploadFileUtil uploadFileUtil;
    // 图片选择提示
    private DialogUtil dialogUtil;
    private PictureUtil pictureUtil;
    // 字段
    private String shopName, shopType, shopTypeName, shopTel, shopArea, shopAddress, bossName, bossTel, boosRemark;
    private String mianji, startTime, endTime, renShu, peiSongShu, heZuoShang, zhuYing, zhanBi, yingYeE;
    private String zhiFu, SKU, qiTa, lianSuo, yanCao, POS, PC, Wifi;

    private Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_client_compile);

        initEvent();
    }

    @Override
    protected void initView() {
        TextView tvTitel = (TextView) findViewById(R.id.tv_top_title);
        tvTitel.setText(R.string.client_compile);
        tvBack = (TextView) findViewById(R.id.tv_top_left);
        tvRight = (TextView) findViewById(R.id.tv_top_right);
        tvRight.setText(R.string.submit);
        tvRight.setVisibility(View.VISIBLE);
        // 第一部分
        layType = (LinearLayout) findViewById(R.id.lay_type);
        layQuyu = (LinearLayout) findViewById(R.id.lay_quyu);
        tvType = (TextView) findViewById(R.id.tv_type_client);
        tvQuyu = (TextView) findViewById(R.id.tv_quyu_client);
        etName = (EditText) findViewById(R.id.et_name_client);
        etTel = (EditText) findViewById(R.id.et_tel_client);
        etAddress = (EditText) findViewById(R.id.et_address_client);
        etBoss = (EditText) findViewById(R.id.et_name_boss);
        etBossTel = (EditText) findViewById(R.id.et_tel_boss);
        etBossRemark = (EditText) findViewById(R.id.et_remark_boss);
        // 第一部分
        layTime = (LinearLayout) findViewById(R.id.lay_time);
        layZhuYing = (LinearLayout) findViewById(R.id.lay_zhuying);
        layZhiFu = (LinearLayout) findViewById(R.id.lay_zhifu);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvZhuYing = (TextView) findViewById(R.id.tv_zhuying);
        tvZhiFu = (TextView) findViewById(R.id.tv_zhifu);
        etMianJi = (EditText) findViewById(R.id.et_mianji);
        etRenShu = (EditText) findViewById(R.id.et_renshu);
        etPeiSong = (EditText) findViewById(R.id.et_peisong);
        etPeiSongShang = (EditText) findViewById(R.id.et_peisongshang);
        etZhanBi = (EditText) findViewById(R.id.et_zhanbi);
        etYingYeE = (EditText) findViewById(R.id.et_yingyee);
        etSKU = (EditText) findViewById(R.id.et_sku);
        ivLocation = (ImageView) findViewById(R.id.iv_location_client);
        // 开关
        tvHeZuo = (TextView) findViewById(R.id.tv_hezuo);
        tvLianSuo = (TextView) findViewById(R.id.tv_liansuo);
        tvYanCao = (TextView) findViewById(R.id.tv_yancao);
        tvPos = (TextView) findViewById(R.id.tv_pos);
        tvPc = (TextView) findViewById(R.id.tv_pc);
        tvWifi = (TextView) findViewById(R.id.tv_wifi);
        tbHeZuo = (ToggleButton) findViewById(R.id.tb_hezuo);
        tbLianSuo = (ToggleButton) findViewById(R.id.tb_liansuo);
        tbYanCao = (ToggleButton) findViewById(R.id.tb_yancao);
        tbPos = (ToggleButton) findViewById(R.id.tb_pos);
        tbPc = (ToggleButton) findViewById(R.id.tb_pc);
        tbWifi = (ToggleButton) findViewById(R.id.tb_wifi);
        // 变化布局
        layNameStore = (LinearLayout) findViewById(R.id.lay_name_store);
        layQuyuStroe = (LinearLayout) findViewById(R.id.lay_quyu_store);
        // 图片
        gridView = (GridView) findViewById(R.id.gv_client_compile);
        adapter = new ShopImageAdapter(this, mData);
        gridView.setAdapter(adapter);
        uploadFileUtil = new UploadFileUtil(this);
        dialogUtil = new DialogUtil(this);
        dialogUtil.setPickPhotoOnClickListener(this);
        pictureUtil = new PictureUtil(this, mData, adapter);

        cityUtil = new CityUtil();
        clientTypeUtil = new ClientTypeUtil();
        mCityPickerDialog = DialogCreator.createNormalDialog(this, R.layout.dialog_citypicker, DialogCreator.Position.BOTTOM);
        tvSelectCity = (TextView) mCityPickerDialog.findViewById(R.id.tv_selectcity);
        cityPicker = (CityPicker) mCityPickerDialog.findViewById(R.id.citypicker);
        CityUtil.initCityPicker(cityPicker);
    }

    private void initEvent() {
        tvBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        layType.setOnClickListener(this);
        layQuyu.setOnClickListener(this);
        layTime.setOnClickListener(this);
        layZhuYing.setOnClickListener(this);
        layZhiFu.setOnClickListener(this);
        ivLocation.setOnClickListener(this);

        tbHeZuo.setOnCheckedChangeListener(this);
        tbLianSuo.setOnCheckedChangeListener(this);
        tbYanCao.setOnCheckedChangeListener(this);
        tbPos.setOnCheckedChangeListener(this);
        tbPc.setOnCheckedChangeListener(this);
        tbWifi.setOnCheckedChangeListener(this);

        gridView.setOnItemClickListener(this);
        cityUtil.setOnAreaListener(this);
        clientTypeUtil.setClientTypeListener(this);
        tvSelectCity.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        clientDetail = getIntent().getParcelableExtra("clientDetail");
        if (null != clientDetail) {
            initPic(clientDetail.picList);

            etName.setText(clientDetail.shopName);
            tvType.setText(clientDetail.shopTypeName);
            etTel.setText(clientDetail.telephone);
            tvQuyu.setText(clientDetail.province + clientDetail.city + clientDetail.area);
            etAddress.setText(clientDetail.shopAddress);
            etBoss.setText(clientDetail.bossName);
            etBossTel.setText(clientDetail.bossTel);
            etBossRemark.setText(ReplaceSymbolUtil.reverseReplaceHuanHang(clientDetail.remarks));
            etMianJi.setText(clientDetail.shopArea);
            tvTime.setText(yingYeTime(clientDetail));// 营业时间
            etRenShu.setText(clientDetail.staffNum);
            etPeiSong.setText(clientDetail.distributionNum);
            etPeiSongShang.setText(clientDetail.dcShop);
            tvZhuYing.setText(clientDetail.mainProduct);
            etZhanBi.setText(clientDetail.saleRatio);
            etYingYeE.setText(clientDetail.turnover);
            tvZhiFu.setText(clientDetail.ipay);
            etSKU.setText(clientDetail.sku);
            tbHeZuo.setChecked(getBool(clientDetail.otherPatform));
            tbLianSuo.setChecked(getBool(clientDetail.isMultipleShop));
            tbYanCao.setChecked(getBool(clientDetail.baccyLicence));
            tbPos.setChecked(getBool(clientDetail.isPos));
            tbPc.setChecked(getBool(clientDetail.isComputer));
            tbWifi.setChecked(getBool(clientDetail.isWifi));

            lng = clientDetail.longitude;
            lat = clientDetail.latitude;
            province = clientDetail.province;
            city = clientDetail.city;
            district = clientDetail.area;
            provinceId = clientDetail.provinceId;
            cityId = clientDetail.cityId;
            areaId = clientDetail.areaId;
            startTime = clientDetail.startShopHours;
            endTime = clientDetail.endShopHours;
            shopType = clientDetail.shopType;
            // 是否注册店宝
            if ("1".equals(clientDetail.isRegister)) {
                layNameStore.setVisibility(View.GONE);
                layQuyuStroe.setVisibility(View.GONE);
            } else {
                layNameStore.setVisibility(View.VISIBLE);
                layQuyuStroe.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initPic(List<String> picList) {
        if (null != picList) {
            mData.clear();
            for (String s : picList) {
                UploadImageBean imgBean = new UploadImageBean(2);
                imgBean.setPicUrl(s);
                mData.add(imgBean);
            }
            if (mData.size() < 4) {
                UploadImageBean imgBean = new UploadImageBean(1);
                mData.add(imgBean);
            }
            adapter.setData(mData);
        }
    }

    /**
     * 营业时间
     *
     * @param detail
     * @return
     */
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

    private boolean getBool(String str) {
        if ("1".equals(str)) {
            return true;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ShopTypeActivity.FLAG:// 商铺类型
                tvType.setText(data.getStringExtra("shopType"));
                shopType = data.getStringExtra("shopTypeId");
                break;
            case GoodsTypeActivity.FLAG:// 主营商品
                tvZhuYing.setText(data.getStringExtra("goodsType"));
                break;
            case PayWayActivity.FLAG:// 支付平台
                tvZhiFu.setText(data.getStringExtra("payWay"));
                break;
            case ClientTimeActivity.FLAG:// 营业时间
                startTime = data.getStringExtra("StartTime");
                endTime = data.getStringExtra("EndTime");
                tvTime.setText(startTime + "-" + endTime);
                break;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case IntentHelper.TAKE_PHOTO:// 拍照
                pictureUtil.picDispose(LocalImageHelper.getInstance().getCameraImgPath());
                // 上传图片
                showProgressDialog(getString(R.string.uploading), false);
                uploadFileUtil.uploadFile(adapter.getData());
                break;
            case IntentHelper.PICK_PHOTO:// 相册选择
            case IntentHelper.PICK_PHOTO_KITKAT:// 相册选择
                pictureUtil.pickPhoto(data.getData());
                // 上传图片
                showProgressDialog(getString(R.string.uploading), false);
                uploadFileUtil.uploadFile(adapter.getData());
                break;
            case FLAG:
                etAddress.setText(data.getStringExtra("address"));
                break;
        }
    }

    /**
     * 保存客户信息
     */
    private void saveMessage() {
        map.put("shopId", clientDetail.shopId);

        map.put(ShopDetailsBean.SHOPNAME, ReplaceSymbolUtil.transcodeToUTF8(shopName));
        map.put(ShopDetailsBean.SHOPTYPE, shopType);
        map.put(ShopDetailsBean.TELEPHONE, shopTel);
        map.put(ShopDetailsBean.PROVINCE, ReplaceSymbolUtil.transcodeToUTF8(province));
        map.put(ShopDetailsBean.CITY, ReplaceSymbolUtil.transcodeToUTF8(city));
        map.put(ShopDetailsBean.AREA, ReplaceSymbolUtil.transcodeToUTF8(district));
        map.put(ShopDetailsBean.PROVINCEID, String.valueOf(provinceId));
        map.put(ShopDetailsBean.CITYID, String.valueOf(cityId));
        map.put(ShopDetailsBean.AREAID, String.valueOf(areaId));
        map.put(ShopDetailsBean.LONGITUDE, String.valueOf(lng));
        map.put(ShopDetailsBean.LATITUDE, String.valueOf(lat));
        map.put(ShopDetailsBean.SHOPADDRESS, ReplaceSymbolUtil.transcodeToUTF8(shopAddress));
        map.put(ShopDetailsBean.BOSSNAME, ReplaceSymbolUtil.transcodeToUTF8(bossName));
        map.put(ShopDetailsBean.BOSSTEL, bossTel);
        map.put(ShopDetailsBean.REMARKS, ReplaceSymbolUtil.transcodeToUTF8(ReplaceSymbolUtil.replaceHuanHang(boosRemark)));
        map.put(ShopDetailsBean.SHOPAREA, mianji);
        map.put(ShopDetailsBean.STARTSHOPHOURS, startTime);
        map.put(ShopDetailsBean.ENDSHOPHOURS, endTime);
        map.put(ShopDetailsBean.STAFFNUM, renShu);
        map.put(ShopDetailsBean.DISTRIBUTIONNUM, peiSongShu);
        map.put(ShopDetailsBean.DCSHOP, ReplaceSymbolUtil.transcodeToUTF8(heZuoShang));
        map.put(ShopDetailsBean.MAINPRODUCT, ReplaceSymbolUtil.transcodeToUTF8(zhuYing));
        map.put(ShopDetailsBean.SALERATIO, zhanBi);
        map.put(ShopDetailsBean.TURNOVER, yingYeE);
        map.put(ShopDetailsBean.IPAY, ReplaceSymbolUtil.transcodeToUTF8(zhiFu));
        map.put(ShopDetailsBean.SKU, SKU);
        map.put(ShopDetailsBean.OTHERPATFORM, qiTa);
        map.put(ShopDetailsBean.ISMULTIPLESHOP, lianSuo);
        map.put(ShopDetailsBean.BACCYLICENCE, yanCao);
        map.put(ShopDetailsBean.ISPOS, POS);
        map.put(ShopDetailsBean.ISCOMPUTER, PC);
        map.put(ShopDetailsBean.ISWIFI, Wifi);

    }

    /**
     * 提交信息
     */
    private void postMessage() {
        String url = Constant.moduleEditClient;
        saveMessage();
        map.put("picUrl", PictureUtil.sliceNetPicString(adapter.getData()));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean) {
                    if (baseBean.success) {
                        EventBus.getDefault().post(EventBusConfig.CLIENT_DETAIL_REFRESH);
                        EventBus.getDefault().post(EventBusConfig.CLIENT_LIST_REFRESH);
                        ToastUtil.show(ClientCompileActivity.this, getResources().getString(R.string.edit_client_success));
                        finish();
                    } else {
                        ToastUtil.show(ClientCompileActivity.this, baseBean.msg);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
                ToastUtil.show(ClientCompileActivity.this, getResources().getString(R.string.edit_client_fail));
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
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
                //没有检索到结果
                dismissProgressDialog();
                Toast.makeText(ClientCompileActivity.this, "无法获取您选择地区信息，请填写正确的地址", Toast.LENGTH_SHORT).show();
                return;
            }
            //获取地理编码结果
            lat = result.getLocation().latitude;
            lng = result.getLocation().longitude;
            LogUtils.d(TAG, lat + "地理编码" + lng);
            // 上传信息
            postMessage();
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                dismissProgressDialog();
                Toast.makeText(ClientCompileActivity.this, "抱歉，未能找到结果", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mSearch) {
            mSearch.destroy();
        }
    }

    @Override
    public void onTakePhotoClick() {
        String cameraPath = LocalImageHelper.getInstance().setCameraImgPath();
        IntentHelper.takePhoto(this, cameraPath);
    }

    @Override
    public void onPickPhotoClick() {
        IntentHelper.pickPhotoFromGallery(this);
    }

    @Override
    public void uploadFileFail() {
        dismissProgressDialog();
        ToastUtil.show(this, getString(R.string.upload_pic_fail));
        List<String> picUrlList = PictureUtil.getPicUrlList(adapter.getData());
        initPic(picUrlList);
    }

    @Override
    public void uploadFileSuccess(List<UploadPicBean.ImagePath> picList) {
        dismissProgressDialog();
        List<String> picUrlList = PictureUtil.getPicUrlList(adapter.getData());
        picUrlList.add(picList.get(0).filename);
        initPic(picUrlList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UploadImageBean bean = adapter.getItem(position);
        if (bean != null && bean.type == 2) {
            PictureUtil.lookBigPicFromNet(this, position, adapter.getData());
        } else {
            dialogUtil.showPhotoPickDialog();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tb_hezuo:
                tvHeZuo.setText(judgeStr(isChecked));
                break;
            case R.id.tb_liansuo:
                tvLianSuo.setText(judgeStr(isChecked));
                break;
            case R.id.tb_yancao:
                tvYanCao.setText(judgeStr(isChecked));
                break;
            case R.id.tb_pos:
                tvPos.setText(judgeStr(isChecked));
                break;
            case R.id.tb_pc:
                tvPc.setText(judgeStr(isChecked));
                break;
            case R.id.tb_wifi:
                tvWifi.setText(judgeStr(isChecked));
                break;
        }
    }

    /**
     * 是否字符判读
     */
    private String judgeStr(boolean bool) {
        if (bool) {
            return "有";
        } else {
            return "无";
        }
    }

    private String boolToStr(ToggleButton tb) {
        return tb.isChecked() ? "1" : "0";
    }

    private void judgeMessage() {
        // 必填
        shopName = etName.getText().toString().trim();
        shopTypeName = tvType.getText().toString().trim();
        shopTel = etTel.getText().toString().trim();
        shopArea = tvQuyu.getText().toString().trim();
        shopAddress = etAddress.getText().toString().trim();
        bossName = etBoss.getText().toString().trim();
        bossTel = etBossTel.getText().toString().trim();
        boosRemark = etBossRemark.getText().toString().trim();
        // 选填
        mianji = etMianJi.getText().toString().trim();
        renShu = etRenShu.getText().toString().trim();
        peiSongShu = etPeiSong.getText().toString().trim();
        heZuoShang = etPeiSongShang.getText().toString().trim();
        zhuYing = tvZhuYing.getText().toString().trim();
        zhanBi = etZhanBi.getText().toString().trim();
        yingYeE = etYingYeE.getText().toString().trim();
        zhiFu = tvZhiFu.getText().toString().trim();
        SKU = etSKU.getText().toString().trim();
        qiTa = boolToStr(tbHeZuo);
        lianSuo = boolToStr(tbLianSuo);
        yanCao = boolToStr(tbYanCao);
        POS = boolToStr(tbPos);
        PC = boolToStr(tbPc);
        Wifi = boolToStr(tbWifi);

        if (TextUtils.isEmpty(shopName)) {
            ToastUtil.show(this, getString(R.string.input_shop_name_please));
            return;
        }
        if (TextUtils.isEmpty(shopTypeName) || TextUtils.isEmpty(shopType)) {
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
        if (layQuyuStroe.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(shopArea)) {
                ToastUtil.show(this, getString(R.string.select_shop_area_please));
                return;
            }
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
        // 地理位置校验
        showProgressDialog(getString(R.string.loading1), false);
        getGPRSLatLon(city, shopAddress);
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
                        areaId = TextUtils.isEmpty(diquArray[2]) ? 0 : Integer.parseInt(diquArray[2]);
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
                finish();
                break;
            case R.id.tv_top_right:// 提交
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
            case R.id.lay_zhuying:// 主营商品
                Intent goodsType = new Intent(this, GoodsTypeActivity.class);
                goodsType.putExtra("goodsType", tvZhuYing.getText().toString());
                startActivityForResult(goodsType, GoodsTypeActivity.FLAG);
                break;
            case R.id.lay_zhifu:// 支付平台
                Intent payWay = new Intent(this, PayWayActivity.class);
                payWay.putExtra("payWay", tvZhiFu.getText().toString());
                startActivityForResult(payWay, PayWayActivity.FLAG);
                break;
            case R.id.lay_time:// 营业时间
                Intent timeIntent = new Intent(this, ClientTimeActivity.class);
                timeIntent.putExtra("StartTime", startTime);
                timeIntent.putExtra("EndTime", endTime);
                startActivityForResult(timeIntent, ClientTimeActivity.FLAG);
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
