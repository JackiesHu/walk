package com.buxingzhe.pedestrian.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.buxingzhe.pedestrian.PDConfig;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SystemUtils {
	public static void hideKeyboard(Context context,View view){
		 InputMethodManager imm = (InputMethodManager)context.getSystemService(
				 Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	public static void showKeyboard(EditText editText){
		InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(editText, 0);
	}
	/**
	 * 显示键盘
	 */
	public static void showKey(EditText editText) {
		editText.requestFocus();
		editText.setSelection(editText.length());
		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true);
		//editText.findFocus();
		//editText.setCursorVisible(true);
		InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

		/*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);*/

	}
	/**
	 * @param mActivty 
	 * @return 手机的宽高
	 */
	public static int[] getDisplayWidth(Activity mActivty){
		DisplayMetrics dm = new DisplayMetrics();
		mActivty.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		int[] display = {screenWidth,screenHeigh};
		return display;
	}  
	public static int[] getDisplayWidth(Context context){
		WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

		int screenWidth = wm.getDefaultDisplay().getWidth();
		int screenHeigh = wm.getDefaultDisplay().getHeight();

		int[] display = {screenWidth,screenHeigh};
		return display;
	}
	public static String getByString(int id){
		Context context = PDConfig.getInstance().getContext();
		String s = context.getResources().getString(id);
		return s;
	}public static int getByColor(int id){
		Context context = PDConfig.getInstance().getContext();
		int s = context.getResources().getColor(id);
		return s;
	}
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	//判断是否有虚拟键
	public static boolean checkDeviceHasNavigationBar(Context context) {
		boolean hasNavigationBar = false;
		Resources rs = context.getResources();
		int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
		if (id > 0) {
			hasNavigationBar = rs.getBoolean(id);
		}
		try {
			Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
			Method m = systemPropertiesClass.getMethod("get", String.class);
			String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
			if ("1".equals(navBarOverride)) {
				hasNavigationBar = false;
			} else if ("0".equals(navBarOverride)) {
				hasNavigationBar = true;
			}
		} catch (Exception e) {
			Log.w("", e);
		}

		return hasNavigationBar;
	}
	//获取虚拟键盘高度
	public static int getNavigationBarHeight(Context context) {
		int navigationBarHeight = 0;
		Resources rs = context.getResources();
		int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
		if (id > 0 && checkDeviceHasNavigationBar(context)) {
			navigationBarHeight = rs.getDimensionPixelSize(id);
		}
		return navigationBarHeight;
	}
	/**
	 * 判断某个服务是否正在运行的方法
	 *
	 * @param mContext
	 * @param serviceName
	 *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
	 * @return true代表正在运行，false代表服务没有正在运行
	 */
	public static boolean isServiceWork(Context mContext, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
		if (myList.size() <= 0) {
			return false;
		}
		for (int i = 0; i < myList.size(); i++) {
			String mName = myList.get(i).service.getClassName().toString();
			if (mName.equals(serviceName)) {
				isWork = true;
				break;
			}
		}
		return isWork;
	}
	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}
	public static void getExifInfo(String path){
		try {
			//android读取图片EXIF信息
			ExifInterface exifInterface=new ExifInterface(path);
			String smodel=exifInterface.getAttribute(ExifInterface.TAG_MODEL);//设备型号
			String width=exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);//图片宽
			String height=exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);//图片长
			String make = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
			//Toast.makeText(MainActivity.this, smodel+"  "+width+"*"+height, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String updateExifInfo(String oldPath,String path){
		  /*
         * 目前Android SDK定义的Tag有:
        TAG_DATETIME 时间日期
        TAG_FLASH 闪光灯
        TAG_GPS_LATITUDE 纬度
        TAG_GPS_LATITUDE_REF 纬度参考
        TAG_GPS_LONGITUDE 经度
        TAG_GPS_LONGITUDE_REF 经度参考
        TAG_IMAGE_LENGTH 图片长
        TAG_IMAGE_WIDTH 图片宽
        TAG_MAKE 设备制造商
        TAG_MODEL 设备型号
        TAG_ORIENTATION 方向
        TAG_WHITE_BALANCE 白平衡
        */
		try {
			//android读取图片EXIF信息
			ExifInterface exifInterface=new ExifInterface(oldPath);
			String smodel=exifInterface.getAttribute(ExifInterface.TAG_MODEL);//设备型号
			String width=exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);//图片宽
			String height=exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);//图片长
			String make = exifInterface.getAttribute(ExifInterface.TAG_MAKE); //设备制造商
			String datetime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME); //时间日期
			String flash = exifInterface.getAttribute(ExifInterface.TAG_FLASH);//闪光灯
			String latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);//纬度
			String longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);//经度
			String longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);//经度参考
			String latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);//纬度参考
			String orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);//方向
			String balance = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);//白平衡

			ExifInterface newExifInterface=new ExifInterface(path);
			newExifInterface.setAttribute(ExifInterface.TAG_MODEL,smodel);
			newExifInterface.setAttribute(ExifInterface.TAG_IMAGE_WIDTH,width);
			newExifInterface.setAttribute(ExifInterface.TAG_IMAGE_LENGTH,height);
			newExifInterface.setAttribute(ExifInterface.TAG_MAKE,make);
			newExifInterface.setAttribute(ExifInterface.TAG_DATETIME,datetime);
			newExifInterface.setAttribute(ExifInterface.TAG_FLASH,flash);
			newExifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE,latitude);
			newExifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,longitude);
			newExifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF,longitudeRef);
			newExifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF,latitudeRef);
			newExifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,orientation);
			newExifInterface.setAttribute(ExifInterface.TAG_WHITE_BALANCE,balance);
			newExifInterface.saveAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String CreateBitmapTime(){
		return System.currentTimeMillis()+"";
	}
	//防止快速点击
	private static long lastClickTime;
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if ( 0 < timeD && timeD < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	public static int getStatusBarHeight(Context context){
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}
	//判断文件是否存在
	public static boolean fileIsExists(String strFile)
	{
		try
		{
			File f=new File(strFile);
			if(!f.exists())
			{
				return false;
			}

		}
		catch (Exception e)
		{
			return false;
		}

		return true;
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
	public static String getAge(Date birthday){
		Calendar c1 = Calendar.getInstance();
		long nowmillSeconds = c1.getTimeInMillis();
		Calendar c2 = Calendar.getInstance();
		c2.setTime(birthday);
		long birmillSeconds = c2.getTimeInMillis();
		Calendar c3 = Calendar.getInstance();
		long millis = nowmillSeconds - birmillSeconds;
		c3.setTimeInMillis(millis);
		int year = c3.get(Calendar.YEAR);
		int month = c3.get(Calendar.MONTH);
		int day = c3.get(Calendar.DAY_OF_MONTH);
		int hour = c3.get(Calendar.HOUR_OF_DAY);
		if (year > 1970) {
			return year - 1970 + "岁";
		} else if (month > Calendar.JANUARY) {
			return month - Calendar.JANUARY + "月";
		} else if (day > 1) {
			return day - 1 + "天";
		}else{
			return "1天";
		}
	}
}
