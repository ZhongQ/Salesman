package com.salesman.activity.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.salesman.R;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.UploadImageBean;
import com.salesman.entity.UploadPicBean;
import com.salesman.network.BaseHelper;
import com.salesman.utils.AlarmUtil;
import com.salesman.utils.DateUtils;
import com.salesman.utils.DialogUtil;
import com.salesman.utils.FileSizeUtil;
import com.salesman.utils.LocalImageHelper;
import com.salesman.utils.LocationCoordinateUtil;
import com.salesman.utils.LocationManagerUtil;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UploadFileUtil;
import com.salesman.utils.UserConfigPreference;
import com.salesman.utils.UserInfoPreference;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.BitmapUtils;
import com.studio.jframework.utils.FileUtils;
import com.studio.jframework.utils.IntentHelper;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.utils.NetworkUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * 签到界面
 * （版本V1.3.0改为“打卡”）
 * Created by LiHuai on 2016/1/21.
 */
public class SignInActivity extends BaseActivity implements View.OnClickListener, DialogUtil.DialogOnClickListener, UploadFileUtil.UploadFileListener {
    public static final String TAG = SignInActivity.class.getSimpleName();

    private TextView ivTopLeft;
    private TextView tvTitle;
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();

    private LocationManagerUtil locationManagerUtil;
    private MySingleLocationListener mySingleLocationListener;// 定位
    private LocationClient locationClient;

    private TextView tvTime;
    private TextView tvAddress;
    private ImageView ivSignin;// 签到图片
    private ImageView ivDelPic;// 删除图片
    private Button btnAffirm;
    private EditText etRemark;
    private RadioGroup radioGroup;
    private LinearLayout layAddress;

    private Timer timer;
    private TimerTask timerTask;
    private long interval = 1000 * 10;// 定时器刷新时间，单位ms
    public static final int TAKE_BIG_PICTURE = 1; // 拍照
    private UploadImageBean imgBean;// 上传图片实体
    // 图片保存文件名
    private String fileName = "signinImg.jpg";
    private UploadFileUtil uploadFileUtil;

    private int type = 0;
    private String time;
    private String address;
    private String remark;
    private double lat = 0d;
    private double lng = 0d;

