package com.buxingzhe.pedestrian.run;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.DistanceRequest;
import com.baidu.trace.api.track.DistanceResponse;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.LatestPoint;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.SortType;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;
import com.baidu.trace.model.TransportMode;
import com.buxingzhe.pedestrian.activity.BaseFragment;
import com.buxingzhe.pedestrian.application.PDApplication;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.utils.map.CommonUtil;
import com.buxingzhe.pedestrian.utils.map.Constants;
import com.buxingzhe.pedestrian.utils.map.MapUtil;
import com.buxingzhe.pedestrian.utils.map.TrackReceiver;
import com.buxingzhe.pedestrian.utils.map.ViewUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by chinaso on 2017/7/26.
 */

public class RouteFragment extends BaseFragment implements SensorEventListener {

    private PDApplication trackApp = null;

    private ViewUtil viewUtil = null;

    private Button traceBtn = null;

    private Button gatherBtn = null;

    private PowerManager powerManager = null;

    private PowerManager.WakeLock wakeLock = null;

    private TrackReceiver trackReceiver = null;

    private SensorManager mSensorManager;

    private Double lastX = 0.0;
    private int pageIndex = 1;
    private int mCurrentDirection = 0;

    /**
     * 地图工具
     */
    protected MapUtil mapUtil = null;

    /**
     * 轨迹服务监听器
     */
    private OnTraceListener traceListener = null;

    /**
     * 轨迹监听器(用于接收纠偏后实时位置回调)
     */
    private OnTrackListener trackListener = null;

    /**
     * Entity监听器(用于接收实时定位回调)
     */
    private OnEntityListener entityListener = null;

    /**
     * 实时定位任务
     */
    private RealTimeHandler realTimeHandler = new RealTimeHandler();

    private RealTimeLocRunnable realTimeLocRunnable = null;

    /**
     * 打包周期
     */
    public int packInterval = Constants.DEFAULT_PACK_INTERVAL;
    private long startTime = CommonUtil.getCurrentTime();//查询轨迹的开始时间
    private long endTime = CommonUtil.getCurrentTime();// 查询轨迹的结束时间


    /**
     * 轨迹点集合
     */
    private List<LatLng> trackPoints;

    private boolean firstLocate = true;

