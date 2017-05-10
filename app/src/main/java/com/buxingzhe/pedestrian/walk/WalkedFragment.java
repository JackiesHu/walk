package com.buxingzhe.pedestrian.walk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.buxingzhe.lib.util.Log;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseFragment;
import com.buxingzhe.pedestrian.bean.activity.WalkActivityInfo;
import com.buxingzhe.pedestrian.bean.walk.LatestActivityInfo;
import com.buxingzhe.pedestrian.bean.walk.WalkWeatherInfo;
import com.buxingzhe.pedestrian.common.Constant;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.community.community.CommActFragment;
import com.buxingzhe.pedestrian.community.community.CommActInfoActivity;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.service.StepService;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.JsonParseUtil;
import com.buxingzhe.pedestrian.utils.StepCountModeDispatcher;
import com.buxingzhe.pedestrian.widget.CalendarLayout;
import com.buxingzhe.pedestrian.widget.MaterialSpinnerLayout;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jeek.calendar.widget.calendar.OnCalendarClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;


/**
 * Created by quanjing on 2017/2/23.
 */
public class WalkedFragment extends BaseFragment implements Handler.Callback, View.OnClickListener {

    @BindView(R.id.walk_data_day)
    TextView walkDataDay;
    @BindView(R.id.walk_data_week)
    TextView walkDataWeek;
    @BindView(R.id.walk_data_month)
    TextView walkDataMonth;
    @BindView(R.id.walk_data_year)
    TextView walkDataYear;

    @BindView(R.id.walk_action_title)
    TextView walkActionTitle;
    @BindView(R.id.walk_action_time)
    TextView walkActionTime;
    @BindView(R.id.walk_action_pic)
    ImageView walkActionPic;
    @BindView(R.id.walk_action_content)
    TextView walkActionContent;
    @BindView(R.id.actionLl)
    LinearLayout actionLl;
    @BindView(R.id.stepCount)
    TextView stepCount;

    private ImageView mTitleCalendarImageView;
    private TextView mCurrentSelectedDate;
    private LineChartView mLineChartView;

    private MaterialSpinner mMaterialSpinner;
    private MaterialSpinnerLayout mMaterialSpinnerLayout;

    private CalendarLayout mCalendarLayout;

    private String[] WALK_SPINNER_DATA;

    //天气
    @BindView(R.id.walk_weather_tv_address)
    TextView mWeatherAddress;
    @BindView(R.id.walk_weather_tv_)
    TextView mWeather;
    @BindView(R.id.walk_weather_tv_air_quality)
    TextView mWeatherAirQuality;
    @BindView(R.id.walk_weather_tv_wear_suggest)
    TextView mWeatherWearSuggest;
    @BindView(R.id.walk_weather_tv_sport_suggest)
    TextView mWeatherSportSuggest;
    WalkActivityInfo walkActivitityInfo = new WalkActivityInfo();
    private LatestActivityInfo info;


