package com.buxingzhe.pedestrian.activity;




import android.content.Context;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class MQJMainPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<Fragment> fragments = null;
    public MQJMainPagerAdapter(Context context, FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mContext = context;
        this.fragments = fragments;
    }
    @Override
    public int getCount() {
        return fragments.size();
    }
    @Override
    public Fragment getItem(int index) {
        return fragments.get(index);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
