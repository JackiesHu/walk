package com.buxingzhe.pedestrian.run;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
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
import com.baidu.trace.api.track.LatestPointRequest;
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
import com.buxingzhe.lib.util.NetUtil;
import com.buxingzhe.pedestrian.activity.BaseFragment;
import com.buxingzhe.pedestrian.application.PDApplication;
import com.buxingzhe.pedestrian.listen.OnInteractionData;
import com.buxingzhe.pedestrian.utils.FileConfig;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.utils.map.BitmapUtil;
import com.buxingzhe.pedestrian.utils.map.CommonUtil;
import com.buxingzhe.pedestrian.utils.map.Constants;
import com.buxingzhe.pedestrian.utils.map.MapUtil;
import com.buxingzhe.pedestrian.utils.map.ViewUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;
import static com.buxingzhe.pedestrian.utils.map.Constants.DEFAULT_RADIUS_THRESHOLD;
import com.buxingzhe.pedestrian.utils.map.ViewUtil;

public class RunRunFragment extends BaseFragment implements SensorEventListener {

    private PDApplication trackApp = null;

    protected MapUtil mapUtil = null;
    //private RefreshThread refreshThread = null;  //刷新地图线程以获取实时点

    /**
     * 轨迹服务监听器
     */
    private OnTraceListener mTraceListener = null;
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
    /**
     * 轨迹点集合
     */
    private List<LatLng> trackPoints = new ArrayList<>();

    private HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest();//历史轨迹请求
    private DistanceRequest distanceRequest = new DistanceRequest();
    private LatestPointRequest latestQequest = new LatestPointRequest();
    private int pageIndex = 1;

    private long startTime = CommonUtil.getCurrentTime();//查询轨迹的开始时间

    private long endTime = CommonUtil.getCurrentTime();// 查询轨迹的结束时间

    private SortType sortType = SortType.asc;//轨迹排序规则(倒序或者顺序)

    private OnInteractionData mOnInteractionData;
    private RefreshThread refreshThread = null;  //刷新地图线程以获取实时点

    public double distance = 0;//以千米为单位
    public int stepCount = 0;
    public List<Integer> height = new ArrayList<>();

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
    private int mCurrentDirection = 0;
    private Double lastX = 0.0;
    private SensorManager mSensorManager;
    private ViewUtil viewUtil = null;
    private boolean firstLocate = true;

    public void setOnInteractionData(OnInteractionData onInteractionData) {
        mOnInteractionData = onInteractionData;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        BitmapUtil.init();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        viewUtil = new ViewUtil();
        trackApp = (PDApplication) getActivity().getApplicationContext();
        mapUtil = MapUtil.getInstance();
     //   mapUtil.setCenter(mCurrentDirection);//设置地图中心点
        initListener();
        mSensorManager = (SensorManager) trackApp.getSystemService(SENSOR_SERVICE);// 获取传感器管理服务
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findId();
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

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        mapUtil.onPause();
    }

