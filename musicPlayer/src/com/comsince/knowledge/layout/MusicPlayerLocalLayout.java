package com.comsince.knowledge.layout;

import java.util.List;

import com.comsince.knowledge.MyApplication;
import com.comsince.knowledge.R;
import com.comsince.knowledge.adapter.LocalMusicListAdapter;
import com.comsince.knowledge.entity.Music;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MusicPlayerLocalLayout extends LinearLayout{
	View rootview;
	ListView localistview;
	LayoutInflater inflater;
	Context context;
	List<Music> musicList;
	LocalMusicListAdapter adapter;

	public MusicPlayerLocalLayout(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		rootview = inflater.inflate(R.layout.musiclist, this, true);
		initView();
		initData();
		initListener();
	}
	
	public void initView(){
		localistview = (ListView) rootview.findViewById(R.id.lvSounds);
	}
	
	public void initData(){
		musicList = MyApplication.musics;
		adapter = new LocalMusicListAdapter(context, musicList,localistview);
		localistview.setAdapter(adapter);
	}
	
	public void initListener(){
		
	}

	public ListView getLocalistview() {
		return localistview;
	}

	public void setLocalistview(ListView localistview) {
		this.localistview = localistview;
	}
	
	

}
