package com.buxingzhe.pedestrian.service;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.buxingzhe.pedestrian.application.PDApplication;
import com.buxingzhe.pedestrian.bean.StepData;
import com.buxingzhe.pedestrian.common.Constant;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.common.SPConstant;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.DbUtils;
import com.buxingzhe.pedestrian.walk.HourStep;
import com.buxingzhe.pedestrian.walk.HourStepCache;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by chinaso on 2017/7/24.
 */

public class DistanceService extends Service {
    //默认为30秒进行一次存储
    private static int duration = 50000;
    private BroadcastReceiver mBatInfoReceiver;
    private double CURRENT_DISTANCE = 0.0;
    private double hourDistance = 0;
    protected double stepDistance = 0.0004;//以km 为单位
    private static String CURRENTDATE = "";
    private String DB_NAME = "walkdistance";
    private RefreshThread refreshThread = null;
    private LocationManager lm;
    protected PDApplication pdApp = null;
    private String bestProvider;
    private Location location;
    private static final String TAG = "DistanceService";
    private Location oldLocation;
    private String timeStap;
    private HourStepCache stepCache;
    private List<HourStep> stepList = new ArrayList<>();
    private String today;
    private Subscription mSubscription;

    private class MessenerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MSG_FROM_CLIENT:
                    try {
                        Messenger messenger = msg.replyTo;
                        Message replyMsg = Message.obtain(null, Constant.MSG_FROM_SERVER);
                        Bundle bundle = new Bundle();
                        bundle.putDouble("distance", CURRENT_DISTANCE);
                        replyMsg.setData(bundle);
                        messenger.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


    private Messenger messenger = new Messenger(new DistanceService.MessenerHandler());


    @Override
    public void onCreate() {
        super.onCreate();
        pdApp = (PDApplication) PDApplication.getApp().getApplicationContext();
        stepCache = new HourStepCache(pdApp);
        CURRENT_DISTANCE =pdApp.getSharedPreferences(SPConstant.DISTANCE_SP, Context.MODE_PRIVATE).getFloat(SPConstant.DISTANCE_SP_TOTAL, 0);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH");
        timeStap = format.format(date);
        stepDistance=pdApp.getStepDistance();
        isNewDay();

        initBroadcastReceiver();
        initTodayData();
        initManager();
        startRefreshThread(true);
    }

    private void initManager() {
        lm = (LocationManager) PDApplication.getApp().getSystemService(Context.LOCATION_SERVICE);

        // 判断GPS是否正常启动
        if (ContextCompat.checkSelfPermission(pdApp, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //请求开启GPS
            return;
        }

        // 为获取地理位置信息时设置查询条件
        bestProvider = lm.getBestProvider(getCriteria(), true);
        // 获取位置信息
        // 如果不设置查询要求，getLastKnownLocation方法传入的参数为LocationManager.GPS_PROVIDER
        location = lm.getLastKnownLocation(bestProvider);
        updateDistance(location);
        // 监听状态
        lm.addGpsStatusListener(listener);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBatInfoReceiver);
        lm.removeUpdates(locationListener);
        lm.removeGpsStatusListener(listener);
        SharedPreferences preferences = pdApp.getSharedPreferences(SPConstant.DISTANCE_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(SPConstant.DISTANCE_SP_TOTAL, (float) CURRENT_DISTANCE);
        editor.apply();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
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

    private class RefreshThread extends Thread {


        protected boolean refresh = true;

        public void run() {

            while (refresh) {

                if (ActivityCompat.checkSelfPermission(pdApp, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(pdApp, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                location = lm.getLastKnownLocation(bestProvider);
                if (location != null) {
                    updateDistance(location);
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
            }

        }
    }

    /**
     * 注册广播
     */
    private void initBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //关机广播
        filter.addAction(Intent.ACTION_SHUTDOWN);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
//        filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //监听日期变化
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        // filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);

        mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    Log.d("xf", "screen on");
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    Log.d("xf", "screen off");
                    //改为60秒一存储
                    duration = 60000;
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    Log.d("xf", "screen unlock");
//                    save();
                    //改为30秒一存储
                    duration = 50000;
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                    Log.i("xf", " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
                    //保存一次
                    save();
                } else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
                    Log.i("xf", " receive ACTION_SHUTDOWN");
                    save();
                } else if (Intent.ACTION_DATE_CHANGED.equals(action)) {//日期变化步数重置为0
                    save();
                    isNewDay();
                } else if (Intent.ACTION_TIME_CHANGED.equals(action)) {
                    //时间变化步数重置为0
                    save();
                    isNewDay();
                } else if (Intent.ACTION_TIME_TICK.equals(action)) {//日期变化步数重置为0
                    //判断是否为新一个小时
                    newHourTodo();
                }
            }
        };
        registerReceiver(mBatInfoReceiver, filter);
    }

    private void newHourTodo() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH");
        String s = format.format(date);

        if (!timeStap.equals(s)) {
            //TODO
            if (stepCache.readStepsList() != null) {
                stepList = stepCache.readStepsList();
            }
            int stepCount = (int) (hourDistance / stepDistance);
            HourStep hourStep = new HourStep();
            hourStep.setHour(timeStap + "时");
            hourStep.setStepCount(stepCount);
            stepList.add(hourStep);
            hourDistance = 0;
            timeStap = s;
            stepCache.celarStepsList();
            stepCache.saveStepsList(stepList);
        }
    }

    private void save() {
        Double tempDistance = CURRENT_DISTANCE;

        List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{CURRENTDATE});
        if (list.size() == 0 || list.isEmpty()) {
            StepData data = new StepData();
            data.setToday(CURRENTDATE);
            data.setStep(tempDistance + "");
            DbUtils.insert(data);
        } else if (list.size() == 1) {
            StepData data = list.get(0);
            data.setStep(tempDistance + "");
            DbUtils.update(data);
        } else {
        }
    }

    /**
     * 监听晚上0点变化初始化数据
     */
    private void isNewDay() {
        SharedPreferences preferences = pdApp.getSharedPreferences(SPConstant.DISTANCE_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        today = preferences.getString(SPConstant.DISTANCE_SP_DATE, null);

        if (today == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            today = sdf.format(date);
            editor.putString(SPConstant.DISTANCE_SP_DATE, today);
            editor.apply();
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date newDate = new Date();
            String newDay = sdf.format(newDate);
            if (!newDay.equals(today)) {
                upLoadDistance(newDay);
            }
        }

    }


    private void upLoadDistance(final String newDay) {
        double distanceUp = CURRENT_DISTANCE;
        int stepCount = (int) (CURRENT_DISTANCE / stepDistance);

        Map<String, String> params = new HashMap<>();
        params.put("distance", distanceUp + " ");
        params.put("stepCount", stepCount + " ");
        params.put("publishDate", today);
        params.put("userId", GlobalParams.USER_ID);
        params.put("token", GlobalParams.TOKEN);
        mSubscription = NetRequestManager.getInstance().publishWalkRecord(params, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                com.buxingzhe.lib.util.Log.i("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                com.buxingzhe.lib.util.Log.i(e.getMessage());
            }

            @Override
            public void onNext(String jsonStr) {
                CURRENT_DISTANCE = 0;
                SharedPreferences preferences = pdApp.getSharedPreferences(SPConstant.DISTANCE_SP, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(SPConstant.DISTANCE_SP_DATE, newDay);
                editor.commit();

                //清除按小时计算的步数
                stepCache.celarStepsList();
                stepList.clear();
            }

        });
    }

    /**
     * 初始化当天的步数
     */
    private void initTodayData() {
        CURRENTDATE = getTodayDate();
        DbUtils.createDb(this, DB_NAME);
        //获取当天的数据，用于展示
        List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{CURRENTDATE});
        if (list.size() == 0 || list.isEmpty()) {
            CURRENT_DISTANCE = 0.0;
        } else if (list.size() == 1) {
            Log.v("xf", "StepData=" + list.get(0).toString());
            CURRENT_DISTANCE = Double.parseDouble(list.get(0).getStep());
        } else {
            Log.v("xf", "出错了！");
        }
    }

    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 返回查询条件
     *
     * @return
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        // 设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    // 位置监听
    private LocationListener locationListener = new LocationListener() {


        /**
         * 位置信息变化时触发
         */
        public void onLocationChanged(Location location) {
            updateDistance(location);
            com.buxingzhe.lib.util.Log.i(TAG, "时间：" + location.getTime());
            com.buxingzhe.lib.util.Log.i(TAG, "经度：" + location.getLongitude());
            com.buxingzhe.lib.util.Log.i(TAG, "纬度：" + location.getLatitude());
            com.buxingzhe.lib.util.Log.i(TAG, "海拔：" + location.getAltitude());
        }

        /**
         * GPS状态变化时触发
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                // GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    com.buxingzhe.lib.util.Log.i(TAG, "当前GPS状态为可见状态");
                    break;
                // GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    com.buxingzhe.lib.util.Log.i(TAG, "当前GPS状态为服务区外状态");
                    break;
                // GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    com.buxingzhe.lib.util.Log.i(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * GPS开启时触发
         */
        public void onProviderEnabled(String provider) {
            if (ActivityCompat.checkSelfPermission(pdApp, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = lm.getLastKnownLocation(provider);
            updateDistance(location);
        }

        /**
         * GPS禁用时触发
         */
        public void onProviderDisabled(String provider) {
            updateDistance(null);
        }
    };


    // 状态监听
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                // 第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    com.buxingzhe.lib.util.Log.i(TAG, "第一次定位");
                    break;
                // 卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    com.buxingzhe.lib.util.Log.i(TAG, "卫星状态改变");
                    // 获取当前状态
                    if (ActivityCompat.checkSelfPermission(pdApp, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    GpsStatus gpsStatus = lm.getGpsStatus(null);
                    // 获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    // 创建一个迭代器保存所有卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites()
                            .iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        count++;
                    }
                    System.out.println("搜索到：" + count + "颗卫星");
                    break;
                // 定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    com.buxingzhe.lib.util.Log.i(TAG, "定位启动");
                    break;
                // 定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                    com.buxingzhe.lib.util.Log.i(TAG, "定位结束");
                    break;
            }
        }

        ;
    };


    /**
     * 实时更新文本内容
     *
     * @param location
     */
    private void updateDistance(Location location) {
        if (location != null) {
            if (oldLocation != null) {
                Double d = getDistance(oldLocation, location);
                if (d < 0.01) {
                    CURRENT_DISTANCE = CURRENT_DISTANCE + d;
                    hourDistance = hourDistance + d;
                }
                oldLocation = location;
            }

        }

        oldLocation = location;
    }


    private double getDistance(Location locationOld, Location locationNew) {
        // 维度
        double lat1 = (Math.PI / 180) * locationOld.getLatitude();
        double lat2 = (Math.PI / 180) * locationNew.getLatitude();

        // 经度
        double lon1 = (Math.PI / 180) * locationOld.getLongitude();
        double lon2 = (Math.PI / 180) * locationOld.getLongitude();

        // 地球半径
        double R = 6371;

        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;

        return d;
    }
}
