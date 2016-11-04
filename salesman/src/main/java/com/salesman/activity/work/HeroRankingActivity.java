package com.salesman.activity.work;

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
import com.salesman.fragment.HeroRankingListFragment;
import com.salesman.views.indicator.FixedIndicatorView;
import com.salesman.views.indicator.IndicatorViewPager;
import com.salesman.views.indicator.slidebar.ColorBar;
import com.salesman.views.indicator.transition.OnTransitionTextListener;
import com.studio.jframework.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 英雄榜列表
 * Created by LiHuai on 2016/09/27.
 */
public class HeroRankingActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private FixedIndicatorView indicator;
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;

    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_hero_ranking);
    }

    @Override
    protected void initView() {
        super.initView();
        TextView tvLeft = findView(R.id.tv_top_left);
        tvLeft.setOnClickListener(this);
        TextView tvTitle = findView(R.id.tv_top_title);
        tvTitle.setText("英雄榜");

        mViewPager = findView(R.id.vp_hero_ranking);
        indicator = findView(R.id.fixed_indicator);

        int selectColor = getResources().getColor(R.color.color_0090ff);
        int unSelectColor = getResources().getColor(R.color.color_666666);
        indicator.setScrollBar(new ColorBar(getApplicationContext(), selectColor, 7) {

            @Override
            public int getWidth(int tabWidth) {
                return tabWidth - SizeUtils.convertDp2Px(HeroRankingActivity.this, 25);
            }
        });
        float selectSize = 14;
        indicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, selectSize));

        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        indicatorViewPager = new IndicatorViewPager(indicator, mViewPager);
        inflate = LayoutInflater.from(getApplicationContext());
    }

    @Override
    protected void initData() {
        super.initData();
        mFragments.add(getFragment(""));
        mFragments.add(getFragment("yesterday"));
        mFragments.add(getFragment("month"));
        mFragments.add(getFragment("lastMonth"));
        indicatorViewPager.setAdapter(new HeroIndicatorAdapter(getSupportFragmentManager(), mFragments));
    }

    private Fragment getFragment(String timeType) {
        HeroRankingListFragment fragment = new HeroRankingListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("timeType", timeType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
        }
    }

    private class HeroIndicatorAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private List<Fragment> mData;

        public HeroIndicatorAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public HeroIndicatorAdapter(FragmentManager fragmentManager, List<Fragment> mData) {
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
                    tvTab.setText("今日榜单");
                    break;
                case 1:
                    tvTab.setText("昨日榜单");
                    break;
                case 2:
                    tvTab.setText("本月榜单");
                    break;
                case 3:
                    tvTab.setText("上月榜单");
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
