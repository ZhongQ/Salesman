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
import com.salesman.entity.SubordinateListBean;
import com.salesman.fragment.DayYeJiListFragment;
import com.salesman.fragment.MonthYeJiListFragment;
import com.salesman.views.indicator.FixedIndicatorView;
import com.salesman.views.indicator.IndicatorViewPager;
import com.salesman.views.indicator.slidebar.ColorBar;
import com.salesman.views.indicator.transition.OnTransitionTextListener;
import com.studio.jframework.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史业绩界面
 * Created by LiHuai on 2016/09/28.
 */
public class YeJiActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private FixedIndicatorView indicator;
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;

    private List<Fragment> mFragments = new ArrayList<>();
    private SubordinateListBean.XiaShuBean xiaShuBean;

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
        tvTitle.setText("历史业绩");

        mViewPager = findView(R.id.vp_hero_ranking);
        indicator = findView(R.id.fixed_indicator);

        int selectColor = getResources().getColor(R.color.color_0090ff);
        int unSelectColor = getResources().getColor(R.color.color_666666);
        indicator.setScrollBar(new ColorBar(getApplicationContext(), selectColor, 7) {

            @Override
            public int getWidth(int tabWidth) {
                return tabWidth - SizeUtils.convertDp2Px(YeJiActivity.this, 50);
            }
        });
        float selectSize = 14;
        indicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, selectSize));

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        indicatorViewPager = new IndicatorViewPager(indicator, mViewPager);
        inflate = LayoutInflater.from(getApplicationContext());
    }

    @Override
    protected void initData() {
        super.initData();
        xiaShuBean = (SubordinateListBean.XiaShuBean) getIntent().getSerializableExtra("XiaShu");
        mFragments.add(getFragment(1));
        mFragments.add(getFragment(2));
        indicatorViewPager.setAdapter(new YeJiIndicatorAdapter(getSupportFragmentManager(), mFragments));
    }

    private Fragment getFragment(int type) {
        if (1 == type) {
            DayYeJiListFragment fragment = new DayYeJiListFragment();
            Bundle bundle = new Bundle();
            if (null != xiaShuBean) {
                bundle.putSerializable("XiaShu", xiaShuBean);
            }
            fragment.setArguments(bundle);
            return fragment;
        } else {
            MonthYeJiListFragment fragment = new MonthYeJiListFragment();
            Bundle bundle = new Bundle();
            if (null != xiaShuBean) {
                bundle.putSerializable("XiaShu", xiaShuBean);
            }
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
        }
    }

    private class YeJiIndicatorAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private List<Fragment> mData;

        public YeJiIndicatorAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public YeJiIndicatorAdapter(FragmentManager fragmentManager, List<Fragment> mData) {
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
                    tvTab.setText("日业绩");
                    break;
                case 1:
                    tvTab.setText("月业绩");
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
