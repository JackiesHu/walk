package com.buxingzhe.pedestrian.found;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buxingzhe.pedestrian.R;


/**
 * Created by quanjing on 2017/2/5.
 */
public class MapFoundFragment extends Fragment {
    //private MapView mMapView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_found, null);
        findId(view);
        return view;
    }
    private void findId(View view) {
        //mMapView = (MapView) view.findViewById(R.id.bmapView);
    }
}
