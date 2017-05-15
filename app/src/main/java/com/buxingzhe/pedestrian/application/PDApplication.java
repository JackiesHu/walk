package com.buxingzhe.pedestrian.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.LatestPointRequest;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.OnCustomAttributeListener;
import com.baidu.trace.model.ProcessOption;
import com.buxingzhe.lib.util.Log;
import com.buxingzhe.lib.util.NetUtil;
import com.buxingzhe.pedestrian.PDConfig;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.map.CommonUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Subscriber;
import rx.Subscription;


/**
 * Created by Administrator on 2016/5/9.
 */
public class PDApplication extends Application {
    private Subscription mSubscription;
    private String activityId;
    private double distance;
    private Date today;


    private String cityName;


    public SharedPreferences trackConf = null;
    public SharedPreferences trackStepConf = null;

    public LBSTraceClient mClient = null;//轨迹客户端
  //  public LBSTraceClient mStepClient = null;//轨迹客户端

    public Trace mTrace = null;//轨迹服务
    public Trace mStepTrace = null;//轨迹服务

    public long serviceId = 138866;//轨迹服务ID

    public String entityName = "myTrace";// Entity标识
    public String entityStepName = "myStepTrace";// Entity标识
    private LocRequest locRequest = null;
    private LocRequest locStepRequest = null;

    public boolean isTraceStarted = false;//服务是否开启标识
    public boolean isStepTraceStarted = false;//服务是否开启标识

    public boolean isGatherStarted = false;//isGatherStarted
    public boolean isStepGatherStarted = false;//isGatherStarted
    public Context mContext = null;
    public static int screenWidth = 0;

    public static int screenHeight = 0;
    private AtomicInteger mSequenceGenerator = new AtomicInteger();
    private AtomicInteger mStepSequenceGenerator = new AtomicInteger();

    {
        // umeng                      appid + appkey
        PlatformConfig.setWeixin("wx609e5d32a2de0351", "3dfae04eac20da17e00a31ff17dc618d");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    private LocationClient mLocationClient;

    @Override
    public void onCreate() {
        super.onCreate();
        if (today == null) {
            today = new Date();
        }
        distance = this.getSharedPreferences("stupdistance", Context.MODE_PRIVATE).getLong("stupdistanceKey", 0);

        PDConfig.getInstance().init(this);
        SDKInitializer.initialize(this);
        MobclickAgent.openActivityDurationTrack(false);
        mContext = getApplicationContext();
        entityName = CommonUtil.getImei(this);
        entityStepName = CommonUtil.getImei(this) + "step";
        // 若为创建独立进程，则不初始化成员变量
        if ("com.baidu.track:remote".equals(CommonUtil.getCurProcessName(mContext))) {
            return;
        }

        mClient = new LBSTraceClient(mContext);
      //  mStepClient = new LBSTraceClient(mContext);
        mTrace = new Trace(serviceId, entityName);
        mStepTrace = new Trace(serviceId, entityStepName);
        trackConf = getSharedPreferences("track_conf", MODE_PRIVATE);
        trackStepConf = getSharedPreferences("step_conf", MODE_PRIVATE);
        locRequest = new LocRequest(serviceId);
        locStepRequest = new LocRequest(serviceId);

        mClient.setOnCustomAttributeListener(new OnCustomAttributeListener() {
            @Override
            public Map<String, String> onTrackAttributeCallback() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("key1", "value1");
                map.put("key2", "value2");
                return map;
            }
        });
/*
        mStepClient.setOnCustomAttributeListener(new OnCustomAttributeListener() {
            @Override
            public Map<String, String> onTrackAttributeCallback() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("key1", "value1");
                map.put("key2", "value2");
                return map;
            }
        });*/

        clearTraceStatus();
        clearStepTraceStatus();
        getScreenSize();
        // umeng
        UMShareAPI.get(this);

        Date newDate = new Date();
        if (newDate != today) {
            upLoadDistance();
            today = newDate;
            distance = 0;
        }

        getLocalCityName(this);

    }

