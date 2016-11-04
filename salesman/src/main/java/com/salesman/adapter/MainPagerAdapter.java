package com.salesman.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * 主界面适配器
 * Created by LiHuai on 2016/1/25.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragList;

    public MainPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        mFragList = list;
    }

    @Override
    public int getCount() {
        if (mFragList == null) {
            return 0;
        } else {
            return mFragList.size();
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
    }
}