    public double distance=0;//以千米为单位
    public int stepCount=0;
    public List<Integer> height=new ArrayList<>();
    //View
    // 地图View相关
    protected MapView mMapView;
    protected BaiduMap mBaiduMap;
    protected ImageView mIVRunStart;
    // 轨迹相关
    public static boolean isRecording = false;
    public static boolean isWalking = true;
    boolean isRecodStart = false;
    protected String mapViewName;
    protected File mapFile;
    private HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest();//历史轨迹请求

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findId();
    }

    private void findId() {
        mBaiduMap = mMapView.getMap();
        mMapView.setMinimumHeight(SystemUtils.dip2px(getContext(), 300.0f));
        mMapView.removeViewAt(1); // 去掉百度logo
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(30.0f);// 17 -- 100米；  20 -- 10米
        mBaiduMap.animateMapStatus(msu);
        mapUtil.init(mMapView);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initsetting();
    }

    private void initsetting() {

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

        mapUtil.onResume();


        // 在Android 6.0及以上系统，若定制手机使用到doze模式，请求将应用添加到白名单。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = trackApp.getPackageName();
            boolean isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName);
            if (!isIgnoring) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    protected void stopRun() {
        // 停止采集
        trackApp.isTraceStarted = false;
        trackApp.mClient.stopTrace(trackApp.mTrace, traceListener);//停止服务
        trackApp.mClient.stopGather(traceListener);
        mSensorManager.unregisterListener(this);
    }


    protected void mapStartRun() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
        trackApp.mClient.startTrace(trackApp.mTrace, traceListener);//开始服务
        trackApp.mClient.setInterval(Constants.DEFAULT_GATHER_INTERVAL, packInterval);
        trackApp.mClient.startGather(traceListener);//开启采集
        trackApp.isTraceStarted = true;
        if (Constants.DEFAULT_PACK_INTERVAL != packInterval) {
            stopRealTimeLoc();
            startRealTimeLoc(packInterval);
        }
        startTime = CommonUtil.getCurrentTime();

        if (trackApp.trackConf.contains("is_trace_started")
                && trackApp.trackConf.contains("is_gather_started")
                && trackApp.trackConf.getBoolean("is_trace_started", false)
                && trackApp.trackConf.getBoolean("is_gather_started", false)) {
            startRealTimeLoc(packInterval);
        } else {
            startRealTimeLoc(Constants.LOC_INTERVAL);
        }
        queryHistoryTrack();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapUtil.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopRealTimeLoc();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRealTimeLoc();
        trackPoints.clear();
        trackPoints = null;

    }

    private void init() {
        initListener();
        trackApp = (PDApplication) getActivity().getApplicationContext();
        viewUtil = new ViewUtil();
        mapUtil = MapUtil.getInstance();
        mapUtil.setCenter(mCurrentDirection);//设置地图中心点
        powerManager = (PowerManager) trackApp.getSystemService(Context.POWER_SERVICE);
        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);// 获取传感器管理服务

        trackPoints = new ArrayList<>();
    }

    private void initListener() {
        trackListener = new OnTrackListener() {

            @Override
            public void onLatestPointCallback(LatestPointResponse response) {
                //经过服务端纠偏后的最新的一个位置点，回调
                try {
                    if (StatusCodes.SUCCESS != response.getStatus()) {
                        return;
                    }

                    LatestPoint point = response.getLatestPoint();
                    if (null == point || CommonUtil.isZeroPoint(point.getLocation().getLatitude(), point.getLocation()
                            .getLongitude())) {
                        return;
                    }

                    LatLng currentLatLng = mapUtil.convertTrace2Map(point.getLocation());
                    if (null == currentLatLng) {
                        return;
                    }

                    if (firstLocate) {
                        firstLocate = false;
                        Toast.makeText(getActivity(), "起点获取中，请稍后...", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //当前经纬度
                    CurrentLocation.locTime = point.getLocTime();
                    CurrentLocation.latitude = currentLatLng.latitude;
                    CurrentLocation.longitude = currentLatLng.longitude;

                    if (trackPoints == null) {
                        return;
                    }
                    trackPoints.add(currentLatLng);
                    mapUtil.drawHistoryTrack(trackPoints, false, mCurrentDirection);//时时动态的画出运动轨迹
                } catch (Exception x) {

                }


            }

            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                try {
                    int total = response.getTotal();
                    if (StatusCodes.SUCCESS != response.getStatus()) {
                    } else if (0 == total) {
                    } else {
                        List<TrackPoint> points = response.getTrackPoints();
                        if (null != points) {
                            for (TrackPoint trackPoint : points) {
                                if (!CommonUtil.isZeroPoint(trackPoint.getLocation().getLatitude(),
                                        trackPoint.getLocation().getLongitude())) {
                                   // trackPoints.add(MapUtil.convertTrace2Map(trackPoint.getLocation()));
                                }
                            }
                        }
                    }

                    //查找下一页数据
                    if (total > Constants.PAGE_SIZE * pageIndex) {
                        historyTrackRequest.setPageIndex(++pageIndex);
                        queryHistoryTrack();
                    } else {
                       // mapUtil.drawHistoryTrack(trackPoints, true, 0);//画轨迹
                    }

                    queryDistance();// 查询里程
                } catch (Exception e) {

                }
            }

            @Override
            public void onDistanceCallback(DistanceResponse response) {
                viewUtil.showToast(getActivity(), "里程：" + response.getDistance());
                super.onDistanceCallback(response);
                if (StatusCodes.SUCCESS == response.getStatus()) {
                    distance=response.getDistance();
                    distance=distance/1000;
                }
            }


        };

        entityListener = new OnEntityListener() {

            @Override
            public void onReceiveLocation(TraceLocation location) {
                //本地LBSTraceClient客户端获取的位置
                try {
                    if (StatusCodes.SUCCESS != location.getStatus() || CommonUtil.isZeroPoint(location.getLatitude(),
                            location.getLongitude())) {
                        return;
                    }
                    LatLng currentLatLng = mapUtil.convertTraceLocation2Map(location);
                    if (null == currentLatLng) {
                        return;
                    }
                    CurrentLocation.locTime = CommonUtil.toTimeStamp(location.getTime());
                    CurrentLocation.latitude = currentLatLng.latitude;
                    CurrentLocation.longitude = currentLatLng.longitude;

                    if (null != mapUtil) {
                        mapUtil.updateMapLocation(currentLatLng, mCurrentDirection);//显示当前位置
                        mapUtil.animateMapStatus(currentLatLng);//缩放
                    }

                } catch (Exception x) {

                }


            }

        };

        traceListener = new OnTraceListener() {

            @Override
            public void onStartTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= errorNo) {
                    trackApp.isTraceStarted = true;
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.putBoolean("is_trace_started", true);
                    editor.apply();
                    registerReceiver();
                }
                viewUtil.showToast(getActivity(),
                        String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onStopTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.CACHE_TRACK_NOT_UPLOAD == errorNo) {
                    trackApp.isTraceStarted = false;
                    trackApp.isGatherStarted = false;
                    // 停止成功后，直接移除is_trace_started记录（便于区分用户没有停止服务，直接杀死进程的情况）
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.remove("is_trace_started");
                    editor.remove("is_gather_started");
                    editor.apply();

                    unregisterPowerReceiver();
                    firstLocate = true;
                }
                viewUtil.showToast(getActivity(),
                        String.format("onStopTraceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onStartGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STARTED == errorNo) {
                    trackApp.isGatherStarted = true;
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.putBoolean("is_gather_started", true);
                    editor.apply();

                    stopRealTimeLoc();
                    startRealTimeLoc(packInterval);
                }
                viewUtil.showToast(getActivity(),
                        String.format("onStartGatherCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onStopGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STOPPED == errorNo) {
                    trackApp.isGatherStarted = false;
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.remove("is_gather_started");
                    editor.apply();


                    firstLocate = true;
                    stopRealTimeLoc();
                    startRealTimeLoc(Constants.LOC_INTERVAL);

                    if (trackPoints.size() >= 1) {
                        try {
                            mapUtil.drawEndPoint(trackPoints.get(trackPoints.size() - 1));
                        } catch (Exception e) {

                        }

                    }

                }
                viewUtil.showToast(getActivity(),
                        String.format("onStopGatherCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onPushCallback(byte messageType, PushMessage pushMessage) {

            }
        };
    }

    /**
     * 查询历史轨迹
     */
    private void queryHistoryTrack() {
        System.out.println("draw--queryHistoryTrack");
        trackApp.initRequest(historyTrackRequest);
        endTime = CommonUtil.getCurrentTime();
        historyTrackRequest.setSupplementMode(SupplementMode.no_supplement);
        historyTrackRequest.setSortType(SortType.asc);
        historyTrackRequest.setCoordTypeOutput(CoordType.bd09ll);
        historyTrackRequest.setEntityName(trackApp.entityName);
        historyTrackRequest.setStartTime(startTime);
        historyTrackRequest.setEndTime(endTime);
        historyTrackRequest.setPageIndex(pageIndex);
        historyTrackRequest.setPageSize(Constants.PAGE_SIZE);

        ProcessOption processOption = new ProcessOption();
        processOption.setRadiusThreshold(30);
        processOption.setTransportMode(TransportMode.walking);
        processOption.setNeedDenoise(true);
        processOption.setNeedMapMatch(true);// 绑路
        processOption.setNeedVacuate(true);
        processOption.setTransportMode(TransportMode.walking);// 交通方式为步行
        historyTrackRequest.setProcessOption(processOption);// 设置纠偏选项
        historyTrackRequest.setProcessed(true);// 设置纠偏选项
        historyTrackRequest.setSupplementMode(SupplementMode.no_supplement);// 里程填充方式
        trackApp.mClient.queryHistoryTrack(historyTrackRequest, trackListener);
    }


    private void queryDistance() {
        DistanceRequest distanceRequest = new DistanceRequest(trackApp.getTag(), trackApp.serviceId, trackApp.entityName);
        distanceRequest.setStartTime(startTime);// 设置开始时间
        distanceRequest.setEndTime(endTime);// 设置结束时间
        distanceRequest.setProcessed(true);// 纠偏
        ProcessOption processOption = new ProcessOption();// 创建纠偏选项实例
        processOption.setNeedDenoise(true);// 去噪
        processOption.setNeedMapMatch(true);// 绑路
        processOption.setTransportMode(TransportMode.walking);// 交通方式为步行
        distanceRequest.setProcessOption(processOption);// 设置纠偏选项
        distanceRequest.setSupplementMode(SupplementMode.no_supplement);// 里程填充方式为无
        trackApp.mClient.queryDistance(distanceRequest, trackListener);// 查询里程
    }
    static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //每次方向改变，重新给地图设置定位数据，用上一次的经纬度
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {// 方向改变大于1度才设置，以免地图上的箭头转动过于频繁
            mCurrentDirection = (int) x;
            if (!CommonUtil.isZeroPoint(CurrentLocation.latitude, CurrentLocation.longitude)) {
                mapUtil.updateMapLocation(new LatLng(CurrentLocation.latitude, CurrentLocation.longitude), (float) mCurrentDirection);
            }
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 实时定位任务
     */
    class RealTimeLocRunnable implements Runnable {

        private int interval = 0;

        public RealTimeLocRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            trackApp.getCurrentLocation(entityListener, trackListener);
            realTimeHandler.postDelayed(this, interval * 1000);
        }
    }

    public void startRealTimeLoc(int interval) {
        realTimeLocRunnable = new RealTimeLocRunnable(interval);
        realTimeHandler.post(realTimeLocRunnable);
    }

    public void stopRealTimeLoc() {
        if (null != realTimeHandler && null != realTimeLocRunnable) {
            realTimeHandler.removeCallbacks(realTimeLocRunnable);
        }
       // trackApp.mClient.stopRealTimeLoc();
    }

    /**
     * 注册广播（电源锁、GPS状态）
     */
    private void registerReceiver() {
        if (trackApp.isRegisterReceiver) {
            return;
        }

        if (null == wakeLock) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "track upload");
        }
        if (null == trackReceiver) {
            trackReceiver = new TrackReceiver(wakeLock);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(StatusCodes.GPS_STATUS_ACTION);
        trackApp.registerReceiver(trackReceiver, filter);
        trackApp.isRegisterReceiver = true;

    }

    private void unregisterPowerReceiver() {
        if (!trackApp.isRegisterReceiver) {
            return;
        }
        if (null != trackReceiver) {
            trackApp.unregisterReceiver(trackReceiver);
        }
        trackApp.isRegisterReceiver = false;
    }
}
