package com.salesman.activity.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.salesman.R;
import com.salesman.adapter.ShopImageAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.ClientListBean;
import com.salesman.entity.SingleSelectionBean;
import com.salesman.entity.UploadImageBean;
import com.salesman.entity.UploadPicBean;
import com.salesman.fragment.ClientFragment;
import com.salesman.global.BeanListHolder;
import com.salesman.listener.OnCommonPostListener;
import com.salesman.network.BaseHelper;
import com.salesman.utils.DateUtils;
import com.salesman.utils.DialogUtil;
import com.salesman.utils.LocalImageHelper;
import com.salesman.utils.LocationCoordinateUtil;
import com.salesman.utils.LocationManagerUtil;
import com.salesman.utils.OutSigninLineClientUtil;
import com.salesman.utils.PictureUtil;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.TimerUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UploadFileUtil;
import com.salesman.utils.UserConfigPreference;
import com.salesman.utils.UserInfoPreference;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.IntentHelper;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.utils.NetworkUtils;
import com.studio.jframework.widget.InnerGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 外勤签到界面
 * Created by LiHuai on 2016/5/23.
 */
public class OutsideSignInActivity extends BaseActivity implements View.OnClickListener, DialogUtil.DialogOnClickListener, AdapterView.OnItemClickListener, UploadFileUtil.UploadFileListener, OnCommonPostListener {
    private final String TAG = OutsideSignInActivity.class.getSimpleName();
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private TextView tvTime;
    private TextView tvAddress;
    private LinearLayout layAddress, layLineAndClient;
    private Button btnAffirm;
    private EditText etRemark;
    private TextView tvLine, tvClient;
    private RadioGroup radioGroup;

    private LocationManagerUtil locationManagerUtil;
    private SingleLocationListener mSingleLocationListener;// 定位
    private LocationClient locationClient;
    // 图片部分
    private InnerGridView gridView;
    private List<UploadImageBean> uploadImgList = BeanListHolder.getUploadImageBeanList();
    private ShopImageAdapter imgAdpter;
    private PictureUtil pictureUtil;
    private UploadFileUtil uploadFileUtil;
    // GPS提示
    private DialogUtil dialogUtil;

