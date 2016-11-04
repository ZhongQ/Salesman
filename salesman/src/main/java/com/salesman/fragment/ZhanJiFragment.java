package com.salesman.fragment;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.work.OrderListActivity;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseFragment;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.ZhanJiDetailBean;
import com.salesman.network.BaseHelper;
import com.salesman.umeng.UmengAnalyticsUtil;
import com.salesman.umeng.UmengConfig;
import com.salesman.utils.DateUtils;
import com.salesman.utils.StringUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserInfoPreference;
import com.salesman.views.ArcProgressBar;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.DateUtil;
import com.studio.jframework.utils.LogUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 战绩界面
 * Created by LiHuai on 2016/07/05.
 */
public class ZhanJiFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = ZhanJiFragment.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private ArcProgressBar aPBDianBao;
    private ArcProgressBar aPBLianHe;
    private TextView tvRepresentative, tvTime;
    private TextView tvVisitDb, tvNoVisitDb, tvVisitLh, tvNoVisitLh;
    private String createTime = DateUtils.getCurrentDate();
    private LinearLayout layZhanjiBD;

    @Override
    protected int setLayout() {
        return R.layout.fragment_zhan_ji;
    }

    @Override
    protected void findViews(View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.my_zhanji);
        ImageView ivTopR2 = (ImageView) view.findViewById(R.id.iv_top_right2);
        ivTopR2.setImageResource(R.drawable.calendar_icon);
        ivTopR2.setOnClickListener(this);

        tvRepresentative = (TextView) view.findViewById(R.id.tv_representative);
        tvTime = (TextView) view.findViewById(R.id.tv_time);
        tvVisitDb = (TextView) view.findViewById(R.id.tv_visit_dianbao);
        tvNoVisitDb = (TextView) view.findViewById(R.id.tv_no_visit_dianbao);
        tvVisitLh = (TextView) view.findViewById(R.id.tv_visit_lianhe);
        tvNoVisitLh = (TextView) view.findViewById(R.id.tv_no_visit_lianhe);
        layZhanjiBD = (LinearLayout) view.findViewById(R.id.lay_zhanji_bd);

        aPBDianBao = (ArcProgressBar) view.findViewById(R.id.progressbar_dianbao);
        aPBLianHe = (ArcProgressBar) view.findViewById(R.id.progressbar_lianhe);
    }

    @Override
    protected void initialization() {
        aPBDianBao.setTitleText(getString(R.string.dianbao_turnover));
        aPBDianBao.setUnitText(getString(R.string.look_detail));
        aPBLianHe.setTitleText(getString(R.string.zhuanjiao_ziying));
        aPBLianHe.setUnitText(getString(R.string.look_detail));
        getZhanJiDetail();
    }

    @Override
    protected void bindEvent() {
        aPBDianBao.setOnClickListener(this);
        aPBLianHe.setOnClickListener(this);
    }

    @Override
    protected void onCreateView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        UmengAnalyticsUtil.umengOnPageStart(UmengConfig.ZHANJI_PAGE);
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengAnalyticsUtil.umengOnPageEnd(UmengConfig.ZHANJI_PAGE);
    }

    private void getZhanJiDetail() {
        String url = Constant.moduleZhanJiDetail;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", mUserInfo.getDeptId());
        map.put("areaId", mUserInfo.getAreaId());
        map.put("salesmanId", mUserInfo.getUserId());
        map.put("userType", mUserInfo.getUserType());
        map.put("createTime", createTime);
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
            }
        });
        VolleyController.getInstance(mContext).addToQueue(post);
    }

    /**
     * 日期选择器
     */
    private void showCalendar() {
        Calendar c = Calendar.getInstance();
        // THEME_HOLO_LIGHT
        new DatePickerDialog(mContext, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                createTime = DateUtils.fmtTimeToStr((year + "-" + (month + 1) + "-" + dayOfMonth), "yyyy-MM-dd");
                Date selectDate = DateUtil.toShortDate(createTime);
                Date todayDate = DateUtil.toShortDate(new Date());
                if (selectDate.before(todayDate) || selectDate.equals(todayDate)) {
                    getZhanJiDetail();
                } else {
                    ToastUtil.show(mContext, getResources().getString(R.string.date_after_today));
                }
            }

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_right2:
                showCalendar();
                break;
            case R.id.progressbar_dianbao:// 店宝明细
                Intent dianBaoIntent = new Intent(mContext, OrderListActivity.class);
                dianBaoIntent.putExtra("createTime", createTime);
                dianBaoIntent.putExtra("salesmanId", mUserInfo.getUserId());
                dianBaoIntent.putExtra("isUnion", "1");
                dianBaoIntent.putExtra("title", getString(R.string.jiaoyi_detail_dianbao));
                startActivity(dianBaoIntent);
                break;
            case R.id.progressbar_lianhe:// 转角自营明细
                Intent lianHeIntent = new Intent(mContext, OrderListActivity.class);
                lianHeIntent.putExtra("createTime", createTime);
                lianHeIntent.putExtra("salesmanId", mUserInfo.getUserId());
                lianHeIntent.putExtra("isUnion", "2");
                lianHeIntent.putExtra("title", getString(R.string.jiaoyi_detail_zhuanjiao));
                startActivity(lianHeIntent);
                break;
        }
    }
}
