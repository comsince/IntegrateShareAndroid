package com.comsince.knowledge.layout;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.comsince.knowledge.R;
import com.comsince.knowledge.adapter.LocalMusicListAdapter;
import com.comsince.knowledge.constant.Constant;
import com.comsince.knowledge.entity.Music;
import com.comsince.knowledge.utils.MusicDataUtil;
import com.comsince.knowledge.view.xlistview.MsgListView;
import com.comsince.knowledge.view.xlistview.MsgListView.IXListViewListener;
import com.tarena.fashionmusic.MyApplication;

public class LocalLayout extends LinearLayout implements IXListViewListener{
	private static String TAG = "Aisa";
	View rootview;
	MsgListView localistview;
	LayoutInflater inflater;
	Context context;
	List<Music> musicList;
	LocalMusicListAdapter adapter;
	ScanSdReceiver scanSdReceiver;

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
		localistview = (MsgListView) rootview.findViewById(R.id.lvSounds);
		//禁用上拉加载数据
		localistview.setPullLoadEnable(false);
		localistview.setXListViewListener(this);
	}
	
	public void initData(){
		
		musicList = MyApplication.musics;
		Log.d(TAG, "musicList size :" + musicList.size());
		adapter = new LocalMusicListAdapter(context, musicList,localistview);
		if(musicList.size() > 0){
			localistview.setAdapter(adapter);
		}
	}
	
	public void initListener(){
		localistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View itemView, int position, long arg3) {
				Log.d("LocalLayout", itemView.toString());
				//((LocalMusicListAdapter)localistview.getAdapter()).showNowPlayPos(position);
				Log.d("LocalLayout ", "onclick position :"+position);
				if(position != 0){
					goplay(position -1);
				}
				//goplay(position);
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

	public MsgListView getLocalistview() {
		return localistview;
	}

	public void setLocalistview(MsgListView localistview) {
		this.localistview = localistview;
	}

	@Override
	public void onRefresh() {
		Log.d("scan", "RefreshStart");
		scanSdCard();
	}

	@Override
	public void onLoadMore() {
		
	}
	
	/**
	 * 开始媒体库扫描
	 * */
	public void scanSdCard() {
		IntentFilter intentfilter = new IntentFilter(Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentfilter.addDataScheme("file");
		scanSdReceiver = new ScanSdReceiver();
		context.registerReceiver(scanSdReceiver, intentfilter);
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath())));
	}

	public class ScanSdReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(Intent.ACTION_MEDIA_SCANNER_STARTED)){
				Log.d("scan", "Start");
			}else if(action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)){
				musicList = MusicDataUtil.getMultiDatas(context);
				Log.d("scan", "Finish");
				localistview.stopRefresh();
				//刷新adapter
				Log.d("scan" , String.valueOf(musicList.size()));
			    adapter.refreshData(musicList);
			}
		}
		
	}

}
