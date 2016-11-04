package com.salesman.activity.guide;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.salesman.R;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.fragment.guide.ClientGuideFragment;
import com.salesman.fragment.guide.ClientGuideFragment2;
import com.salesman.fragment.guide.ClientInfoGuideFragment;
import com.salesman.fragment.guide.XiaShuGuideFragment;
import com.salesman.fragment.guide.YeJiGuideFragment;
import com.salesman.fragment.guide.YeJiTrednGuideFragment1;
import com.salesman.fragment.guide.YeJiTrednGuideFragment2;
import com.salesman.utils.UserConfigPreference;
import com.salesman.utils.UserInfoUtil;

/**
 * 新功能引导页
 * Created by LiHuai on 2016/08/23.
 */
public class NewActionGuideActivity extends BaseActivity {
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
    private FrameLayout layoutContain;
    private FragmentManager manager;
    private String come_from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_new_action_guide);
    }

    @Override
    protected void initView() {
        layoutContain = (FrameLayout) findViewById(R.id.fragment_container);
        manager = getFragmentManager();
        come_from = getIntent().getStringExtra("come_from");
    }

    @Override
    protected void initData() {
        FragmentTransaction transaction = manager.beginTransaction();
        if (TextUtils.isEmpty(come_from)) {
            return;
        }
        savePageName();
        switch (come_from) {
            case "ClientFragment":
                if (UserInfoUtil.isAdministrator()) {
                    ClientGuideFragment clientGuideFragment = new ClientGuideFragment();
                    transaction.add(R.id.fragment_container, clientGuideFragment);
                } else {
                    ClientGuideFragment2 clientGuideFragment2 = new ClientGuideFragment2();
                    transaction.add(R.id.fragment_container, clientGuideFragment2);
                }
                break;
            case "ZhanJiFragmentNew":// 战绩首页
                YeJiGuideFragment yeJiGuideFragment = new YeJiGuideFragment();
                transaction.add(R.id.fragment_container, yeJiGuideFragment);
                break;
            case "MySubordinateActivity11":
                XiaShuGuideFragment xiaShuGuideFragment = new XiaShuGuideFragment();
                transaction.add(R.id.fragment_container, xiaShuGuideFragment);
                break;
            case "YeJiTrendActivity":// 业绩趋势
                if (UserInfoUtil.isAdministrator()) {
                    YeJiTrednGuideFragment1 yeJiTrednGuideFragment1 = new YeJiTrednGuideFragment1();
                    transaction.add(R.id.fragment_container, yeJiTrednGuideFragment1);
                } else {
                    YeJiTrednGuideFragment2 yeJiTrednGuideFragment2 = new YeJiTrednGuideFragment2();
                    transaction.add(R.id.fragment_container, yeJiTrednGuideFragment2);
                }
                break;
            case "ClientInfoActivity":
                ClientInfoGuideFragment clientInfoGuideFragment = new ClientInfoGuideFragment();
                transaction.add(R.id.fragment_container, clientInfoGuideFragment);
                break;
        }
        transaction.commit();
    }

    /**
     * 保存页面记录
     */
    private void savePageName() {
        String str = mUserConfig.getNewActionGuide();
        mUserConfig.saveNewActionGuide(str + come_from).apply();
    }

    @Override
    public void onBackPressed() {
    }
}
