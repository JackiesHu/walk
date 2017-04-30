package com.buxingzhe.pedestrian.found;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.bean.RequestResultInfo;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.found.bean.PageContent;
import com.buxingzhe.pedestrian.found.bean.WalkRecord;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 *
 * Created by jackie on 2017/2/11.
 */

public class WalkCategoryFragment extends Fragment {
    private PullToRefreshListView mListView;
    private List<WalkRecord> walkRecords = new ArrayList<>();
    private WalkCategoryAdapter adapter;
    private int pageNo = 1;
    private String key;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.walk_category_fragment, container,false);
        findViewId(view);
        setRecycler();
        onClick();
        return view;
    }

    private void setRecycler() {
        adapter = new WalkCategoryAdapter(getContext(),walkRecords, R.layout.walk_category_item);
        mListView.setAdapter(adapter);

    }

    public void setData(List<WalkRecord> walkRecordses, String key){
        this.key = key;
        this.walkRecords.clear();
        this.walkRecords.addAll(walkRecordses);
        adapter.notifyDataSetChanged();
        pageNo = 1;
    }

    public void findViewId(View view){
        mListView = (PullToRefreshListView) view.findViewById(R.id.mListView);

        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    private void loadData() {
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("title", key);
        paramsMap.put("userId", GlobalParams.USER_ID);
        paramsMap.put("pageNo",String.valueOf(++pageNo));
        paramsMap.put("pageSize ","20");

        Subscriber mSubscriber = new Subscriber<String>(){

            @Override
            public void onCompleted() {
                stopRefreshAnimation();
            }

            @Override
            public void onError(Throwable e) {
                stopRefreshAnimation();
            }

            @Override
            public void onNext(final String jsonStr) {
                // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                RequestResultInfo resultInfo = JSON.parseObject(jsonStr, RequestResultInfo.class);
                if ("0".equals(resultInfo.getCode())) {
                    Object o = resultInfo.getContent();
                    if (o != null) {
                        PageContent content = JSON.parseObject(o.toString(),PageContent.class);
                        walkRecords.addAll(JSON.parseArray(content.getList().toString(), WalkRecord.class));
                        adapter.notifyDataSetChanged();
                    }
                }
                stopRefreshAnimation();

            }
        };

        NetRequestManager.getInstance().queryWalkRecordByTitle(paramsMap,mSubscriber);
    }

    private void stopRefreshAnimation(){
        if (mListView != null){
            mListView.onRefreshComplete();
        }
    }

    private void onClick() {
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),WalkDetailsActivity.class);
                intent.putExtra("locationData",walkRecords.get(position));
                intent.putExtra("myLocation",FoundFragment.ll);
                startActivity(intent);
            }
        });
    }
}
