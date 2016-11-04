package com.salesman.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.adapter.ReleaseDailyAdapter;
import com.salesman.adapter.ShopImageAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.ReleaseDailyBean;
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
import com.salesman.utils.UserInfoPreference;
import com.salesman.utils.ViewUtil;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.IntentHelper;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.InnerGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 发日报界面
 * Created by LiHuai on 2016/3/25.
 */
public class ReleaseDailyActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, DialogUtil.PickPhotoOnClickListener, DialogUtil.DialogOnClickListener, UploadFileUtil.UploadFileListener {
    public static final String TAG = ReleaseDailyActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private TextView tvBack;
    private List<ReleaseDailyBean.ReleaseListBean> mDatas = new ArrayList<>();

    private Button btnAffirm;
    private EditText etRemark;
    private ScrollView scrollView;
    private LinearLayout layDaily;
    // 图片部分
    private InnerGridView gridView;
    private List<UploadImageBean> uploadImgList = BeanListHolder.getUploadImageBeanList();
    private ShopImageAdapter imgAdpter;
    private PictureUtil pictureUtil;
    private UploadFileUtil uploadFileUtil;
    // 模板id
    private String tmplId = "";
    // 模板名称
    private String tmplName = "";
    // 图片选择提示
    private DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_release_daily);
    }

    @Override
    protected void initView() {
        tmplId = getIntent().getStringExtra("tmplId");
        tmplName = getIntent().getStringExtra("tmplName");
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.release_report);
        tvBack = (TextView) findViewById(R.id.tv_top_left);
        TextView tvLogName = (TextView) findViewById(R.id.tv_log_name);
        tvLogName.setText(tmplName);
        btnAffirm = (Button) findViewById(R.id.btn_affirm);
        etRemark = (EditText) findViewById(R.id.et_remark_daily);
        scrollView = (ScrollView) findViewById(R.id.scroll_release_daily);
        // 图片部分
        gridView = (InnerGridView) findViewById(R.id.gv_inner_daily);
        imgAdpter = new ShopImageAdapter(this, uploadImgList);
        gridView.setAdapter(imgAdpter);
        pictureUtil = new PictureUtil(this, uploadImgList, imgAdpter);
        uploadFileUtil = new UploadFileUtil(this);
        // 图片选择提示
        dialogUtil = new DialogUtil(this);
        dialogUtil.setPickPhotoOnClickListener(this);

        layDaily = (LinearLayout) findViewById(R.id.lay_release_daily);

        tvBack.setOnClickListener(this);
        btnAffirm.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        getDailyContent();
    }

    private void getDailyContent() {
        showProgressDialog(getString(R.string.loading1), false);
        String url = Constant.moduleDailyFieldList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("tmplId", tmplId);
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                ReleaseDailyBean releaseDailyBean = GsonUtils.json2Bean(response, ReleaseDailyBean.class);
                if (null != releaseDailyBean && releaseDailyBean.success && null != releaseDailyBean.data.list) {
                    initLinearLayout(releaseDailyBean.data.list);
                    slideTop();
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

    private void initLinearLayout(List<ReleaseDailyBean.ReleaseListBean> data) {
        mDatas.clear();
        mDatas.addAll(data);
        layDaily.removeAllViews();
        for (ReleaseDailyBean.ReleaseListBean bean : data) {
            View view = View.inflate(this, R.layout.item_release_daily, null);
            TextView tvKey = (TextView) view.findViewById(R.id.tv_key_daily);
            EditText etValue = (EditText) view.findViewById(R.id.et_value_daily);
            tvKey.setText(bean.fieldCnName);
            setEditText(etValue, bean);
            etValue.addTextChangedListener(new MyTextWatcher(bean));
            ViewUtil.scaleContentView((ViewGroup) view);
            layDaily.addView(view);
        }
    }

    /**
     * 设置输入框输入类型及输入长度
     *
     * @param et
     * @param releaseListBean
     */
    private void setEditText(EditText et, ReleaseDailyBean.ReleaseListBean releaseListBean) {
        switch (releaseListBean.fieldType) {
            case ReleaseDailyAdapter.STRING:
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                setEtHint(et, ReleaseDailyAdapter.STRING, releaseListBean.isRequired);
                break;
            case ReleaseDailyAdapter.INTEGER:
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                et.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                setEtHint(et, ReleaseDailyAdapter.INTEGER, releaseListBean.isRequired);
                break;
            case ReleaseDailyAdapter.FLOAT:
                et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);// 输入小数
                setEtHint(et, ReleaseDailyAdapter.FLOAT, releaseListBean.isRequired);
                break;
        }
        // 设置输入框输入长度
        if (releaseListBean.length > 0) {
            InputFilter[] filters = {new InputFilter.LengthFilter(releaseListBean.length)};
            et.setFilters(filters);
        } else {
            InputFilter[] filters = {new InputFilter.LengthFilter(3)};
            et.setFilters(filters);
        }
    }

    /**
     * 设置输入框提示语
     *
     * @param et
     * @param type
     * @param isMust
     */
    private void setEtHint(EditText et, int type, int isMust) {
        switch (type) {
            case ReleaseDailyAdapter.STRING:
                if (1 == isMust) {
                    et.setHint(getResources().getString(R.string.daily_text_hint_must));
                } else {
                    et.setHint(getResources().getString(R.string.daily_text_hint_select));
                }
                break;
            case ReleaseDailyAdapter.INTEGER:
            case ReleaseDailyAdapter.FLOAT:
                if (1 == isMust) {
                    et.setHint(getResources().getString(R.string.daily_number_hint_must));
                } else {
                    et.setHint(getResources().getString(R.string.daily_number_hint_select));
                }
                break;
        }
    }

    /**
     * 滑动到顶部
     */
    private void slideTop() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    /**
     * 判断信息
     */
    private void judgeMessage() {
        if (mDatas.size() > 0 && layDaily.getChildCount() != 0) {
            for (ReleaseDailyBean.ReleaseListBean bean : mDatas) {
                LogUtils.d(TAG, bean.getValue());
                if (bean.isRequired == 1) {
                    if (TextUtils.isEmpty(bean.getValue()) || TextUtils.isEmpty(bean.getValue().trim())) {
                        ToastUtil.show(this, getString(R.string.daily_hint_must_write));
                        return;
                    }
                }
            }
            uploadImage();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.gv_inner_daily:// // 图片
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
                    dialogUtil.showPhotoPickDialog();
                }
                break;
        }
    }

    /**
     * 提交图片
     */
    private void uploadImage() {
        showProgressDialog(getString(R.string.submitting), false);
        uploadImgList = imgAdpter.getData();
        if (PictureUtil.isPictureUpload(uploadImgList)) {
            uploadFileUtil.uploadFile(uploadImgList);
        } else {
            // 上传文本信息
            postMessage(null);
        }
    }

    /**
     * 上传日报
     *
     * @param imgList
     */
    private void postMessage(List<UploadPicBean.ImagePath> imgList) {
        // 日报信息
        StringBuffer sbDaily = new StringBuffer();
        for (ReleaseDailyBean.ReleaseListBean bean : mDatas) {
            if (!TextUtils.isEmpty(bean.fieldCnName)) {// 由于数据中有一条假数据，所以需要过滤掉
                // 版本 V1.2.0
                sbDaily.append(bean.toString());
            }
        }
        LogUtils.d(TAG, sbDaily.toString());

        String url = Constant.moduleAddReport;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("content", ReplaceSymbolUtil.transcodeToUTF8(sbDaily.toString()));
        map.put("picUrl", PictureUtil.sliceUploadPicString(imgList));
        map.put("userId", mUserInfo.getUserId());
        map.put("userName", mUserInfo.getUserName());
        map.put("deptId", mUserInfo.getDeptId());
        map.put("tmplId", tmplId);
        map.put("remark", ReplaceSymbolUtil.transcodeToUTF8(ReplaceSymbolUtil.replaceHuanHang(etRemark.getText().toString().trim())));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean) {
                    if (baseBean.success) {
                        ToastUtil.show(ReleaseDailyActivity.this, getString(R.string.release_succeed));
                        ReleaseDailyActivity.this.finish();
                    } else {
                        ToastUtil.show(ReleaseDailyActivity.this, baseBean.msg);
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
    public void onBackPressed() {
        DialogUtil dialogUtil = new DialogUtil(this, "确认退出此次编辑？");
        dialogUtil.setDialogListener(this);
        dialogUtil.showDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                onBackPressed();
                break;
            case R.id.btn_affirm:// 提交
                judgeMessage();
                break;
        }
    }


    @Override
    public void confirmDialog() {
        finish();
    }

    @Override
    public void onTakePhotoClick() {
        String cameraPath = LocalImageHelper.getInstance().setCameraImgPath();
        IntentHelper.takePhoto(this, cameraPath);
    }

    @Override
    public void onPickPhotoClick() {
        IntentHelper.pickPhotoFromGallery(ReleaseDailyActivity.this);
    }

    @Override
    public void uploadFileFail() {
        dismissProgressDialog();
        ToastUtil.show(ReleaseDailyActivity.this, getResources().getString(R.string.upload_pic_fail));
    }

    @Override
    public void uploadFileSuccess(List<UploadPicBean.ImagePath> picList) {
        postMessage(picList);
    }

    private class MyTextWatcher implements TextWatcher {
        private ReleaseDailyBean.ReleaseListBean releaseListBean;

        public MyTextWatcher(ReleaseDailyBean.ReleaseListBean releaseListBean) {
            this.releaseListBean = releaseListBean;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (null != s && null != releaseListBean) {
                releaseListBean.setValue(s.toString());
            }
        }
    }
}
