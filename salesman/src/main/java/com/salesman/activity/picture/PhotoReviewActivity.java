package com.salesman.activity.picture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.WindowManager;

import com.salesman.R;
import com.salesman.common.BaseActivity;
import com.salesman.fragment.ImageDetailFragment;
import com.studio.jframework.widget.pager.HackyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiHuai
 * 九宫图片查看器
 */
public class PhotoReviewActivity extends BaseActivity {

    public static final String TAG = PhotoReviewActivity.class.getSimpleName();

    private HackyViewPager mViewPager;

    private int mPosition;
    private ArrayList<String> mImgs;
    private List<Fragment> mFragmentList;

//    private QiniuUtils mQiniuTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_review);
        initViews();
        initDatas();
        bindEvent();
    }

    protected void initViews() {
        mViewPager = (HackyViewPager) findViewById(R.id.photo_view_pager);
    }

    protected void initDatas() {
//        mQiniuTools = QiniuUtils.getInstance(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mImgs = bundle.getStringArrayList("Imgs");
        if (mImgs == null) {
            return;
        }
        mPosition = bundle.getInt("Position");
        mFragmentList = new ArrayList<>();

        for (int i = 0; i < mImgs.size(); i++) {
            Fragment fragment = new ImageDetailFragment();
            Bundle args = new Bundle();
//            args.putString("url", mQiniuTools.getScaleByWidth(mImgs.get(i).split(",")[0], mQiniuTools.getScreenWidth() / 2));
            args.putString("url", mImgs.get(i));
            fragment.setArguments(args);
            mFragmentList.add(fragment);
        }
        mViewPager.setAdapter(new PhotoPagerAdapter(getSupportFragmentManager(), mFragmentList));
        mViewPager.setCurrentItem(mPosition);
    }

    protected void bindEvent() {

    }

    private class PhotoPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragments;

        public PhotoPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
