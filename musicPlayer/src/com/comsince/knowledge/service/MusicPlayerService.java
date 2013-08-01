package com.comsince.knowledge.service;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
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
	private int current = 0;
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
	/**
	 * 播放模式 0 顺序播放 1 随机播放 2 单曲循环
	 * */
	private int playMode = 0;
	/**
	 * 单曲播放记录当前播放的位置
	 * */
	public int singlePlayCurrent = 0;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("service boardcast", "onCreate");
		context = this;
		// 获取mediaPlayer
		mPlayer = MyApplication.mediaPlayer;
		// 1 实例化广播接收器
		mReceiver = new MyReciever();
		// 获取音乐列表
		musicList = ((MyApplication) getApplication()).getMusics();
		//实现当前歌曲播放完后自动播放下一曲的功能
		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				next();
			}
		});
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
		filter.addAction(Constant.ACTION_PAUSE);
		filter.addAction(Constant.ACTION_PLAY);
		filter.addAction(Constant.ACTION_PREVIOUS);
		filter.addAction(Constant.ACTION_NEXT);
		//musicPlay 增加更新歌曲信息的action
		filter.addAction(Constant.ACTION_UPDATE_ALL);
		//播放模式filter
		filter.addAction(Constant.ACTION_SET_PLAYMODE);
		
		/**
		 * 1.定义自己的boardcastReceiver,并重写onReceive方法 2.给boardcastReceiver 加fileter
		 * 
		 * */
		registerReceiver(mReceiver, filter);
		
		playMode = MyApplication.musicPreference.getPlayMode(context);
	}
	/**
	 * 播放点击的某一位置歌曲
	 * 
	 * @param position
	 */
	boolean isJumpToNowMusic = false;
	private void jump(int position) {
		if (musicList != null && musicList.size() > 0) {
			current = position;
			singlePlayCurrent = position;
			isJumpToNowMusic = true;
			play();
		}
	}

	/**
	 * 播放音乐
	 * */
	private void play() {
		if (musicList != null && musicList.size() > 0) {
			//根据播放模式选取歌曲播放
			if(playMode == Constant.PLAY_MODE_BY_RANDOM){
				//如果是点击进去歌曲播放不促发随机播放算法
				if(!isJumpToNowMusic){
					current = new Random().nextInt(musicList.size());
				}
			}else if(playMode == Constant.PLAY_MODE_BY_SINGLE){
				current = singlePlayCurrent;
			}
			nowPlayMusic = musicList.get(current);
			try {
				mPlayer.reset();
				mPlayer.setDataSource(nowPlayMusic.getSavePath());
				mPlayer.prepare();
				mPlayer.start();
				status = 3;
				totalms = mPlayer.getDuration();
				Log.d("Asia", "service totalms" + totalms);
				updateAllMusicInfo();
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
	 * 播放上一首歌曲
	 * */
	private void previous() {
		Log.d("MusicPlayerService", "previous");
		Log.d("MusicPlayerService", "previous current :" + current);
      if(musicList != null && musicList.size() > 0){
    	  if(current == 0){
    		  current = musicList.size()-1;
    	  }else{
    		  current--;
    	  }
    	  play();
      }
	}

	/**
	 * 播放下一首歌曲
	 * */
	private void next() {
		Log.d("MusicPlayerService", "next");
		Log.d("MusicPlayerService", "next current :" + current);
		if (musicList != null && musicList.size() > 0) {
			if (current == musicList.size() - 1) {
				current = 0;
			} else {
				current++;
			}
			play();
		}
	}

	/**
	 * 更新当前播放音乐的信息
	 * */
	private Intent updateIntent;

	private void updateAllMusicInfo() {
		if (updateIntent == null) {
			updateIntent = new Intent(Constant.ACTION_UPDATE);
		}
		updateIntent.putExtra("status", status);
		updateIntent.putExtra("music", nowPlayMusic);
		updateIntent.putExtra("totalms", totalms);
		updateIntent.putExtra("position", current);
		sendBroadcast(updateIntent);
	}

	/**
	 * 接收来自activity的播放任务并作相应的处理
	 * */
	private class MyReciever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constant.ACTION_JUMR.equals(intent.getAction())) {
				Log.d("service boardcast", Constant.ACTION_JUMR);
				//Toast.makeText(MusicPlayerService.this, "进入播放service", Toast.LENGTH_SHORT).show();
				int position = intent.getIntExtra("position", 0);
				if (position >= 0) {
					jump(position);
				}
			} else if (Constant.ACTION_PAUSE.equals(intent.getAction())) {
				// 暂停播放
				//updataAllMusicInfo();
				mPlayer.pause();
				status = 2;
			} else if (Constant.ACTION_PLAY.equals(intent.getAction())) {
				switch (status) {
				case 2:
					//updateAllMusicInfo();
					mPlayer.start();
					status = 3;
					break;

				default:
					break;
				}
			} else if (Constant.ACTION_NEXT.equals(intent.getAction())) {
				next();
				status = 3;
			} else if (Constant.ACTION_PREVIOUS.equals(intent.getAction())) {
				previous();
				status = 3;
			} else if(Constant.ACTION_UPDATE_ALL.equals(intent.getAction())){
			   	updateAllMusicInfo();
			} else if(Constant.ACTION_SET_PLAYMODE.equals(intent.getAction())){
				//设置播放模式
				playMode = intent.getIntExtra("play_mode", -1);
				if(playMode == Constant.PLAY_MODE_BY_SINGLE){
					singlePlayCurrent = current;
				}
			}
		}

	}

}
