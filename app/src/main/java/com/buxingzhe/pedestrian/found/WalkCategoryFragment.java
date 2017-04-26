package com.buxingzhe.pedestrian.found;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.found.adapter.RecycleBaseAdapter;
import com.buxingzhe.pedestrian.found.bean.RemarkPoint;
import com.buxingzhe.pedestrian.found.bean.WalkRecord;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by jackie on 2017/2/11.
 */

public class WalkCategoryFragment extends Fragment {
    private RecyclerView vRecycler;
    private List<WalkRecord> walkRecords = new ArrayList<>();
    private WalkCategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.walk_category_fragment, container,false);
        findViewId(view);
        setRecycler();
        onClick();
        return view;
    }

    private void setRecycler() {
        LinearLayoutManager linearLayoutManger =  new LinearLayoutManager(getContext());
        vRecycler.setLayoutManager(linearLayoutManger);//这里用线性显示 类似于listview
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示 类似于grid view
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
        adapter = new WalkCategoryAdapter(getContext(),walkRecords, R.layout.walk_category_item);
        vRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecycleBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(),WalkDetailsActivity.class);
                intent.putExtra("locationData",walkRecords.get(position));
                intent.putExtra("myLocation",FoundFragment.ll);
                startActivity(intent);
            }
        });
    }

    public void setData(List<WalkRecord> walkRecordses){
        this.walkRecords.clear();
        this.walkRecords.addAll(walkRecordses);
        adapter.notifyDataSetChanged();
    }

    public void findViewId(View view){
        vRecycler = (RecyclerView) view.findViewById(R.id.walk_list);
    }
    private void onClick() {
    }
}
