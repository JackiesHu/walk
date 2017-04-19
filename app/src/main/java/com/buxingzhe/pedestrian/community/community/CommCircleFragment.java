package com.buxingzhe.pedestrian.community.community;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseAdapter;
import com.buxingzhe.pedestrian.activity.BaseFragment;
import com.buxingzhe.pedestrian.activity.ILoadCallback;
import com.buxingzhe.pedestrian.activity.LoadMoreAdapterWrapper;
import com.buxingzhe.pedestrian.activity.OnLoad;
import com.buxingzhe.pedestrian.bean.activity.WalkRecordInfo;
import com.buxingzhe.pedestrian.bean.activity.WalkRecordsInfo;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.JsonParseUtil;
import com.google.gson.Gson;

import rx.Subscriber;

/**
 * 圈子
 * Created by jackie on 2017/3/17.
 */

public class CommCircleFragment extends BaseFragment {
    private SwipeRefreshLayout mRefresh;
    private RecyclerView vRecyclerView;
    private BaseAdapter mAdapter;
    private CommCircleAdapter commCircleAdapter;
    private int currentIndex = 1;
    private final static int pageSize = 10;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comm_acti, null);
        findView(view);
        mContext = getContext();
        initPullRefresh();
        setData();
        setOnClick();
        return view;

//        SharedPreferences sharedPreferences = SharedPreferencesUtil.getInstance().getSharedPreferences(mContext);
//        userId = shared
    }

    private void findView(View view) {
        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        vRecyclerView = (RecyclerView) view.findViewById(R.id.walk_list);

//        SwipeRefreshProperty.getInstall().setSwipeInfo(mContext, mRefresh);
//        SwpipeListViewOnScrollListener scrollListener = new SwpipeListViewOnScrollListener(mRefresh);
//        vRecyclerView.setOnScrollListener(scrollListener);
        mRefresh.setColorSchemeResources(R.color.red);

    }


    private void initPullRefresh() {
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NetRequestManager.getInstance().getWalkRecords("43ac41862ca14c65a7ede94ab4d438f0", currentIndex, pageSize, new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(String jsonStr) {
                                // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                                Object[] datas = JsonParseUtil.getInstance().parseJsonList(jsonStr, WalkRecordsInfo.class);
                                if ((Integer) datas[0] == 0) {
                                    WalkRecordsInfo walkRecordsInfo = new Gson().fromJson(datas[1].toString(), WalkRecordsInfo.class);
                                    if (walkRecordsInfo != null && walkRecordsInfo.getList() != null) {
                                        new CommCircleAdapter(mContext).updateData(walkRecordsInfo.getList());
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
        commCircleAdapter = new CommCircleAdapter(mContext);

        //创建装饰者实例，并传入被装饰者和回调接口
        mAdapter = new LoadMoreAdapterWrapper(commCircleAdapter, new OnLoad() {
            @Override
            public void load(int pagePosition, int pageSize, final ILoadCallback callback) {
                NetRequestManager.getInstance().getWalkRecords(GlobalParams.USER_ID, pagePosition, pageSize, new Subscriber<String>() {
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
                        Object[] datas = JsonParseUtil.getInstance().parseJsonList(jsonStr, WalkRecordsInfo.class);
                        if ((Integer) datas[0] == 0) {
                            WalkRecordsInfo walkRecordsInfo = new Gson().fromJson(datas[1].toString(), WalkRecordsInfo.class);
                            if (walkRecordsInfo != null && walkRecordsInfo.getList() != null) {
                                commCircleAdapter.appendData(walkRecordsInfo.getList());
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

    private void setOnClick() {
        commCircleAdapter.setOnItemClickListener(new CommCircleAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, WalkRecordInfo data) {

            }
        });
    }
}
