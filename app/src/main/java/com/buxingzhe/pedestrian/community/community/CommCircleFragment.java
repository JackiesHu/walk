package com.buxingzhe.pedestrian.community.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseFragment;
import com.buxingzhe.pedestrian.bean.activity.WalkRecordInfo;
import com.buxingzhe.pedestrian.bean.activity.WalkRecordsInfo;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.listen.SwpipeListViewOnScrollListener;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.JsonParseUtil;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import rx.Subscriber;

/**
 * 圈子
 * Created by jackie on 2017/3/17.
 */

public class CommCircleFragment extends BaseFragment {
    public final static String WALKRECORDINFO = "WALKRECORDINFO";
    private SwipeRefreshLayout mRefresh;
    private PullToRefreshListView mListView;
    private CommCircleAdapter mAdapter;
    private int mPage = 1;
    private final static int pageSize = 10;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comm_acti, null);
        mActivity = getActivity();
        mContext = getContext();
        findView(view);
        setListAdapter();
        loadData();
        setOnClick();
        return view;

//        SharedPreferences sharedPreferences = SharedPreferencesUtil.getInstance().getSharedPreferences(mContext);
//        userId = shared
    }

    private void findView(View view) {
        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        mListView = (PullToRefreshListView) view.findViewById(R.id.mListView);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);

        SwpipeListViewOnScrollListener scrollListener = new SwpipeListViewOnScrollListener(mRefresh);
        mListView.setOnScrollListener(scrollListener);

        mRefresh.setColorSchemeResources(R.color.red);

        //暂无数据
        View emptyView = LayoutInflater.from(mActivity).inflate(R.layout.view_empty, null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));
        ((ViewGroup) mListView.getParent()).addView(emptyView);
        TextView tv_empty = (TextView) emptyView.findViewById(R.id.tv_empty);
        tv_empty.setText(getString(R.string.activity_no_data));
        mListView.setEmptyView(emptyView);
    }


    private void setListAdapter() {
        mAdapter = new CommCircleAdapter(mActivity,getActivity());
        mListView.setAdapter(mAdapter);
    }



    private void loadData() {
        NetRequestManager.getInstance().getWalkRecords(GlobalParams.USER_ID, mPage, pageSize, new Subscriber<String>() {
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
                Object[] datas = JsonParseUtil.getInstance().parseJsonList(jsonStr, WalkRecordsInfo.class);
                if ((Integer) datas[0] == 0) {
                    WalkRecordsInfo walkRecordsInfo = new Gson().fromJson(datas[1].toString(), WalkRecordsInfo.class);
                    if (walkRecordsInfo != null && walkRecordsInfo.getList() != null) {
                        mAdapter.setDatas(mPage,walkRecordsInfo.getList());
                        mPage++;
                    }
                } else {
                    Toast.makeText(mContext, datas[2].toString(), Toast.LENGTH_SHORT).show();
                }
                stopRefreshAnimation();
            }
        });
    }

    private void stopRefreshAnimation(){
        if (mListView != null){
            mListView.onRefreshComplete();
        }
        if (mRefresh != null){
            mRefresh.setRefreshing(false);
        }
    }

    private void setOnClick() {
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }
        });
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                loadData();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position --;
                if (mAdapter != null && position >= 0){
                    WalkRecordInfo walkRecordInfo = mAdapter.getItem(position);
                    enterWalkRecordDetailActivity(walkRecordInfo);
                }
            }
        });
    }

    private void enterWalkRecordDetailActivity(WalkRecordInfo data){
        Intent intent = new Intent(getActivity(),WalkRecordDetailActivity.class);
        intent.putExtra(WALKRECORDINFO,data);
        EnterActUtils.startAct(getActivity(),intent);
    }
}
