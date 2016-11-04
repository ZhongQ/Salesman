package com.salesman.activity.work;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.salesman.R;
import com.salesman.activity.guide.NewActionGuideActivity;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.SubordinateListBean;
import com.salesman.entity.TrendBean;
import com.salesman.presenter.RequestDataPresenter;
import com.salesman.utils.DateUtils;
import com.salesman.utils.StringUtil;
import com.salesman.utils.UserInfoPreference;
import com.salesman.utils.UserInfoUtil;
import com.salesman.utils.ZhanJiFilterUtil;
import com.salesman.view.OnCommonListener;
import com.salesman.views.popupwindow.FilterItem;
import com.salesman.views.popupwindow.FilterPopup;
import com.studio.jframework.utils.LogUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 业绩趋势界面
 * Created by LiHuai on 2016/10/09.
 */
public class YeJiTrendActivity extends BaseActivity implements View.OnClickListener, OnCommonListener, ZhanJiFilterUtil.OnGetZhanJiFilterListener, FilterPopup.OnItemOnClickListener {
    private final String TAG = YeJiTrendActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private RequestDataPresenter mPresenter = new RequestDataPresenter(this);

    private TextView tvTitle;
    private LineChart mChart;
    private TextView tvDealDianBao, tvDealZiYing;
    private TextView tvTodayDB, tvYesterdayDB, tvMonthDB, tvLastMonthDB;
    private TextView tvTodayZY, tvYesterdayZY, tvMonthZY, tvLastMonthZY;
    private TextView tvTodayAdd, tvYesterdayAdd, tvMonthAdd, tvLastMonthAdd;
    private TextView tvTodayActive, tvYesterdayActive, tvMonthActive, tvLastMonthActive;
    private ProgressBar pb1, pb2, pb3, pb4, pb5, pb6, pb7, pb8;

