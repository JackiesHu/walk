package com.buxingzhe.pedestrian.found.tag;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.bean.HotUserTag;
import com.buxingzhe.pedestrian.common.SwipeRefreshProperty;
import com.buxingzhe.pedestrian.listen.SwpipeListViewOnScrollListener;
import com.buxingzhe.pedestrian.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * 评价
 * Created by QJ on 2016/6/30.
 */
public class TagAddActivity extends BaseActivity{
    private SwipeRefreshLayout mRefresh;
    private ListView mListView;
    private int currentIndex = 1;
    private final static int pageSize = 20;
    private AddTagAdapter addTagAdapter;
    private String checkTag = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtag);
        findViewId();
        titleBar();
        loadData();
        addListen();

    }
    public void findViewId() {
        vTitleBar = (TitleBarView) findViewById(R.id.tag_title_bar);

        Timer timer = new Timer();

        mRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mListView = (ListView) findViewById(R.id.myHistoryActListView);
        SwipeRefreshProperty.getInstall().setSwipeInfo(getBaseContext(), mRefresh);
        SwpipeListViewOnScrollListener scrollListener = new SwpipeListViewOnScrollListener(mRefresh);
        mRefresh.setEnabled(false);
        mListView.setOnScrollListener(scrollListener);
    }

    public void loadData() {
        addTagAdapter = new AddTagAdapter(mContext);
        mListView.setAdapter(addTagAdapter);
        getHotTag(true, checkTag);
    }
    public void addListen() {
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据操作
                currentIndex = 1;
                getHotTag(true, checkTag);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* HotUserTag hotUserTag = (HotUserTag) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(hotUserTag.tag)) {
                    et_tag.setText(hotUserTag.tag);
                }*/
            }
        });
    }

    public void titleBar() {
        setTitle("添加标签");
    }

    @Override
    public void onRightListener(View v) {
        super.onRightListener(v);
        addTag();
    }

    private void addTag() {

    }


    private void getHotTag(final boolean isClean,String checkTag){
        List<HotUserTag> hotUserTags = new ArrayList<>();
        for (int i=0;i<20;i++){
            HotUserTag tag = new HotUserTag();
            tag.tag = "表情"+i;
            tag.count = i;
            hotUserTags.add(tag);
        }
        if (addTagAdapter != null){
            addTagAdapter.setHotUserTagDatas(false,hotUserTags);
        }
    }

    private void stopRefreshAnimation(){

    }
}
