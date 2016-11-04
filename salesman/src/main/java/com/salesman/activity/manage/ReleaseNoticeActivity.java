package com.salesman.activity.manage;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.home.BigImageActivity;
import com.salesman.adapter.NoticeReleaseObjAdapter;
import com.salesman.adapter.ShopImageAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.NoticeListBean;
import com.salesman.entity.NoticeReleaseObj;
import com.salesman.entity.UploadImageBean;
import com.salesman.entity.UploadPicBean;
import com.salesman.global.BeanListHolder;
import com.salesman.network.BaseHelper;
import com.salesman.utils.DialogUtil;
import com.salesman.utils.LocalImageHelper;
import com.salesman.utils.PictureUtil;
import com.salesman.utils.ReplaceSymbolUtil;
import com.salesman.utils.StringUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UploadFileUtil;
import com.salesman.utils.UserInfoPreference;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.IntentHelper;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.InnerGridView;
import com.studio.jframework.widget.dialog.DialogCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 发公告界面
 * Created by LiHuai on 2016/1/26.
 */
public class ReleaseNoticeActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemClickListener, DialogUtil.PickPhotoOnClickListener, UploadFileUtil.UploadFileListener {
    public static final String TAG = ReleaseNoticeActivity.class.getSimpleName();

    private TextView ivBack;
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private EditText edSubject;
    private EditText edContent;
    private TextView tvObj;
    private Button btnFaBu;
    private RadioGroup radioGroup;

    // 发布对象选择提示
    private Dialog mDialog;
    private ListView listView;
    private NoticeReleaseObjAdapter adapter;
    private List<NoticeReleaseObj> mData = new ArrayList<>();
    private int sendType = 0;
    // 图片部分
    private InnerGridView gridView;
    private List<UploadImageBean> uploadImgList = BeanListHolder.getUploadImageBeanList();
    private ShopImageAdapter imgAdpter;
    private PictureUtil pictureUtil;
    private DialogUtil dialogUtil;
    private UploadFileUtil uploadFileUtil;

    private String subject = "";
    private String content = "";

    @Override
    protected void initView() {
        ivBack = (TextView) findViewById(R.id.tv_top_left);
        ivBack.setOnClickListener(this);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.release_notice);

        edSubject = (EditText) findViewById(R.id.ed_subject_notice);
        edContent = (EditText) findViewById(R.id.ed_content_notice);
        tvObj = (TextView) findViewById(R.id.tv_obj_notice);
        tvObj.setText(StringUtil.getSpannStrNoticeReleaseObj("请选择"));
        btnFaBu = (Button) findViewById(R.id.btn_affirm);
        btnFaBu.setText(R.string.release);
        radioGroup = (RadioGroup) findViewById(R.id.rg_release_obj);
        // 对象选择提示
        View objView = View.inflate(this, R.layout.dialog_notice_release_obj, null);
        listView = (ListView) objView.findViewById(R.id.lv_dialog);
        mDialog = DialogCreator.createNormalDialog(this, objView, DialogCreator.Position.CENTER);
        // 图片部分
        gridView = (InnerGridView) findViewById(R.id.gv_release_notice);
        imgAdpter = new ShopImageAdapter(this, uploadImgList);
        gridView.setAdapter(imgAdpter);
        pictureUtil = new PictureUtil(this, uploadImgList, imgAdpter);
        uploadFileUtil = new UploadFileUtil(this);
        // 图片选择提示
        dialogUtil = new DialogUtil(this);
        dialogUtil.setPickPhotoOnClickListener(this);

