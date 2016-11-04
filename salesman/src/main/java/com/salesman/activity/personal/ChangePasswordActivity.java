package com.salesman.activity.personal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.network.BaseHelper;
import com.salesman.utils.DeviceUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserInfoPreference;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.Map;

/**
 * 修改密码界面
 * Created by LiHuai on 2016/1/27.
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = ChangePasswordActivity.class.getSimpleName();

    private TextView tvBack;
    private Button btnAffirm;
    private EditText etOld;
    private EditText etNew;
    private EditText etNewAgain;

    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_change_password);
    }

    @Override
    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.change_password);
        tvBack = (TextView) findViewById(R.id.tv_top_left);
        btnAffirm = (Button) findViewById(R.id.btn_affirm);
        btnAffirm.setText(R.string.affirm);
        etOld = (EditText) findViewById(R.id.et_old_pw);
        etNew = (EditText) findViewById(R.id.et_new_pw);
        etNewAgain = (EditText) findViewById(R.id.et_new_pw_again);
        tvBack.setOnClickListener(this);
        btnAffirm.setOnClickListener(this);
        btnAffirm.setEnabled(false);
        etOld.addTextChangedListener(textWatcher);
        etNew.addTextChangedListener(textWatcher);
        etNewAgain.addTextChangedListener(textWatcher);
    }

    private void postMessage() {
        hideSoftInput(this, etNewAgain.getWindowToken());
        String oldPw = etOld.getText().toString().trim();
        String newPw = etNew.getText().toString().trim();
        String newPwAgain = etNewAgain.getText().toString().trim();

        if (TextUtils.isEmpty(oldPw)) {
            ToastUtil.show(this, "请输入原密码");
            return;
        }
        if (TextUtils.isEmpty(newPw)) {
            ToastUtil.show(this, "请输入新密码");
            return;
        }
        if (newPw.length() < 6) {
            ToastUtil.show(this, "密码长度需大于6位");
            return;
        }

        if (oldPw.equals(newPw)) {
            ToastUtil.show(this, "新密码不能与原密码一致");
            return;
        }

        if (TextUtils.isEmpty(oldPw)) {
            ToastUtil.show(this, "请输入新密码");
            return;
        }

        if (!newPw.equals(newPwAgain)) {
            ToastUtil.show(this, "两次输入密码不同");
            return;
        }

        String url = Constant.moduleChangePassword;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("mobile", mUserInfo.getMobile());
        map.put("password", oldPw);
        map.put("newPwd", newPw);
        map.put("confirmPwd", newPwAgain);
        map.put("deviceUUID", DeviceUtil.getImei(this));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean) {
                    if (baseBean.success) {
                        ToastUtil.show(ChangePasswordActivity.this, getResources().getString(R.string.pw_success_change));
//                        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                        ChangePasswordActivity.this.finish();
                    } else {
                        ToastUtil.show(ChangePasswordActivity.this, baseBean.msg);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
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
            if (!TextUtils.isEmpty(etOld.getText()) && !TextUtils.isEmpty(etNew.getText()) && !TextUtils.isEmpty(etNewAgain.getText())) {
                btnAffirm.setEnabled(true);
            } else {
                btnAffirm.setEnabled(false);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.btn_affirm:
                postMessage();
                break;
        }
    }
}
