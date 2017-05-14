package com.buxingzhe.pedestrian.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.User.MeFragment;
import com.buxingzhe.pedestrian.community.CommunityFragment;
import com.buxingzhe.pedestrian.found.FoundFragment;
import com.buxingzhe.pedestrian.listen.OnInteractionData;
import com.buxingzhe.pedestrian.run.RunFragment;
import com.buxingzhe.pedestrian.run.RunRunFragment;
import com.buxingzhe.pedestrian.walk.WalkedFragment;
import com.buxingzhe.pedestrian.widget.MWTTabBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, OnInteractionData {
    private MWTTabBar vTabbar;
    private MainTabBarAdapter adapter;


    private ViewPager mViewPager;

    private List<Fragment> fragments = new ArrayList<Fragment>();
    private MQJMainPagerAdapter pagerAdapter;
    private ImageView vRun;

    private RelativeLayout main_tab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        vTabbar = (MWTTabBar) findViewById(R.id.TabBar);
        vRun = (ImageView) findViewById(R.id.main_run);
        main_tab = (RelativeLayout) findViewById(R.id.main_tab);
        initTabbar();
        construct();
        pagerAdapter = new MQJMainPagerAdapter(MainActivity.this,getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(pagerAdapter.getCount());


    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle mBundle = getIntent().getExtras();
        if(mBundle!=null){
            if (getIntent().hasExtra("fragId")) {
                int id = mBundle.getInt("fragId");
                if(id==2){
                    main_tab.setVisibility(View.GONE);
                    vRun.setVisibility(View.GONE);
                    vRun.setImageResource(R.mipmap.ic_run_press);

                    adapter.switchView(2);
                    mViewPager.setCurrentItem(2,false);
                }


            }
        }
    }


    private void initTabbar(){
        adapter = new MainTabBarAdapter();
        vTabbar.onChangeViewListener(this);
        vTabbar.setAdapter(adapter);
        adapter.switchView(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void construct() {
        WalkedFragment mWalkFragment = new WalkedFragment();
        FoundFragment mFoundFragment = new FoundFragment();
        RunFragment mRunFragment = new RunFragment();
        CommunityFragment mCommunityFragment = new CommunityFragment();
        MeFragment mMeFragment = new MeFragment();

        mRunFragment.setOnInteractionData(this);

        fragments.add(mWalkFragment);
        fragments.add(mFoundFragment);
        fragments.add(mRunFragment);
        fragments.add(mCommunityFragment);
        fragments.add(mMeFragment);

    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        System.out.println("actId--onClick"+id);
        if (id == 2){
            main_tab.setVisibility(View.GONE);
            vRun.setVisibility(View.GONE);
            vRun.setImageResource(R.mipmap.ic_run_press);
        }else {
            vRun.setImageResource(R.mipmap.ic_run_nor);
        }
        adapter.switchView(id);
        mViewPager.setCurrentItem(id,false);

    }

    @Override
    public void onInteraction() {
        dealWithTab();
        fragments.set(2,new RunFragment());
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        dealWithTab();

    }

    private void dealWithTab() {
        adapter.switchView(0);
        mViewPager.setCurrentItem(0,false);
        main_tab.setVisibility(View.VISIBLE);
        vRun.setVisibility(View.VISIBLE);
        vRun.setImageResource(R.mipmap.ic_run_nor);
    }

}
