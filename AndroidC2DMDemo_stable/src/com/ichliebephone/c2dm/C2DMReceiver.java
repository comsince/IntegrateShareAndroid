package com.ichliebephone.c2dm;

import java.io.IOException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.c2dm.C2DMBaseReceiver;
//接收C2DM服务器Push的消息，包括注册返回的registration_id消息，推送的数据消息等
public class C2DMReceiver extends C2DMBaseReceiver{
	
	private static final String TAG="C2DMReceiver";
	//
	public C2DMReceiver()
	{
		super(AndroidC2DMDemo.SENDER_ID);
	}
	public C2DMReceiver(String senderId) {
		super(senderId);
		// TODO Auto-generated constructor stub
	}
	//接收到Push消息的回调函数
	@Override
	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.v(TAG, "C2DMReceiver message");
		Bundle extras = intent.getExtras();
		if(extras!=null){
			String msg = (String)extras.get(AndroidC2DMDemo.MESSAGE_KEY_ONE);
			Log.v(TAG, "The received msg = "+msg);
			//在标题栏上显示通知
			NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			Notification notification = new Notification(R.drawable.icon, msg, System.currentTimeMillis());
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, AndroidC2DMDemo.class), 0);
			notification.setLatestEventInfo(this, getString(R.string.app_name), msg, contentIntent);
			notificationManager.notify(0, notification);
			
		}
	}

	@Override
	public void onError(Context context, String errorId) {
		// TODO Auto-generated method stub
		Log.v(TAG, "C2DMReceiver error");
	}
	
	@Override
	public void onRegistered(Context context, String registrationId)
			throws IOException {
		// TODO Auto-generated method stub
		super.onRegistered(context, registrationId);
		Log.v(TAG, "C2DMReceiver Register");
	}
	@Override
	public void onUnregistered(Context context) {
		// TODO Auto-generated method stub
		super.onUnregistered(context);
		Log.v(TAG, "C2DMReceiver UnRegister");
	}	
}
