package com.buxingzhe.pedestrian.community.community;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.buxingzhe.lib.util.Log;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseAdapter;
import com.buxingzhe.pedestrian.activity.BaseFragment;
import com.buxingzhe.pedestrian.activity.ILoadCallback;
import com.buxingzhe.pedestrian.activity.LoadMoreAdapterWrapper;
import com.buxingzhe.pedestrian.activity.OnLoad;
import com.buxingzhe.pedestrian.bean.activity.WalkActivitiesInfo;
import com.buxingzhe.pedestrian.bean.activity.WalkActivityInfo;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.JsonParseUtil;
import com.google.gson.Gson;

import rx.Subscriber;

/**
 * 社区
 * Created by jackie on 2017/4/17.
 */
public class CommActFragment extends BaseFragment {
    public final static String WALKACTIVITYINFO = "WALKACTIVITYINFO";
    private SwipeRefreshLayout mRefresh;
    private RecyclerView vRecyclerView;
    private BaseAdapter mAdapter;
    private CommActAdapter commActAdapter;
    private int currentIndex = 1;
    private final static int pageSize = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_comm_acti, null);
        initView(view);
        setData();
        initPullRefresh();
        setOnClick();
        Log.e("log");
        return view;
    }

    private void initView(View view) {
        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        vRecyclerView = (RecyclerView) view.findViewById(R.id.walk_list);

//        vRecyclerView.addItemDecoration(new SpaceItemDecoration(SystemUtils.dip2px(mContext,20)));

//        SwipeRefreshProperty.getInstall().setSwipeInfo(mContext, mRefresh);
//        SwpipeListViewOnScrollListener scrollListener = new SwpipeListViewOnScrollListener(mRefresh);
//        vRecyclerView.setOnScrollListener(scrollListener);
        mRefresh.setColorSchemeResources(R.color.red);

    }

    private void setOnClick() {
        commActAdapter.setOnItemClickListener(new CommActAdapter.OnRecyclerViewItemClickListener(){
            @Override
            public void onItemClick(View view , WalkActivityInfo data){
                enterCommActInfoActivity(data);
            }
        });
    }

    private void enterCommActInfoActivity(WalkActivityInfo walkActivityInfo) {
        Intent intent = new Intent();
        intent.setClass(mContext, CommActInfoActivity.class);
        intent.putExtra(WALKACTIVITYINFO,walkActivityInfo);
        EnterActUtils.startAct(getActivity(), intent);
    }



    private void initPullRefresh() {
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NetRequestManager.getInstance().getActivities(currentIndex, pageSize, new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(String jsonStr) {
                                // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                                Object[] datas = JsonParseUtil.getInstance().parseJsonList(jsonStr, WalkActivitiesInfo.class);
                                if ((Integer) datas[0] == 0) {
                                    WalkActivitiesInfo walkActivitiesInfo = new Gson().fromJson(datas[1].toString(), WalkActivitiesInfo.class);
                                    if (walkActivitiesInfo != null && walkActivitiesInfo.getList() != null) {
//                                        new CommActAdapter(getActivity(),mContext).updateData(walkActivitiesInfo.getList());
                                        commActAdapter.updateData(walkActivitiesInfo.getList());
                                    }
                                }
                            }
                        });
                        //刷新完成
                        mRefresh.setRefreshing(false);
                    }

                }, 2000);

            }
        });
    }


    private void setData() {
        //创建被装饰者类实例
        commActAdapter = new CommActAdapter(getActivity(),mContext);

        //创建装饰者实例，并传入被装饰者和回调接口
        mAdapter = new LoadMoreAdapterWrapper(commActAdapter, new OnLoad() {
            @Override
            public void load(int pagePosition, int pageSize, final ILoadCallback callback) {
                NetRequestManager.getInstance().getActivities(pagePosition, pageSize, new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure();
                    }

                    @Override
                    public void onNext(String jsonStr) {
                        // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                        Object[] datas = JsonParseUtil.getInstance().parseJsonList(jsonStr, WalkActivitiesInfo.class);
                        if ((Integer) datas[0] == 0) {
                            WalkActivitiesInfo walkActivitiesInfo = new Gson().fromJson(datas[1].toString(), WalkActivitiesInfo.class);
                            if (walkActivitiesInfo != null && walkActivitiesInfo.getList() != null) {
                                commActAdapter.appendData(walkActivitiesInfo.getList());
                                callback.onSuccess();
                            }
                        } else {
                            callback.onFailure();
                        }
                    }
                });
            }
        });

        LinearLayoutManager linearLayoutManger = new LinearLayoutManager(getContext());
        vRecyclerView.setLayoutManager(linearLayoutManger);//这里用线性显示 类似于listview
        vRecyclerView.setAdapter(mAdapter);
    }
}
