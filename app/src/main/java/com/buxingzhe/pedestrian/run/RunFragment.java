package com.buxingzhe.pedestrian.run;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseFragment;
import com.buxingzhe.pedestrian.listen.OnInteractionData;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.TitleBarLinstener;
import com.buxingzhe.pedestrian.widget.TitleBarView;

import java.util.Timer;
import java.util.TimerTask;

import static com.buxingzhe.pedestrian.R.id.run_iv_start;
import static com.buxingzhe.pedestrian.R.id.run_iv_zoom_suofang;
import static com.buxingzhe.pedestrian.R.id.run_iv_zoom_zhankai;

/**
 * Created by quanjing on 2017/2/23.
 */
public class RunFragment extends BaseFragment implements View.OnClickListener, Chronometer.OnChronometerTickListener {

    private OnInteractionData mOnInteractionData;

    public void setOnInteractionData(OnInteractionData onInteractionData) {
        mOnInteractionData = onInteractionData;
    }

    // 地图相关
    private MapView mMapView;
    private ImageView mIVLocation, mIVRunStart, mIVZoomSuoFang, mIVZoomZhankai;

    // 倒计时开始计步
//    private TextView mTVCountDown;
    TextView mTVCountDown;
    private int countDownNum = 3;
    private Timer timer = new Timer();


    // 底部Small View
    private LinearLayout mLLBottomSmallView;
    private TextView mTVBottomSamllDistance;
    private TextView mTVBottomSmallTime;

    // 底部Data View
    private LinearLayout mLLBottomData, mLLBottomDataPic;
    private TextView mTVBottomDataTime, mTVBottomDataDistance, mTVBottomDataWalkNum;// 时长／里程／步数

    // 底部All View
    private LinearLayout mLLBottomAllView;
    private ImageView mIVRunState, mIVRunDone;
    private TextView mTVRunStart, mTVRunState;
    private TextView mTVRunDistance, mTVRunWalkNum, mTVRunCalorie;// 距离／步数／卡路里

    // 运动中显示的标记
    private static boolean isRunState = false;//运动状态，（false：暂停；  true：继续）
    private static boolean isRunDone = false;//完成运动 （false：未完成 true：完成）
    // 计时器
    private Chronometer mChronometer;//计时器
    private static int seconds;//秒数
    private static long mCurrentRunTime;//记录运动时间


