package com.buxingzhe.pedestrian.found;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackie on 2017/2/9.
 */

public class ActivitySearchFound extends BaseActivity implements View.OnClickListener {
    private AppBarLayout vAppbar;
    private Toolbar vToolbar;
    private ViewPager viewPager;
    private TabLayout tab_FindFragment_title;                            //定义TabLayout
    private ViewPager vp_FindFragment_pager;                             //定义viewPager
    private FragmentPagerAdapter fAdapter;                               //定义adapter
    List<Fragment> list_fragment;                                //定义要装fragment的列表

    List<String> list_title;                                     //tab名称列表
    EditText vSearchContent;
    TextView vSearchBack;
    LinearLayout vSearchBar;
    private WalkCategoryFragment mSearchFragment;
    FrameLayout vSearchContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_found);
        findId();
        setOnClick();
        initFragment();
        initSearchFragment();
    }
    private void setOnClick() {
        vSearchBack.setOnClickListener(this);
        vSearchContent.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()){
                        goSearch();
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );
                    }
                    return true;
                }
                return false;
            }
        });
    }
    private void goSearch(){
        vp_FindFragment_pager.setVisibility(View.GONE);
        vToolbar.setVisibility(View.GONE);
        tab_FindFragment_title.setVisibility(View.GONE);
        vSearchContainer.setVisibility(View.VISIBLE);

        AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) vAppbar.getChildAt(0).getLayoutParams();
        mParams.setScrollFlags(0);  //mParams.setScrollFlags(0);的时候AppBarLayout下的toolbar就不会随着滚动条折叠 mParams.setScrollFlags(5);的时候AppBarLayout下的toolbar会随着滚动条折叠




    }
    private void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, fragment);
        transaction.commit();
    }
    private void findId() {
        vAppbar = (AppBarLayout)findViewById(R.id.appbar);
        vToolbar = (Toolbar) findViewById(R.id.toolbar);
        vSearchBar = (LinearLayout) findViewById(R.id.search_bar);
        setSupportActionBar(vToolbar);

        tab_FindFragment_title = (TabLayout)findViewById(R.id.tab_FindFragment_title);
        vp_FindFragment_pager = (ViewPager)findViewById(R.id.vp_FindFragment_pager);

        vSearchContent = (EditText) findViewById(R.id.et_search);
        vSearchBack = (TextView) findViewById(R.id.search_back);

        vSearchContainer = (FrameLayout) findViewById(R.id.fragment_container);
    }
    private void initFragment() {
        list_fragment = new ArrayList<Fragment>();
        for (int i=0;i<10;i++){
            WalkCategoryFragment fragment = new WalkCategoryFragment();
            list_fragment.add(fragment);
        }

        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        list_title = new ArrayList<>();
        for (int i=0;i<5;i++){
            list_title.add("美食街"+i);
            list_title.add("山城步行"+i);
        }
        //设置TabLayout的模式
        tab_FindFragment_title.setTabMode(TabLayout.MODE_SCROLLABLE);
        //为TabLayout添加tab名称
        for (int i=0;i<10;i++){
            tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(i)));
        }
        fAdapter = new FindTabAdapter(this.getSupportFragmentManager(),list_fragment,list_title);

        //viewpager加载adapter
        vp_FindFragment_pager.setAdapter(fAdapter);
        tab_FindFragment_title.setupWithViewPager(vp_FindFragment_pager);
    }
    private void initSearchFragment() {
        mSearchFragment = new WalkCategoryFragment();
        addFragment(mSearchFragment);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_back:
                finish();
                break;
        }
    }
}
