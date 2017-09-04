package com.buxingzhe.pedestrian.User;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.bean.activity.WalkActivitiesInfo;
import com.buxingzhe.pedestrian.community.community.CommActAdapter;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.listen.SwpipeListViewOnScrollListener;
import com.buxingzhe.pedestrian.utils.JsonParseUtil;
import com.buxingzhe.pedestrian.widget.TitleBarView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class MyRunTeamActivity extends BaseActivity {
    @BindView(R.id.mListViewR)
    PullToRefreshListView mListViewR;
    @BindView(R.id.swipeLayoutR)
    SwipeRefreshLayout swipeLayoutR;
    @BindView(R.id.emptyR)
    TextView emptyT;

    private CommActAdapter mAdapter;
    private int mPage = 1;
    private final static int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_run_team);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    private void loadData() {
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("userId", baseApp.getUserId());
        paramsMap.put("token", baseApp.getUserToken());
        paramsMap.put("publisher", baseApp.getUserId());
        paramsMap.put("pageNo",String.valueOf(mPage));
        paramsMap.put("pageSize",String.valueOf(pageSize));

        NetRequestManager.getInstance().getMineRunTeam(paramsMap, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                stopRefreshAnimation();
            }

            @Override
            public void onNext(String jsonStr) {
                // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                Object[] datas = JsonParseUtil.getInstance().parseJsonList(jsonStr, WalkActivitiesInfo.class);
                if ((Integer) datas[0] == 0) {
                    WalkActivitiesInfo walkActivitiesInfo = new Gson().fromJson(datas[1].toString(), WalkActivitiesInfo.class);
                    if (walkActivitiesInfo != null && walkActivitiesInfo.getList() != null) {
                        mAdapter.setWalkActivityInfos(mPage,walkActivitiesInfo.getList());
                        mPage++;

                    }
                } else {
                    Toast.makeText(mContext, datas[2].toString(), Toast.LENGTH_SHORT).show();
                }
                stopRefreshAnimation();
            }
        });
    }

    private void initView() {
        vTitleBar = (TitleBarView) findViewById(R.id.title_bar);
        setTitle("我的跑团");

        mListViewR.setMode(PullToRefreshBase.Mode.BOTH);

        SwpipeListViewOnScrollListener scrollListener = new SwpipeListViewOnScrollListener(swipeLayoutR);
        mListViewR.setOnScrollListener(scrollListener);

        swipeLayoutR.setColorSchemeResources(R.color.red);
        mAdapter = new CommActAdapter(mActivity,MyRunTeamActivity.this);
        mListViewR.setAdapter(mAdapter);

        //暂无数据
        View emptyView = LayoutInflater.from(mActivity).inflate(R.layout.view_empty, null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));
        ((ViewGroup) mListViewR.getParent()).addView(emptyView);
        TextView tv_empty = (TextView) emptyView.findViewById(R.id.tv_empty);
        tv_empty.setText(getString(R.string.activity_no_data));
        mListViewR.setEmptyView(emptyView);
    }

    private void stopRefreshAnimation(){
        if (mListViewR != null){
            mListViewR.onRefreshComplete();
        }
        if (swipeLayoutR != null){
            swipeLayoutR.setRefreshing(false);
        }
    }

}
