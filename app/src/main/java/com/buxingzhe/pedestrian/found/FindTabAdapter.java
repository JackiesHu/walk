package com.buxingzhe.pedestrian.found;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by jackie on 2017/2/11.
 */

public class FindTabAdapter extends FragmentPagerAdapter {
    List<Fragment> list_fragment;                         //fragment列表
    private List<String> list_Title;                              //tab名的列表
    FindTabAdapter(FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title) {
        super(fm);
        this.list_fragment = list_fragment;
        this.list_Title = list_Title;
    }
    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }
    @Override
    public int getCount() {
        if (list_fragment != null)
        return list_fragment.size();
        return 0;
    }
    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        if (list_Title != null){
            return list_Title.get(position);
        }
        return "";
    }
}