    // GPS提示
    private DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_sign_in);
        if (!NetworkUtils.isGpsEnable(this)) {
            dialogUtil.showDialog();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        ivTopLeft = (TextView) findViewById(R.id.tv_top_left);
        tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.hit_card);
        tvTime = (TextView) findViewById(R.id.tv_signin_time);
        tvAddress = (TextView) findViewById(R.id.tv_signin_address);
        ivSignin = (ImageView) findViewById(R.id.iv_signin);
        ivDelPic = (ImageView) findViewById(R.id.iv_del_pic);
        btnAffirm = (Button) findViewById(R.id.btn_affirm);
        etRemark = (EditText) findViewById(R.id.et_remark_signin);
        radioGroup = (RadioGroup) findViewById(R.id.rg_signin);
        layAddress = (LinearLayout) findViewById(R.id.lay_address_signin);
        // GPS提示
        dialogUtil = new DialogUtil(this, getString(R.string.gps_open_setting), false);
        dialogUtil.setDialogListener(this);
        uploadFileUtil = new UploadFileUtil(this);

        ivTopLeft.setOnClickListener(this);
        ivSignin.setOnClickListener(this);
        ivDelPic.setOnClickListener(this);
        btnAffirm.setOnClickListener(this);
        layAddress.setOnClickListener(this);
        delIconControl(imgBean);
    }

    @Override
    protected void initData() {
        startTimer();

        locationManagerUtil = LocationManagerUtil.getInstance(getApplicationContext());
        locationClient = locationManagerUtil.initLocation(this, 0);
        mySingleLocationListener = new MySingleLocationListener();
        locationManagerUtil.startLocationListener(locationClient, mySingleLocationListener);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvTime.setText(DateUtils.getYMDHM(System.currentTimeMillis()));
//            tvAddress.setText(mUserConfig.getLocationAddress());
            signinControl();
        }
    };

    /**
     * 签到次数控制
     */
    private void signinControl() {
        if (!DateUtils.isSameDate()) {
            // 将上下班签到置为false
            mUserConfig.saveGoToWork(false).saveGetOffWork(false).apply();
        }
    }

    @Override
    public void confirmDialog() {
        IntentHelper.openGPSSetting(this);
    }

    @Override
    public void uploadFileFail() {
        dismissProgressDialog();
        ToastUtil.show(SignInActivity.this, "图片上传失败请重新提交");
    }

    @Override
    public void uploadFileSuccess(List<UploadPicBean.ImagePath> picList) {
        postTextMessage(type, address, time, remark, picList.get(0).filename);
    }

    /**
     * 单次定位回调
     */
    public class MySingleLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            locationManagerUtil.unRegisterLocationListener(locationClient, mySingleLocationListener);
            LogUtils.d(TAG, LocationCoordinateUtil.getLocationDetail(location));
            String singleStr = LocationCoordinateUtil.getLocationAddress(location);
            if (!TextUtils.isEmpty(singleStr)) {
                LogUtils.d(TAG, "2222" + singleStr);
                lat = location.getLatitude();
                lng = location.getLongitude();
                mUserConfig.saveLocationAddress(singleStr)
                        .saveLatitude(String.valueOf(location.getLatitude()))
                        .saveLongitude(String.valueOf(location.getLongitude()))
                        .apply();
                tvAddress.setText(singleStr);
            } else {
                ToastUtil.show(SignInActivity.this, "定位失败，请重新定位");
            }
        }
    }

    /**
     * 上传足迹
     */
    private void uploadTrack(double longitude, double latitude, String address, String type) {
        String url = Constant.moduleUploadTrack;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", mUserInfo.getDeptId());
        map.put("longitude", String.valueOf(longitude));
        map.put("latitude", String.valueOf(latitude));
        map.put("localtimes", String.valueOf(System.currentTimeMillis()));
        map.put("type", type);//0:百度默认抓取；1：手动抓取
        map.put("positionName", ReplaceSymbolUtil.transcodeToUTF8(address));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean && baseBean.success) {
                    LogUtils.d(TAG, "足迹上传成功！");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleyController.getInstance(getApplicationContext()).addToQueue(post);
    }

    /**
     * 开始计时
     */
    private void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        timer.schedule(timerTask, 0, interval);
    }

    /**
     * 停止计时
     */
    private void clearTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }

    public void onEventMainThread(String action) {
        if (EventBusConfig.SIGNIN_ADDRESS.equals(action)) {
            LogUtils.d(TAG, "SIGNIN_ADDRESS");
            tvAddress.setText(mUserConfig.getLocationAddress());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearTimer();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case TAKE_BIG_PICTURE:// 拍照
                imgBean = null;
                String cameraPath = LocalImageHelper.getInstance().getCameraImgPath();
                LogUtils.d(TAG, cameraPath);
                if (TextUtils.isEmpty(cameraPath)) {
                    Toast.makeText(this, "图片获取失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file = new File(cameraPath);
                if (file.exists()) {
                    Bitmap ivBitmap = BitmapUtils.getPicBitmap(cameraPath, Constant.UPLOAD_WIDTH, Constant.UPLOAD_HEIGHT);
                    FileUtils fileUtils = new FileUtils(this, 2, Constant.APP_FOLDER);
                    if (ivBitmap == null) {
                        Toast.makeText(this, "图片获取失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!fileUtils.saveBitmap(fileName, ivBitmap)) {
                        ivBitmap.recycle();
                        Toast.makeText(this, "图片获取失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ivSignin.setImageBitmap(ivBitmap);
                    imgBean = new UploadImageBean();
                    imgBean.setType(2);
                    imgBean.setCreateTime(System.currentTimeMillis());
                    imgBean.setCameraPath(cameraPath);
                    imgBean.setImgPath(fileUtils.getAppFolderPath() + fileName);
                    delIconControl(imgBean);
                } else {
                    Toast.makeText(this, "图片获取失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1300:
                lat = data.getDoubleExtra("latitude", 0d);
                lng = data.getDoubleExtra("longitude", 0d);
                address = data.getStringExtra("address");
                break;
        }
    }

    /**
     * 提交信息
     */
    private void postMessage() {
        time = tvTime.getText().toString();
        address = tvAddress.getText().toString();
        remark = etRemark.getText().toString().trim();

        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_gotowork:// 上班
                type = 1;
                break;
            case R.id.rb_getoffwork: // 下班
                type = 2;
                break;
        }

        if (type == 1) {
            if (mUserConfig.getGotoWork()) {
                ToastUtil.show(this, "今日已上班签到");
                return;
            }
        } else if (type == 2) {
            if (!mUserConfig.getGotoWork()) {
                ToastUtil.show(this, "请先上班签到");
                return;
            }
            if (mUserConfig.getGetOffWork()) {
                ToastUtil.show(this, "今日已下班签到");
                return;
            }
        } else {
            ToastUtil.show(this, "请选择签到类型");
            return;
        }

        if (TextUtils.isEmpty(address)) {
            ToastUtil.show(this, "地址错误");
            return;
        }

        if (type == 0) {
            ToastUtil.show(this, "请选择签到类型");
            return;
        }

        if (null == imgBean) {
            ToastUtil.show(this, "请拍照");
            return;
        } else {
            long nowTime = System.currentTimeMillis();
            long timeInterval = nowTime - imgBean.createTime;
            if (timeInterval > 1000 * 60 * 5) {
                ToastUtil.show(this, "图片已失效请重新拍摄");
                return;
            }
        }
        showProgressDialog("提交中...", false);
        List<UploadImageBean> uploadList = new ArrayList<>();
        uploadList.add(imgBean);
        uploadFileUtil.uploadFile(uploadList);
    }

    /**
     * 上传文字信息
     */
    private void postTextMessage(final int type, String address, String time, String remark, String imgUrl) {
        final String url = Constant.moduleSignData;
        final Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", mUserInfo.getDeptId());
        map.put("type", String.valueOf(type));
        map.put("address", ReplaceSymbolUtil.transcodeToUTF8(address));
        map.put("signTime", time);
        map.put("remarks", ReplaceSymbolUtil.transcodeToUTF8(ReplaceSymbolUtil.replaceHuanHang(remark)));
        map.put("picUrl", imgUrl);
        map.put("longitude", String.valueOf(lng));
        map.put("latitude", String.valueOf(lat));
        map.put("localtimes", String.valueOf(System.currentTimeMillis()));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                dismissProgressDialog();
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean) {
                    if (baseBean.success) {
                        ToastUtil.show(SignInActivity.this, getResources().getString(R.string.siginin_succeed));
                        if (type == 1) {// 上班签到
                            afterGoToWork();
                        } else if (type == 2) {// 下班签到
                            afterGetOffWork();
                        }
                        finish();
                    } else {
                        ToastUtil.show(SignInActivity.this, baseBean.msg);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
                ToastUtil.show(SignInActivity.this, getResources().getString(R.string.siginin_fail));
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
    }

    /**
     * 上班签到后执行操作
     */
    private void afterGoToWork() {
        mUserConfig.saveGoToWork(true).apply();
        // 关闭定位
        locationManagerUtil.unRegisterLocationListener();
        locationManagerUtil.stopLocation();
        // 保存签到时间
        mUserConfig.saveDate(DateUtils.getYMD(System.currentTimeMillis()));
        AlarmUtil.startServiceAlarm();
    }

    /**
     * 下班签到后执行操作
     */
    private void afterGetOffWork() {
        mUserConfig.saveGetOffWork(true).apply();
        AlarmUtil.cancelServiceAlarm();
    }

    private void showMessage(final String message, final Integer errorNo) {

        new Handler(SignInActivity.this.getMainLooper()).post(new Runnable() {
            public void run() {
                Toast.makeText(SignInActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * 图片删除按钮控制
     */
    private void delIconControl(UploadImageBean bean) {
        if (null == bean) {
            ivDelPic.setVisibility(View.GONE);
        } else {
            ivDelPic.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.iv_signin:// 拍照
                if (imgBean != null && imgBean.type == 2) {
                    ArrayList<String> imgs = new ArrayList<>();
                    imgs.add(imgBean.getCameraPath());
                    Intent intent = new Intent(this, BigImageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("Imgs", imgs);
                    bundle.putInt("Position", 0);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //  拍照后保存图片的绝对路径
                    String cameraPath = LocalImageHelper.getInstance().setCameraImgPath();
                    LogUtils.d(TAG, cameraPath);
                    File file = new File(cameraPath);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, TAKE_BIG_PICTURE);
                }
                break;
            case R.id.iv_del_pic:// 删除图片
                if (null != imgBean) {
                    FileSizeUtil.deleteFile(imgBean.imgPath);
                    ivSignin.setImageResource(R.drawable.select_pic_icon);
                    imgBean = null;
                    delIconControl(imgBean);
                }
                break;
            case R.id.btn_affirm:// 提交
                if (NetworkUtils.isGpsEnable(this)) {
                    postMessage();
                } else {
                    dialogUtil.showDialog();
                }
                break;
            case R.id.lay_address_signin:// 看地图
                Intent mapIntent = new Intent(this, SigninMapActivity.class);
                mapIntent.putExtra("address", tvAddress.getText().toString());
                startActivityForResult(mapIntent, 1300);
                break;
        }
    }
}
