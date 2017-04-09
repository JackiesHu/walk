package com.buxingzhe.pedestrian.community.community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseFragment;

/**
 * 圈子
 * Created by jackie on 2017/3/17.
 */

public class CommCircleFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comm_acti,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        view.findViewById(R.id.comm_circle_list);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
