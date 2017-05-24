package com.buxingzhe.pedestrian.User;

import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.buxingzhe.lib.util.Log;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseFragment;
import com.buxingzhe.pedestrian.bean.user.UserLoginResultInfo;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.JsonParseUtil;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.CircularImageView;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Created by quanjing on 2017/2/23.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    /**
     * 用户头像
     */
    private CircularImageView mUserAvatar;
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
        ButterKnife.bind(this, view);
        findId(view);
        onClick();
        return view;
    }

    private void findId(View view) {


        mUserAvatar = (CircularImageView) view.findViewById(R.id.user_iv_avatar);
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
        MobclickAgent.onPageStart("MeFragment");
        setData();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MeFragment");
    }

    private void onClick() {
        mUserAvatar.setOnClickListener(this);
        mMineStep.setOnClickListener(this);
        mMineTeam.setOnClickListener(this);
        mMineTask.setOnClickListener(this);
        mMineSetting.setOnClickListener(this);
    }

    private void setData() {
        System.out.println("splash--Me"+"GlobalParams.TOKEN"+GlobalParams.TOKEN+"GlobalParams.USER_ID"+GlobalParams.USER_ID);

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
                    //保存用户数据到share里
                    UserInfo userInfo = new UserInfo();
                    userInfo.formatUser(resultInfo);
                    userInfo.saveUserInfo(mContext,userInfo);
                    if (resultInfo != null) {
                        String userAvatar = (!TextUtils.isEmpty(userInfo.getAvatar())) ? userInfo.getAvatar() : "";
                        if (!TextUtils.isEmpty(userAvatar)) {

                            Glide
                                    .with(getActivity())
                                    .load(userAvatar)
                                    .override(SystemUtils.dip2px(getActivity(), 67.0f), SystemUtils.dip2px(getActivity(), 67.0f))
                                    .centerCrop()
                                    .into(mUserAvatar);
                        }

                        mUserName.setText(userInfo.getNickName());
                        if(userInfo.getGender().equals("1")){
                            mUserGender.setText("女");
                        }else{
                            mUserGender.setText("男");
                        }

                        mUserHeight.setText(userInfo.getHeight()+"cm");
                        mUserWeight.setText(userInfo.getWeight()+"kg");
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
                Intent intent = new Intent(getActivity(),UserInfoActivity.class);
                EnterActUtils.startForResultAct(getActivity(),intent,1);
                break;
            case R.id.user_rl_walk://我的步行
                Intent intent1 = new Intent(getActivity(),MyWalkActivity.class);
                EnterActUtils.startForResultAct(getActivity(),intent1,2);
                break;
            case R.id.user_rl_team://我的跑团
                Intent intent2 = new Intent(getActivity(),MyRunTeamActivity.class);
                EnterActUtils.startForResultAct(getActivity(),intent2,3);
                break;
            case R.id.user_rl_task://我的任务
                Intent intent3 = new Intent(getActivity(),MyTaskActivity.class);
                EnterActUtils.startForResultAct(getActivity(),intent3,4);
                break;
            case R.id.user_rl_setting://设置
                Intent intent4 = new Intent(getActivity(),SettingActivity.class);
                EnterActUtils.startForResultAct(getActivity(),intent4,5);
                break;
        }
    }
}
