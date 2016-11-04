package com.salesman.activity.client;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.salesman.R;
import com.salesman.activity.home.SingleSelectionActivity;
import com.salesman.activity.picture.PhotoReviewActivity;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.ClientCheckDetailBean;
import com.salesman.entity.SingleSelectionBean;
import com.salesman.network.BaseHelper;
import com.salesman.utils.StaticData;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserInfoPreference;
import com.salesman.views.CircleHeadView;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 客户审核详情
 * Created by LiHuai on 2016/06/22.
 */
public class ClientCheckDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ClientCheckDetailActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private String storeId, provinceId, cityId, areaId;
    private TextView tvShopName, tvBoosName, tvTel, tvArea, tvAddress, tvLicense, tvDingge;
    private ImageView ivLicense, ivIdCardFront, ivIdCardBlack;
    private CircleHeadView hv;
    private Button btnPass, btnRefuse;
    private LinearLayout layChoose;
    // 图片
    private DisplayImageOptions options;
    private List<Integer> circleList = StaticData.getCircleColorList();

    private ClientCheckDetailBean.CheckDetailBean detailBean;
    // 定格单选数据
    private ArrayList<SingleSelectionBean> mData = new ArrayList<>();
    // 定格Id
    private String dingGeId = "";
    private String title = "审核";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_client_check_detail);
    }

    @Override
    protected void initView() {
        title = getIntent().getStringExtra("title");
        TextView tvLeft = (TextView) findViewById(R.id.tv_top_left);
        tvLeft.setOnClickListener(this);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        if (TextUtils.isEmpty(title)) {
            tvTitle.setText("审核");
        } else {
            tvTitle.setText(title);
        }

        tvShopName = (TextView) findViewById(R.id.tv_name_client);
        tvBoosName = (TextView) findViewById(R.id.tv_shopkeeper);
        tvTel = (TextView) findViewById(R.id.tv_tel);
        tvArea = (TextView) findViewById(R.id.tv_area);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvLicense = (TextView) findViewById(R.id.tv_license_num);
        tvDingge = (TextView) findViewById(R.id.tv_dingge);
        hv = (CircleHeadView) findViewById(R.id.hv_check);
        ivLicense = (ImageView) findViewById(R.id.iv_license);
        ivIdCardFront = (ImageView) findViewById(R.id.iv_idcard_front);
        ivIdCardBlack = (ImageView) findViewById(R.id.iv_idcard_black);
        btnPass = (Button) findViewById(R.id.btn_pass);
        btnRefuse = (Button) findViewById(R.id.btn_refuse);
        layChoose = (LinearLayout) findViewById(R.id.lay_choose_dingge);
        hv.setCircleColorResources(StaticData.getImageId(circleList));
        // 图片
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_pic_default)
                .showImageForEmptyUri(R.drawable.no_pic_default)
                .showImageOnFail(R.drawable.no_pic_default)
                .cacheInMemory(true)// 开启内存缓存
                .cacheOnDisk(true) // 开启硬盘缓存
                .resetViewBeforeLoading(false).build();

        ivLicense.setOnClickListener(this);
        ivIdCardFront.setOnClickListener(this);
        ivIdCardBlack.setOnClickListener(this);
        btnPass.setOnClickListener(this);
        btnRefuse.setOnClickListener(this);
        layChoose.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        storeId = getIntent().getStringExtra("storeId");
