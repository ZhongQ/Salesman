package com.salesman.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.guide.NewActionGuideActivity;
import com.salesman.adapter.WorkAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseFragment;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.AllDealDataBean;
import com.salesman.entity.WorkBean;
import com.salesman.global.BeanListHolder;
import com.salesman.network.BaseHelper;
import com.salesman.presenter.RequestDataPresenter;
import com.salesman.umeng.UmengAnalyticsUtil;
import com.salesman.umeng.UmengConfig;
import com.salesman.utils.DateUtils;
import com.salesman.utils.StringUtil;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserInfoPreference;
import com.salesman.view.OnCommonListener;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.DateUtil;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.widget.itemdecoration.DividerGridItemDecoration;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工作界面
 * Created by LiHuai on 2016/07/04.
 */
public class WorkFragment extends BaseFragment implements View.OnClickListener, OnCommonListener {
    public static final String TAG = WorkFragment.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private RecyclerView recyclerView;
    private List<WorkBean> mData = BeanListHolder.getWorkBeanList();
    private WorkAdapter adapter;
    private TextView tvArea, tvDianBao, tvLianHe, tvTotalDeal;
    private String createTime = DateUtils.getCurrentDate();
    private RequestDataPresenter requestDataPresenter = new RequestDataPresenter(this);

    @Override
    protected int setLayout() {
        return R.layout.fragment_work;
    }

    @Override
    protected void findViews(View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.work);
        ImageView ivTopR2 = (ImageView) view.findViewById(R.id.iv_top_right2);
        ivTopR2.setImageResource(R.drawable.refresh_work);
        ivTopR2.setOnClickListener(this);

        tvArea = (TextView) view.findViewById(R.id.tv_area);
        tvDianBao = (TextView) view.findViewById(R.id.tv_dianbao_deal);
        tvLianHe = (TextView) view.findViewById(R.id.tv_lianhe_deal);
        tvTotalDeal = (TextView) view.findViewById(R.id.tv_total_deal);
        AssetManager mgr = mContext.getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, Constant.CUSTOM_FONTS);
        tvDianBao.setTypeface(tf);
        tvLianHe.setTypeface(tf);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_work);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(mContext));
        adapter = new WorkAdapter(mContext, mData);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initialization() {
        requestDataPresenter.getData();
    }

    @Override
    protected void bindEvent() {

    }

    @Override
    protected void onCreateView() {

    }

    @Override
    protected void onVisible(boolean prepared) {
        super.onVisible(prepared);
        if (!StringUtil.isOpenGuide(TAG)) {
            Intent intent = new Intent(mContext, NewActionGuideActivity.class);
            intent.putExtra("come_from", TAG);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengAnalyticsUtil.umengOnPageStart(UmengConfig.WORK_PAGE);
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengAnalyticsUtil.umengOnPageEnd(UmengConfig.WORK_PAGE);
    }

    private void getAllDealData() {
        String url = Constant.moduleWorkDealData;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", mUserInfo.getDeptId());
        map.put("areaId", mUserInfo.getAreaId());
        map.put("createTime", createTime);
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                LogUtils.d(TAG, response);
                AllDealDataBean allDealDataBean = GsonUtils.json2Bean(response, AllDealDataBean.class);
                if (null != allDealDataBean && allDealDataBean.success && null != allDealDataBean.data) {
//                    tvDianBao.setText(String.valueOf(StringUtil.getStringToInt(allDealDataBean.data.saleMoney)));
//                    tvLianHe.setText(String.valueOf(StringUtil.getStringToInt(allDealDataBean.data.buyMoney)));
                    setAnimation(0, StringUtil.getStringToInt(allDealDataBean.data.saleMoney), 1000, tvDianBao);
                    setAnimation(0, StringUtil.getStringToInt(allDealDataBean.data.buyMoney), 1000, tvLianHe);
                    tvTotalDeal.setText(String.valueOf(StringUtil.getStringToInt(allDealDataBean.data.monthMoney)));
                    tvArea.setText(allDealDataBean.data.deptName);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
            }
        });
        VolleyController.getInstance(mContext).addToQueue(post);
    }

    private void setAnimation(int start, int end, int during, final TextView tvText) {
        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(during);
        animator.setIntValues(start, end);
        animator.setInterpolator(new LinearInterpolator());// 时间插值;LinearInterpolator：表示以常量速率改变
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tvText.setText(String.valueOf((int) animation.getAnimatedValue()));
            }
        });
        animator.start();
    }

    /**
     * 无限旋转动画
     *
     * @param view
     */
    private void setRotationAnimation(View view) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "rotation", 0f, 359f);
        anim.setDuration(500);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.start();
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
                    showProgressDialog(getString(R.string.loading1), false);
                    requestDataPresenter.getData();
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
                requestDataPresenter.getData();
                break;
        }
    }

    @Override
    public Context getRequestContext() {
        return mContext;
    }

    @Override
    public String getRequestUrl() {
        return Constant.moduleWorkDealData;
    }

    @Override
    public Map<String, String> getRequestParam() {
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", mUserInfo.getDeptId());
        map.put("areaId", mUserInfo.getAreaId());
        map.put("createTime", createTime);
        return map;
    }

    @Override
    public void showLoading() {
        showProgressDialog(getString(R.string.loading1), false);
    }

    @Override
    public void hideLoading() {
        dismissProgressDialog();
    }

    @Override
    public void requestSuccess(String response) {
        LogUtils.d(TAG, TAG);
        AllDealDataBean allDealDataBean = GsonUtils.json2Bean(response, AllDealDataBean.class);
        if (null != allDealDataBean && allDealDataBean.success && null != allDealDataBean.data) {
            setAnimation(0, StringUtil.getStringToInt(allDealDataBean.data.saleMoney), 1000, tvDianBao);
            setAnimation(0, StringUtil.getStringToInt(allDealDataBean.data.buyMoney), 1000, tvLianHe);
            tvTotalDeal.setText(String.valueOf(StringUtil.getStringToInt(allDealDataBean.data.monthMoney)));
            tvArea.setText(allDealDataBean.data.deptName);
        }
    }

    @Override
    public void requestFail() {

    }
}
