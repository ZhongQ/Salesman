package com.salesman.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.salesman.R;
import com.salesman.application.SalesManApplication;
import com.salesman.utils.UserConfigPreference;
import com.salesman.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import static com.salesman.R.drawable.locality_life_focus;
import static com.salesman.R.drawable.locality_life_normal;

/**
 * 引导界面
 * Created by LiHuai on 2016/1/22.
 */
public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
    private ViewPager mViewPager;
    private LinearLayout mDotsLayout;
    private List<View> mPagerViews;
    private List<View> mViews;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mUserConfig.saveIsFirst(false).apply();

        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.guide_viewpager);
        mDotsLayout = (LinearLayout) findViewById(R.id.dots_layout);

        inflater = LayoutInflater.from(this);

        mPagerViews = new ArrayList<View>();
        View page1 = inflater.inflate(R.layout.view_guidepage, null);
        ViewUtil.scaleContentView((ViewGroup) page1);
        mPagerViews.add(page1);
        View page2 = inflater.inflate(R.layout.view_guidepage2, null);
        ViewUtil.scaleContentView((ViewGroup) page2);
        mPagerViews.add(page2);
        View page3 = inflater.inflate(R.layout.view_guidepage3, null);
        ViewUtil.scaleContentView((ViewGroup) page3);
        mPagerViews.add(page3);

        initdots();

        mViewPager.setAdapter(new GuidePageAdapter());
        mViewPager.setOnPageChangeListener(this);
    }

    private void initdots() {
        mDotsLayout.removeAllViews();
        mViews = new ArrayList<View>();
        mViews.clear();
        for (int i = 0; i < mPagerViews.size(); i++) {
            View view = new View(this);
            if (i == 0) {
                view.setBackgroundResource(locality_life_focus);
            } else {
                view.setBackgroundResource(locality_life_normal);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.setMargins(20, 0, 20, 0);
            mDotsLayout.addView(view, layoutParams);
            mViews.add(view);
        }
    }

    class GuidePageAdapter extends PagerAdapter {

        // 销毁position位置的界面
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mPagerViews.get(position));
        }

        // 获取当前窗体界面数
        @Override
        public int getCount() {
            return mPagerViews == null ? 0 : mPagerViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mPagerViews.get(position);
            container.addView(view);


            // 页卡2内的按钮事件
            if (position == 2) {
                ImageView ivExperience = (ImageView) view.findViewById(R.id.iv_experience);
                ivExperience.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(GuideActivity.this, StartActivity.class));
                        GuideActivity.this.finish();
                    }
                });
            }

            return view;
        }

        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View v, Object arg1) {
            return v == arg1;
        }


        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mViews.size(); i++) {
            if (i == position) {
                mViews.get(position).setBackgroundResource(locality_life_focus);
            } else {
                mViews.get(i).setBackgroundResource(locality_life_normal);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
    }
}
