package com.comsince.knowledge.service;

import com.comsince.knowledge.constant.Constant;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MusicPlayerService extends Service {
	//播放广播接收器
	private MyReciever mReceiver;
	Context context;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("service boardcast", "onCreate");
		context = this;
		//1 实例化广播接收器
		mReceiver = new MyReciever();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_JUMR);
		//2给boardcastReceiver 加fileter
		registerReceiver(mReceiver, filter);
	}
	/**
	 * 接收来自activity的播放任务并作相应的处理
	 * */
	private class MyReciever extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(Constant.ACTION_JUMR.equals(intent.getAction())){
				Log.d("service boardcast", Constant.ACTION_JUMR);
				Toast.makeText(MusicPlayerService.this, "进入播放service", Toast.LENGTH_SHORT).show();
			}
		}
		
	}

}