    private Handler mCountDownHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (countDownNum > 0) {
                    mTVCountDown.setText("" + (countDownNum--));
                } else {
                    getActivity().getWindowManager().removeViewImmediate(mTVCountDown);
                    timer.cancel();
                    timer = null;
                    countDownNum = 3;
                    // TODO 倒计时完成 开始计步
                    mTVCountDown.setVisibility(View.GONE);
                    mTVRunStart.setVisibility(View.GONE);
                    mLLBottomAllView.setVisibility(View.VISIBLE);
                    mLLBottomSmallView.setVisibility(View.GONE);
                    mIVZoomSuoFang.setClickable(true);
                    mIVZoomZhankai.setClickable(true);
                    mChronometer.start();
                }
            }

        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run, null);
        findId(view);
        setData();
        onClick();
        return view;
    }

    private void findId(View view) {
        vTitleBar = (TitleBarView) view.findViewById(R.id.run_title_bar);
        // map
        mMapView = (MapView) view.findViewById(R.id.run_map_view);
        mIVZoomSuoFang = (ImageView) view.findViewById(run_iv_zoom_suofang);
        mIVZoomZhankai = (ImageView) view.findViewById(run_iv_zoom_zhankai);
        mIVLocation = (ImageView) view.findViewById(R.id.run_iv_location);
        // 倒计时计步
        mIVRunStart = (ImageView) view.findViewById(run_iv_start);
//        mTVCountDown = (TextView) view.findViewById(R.id.run_tv_count_down);
        mTVRunStart = (TextView) view.findViewById(R.id.run_tv_start);

        // 底部Small View
        mLLBottomSmallView = (LinearLayout) view.findViewById(R.id.fun_ll_bottom_small);
        mTVBottomSamllDistance = (TextView) view.findViewById(R.id.run_tv_distance_small);
        mTVBottomSmallTime = (TextView) view.findViewById(R.id.run_tv_time_small);

        // 底部Data View
        mLLBottomDataPic = (LinearLayout) view.findViewById(R.id.run_ll_bottom_data_pic);
        mLLBottomData = (LinearLayout) view.findViewById(R.id.run_ll_bottom_data);
        mTVBottomDataTime = (TextView) view.findViewById(R.id.run_tv_data_time);
        mTVBottomDataDistance = (TextView) view.findViewById(R.id.run_tv_data_distance);
        mTVBottomDataWalkNum = (TextView) view.findViewById(R.id.run_tv_data_walk_num);

        // 底部All View
        mLLBottomAllView = (LinearLayout) view.findViewById(R.id.run_ll_bottom_all);
        mChronometer = (Chronometer) view.findViewById(R.id.run_chronometer);
        mTVRunDistance = (TextView) view.findViewById(R.id.run_tv_distance);
        mTVRunWalkNum = (TextView) view.findViewById(R.id.run_tv_walk_num);
        mTVRunCalorie = (TextView) view.findViewById(R.id.run_tv_calorie);
        mTVRunState = (TextView) view.findViewById(R.id.run_tv_run_state);
        mIVRunState = (ImageView) view.findViewById(R.id.run_iv_run_state);
        mIVRunDone = (ImageView) view.findViewById(R.id.run_iv_run_done);

    }

    private void setData() {
        initCountDownTV();

        setInitTitle();

        mLLBottomAllView.setVisibility(View.GONE);
        mIVZoomSuoFang.setVisibility(View.GONE);
        if (!isRunState) {
            mTVRunState.setText(getResources().getString(R.string.run_pause));
        }

    }

    /**
     * 初始化倒计时TextView
     */
    private void initCountDownTV() {
        mTVCountDown = new TextView(getContext());
        mTVCountDown.setBackgroundColor(Color.parseColor("#B3000000"));
        mTVCountDown.setTextSize(TypedValue.COMPLEX_UNIT_SP, 360.0f);
        mTVCountDown.setTextColor(Color.parseColor("#FFFFFF"));
        mTVCountDown.setGravity(Gravity.CENTER);
        mTVCountDown.setFocusable(true);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mTVCountDown.setLayoutParams(param);
    }

    private void setInitTitle() {
        //set title
        setTitle("地名");
        setTitleRight("骑行");
        showLeftIco();
        setImgLeftOnclick();
    }

    private void onClick() {

        mIVLocation.setOnClickListener(this);
        mIVRunStart.setOnClickListener(this);
        mIVRunState.setOnClickListener(this);
        mIVRunDone.setOnClickListener(this);

        // ImageView 设置不可点击，需要按照下面顺序写。
        mIVZoomSuoFang.setOnClickListener(this);
        mIVZoomZhankai.setOnClickListener(this);
        mIVZoomSuoFang.setClickable(false);
        mIVZoomZhankai.setClickable(false);

        mChronometer.setOnChronometerTickListener(this);


        if (vTitleBar != null) {
            vTitleBar.setTitleBarLinstener(new MineTitleBarListener());
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.run_iv_location://TODO 定位
                break;
            case run_iv_start://开始Run
                mIVRunStart.setVisibility(View.GONE);
                mTVRunStart.setVisibility(View.GONE);
                mTVCountDown.setVisibility(View.VISIBLE);

                //TODO 此时倒计时需要全凭
                WindowManager windowManager = getActivity().getWindowManager();
                int[] screenAttr = SystemUtils.getDisplayWidth(getContext());
                WindowManager.LayoutParams params = new WindowManager.LayoutParams(screenAttr[0], screenAttr[1]);
                params.gravity = Gravity.CENTER;
                params.alpha = 0.7f;
                params.x = 0;
                params.y = 0;
                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
                params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
                windowManager.addView(mTVCountDown, params);

                if (timer == null) {
                    timer = new Timer();
                }
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        mCountDownHander.sendEmptyMessage(0);
                    }
                }, 0, 1000);

                break;
            case R.id.run_iv_zoom_zhankai://展开地图 (缩放状态)
                mIVZoomZhankai.setVisibility(View.GONE);
                mIVZoomSuoFang.setVisibility(View.VISIBLE);
                if (!isRunDone) {
                    //运动中
                    mLLBottomAllView.setVisibility(View.GONE);
                    mLLBottomSmallView.setVisibility(View.VISIBLE);
//                    if(isRunState == Boolean.FALSE){
////                        mTVBottomSmallTime.start(mCurrentRunTime);
////                        mTVBottomSmallTime.restart();
//                    }else{
////                        mTVBottomSmallTime.pause();
//                    }
//
//                    saveCurrentRunTime(mChronometer);
                } else {
                    //运动结束
                    mLLBottomDataPic.setVisibility(View.GONE);
                }

                break;
            case R.id.run_iv_zoom_suofang://缩放地图 (展开状态)
                mIVZoomZhankai.setVisibility(View.VISIBLE);
                mIVZoomSuoFang.setVisibility(View.GONE);
                if (!isRunDone) {
                    //运动中
                    mLLBottomAllView.setVisibility(View.VISIBLE);
                    mLLBottomSmallView.setVisibility(View.GONE);
//                    if(isRunState == Boolean.FALSE){
////                        mTVBottomSmallTime.start(mCurrentRunTime);
////                        mTVBottomSmallTime.restart();
//                        mChronometer.start();
//                    }else{
////                        mTVBottomSmallTime.pause();
//                        mChronometer.stop();
//                    }
//
//                    saveCurrentRunTime(mChronometer);

                } else {
                    //运动结束
                    mLLBottomDataPic.setVisibility(View.GONE);
                }

                break;
            case R.id.run_iv_run_state://运动状态（暂停／继续）
                changeRunState();
                break;
            case R.id.run_iv_run_done:// TODO 运动完成
                isRunDone = true;
                isRunState = false;
