package com.buxingzhe.pedestrian.User;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseFragment;

/**
 * Created by quanjing on 2017/2/23.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    /**
     * 用户头像
     */
    private ImageView mUserAvatar;
    /**
     * 用户名称
     */
    private TextView mUserName;
    /**
     * 用户性别
     */
    private TextView mUserGender;
    /**
     * 用户身高
     */
    private TextView mUserHeight;
    /**
     * 用户体重
     */
    private TextView mUserWeight;
    /**
     * 我的步票
     */
    private TextView mUserStepTicket;
    /**
     * 我的积分
     */
    private TextView mUserPoints;
    /**
     * 我的步行
     */
    private RelativeLayout mMineStep;
    /**
     * 我的跑团
     */
    private RelativeLayout mMineTeam;
    /**
     * 我的任务
     */
    private RelativeLayout mMineTask;
    /**
     * 设置
     */
    private RelativeLayout mMineSetting;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        mContext = getContext();
        findId(view);
        onClick();
        return view;
    }

    private void findId(View view) {
//        vTitleBar = (TitleBarView) view.findViewById(R.id.me_title_bar);
//        setTitle("我");
//        hideLeftIco();

        mUserAvatar = (ImageView) view.findViewById(R.id.user_iv_avatar);
        mUserName = (TextView) view.findViewById(R.id.user_tv_username);
        mUserGender = (TextView) view.findViewById(R.id.user_tv_gender);
        mUserHeight = (TextView) view.findViewById(R.id.user_tv_height);
        mUserWeight = (TextView) view.findViewById(R.id.user_tv_weight);
        mUserStepTicket = (TextView) view.findViewById(R.id.user_tv_step_ticket);
        mUserPoints = (TextView) view.findViewById(R.id.user_tv_points);
        mMineStep = (RelativeLayout) view.findViewById(R.id.user_rl_walk);
        mMineTeam = (RelativeLayout) view.findViewById(R.id.user_rl_team);
        mMineTask = (RelativeLayout) view.findViewById(R.id.user_rl_task);
        mMineSetting = (RelativeLayout) view.findViewById(R.id.user_rl_setting);

    }


    private void onClick() {
        mUserAvatar.setOnClickListener(this);
        mMineStep.setOnClickListener(this);
        mMineTeam.setOnClickListener(this);
        mMineTask.setOnClickListener(this);
        mMineSetting.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_iv_avatar://头像

                break;
            case R.id.user_rl_walk://我的步行

                break;
            case R.id.user_rl_team://我的跑团

                break;
            case R.id.user_rl_task://我的任务

                break;
            case R.id.user_rl_setting://设置

                break;
        }
    }
}
