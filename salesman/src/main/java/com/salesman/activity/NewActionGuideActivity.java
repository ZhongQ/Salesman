package com.salesman.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.salesman.R;
import com.salesman.application.SalesManApplication;
import com.salesman.fragment.guide.ClientGuideFragment;
import com.salesman.fragment.guide.VisitGuideFragment;
import com.salesman.fragment.guide.WorkGuideFragment;
import com.salesman.utils.UserConfigPreference;

/**
 * 新功能引导页
 * Created by LiHuai on 2016/08/23.
 */
public class NewActionGuideActivity extends FragmentActivity {
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
    private FrameLayout layoutContain;
    private FragmentManager manager;
    private String come_from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_action_guide);

        initView();
        initData();
    }

    private void initView() {
        layoutContain = (FrameLayout) findViewById(R.id.fragment_container);
        manager = getFragmentManager();
        come_from = getIntent().getStringExtra("come_from");
    }

    private void initData() {
        FragmentTransaction transaction = manager.beginTransaction();
        if (TextUtils.isEmpty(come_from)) {
            return;
        }
        savePageName();
        switch (come_from) {
            case "WorkFragment":
                WorkGuideFragment workGuideFragment = new WorkGuideFragment();
                transaction.add(R.id.fragment_container, workGuideFragment);
                break;
            case "ClientFragment":
                ClientGuideFragment clientGuideFragment = new ClientGuideFragment();
                transaction.add(R.id.fragment_container, clientGuideFragment);
                break;
            case "VisitListActivity":
                VisitGuideFragment visitGuideFragment = new VisitGuideFragment();
                transaction.add(R.id.fragment_container, visitGuideFragment);
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
