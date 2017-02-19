package com.buxingzhe.pedestrian.found;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buxingzhe.pedestrian.R;

/**
 * Created by jackie on 2017/2/11.
 */

public class WalkCategoryFragment extends Fragment {
    private RecyclerView vRecycler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.walk_category_fragment, null);
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
        vRecycler.setAdapter(new WalkCategoryAdapter(getContext(),getActivity()));

    }

    public void findViewId(View view){
        vRecycler = (RecyclerView) view.findViewById(R.id.walk_list);
    }
    private void onClick() {
    }
}
