package com.salesman.activity.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.common.BaseActivity;
import com.salesman.fragment.ClientListCheckFragment;
import com.salesman.umeng.UmengAnalyticsUtil;
import com.salesman.views.indicator.FixedIndicatorView;
import com.salesman.views.indicator.IndicatorViewPager;
import com.salesman.views.indicator.slidebar.ColorBar;
import com.salesman.views.indicator.transition.OnTransitionTextListener;
import com.studio.jframework.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户审核列表
 * Created by LiHuai on 2016/06/22.
 */
public class ClientCheckListActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater inflate;
    private ViewPager mViewPager;
    private IndicatorViewPager indicatorViewPager;
    private FixedIndicatorView indicator;

    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_client_check_list);
    }

    @Override
    protected void initView() {
        TextView tvLeft = (TextView) findViewById(R.id.tv_top_left);
        tvLeft.setOnClickListener(this);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTitle.setText("注册审核");

        mViewPager = (ViewPager) findViewById(R.id.vp_client);
        indicator = findView(R.id.fixed_indicator);

        int selectColor = getResources().getColor(R.color.color_0090ff);
        int unSelectColor = getResources().getColor(R.color.color_666666);
        indicator.setScrollBar(new ColorBar(getApplicationContext(), selectColor, 7) {
            @Override
            public int getWidth(int tabWidth) {
                return tabWidth - SizeUtils.convertDp2Px(ClientCheckListActivity.this, 70);
            }
        });
        float unSelectSize = 16;
        float selectSize = unSelectSize * 1.2f;
        indicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(unSelectSize, unSelectSize));
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        indicatorViewPager = new IndicatorViewPager(indicator, mViewPager);
        inflate = LayoutInflater.from(getApplicationContext());
    }

    @Override
    protected void initData() {
        mFragments.add(getFragment("2"));// 待审核
        mFragments.add(getFragment("3"));// 已拒绝
        indicatorViewPager.setAdapter(new MyPageIndicatorAdapter(getSupportFragmentManager(), mFragments));
//        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), mFragments));
    }

    private Fragment getFragment(String status) {
        ClientListCheckFragment fragment = new ClientListCheckFragment();
        Bundle bundle = new Bundle();
        bundle.putString("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengAnalyticsUtil.umengOnResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengAnalyticsUtil.umengOnPause(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
        }

    }

    private class MyPageIndicatorAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private List<Fragment> mData;

        public MyPageIndicatorAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public MyPageIndicatorAdapter(FragmentManager fragmentManager, List<Fragment> mData) {
            super(fragmentManager);
            this.mData = mData;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.tab_top, container, false);
            }
            TextView tvTab = (TextView) convertView;
            switch (position) {
                case 0:
                    tvTab.setText("待审核");
                    break;
                case 1:
                    tvTab.setText("已拒绝");
                    break;
            }
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            return mData.get(position);
        }
    }
}