    private void getLocalCityName(Context context) {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开GPS
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(3000);// 设置发起定位请求的间隔时间为3000ms
        option.disableCache(false);// 禁止启用缓存定位
        option.setPriority(LocationClientOption.NetWorkFirst);// 网络定位优先
        mLocationClient = new LocationClient(context); // 声明LocationClient类
        mLocationClient.setLocOption(option);// 使用设置
        mLocationClient.start();// 开启定位SDK
        mLocationClient.requestLocation();// 开始请求位置

        mLocationClient.registerLocationListener(new BDLocationListener() {


            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location != null) {
                    String city = location.getCity();
                    city = city.replaceAll("市", "");
                    setCityName(city);
                } else {
                    setCityName("北京");

                    return;
                }
            }
        });

    }

    private void upLoadDistance() {
        double distanceUp = distance / 1000;
        int stepCount = (int) (distance / 0.4);


        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String s = format.format(today);
        Map<String, String> params = new HashMap<>();
        params.put("distance", distanceUp + " ");
        params.put("stepCount", stepCount + " ");
        params.put("publishDate", s);
        params.put("userId", GlobalParams.USER_ID);
        params.put("token", GlobalParams.TOKEN);
        mSubscription = NetRequestManager.getInstance().publishWalkRecord(params, new Subscriber<String>() {
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

    public String getActId() {
        return activityId;
    }

    public void setActId(String activityId) {
        this.activityId = activityId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * 获取屏幕尺寸
     */
    private void getScreenSize() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
    }

    /**
     * 初始化请求公共参数
     *
     * @param request
     */
    public void initRequest(BaseRequest request) {
        request.setTag(getTag());
        request.setServiceId(serviceId);
    }

    public void initStepRequest(BaseRequest request) {
        request.setTag(getStepTag());
        request.setServiceId(serviceId);
    }


    /**
     * 获取当前位置
     */
    public void getCurrentLocation(OnEntityListener entityListener, OnTrackListener trackListener) {
        // 网络连接正常，开启服务及采集，则查询纠偏后实时位置；否则进行实时定位
        if (NetUtil.isNetWorkConnectted(getApplicationContext())
                && trackConf.contains("is_trace_started")
                && trackConf.contains("is_gather_started")
                && trackConf.getBoolean("is_trace_started", false)
                && trackConf.getBoolean("is_gather_started", false)) {
            LatestPointRequest request = new LatestPointRequest(getTag(), serviceId, entityName);
            ProcessOption processOption = new ProcessOption();
            processOption.setNeedDenoise(true);
            processOption.setRadiusThreshold(100);
            request.setProcessOption(processOption);
            mClient.queryLatestPoint(request, trackListener);
        } else {
            mClient.queryRealTimeLoc(locRequest, entityListener);
        }
    }

    public void getStepCurrentLocation(OnEntityListener entityListener, OnTrackListener trackListener) {
        // 网络连接正常，开启服务及采集，则查询纠偏后实时位置；否则进行实时定位
        if (NetUtil.isNetWorkConnectted(getApplicationContext())
                && trackStepConf.contains("is_trace_started")
                && trackStepConf.contains("is_gather_started")
                && trackStepConf.getBoolean("is_trace_started", false)
                && trackStepConf.getBoolean("is_gather_started", false)) {
            LatestPointRequest request = new LatestPointRequest(getStepTag(), serviceId, entityStepName);
            ProcessOption processOption = new ProcessOption();
            processOption.setNeedDenoise(true);
            processOption.setRadiusThreshold(100);
            request.setProcessOption(processOption);
            mClient.queryLatestPoint(request, trackListener);
        } else {
            mClient.queryRealTimeLoc(locStepRequest, entityListener);
        }
    }

    /**
     * 获取请求标识
     *
     * @return
     */
    public int getTag() {
        return mSequenceGenerator.incrementAndGet();
    }

    public int getStepTag() {
        return mStepSequenceGenerator.incrementAndGet();
    }

    /**
     * 清除Trace状态：初始化app时，判断上次是正常停止服务还是强制杀死进程，根据trackConf中是否有is_trace_started字段进行判断。
     * <p>
     * 停止服务成功后，会将该字段清除；若未清除，表明为非正常停止服务。
     */
    private void clearTraceStatus() {
        if (trackConf.contains("is_trace_started") || trackConf.contains("is_gather_started")) {
            SharedPreferences.Editor editor = trackConf.edit();
            editor.remove("is_trace_started");
            editor.remove("is_gather_started");
            editor.apply();
        }
    }

    private void clearStepTraceStatus() {
        if (trackStepConf.contains("is_trace_started") || trackStepConf.contains("is_gather_started")) {
            SharedPreferences.Editor editor = trackStepConf.edit();
            editor.remove("is_trace_started");
            editor.remove("is_gather_started");
            editor.apply();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }
}
