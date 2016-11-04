package com.salesman.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.MainActivity;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.UserBean;
import com.salesman.network.BaseHelper;
import com.salesman.utils.DeviceUtil;
import com.salesman.utils.MobileInfo;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserConfigPreference;
import com.salesman.utils.UserInfoPreference;
import com.salesman.views.ClearEditText;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.utils.PackageUtils;

import java.util.Map;

/**
 * 登录界面
 * Created by LiHuai on 2016/2/3.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
    private ClearEditText etUserName;
    private ClearEditText etPassWord;
    private Button btnLogin;
    private boolean isBackClick = true;
    private String account = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_login);
    }

    @Override
    protected void initView() {
        account = getIntent().getStringExtra("account");
        etUserName = (ClearEditText) findViewById(R.id.et_username);
        etPassWord = (ClearEditText) findViewById(R.id.et_password);
        etUserName.addTextChangedListener(textWatcher);
        etPassWord.addTextChangedListener(textWatcher);
        btnLogin = (Button) findViewById(R.id.btn_login);

        if (TextUtils.isEmpty(account)) {
            etUserName.setText(mUserInfo.getMobile());
        } else {
            etUserName.setText(account);
            mUserInfo.saveMobile(account).apply();
        }
        etUserName.setSelection(etUserName.getText().length());
        btnLogin.setOnClickListener(this);
    }

    /**
     * 登录
     */
    private void logIn() {
        final String userName = etUserName.getText().toString().trim();
        String password = etPassWord.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            ToastUtil.show(this, "请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.show(this, "请输入密码");
            return;
        }
//        if (password.length() < 6) {
//            ToastUtil.show(this, "密码长度小于六位");
//            return;
//        }
        showProgressDialog("登录中...", false);
        String url = Constant.moduleLogIn;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("userName", userName);// 手机号码
        map.put("password", password);
        map.put("token", "");
        map.put("version", PackageUtils.getVersionName(this));
        map.put("deviceUUID", DeviceUtil.getImei(this));
        map.put("deviceName", MobileInfo.getMobileModel());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                if (!isBackClick) {
                    return;
                }
                LogUtils.d(TAG, response);
                UserBean userBean = GsonUtils.json2Bean(response, UserBean.class);
                if (null != userBean) {
                    if (null != userBean.data && userBean.success) {
                        mUserInfo.saveUserId(userBean.data.userId)
                                .saveUserName(userBean.data.userName)
                                .saveNickName(userBean.data.nickName)
                                .saveMobile(userBean.data.mobile)
                                .saveDeviceUUID(userBean.data.deviceUUID)
                                .saveSessionid(userBean.data.sessionid)
                                .saveToken(userBean.data.token)
                                .saveSex(userBean.data.gender)
                                .saveUserType(userBean.data.userType)
                                .savePostName(userBean.data.postName)
                                .saveDeptId(userBean.data.deptId)
                                .saveDeptName(userBean.data.deptName)
                                .saveAreaId(userBean.data.areaId)
                                .saveBDType(userBean.data.bdType);
                        mUserInfo.apply();
                        mUserConfig.saveGoToWork(1 == userBean.data.signStart)
                                .saveGetOffWork(1 == userBean.data.signEnd);
                        mUserConfig.apply();
                        if (!TextUtils.isEmpty(userBean.data.timeHz)) {
                            mUserInfo.saveUploadTrackTime(Long.parseLong(userBean.data.timeHz) * 1000).apply();
                        } else {
                            mUserInfo.saveUploadTrackTime(60000).apply();
                        }
//                        if (mUserConfig.getIsFirst()) {
//                            startActivity(new Intent(LoginActivity.this, GuideActivity.class));
//                        } else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        }
                        LoginActivity.this.finish();
                    } else {
                        ToastUtil.show(LoginActivity.this, userBean.msg);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
                ToastUtil.show(LoginActivity.this, "网络错误");
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isBackClick = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                hideSoftInput(this, etPassWord.getWindowToken());
                logIn();
                break;
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(etUserName.getText()) && !TextUtils.isEmpty(etPassWord.getText())) {
                btnLogin.setEnabled(true);
            } else {
                btnLogin.setEnabled(false);
            }
        }
    };
}

