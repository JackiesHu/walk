package com.buxingzhe.pedestrian.User;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buxingzhe.lib.util.Log;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseFragment;
import com.buxingzhe.pedestrian.bean.user.UserLoginResultInfo;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.JsonParseUtil;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.squareup.picasso.Picasso;

import rx.Subscriber;

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

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void onClick() {
        mUserAvatar.setOnClickListener(this);
        mMineStep.setOnClickListener(this);
        mMineTeam.setOnClickListener(this);
        mMineTask.setOnClickListener(this);
        mMineSetting.setOnClickListener(this);
    }

    private void setData() {


        NetRequestManager.getInstance().getUserInfo(GlobalParams.USER_ID, GlobalParams.TOKEN, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String jsonStr) {

                Log.i(jsonStr);

                // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                Object[] datas = JsonParseUtil.getInstance().parseJson(jsonStr, UserLoginResultInfo.class);
                if ((Integer) datas[0] == 0) {
                    Log.i(datas[1].toString());

                    UserLoginResultInfo resultInfo = (UserLoginResultInfo) datas[1];
                    if (resultInfo != null) {
                        String userAvatar = (!TextUtils.isEmpty(resultInfo.getAvatarUrl())) ? resultInfo.getAvatarUrl() : "";
                        if (!TextUtils.isEmpty(userAvatar)) {
                            Picasso.with(getActivity()).load(userAvatar).resize(SystemUtils.dip2px(getActivity(),67.0f),SystemUtils.dip2px(getActivity(),67.0f)).centerCrop().into(mUserAvatar);
                        }

                        mUserName.setText(resultInfo.getNickName());
                        mUserGender.setText(resultInfo.getGender());
                        mUserHeight.setText(resultInfo.getHeight());
                        mUserWeight.setText(resultInfo.getWeight());
                        mUserStepTicket.setText(resultInfo.getWalkMoney());
                        mUserPoints.setText(resultInfo.getScore());
                    }

                } else  if ((Integer) datas[0] == 2){
                    Toast.makeText(getActivity(), datas[2].toString(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), datas[2].toString(), Toast.LENGTH_SHORT).show();
                }
            }

        });


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
