package com.comsince.knowledge.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

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
	
	/**
	 * 
	 * @param context
	 * @param lrc_color
	 */
	public void savaLrcColor(Context context, int lrc_color) {
		sharedPreferences.edit().putInt("lrc_color", lrc_color).commit();
	}

	/**
	 * 
	 * @param context
	 * @return int lrc_color 
	 */
	public int getLrcColor(Context context) {
		return sharedPreferences.getInt("lrc_color", Color.rgb(51, 181, 229));
	}
	
	/**
	 * 
	 * @param context
	 * @param lrc_size
	 */
	public void savaLrcSize(Context context, int lrc_size) {
		sharedPreferences.edit().putInt("lrc_size", lrc_size).commit();
	}

	/**
	 * 
	 * @param context
	 * @return int lrc_size 
	 */
	public int getLrcSize(Context context) {
		return sharedPreferences.getInt("lrc_size", 25);
	}
	
	/**
	 * 保存歌曲退出时的 播放位置
	 * 
	 * @param context
	 * @param position
	 */
	public void savaPlayPosition(Context context, int position) {
		sharedPreferences.edit().putInt("position", position).commit();
	}

	/**
	 * 获取退出时的播放位置
	 * 
	 * @param context
	 * @return
	 */
	public int getsaveposition(Context context) {
		return sharedPreferences.getInt("position", 0);
	}


}
