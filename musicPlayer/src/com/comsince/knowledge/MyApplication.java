package com.comsince.knowledge;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.comsince.knowledge.constant.Constant;
import com.comsince.knowledge.entity.Music;
import com.comsince.knowledge.preferences.MusicPreference;
import com.comsince.knowledge.utils.MusicDataUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class MyApplication extends Application {
	public static MediaPlayer mediaPlayer;
	public static MusicPreference musicPreference;
	public static Context context;
	public static List<Music> musics = new ArrayList<Music>();
	// 微信通信api接口
	public static IWXAPI api;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 加载音乐文件列表
				setMusics(MusicDataUtil.getMultiDatas(context));
			}
		}).start();
		// 初始化mediaPlayer
		mediaPlayer = new MediaPlayer();
		musicPreference = new MusicPreference(context);
		// 注册到微信
		regToWx();
	}

	/**
	 * 注册微信
	 * */
	public void regToWx() {
		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);
		api.registerApp(Constant.APP_ID);
	}

	/**
	 * 设置music集合
	 * 
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
