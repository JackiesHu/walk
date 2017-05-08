package com.buxingzhe.pedestrian.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.MainActivity;
import com.buxingzhe.pedestrian.bean.StepData;
import com.buxingzhe.pedestrian.common.Constant;
import com.buxingzhe.pedestrian.utils.DbUtils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class StepService extends Service implements SensorEventListener {
    //默认为30秒进行一次存储
    private static int duration = 30000;
    private static String CURRENTDATE = "";
    private SensorManager sensorManager;
    //    private StepDcretor stepDetector;
    private NotificationManager nm;
    private NotificationCompat.Builder builder;
    private Messenger messenger = new Messenger(new MessenerHandler());
    private BroadcastReceiver mBatInfoReceiver;
    private WakeLock mWakeLock;
    private TimeCount time;
    private int CURRENT_SETP;
    //计步传感器类型 0-counter 1-detector
    private static int stepSensor = -1;
    private boolean hasRecord = false;
    private int hasStepCount = 0;
    private int prviousStepCount = 0;
    private String DB_NAME = "walkstep";

    private class MessenerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MSG_FROM_CLIENT:
                    try {
                        Messenger messenger = msg.replyTo;
                        Message replyMsg = Message.obtain(null, Constant.MSG_FROM_SERVER);
                        Bundle bundle = new Bundle();
                        bundle.putInt("step", CURRENT_SETP);
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

    @Override
    public void onCreate() {
        super.onCreate();
        initBroadcastReceiver();
        new Thread(new Runnable() {
            public void run() {
                startStepDetector();
            }
        }).start();


        startTimeCount();
        initTodayData();
    }

    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
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
            CURRENT_SETP = 0;
        } else if (list.size() == 1) {
            Log.v("xf", "StepData=" + list.get(0).toString());
            CURRENT_SETP = Integer.parseInt(list.get(0).getStep());
        } else {
            Log.v("xf", "出错了！");
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
        filter.addAction(Intent.ACTION_TIME_CHANGED);
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
                    duration = 30000;
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
                    save();
                    isNewDay();
                }
            }
        };
        registerReceiver(mBatInfoReceiver, filter);
    }


    /**
     * 监听晚上0点变化初始化数据
     */
    private void isNewDay() {
        String time = "00:00";
        if (time.equals(new SimpleDateFormat("HH:mm").format(new Date())) || !CURRENTDATE.equals(getTodayDate())) {
            initTodayData();
        }
    }



    private void startTimeCount() {
        time = new TimeCount(duration, 1000);
        time.start();
    }

    /**
     * 更新通知
     */
    private void updateNotification(String content) {
        builder = new NotificationCompat.Builder(this);
        builder.setPriority(Notification.PRIORITY_MIN);

        //Notification.Builder builder = new Notification.Builder(this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("Dylan计步");
        builder.setContentTitle("Dylan计步");
        //设置不可清除
        builder.setOngoing(true);
        builder.setContentText(content);
        Notification notification = builder.build();

        startForeground(0, notification);

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(R.string.app_name, notification);
    }

    /**
     * Notification构造器
     */
    android.support.v4.app.NotificationCompat.Builder mBuilder;
    /**
     * Notification的ID
     */
    int notifyId = 100;

    /**
     * 初始化通知栏
     */
    private void initNotify() {

        String plan = this.getSharedPreferences("share_date", Context.MODE_MULTI_PROCESS).getString("planWalk_QTY", "7000");
        mBuilder = new android.support.v4.app.NotificationCompat.Builder(this);
        mBuilder.setContentTitle("今日步数" + CURRENT_SETP + " 步")
                .setContentText("距离目标还差" + (Integer.valueOf(plan) - CURRENT_SETP) + "步，加油！")
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
//				.setNumber(number)//显示数量
                .setTicker("Dylan计步提醒您开始锻炼了")//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return messenger.getBinder();
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * 获取传感器实例
     */
    private void startStepDetector() {
        if (sensorManager != null) {
            sensorManager = null;
        }
        // 获取传感器管理器的实例
        sensorManager = (SensorManager) this
                .getSystemService(SENSOR_SERVICE);
        //android4.4以后可以使用计步传感器
        int VERSION_CODES = Build.VERSION.SDK_INT;
        if (VERSION_CODES >= 19) {
            addCountStepListener();
        } else {
//            addBasePedoListener();
        }
    }

    /**
     * 添加传感器监听
     */
    private void addCountStepListener() {
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (countSensor != null) {
            stepSensor = 0;
            Log.v("xf", "countSensor");
            sensorManager.registerListener(StepService.this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else if (detectorSensor != null) {
            stepSensor = 1;
            Log.v("xf", "detectorSensor");
            sensorManager.registerListener(StepService.this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.v("xf", "Count sensor not available!");
        }
    }


    /**
     * 传感器监听回调
     *
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        //   i++;
        if (stepSensor == 0) {
            int tempStep = (int) event.values[0];
            if (!hasRecord) {
                hasRecord = true;
                hasStepCount = tempStep;
            } else {
                int thisStepCount = tempStep - hasStepCount;
                CURRENT_SETP += (thisStepCount - prviousStepCount);
                prviousStepCount = thisStepCount;

            }
        } else if (stepSensor == 1) {
            if (event.values[0] == 1.0) {
                CURRENT_SETP++;
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则开始计步
            time.cancel();
            save();
            startTimeCount();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

    }

    private void save() {
        int tempStep = CURRENT_SETP;

        List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{CURRENTDATE});
        if (list.size() == 0 || list.isEmpty()) {
            StepData data = new StepData();
            data.setToday(CURRENTDATE);
            data.setStep(tempStep + "");
            DbUtils.insert(data);
        } else if (list.size() == 1) {
            StepData data = list.get(0);
            data.setStep(tempStep + "");
            DbUtils.update(data);
        } else {
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消前台进程
        stopForeground(true);
        DbUtils.closeDb();
        unregisterReceiver(mBatInfoReceiver);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

}
