package com.comsince.knowledge.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class MusicPreference {
	SharedPreferences sharedPreferences;

	public MusicPreference(Context context) {
		sharedPreferences = context.getSharedPreferences("music_preference", Context.MODE_PRIVATE);
	}
	/**
	 * 保存 播放模式
	 * 
	 * @param context
	 * @param playmode
	 *            0 顺序播放 1 随机播放 2 单曲循环
	 */
	public void savaPlayMode(Context context, int playmode) {
		sharedPreferences.edit().putInt("playmode", playmode).commit();
	}
	
	/**
	 * 获取播放模式
	 * 
	 * @param context
	 * @return int playmode 0 顺序播放 1 随机播放 2 单曲循环
	 */
	public int getPlayMode(Context context) {
		return sharedPreferences.getInt("playmode", 0);
	}
	
	/**
	 * 保存当前歌曲的播放位置
	 * */
	public void saveMusicCurrentMs(Context context, int curMs){
		sharedPreferences.edit().putInt("curMs", curMs);
	}
	/**
	 * 获取当前播放歌曲的播放位置
	 * */
	public int getMusicCurrentMs(Context context){
		return sharedPreferences.getInt("curMs", 0);
	}

}
