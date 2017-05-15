package com.buxingzhe.pedestrian.run;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.User.UserInfo;
import com.buxingzhe.pedestrian.application.PDApplication;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.common.StarBarBean;
import com.buxingzhe.pedestrian.found.adapter.PicAdapter;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.listen.OnInteractionData;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.FullGridView;
import com.buxingzhe.pedestrian.widget.MWTStarBar;
import com.buxingzhe.pedestrian.widget.MWTStarOnclick;
import com.buxingzhe.pedestrian.widget.TitleBarLinstener;
import com.buxingzhe.pedestrian.widget.TitleBarView;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.activity.ImagesGridActivity;
import com.pizidea.imagepicker.bean.ImageItem;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;

import static com.buxingzhe.pedestrian.R.id.run_iv_start;

/**
 * Created by quanjing on 2017/2/23.
 */
public class RunFragment extends RunRunFragment implements View.OnClickListener, Chronometer.OnChronometerTickListener, AndroidImagePicker.OnPictureTakeCompleteListener,AndroidImagePicker.OnImagePickCompleteListener {

    private OnInteractionData mOnInteractionData;
    double calories = 0;
    private MWTStarBar run_bottom_walked_stress_star, run_bottom_walked_environment_star, run_bottom_walked_safety_star;
    private EditText bottom_intro, bottom_title;
    private ScrollView run_done_scroll;
    private PDApplication myApp;
    private PicAdapter adapter;

    public void setOnInteractionData(OnInteractionData onInteractionData) {
        mOnInteractionData = onInteractionData;
    }


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
    private ImageView mLLBottomPickPic;
    private TextView tv_duration, tv_distance, tv_stepCount;// 时长／里程／步数
    private TextView tv_altitudeAsend;
    private TextView tv_altitudeHigh;
    private TextView tv_altitudeLow;
    private TextView tv_calorie;
    private TextView tv_fat;
    private TextView tv_nutrition;

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