    private String time;
    private String address;
    private String line = "";
    private String client = "";
    private String remark;
    private String markType;
    private double lat = 0d;
    private double lng = 0d;
    // 路线和客户
    private String lineId = "";
    private String clientId = "";
    private ArrayList<SingleSelectionBean> mLines = new ArrayList<>();
    private ArrayList<SingleSelectionBean> mClients = new ArrayList<>();
    private OutSigninLineClientUtil outSigninLineClientUtil;
    private boolean isClickLine = false;
    // 请求编号
    private final int requestCode1 = 1004, requestCode2 = 1005, requestCode3 = 1006;
    // 签到客户
    private ClientListBean.ShopBean shopBean;
    private String come_from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_outside_sign_in);
        EventBus.getDefault().register(this);
        if (!NetworkUtils.isGpsEnable(this)) {
            dialogUtil.showDialog();
        }
    }

    @Override
    protected void initView() {
        TextView ivTopLeft = (TextView) findViewById(R.id.tv_top_left);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.outside_signin);
        tvTime = (TextView) findViewById(R.id.tv_time_outside);
        tvAddress = (TextView) findViewById(R.id.tv_address_outside);
        btnAffirm = (Button) findViewById(R.id.btn_affirm);
        layAddress = (LinearLayout) findViewById(R.id.lay_address_outside);
        layLineAndClient = (LinearLayout) findViewById(R.id.lay_line_client);
        etRemark = (EditText) findViewById(R.id.et_remark_outside);
        tvLine = (TextView) findViewById(R.id.tv_line_outside);
        tvClient = (TextView) findViewById(R.id.tv_client_outside);
        radioGroup = (RadioGroup) findViewById(R.id.rg_outside_type);
        // 图片部分
        gridView = (InnerGridView) findViewById(R.id.gv_outside);
        imgAdpter = new ShopImageAdapter(this, uploadImgList);
        gridView.setAdapter(imgAdpter);
        pictureUtil = new PictureUtil(this, uploadImgList, imgAdpter);
        uploadFileUtil = new UploadFileUtil(this);
        // GPS提示
        dialogUtil = new DialogUtil(this, getString(R.string.gps_open_setting), false);
        dialogUtil.setDialogListener(this);
        // 路线
        outSigninLineClientUtil = new OutSigninLineClientUtil();
        outSigninLineClientUtil.setOnCommonPostListener(this);

        ivTopLeft.setOnClickListener(this);
        btnAffirm.setOnClickListener(this);
        layAddress.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
        tvLine.setOnClickListener(this);
        tvClient.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        shopBean = (ClientListBean.ShopBean) getIntent().getSerializableExtra("shopBean");
        come_from = getIntent().getStringExtra("come_from");
        if (null == shopBean) {
            // 获取线路与客户
            outSigninLineClientUtil.getOutLineAndClientData();
        } else {
            tvLine.setEnabled(false);
            tvClient.setEnabled(false);
            tvLine.setText(shopBean.lineName);
            tvClient.setText(shopBean.shopName);
            lineId = shopBean.lineId;
            clientId = shopBean.shopNo;
            if ("1".equals(shopBean.status)) {
                radioGroup.check(R.id.rb_leave);
            }
        }
        TimerUtil.restartTimer(handler, 0, 10000);
        locationManagerUtil = LocationManagerUtil.getInstance(getApplicationContext());
        locationClient = locationManagerUtil.initLocation(this, 0);
        mSingleLocationListener = new SingleLocationListener();
        locationManagerUtil.startLocationListener(locationClient, mSingleLocationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvTime.setText(DateUtils.getYMDHM(System.currentTimeMillis()));
        }
    };

    public void onEventMainThread(String action) {
        if (EventBusConfig.SIGNIN_ADDRESS.equals(action)) {
            tvAddress.setText(mUserConfig.getLocationAddress());
        }
    }

    @Override
    public void confirmDialog() {
        IntentHelper.openGPSSetting(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UploadImageBean imgBean = imgAdpter.getItem(position);
        if (imgBean != null && imgBean.type == 2) {
            ArrayList<String> imgs = new ArrayList<>();
            for (UploadImageBean upBean : uploadImgList) {
                if (upBean.type == 2 && !TextUtils.isEmpty(upBean.cameraPath)) {
                    imgs.add(upBean.getCameraPath());
                }
            }
            Intent bigIntent = new Intent(this, BigImageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("Imgs", imgs);
            bundle.putInt("Position", position);
            bigIntent.putExtras(bundle);
            startActivity(bigIntent);
        } else {
            String cameraPath = LocalImageHelper.getInstance().setCameraImgPath();
            IntentHelper.takePhoto(this, cameraPath);
        }
    }

    @Override
    public void uploadFileFail() {
        dismissProgressDialog();
        ToastUtil.show(OutsideSignInActivity.this, "图片上传失败请重新提交");
    }

    @Override
    public void uploadFileSuccess(List<UploadPicBean.ImagePath> picList) {
        postMessage(picList);
    }

    @Override
    public void onSuccessListener() {
        dismissProgressDialog();
        // 无客户时无需选择线路和客户，可直接签外勤到
        if (!OutSigninLineClientUtil.isHaveClient()) {
            layLineAndClient.setVisibility(View.GONE);
        } else {
            layLineAndClient.setVisibility(View.VISIBLE);
            if (isClickLine) {
                onClick(tvLine);
            }
        }
        initLineSelect();
    }

    /**
     * 线路选择控制
     */
    private void initLineSelect() {
        if (OutSigninLineClientUtil.isOnlyLine()) {
            tvLine.setEnabled(false);
            mLines = OutSigninLineClientUtil.getAllOutLineSingSelectionData();
            tvLine.setText(mLines.get(0).name);
            lineId = mLines.get(0).id;
        } else {
            tvLine.setEnabled(true);
        }
    }

    @Override
    public void onFailListener() {
        dismissProgressDialog();
    }

    /**
     * 单次定位回调
     */
    public class SingleLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            locationManagerUtil.unRegisterLocationListener(locationClient, mSingleLocationListener);
            LogUtils.d(TAG, LocationCoordinateUtil.getLocationDetail(location));
            String singleStr = LocationCoordinateUtil.getLocationAddress(location);
            if (!TextUtils.isEmpty(singleStr)) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                mUserConfig.saveLocationAddress(singleStr)
                        .saveLatitude(String.valueOf(location.getLatitude()))
                        .saveLongitude(String.valueOf(location.getLongitude()))
                        .apply();
                tvAddress.setText(singleStr);
            } else {
                ToastUtil.show(OutsideSignInActivity.this, "定位失败，请重新定位");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case requestCode2:// 线路
                if (null != data) {
                    SingleSelectionBean bean = data.getParcelableExtra(SingleSelectionActivity.SELECT_BEAN);
                    if (null != bean) {
                        setSelectItem(bean);
                        tvLine.setText(bean.name);
                        lineId = bean.id;
                        line = bean.name;

                        tvClient.setText(R.string.select_please);
                        clientId = "";
                        client = "";
                        mClients.clear();
                    }
                }
                break;
            case requestCode3:// 客户
                if (null != data) {
                    SingleSelectionBean bean = data.getParcelableExtra(SingleSelectionActivity.SELECT_BEAN);
                    if (null != bean) {
                        tvClient.setText(bean.name);
                        clientId = bean.id;
                        client = bean.name;
                    }
                }
                break;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case IntentHelper.TAKE_PHOTO:// 拍照
                pictureUtil.picDispose(LocalImageHelper.getInstance().getCameraImgPath());
                break;
            case requestCode1:
                lat = data.getDoubleExtra("latitude", 0d);
                lng = data.getDoubleExtra("longitude", 0d);
                address = data.getStringExtra("address");
                break;
        }
    }

    private void setSelectItem(SingleSelectionBean bean) {
        for (SingleSelectionBean singleSelectionBean : mLines) {
            if (bean.id.equals(singleSelectionBean.id)) {
                singleSelectionBean.setIsSelect(true);
            } else {
                singleSelectionBean.setIsSelect(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TimerUtil.clearTimer();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 检查信息
     */
    private void checkMessage() {
        time = tvTime.getText().toString();
        address = tvAddress.getText().toString();
        line = tvLine.getText().toString().trim();
        client = tvClient.getText().toString().trim();
        remark = etRemark.getText().toString().trim();
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_enter:// 到店
                markType = "1";
                break;
            case R.id.rb_leave: // 离店
                markType = "2";
                break;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtil.show(this, "地址错误");
            return;
        }
        if (lng == 4.9E-324 || lat == 4.9E-324 || lng == 0 || lat == 0) {
            ToastUtil.show(this, "无法获取您的经纬度");
            return;
        }
        if (layLineAndClient.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(line) || getString(R.string.must_choose_please).equals(line)) {
                ToastUtil.show(this, "请选择线路");
                return;
            }
            if (TextUtils.isEmpty(client) || getString(R.string.must_choose_please).equals(client) || TextUtils.isEmpty(clientId)) {
                ToastUtil.show(this, "请选择客户");
                return;
            }
        } else {
            line = "";
            client = "";
            clientId = "";
            markType = "";
        }
        if (pictureUtil.isPictureUsable(imgAdpter.getData())) {
            showProgressDialog("提交中...", false);
            uploadFileUtil.uploadFile(imgAdpter.getData());
        }
    }

    /**
     * 上传信息
     */
    private void postMessage(List<UploadPicBean.ImagePath> urlList) {
        String url = Constant.moduleSignData;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("type", String.valueOf(3));
        map.put("deptId", mUserInfo.getDeptId());
        map.put("signTime", time);
        map.put("picUrl", PictureUtil.sliceUploadPicString(urlList));
        map.put("address", ReplaceSymbolUtil.transcodeToUTF8(address));
        map.put("remarks", ReplaceSymbolUtil.transcodeToUTF8(ReplaceSymbolUtil.replaceHuanHang(remark)));
        map.put("visitLine", ReplaceSymbolUtil.transcodeToUTF8(line));
        map.put("visitCust", ReplaceSymbolUtil.transcodeToUTF8(client));
        map.put("longitude", String.valueOf(lng));
        map.put("latitude", String.valueOf(lat));
        map.put("localtimes", String.valueOf(System.currentTimeMillis()));
        map.put("markType", markType);
        map.put("shopNo", clientId);
        map.put("lineId", lineId);
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                dismissProgressDialog();
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean) {
                    if (baseBean.success) {
                        if (ClientFragment.TAG.equals(come_from)) {
                            EventBus.getDefault().post(EventBusConfig.CLIENT_LIST_REFRESH);
                        }
                        ToastUtil.show(OutsideSignInActivity.this, getResources().getString(R.string.siginin_succeed));
                        finish();
                    } else {
                        ToastUtil.show(OutsideSignInActivity.this, baseBean.msg);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
                ToastUtil.show(OutsideSignInActivity.this, getResources().getString(R.string.siginin_fail));
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
            case R.id.lay_address_outside:// 看地图
                Intent mapIntent = new Intent(this, SigninMapActivity.class);
                mapIntent.putExtra("address", tvAddress.getText().toString());
                startActivityForResult(mapIntent, requestCode1);
                break;
            case R.id.btn_affirm:// 提交
                if (NetworkUtils.isGpsEnable(this)) {
                    checkMessage();
                } else {
                    dialogUtil.showDialog();
                }
                break;
            case R.id.tv_line_outside:// 路线
                isClickLine = true;
                if (OutSigninLineClientUtil.isSecondRequest()) {
                    showProgressDialog(getString(R.string.loading1), false);
                    outSigninLineClientUtil.getOutLineAndClientData();
                } else {
                    if (mLines.isEmpty()) {
                        mLines = OutSigninLineClientUtil.getAllOutLineSingSelectionData();
                    }
                    Intent luXianIntent = new Intent(this, SingleSelectionActivity.class);
                    luXianIntent.putParcelableArrayListExtra("data", mLines);
                    luXianIntent.putExtra(SingleSelectionActivity.TITLE, "拜访路线");
                    startActivityForResult(luXianIntent, requestCode2);
                }
                break;
            case R.id.tv_client_outside:// 客户
                if (TextUtils.isEmpty(lineId)) {
                    ToastUtil.show(this, "请先选择线路");
                    return;
                }
                if (mClients.isEmpty()) {
                    mClients = OutSigninLineClientUtil.getClientByLineId(lineId);
                }
                Intent clientIntent = new Intent(OutsideSignInActivity.this, SingleSelectionActivity.class);
                clientIntent.putParcelableArrayListExtra("data", mClients);
                clientIntent.putExtra(SingleSelectionActivity.TITLE, "拜访客户");
                startActivityForResult(clientIntent, requestCode3);
                break;
        }
    }
}
