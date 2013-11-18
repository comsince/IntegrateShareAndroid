package com.comsince.knowledge.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.comsince.knowledge.R;
import com.comsince.knowledge.adapter.LocalMusicListAdapter;
import com.comsince.knowledge.adapter.MyPagerAdapter;
import com.comsince.knowledge.constant.Constant;
import com.comsince.knowledge.entity.BaiduDevMusic;
import com.comsince.knowledge.entity.Music;
import com.comsince.knowledge.entity.TingMusicJson;
import com.comsince.knowledge.layout.FavoriteLayout;
import com.comsince.knowledge.layout.LocalLayout;
import com.comsince.knowledge.layout.NetLayout;
import com.comsince.knowledge.layout.LocalLayout.ScanSdReceiver;
import com.comsince.knowledge.lrcutil.BaiduLrc;
import com.comsince.knowledge.service.DownloadService;
import com.comsince.knowledge.service.DownloadService.DownLoadBinder;
import com.comsince.knowledge.service.MusicPlayerService;
import com.comsince.knowledge.uikit.MMAlert;
import com.comsince.knowledge.utils.BitmapTool;
import com.comsince.knowledge.utils.StrTime;
import com.tarena.fashionmusic.MyApplication;
import com.tarena.fashionmusic.play.MusicPlayActivity;

public class MainActivity extends Activity implements OnClickListener{
	// 基本控件
	ViewPager viewPager;
	TextView localmusic, favoritemusic, netmusic;
	ImageView listShowAlbum;
	TextView musicName, musicTime;
	ImageButton preBtn, playBtn, nextBtn;
	ProgressBar MusicprogressBar;
	Context context;
	// 装载滑动页面
	List<View> pageViews;
	LayoutInflater inflater;
	// 滑动布局
	FavoriteLayout favorLayout;
	LocalLayout localLayout;
	NetLayout netLayout;
	MyPagerAdapter pagerAdapter;
	/**
	 * 记录当前音乐的播放状态
	 */
	boolean isPlaying = false; 
	/**
	 * musicReceiver
	 * */
	MusicReceiver musicReceiver;
	/**
	 * music thread 更新播放进度的线程
	 * */
	MusicProgressThread musicProgressThread;

	private static String TAG = "Aisa";