    //分享图片
    private AndroidImagePicker mImagePicker;
    private List<String> pics = new ArrayList<>();
    ;
    private List<String> picNames = new ArrayList<>();
    ;
    private List<MultipartBody.Part> picParts = new ArrayList<>();
    private MultipartBody.Part pathPic =null;
    private FullGridView gv_pic ;

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
                    mChronometer.start();

                }
            }

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = (PDApplication) getActivity().getApplication();
        mImagePicker = AndroidImagePicker.getInstance();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run, null);
        findId(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setData();
        onClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("RunFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("RunFragment");
    }

    private void findId(View view) {
        vTitleBar = (TitleBarView) view.findViewById(R.id.run_title_bar);
        // map
        mMapView = (MapView) view.findViewById(R.id.run_map_view);

        // 倒计时计步
        mIVRunStart = (ImageView) view.findViewById(run_iv_start);
        mTVCountDown = (TextView) view.findViewById(R.id.run_tv_count_down);
        mTVRunStart = (TextView) view.findViewById(R.id.run_tv_start);

        // 底部Small View
        mLLBottomSmallView = (LinearLayout) view.findViewById(R.id.fun_ll_bottom_small);
        mTVBottomSamllDistance = (TextView) view.findViewById(R.id.run_tv_distance_small);
        mTVBottomSmallTime = (TextView) view.findViewById(R.id.run_tv_time_small);

        // 底部Data View
        run_done_scroll=(ScrollView)view.findViewById(R.id.run_done_data);
        mLLBottomDataPic = (LinearLayout) view.findViewById(R.id.run_ll_bottom_data_pic);
        mLLBottomPickPic = (ImageView) view.findViewById(R.id.run_ll_bottom_pick_pic);
        run_bottom_walked_stress_star = (MWTStarBar) view.findViewById(R.id.run_bottom_walked_stress_star);
        run_bottom_walked_environment_star = (MWTStarBar) view.findViewById(R.id.run_bottom_walked_environment_star);
        run_bottom_walked_safety_star = (MWTStarBar) view.findViewById(R.id.run_bottom_walked_safety_star);
        List<StarBarBean> starbars = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            StarBarBean starBaarBean = new StarBarBean(R.mipmap.ic_pingjia_star_grey);
            starBaarBean.dividerHeight = SystemUtils.dip2px(mContext, 5);
            starbars.add(starBaarBean);
        }
        run_bottom_walked_stress_star.setStarBarBeanList(starbars);
        run_bottom_walked_stress_star.setMwtStarOnclick(new OnStarClick(run_bottom_walked_stress_star));
        run_bottom_walked_environment_star.setStarBarBeanList(starbars);
        run_bottom_walked_environment_star.setMwtStarOnclick(new OnStarClick(run_bottom_walked_environment_star));
        run_bottom_walked_safety_star.setStarBarBeanList(starbars);
        run_bottom_walked_safety_star.setMwtStarOnclick(new OnStarClick(run_bottom_walked_safety_star));
        bottom_intro = (EditText) view.findViewById(R.id.run_ll_bottom_intro);
        bottom_title = (EditText) view.findViewById(R.id.run_ll_bottom_title);
        gv_pic = (FullGridView) view.findViewById(R.id.bottom_gv_pic);

        mLLBottomData = (LinearLayout) view.findViewById(R.id.run_ll_bottom_data);
        tv_duration = (TextView) view.findViewById(R.id.tv_duration);
        tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        tv_stepCount = (TextView) view.findViewById(R.id.tv_stepCount);
        tv_altitudeAsend = (TextView) view.findViewById(R.id.tv_altitudeAsend);
        tv_altitudeHigh = (TextView) view.findViewById(R.id.tv_altitudeHigh);
        tv_altitudeLow = (TextView) view.findViewById(R.id.tv_altitudeLow);
        tv_calorie = (TextView) view.findViewById(R.id.tv_calorie);
        tv_fat = (TextView) view.findViewById(R.id.tv_fat);
        tv_nutrition = (TextView) view.findViewById(R.id.tv_nutrition);

        // 底部All View running
        mLLBottomAllView = (LinearLayout) view.findViewById(R.id.run_ll_bottom_all);
        mChronometer = (Chronometer) view.findViewById(R.id.run_chronometer);

        mTVRunDistance = (TextView) view.findViewById(R.id.run_tv_distance);//running-距离
        mTVRunWalkNum = (TextView) view.findViewById(R.id.run_tv_walk_num);//running-步数
        mTVRunCalorie = (TextView) view.findViewById(R.id.run_tv_calorie);//running-卡路里
        mTVRunState = (TextView) view.findViewById(R.id.run_tv_run_state);
        mIVRunState = (ImageView) view.findViewById(R.id.run_iv_run_state);
        mIVRunDone = (ImageView) view.findViewById(R.id.run_iv_run_done);

    }

    private void setData() {
        setInitTitle();
        mLLBottomAllView.setVisibility(View.GONE);
        if (!isRunState) {
            mTVRunState.setText(getResources().getString(R.string.run_pause));
        }

        initCountDownTV();

    }

    /**
     * 初始化倒计时TextView
     */
    private void initCountDownTV() {
        mTVCountDown = new TextView(getContext());
        mTVCountDown.setBackgroundColor(Color.parseColor("#B3000000"));
        mTVCountDown.setTextSize(TypedValue.COMPLEX_UNIT_PX, 360.0f);
        mTVCountDown.setTextColor(Color.parseColor("#FFFFFF"));
        mTVCountDown.setGravity(Gravity.CENTER);
        mTVCountDown.setFocusable(true);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mTVCountDown.setLayoutParams(param);
    }

    private void setInitTitle() {
        //set title

        setTitle(myApp.getCityName());

        if (isWalking) {
            setTitleRight("骑行");

        } else {
            setTitleRight("步行");
        }

        showLeftIco();
        setImgLeftOnclick();
    }

    private void onClick() {

        mIVRunStart.setOnClickListener(this);
        mIVRunState.setOnClickListener(this);
        mIVRunDone.setOnClickListener(this);
        mChronometer.setOnChronometerTickListener(this);


        if (vTitleBar != null) {
            vTitleBar.setTitleBarLinstener(new MineTitleBarListener());
        }
        mLLBottomPickPic.setOnClickListener(this);

    }

    @Override
    public void onPictureTakeComplete(String picturePath) {

    }

    class OnStarClick implements MWTStarOnclick {
        MWTStarBar mwtStarBar;

        OnStarClick(MWTStarBar mwtStarBar) {
            this.mwtStarBar = mwtStarBar;
        }

        @Override
        public void upStarIco(int selectSize) {
            List<StarBarBean> starbars = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                StarBarBean starBaarBean = new StarBarBean(R.mipmap.ic_pingjia_star_grey);
                starBaarBean.dividerHeight = SystemUtils.dip2px(mContext, 5);
                if (i < selectSize) {
                    starBaarBean.pict = R.mipmap.ic_pingjia_star_yello;
                }
                starbars.add(starBaarBean);
            }
            mwtStarBar.setStarBarBeanList(starbars);
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
                //    requestLocation();
                break;
            case run_iv_start://开始Run
                startRun();
                mapStartRun();
                break;
            case R.id.run_iv_zoom_zhankai://展开地图 (缩放状态)


                break;
            case R.id.run_iv_zoom_suofang://缩放地图 (展开状态)


                break;
            case R.id.run_iv_run_state://运动状态（暂停／继续）
                changeRunState();

                break;
            case R.id.run_iv_run_done:// TODO 运动完成
                stopRun();
                isRunDone = true;
                isRunState = false;
                changeRunState();
                mLLBottomAllView.setVisibility(View.GONE);
                mLLBottomData.setVisibility(View.VISIBLE);
                mLLBottomDataPic.setVisibility(View.VISIBLE);
                run_done_scroll.setVisibility(View.VISIBLE);
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
            case R.id.run_ll_bottom_pick_pic:
                selectImage();
                break;

        }
    }

    private void startRun() {

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
    }

    /**
     * 设置底部 Data View 的数据
     */
    private void setBottomData() {
        tv_duration.setText(formatSeconds());
        if (height.size() == 1) {
            tv_altitudeAsend.setText(height.get(0) + "");
            tv_altitudeHigh.setText(height.get(0) + "");
            tv_altitudeLow.setText(height.get(0) + "");
        } else if (height.size() > 1) {
            tv_altitudeAsend.setText(height.get(0) + "");
            tv_altitudeHigh.setText(height.get(1) + "");
            tv_altitudeLow.setText((height.get(1) - height.get(0)) + " ");
        }
        tv_distance.setText((int) distance + " ");
        stepCount=(int) (distance / 0.4);
        tv_stepCount.setText(stepCount + " ");
        UserInfo userInfo = new UserInfo();
        userInfo.getUserInfo(getActivity());
        String ages = userInfo.getAge();
        int userAge = 0;
        try {
            userAge = Integer.parseInt(ages);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String weights = userInfo.getAge();
        int weight = 0;
        try {
            weight = Integer.parseInt(weights);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (distance > 0) {
            calories = (((double) userAge * 0.2017 + (double) weight * 0.09036 + distance / 1000.0 * 3600 * 1.8) - 55.0969) * seconds / 6.34;

        } else {
            calories = 0;
        }

        tv_calorie.setText((int) calories + "");
        tv_fat.setText((int) calories / 7 + "");
        tv_nutrition.setText((int) calories / 11 + "");


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

        mTVRunDistance.setText((int) distance + " ");
        stepCount=(int)(distance / 0.4) ;
        mTVRunWalkNum.setText((int) stepCount + " ");
        mTVRunCalorie.setText((int) calories + " ");
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

            setInitTitle();
            changeLayout();
        }

        @Override
        public void onRightListener(View v) {
            //TODO  完成 回到 Walk界面.

            if (getTitleRight().equals("骑行")) {
                Toast.makeText(getActivity(), "准备骑行", Toast.LENGTH_SHORT).show();
                isWalking = false;
                setInitTitle();

            } else if (getTitleRight().equals("步行")) {
                Toast.makeText(getActivity(), "开始步行", Toast.LENGTH_SHORT).show();
                isWalking = true;
                setInitTitle();

            }
            if (getTitleRight().equals("完成")) {
                Toast.makeText(getActivity(), "完成行驶", Toast.LENGTH_SHORT).show();
                changeLayout();

                uploadRunRecord();
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

    private void uploadRunRecord() {


        //得到传来的值
        String actId = myApp.getActId();
        if(actId==null){
            actId="0";
        }
        //TODO 发送数据到服务端
        Map<String, RequestBody> paramsMap = new HashMap<String, RequestBody>();
        paramsMap.put("userId", RequestBody.create(MediaType.parse("multipart/form-data"),GlobalParams.USER_ID));//用户ID ,不可空
        paramsMap.put("token", RequestBody.create(MediaType.parse("multipart/form-data"),GlobalParams.TOKEN));//访问凭证 不可空
        if (isWalking) {
            paramsMap.put("type", RequestBody.create(MediaType.parse("multipart/form-data"),"0"));//类型 : 0 步行，1骑行 ,不可空	type
        } else {
            paramsMap.put("type", RequestBody.create(MediaType.parse("multipart/form-data"),"1"));//类型 : 0 步行，1骑行 ,不可空	type
        }

        paramsMap.put("activity", RequestBody.create(MediaType.parse("multipart/form-data"),actId));//活动Id, 可空

        String title = TextUtils.isEmpty(bottom_title.getText().toString()) ? "0" : bottom_title.getText().toString();
        paramsMap.put("title",RequestBody.create(MediaType.parse("multipart/form-data"), title));//记录标题，可空

        paramsMap.put("stepCount", RequestBody.create(MediaType.parse("multipart/form-data"),0+""));//Integer  步数，可空
        paramsMap.put("distance", RequestBody.create(MediaType.parse("multipart/form-data"),0 + ""));//Integer 距离，可空
        String dura = TextUtils.isEmpty(tv_duration.getText().toString()) ? "0" : tv_duration.getText().toString();
        paramsMap.put("duration", RequestBody.create(MediaType.parse("multipart/form-data"),dura));//Integer 时长，可空
        String altitudeAsend = TextUtils.isEmpty(tv_altitudeAsend.getText().toString()) ? "0" : tv_altitudeAsend.getText().toString();
        paramsMap.put("altitudeAsend",RequestBody.create(MediaType.parse("multipart/form-data"), altitudeAsend));//海拔上升，可空
        String altitudeHigh = TextUtils.isEmpty(tv_altitudeHigh.getText().toString()) ? "0" : tv_altitudeHigh.getText().toString();
        paramsMap.put("altitudeHigh", RequestBody.create(MediaType.parse("multipart/form-data"),altitudeHigh));//最高海拔，可空
        String altitudeLow = TextUtils.isEmpty(tv_altitudeLow.getText().toString()) ? "0" : tv_altitudeLow.getText().toString();
        paramsMap.put("altitudeLow", RequestBody.create(MediaType.parse("multipart/form-data"),altitudeLow));//最低海拔，可空
        String calorie = TextUtils.isEmpty(tv_calorie.getText().toString()) ? "0" : tv_calorie.getText().toString();

        paramsMap.put("calorie",RequestBody.create(MediaType.parse("multipart/form-data"), calorie));//卡路里，可空
        String fat = TextUtils.isEmpty(tv_fat.getText().toString()) ? "0" : tv_fat.getText().toString();

        paramsMap.put("fat", RequestBody.create(MediaType.parse("multipart/form-data"),fat));//脂肪，可空
        String nutrition = TextUtils.isEmpty(tv_nutrition.getText().toString()) ? "0" : tv_nutrition.getText().toString();

        paramsMap.put("nutrition", RequestBody.create(MediaType.parse("multipart/form-data"),nutrition));//蛋白质，可空

        paramsMap.put("star", RequestBody.create(MediaType.parse("multipart/form-data"),0+""));//Double 星级，可空

        int ss=run_bottom_walked_stress_star.getStarSize();
        paramsMap.put("streetStar", RequestBody.create(MediaType.parse("multipart/form-data"),0+""));//Double 星级，可空
        int safeStar=run_bottom_walked_safety_star.getStarSize();
        paramsMap.put("safeStar", RequestBody.create(MediaType.parse("multipart/form-data"),0+""));//Double 星级，可空
        int es=run_bottom_walked_environment_star.getStarSize();
        paramsMap.put("envirStar", RequestBody.create(MediaType.parse("multipart/form-data"),0+""));//Double 星级，可空

        String introduction = TextUtils.isEmpty(bottom_intro.getText().toString()) ? "0" : bottom_intro.getText().toString();
        paramsMap.put("introduction", RequestBody.create(MediaType.parse("multipart/form-data"),introduction));//介绍，可空
        paramsMap.put("tags", RequestBody.create(MediaType.parse("multipart/form-data"),"0"));//记录标签，可空
        paramsMap.put("path", RequestBody.create(MediaType.parse("multipart/form-data")," 0"));//路径点集，可空
        paramsMap.put("location",RequestBody.create(MediaType.parse("multipart/form-data"), "0"));//步行记录位置

        if (pics != null) {
            for(int i=0;i<pics.size();i++){
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), pics.get(i));

                MultipartBody.Part  filePart = MultipartBody.Part.createFormData("viewUrls", picNames.get(i), requestFile);
                picParts.add(filePart);

            }


        } else {
            picParts = null;
        }

        NetRequestManager.getInstance().uploadWalkRecord(paramsMap, picParts,pathPic,new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
            }

        });
    }

    private void changeLayout() {
        setInitTitle();
        isRunDone = false;
        mChronometer.stop();
        seconds = 0;


        mLLBottomAllView.setVisibility(View.GONE);
        mLLBottomSmallView.setVisibility(View.GONE);
        mLLBottomData.setVisibility(View.GONE);
        mLLBottomDataPic.setVisibility(View.GONE);
        run_done_scroll.setVisibility(View.GONE);

        mIVRunStart.setVisibility(View.VISIBLE);
        mTVRunStart.setVisibility(View.VISIBLE);
        mImagePicker.clear();
        pics.clear();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }

        gv_pic.setVisibility(View.VISIBLE);
        mLLBottomPickPic.setVisibility(View.VISIBLE);
    }

    private void selectImage(){

        mImagePicker.setOnImagePickCompleteListener(this);
        mImagePicker.setSelectMode(AndroidImagePicker.Select_Mode.MODE_MULTI);
        mImagePicker.setShouldShowCamera(true);
        Intent intent = new Intent();
        intent.putExtra("isCrop", false);
        intent.setClass(getActivity(), ImagesGridActivity.class);
        EnterActUtils.startAct(mActivity, intent);

    }
    @Override
    public void onImagePickComplete(List<ImageItem> items) {
        pics.clear();
        if (items != null && items.size() > 0) {
            for (ImageItem item : items){
                pics.add(item.path);
                picNames.add(item.name);
            }
        }
         adapter = new PicAdapter(mContext,pics,R.layout.item_pic);
        gv_pic.setAdapter(adapter);
        gv_pic.setVisibility(View.VISIBLE);
        mLLBottomPickPic.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        seconds = 0;
    }
}