//        provinceId = getIntent().getStringExtra("provinceId");
//        cityId = getIntent().getStringExtra("cityId");
//        areaId = getIntent().getStringExtra("areaId");

        getClientDetail();
    }

    private void getClientDetail() {
        showProgressDialog(getString(R.string.loading1), false);
        String url = Constant.moduleClientCheckDetail;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("storeId", storeId);
        map.put("deptId", mUserInfo.getDeptId());
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                dismissProgressDialog();
                ClientCheckDetailBean clientCheckDetailBean = GsonUtils.json2Bean(response, ClientCheckDetailBean.class);
                if (null != clientCheckDetailBean) {
                    if (clientCheckDetailBean.success && null != clientCheckDetailBean.data) {
                        detailBean = clientCheckDetailBean.data;
                        setData(clientCheckDetailBean.data);
                    } else {
                        ToastUtil.show(ClientCheckDetailActivity.this, clientCheckDetailBean.msg);
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

    private void setData(ClientCheckDetailBean.CheckDetailBean bean) {
        initPic(bean.licensePic, bean.idCardUpPic, bean.idCardDownPic);
        initDingGe(bean.spGroupList);
        tvShopName.setText(bean.name);
        tvBoosName.setText(bean.contact);
        tvTel.setText(bean.mobile);
        tvArea.setText(bean.areaStr);
        tvAddress.setText(bean.address);
        tvLicense.setText(bean.licenseNum);

    }

    /**
     * 初始化图片
     *
     * @param licensePic
     * @param idCardUp
     * @param idCardDown
     */
    private void initPic(String licensePic, String idCardUp, String idCardDown) {
        if (!TextUtils.isEmpty(licensePic)) {
            ImageLoader.getInstance().displayImage(licensePic, ivLicense, options);
        }
        if (!TextUtils.isEmpty(idCardUp)) {
            ImageLoader.getInstance().displayImage(idCardUp, ivIdCardFront, options);
        }
        if (!TextUtils.isEmpty(idCardDown)) {
            ImageLoader.getInstance().displayImage(idCardDown, ivIdCardBlack, options);
        }

    }

    /**
     * 看大图
     *
     * @param imgUrl
     */
    private void lookBigPic(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {
            ArrayList<String> imgsList = new ArrayList<>();
            imgsList.add(imgUrl);
            Intent intent = new Intent(this, PhotoReviewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("Imgs", imgsList);
            bundle.putInt("Position", 0);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * 初始化定格数据
     *
     * @param list
     */
    private void initDingGe(List<ClientCheckDetailBean.DingGeBean> list) {
        if (null != list) {
            mData.clear();
            for (ClientCheckDetailBean.DingGeBean dingGeBean : list) {
                SingleSelectionBean single = new SingleSelectionBean(String.valueOf(dingGeBean.id), dingGeBean.name);
                mData.add(single);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case SingleSelectionActivity.FLAG:
                if (null != data) {
                    SingleSelectionBean bean = data.getParcelableExtra(SingleSelectionActivity.SELECT_BEAN);
                    if (null != bean) {
                        setSelectItem(bean);
                        tvDingge.setText(bean.name);
                        dingGeId = bean.id;
                    }
                }
                break;
        }
    }

    private void setSelectItem(SingleSelectionBean bean) {
        for (SingleSelectionBean singleSelectionBean : mData) {
            if (bean.id.equals(singleSelectionBean.id)) {
                singleSelectionBean.setIsSelect(true);
            } else {
                singleSelectionBean.setIsSelect(false);
            }
        }
    }

    /**
     * 审核或拒绝
     *
     * @param status
     * @param spGroupId
     */
    private void checkClient(String status, String spGroupId) {
        showProgressDialog(getString(R.string.submitting), false);
        String url = Constant.moduleClientCheck;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", mUserInfo.getDeptId());
        map.put("mobile", mUserInfo.getMobile());
        map.put("userName", mUserInfo.getUserName());
        map.put("storeId", storeId);
        map.put("spGroupId", spGroupId);// 定格id
        map.put("status", status);
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                dismissProgressDialog();
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean) {
                    if (baseBean.success) {
                        EventBus.getDefault().post(EventBusConfig.CLIENT_CHECK);
                        ToastUtil.show(ClientCheckDetailActivity.this, baseBean.msg);
                        finish();
                    } else {
                        ToastUtil.show(ClientCheckDetailActivity.this, baseBean.msg);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.iv_license:// 营业执照
                if (null != detailBean) {
                    lookBigPic(detailBean.licensePic);
                }
                break;
            case R.id.iv_idcard_front:// 身份证正面
                if (null != detailBean) {
                    lookBigPic(detailBean.idCardUpPic);
                }
                break;
            case R.id.iv_idcard_black:// 身份证反面
                if (null != detailBean) {
                    lookBigPic(detailBean.idCardDownPic);
                }
                break;
            case R.id.lay_choose_dingge:// 选择定格
                Intent intent = new Intent(this, SingleSelectionActivity.class);
                intent.putParcelableArrayListExtra("data", mData);
                intent.putExtra(SingleSelectionActivity.TITLE, "定格");
                startActivityForResult(intent, SingleSelectionActivity.FLAG);
                break;
            case R.id.btn_pass:// 通过
                if (TextUtils.isEmpty(dingGeId)) {
                    ToastUtil.show(this, "请选择定格");
                    return;
                } else {
                    checkClient("1", dingGeId);
                }
                break;
            case R.id.btn_refuse:// 拒绝
                checkClient("3", "");
                break;
        }
    }
}
