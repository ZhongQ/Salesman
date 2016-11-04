package com.salesman.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.salesman.R;
import com.salesman.adapter.MainPagerAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseActivity;
import com.salesman.common.EventBusConfig;
import com.salesman.fragment.ClientFragment;
import com.salesman.fragment.HomeFragment3;
import com.salesman.fragment.PersonalFragment;
import com.salesman.fragment.ZhanJiFragmentNew;
import com.salesman.umeng.UmengAnalyticsUtil;
import com.salesman.utils.AlarmUtil;
import com.salesman.utils.AppLogUtil;
import com.salesman.utils.DateUtils;
import com.salesman.utils.SalesmanLineUtil;
import com.salesman.utils.TrackUtil;
import com.salesman.utils.UserConfigPreference;
import com.salesman.utils.UserInfoPreference;
import com.salesman.utils.UserInfoUtil;
import com.salesman.utils.ZhanJiFilterUtil;
import com.salesman.verson.UpdateManager;
import com.studio.jframework.utils.LogUtils;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.tag.TagManager;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 主界面,这里承载各个Fragment的显示，以及相关控制
 * Created by LiHuai on 2016/1/21.
 */
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();

    private ViewPager mViewPager;
    private RadioGroup radioGroup;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    // 友盟推送
    private PushAgent mPushAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_main);

        signinControl();
        serviceControl();
        // 检测升级
        updateSalesmanApp();
        // 获取业务员与线路
        new SalesmanLineUtil().getSalesmanAndLineData(true);
        // 战绩筛选条件
        new ZhanJiFilterUtil().getZhanJiFilterData();
        // 上传离线足迹
        uploadOffLineTrack();
        // 上传日志
        AppLogUtil.uploadAppLog();
        EventBus.getDefault().register(this);

        initUmengPush();
    }

    @Override
    protected void initData() {
//        HomeFragment homeFragment = new HomeFragment();
//        ManageFragment manageFragment = new ManageFragment();
//        HomeFragment11 homeFragment11 = new HomeFragment11();           // 新首页界面，V1.1.0
        HomeFragment3 homeFragment3 = new HomeFragment3();                // 新首页，V1.6.0
        ClientFragment clientFragment = new ClientFragment();
        PersonalFragment personalFragment = new PersonalFragment();
        ZhanJiFragmentNew zhanJiFragmentNew = new ZhanJiFragmentNew();  // 战绩界面V2.0.0

        mFragments.add(homeFragment3);
        if (UserInfoUtil.isAdministrator()) {
//            WorkFragment workFragment = new WorkFragment();                 // 工作界面，V1.5.0
//            mFragments.add(workFragment);
        } else {
//            ZhanJiFragment zhanJiFragment = new ZhanJiFragment();           // 战绩界面
        }
        mFragments.add(zhanJiFragmentNew);
        mFragments.add(clientFragment);
        mFragments.add(personalFragment);

        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), mFragments));
    }

    @Override
    protected void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
        mViewPager = (ViewPager) findViewById(R.id.nosliding_viewpager);
        mViewPager.addOnPageChangeListener(this);
        // 设置ViewPager是否可滑
//        mViewPager.setCanSliding(true);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        findViewById(R.id.main_rb1).setOnClickListener(this);
        findViewById(R.id.main_rb2).setOnClickListener(this);
        findViewById(R.id.main_rb3).setOnClickListener(this);
        findViewById(R.id.main_rb4).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume");
        UmengAnalyticsUtil.umengOnResume(this);
        signinControl();
        mUserConfig.saveHandExit(false).apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengAnalyticsUtil.umengOnPause(this);
    }

    /**
     * 签到次数控制
     */
    private void signinControl() {
//        if (!DateUtils.isSameDate()) {
//            // 将上下班签到置为false
//            mUserConfig.saveGoToWork(false).saveGetOffWork(false).apply();
//        }
        if (mUserConfig.getGotoWork() && !mUserConfig.getGetOffWork()) {
            mUserConfig.saveDate(DateUtils.getYMD(System.currentTimeMillis()));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                radioGroup.check(R.id.main_rb1);
                break;
            case 1:
                radioGroup.check(R.id.main_rb2);
                break;
            case 2:
                radioGroup.check(R.id.main_rb3);
                break;
            case 3:
                radioGroup.check(R.id.main_rb4);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onEventMainThread(String action) {
        if (EventBusConfig.APP_EXIT.equals(action)) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(home);
    }

    /**
     * 更新app
     */
    private void updateSalesmanApp() {
        UpdateManager versionManager = new UpdateManager(this);
        versionManager.checkUpdate();
    }

    /**
     * 服务控制
     */
    private void serviceControl() {
        if (mUserConfig.getGotoWork() && !mUserConfig.getGetOffWork()) {
            AlarmUtil.startServiceAlarm();
        } else {
            AlarmUtil.cancelServiceAlarm();
        }
    }

    /**
     * 上传离线足迹
     */
    private void uploadOffLineTrack() {
        TrackUtil.uploadTrack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_rb1:
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.main_rb2:
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.main_rb3:
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.main_rb4:
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    /**
     * 初始化友盟推送
     */
    private void initUmengPush() {
        // 友盟注册id
        LogUtils.d(TAG, UmengRegistrar.getRegistrationId(this));
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable(new IUmengRegisterCallback() {
            @Override
            public void onRegistered(String s) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        LogUtils.d(TAG, "s");
                        mPushAgent.setAlias(mUserInfo.getUserId(), "userId");
                        mPushAgent.setExclusiveAlias(mUserInfo.getUserId(), "userId");
                    }
                });
            }
        });
        new AddTagTask().execute();
    }

    /**
     * 设置用户标签(tag)
     */
    class AddTagTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                TagManager.Result result = mPushAgent.getTagManager().add(mUserInfo.getDeptId(), mUserInfo.getUserType(), "android");
//                mPushAgent.getTagManager().add(); // 添加标签
//                mPushAgent.getTagManager().delete();// 删除标签
//                mPushAgent.getTagManager().update();// 更新标签
//                mPushAgent.getTagManager().reset();// 清除所有标签
//                mPushAgent.getTagManager().list();// 获取服务器端的所有标签
                Log.d(TAG, result.toString());
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Fail";
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }
}