    //计步
    private Handler delayHandler;
    //循环取当前时刻的步数中间的间隔时间
    private long TIME_INTERVAL = 500;
    private boolean isBind = false;
    private Messenger messenger;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));

    /**
     * 从service服务中拿到步数
     *
     * @param msg
     * @return
     */
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constant.MSG_FROM_SERVER:
                stepCount.setText(msg.getData().getInt("step")+"");
                delayHandler.sendEmptyMessageDelayed(Constant.REQUEST_SERVER, TIME_INTERVAL);

                break;
            case Constant.REQUEST_SERVER:
                try {
                    Message msg1 = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                    msg1.replyTo = mGetReplyMessenger;
                    messenger.send(msg1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_walk, container, false);
        mContext = getContext();
        ButterKnife.bind(this, view);
        findId(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delayHandler = new Handler(this);
        checkSensor();
    }

    private void checkSensor() {
        if (StepCountModeDispatcher.isSupportStepCountSensor(mContext)) {
            Toast.makeText(mContext, "计步中", Toast.LENGTH_SHORT).show();
            setupService();
        } else {
            Toast.makeText(mContext, "该设备不支持计步", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(mContext, StepService.class);
        isBind = mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        mContext.startService(intent);
    }


    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                messenger = new Messenger(service);
                Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                msg.replyTo = mGetReplyMessenger;
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onClick();
        setData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCurrentSelectedDate.setText(mCalendarLayout.getCurrrentSelectedDate());
    }

    private void findId(View view) {
        mCurrentSelectedDate = (TextView) view.findViewById(R.id.currentSelectedDate);
        mLineChartView = (LineChartView) view.findViewById(R.id.linechartview);
        mMaterialSpinner = (MaterialSpinner) view.findViewById(R.id.walk_spinner);
        mMaterialSpinnerLayout = (MaterialSpinnerLayout) view.findViewById(R.id.walk_spinner_layout);
        mCalendarLayout = (CalendarLayout) view.findViewById(R.id.walk_calendar_layout);
        mTitleCalendarImageView = (ImageView) view.findViewById(R.id.walk_title_calendar);

    }

    private void onClick() {
        mLineChartView.setOnValueTouchListener(new MineLineChartOnValueSelectListener());
        mMaterialSpinner.setOnItemSelectedListener(new MineOnItemSelectedListener());
        mCalendarLayout.setOnCalendarClickListener(new MineCalendarClickListener());
        mTitleCalendarImageView.setOnClickListener(this);
    }

    private void setData() {
        setWeatherData(true, "");
        setActionData();
        setSpinnerData();
        setChartData();
    }

    private void setActionData() {
        mSubscription = NetRequestManager.getInstance().getLatestActivity(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.i("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(e.getMessage());
            }

            @Override
            public void onNext(String jsonStr) {
                Log.i("jsonStr" + jsonStr);
               /* Gson gson = new Gson();
                Object[] datas = JsonParseUtil.getInstance().parseJson(jsonStr, LatestActivityInfo.class);
                String contentStr=(String) datas[1];
                walkActivitityInfo = gson.fromJson(contentStr, WalkActivityInfo.class);*/
                Gson gson2 = new Gson();
                info = gson2.fromJson(jsonStr, LatestActivityInfo.class);
                LatestActivityInfo.ContentBean content = info.getContent();

                if (null != content) {
                    walkActionTitle.setText(content.getTitle());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Long time = new Long(info.getContent().getStartTimeStamp());
                    String d = format.format(time);
                    walkActionTime.setText(d);

                    Glide.with(mContext).load(content.getBanner())
                            .error(R.drawable.default_img)
                            .into(walkActionPic);
                    walkActionContent.setText(content.getIntroduction());
                }


            }

        });
    }


    private void setWeatherData(boolean isCurrentWeatherData) {
        setWeatherData(isCurrentWeatherData, "");
    }


    private void setWeatherData(boolean isCurrentWeatherData, String date) {
        String cityName = "北京";//TODO
        if (isCurrentWeatherData) {
            mSubscription = NetRequestManager.getInstance().getCurrentWeather(cityName, new Subscriber<String>() {
                @Override
                public void onCompleted() {
                    Log.i("onCompleted");
                }

                @Override
                public void onError(Throwable e) {
                    Log.i(e.getMessage());
                }

                @Override
                public void onNext(String jsonStr) {
                    Log.i("jsonStr" + jsonStr);

                    // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                    Object[] datas = JsonParseUtil.getInstance().parseJson(jsonStr, WalkWeatherInfo.class);
                    if ((Integer) datas[0] == 0) {
                        Log.i(datas[1].toString());

                        WalkWeatherInfo content = (WalkWeatherInfo) datas[1];
                        if (content != null) {
                            Log.i(content.toString());
                            WalkedFragment.this.setWeatherData(content);
                        }

                    } else {
                        Toast.makeText(mContext, datas[2].toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            });
        } else {
            Log.i(date);
            mSubscription = NetRequestManager.getInstance().getHistoryWeather(cityName, date, new Subscriber<String>() {
                @Override
                public void onCompleted() {
                    Log.i("onCompleted");
                }

                @Override
                public void onError(Throwable e) {
                    Log.i(e.getMessage());
                }

                @Override
                public void onNext(String jsonStr) {
                    Log.i("jsonStr" + jsonStr);

                    // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                    Object[] datas = JsonParseUtil.getInstance().parseJson(jsonStr, WalkWeatherInfo.class);
                    if ((Integer) datas[0] == 0) {
                        Log.i(datas[1].toString());

                        WalkWeatherInfo content = (WalkWeatherInfo) datas[1];
                        if (content != null) {
                            Log.i(content.toString());
                            WalkedFragment.this.setWeatherData(content);
                        }

                    } else {
                        Toast.makeText(mContext, datas[2].toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
    }

    private void setWeatherData(WalkWeatherInfo content) {
        if (TextUtils.isEmpty(content.getCityName())) {
            return;
        }
        mWeatherAddress.setText(content.getCityName());
        mWeather.setText(content.getTempLow() + "~" + content.getTempHigh() + "  " + content.getWeather() + "  " + content.getWindDirection() + content.getWindDegree());
        mWeatherAirQuality.setText(content.getAirQuality());
        mWeatherWearSuggest.setText(content.getWearSuggest());
        mWeatherSportSuggest.setText(content.getSportSuggest());
    }

    /**
     * 初始化Spinner
     */
    private void setSpinnerData() {
        WALK_SPINNER_DATA = getActivity().getResources().getStringArray(R.array.walk_spinner);
        mMaterialSpinner.setItems(WALK_SPINNER_DATA);
    }

    /**
     * 初始化折线图
     */
    private void setChartData() {

        getMonthData();
        String[] XData = {"2017年3月", "2月", "1月", "2016年12月", "11月", "10月", "9月", "8月", "7月", "6月", "5月"};
        int[] YData = {1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 11000, 12000,};
//        String[] YData = {"5000","10000","15000","20000"};
        int[] score = {5200, 4000, 7000, 0, 2000, 3000, 10000, 12000, 15000};


        //1. 获取XY轴的标注
        List<AxisValue> mAxisXValue = new ArrayList<AxisValue>();
        for (int i = 0; i < XData.length; i++) {
            mAxisXValue.add(new AxisValue(i).setLabel(XData[i]));
        }
        List<AxisValue> mAxisYValue = new ArrayList<AxisValue>();
        for (int i = 0; i < YData.length; i++) {
            mAxisYValue.add(new AxisValue(i).setLabel(YData[i] + ""));
        }
        //2. 获取坐标点
        List<PointValue> mPointValue = new ArrayList<PointValue>();
        for (int i = 0; i < score.length; i++) {
            mPointValue.add(new PointValue(i, score[i]));
        }

        //3. 折线的属性
        // 创建折线并设置折线的颜色
        Line line = new Line(mPointValue).setColor(Color.parseColor("#00AE66"));
        //折线图上的每个数据点的形状。节点图形样式 DIAMOND菱形、SQUARE方形、CIRCLE圆形
        line.setShape(ValueShape.CIRCLE);
        //折线是否平滑，即是曲线还是折线。false：折线，true：曲线
        line.setCubic(false);
        //是否填充去信的面积
        line.setFilled(false);
        //曲线的数据坐标是否加上备注
        line.setHasLabels(true);
        // 隐藏数据，触摸可以显示。（设置了这个属性后，那么 setHasLabels()方法就会无效）
        line.setHasLabelsOnlyForSelected(false);
        //是否用线显示。false: 只有点显示，没有曲线
        line.setHasLines(true);
        //是否显示原点。false：没有原点只有点显示 (每个数据点都是个大的圆点)
        line.setHasPoints(true);
        //设置折线的颜色
        //line.setColor(Color.BLACK);
        //设置折线的宽度
        //line.setStrokeWidth(2);
        //设置节点颜色
        //line.setPointColor(Color.BLACK);
        //设置节点半径
        //line.setPointRadius(3);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData lineChartData = new LineChartData();
        lineChartData.setLines(lines);


        //4. 坐标轴
        Axis axisX = new Axis();//X轴
        //X坐标轴字体是 斜的显示还是直的显示， true：斜的显示
        axisX.setHasTiltedLabels(false);
        //X坐标轴字体颜色
        axisX.setTextColor(Color.parseColor("#D7D7D7"));
        //X坐标轴字体大小
        axisX.setTextSize(13);
        //X坐标轴 表格名称
        //axisX.setName("日期");
        //最多几个X轴坐标，就是说：缩放让X轴上显示数据的个数（7<= x <= mAxisXValues.length）
        axisX.setMaxLabelChars(8);
        //填充X轴坐标上 原点的值
        axisX.setValues(mAxisXValue);
        //X轴网格线
        axisX.setHasLines(false);
        //X轴的颜色
        axisX.setLineColor(Color.parseColor("#DDDDDD"));
        //X轴显示的数值在外部显示
        axisX.setInside(false);


        //Y轴
        Axis axisY = new Axis();
        //Y轴标注
        //axisY.setName("步数");
        //Y轴字体大小
        axisY.setTextSize(13);
        //Y轴字体颜色
        axisY.setTextColor(Color.parseColor("#CCCCCC"));
        //Y轴网格线
        axisY.setHasLines(true);
        //Y轴分割线（就是坐标轴是否显示）
        axisY.setHasSeparationLine(false);
        //填充Y轴坐标上 原点的值
        axisY.setValues(mAxisYValue);
        //Y轴数值自动生成 (需要在setValues()方法之后调用才起作用)
        axisY.setAutoGenerated(true);
        //Y轴显示的数值在内部显示
        axisY.setInside(true);


        //X轴坐标显示在底部
        lineChartData.setAxisXBottom(axisX);
        //Y轴坐标显示在左边
        lineChartData.setAxisYLeft(axisY);


        //5.设置LineChartData属性
        //数据背景颜色
        lineChartData.setValueLabelBackgroundColor(Color.WHITE);
        //数据背景颜色和节点颜色是否一致
        //lineChartData.setValueLabelBackgroundAuto(true);
        //是否有数据背景颜色
        lineChartData.setValueLabelBackgroundEnabled(true);


        //6.设置行为属性，支持缩放／滑动以及平移
        //LineChartView 与 用户是否可交互
        mLineChartView.setInteractive(true);
        //缩放的方向
        mLineChartView.setZoomType(ZoomType.HORIZONTAL);
        //最大缩放比例
        mLineChartView.setMaxZoom(4.0f);
        mLineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        mLineChartView.setLineChartData(lineChartData);
        mLineChartView.setVisibility(View.VISIBLE);

        //7.X轴固定显示数据项
        Viewport viewport = new Viewport(mLineChartView.getMaximumViewport());
        viewport.left = 0; //代表显示图表起始x轴坐标，0表示第一个
        viewport.right = 6; //代表显示图表结束x轴坐标，6表示第7个
        mLineChartView.setCurrentViewport(viewport);
        //8.如何设置以Y轴最小值0开始 显示折线？


    }

    private void getMonthData() {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date mBefore;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,-30);//往上推一天  30推三十天  365推一年
        mBefore = calendar.getTime();
        sdf.format(mBefore);

        //Date或者String转化为时间戳
        String time="1970-01-06 11:45:55";
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<>();
        params.put("beginDate", date.toString());
        params.put("userId", GlobalParams.USER_ID);
        params.put("token",  GlobalParams.TOKEN);
        mSubscription = NetRequestManager.getInstance().queryWalkRecordByDay(params, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.i("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(e.getMessage());
            }

            @Override
            public void onNext(String jsonStr) {
                Log.i("walk--jsonStr" + jsonStr);


            }

        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }


    @OnClick({R.id.walk_data_day, R.id.walk_data_week, R.id.walk_data_month, R.id.walk_data_year, R.id.actionLl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.walk_title_calendar://title calendar
                mCalendarLayout.skipTodayCalendar();
                break;
            case R.id.walk_data_day:
                walkDataDay.setSelected(true);
                walkDataMonth.setSelected(false);
                walkDataWeek.setSelected(false);
                walkDataYear.setSelected(false);
                mLineChartView.refreshDrawableState();

                break;
            case R.id.walk_data_week:
                walkDataDay.setSelected(false);
                walkDataMonth.setSelected(false);
                walkDataWeek.setSelected(true);
                walkDataYear.setSelected(false);
                break;
            case R.id.walk_data_month:
                walkDataDay.setSelected(false);
                walkDataMonth.setSelected(true);
                walkDataWeek.setSelected(false);
                walkDataYear.setSelected(false);
                mLineChartView.refreshDrawableState();
                break;
            case R.id.walk_data_year:
                walkDataDay.setSelected(false);
                walkDataMonth.setSelected(false);
                walkDataWeek.setSelected(false);
                walkDataYear.setSelected(true);
                mLineChartView.refreshDrawableState();
                break;
            case R.id.actionLl:
                LatestActivityInfo.ContentBean content = info.getContent();
                walkActivitityInfo.setId(content.getId());
                walkActivitityInfo.setBanner(content.getBanner());
                walkActivitityInfo.setIntroduction(content.getIntroduction());
                walkActivitityInfo.setIsOutDate(content.getIsOutDate());
                walkActivitityInfo.setTitle(content.getTitle());

             /*   walkActivitityInfo.setCreateTimestamp((int)content.getCreateTimestamp());
                walkActivitityInfo.setEndTimestamp((int)content.getEndTimestamp());
                PublisherBean publisher=new PublisherBean();
                publisher.setAvatarUrl(content.getPublisher().getAvatar());
                publisher.setId(content.getPublisher().getId());
                publisher.setNickName(content.getPublisher().getNickName());
                walkActivitityInfo.setPublisher(publisher);*/
                Intent intent = new Intent();
                intent.setClass(mContext, CommActInfoActivity.class);
                intent.putExtra(CommActFragment.WALKACTIVITYINFO, walkActivitityInfo);
                EnterActUtils.startAct(getActivity(), intent);
                break;
        }
    }


    private class MineLineChartOnValueSelectListener implements LineChartOnValueSelectListener {
        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(mContext, value.getY() + "", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

        }
    }

    private class MineOnItemSelectedListener implements MaterialSpinner.OnItemSelectedListener<String> {
        @Override
        public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
            Toast.makeText(mContext, item, Toast.LENGTH_SHORT).show();
            mMaterialSpinnerLayout.setArrow(false);
        }
    }

    private class MineCalendarClickListener implements OnCalendarClickListener {
        @Override
        public void onClickDate(int year, int month, int day) {
            mCalendarLayout.setSelectPosition(mCalendarLayout.getPosition(year, month, day));
            mCurrentSelectedDate.setText(mCalendarLayout.getCurrrentSelectedDate());
            String date = year + (String.valueOf(month).length() == 1 ? ("0" + String.valueOf(month)) : String.valueOf(month)) + (String.valueOf(day).length() == 1 ? ("0" + String.valueOf(day)) : String.valueOf(day));
            setWeatherData(false, date);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            mContext.unbindService(conn);
        }
    }
}
