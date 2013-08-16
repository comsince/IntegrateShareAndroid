package com.comsince.phonebook.util;

import java.io.File;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;

public class AndroidUtil {
	public static final int  WIFI_ON = 0;
	public static final int  WIFI_OFF = 1;
	public static final int  MOBILE_ON = 2;
	public static final int  MOBILE_OFF = 3;
	
	
	/**
	 * 判断当前网络状态
	 * @author comsince
	 * @param context 
	 * @see 注意要在androidManifest.xml中配置权限
	 * */
	public static int checkNetWorkStatus(Context context){
		int status = 0;
		ConnectivityManager connMgr = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(wifi.isConnectedOrConnecting()){
			status = WIFI_ON;
		}else{
			status = WIFI_OFF;
		}
		if(mobile.isConnectedOrConnecting()){
			status = MOBILE_ON;
		}else{
			status = MOBILE_OFF;
		}
		
		return status;
	}
	
	
	/**
	 * 获取网络状态
	 * @param context
	 * @return  true 有网络
	 *          fasle 无网络
	 * */
	public static boolean getNetWorkStatus(Context context){
		boolean flag = false;
		ConnectivityManager connMgr = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(!wifi.isConnectedOrConnecting() && !mobile.isConnectedOrConnecting()){
			flag = false;
		}else{
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 獲取sdcard路徑
	 */
	public static String getSDCardRoot(){
		return Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator;
	}

}
