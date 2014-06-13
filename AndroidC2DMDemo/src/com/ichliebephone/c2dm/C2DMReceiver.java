package com.ichliebephone.c2dm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class C2DMReceiver extends BroadcastReceiver{
	//
	private static final String TAG="C2DMReceiver";
	//
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")){
			//如果是注册返回的Intent
			handleRegistration(context, intent);
		}else if(intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")){
			//如果是接收到C2DM推送消息的Intent
			handleMessage(context, intent);
		}
	}
	//注册后的回调处理
	private void handleRegistration(final Context context, Intent intent){
		//如果是注册成功，则Intent中包含"registration_id"键对应的值
        final String registrationId = intent.getStringExtra("registration_id");
        //如果是注册失败，则Intent中包含"error"键对应的值
        String error = intent.getStringExtra("error");
        //如果是取消注册，则Intent中包含"unregistered"键对应的值
        String removed = intent.getStringExtra("unregistered");
        //打印出接收到的registraton_id，为了调试是查看
        Log.v(TAG, "handleRegistration");
        Log.v(TAG, "dmControl: registrationId = " + registrationId +
                ", error = " + error + ", removed = " + removed);
        //
        if(removed != null){
        	//如果存在"unregistered"键对应的值，则表示是取消注册
        	onUnregistrated(context);
        	return;
        }else if(error != null){
        	//如果存在"error"键对应的值，则表示注册失败
        	onError(context, error);
        	return;
        }else{
        	//如果以上两种情况都不存在，则表示注册成功了
        	onRegistrated(context, registrationId);
        }
        
	}
	//接收到C2DM推送信息的回调处理
	private void handleMessage(Context context, Intent intent){
		onMessage(context, intent);
	}
	//和C2DM推送信息相关的具体处理函数
	//注册成功后的处理函数
	private void onRegistrated(Context context, String registrationId){
		Log.v(TAG, "C2DMReceiver Register with the registrationId = " + registrationId);
		//注册成功的话，保存获取的registrationId值
		C2DMRegistration.setRegistraionID(context, registrationId);
		//然后把registrationId值发送给我们自己的第三方服务器
	}
	//取消注册后的处理函数
	private void onUnregistrated(Context context){
		Log.v(TAG, "C2DMReceiver Unregister");
		//取消注册的话，同时也清除保存着的registrationId值
    	C2DMRegistration.clearRegistrationId(context);
	}
	//出错的处理函数
	private void onError(Context context, String errorId){
		Log.v(TAG, "C2DMReceiver Error with the reason: " + errorId);
		//首先清除保存着的registrationId值
		C2DMRegistration.clearRegistrationId(context);
		//判断出错的原因,一共有六种原因：SERVICE_NOT_AVAILABLE，ACCOUNT_MISSING，AUTHENTICATION_FAILED，
		//TOO_MANY_REGISTRATIONS,INVALID_SENDER,PHONE_REGISTRATION_ERROR
		if("SERVICE_NOT_AVAILABLE".equals(errorId)){
			//只有SERVICE_NOT_AVAILABLE这个是C2DM服务器没有响应的原因，可以等待一段时间后重新发送注册请求
			//其他原因都是属于客户端设备没有处理好的原因
			long backoffTimeMs = C2DMRegistration.getBackoff(context);
			Intent retryIntent = new Intent("com.google.android.c2dm.intent.RETRY");
			PendingIntent retryPIntent = PendingIntent.getBroadcast(context, 
                    0 /*requestCode*/, retryIntent, 0 /*flags*/);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.ELAPSED_REALTIME,
                    backoffTimeMs, retryPIntent);
            // 下一次重试时等待更长的时间
            backoffTimeMs *= 2;
            C2DMRegistration.setBackoff(context, backoffTimeMs);
		}
		
		
	}
	//接收到推送消息后的处理函数
	private void onMessage(Context context, Intent intent){
		Log.v(TAG, "C2DMReceiver Message");
		Bundle extras = intent.getExtras();
		if(extras!=null){
			//根据键值，提取对应的消息内容
			String msg = (String)extras.get(AndroidC2DMDemo.MESSAGE_KEY_ONE);
			Log.v(TAG, "The received msg = "+msg);
//			//在标题栏上显示通知
			NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(R.drawable.icon, msg, System.currentTimeMillis());
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, AndroidC2DMDemo.class), 0);
			notification.setLatestEventInfo(context, context.getString(R.string.app_name), msg, contentIntent);
			notificationManager.notify(0, notification);			
		}
	}
}
