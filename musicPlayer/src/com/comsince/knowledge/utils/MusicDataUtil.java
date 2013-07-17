package com.comsince.knowledge.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.comsince.knowledge.entity.Music;

public class MusicDataUtil {
	/**
	 * 循环遍历歌曲列表
	 * 
	 * @param context 
	 * @return
	 * */
	public static List<Music> getMultiDatas(Context context) {
		ArrayList<Music> musics = new ArrayList<Music>();
		ContentResolver contentResolver = context.getContentResolver();
		Cursor musicCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		int musicColumnIndex;
		Music music;
		if (musicCursor != null && musicCursor.getCount() > 0) {
			for (musicCursor.moveToFirst(); !musicCursor.isAfterLast(); musicCursor.moveToNext()) {
				music = new Music();
				music.setId(musicCursor.getInt(musicCursor.getColumnIndex("_id")));
				musicColumnIndex = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
				music.setMusicName(musicCursor.getString(musicColumnIndex));
				musicColumnIndex = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
				music.setSavePath(musicCursor.getString(musicColumnIndex));
				musicColumnIndex = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM);
				music.setAlbumName(musicCursor.getString(musicColumnIndex));
				musicColumnIndex = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
				music.setSinger(musicCursor.getString(musicColumnIndex));
				musicColumnIndex = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);
				music.setTime(musicCursor.getString(musicColumnIndex));
				musicColumnIndex = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_KEY);
				music.setAlbumkey(musicCursor.getString(musicColumnIndex));
				musics.add(music);
			}
			musicCursor.close();
		}
		return musics;
	}
}
