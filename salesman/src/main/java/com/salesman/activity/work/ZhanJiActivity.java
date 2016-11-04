package com.salesman.activity.work;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.home.SingleSelectionActivity;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.SingleSelectionBean;
import com.salesman.entity.ZhanJiDetailBean;
import com.salesman.entity.ZhanJiFilterListBean;
import com.salesman.network.BaseHelper;
import com.salesman.utils.DateUtils;
import com.salesman.utils.StringUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserInfoPreference;
import com.salesman.utils.ZhanJiFilterUtil;
import com.salesman.views.ArcProgressBar;
import com.salesman.views.HoveringScrollView;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.DateUtil;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 战绩界面
 * Created by LiHuai on 2016/07/06.
 */
public class ZhanJiActivity extends BaseActivity implements View.OnClickListener, ZhanJiFilterUtil.OnGetZhanJiFilterListener {
    private static final String TAG = ZhanJiActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private final int FLAG = 1008;
    private ArcProgressBar aPBDianBao;
    private ArcProgressBar aPBLianHe;
    private TextView tvRepresentative, tvTime;
    private TextView tvVisitDb, tvNoVisitDb, tvVisitLh, tvNoVisitLh;
    private TextView tvFilter1, tvFilter2, btnFilter;
    private String areaId = "", deptId = "", salesmanId = "", createTime = "";
    private LinearLayout layZhanjiBD;

