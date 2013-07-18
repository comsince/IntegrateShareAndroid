package com.comsince.knowledge.service;

import java.io.IOException;
import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.comsince.knowledge.MyApplication;
import com.comsince.knowledge.constant.Constant;
import com.comsince.knowledge.entity.Music;

public class MusicPlayerService extends Service {
	public static MediaPlayer mPlayer;
	Context context;
	private List<Music> musicList;
	/**
	 * 播放广播接收器
	 */
	private MyReciever mReceiver;
	/**
	 * 当前播放歌曲序号
	 * */
	private int current;
	/**
	 * 当前播放的音乐
	 * */
	private Music nowPlayMusic;
	/**
	 * @播放状态 1 未播放 2 暂停 3 播放
	 * */
	public static int status = 1;
	/**
	 * 当前歌曲总时长
	 * */ 
	private int totalms = 0;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("service boardcast", "onCreate");
		context = this;
		//获取mediaPlayer
		mPlayer = MyApplication.mediaPlayer;
		//1 实例化广播接收器
		mReceiver = new MyReciever();
		//获取音乐列表
		musicList = ((MyApplication)getApplication()).getMusics();
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
		/**
		 * 1.定义自己的boardcastReceiver,并重写onReceive方法
		 * 2.给boardcastReceiver 加fileter
		 * 
		 * */
		registerReceiver(mReceiver, filter);
	}
	
	private void jump(int position){
		if(musicList!=null && musicList.size()>0){
			current = position;
			play();
		}
	}
	/**
	 * */
	private void play(){
		if(musicList!=null && musicList.size()>0){
			nowPlayMusic = musicList.get(current);
			try {
				mPlayer.reset();
				mPlayer.setDataSource(nowPlayMusic.getSavePath());
				mPlayer.prepare();
				mPlayer.start();
				status = 3;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
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
				int position = intent.getIntExtra("position", 0);
				if (position >= 0) {
					jump(position);
				}
			}
		}
		
	}

}
