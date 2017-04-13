package com.buxingzhe.pedestrian.community.community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseFragment;
import com.buxingzhe.pedestrian.bean.AdvCommunityData;
import com.buxingzhe.pedestrian.bean.user.UserLoginResultInfo;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.JsonParseUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * 圈子
 * Created by jackie on 2017/3/17.
 */

public class CommCircleFragment extends BaseFragment {
    private RecyclerView vRecyclerView;
    private CommCircleAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comm_acti,null);
        findView(view);
        mContext = getContext();
        getWalkRecords();
        setAct();
        return view;
    }

    private void getWalkRecords() {
        NetRequestManager.getInstance().getWalkRecords("43ac41862ca14c65a7ede94ab4d438f0", 1, 20, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String jsonStr) {
                // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                Object[] datas = JsonParseUtil.getInstance().parseJson(jsonStr, UserLoginResultInfo.class);
//                if ((Integer) datas[0] == 0) {
//                    Log.i(datas[1].toString());
//
//                    UserLoginResultInfo resultInfo = (UserLoginResultInfo) datas[1];
//                    if (resultInfo != null) {
//                        //TODO 保存数据
//
//                        //TODO 跳转Main
//                        EnterActUtils.enterMainActivity(RegisterActivity.this);
//                        finish();
//                    }
//
//                } else {
//                    Toast.makeText(RegisterActivity.this, datas[2].toString(), Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    private void findView(View view) {
        vRecyclerView = (RecyclerView)view.findViewById(R.id.walk_list);
    }
    private void setAct() {
        mActivity = getActivity();
        ArrayList<AdvCommunityData> communityDatas = createCommunities();
        mAdapter = new CommCircleAdapter(mContext,mActivity,communityDatas);
        LinearLayoutManager linearLayoutManger =  new LinearLayoutManager(getContext());
        vRecyclerView.setLayoutManager(linearLayoutManger);//这里用线性显示 类似于listview
        vRecyclerView.setAdapter(mAdapter);
    }
    private ArrayList<AdvCommunityData> createCommunities(){
        ArrayList<AdvCommunityData> communityDatas = new ArrayList<AdvCommunityData>();
        for (int i=0;i<10;i++){
            AdvCommunityData communityData = new AdvCommunityData();
            communityData.imageCount =i;
            communityData.summary = "游玩";
        }
        return communityDatas;
    }
}
