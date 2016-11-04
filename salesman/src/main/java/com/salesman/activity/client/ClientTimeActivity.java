package com.salesman.activity.client;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.salesman.R;
import com.salesman.common.BaseActivity;
import com.salesman.utils.DateUtils;
import com.salesman.utils.ToastUtil;
import com.studio.jframework.utils.LogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 客户营业时间界面
 * Created by LiHuai on 2016/06/29.
 */
public class ClientTimeActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = ClientTimeActivity.class.getSimpleName();
    public static final int FLAG = 1007;
    private LinearLayout layOpen, layClose;
    private TextView tvOpen, tvClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_client_time);
    }

    @Override
    protected void initView() {
        TextView tvTitel = (TextView) findViewById(R.id.tv_top_title);
        tvTitel.setText(R.string.business_hours);
        TextView tvBack = (TextView) findViewById(R.id.tv_top_left);
        TextView tvRight = (TextView) findViewById(R.id.tv_top_right);
        tvRight.setText(R.string.save);
        tvRight.setVisibility(View.VISIBLE);
        tvOpen = (TextView) findViewById(R.id.tv_open_time);
        tvClose = (TextView) findViewById(R.id.tv_close_time);
        layOpen = (LinearLayout) findViewById(R.id.lay_open_store);
        layClose = (LinearLayout) findViewById(R.id.lay_close_store);

        String startTime = getIntent().getStringExtra("StartTime");
        String endTime = getIntent().getStringExtra("EndTime");
        if (TextUtils.isEmpty(startTime)) {
            tvOpen.setText(R.string.select_please);
        } else {
            tvOpen.setText(startTime);
        }
        if (TextUtils.isEmpty(endTime)) {
            tvClose.setText(R.string.select_please);
        } else {
            tvClose.setText(endTime);
        }

        tvBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        layOpen.setOnClickListener(this);
        layClose.setOnClickListener(this);
    }

    /**
     * 时间选择器
     */
    private void showTimePicker(final TextView textView, boolean bool) {
        Calendar c = Calendar.getInstance();
        LogUtils.d(TAG, String.valueOf(c.get(Calendar.HOUR)));
        LogUtils.d(TAG, String.valueOf(c.get(Calendar.MINUTE)));
        int hour = c.get(Calendar.HOUR);
        if (bool) {
            hour = 6;
        } else {
            hour = 23;
        }
        new TimePickerDialog(this, TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                textView.setText(DateUtils.fmtTimeToStr((hourOfDay + ":" + minute), "HH:mm"));
            }
        }, hour, 0, true).show();

    }

    private void saveMessage() {
        String openTime = tvOpen.getText().toString();
        String closeTime = tvClose.getText().toString();
        if (TextUtils.isEmpty(openTime) || getString(R.string.select_please).equals(openTime)) {
            ToastUtil.show(this, "请选择开店时间");
            return;
        }
        if (TextUtils.isEmpty(closeTime) || getString(R.string.select_please).equals(closeTime)) {
            ToastUtil.show(this, "请选择关店时间");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date openDate = sdf.parse(openTime);
            Date closeDate = sdf.parse(closeTime);
            if (closeDate.before(openDate)) {
                ToastUtil.show(this, "关店时间早于开店时间");
                return;
            }
            if (closeDate.equals(openDate)) {
                ToastUtil.show(this, "开店时间与关店时间相等");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            ToastUtil.show(this, "时间错误");
            return;
        }

        Intent intent = getIntent();
        intent.putExtra("StartTime", openTime);
        intent.putExtra("EndTime", closeTime);
        setResult(FLAG, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
            case R.id.tv_top_right:// 保存
                saveMessage();
                break;
            case R.id.lay_open_store:// 开始时间
                showTimePicker(tvOpen, true);
                break;
            case R.id.lay_close_store:// 结束时间
                showTimePicker(tvClose, false);
                break;

        }
    }
}
