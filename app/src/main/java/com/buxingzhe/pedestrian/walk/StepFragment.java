package com.buxingzhe.pedestrian.walk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

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
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseFragment;
import com.buxingzhe.pedestrian.application.PDApplication;
import com.buxingzhe.pedestrian.listen.OnInteractionData;
import com.buxingzhe.pedestrian.utils.map.CommonUtil;
import com.buxingzhe.pedestrian.utils.map.Constants;
import com.buxingzhe.pedestrian.utils.map.MapUtil;
import com.buxingzhe.pedestrian.utils.map.ViewUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chinaso on 2017/5/13.
 */

public class StepFragment extends BaseFragment {
    protected PDApplication trackApp = null;
    protected TextView distanceTv;
    protected TextView stepCountTv;
    //private MapUtil mapUtil = null;
    private static List<LatLng> pointList = new ArrayList<LatLng>();  //定位点的集合
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
    private int pageIndex = 1;

    private long startTime = CommonUtil.getCurrentTime();//查询轨迹的开始时间

    private long endTime = CommonUtil.getCurrentTime();// 查询轨迹的结束时间

    private SortType sortType = SortType.asc;//轨迹排序规则(倒序或者顺序)

    private OnInteractionData mOnInteractionData;
    private RefreshThread refreshThread = null;  //刷新地图线程以获取实时点

    protected double distance = 0;
    private HourStepCache stepCache;
    private List<HourStep> stepList = new ArrayList<HourStep>();
    private int hourStepCount = 0;
    private String timeStap;