    private ArrayList<Entry> values, values2;
    // 筛选
    private FilterPopup filterPopup;
    private ZhanJiFilterUtil zhanJiFilterUtil;
    // 筛选数据
    private List<FilterItem> mFilter = ZhanJiFilterUtil.getFilterList();
    private View topView;
    private String deptId = "", deptName = "", salesmanId = "", userType = "";
    private SubordinateListBean.XiaShuBean xiaShuBean, xiaShuBean2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_ye_ji_trend);
        initLineChart();
    }

    @Override
    protected void initView() {
        super.initView();
        // 由下属列表传递进来
        xiaShuBean = (SubordinateListBean.XiaShuBean) getIntent().getSerializableExtra("XiaShu");
        TextView tvLeft = findView(R.id.tv_top_left);
        tvLeft.setOnClickListener(this);
        tvTitle = findView(R.id.tv_top_title);
        if (null == xiaShuBean) {
            tvTitle.setText("业绩趋势");
        } else {
            if ("1".equals(xiaShuBean.userType)) {// 若下属为管理员，则显示部门名称
                tvTitle.setText(xiaShuBean.deptName);
            } else {
                tvTitle.setText(xiaShuBean.userName);
            }
        }
        if (UserInfoUtil.isAdministrator() && null == xiaShuBean) {
            tvTitle.setOnClickListener(this);
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.triangle_down_icon, 0);
            tvTitle.setCompoundDrawablePadding(10);
        }

        TextView tvRight = findView(R.id.tv_top_right);
        tvRight.setText("历史业绩");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(this);

        mChart = findView(R.id.line_chart);

        tvDealDianBao = findView(R.id.tv_dianbao_deal);
        tvDealZiYing = findView(R.id.tv_ziying_deal);

        tvTodayDB = findView(R.id.tv_today_dianbao);
        tvYesterdayDB = findView(R.id.tv_yesterday_dianbao);
        tvMonthDB = findView(R.id.tv_month_dianbao);
        tvLastMonthDB = findView(R.id.tv_last_month_dianbao);

        tvTodayZY = findView(R.id.tv_today_ziying);
        tvYesterdayZY = findView(R.id.tv_yesterday_ziying);
        tvMonthZY = findView(R.id.tv_month_ziying);
        tvLastMonthZY = findView(R.id.tv_last_month_ziying);

        tvTodayAdd = findView(R.id.tv_today_add);
        tvYesterdayAdd = findView(R.id.tv_yesterday_add);
        tvMonthAdd = findView(R.id.tv_month_add);
        tvLastMonthAdd = findView(R.id.tv_last_month_add);

        tvTodayActive = findView(R.id.tv_today_active);
        tvYesterdayActive = findView(R.id.tv_yesterday_active);
        tvMonthActive = findView(R.id.tv_month_active);
        tvLastMonthActive = findView(R.id.tv_last_month_active);

        pb1 = findView(R.id.pb_1);
        pb2 = findView(R.id.pb_2);
        pb3 = findView(R.id.pb_3);
        pb4 = findView(R.id.pb_4);
        pb5 = findView(R.id.pb_5);
        pb6 = findView(R.id.pb_6);
        pb7 = findView(R.id.pb_7);
        pb8 = findView(R.id.pb_8);

        topView = findView(R.id.top_view_trend);
        filterPopup = new FilterPopup(this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        filterPopup.setItemOnClickListener(this);

        zhanJiFilterUtil = new ZhanJiFilterUtil();
        zhanJiFilterUtil.setOnGetZhanJiFilterListener(this);

    }

    @Override
    protected void initData() {
        super.initData();

        mPresenter.getData();

        if (ZhanJiFilterUtil.isSecondRequest()) {
            showProgressDialog(getString(R.string.loading1), false);
            zhanJiFilterUtil.getZhanJiFilterData();
        }
    }

    /**
     * 初始化折线图
     */
    private void initLineChart() {
        // 背景
        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(true);
        // 拖拽移动
        mChart.setDragEnabled(true);
        // 缩放
        mChart.setScaleEnabled(true);
        // 扩展可以在x轴和y轴分别完成
        mChart.setPinchZoom(false);
        // 表格描述
        mChart.setDescription(null);
        // 位置
//        mChart.setDescriptionPosition(200f, 100f);
//        mChart.setDrawBorders(false);
//        mChart.setAutoScaleMinMaxEnabled(false);
        // Y轴是否缩放
        mChart.setScaleYEnabled(false);
        // 是否展示右边轴线
        mChart.getAxisRight().setEnabled(false);
        // 高亮指示线是否拖拽
        mChart.setHighlightPerDragEnabled(true);
        // 高亮指示线是否点击
        mChart.setHighlightPerTapEnabled(true);
        // 无数据时
        mChart.setNoDataText("暂无数据");
        // 色块说明
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);
        // 设置额外的偏移，将被添加到自动计算的偏移
        mChart.setExtraBottomOffset(10);

        // 设置监听
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float x = e.getX();
                for (Entry value : values) {// 店宝
                    if (value.getX() == x) {
                        tvDealDianBao.setText(StringUtil.formatNumbers(value.getY()));
                    }
                }
                for (Entry entry : values2) {// 自营
                    if (entry.getX() == x) {
                        tvDealZiYing.setText(StringUtil.formatNumbers(entry.getY()));
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        ///////////////////X轴/////////////////
        XAxis xAxis = mChart.getXAxis();
        // 是否网格线
        xAxis.setDrawGridLines(false);
        // 设置网格虚线
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        // 文字位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // X轴文字偏移量
        xAxis.setYOffset(10);
        // X轴文字选中角度
//        xAxis.setLabelRotationAngle(45f);
        // X轴字体颜色
        xAxis.setTextColor(getResources().getColor(R.color.color_666666));
        // 绘制时会避免“剪掉”在x轴上的图表或屏幕边缘的第一个和最后一个坐标轴标签项
        xAxis.setAvoidFirstLastClipping(true);
        // 是否绘制X轴
        xAxis.setDrawAxisLine(true);
        // X轴颜色
//        xAxis.setAxisLineColor(Color.RED);
        // X轴标注
        xAxis.setDrawLabels(true);
        // X轴间隔尺寸
        xAxis.setGranularity(1f);
        // X轴高度
//        xAxis.setAxisLineWidth(4f);
        // X轴原点(设置为true时，X轴不准确)
        xAxis.setCenterAxisLabels(false);

        xAxis.setEnabled(true);

        xAxis.setLabelCount(6);

//        xAxis.setDrawLimitLinesBehindData(false);
        // 设置标签字符间的最大空隙
//        xAxis.setSpaceMax(4);
        // 设置标签字符间的最小空隙
        xAxis.setSpaceMin(1);

        // 自定义X轴标注
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private SimpleDateFormat mFormat = new SimpleDateFormat("MM/dd");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                long millis = TimeUnit.DAYS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        ///////////////////Y轴/////////////////
        YAxis yAxis = mChart.getAxisLeft();
        yAxis.removeAllLimitLines();
//        yAxis.setAxisMaxValue(1000f);
//        yAxis.setAxisMinValue(0f);
//        yAxis.setYOffset(20f);
        yAxis.setTextColor(getResources().getColor(R.color.color_666666));
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setDrawZeroLine(false);
        // limit lines are drawn behind data (and not on top)
        yAxis.setDrawLimitLinesBehindData(true);
        yAxis.setLabelCount(5);
        // 自定义Y轴标签
        yAxis.setValueFormatter(new IAxisValueFormatter() {
            DecimalFormat mFormat = new DecimalFormat("###,###,###,###");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value >= 10000) {
                    int temp = (int) value / 10000;
                    return String.valueOf(temp) + "w";
                } else if (value >= 1000) {
                    int temp = (int) value / 1000;
                    return String.valueOf(temp) + "k";
                } else {
                    return "0";
                }
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!StringUtil.isOpenGuide(TAG)) {
            Intent intent = new Intent(this, NewActionGuideActivity.class);
            intent.putExtra("come_from", TAG);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.tv_top_right:// 历史业绩
                Intent yeJiIntent = new Intent(this, YeJiActivity.class);
                if (null != xiaShuBean) {
                    yeJiIntent.putExtra("XiaShu", xiaShuBean);
                } else {
                    yeJiIntent.putExtra("XiaShu", xiaShuBean2);
                }
                startActivity(yeJiIntent);
                break;
            case R.id.tv_top_title:// 趋势筛选
                filterPopup.addPopupList(mFilter);
                filterPopup.show(topView);
                tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.triangle_up_icon, 0);
                filterPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.triangle_down_icon, 0);
                    }
                });
                break;
        }
    }

    @Override
    public Context getRequestContext() {
        return this;
    }

    @Override
    public String getRequestUrl() {
        return Constant.moduleYeJiTrend;
    }

    @Override
    public Map<String, String> getRequestParam() {
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        if (null == xiaShuBean) {
            if (TextUtils.isEmpty(salesmanId)) {
                map.put("userId", mUserInfo.getUserId());
            } else {
                map.put("userId", salesmanId);
            }
            if (TextUtils.isEmpty(userType)) {
                map.put("userType", mUserInfo.getUserType());
            } else {
                map.put("userType", userType);
            }
            if (TextUtils.isEmpty(deptId)) {
                map.put("deptId", mUserInfo.getDeptId());
            } else {
                map.put("deptId", deptId);
            }
            if (TextUtils.isEmpty(deptName)) {
                map.put("deptName", mUserInfo.getDeptName());
            } else {
                map.put("deptName", deptName);
            }
        } else {
            map.put("userId", xiaShuBean.userId);
            map.put("deptId", xiaShuBean.deptId);
            map.put("deptName", xiaShuBean.deptName);
            map.put("userType", xiaShuBean.userType);
        }
        map.put("createTime", DateUtils.getCurrentDate());
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
        LogUtils.d(TAG, response);
        TrendBean trendBean = GsonUtils.json2Bean(response, TrendBean.class);
        if (null != trendBean) {
            if (trendBean.success && trendBean.data != null) {
                TrendBean.DataBean dataBean = trendBean.data;
                if (null != dataBean.list) {
                    initLineChartData(dataBean.list);
                }
                // 新增客户和活跃客户
                tvTodayAdd.setText(String.valueOf(dataBean.jt_regStore));
                tvYesterdayAdd.setText(String.valueOf(dataBean.zt_regStore));
                tvMonthAdd.setText(String.valueOf(dataBean.by_regStore));
                tvLastMonthAdd.setText(String.valueOf(dataBean.sy_regStore));
                tvTodayActive.setText(String.valueOf(dataBean.jt_activeStore));
                tvYesterdayActive.setText(String.valueOf(dataBean.zt_activeStore));
                tvMonthActive.setText(String.valueOf(dataBean.by_activeStore));
                tvLastMonthActive.setText(String.valueOf(dataBean.sy_activeStore));
                // 店宝交易额
                tvTodayDB.setText(StringUtil.formatNumbers(dataBean.jt_turnover));
                tvYesterdayDB.setText(StringUtil.formatNumbers(dataBean.zt_turnover));
                tvMonthDB.setText(StringUtil.formatNumbers(dataBean.by_turnover));
                tvLastMonthDB.setText(StringUtil.formatNumbers(dataBean.sy_turnover));
                setProgressData(pb1, pb2, dataBean.jt_turnover, dataBean.zt_turnover);
                setProgressData(pb3, pb4, dataBean.by_turnover, dataBean.sy_turnover);
                // 自营交易额
                tvTodayZY.setText(StringUtil.formatNumbers(dataBean.jt_zjturnover));
                tvYesterdayZY.setText(StringUtil.formatNumbers(dataBean.zt_zjturnover));
                tvMonthZY.setText(StringUtil.formatNumbers(dataBean.by_zjturnover));
                tvLastMonthZY.setText(StringUtil.formatNumbers(dataBean.sy_zjturnover));
                setProgressData(pb5, pb6, dataBean.jt_zjturnover, dataBean.zt_zjturnover);
                setProgressData(pb7, pb8, dataBean.by_zjturnover, dataBean.sy_zjturnover);
            }
        }
    }

    @Override
    public void requestFail() {

    }

    /**
     * 初始化折线图数据
     *
     * @param data
     */
    private void initLineChartData(List<TrendBean.DataBean.PointBean> data) {
        values = new ArrayList<>();
        values2 = new ArrayList<>();
        if (!data.isEmpty() && data.size() > 0) {
            tvDealDianBao.setText(String.valueOf(StringUtil.formatNumbers(data.get(data.size() - 1).turnover)));
            tvDealZiYing.setText(String.valueOf(StringUtil.formatNumbers(data.get(data.size() - 1).zjturnover)));
        } else {
            tvDealDianBao.setText("0");
            tvDealZiYing.setText("0");
        }
        for (TrendBean.DataBean.PointBean pointBean : data) {
            long days = TimeUnit.MILLISECONDS.toDays(DateUtils.fmtDateToLong(pointBean.dayTime, "yyyy-MM-dd"));
            // 时间相差一天，所以加1
            values.add(new Entry((float) days + 1, pointBean.turnover));
            values2.add(new Entry((float) days + 1, pointBean.zjturnover));
        }
        LineDataSet set1, set2;
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
            set2.setValues(values2);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        } else {
            set1 = new LineDataSet(values, "DianBao");
            set2 = new LineDataSet(values2, "ZiYing");
            // 设置虚线
//            set1.enableDashedLine(10f, 5f, 0f);
            // 指示线虚线
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
            // 指示线颜色
            set1.setHighLightColor(getResources().getColor(R.color.color_0090ff));
            // 指示线宽度
            set1.setHighlightLineWidth(1f);
            // 水平指示线
            set1.setDrawHorizontalHighlightIndicator(false);

            // 设置线条颜色
            set1.setColor(getResources().getColor(R.color.color_ff9c00));
            // 设置线条宽度
            set1.setLineWidth(1.5f);
            // 设置圆点颜色
            set1.setCircleColor(getResources().getColor(R.color.color_ff9c00));
            // 设置圆点半径
            set1.setCircleRadius(2f);
            // 设置圆点是否空心
            set1.setDrawCircleHole(false);
            // 设置字体大小
            set1.setValueTextSize(9f);
            // 字体是否显示
            set1.setDrawValues(false);
            // 是否填充颜色
//            set1.setDrawFilled(true);
//            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//            set1.setFormSize(15.f);

            set2.setColor(getResources().getColor(R.color.color_13c0c9));
            set2.setLineWidth(1.5f);
            set2.setCircleColor(getResources().getColor(R.color.color_13c0c9));
            set2.setCircleRadius(2f);
            set2.setDrawCircleHole(false);
            set2.setValueTextSize(9f);
            set2.setDrawValues(false);
//            set2.setDrawFilled(true);
            set2.setHighLightColor(getResources().getColor(R.color.color_0090ff));
            set2.setHighlightLineWidth(1f);
            set2.setDrawHorizontalHighlightIndicator(false);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_orange_linechart);
                set1.setFillDrawable(drawable);
                Drawable drawable2 = ContextCompat.getDrawable(this, R.drawable.fade_blue_linechart);
                set2.setFillDrawable(drawable2);
            } else {
                set1.setFillColor(Color.BLACK);
                set2.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            dataSets.add(set2);
            for (ILineDataSet iSet : dataSets) {
                LineDataSet set = (LineDataSet) iSet;
                set.setDrawFilled(true);
            }

            LineData lineData = new LineData(dataSets);
            mChart.setData(lineData);
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
        // 修改视口的所有方法需要在 为Chart 设置数据之后 调用 。
        // 设定x轴最大可见区域范围的大小
        mChart.setVisibleXRangeMaximum(30);
        // 设定x轴最大可见区域范围的大小(限制无限放大)
        mChart.setVisibleXRangeMinimum(5);
    }

    @Override
    public void onGetZhanJiFilterSuccess() {
        mFilter = ZhanJiFilterUtil.getFilterList();
        dismissProgressDialog();
    }

    @Override
    public void onGetZhanJiFilterFail() {
        dismissProgressDialog();
    }

    @Override
    public void onItemClick(FilterItem item, int position) {
        deptName = item.name;
        salesmanId = item.idNd;
        userType = item.nameNd;
        if ("ALL".equals(item.id)) {
            deptId = mUserInfo.getDeptId();
        } else {
            deptId = item.id;
        }
        tvTitle.setText(deptName);
        mPresenter.getData();
        // 构造历史业绩查询条件
        xiaShuBean2 = new SubordinateListBean().new XiaShuBean();
        if (!TextUtils.isEmpty(salesmanId)) {
            xiaShuBean2.userId = salesmanId;
        } else {
            xiaShuBean2.userId = mUserInfo.getUserId();
        }
        xiaShuBean2.deptId = deptId;
        xiaShuBean2.deptName = deptName;
        xiaShuBean2.userType = userType;

        for (FilterItem filterItem : mFilter) {
            if (item.id.equals(filterItem.id) && item.idNd.equals(filterItem.idNd)) {
                filterItem.setSelect(true);
            } else {
                filterItem.setSelect(false);
            }
        }
    }

    private void setProgressData(ProgressBar pA, ProgressBar pB, int d1, int d2) {
        float percent = 0.7f;
        int max = Math.max(d1, d2);
        pA.setMax(max);
        pB.setMax(max);
        pA.setProgress((int) (percent * d1));
        pB.setProgress((int) (percent * d2));
    }
}
