package com.buxingzhe.pedestrian.found;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.buxingzhe.pedestrian.R;


/**
 * Created by quanjing on 2017/2/5.
 */
public class FoundFragment extends Fragment implements View.OnClickListener {
    private Context mContext;
    private Toolbar toolbar;
    private RelativeLayout vMunuSearch;
    private BikeNavigateHelper mNaviHelper;
    private MapView vMapView;
    private BaiduMap mBaidumap = null;
    public FoundFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found, null);
        findViewId(view);
        onClick();

        return view;
    }
    private void onClick() {
        vMunuSearch.setOnClickListener(this);
    }

    public void findViewId(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        vMunuSearch = (RelativeLayout) view.findViewById(R.id.rl_munu_search);
        vMapView = (MapView) view.findViewById(R.id.map);
        mBaidumap = vMapView.getMap();
        toolbar.setTitle("发现");// 标题的文字需在setSupportActionBar之前，不然会无效
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Log.e(TAG, "onCreateOptionsMenu()");
        menu.clear();
        //inflater.inflate(R.menu.menu_parent_fragment, menu);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rl_munu_search:
                Intent intent = new Intent(getContext(),ActivitySearchFound.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void initNaviHelper(){


    }
}
