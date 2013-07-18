package com.comsince.knowledge;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.comsince.knowledge.entity.Music;
import com.comsince.knowledge.utils.MusicDataUtil;

public class MyApplication extends Application {
	public static MediaPlayer mediaPlayer;
	public static Context context;
	public static List<Music> musics = new ArrayList<Music>();
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//加载音乐文件列表
				setMusics(MusicDataUtil.getMultiDatas(context));
			}
		}).start();
		//初始化mediaPlayer
		mediaPlayer = new MediaPlayer();
	}
	
	/**
	 * 设置music集合
	 * @param musics
	 */
	public void setMusics(List<Music> ms) {
		musics.clear();
		musics = ms;
		Log.i("test", "列表长度" + this.musics.size());
	}

	public static List<Music> getMusics() {
		return musics;
	}

 
}