    private void initListener() {

        // 初始化轨迹服务监听器
        mTraceListener = new OnTraceListener() {
            // 开启服务回调
            @Override
            public void onStartTraceCallback(int status, String message) {
                if (StatusCodes.SUCCESS == status || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= status) {
                    trackApp.isTraceStarted = true;
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.putBoolean("is_trace_started", true);
                    editor.apply();
                }

                if(StatusCodes.SUCCESS == status ){
                    startRefreshThread(true);
                }else{
                    viewUtil.showToast(getActivity(),
                            String.format("服务启动失败, errorNo:%d, message:%s --请检查网络和GPS", status, message));
                }


            }


            // 停止服务回调
            @Override
            public void onStopTraceCallback(int status, String message) {
                if (StatusCodes.SUCCESS == status || StatusCodes.CACHE_TRACK_NOT_UPLOAD == status) {
                    trackApp.isTraceStarted = false;
                    trackApp.isGatherStarted = false;
                    // 停止成功后，直接移除is_trace_started记录（便于区分用户没有停止服务，直接杀死进程的情况）
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.remove("is_trace_started");
                    editor.remove("is_gather_started");
                    editor.apply();
                }

                startRefreshThread(false);

                getMapView();

            }

            private void getMapView() {
                mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {

                        mapViewName = System.currentTimeMillis() + ".png";
                        mapFile = new File(FileConfig.IMAGE_UP_PATH + mapViewName);
                        FileOutputStream out;
                        try {
                            out = new FileOutputStream(mapFile);
                            if (bitmap.compress(
                                    Bitmap.CompressFormat.PNG, 100, out)) {
                                out.flush();
                                out.close();
                            }
                            Toast.makeText(getActivity(),
                                    "屏幕截图成功，图片存在: " + mapFile.toString(),
                                    Toast.LENGTH_SHORT).show();
                        } catch (FileNotFoundException e) {
                            Toast.makeText(getActivity(),
                                    "屏幕截图不成功，无法上传路线图 ",
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            // 开启采集回调
            @Override
            public void onStartGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STARTED == errorNo) {
                    trackApp.isGatherStarted = true;
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.putBoolean("is_gather_started", true);
                    editor.apply();
                }
                if(StatusCodes.SUCCESS != errorNo ){
                    viewUtil.showToast(getActivity(),
                            String.format("数据采集失败, errorNo:%d, message:%s 请检查网络和GPS", errorNo, message));
                }


            }

            // 停止采集回调
            @Override
            public void onStopGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STOPPED == errorNo) {
                    trackApp.isGatherStarted = false;
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.remove("is_gather_started");
                    editor.apply();
                }

                if (trackPoints.size() >= 1) {
                    try {
                        mapUtil.drawEndPoint(trackPoints.get(trackPoints.size() - 1));
                    } catch (Exception e) {

                    }

                }

            }

            // 推送回调
            @Override
            public void onPushCallback(byte messageNo, PushMessage message) {
            }
        };


        trackListener = new OnTrackListener() {

            @Override
            public void onLatestPointCallback(LatestPointResponse response) {

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
                if(firstLocate){
                    currentLatLng=null;
                    firstLocate = false;
                    Toast.makeText(getActivity(),"起点获取中，请稍后...",Toast.LENGTH_SHORT).show();
                    return;
                }

                CurrentLocation.locTime = point.getLocTime();
                CurrentLocation.latitude = currentLatLng.latitude;
                CurrentLocation.longitude = currentLatLng.longitude;
                if (trackPoints == null) {
                    return;
                }
                trackPoints.add(currentLatLng);

                mapUtil.drawHistoryTrack(trackPoints, false, mCurrentDirection);//显示当前位置，并时时动态的画出运动轨迹
/*
                if (null != mapUtil) {
                    mapUtil.updateStatus(currentLatLng, true);
                }*/


            }

            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                int total = response.getTotal();
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    ViewUtil.showToast(getActivity(), response.getMessage());
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

                if (total > Constants.PAGE_SIZE * pageIndex) {
                    historyTrackRequest.setPageIndex(++pageIndex);
                    queryHistoryTrack();
                } else {
                    if (trackPoints.size() > 0) {
                        //   mapUtil.drawHistoryTrack(trackPoints, sortType);
                    }

                }
            }

            @Override
            public void onDistanceCallback(DistanceResponse response) {
                super.onDistanceCallback(response);
                if (StatusCodes.SUCCESS == response.getStatus()) {
                    distance = response.getDistance();
                    distance = distance / 1000;
                } else {
                }


            }

        };
        entityListener = new OnEntityListener() {

            @Override
            public void onReceiveLocation(TraceLocation location) {


                if (StatusCodes.SUCCESS != location.getStatus() || CommonUtil.isZeroPoint(location.getLatitude(),
                        location.getLongitude())) {
                    return;
                }
                LatLng currentLatLng = mapUtil.convertTraceLocation2Map(location);
                if (null == currentLatLng) {
                    return;
                }

                height.add(location.getAltitude());
                CurrentLocation.locTime = CommonUtil.toTimeStamp(location.getTime());
                CurrentLocation.latitude = currentLatLng.latitude;
                CurrentLocation.longitude = currentLatLng.longitude;

                if (null != mapUtil) {
                   // mapUtil.updateStatus(currentLatLng, true);
                   // mapUtil.animateMapStatus(currentLatLng);//缩放
                    mapUtil.updateMapLocation(currentLatLng, mCurrentDirection);//显示当前位置
                }
            }

        };

    }


    /**
     * 启动刷新线程
     *
     * @param isStart
     */
    private void startRefreshThread(boolean isStart) {

        if (refreshThread == null) {
            refreshThread = new RefreshThread();
        }

        refreshThread.refresh = isStart;

        if (isStart) {
            if (!refreshThread.isAlive()) {
                refreshThread.start();
            }
        } else {
            refreshThread = null;
        }


    }


    private void findId() {

        mBaiduMap = mMapView.getMap();
        mMapView.setMinimumHeight(SystemUtils.dip2px(getContext(), 300.0f));
        mMapView.removeViewAt(1); // 去掉百度logo
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);// 17 -- 100米；  20 -- 10米
        mBaiduMap.animateMapStatus(msu);
        mapUtil.init(mMapView);
        mapUtil.setCenter(trackApp);
        startRealTimeLoc(Constants.LOC_INTERVAL);

        mIVRunStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开启采集
                mapStartRun();

            }

        });

    }

    protected void stopRun() {
        // 停止采集
        trackApp.isTraceStarted = false;
        trackApp.mClient.stopTrace(trackApp.mTrace, mTraceListener);
        trackApp.mClient.stopGather(mTraceListener);
        startRefreshThread(false);

    }


    protected void mapStartRun() {

            if(trackPoints!=null){
                trackPoints.clear();
            }
            trackApp.isTraceStarted = true;
            trackApp.mClient.startTrace(trackApp.mTrace, mTraceListener);
            trackApp.mClient.startGather(mTraceListener);
            if (Constants.DEFAULT_PACK_INTERVAL != packInterval) {
                stopRealTimeLoc();
                startRealTimeLoc(packInterval);
            }
            startRefreshThread(true);
            startTime = CommonUtil.getCurrentTime();

    }


    /**
     * 查询历史轨迹
     */
    private void queryHistoryTrack() {
        initsetting();
        endTime = CommonUtil.getCurrentTime();
        trackApp.initRequest(historyTrackRequest);
        trackApp.initRequest(distanceRequest);
        trackApp.initRequest(latestQequest);

        historyTrackRequest.setEntityName(trackApp.entityName);
        historyTrackRequest.setStartTime(startTime);
        historyTrackRequest.setEndTime(endTime);
        historyTrackRequest.setPageIndex(pageIndex);
        historyTrackRequest.setPageSize(Constants.PAGE_SIZE);
        trackApp.mClient.queryHistoryTrack(historyTrackRequest, trackListener);


        distanceRequest.setEntityName(trackApp.entityName);
        distanceRequest.setStartTime(startTime);
        distanceRequest.setEndTime(endTime);
        trackApp.mClient.queryDistance(distanceRequest, trackListener);

        latestQequest.setEntityName(trackApp.entityName);
        trackApp.mClient.queryLatestPoint(latestQequest, trackListener);
    }

    private void initsetting() {
        // 设置需要纠偏
        distanceRequest.setProcessed(true);
        historyTrackRequest.setProcessed(true);

        ProcessOption processOption = new ProcessOption();// 创建纠偏选项实例
        processOption.setNeedDenoise(true);// 设置需要去噪
        processOption.setNeedVacuate(true);
        processOption.setNeedMapMatch(true);// 设置需要绑路
        processOption.setRadiusThreshold(DEFAULT_RADIUS_THRESHOLD);
        processOption.setTransportMode(TransportMode.walking);// 设置交通方式为

        distanceRequest.setProcessOption(processOption);// 设置纠偏选项
        historyTrackRequest.setProcessOption(processOption);// 设置纠偏选项

        if (isWalking) {
            historyTrackRequest.setSupplementMode(SupplementMode.walking);// 设置里程填充方式为步行
            distanceRequest.setSupplementMode(SupplementMode.walking);// 设置里程填充方式为步行
        } else {
            historyTrackRequest.setSupplementMode(SupplementMode.riding);// 设置里程填充方式为骑车
            distanceRequest.setSupplementMode(SupplementMode.riding);// 设置里程填充方式为骑车
        }

        //查询服务端纠偏后的最新轨迹点请求参数类
        latestQequest.setProcessOption(processOption);//设置参数
    }


    @Override
    public void onDetach() {
        super.onDetach();
        // 停止服务
        trackApp.mClient.stopTrace(trackApp.mTrace, mTraceListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();

        stopRealTimeLoc();
        mSensorManager.unregisterListener(this);
        trackPoints.clear();
        trackPoints = null;
        mapUtil.clear();
    }

    static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    /**
     * 实时定位任务
     *
     * @author baidu
     */
    class RealTimeLocRunnable implements Runnable {

        private int interval = 2;

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
            realTimeLocRunnable = null;
        }
    }


    private class RefreshThread extends Thread {


        protected boolean refresh = true;

        public void run() {

            while (refresh) {
                queryHistoryTrack();
                try {
                    Thread.sleep(packInterval * 1000);
                } catch (InterruptedException e) {
                }
            }

        }

    }

}
