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

import java.util.ArrayList;

/**
 * 社区
 * Created by jackie on 2017/2/28.
 */
public class CommActFragment extends BaseFragment {
    private RecyclerView vRecyclerView;
    private CommActAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comm_acti,null);
        findView(view);
        mContext = getContext();
        setAct();
        return view;
    }

    private void findView(View view) {
        vRecyclerView = (RecyclerView)view.findViewById(R.id.walk_list);
    }
    private void setAct() {
        mActivity = getActivity();
        ArrayList<AdvCommunityData> communityDatas = createCommunities();
        mAdapter = new CommActAdapter(mContext,mActivity,communityDatas);
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
