package com.ichliebephone.c2dm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class C2DMRegistration {

	//注册C2DM服务
	public static void register(Context context, String senderID){
		Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(context, 0, new Intent(), 0)); // boilerplate
		registrationIntent.putExtra("sender", senderID);
		context.startService(registrationIntent);
	}
	//取消C2DM服务
	public static void unregister(Context context){
		Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
		unregIntent.putExtra("app", PendingIntent.getBroadcast(context, 0, new Intent(), 0));
		context.startService(unregIntent);
	}
	//保存注册成功获得的registration_id值
	static void setRegistraionID(Context context, String registrationId){
        final SharedPreferences prefs = context.getSharedPreferences(
                "c2dm_preference",
                Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("dm_registration", registrationId);
        editor.commit();
	}
	//获取保存的registration_id值
	public static String getRegistrationID(Context context){
        final SharedPreferences prefs = context.getSharedPreferences(
        		"c2dm_preference",
                Context.MODE_PRIVATE);
        String registrationId = prefs.getString("dm_registration", "");
        return registrationId;
	}
    //当取消C2DM服务时，清空之前保存着的registration_id值
    static void clearRegistrationId(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(
        		"c2dm_preference",
                Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("dm_registration", "");
        editor.commit();
    }
    //当SERVICE_NOT_AVAILABLE时，需要回退一定时间后重新启动注册
    //获取当前的回退等待时间
    static long getBackoff(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(
        		"c2dm_preference",
                Context.MODE_PRIVATE);
        //默认回退等待时间为30000微秒
        return prefs.getLong("back_off", 30000);
    }
    //设置回退等待时间
    static void setBackoff(Context context, long backoff) {
        final SharedPreferences prefs = context.getSharedPreferences(
        		"c2dm_preference",
                Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putLong("back_off", backoff);
        editor.commit();
    }
}
