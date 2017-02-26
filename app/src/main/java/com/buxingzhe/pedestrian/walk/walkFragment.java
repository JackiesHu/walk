package com.buxingzhe.pedestrian.walk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseFragment;
import com.buxingzhe.pedestrian.widget.TitleBarView;

/**
 * Created by quanjing on 2017/2/23.
 */
public class WalkFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_walk,container,false);
        mContext = getContext();
        findId(view);
        onClick();
        return view;
    }
    private void findId(View view){
        vTitleBar = (TitleBarView) view.findViewById(R.id.walk_title_bar);
        setTitle("步行者联盟");
        hideLeftIco();
    };
    private void onClick() {

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}