    public void setOnInteractionData(OnInteractionData onInteractionData) {
        mOnInteractionData = onInteractionData;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        trackApp = (PDApplication) getActivity().getApplicationContext();
        distance = trackApp.getDistance();
        startRealTimeLoc(Constants.LOC_INTERVAL);
        initListener();
        stepCache = new HourStepCache(getActivity());
        stepCache.saveStepsList(stepList);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH");
        timeStap = format.format(date);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initListener() {

        // 初始化轨迹服务监听器
        mTraceListener = new OnTraceListener() {
            // 开启服务回调
            @Override
            public void onStartTraceCallback(int status, String message) {
                if (StatusCodes.SUCCESS == status || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= status) {

                    trackApp.isStepTraceStarted = true;
                    SharedPreferences.Editor editor = trackApp.trackStepConf.edit();
                    editor.putBoolean("is_trace_started", true);
                    editor.apply();
                }
                ViewUtil.showToast(getActivity(),
                        String.format("onStartTraceCallback, errorNo:%d, message:%s ", status, message));

                startRefreshThread(true);
            }


            // 停止服务回调
            @Override
            public void onStopTraceCallback(int status, String message) {
                if (StatusCodes.SUCCESS == status || StatusCodes.CACHE_TRACK_NOT_UPLOAD == status) {
                    trackApp.isStepTraceStarted = false;
                    trackApp.isStepGatherStarted = false;
                    // 停止成功后，直接移除is_trace_started记录（便于区分用户没有停止服务，直接杀死进程的情况）
                    SharedPreferences.Editor editor = trackApp.trackStepConf.edit();
                    editor.remove("is_trace_started");
                    editor.remove("is_gather_started");
                    editor.apply();
                }
                ViewUtil.showToast(getActivity(),
                        String.format("onStopTraceCallback, errorNo:%d, message:%s ", status, message));

                startRefreshThread(false);

            }

            // 开启采集回调
            @Override
            public void onStartGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STARTED == errorNo) {

                    trackApp.isStepGatherStarted = true;
                    SharedPreferences.Editor editor = trackApp.trackStepConf.edit();
                    editor.putBoolean("is_gather_started", true);
                    editor.apply();
                }
                ViewUtil.showToast(getActivity(),
                        String.format("onStartGatherCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            // 停止采集回调
            @Override
            public void onStopGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STOPPED == errorNo) {
                    trackApp.isStepGatherStarted = false;
                    SharedPreferences.Editor editor = trackApp.trackStepConf.edit();
                    editor.remove("is_gather_started");
                    editor.apply();
                }
                ViewUtil.showToast(getActivity(),
                        String.format("onStopGatherCallback, errorNo:%d, message:%s ", errorNo, message));

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

              /*  LatLng currentLatLng = mapUtil.convertTrace2Map(point.getLocation());
                if (null == currentLatLng) {
                    return;
                }
                CurrentLocation.locTime = point.getLocTime();
                CurrentLocation.latitude = currentLatLng.latitude;
                CurrentLocation.longitude = currentLatLng.longitude;

                if (null != mapUtil) {
                    mapUtil.updateStatus(currentLatLng, true);
                }*/
            }

            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                int total = response.getTotal();
                if (StatusCodes.SUCCESS != response.getStatus()) {
                   // ViewUtil.showToast(getActivity(), response.getMessage());
                } else if (0 == total) {
                   // ViewUtil.showToast(getActivity(), getString(R.string.no_track_data));
                } else {
                    List<TrackPoint> points = response.getTrackPoints();
                    if (null != points) {
                        for (TrackPoint trackPoint : points) {
                            if (!CommonUtil.isZeroPoint(trackPoint.getLocation().getLatitude(),
                                    trackPoint.getLocation().getLongitude())) {
                                trackPoints.add(MapUtil.convertTrace2Map(trackPoint.getLocation()));
                            }
                        }
                    }
                }

                if (total > Constants.PAGE_SIZE * pageIndex) {
                    historyTrackRequest.setPageIndex(++pageIndex);
                    queryHistoryTrack();

                } else {
                    if (trackPoints.size() > 0) {

                        // mapUtil.drawHistoryTrack(trackPoints, sortType);
                    }

                }
            }

            @Override
            public void onDistanceCallback(DistanceResponse response) {
                super.onDistanceCallback(response);
                if (StatusCodes.SUCCESS == response.getStatus()) {
                    if (distance < 1.5) {//1s最多1.5米，否则不算做步行
                        distance = distance + response.getDistance();
                        hourStepCount = hourStepCount + (int) (response.getDistance() / 0.4);
                    }
                    distanceTv.setText(distance + "");
                    stepCountTv.setText((int) distance / 4 + "");
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

    protected void stopStep() {
        // 停止采集
        trackApp.isStepTraceStarted = false;
        trackApp.mClient.stopGather(mTraceListener);

        startRefreshThread(false);

    }


    protected void startStep() {
        trackApp.isStepTraceStarted = true;
        trackApp.mClient.startTrace(trackApp.mTrace, mTraceListener);
        if (Constants.DEFAULT_PACK_INTERVAL != packInterval) {
            stopRealTimeLoc();
            startRealTimeLoc(packInterval);
        }

        trackApp.mClient.startGather(mTraceListener);
        startTime = CommonUtil.getCurrentTime();
    }


    /**
     * 查询历史轨迹
     */
    private void queryHistoryTrack() {
        endTime = CommonUtil.getCurrentTime();
        trackApp.initStepRequest(historyTrackRequest);
        trackApp.initStepRequest(distanceRequest);

        historyTrackRequest.setEntityName(trackApp.entityStepName);
        historyTrackRequest.setStartTime(startTime);
        historyTrackRequest.setEndTime(endTime);
        historyTrackRequest.setPageIndex(pageIndex);
        historyTrackRequest.setPageSize(Constants.PAGE_SIZE);
        trackApp.mClient.queryHistoryTrack(historyTrackRequest, trackListener);

        // 设置需要纠偏
        distanceRequest.setProcessed(true);

        ProcessOption processOption = new ProcessOption();// 创建纠偏选项实例

        processOption.setNeedDenoise(true);// 设置需要去噪

        processOption.setNeedMapMatch(true);// 设置需要绑路

        processOption.setTransportMode(TransportMode.walking);// 设置交通方式为步行

        distanceRequest.setProcessOption(processOption);// 设置纠偏选项

        distanceRequest.setSupplementMode(SupplementMode.walking);// 设置里程填充方式为驾车
        distanceRequest.setEntityName(trackApp.entityStepName);
        distanceRequest.setStartTime(startTime);
        distanceRequest.setEndTime(endTime);
        trackApp.mClient.queryDistance(distanceRequest, trackListener);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        // 停止服务
        trackApp.mClient.stopTrace(trackApp.mStepTrace, mTraceListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRealTimeLoc();
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

        private int interval = 0;

        public RealTimeLocRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            trackApp.getStepCurrentLocation(entityListener, trackListener);
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


    @Override
    public void onActivityResult(int historyTrackRequestCode, int resultCode, Intent data) {
        if (null == data) {
            return;
        }

        trackPoints.clear();
        pageIndex = 1;

        if (data.hasExtra("startTime")) {
            startTime = data.getLongExtra("startTime", CommonUtil.getCurrentTime());
        }

        if (data.hasExtra("endTime")) {
            endTime = data.getLongExtra("endTime", CommonUtil.getCurrentTime());
        }

        ProcessOption processOption = new ProcessOption();
        if (data.hasExtra("radius")) {
            processOption.setRadiusThreshold(data.getIntExtra("radius", Constants.DEFAULT_RADIUS_THRESHOLD));
        }
        if (data.hasExtra("transportMode")) {
            processOption.setTransportMode(TransportMode.valueOf(data.getStringExtra("transportMode")));
        }
        if (data.hasExtra("denoise")) {
            processOption.setNeedDenoise(data.getBooleanExtra("denoise", true));
        }
        if (data.hasExtra("vacuate")) {
            processOption.setNeedVacuate(data.getBooleanExtra("vacuate", true));
        }
        if (data.hasExtra("mapmatch")) {
            processOption.setNeedMapMatch(data.getBooleanExtra("mapmatch", true));
        }
        historyTrackRequest.setProcessOption(processOption);

        if (data.hasExtra("supplementMode")) {
            historyTrackRequest.setSupplementMode(SupplementMode.valueOf(data.getStringExtra("supplementMode")));
        }
        if (data.hasExtra("sortType")) {
            sortType = SortType.valueOf(data.getStringExtra("sortType"));
            historyTrackRequest.setSortType(sortType);
        }
        if (data.hasExtra("coordTypeOutput")) {
            historyTrackRequest.setCoordTypeOutput(CoordType.valueOf(data.getStringExtra("coordTypeOutput")));
        }
        if (data.hasExtra("processed")) {
            historyTrackRequest.setProcessed(data.getBooleanExtra("processed", true));
        }


        queryHistoryTrack();
    }

    private class RefreshThread extends Thread {


        protected boolean refresh = true;

        public void run() {

            while (refresh) {
                queryHistoryTrack();
                newHourTodo();
                try {
                    Thread.sleep(packInterval * 100);
                } catch (InterruptedException e) {
                }
            }

        }

        private void newHourTodo() {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH");
            String s = format.format(date);
            if (!timeStap.equals(s)) {
                //TODO
                stepList = stepCache.readStepsList();
                HourStep hourStep = new HourStep();
                hourStep.setHour(s+"点");
                hourStep.setStepCount(hourStepCount);
                stepList.add(hourStep);
                hourStepCount = 0;
                stepCache.saveStepsList(stepList);

            }
        }

    }
}