        tvObj.setOnClickListener(this);
        btnFaBu.setOnClickListener(this);
        btnFaBu.setEnabled(false);
        edSubject.addTextChangedListener(textWatcher);
        edContent.addTextChangedListener(textWatcher);
        radioGroup.setOnCheckedChangeListener(this);
        listView.setOnItemClickListener(this);
        gridView.setOnItemClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_release_announcement);
    }

    @Override
    protected void initData() {
        mData = BeanListHolder.getReleaseObjList();
        adapter = new NoticeReleaseObjAdapter(this, mData);
        listView.setAdapter(adapter);

        NoticeListBean.NoticeBean noticeBean = (NoticeListBean.NoticeBean) getIntent().getSerializableExtra("NoticeBean");
        if (null != noticeBean) {
            subject = noticeBean.subject;
            content = noticeBean.content;
            edSubject.setText(subject);
            edContent.setText(ReplaceSymbolUtil.reverseReplaceHuanHang(content));
        }
    }

    private void checkMessage() {
        subject = edSubject.getText().toString().trim();
        content = edContent.getText().toString().trim();

        if (TextUtils.isEmpty(subject)) {
            ToastUtil.show(this, getResources().getString(R.string.input_subject));
            return;
        }
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show(this, getResources().getString(R.string.input_content));
            return;
        }
        if (sendType == 0) {
            ToastUtil.show(this, getResources().getString(R.string.select_release_obj));
            return;
        }
        uploadImgList = imgAdpter.getData();
        showProgressDialog(getString(R.string.submitting), false);
        if (PictureUtil.isPictureUpload(uploadImgList)) {
            uploadFileUtil.uploadFile(uploadImgList);
        } else {
            postData("");
        }
    }

    /**
     * 发布公告
     */
    private void postData(String picUrl) {
        String url = Constant.moduleRelaseNoticeV130;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("userId", mUserInfo.getUserId());
        map.put("deptId", mUserInfo.getDeptId());
        map.put("subject", ReplaceSymbolUtil.transcodeToUTF8(subject));
        map.put("content", ReplaceSymbolUtil.transcodeToUTF8(ReplaceSymbolUtil.replaceHuanHang(content)));
        map.put("sendType", String.valueOf(sendType));
        map.put("picUrl", picUrl);
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean) {
                    if (baseBean.success) {
                        ToastUtil.show(ReleaseNoticeActivity.this, getResources().getString(R.string.release_succeed));
                        setResult(RESULT_OK, getIntent());
                        finish();
                    } else {
                        ToastUtil.show(ReleaseNoticeActivity.this, baseBean.msg);
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

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(edSubject.getText()) && !TextUtils.isEmpty(edContent.getText())) {
                btnFaBu.setEnabled(true);
            } else {
                btnFaBu.setEnabled(false);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.tv_obj_notice:// 发布对象
                mDialog.show();
                break;
            case R.id.btn_affirm:// 发布
                checkMessage();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_all_people:
                tvObj.setText(getResources().getString(R.string.release_object) + "@" + getResources().getString(R.string.all_people));
                break;
            case R.id.rb_my_dept:
                tvObj.setText(getResources().getString(R.string.release_object) + "@" + getResources().getString(R.string.my_department));
                break;
            case R.id.rb_all_manmager:
                tvObj.setText(getResources().getString(R.string.release_object) + "@" + getResources().getString(R.string.all_manager));
                break;
            case R.id.rb_gioc_people:
                tvObj.setText(getResources().getString(R.string.release_object) + "@" + getResources().getString(R.string.operating_center_people));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.gv_release_notice:// 图片
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
            case R.id.lv_dialog: // 发布对象列表
                mDialog.dismiss();
                NoticeReleaseObj noticeReleaseObj = adapter.getItem(position);
                sendType = noticeReleaseObj.id;
                tvObj.setText(StringUtil.getSpannStrNoticeReleaseObj(noticeReleaseObj.name));
                adapter.setCheckItem(position);
                break;
        }
    }

    @Override
    public void onTakePhotoClick() {
        String cameraPath = LocalImageHelper.getInstance().setCameraImgPath();
        IntentHelper.takePhoto(this, cameraPath);
    }

    @Override
    public void onPickPhotoClick() {
        IntentHelper.pickPhotoFromGallery(ReleaseNoticeActivity.this);
    }

    @Override
    public void uploadFileFail() {
        dismissProgressDialog();
        ToastUtil.show(this, getResources().getString(R.string.upload_pic_fail));
    }

    @Override
    public void uploadFileSuccess(List<UploadPicBean.ImagePath> picList) {
        postData(PictureUtil.sliceUploadPicString(picList));
    }
}