//                saveCurrentRunTime(mCountdownView);
                changeRunState();
                mLLBottomAllView.setVisibility(View.GONE);
                mIVLocation.setVisibility(View.GONE);
                mLLBottomData.setVisibility(View.VISIBLE);
                mIVZoomZhankai.setVisibility(View.GONE);
                mIVZoomSuoFang.setVisibility(View.VISIBLE);

                //设置数据
                setBottomData();

                // TODO 重新定义Title
                hideLeftIco();
                setTitle("2017年1月12日 18:21");
                setTitleRight("完成");
                setTitleLeft("取消");

                setTitleLeftOnclick();
                setTitleRightOnclick();


                break;
        }
    }

    /**
     * 设置底部 Data View 的数据
     */
    private void setBottomData() {
        mTVBottomDataTime.setText(formatSeconds());
    }

    /**
     * 保存当前运动的时间
     *
     * @param countdownView
     */
    private void saveCurrentRunTime(Chronometer countdownView) {
        // 保留此时的时间
        mCurrentRunTime = countdownView.getBase();
    }

    private void changeRunState() {

        if (!isRunState) {
            // 显示继续(暂停状态)
            mTVRunState.setText(getResources().getString(R.string.run_continue));
//            mCountdownView.pause();
            mChronometer.stop();
        } else {
            // 显示暂停(继续状态)
            mTVRunState.setText(getResources().getString(R.string.run_pause));
//            mCountdownView.restart();
            mChronometer.start();
        }
        isRunState = !isRunState;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        //计时器的监听事件
        seconds++;
        saveCurrentRunTime(chronometer);
        String str = formatSeconds();
        chronometer.setText(str);
        mTVBottomSmallTime.setText(str);
    }

    private String formatSeconds() {
        String hh = seconds / 3600 > 9 ? seconds / 3600 + "" : "0" + seconds
                / 3600;
        String mm = (seconds % 3600) / 60 > 9 ? (seconds % 3600) / 60 + ""
                : "0" + (seconds % 3600) / 60;
        String ss = (seconds % 3600) % 60 > 9 ? (seconds % 3600) % 60 + ""
                : "0" + (seconds % 3600) % 60;

        return hh + ":" + mm + ":" + ss;

    }


    private class MineTitleBarListener implements TitleBarLinstener {
        @Override
        public void onLeftTitleListener(View v) {
            // 取消操作
            isRunDone = false;
            mLLBottomAllView.setVisibility(View.VISIBLE);
            mIVLocation.setVisibility(View.VISIBLE);
            mLLBottomData.setVisibility(View.GONE);
            mIVZoomZhankai.setVisibility(View.GONE);
            mIVZoomSuoFang.setVisibility(View.VISIBLE);

            setInitTitle();
        }

        @Override
        public void onRightListener(View v) {
            //TODO  完成 回到 Walk界面
            mChronometer.stop();
            seconds = 0;
            if (mOnInteractionData != null) {

                mOnInteractionData.onInteraction();
            }


        }

        @Override
        public void onRightImageListener(View v) {

        }

        @Override
        public void onLeftImgListener(View v) {
            //TODO 关闭 Run 页面
            if (mOnInteractionData != null) {
                mOnInteractionData.onInteraction();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        seconds = 0;
    }

}