	private DownLoadBinder downLoadBinder;
	// 绑定和解绑service时的回调对象conn
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			downLoadBinder = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			downLoadBinder = (DownLoadBinder) service;
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.playermain);
		context = this;
		inflater = LayoutInflater.from(this);
		initView();
		//百度云推送服务初始化api
		initBaiduPushService();
		// 实例化musicReceiver
		musicReceiver = new MusicReceiver();
		// 启动service
		startService(new Intent(context, MusicPlayerService.class));
		Intent downLoadIntent = new Intent(context, DownloadService.class);
        startService(downLoadIntent);
		this.getApplicationContext().bindService(downLoadIntent, conn, BIND_AUTO_CREATE);
	}
	

	@Override
	protected void onStart() {
		super.onStart();
		//从sharedPreference读取数据初始化当前播放歌曲
		position = MyApplication.musicPreference.getsaveposition(context);
		//启动更新当前播信息
		sendBroadcast(new Intent(Constant.ACTION_UPDATE_ALL));
		showNowPalyMusicInfo();
	}


	@Override
	protected void onResume() {
		super.onResume();
		initpageData();
		// 给musicReceiver加intent过滤器
		IntentFilter musicFilter = new IntentFilter();
		musicFilter.addAction(Constant.ACTION_UPDATE);
		// 注册监听器
		registerReceiver(musicReceiver, musicFilter);
		//启动更新音乐播放进度的线程
		isrunable= true;
		musicProgressThread = new MusicProgressThread();
		musicProgressThread.start();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(musicReceiver);
		if(favorLayout.getVoiceDialog() != null){
			favorLayout.getVoiceDialog().dismiss();
		}
		super.onDestroy();
	}


	@Override
	protected void onStop() {
		isrunable = false;
		super.onStop();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// 当keyCode等于退出事件值时
			ToQuitTheApp();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	/**
	 * 退出
	 * */ 
	public boolean isExit = false;
	private void ToQuitTheApp() {
		if (isExit) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			//System.exit(0);// 使虚拟机停止运行并退出程序
		} else {
			isExit = true;
			Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 3000);// 3秒后发送消息
		}
	}

	// 创建Handler对象，用来处理消息
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// 处理消息
			super.handleMessage(msg);
			isExit = false;
		}
	};

	/**
	 * 
	 * 初始化界面
	 **/
	public void initView() {
		// 滑动控件
		viewPager = (ViewPager) findViewById(R.id.center_body_view_pager);
		pageViews = new ArrayList<View>();

		localLayout = new LocalLayout(context);
		favorLayout = new FavoriteLayout(context);
		netLayout = new NetLayout(context);
		// 加入时注意顺序
		pageViews.add(localLayout);
		pageViews.add(favorLayout);
		pageViews.add(netLayout);

		// 顶部三个导航按钮
		localmusic = (TextView) findViewById(R.id.localmusic);
		favoritemusic = (TextView) findViewById(R.id.favoritemusic);
		netmusic = (TextView) findViewById(R.id.netmusic);
		// 播放进度条
		MusicprogressBar = (ProgressBar) findViewById(R.id.musicProgressBar);
		// 底部播放按钮
		listShowAlbum = (ImageView) findViewById(R.id.list_show_album);
		musicName = (TextView) findViewById(R.id.musicName);
		musicTime = (TextView) findViewById(R.id.musicTime);
		preBtn = (ImageButton) findViewById(R.id.prebtn);
		nextBtn = (ImageButton) findViewById(R.id.nextbtn);
		playBtn = (ImageButton) findViewById(R.id.playbtn);
		// 设置监听器
		setupListener();
	}

	/**
	 * 设置监听器
	 * */
	public void setupListener() {
		localmusic.setOnClickListener(new MyMusicListener());
		favoritemusic.setOnClickListener(new LoveMusicListener());
		netmusic.setOnClickListener(new NetMusicListener());
		preBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		playBtn.setOnClickListener(this);
		listShowAlbum.setOnClickListener(this);
		favorLayout.getSearchResultList().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				final BaiduDevMusic baiduDevMusic = (BaiduDevMusic) favorLayout.getSearchResultList().getAdapter().getItem(position);
				MMAlert.showAlert(context, context.getString(R.string.down_info), context.getResources().getStringArray(R.array.download_music_item), null, new MMAlert.OnAlertSelectId(){

					@Override
					public void onClick(int whichButton) {
						switch (whichButton) {
						case Constant.MMALERT_DOWNLOAD_MUSIC:
							downLoadMusic(baiduDevMusic);
							break;
						case Constant.MMALERT_SHARE_MUSIC:
							break;
						case Constant.MMALERT_CANCEL_MUSIC:
							break;
						default:
							break;
						}
						
						
					}
					
				});
			}
		});
	}
	/**
	 * 下载音乐
	 * */
	public void downLoadMusic(BaiduDevMusic baiduDevMusic){
		final String songId = baiduDevMusic.getSong_id();
		if(!TextUtils.isEmpty(songId)){
			//android.os.NetworkOnMainThreadException 不能在主线程上访问网络等耗时操作
			new Thread(){
				@Override
				public void run() {
					TingMusicJson musicJoson = BaiduLrc.getTingMusicJsonBySongId(songId);
					downLoadBinder.addTask(musicJoson);
				}
				
			}.start();
			
		}
	}
	/**
	 * 初始化当前音乐播放信息
	 * */
	public void showNowPalyMusicInfo(){
		if(MyApplication.musics.size() != 0){
			Music music = MyApplication.musics.get(position);
			if (music != null) {
				nowBitMap = BitmapTool.getbitBmBykey(context, music.getAlbumkey());
				if (nowBitMap != null && nowBitMap.isRecycled() == false) {
					listShowAlbum.setImageBitmap(nowBitMap);
				} else {
					listShowAlbum.setImageResource(R.drawable.default_bg_s);
				}
				musicName.setText(music.getMusicName());
				musicTime.setText("00:00/" + StrTime.getTime(music.getTime()));
			}
		}
		
	}

	public void initpageData() {
		if (viewPager.getChildCount() <= 0) {
			pagerAdapter = new MyPagerAdapter(pageViews);
		}
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				localmusic.setSelected(false);
				favoritemusic.setSelected(false);
				netmusic.setSelected(false);
				switch (position) {
				case 0:
					localmusic.setSelected(true);
					break;
				case 1:
					favoritemusic.setSelected(true);
					break;
				case 2:
					netmusic.setSelected(true);
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		viewPager.setCurrentItem(0);
		localmusic.setSelected(true);
	}

	// 我的音乐
	class MyMusicListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			localmusic.setSelected(true);
			favoritemusic.setSelected(false);
			netmusic.setSelected(false);
			viewPager.setCurrentItem(0);
		}

	}

	// 最喜欢的歌曲列表
	class LoveMusicListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			localmusic.setSelected(false);
			favoritemusic.setSelected(true);
			netmusic.setSelected(false);
			viewPager.setCurrentItem(1);
		}

	}

	// 网络歌曲列表
	class NetMusicListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			localmusic.setSelected(false);
			favoritemusic.setSelected(false);
			netmusic.setSelected(true);
			viewPager.setCurrentItem(2);
		}

	}

	/**
	 * 定义更新底部播放控件的状态，播放,暂停 播放的图片，音乐名称，音乐的时间
	 * */
	/**
	 * 音乐的总时间
	 * */
	int totalms = 1;
	/**
	 * 播放状态
	 * */
	int status;
	/**
	 * 当前选中的歌曲位置，也是其在listview中的位置
	 * */
	public int position;
	public Bitmap nowBitMap;
	public Music music;

	private class MusicReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constant.ACTION_UPDATE)) {
				// 实现从intent获取entity,该类必须实现Serializable接口
				music = (Music) intent.getSerializableExtra("music");
				//如果初始广播music为空，则初始化
				if(music == null){
					music = MyApplication.musics.get(position);
				}
				totalms = intent.getIntExtra("totalms", 28888);
				status = intent.getIntExtra("status", -1);
				position = intent.getIntExtra("position", 0);
				String musicname = music.getMusicName();
				musicName.setText(musicname);
				String musictime = music.getTime();
				//totalms = Integer.parseInt(musictime);
				musicTime.setText("00:00/" + StrTime.getTime(musictime));
				// 设置音乐图片
				String albumkey = music.getAlbumkey();
				Log.d("MainActivity", "music albumkey: "+music.getAlbumkey());
				if (!TextUtils.isEmpty(albumkey)) {
					nowBitMap = BitmapTool.getbitBmBykey(context, albumkey);
				}
				if (nowBitMap != null && !nowBitMap.isRecycled()) {
					listShowAlbum.setImageBitmap(nowBitMap);
				} else {
					listShowAlbum.setImageResource(R.drawable.default_bg_s);
				}
				if (status == 3) {
					playBtn.setImageResource(R.drawable.desktop_pausebt);
					isPlaying = true;
				}else{
					playBtn.setImageResource(R.drawable.play_play_btn);
					isPlaying  = false;
				}
				//更新listview歌曲播放选中状态
				if(localLayout.getLocalistview()!=null){
					((LocalMusicListAdapter)localLayout.getLocalistview().getAdapter()).showNowPlayPos(position);
					int lastPostion = localLayout.getLocalistview().getLastVisiblePosition();
					int prePostion = localLayout.getLocalistview().getFirstVisiblePosition();
					//居中显示
					//localLayout.getLocalistview().setSelection(position);
					int listViewHeight = localLayout.getLocalistview().getHeight();
					//localLayout.getLocalistview().setSelectionFromTop(position, listViewHeight/2);
					if(position >= lastPostion ||position <= prePostion){
						localLayout.getLocalistview().setSelection(position);
						listViewHeight = localLayout.getLocalistview().getHeight();
						localLayout.getLocalistview().setSelectionFromTop(position, listViewHeight/2);
						
					}
				}
				Log.d(TAG, "position"+String.valueOf(position));
			}
		}

	}

	/**
	 * 监听musicPalyer,进而更新播放进度
	 * 
	 * */
	boolean isrunable = true;
	/**
	 * 当前播放进度：ms
	 * */
	int curms;

	private class MusicProgressThread extends Thread {

		@Override
		public void run() {
			while (isrunable) {
				if (MyApplication.mediaPlayer != null && MyApplication.mediaPlayer.isPlaying()) {
					curms = MyApplication.mediaPlayer.getCurrentPosition();
					musicHandler.sendEmptyMessage(1);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * musicPlayer handler 更新播放进度
	 * */
	Handler musicHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				int musicProgress = curms * 100 / totalms;
				MusicprogressBar.setProgress(musicProgress);
				musicTime.setText(StrTime.gettim(curms) + "/" + StrTime.gettim(totalms));
				break;

			default:
				break;
			}
		}

	};
	
	/**
	 * 播放intent
	 */
	Intent playIntent;
   
	@Override
	public void onClick(View v) {
		playIntent = new Intent();
		switch (v.getId()) {
		case R.id.list_show_album:
			startActivity(new Intent(context, MusicPlayActivity.class));
			overridePendingTransition(R.anim.act_in, R.anim.act_out);
			break;
		case R.id.playbtn:
			if(isPlaying){
				playIntent.setAction(Constant.ACTION_PAUSE);
				sendBroadcast(playIntent);
				isPlaying = false;
				playBtn.setImageResource(R.drawable.play_play_btn);
			}else{
				playBtn.setImageResource(R.drawable.desktop_pausebt);
				playIntent.setAction(Constant.ACTION_PLAY);
				sendBroadcast(playIntent);
				isPlaying = true;
			}
			break;
		case R.id.nextbtn:
			isPlaying = true;
			playIntent.setAction(Constant.ACTION_NEXT);
			sendBroadcast(playIntent);
			break;
		case R.id.prebtn:
			isPlaying = true;
			playIntent.setAction(Constant.ACTION_PREVIOUS);
			sendBroadcast(playIntent);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 初始化百度云推送服务
	 * */
	public void initBaiduPushService(){
		PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, Constant.BAIDU_APP_KEY);
	}

}
