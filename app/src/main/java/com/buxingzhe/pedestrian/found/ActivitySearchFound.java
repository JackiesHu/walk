package com.buxingzhe.pedestrian.found;

import android.content.Context;
import android.graphics.Color;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.bean.RequestResultInfo;
import com.buxingzhe.pedestrian.found.bean.PageContent;
import com.buxingzhe.pedestrian.found.bean.Tag;
import com.buxingzhe.pedestrian.found.bean.WalkRecord;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by jackie on 2017/2/9.
 */

public class ActivitySearchFound extends BaseActivity implements View.OnClickListener {
    private AppBarLayout vAppbar;
    private Toolbar vToolbar;
    private TabLayout tab_FindFragment_title;                            //定义TabLayout
    private ViewPager vp_FindFragment_pager;                             //定义viewPager
    List<WalkCategoryFragment> list_fragment;                                //定义要装fragment的列表

    List<String> list_title;                                     //tab名称列表
    EditText vSearchContent;
    TextView vSearchBack;
    LinearLayout vSearchBar;
    FrameLayout vSearchContainer;
    private List<Tag> tags;
    private List walkRecords;
    private WalkCategoryFragment mSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_found);
        findId();
        setOnClick();
        initSearchFragment();
        loadTag();
    }

    private void loadTag() {
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("userId", baseApp.getUserId());
        paramsMap.put("code","1");

        Subscriber mSubscriber = new Subscriber<String>(){

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final String jsonStr) {
                // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                RequestResultInfo resultInfo = JSON.parseObject(jsonStr, RequestResultInfo.class);
                if ("0".equals(resultInfo.getCode())) {
                    Object o = resultInfo.getContent();
                    if (o != null) {
                        tags = JSON.parseArray(o.toString(),Tag.class);
                        initFragment();
                    }
                }
            }
        };

        NetRequestManager.getInstance().queryTag(paramsMap,mSubscriber);
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
        loadData(vSearchContent.getText().toString(), 1);
        AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) vAppbar.getChildAt(0).getLayoutParams();
        mParams.setScrollFlags(0);  //mParams.setScrollFlags(0);的时候AppBarLayout下的toolbar就不会随着滚动条折叠 mParams.setScrollFlags(5);的时候AppBarLayout下的toolbar会随着滚动条折叠

    }

    private void loadData(final String key, int pageNo) {
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("title", key);
        paramsMap.put("userId", baseApp.getUserId());
        paramsMap.put("pageNo",String.valueOf(pageNo));
        paramsMap.put("pageSize ","20");

        Subscriber mSubscriber = new Subscriber<String>(){

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final String jsonStr) {
                // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                RequestResultInfo resultInfo = JSON.parseObject(jsonStr, RequestResultInfo.class);
                if ("0".equals(resultInfo.getCode())) {
                    Object o = resultInfo.getContent();
                    if (o != null) {
                        PageContent content = JSON.parseObject(o.toString(),PageContent.class);
                        walkRecords = JSON.parseArray(content.getList().toString(), WalkRecord.class);
                        mSearchFragment.setData(walkRecords,key);
                    }
                }
            }
        };

        NetRequestManager.getInstance().queryWalkRecordByTitle(paramsMap,mSubscriber);
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
        list_fragment = new ArrayList<>();
        for (int i=0;i<tags.size();i++){
            WalkCategoryFragment fragment = new WalkCategoryFragment();
            list_fragment.add(fragment);
        }

        //设置TabLayout的模式
        tab_FindFragment_title.setTabMode(TabLayout.MODE_SCROLLABLE);
        //为TabLayout添加tab名称
        for (int i=0;i<tags.size();i++){
            tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(tags.get(i).getName()));
        }
        tab_FindFragment_title.setSelectedTabIndicatorColor(Color.RED);
        FragmentPagerAdapter fAdapter = new FindTabAdapter(this.getSupportFragmentManager(), list_fragment, tags);

        //viewpager加载adapter
        vp_FindFragment_pager.setAdapter(fAdapter);
        tab_FindFragment_title.setupWithViewPager(vp_FindFragment_pager);
    }
    private void initSearchFragment() {
        mSearchFragment = new WalkCategoryFragment();
        addFragment(mSearchFragment);
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