    private HoveringScrollView hoverScl;
    // 筛选数据
    private List<ZhanJiFilterListBean.ZhanJiFilterBean> mFilter1 = ZhanJiFilterUtil.getZhanJiFilterList();
    private ArrayList<SingleSelectionBean> mFilter2 = ZhanJiFilterUtil.getSecondZhanJiFilterList();
    private ZhanJiFilterUtil zhanJiFilterUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_zhan_ji);

        setData();
    }

    @Override
    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.my_zhanji);
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        TextView tvRight = (TextView) findViewById(R.id.tv_top_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("");
        tvRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar_icon, 0);
        tvRepresentative = (TextView) findViewById(R.id.tv_representative);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvVisitDb = (TextView) findViewById(R.id.tv_visit_dianbao);
        tvNoVisitDb = (TextView) findViewById(R.id.tv_no_visit_dianbao);
        tvVisitLh = (TextView) findViewById(R.id.tv_visit_lianhe);
        tvNoVisitLh = (TextView) findViewById(R.id.tv_no_visit_lianhe);
        tvFilter1 = (TextView) findViewById(R.id.tv_zhanji_filter1);
        tvFilter2 = (TextView) findViewById(R.id.tv_zhanji_filter2);
        btnFilter = (TextView) findViewById(R.id.tv_filter_zhanji);
        layZhanjiBD = (LinearLayout) findViewById(R.id.lay_zhanji_bd);

        hoverScl = (HoveringScrollView) findViewById(R.id.hs_zhanji);
        hoverScl.setTopView(R.id.lay_top);
        aPBDianBao = (ArcProgressBar) findViewById(R.id.progressbar_dianbao);
        aPBLianHe = (ArcProgressBar) findViewById(R.id.progressbar_lianhe);

        zhanJiFilterUtil = new ZhanJiFilterUtil();

        tvBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
        zhanJiFilterUtil.setOnGetZhanJiFilterListener(this);
    }

    @Override
    protected void initData() {
        if (ZhanJiFilterUtil.isSecondRequest()) {
            showProgressDialog(getString(R.string.loading1), false);
            zhanJiFilterUtil.getZhanJiFilterData();
            return;
        }
        if (!mFilter1.isEmpty()) {
            tvFilter1.setText(mFilter1.get(0).deptName);
        }
        if (!mFilter2.isEmpty()) {
            tvFilter2.setText(mFilter2.get(0).name);
            mFilter2.get(0).setIsSelect(true);
            deptId = mFilter2.get(0).id;
            areaId = mFilter2.get(0).idSecond;
            salesmanId = mFilter2.get(0).idThird;
        } else {
            deptId = mUserInfo.getDeptId();
            areaId = mUserInfo.getAreaId();
        }
        createTime = DateUtils.getCurrentDate();
        getZhanJiDetail();
    }

    private void setData() {
//        aPBDianBao.setTitleText(getString(R.string.dianbao_turnover));
        aPBDianBao.setUnitText(getString(R.string.dianbao_turnover));
        aPBDianBao.setMaxValue(0);
        aPBDianBao.setShowArrow(false);
//        aPBLianHe.setTitleText(getString(R.string.lianhe_turnover));
        aPBLianHe.setUnitText(getString(R.string.zhuanjiao_ziying));
        aPBLianHe.setMaxValue(0);
        aPBLianHe.setShowArrow(false);
    }

    /**
     * 日期选择器
     */
    private void showCalendar() {
        Calendar c = Calendar.getInstance();
        // THEME_HOLO_LIGHT
        new DatePickerDialog(this, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                createTime = DateUtils.fmtTimeToStr((year + "-" + (month + 1) + "-" + dayOfMonth), "yyyy-MM-dd");
                Date selectDate = DateUtil.toShortDate(createTime);
                Date todayDate = DateUtil.toShortDate(new Date());
                if (selectDate.before(todayDate) || selectDate.equals(todayDate)) {
                    getZhanJiDetail();
                } else {
                    ToastUtil.show(ZhanJiActivity.this, getResources().getString(R.string.date_after_today));
                }
            }

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void getZhanJiDetail() {
        showProgressDialog(getString(R.string.loading1), false);
        String url = Constant.moduleZhanJiDetail;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", deptId);
        map.put("areaId", areaId);
        map.put("salesmanId", salesmanId);
        map.put("userType", mUserInfo.getUserType());
        map.put("createTime", createTime);
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                ZhanJiDetailBean zhanJiDetailBean = GsonUtils.json2Bean(response, ZhanJiDetailBean.class);
                if (null != zhanJiDetailBean && zhanJiDetailBean.success && null != zhanJiDetailBean.data) {
                    tvTime.setText(zhanJiDetailBean.data.createTime);
                    tvVisitDb.setText(String.valueOf(StringUtil.getStringToInt(zhanJiDetailBean.data.visitSaleMoney)));
                    tvNoVisitDb.setText(String.valueOf(StringUtil.getStringToInt(zhanJiDetailBean.data.saleMoneyAll) - StringUtil.getStringToInt(zhanJiDetailBean.data.visitSaleMoney)));
                    tvVisitLh.setText(String.valueOf(StringUtil.getStringToInt(zhanJiDetailBean.data.buyMoney)));
                    tvNoVisitLh.setText(String.valueOf(StringUtil.getStringToInt(zhanJiDetailBean.data.buyMoneyAll) - StringUtil.getStringToInt(zhanJiDetailBean.data.buyMoney)));
                    aPBDianBao.setMaxValue(StringUtil.getStringToInt(zhanJiDetailBean.data.saleMoneyAll));
                    aPBDianBao.setProgress(StringUtil.getStringToInt(zhanJiDetailBean.data.visitSaleMoney));
                    aPBLianHe.setMaxValue(StringUtil.getStringToInt(zhanJiDetailBean.data.buyMoneyAll));
                    aPBLianHe.setProgress(StringUtil.getStringToInt(zhanJiDetailBean.data.buyMoney));
                    if (!TextUtils.isEmpty(zhanJiDetailBean.data.dbUser)) {
                        tvRepresentative.setText(zhanJiDetailBean.data.dbUser);
                        layZhanjiBD.setVisibility(View.VISIBLE);
                    } else {
                        layZhanjiBD.setVisibility(View.GONE);
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
        switch (requestCode) {
            case FLAG:
                if (null != data) {
                    SingleSelectionBean bean = data.getParcelableExtra(SingleSelectionActivity.SELECT_BEAN);
                    if (null != bean) {
                        setSelectItem(bean);
                        tvFilter2.setText(bean.name);
                        deptId = bean.id;
                        areaId = bean.idSecond;
                        salesmanId = bean.idThird;
                        getZhanJiDetail();
                    }
                }
                break;
        }
    }

    private void setSelectItem(SingleSelectionBean bean) {
        for (SingleSelectionBean singleSelectionBean : mFilter2) {
            if (bean.id.equals(singleSelectionBean.id) && bean.idSecond.equals(singleSelectionBean.idSecond) && bean.idThird.equals(singleSelectionBean.idThird)) {
                singleSelectionBean.setIsSelect(true);
            } else {
                singleSelectionBean.setIsSelect(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.tv_top_right:
                showCalendar();
                break;
            case R.id.tv_filter_zhanji:// 筛选
                Intent zhanJiIntent = new Intent(this, SingleSelectionActivity.class);
                zhanJiIntent.putParcelableArrayListExtra("data", mFilter2);
                zhanJiIntent.putExtra(SingleSelectionActivity.TITLE, "区域选择");
                startActivityForResult(zhanJiIntent, FLAG);
//                if (!mFilter2.isEmpty()) {
//                }
                break;
        }
    }

    @Override
    public void onGetZhanJiFilterSuccess() {
        mFilter1 = ZhanJiFilterUtil.getZhanJiFilterList();
        mFilter2 = ZhanJiFilterUtil.getSecondZhanJiFilterList();
        initData();
    }

    @Override
    public void onGetZhanJiFilterFail() {
        dismissProgressDialog();
    }
}
