package com.yoo.ymh.whoru.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoo on 2016-08-12.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter{
    private final List<Fragment> myFragments = new ArrayList<>();
    private Context context;

    public MainViewPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
    }
    public void addFragment(Fragment fragment)
    {
        myFragments.add(fragment);
    }
    @Override
    public Fragment getItem(int position) {
        return myFragments.get(position);
    }

    @Override
    public int getCount() {
        return myFragments.size();
    }

}
