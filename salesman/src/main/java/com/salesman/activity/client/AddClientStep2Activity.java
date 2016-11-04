package com.salesman.activity.client;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
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
import com.salesman.global.BeanListHolder;
import com.salesman.network.BaseHelper;
import com.salesman.utils.DialogUtil;
import com.salesman.utils.LocalImageHelper;
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

import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 添加客户界面
 * 版本V1.4.0
 * Created by LiHuai on 2016/06/21.
 */
public class AddClientStep2Activity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, UploadFileUtil.UploadFileListener, DialogUtil.PickPhotoOnClickListener, AdapterView.OnItemClickListener {
    public static final String TAG = AddClientStep2Activity.class.getSimpleName();

    private TextView tvBack;
    private TextView tvRight;

    private Dialog mCityPickerDialog;
    private TextView tvSelectCity;
    private CityPicker cityPicker;
    // 第一部分
    private LinearLayout layTime, layZhuYing, layZhiFu;
    private TextView tvTime, tvZhuYing, tvZhiFu;
    private EditText etMianJi, etRenShu, etPeiSong, etPeiSongShang, etZhanBi, etYingYeE, etSKU;
    // 开关
    private ToggleButton tbHeZuo, tbLianSuo, tbYanCao, tbPos, tbPc, tbWifi;
    private TextView tvHeZuo, tvLianSuo, tvYanCao, tvPos, tvPc, tvWifi;
    // 图片
    private GridView gridView;
    private List<UploadImageBean> mData = BeanListHolder.getUploadImageBeanList();
    private ShopImageAdapter adapter;
    private UploadFileUtil uploadFileUtil;
    // 图片选择提示
    private DialogUtil dialogUtil;
    private PictureUtil pictureUtil;
    // 客户
    private ShopDetail shopDetail;
    private String mianji = "", startTime = "", endTime = "", renShu = "", peiSongShu = "", heZuoShang = "", zhuYing = "", zhanBi = "", yingYeE = "";
    private String zhiFu = "", SKU = "", qiTa, lianSuo, yanCao, POS, PC, Wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_add_client_step2);

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
        // 图片
        gridView = (GridView) findViewById(R.id.gv_add_client);
        adapter = new ShopImageAdapter(this, mData);
        gridView.setAdapter(adapter);
        uploadFileUtil = new UploadFileUtil(this);
        dialogUtil = new DialogUtil(this);
        dialogUtil.setPickPhotoOnClickListener(this);
        pictureUtil = new PictureUtil(this, mData, adapter);

        mCityPickerDialog = DialogCreator.createNormalDialog(this, R.layout.dialog_citypicker, DialogCreator.Position.BOTTOM);
        tvSelectCity = (TextView) mCityPickerDialog.findViewById(R.id.tv_selectcity);
        cityPicker = (CityPicker) mCityPickerDialog.findViewById(R.id.citypicker);
    }

    private void initEvents() {
        tvBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        layTime.setOnClickListener(this);
        layZhuYing.setOnClickListener(this);
        layZhiFu.setOnClickListener(this);

        tbHeZuo.setOnCheckedChangeListener(this);
        tbLianSuo.setOnCheckedChangeListener(this);
        tbYanCao.setOnCheckedChangeListener(this);
        tbPos.setOnCheckedChangeListener(this);
        tbPc.setOnCheckedChangeListener(this);
        tbWifi.setOnCheckedChangeListener(this);

        tvSelectCity.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        shopDetail = getIntent().getParcelableExtra("ClientDetail");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == shopDetail) {
            LogUtils.d(TAG, "onResume");
            shopDetail = getIntent().getParcelableExtra("ClientDetail");
        }
    }

    /**
     * 保存客户信息
     */
    private void saveMessage(Map<String, String> map) {
        mianji = etMianJi.getText().toString().trim();
        renShu = etRenShu.getText().toString().trim();
        peiSongShu = etPeiSong.getText().toString().trim();
        heZuoShang = etPeiSongShang.getText().toString().trim();
        zhuYing = tvZhuYing.getText().toString().trim();
        zhanBi = etZhanBi.getText().toString().trim();
        yingYeE = etYingYeE.getText().toString().trim();
        zhiFu = tvZhiFu.getText().toString().trim();
        SKU = etSKU.getText().toString().trim();
        qiTa = tbHeZuo.isChecked() ? "1" : "0";
        lianSuo = tbLianSuo.isChecked() ? "1" : "0";
        yanCao = tbYanCao.isChecked() ? "1" : "0";
        POS = tbPos.isChecked() ? "1" : "0";
        PC = tbPc.isChecked() ? "1" : "0";
        Wifi = tbWifi.isChecked() ? "1" : "0";

        map.put(ShopDetailsBean.SHOPNAME, ReplaceSymbolUtil.transcodeToUTF8(shopDetail.shopName));
        map.put(ShopDetailsBean.SHOPTYPE, shopDetail.shopType);
        map.put(ShopDetailsBean.TELEPHONE, shopDetail.telephone);
        map.put(ShopDetailsBean.PROVINCE, ReplaceSymbolUtil.transcodeToUTF8(shopDetail.province));
        map.put(ShopDetailsBean.CITY, ReplaceSymbolUtil.transcodeToUTF8(shopDetail.city));
        map.put(ShopDetailsBean.AREA, ReplaceSymbolUtil.transcodeToUTF8(shopDetail.area));
        map.put(ShopDetailsBean.PROVINCEID, String.valueOf(shopDetail.provinceId));
        map.put(ShopDetailsBean.CITYID, String.valueOf(shopDetail.cityId));
        map.put(ShopDetailsBean.AREAID, String.valueOf(shopDetail.areaId));
        map.put(ShopDetailsBean.LONGITUDE, String.valueOf(shopDetail.longitude));
        map.put(ShopDetailsBean.LATITUDE, String.valueOf(shopDetail.latitude));
        map.put(ShopDetailsBean.SHOPADDRESS, ReplaceSymbolUtil.transcodeToUTF8(shopDetail.shopAddress));
        map.put(ShopDetailsBean.BOSSNAME, ReplaceSymbolUtil.transcodeToUTF8(shopDetail.bossName));
        map.put(ShopDetailsBean.BOSSTEL, shopDetail.bossTel);
        map.put(ShopDetailsBean.REMARKS, ReplaceSymbolUtil.transcodeToUTF8(ReplaceSymbolUtil.replaceHuanHang(shopDetail.remarks)));
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
    private void postMessage(List<UploadPicBean.ImagePath> pathList) {
        String url = Constant.moduleAddClient;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        saveMessage(map);
        map.put("picUrl", PictureUtil.sliceUploadPicString(pathList));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean) {
                    if (baseBean.success) {
                        ToastUtil.show(AddClientStep2Activity.this, getResources().getString(R.string.add_client_success));
                        EventBus.getDefault().post(EventBusConfig.ADD_CLIENT_FINISH);
                    } else {
                        ToastUtil.show(AddClientStep2Activity.this, baseBean.msg);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
                ToastUtil.show(AddClientStep2Activity.this, getResources().getString(R.string.add_client_fail));
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
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
                break;
            case IntentHelper.PICK_PHOTO:// 相册选择
            case IntentHelper.PICK_PHOTO_KITKAT:// 相册选择
                pictureUtil.pickPhoto(data.getData());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AddClientStep1Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(String action) {
        if (EventBusConfig.ADD_CLIENT_FINISH.equals(action)) {
            finish();
        }
    }

    public void onEventMainThread(ShopDetail shopDetail) {
        if (null != shopDetail) {
            LogUtils.d(TAG, "onEventMainThread");
            this.shopDetail = shopDetail;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                onBackPressed();
                break;
            case R.id.tv_top_right:// 提交
                if (null == shopDetail) {
                    return;
                }
                showProgressDialog(getString(R.string.submitting), false);
                if (PictureUtil.isPictureUpload(adapter.getData())) {
                    uploadFileUtil.uploadFile(adapter.getData());
                } else {
                    postMessage(null);
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

        }
    }

    @Override
    public void uploadFileFail() {
        dismissProgressDialog();
        ToastUtil.show(this, getString(R.string.upload_pic_fail));
    }

    @Override
    public void uploadFileSuccess(List<UploadPicBean.ImagePath> picList) {
        postMessage(picList);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UploadImageBean bean = adapter.getItem(position);
        if (bean != null && bean.type == 2) {
            PictureUtil.lookBigPicFromLocal(this, position, adapter.getData());
        } else {
            dialogUtil.showPhotoPickDialog();
        }
    }
}
