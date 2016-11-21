package com.salesman.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.login.LoginActivity;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.network.BaseHelper;
import com.salesman.utils.AlarmUtil;
import com.salesman.utils.DialogUtil;
import com.salesman.utils.UserConfigPreference;
import com.salesman.utils.UserInfoPreference;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 设置界面
 * Created by LiHuai on 2016/4/19.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, DialogUtil.DialogOnClickListener {
    public static final String TAG = SettingActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();

    private ToggleButton tbTrack;
    private TextView tvUnLogIn;
    private LinearLayout layModifPassword, layAppLog, layAboutUs;

    // 退出提示
    private DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_setting);

        initEvent();
    }

    @Override
    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.setting);
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        tbTrack = (ToggleButton) findViewById(R.id.tb_track);
        tbTrack.setChecked(mUserConfig.getTrackSet());
        tvUnLogIn = (TextView) findViewById(R.id.btn_affirm);
        tvUnLogIn.setText(R.string.cancel_login);
        layModifPassword = (LinearLayout) findViewById(R.id.lay_modif_password);
        layAppLog = (LinearLayout) findViewById(R.id.lay_log_app);
        layAboutUs = findView(R.id.lay_about_us);

        // 注销
        dialogUtil = new DialogUtil(this, "您确定要注销登录？");
        dialogUtil.setDialogListener(this);

        tvBack.setOnClickListener(this);
    }

    private void initEvent() {
        tbTrack.setOnCheckedChangeListener(this);
        tvUnLogIn.setOnClickListener(this);
        layModifPassword.setOnClickListener(this);
        layAppLog.setOnClickListener(this);
        layAboutUs.setOnClickListener(this);
    }

    /**
     * 注销登陆
     */
    private void logOut() {
        String url = Constant.moduleLogOut;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
            }
        });
        VolleyController.getInstance(SettingActivity.this).addToQueue(post);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.btn_affirm:// 注销登录
                dialogUtil.showDialog();
                break;
            case R.id.lay_modif_password:// 修改密码
                Intent changeIntent = new Intent(this, ChangePasswordActivity.class);
                startActivity(changeIntent);
                break;
            case R.id.lay_log_app:// app日志
                Intent appLogIntent = new Intent(this, AppLogRecordActivity.class);
                startActivity(appLogIntent);
                break;
            case R.id.lay_about_us:// 关于我们
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tb_track:
                LogUtils.d(TAG, String.valueOf(isChecked));
                mUserConfig.saveTrackSet(isChecked).apply();
                if (isChecked) {
                    if (mUserConfig.getGotoWork() && !mUserConfig.getGetOffWork()) {
                        AlarmUtil.startServiceAlarm();
                    }
                } else {
                    AlarmUtil.cancelServiceAlarm();
                }
                break;
        }
    }

    @Override
    public void confirmDialog() {
        logOut();

        Intent logInIntent = new Intent(SettingActivity.this, LoginActivity.class);
        logInIntent.putExtra("account", mUserInfo.getMobile());
        mUserInfo.clear();
        mUserConfig.saveHandExit(true).apply();
        AlarmUtil.cancelServiceAlarm();// 停止定时器
        EventBus.getDefault().post(EventBusConfig.APP_EXIT);
        startActivity(logInIntent);
        SettingActivity.this.finish();
    }
}
