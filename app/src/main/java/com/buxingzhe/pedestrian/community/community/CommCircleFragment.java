package com.buxingzhe.pedestrian.community.community;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.buxingzhe.pedestrian.bean.activity.WalkRecordInfo;
import com.buxingzhe.pedestrian.bean.activity.WalkRecordsInfo;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.JsonParseUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * 圈子
 * Created by jackie on 2017/3/17.
 */

public class CommCircleFragment extends BaseFragment {
//    private SwipeRefreshLayout mRefresh;
    private RecyclerView vRecyclerView;
    private BaseAdapter mAdapter;
//    private CommCircleAdapter mAdapter;
    private List<WalkRecordInfo> walkRecordInfos = new ArrayList<>();
    private int currentIndex = 1;
    private final static int pageSize = 20;

    int loadCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comm_acti,null);
        findView(view);
        addListen();
        mContext = getContext();
        getWalkRecords();
        setAct();
        return view;
    }

    private void getWalkRecords() {
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
                Object[] datas = JsonParseUtil.getInstance().parseJson(jsonStr, WalkRecordsInfo.class);
                if ((Integer) datas[0] == 0) {
                    Log.i(datas[1].toString());

                    WalkRecordsInfo walkRecordsInfo = (WalkRecordsInfo) datas[1];
                    if (walkRecordsInfo != null&&walkRecordsInfo.getList()!=null) {
                        walkRecordInfos = walkRecordsInfo.getList();
//                        mAdapter.setWalkRecordInfos(currentIndex, walkRecordInfos);
                        currentIndex++;
//                        stopRefreshAnimation();
                    }

                } else {
                    Toast.makeText(mContext, datas[2].toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findView(View view) {
//        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        vRecyclerView = (RecyclerView)view.findViewById(R.id.walk_list);

//        SwipeRefreshProperty.getInstall().setSwipeInfo(mContext, mRefresh);
//        SwpipeListViewOnScrollListener scrollListener = new SwpipeListViewOnScrollListener(mRefresh);
//        vRecyclerView.setOnScrollListener(scrollListener);
    }
    private void setAct() {
        mActivity = getActivity();
//        mAdapter = new CommCircleAdapter(mContext);

        //创建被装饰者类实例
        final CommCircleAdapter adapter = new CommCircleAdapter(mContext);

        //创建装饰者实例，并传入被装饰者和回调接口
        mAdapter = new LoadMoreAdapterWrapper(adapter, new OnLoad() {
            @Override
            public void load(int pagePosition, int pageSize,final ILoadCallback callback) {
                //此处模拟做网络操作，2s延迟，将拉取的数据更新到adpter中
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> dataSet = new ArrayList();
                        for (int i = 0; i < 10; i++) {
                            dataSet.add("我是一条数据");
                        }
                        //数据的处理最终还是交给被装饰的adapter来处理
                        adapter.appendData(dataSet);
                        callback.onSuccess();
                        //模拟加载到没有更多数据的情况，触发onFailure
                        if (loadCount++ == 3) {
                            callback.onFailure();
                        }
                    }
                }, 2000);
            }
        });

        LinearLayoutManager linearLayoutManger =  new LinearLayoutManager(getContext());
        vRecyclerView.setLayoutManager(linearLayoutManger);//这里用线性显示 类似于listview
        vRecyclerView.setAdapter(mAdapter);
    }

    public void addListen() {
//        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                //刷新数据操作
//                currentIndex = 1;
//                getWalkRecords();
//            }
//        });


//        vRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                position --;
//                if (position < 0){
//                    return;
//                }
//                if (messageRecommendAdapter != null){
//                    MessageOffersData messageOffersData = messageRecommendAdapter.getItem(position);
//                    if (messageOffersData==null){
//                        return;
//                    }
//                    String groupId = messageOffersData.groupid;
//                    if (TextUtils.isEmpty(messageOffersData.groupid)){
//                        return;
//                    }
//                    Intent intent = new Intent(mContext, ChatActivity.class);
//                    String mUserName = "";
//                    String mAvatar = "";
//                    if (mUserInfoData!=null){
//                        if(!TextUtils.isEmpty(mUserInfoData.name)){
//                            mUserName = mUserInfoData.name;
//                        }
//                        if(!TextUtils.isEmpty(mUserInfoData.avatar)){
//                            mAvatar = mUserInfoData.avatar;
//                        }
//                    }
//                    intent.putExtra(Constant.MUSERNAME, mUserName);
//                    intent.putExtra(Constant.MAVATAR, mAvatar);
//                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
//                    intent.putExtra(Constant.EXTRA_USER_ID, groupId);
//                    startActivity(intent);
//                }
//            }
//        });
    }

//    private void stopRefreshAnimation(){
//        if (vRecyclerView != null){
////            vRecyclerView.onRefreshComplete();
//        }
//        if (mRefresh != null){
//            mRefresh.setRefreshing(false);
//        }
//    }
}
