package com.salesman.activity.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.common.BaseActivity;
import com.studio.jframework.utils.PackageUtils;

/**
 * 关于我们界面
 * Created by LiHuai on 2016/08/26.
 */
public class AboutUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_about_us);
    }

    @Override
    protected void initView() {
        super.initView();
        TextView tvTitle = findView(R.id.tv_top_title);
        tvTitle.setText(R.string.about_us);
        TextView tvBack = findView(R.id.tv_top_left);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUsActivity.this.finish();
            }
        });
        TextView tvVersionName = findView(R.id.tv_versionname);
        tvVersionName.setText("版本V" + PackageUtils.getVersionName(this));
    }
}
