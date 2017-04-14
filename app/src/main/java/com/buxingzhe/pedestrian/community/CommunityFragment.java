package com.buxingzhe.pedestrian.community;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseFragment;
import com.buxingzhe.pedestrian.community.community.CommActFragment;
import com.buxingzhe.pedestrian.community.community.CommCircleFragment;
import com.buxingzhe.pedestrian.utils.SystemUtils;

import java.util.ArrayList;

/**
 * Created by quanjing on 2017/2/23.
 */
public class CommunityFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout vCommunity;
    private RelativeLayout vCircle;
    private ImageView vbg2,vbg1;
    private TextView vTv1,vTv2;
    private ViewPager vCityViewPager;
    private int fragmentPostion;
    private ArrayList<android.support.v4.app.Fragment> fragments = new ArrayList<android.support.v4.app.Fragment>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, null);
        mContext = getContext();
        findId(view);
        setViewPager();
        onClick();

        return view;
    }
    private void findId(View view){
        vCommunity = (RelativeLayout) view.findViewById(R.id.rl_community);
        vCircle = (RelativeLayout) view.findViewById(R.id.rl_circle);
        vbg2 = (ImageView)view.findViewById(R.id.community_bg2);
        vbg1 = (ImageView)view.findViewById(R.id.community_bg1);
        vTv2 = (TextView) view.findViewById(R.id.community_tv2);
        vTv1 = (TextView) view.findViewById(R.id.community_tv1);

        vCityViewPager = (ViewPager) view.findViewById(R.id.community_viewPager);
        hideLeftIco();
    };
    private void onClick() {
        vCommunity.setOnClickListener(this);
        vCircle.setOnClickListener(this);
        vCityViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                fragmentPostion = position;
                if (fragmentPostion == 0) {
                    changeCommunity();
                } else {
                    changeCircle();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu.clear();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rl_community:
                changeCommunity();
                break;
            case R.id.rl_circle:
                changeCircle();
                break;
        }
    }
    private void setViewPager(){
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getFragmentManager());
        CommActFragment commActFragment = new CommActFragment();
        fragments.add(commActFragment);
        CommCircleFragment commCircle = new CommCircleFragment();
        fragments.add(commCircle);
        vCityViewPager.setAdapter(adapter);
    }
    private void changeCommunity(){
        switchTitle(vTv1,vbg1,vTv2,vbg2);
        vCityViewPager.setCurrentItem(0);
    }

    private void changeCircle(){
        switchTitle(vTv2,vbg2,vTv1,vbg1);
        vCityViewPager.setCurrentItem(1);
    }
    private void switchTitle(TextView tv1,ImageView iv1,TextView tv2,ImageView iv2){
        if (iv1 != null){
            iv1.setVisibility(View.VISIBLE);
        }
        if (iv2 != null){
            iv2.setVisibility(View.INVISIBLE);
        }
        if (tv1 != null){
            tv1.setTextColor(SystemUtils.getByColor(R.color.colorPrimaryDark));
            tv1.setVisibility(View.VISIBLE);
        }
        if (tv2 != null){
            tv2.setTextColor(SystemUtils.getByColor(R.color.white));
            tv2.setVisibility(View.VISIBLE);
        }
    }
    private  class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
        public FragmentPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            if (fragments == null){
                return 0;
            }
            return fragments.size();
        }

        @Override
        public android.support.v4.app.Fragment getItem(int index) {
            return fragments.get(index);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (getFragmentManager() != null && object instanceof Fragment){
                FragmentManager manager = ((Fragment) object).getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove((Fragment) object);
                trans.commitAllowingStateLoss();
            }
            super.destroyItem(container, position, object);
        }
    }
}
