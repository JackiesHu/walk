package com.buxingzhe.lib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetUtil {
    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static void netWorkType(Context context, NetWorkStatusCallBack callback) {
//        if (callback!=null) {
//            callback.strongNetWork();
//            return;
//        }

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            //网络不可用就算了
            if (info==null||!info.isConnected()){
                if (callback!=null)
                    callback.noNetWork();
                 return;
            }
            int type = info.getType();
            Log.d("net type: "+type);
            if (type== ConnectivityManager.TYPE_WIFI){
                if (callback!=null) {
                    callback.strongNetWork();
                }

            }else if (type== ConnectivityManager.TYPE_MOBILE){
                int subtype = info.getSubtype();
                if (subtype<=12){
                    if (callback!=null){
                        callback.thinNetWork();
                    }
                }else if (subtype>12){
                    if (callback!=null){
                        callback.strongNetWork();
                    }
                }
                /*switch (subtype) {
                    case TelephonyManager.NETWORK_TYPE_GPRS: // 2g

                    case TelephonyManager.NETWORK_TYPE_CDMA: // 2g

                    case TelephonyManager.NETWORK_TYPE_EDGE: // 2g

                    case TelephonyManager.NETWORK_TYPE_1xRTT:

                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        if (callback!=null)
                            callback.thinNetWork();
                        break;

                    case TelephonyManager.NETWORK_TYPE_EVDO_A: // 3g

                    case TelephonyManager.NETWORK_TYPE_UMTS:

                    case TelephonyManager.NETWORK_TYPE_EVDO_0:

                    case TelephonyManager.NETWORK_TYPE_HSDPA:

                    case TelephonyManager.NETWORK_TYPE_HSUPA:

                    case TelephonyManager.NETWORK_TYPE_HSPA:

                    case TelephonyManager.NETWORK_TYPE_EVDO_B:

                    case TelephonyManager.NETWORK_TYPE_EHRPD:

                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        if (callback!=null)
                            callback.thinNetWork();
                        break;

                    case TelephonyManager.NETWORK_TYPE_LTE:
                    case 19:  //TelephonyManager.NETWORK_TYPE_LTE_CA
                        if (callback!=null)
                            callback.strongNetWork();
                        break;
                }*/

            }else{
                if (callback!=null)
                    callback.strongNetWork();
            }


        }
    }


    public static boolean isNetWorkConnectted(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info!=null&&info.isConnected()) {
                return true;
            }else{
                NetworkInfo[] allNetworkInfo = connectivity.getAllNetworkInfo();
                if (allNetworkInfo != null) {
                    int length = allNetworkInfo.length;
                    for (int i = 0; i < length; i++) {
                        // 判断获得的网络状态是否是处于连接状态
                        if (allNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
                return false;

            }
        }
        return false;
    }

    /**
     * 检测网络状态是否联通
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (null != info && info.isConnected() && info.isAvailable()) {
                return true;
            }
        } catch (Exception e) {
            android.util.Log.e("net", "current network is not available");
            return false;
        }
        return false;
    }

    public interface NetWorkStatusCallBack{
        void strongNetWork();
        void thinNetWork();
        void noNetWork();
    }
}
