package com.comsince.knowledge.layout;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.comsince.knowledge.MyApplication;
import com.comsince.knowledge.R;
import com.comsince.knowledge.adapter.LocalMusicListAdapter;
import com.comsince.knowledge.constant.Constant;
import com.comsince.knowledge.entity.Music;

public class LocalLayout extends LinearLayout {
	private static String TAG = "Aisa";
	View rootview;
	ListView localistview;
	LayoutInflater inflater;
	Context context;
	List<Music> musicList;
	LocalMusicListAdapter adapter;

	public LocalLayout(Context context) {
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
		Log.d(TAG, "musicList size :" + musicList.size());
		adapter = new LocalMusicListAdapter(context, musicList,localistview);
		localistview.setAdapter(adapter);
	}
	
	public void initListener(){
		localistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View itemView, int position, long arg3) {
				Log.d("LocalLayout", itemView.toString());
				//((LocalMusicListAdapter)localistview.getAdapter()).showNowPlayPos(position);
				goplay(position);
			}
		});
	}
	/**
	 * 发送广播给播放service,通知其播放选中的音乐
	 * */
	public void goplay(int position) {
		Intent intent = new Intent(Constant.ACTION_JUMR);
		intent.putExtra("position", position);
		context.sendBroadcast(intent);
		Log.d("netlayout boardcast", Constant.ACTION_JUMR);
	}

	public ListView getLocalistview() {
		return localistview;
	}

	public void setLocalistview(ListView localistview) {
		this.localistview = localistview;
	}
	
	

}
