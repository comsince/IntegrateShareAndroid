package com.comsince.knowledge.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.sharesdk.BaiduShareException;
import com.baidu.sharesdk.ShareContent;
import com.baidu.sharesdk.ShareListener;
import com.baidu.sharesdk.Utility;
import com.comsince.knowledge.MyApplication;
import com.comsince.knowledge.R;
import com.comsince.knowledge.adapter.MyPagerAdapter;
import com.comsince.knowledge.constant.Constant;
import com.comsince.knowledge.entity.Music;
import com.comsince.knowledge.layout.MusicPlayerLocalLayout;
import com.comsince.knowledge.preferences.MusicPreference;
import com.comsince.knowledge.uikit.MMAlert;
import com.comsince.knowledge.utils.StrTime;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;


public class MusicPlayActivity extends Activity implements OnClickListener {

	/**
	 * 基本的控件
	 * */
	private ViewPager viewPager;
	private TextView musicTitle, musicName, musicArtist;
	private ImageButton musicMode;
	private SeekBar musicSeekBar;
	private TextView musicTimePlayed, musicTimeTotal;
	private ImageButton musicPre, musicPlay, musicNext;
	private ImageButton shareMusic;
	private Button shareBtn, backToMainBtn;
	/**
	 * 记录歌曲播放状态的preferece
	 * */
	public static MusicPreference musicPreference;
	/**
	 * 当前的播放模式
	 * */
	public int nowPlayMode;
	/**
	 * 装载滑动页面
	 * */
	private List<View> pageViews;
	/**
	 * 滑动装载歌曲布局
	 * */
	LinearLayout musicLocalLayout;
	/**
	 * 装在滑动页面的适配器
	 * */
	MyPagerAdapter pagerAdapter;
	LayoutInflater inflater;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		inflater = LayoutInflater.from(context);
		setContentView(R.layout.activity_music_player);
		// 启动service
		// startService(new Intent(this, MusicPlayerService.class));
		// 实例化音乐信息广播
		musicInfoReceiver = new MusicInfoReceiver();
		// 获取musicPreferece
		musicPreference = MyApplication.musicPreference;
		initView();
		setupListener();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 初始化播放进度条
		initPlayProgress();
		// 该activity启动后，向service发送请求更新歌曲的播放信息
		sendBroadcast(new Intent(Constant.ACTION_UPDATE_ALL));
		nowPlayMode = musicPreference.getPlayMode(context);
		initPlayMode();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initpageData();
		registerMusicInfoReceiver();
		executeThread();
	}
	
	

	@Override
	protected void onStop() {
		super.onStop();
		isProgressThreadRunable = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(musicInfoReceiver);
	}
	

	/**
	 * musicPlayIntent 发送广播给musicservice更新歌曲播放状态
	 * */
	Intent musicPlayIntent;

	@Override
	public void onClick(View v) {
		// 务必实例化intent
		musicPlayIntent = new Intent();
		switch (v.getId()) {
		case R.id.music_button_play:
			if (isPlaying) {
				// 如果要暂停播放记录当前播放歌曲的进度
				musicPreference.saveMusicCurrentMs(context, curMs);
				musicPlayIntent.setAction(Constant.ACTION_PAUSE);
				sendBroadcast(musicPlayIntent);
				isPlaying = false;
				musicPlay.setImageResource(R.drawable.btn_music_play);
			} else {
				musicPlay.setImageResource(R.drawable.btn_music_pause);
				musicPlayIntent.setAction(Constant.ACTION_PLAY);
				sendBroadcast(musicPlayIntent);
				isPlaying = true;
			}
			break;
		case R.id.music_button_next:
			isPlaying = true;
			musicPlayIntent.setAction(Constant.ACTION_NEXT);
			sendBroadcast(musicPlayIntent);
			break;
		case R.id.music_button_prev:
			isPlaying = true;
			musicPlayIntent.setAction(Constant.ACTION_PREVIOUS);
			sendBroadcast(musicPlayIntent);
			break;
		case R.id.music_mode:
			Log.d("MusicPlayActivity", "当前模式：" + nowPlayMode);
			nowPlayMode++;
			if (nowPlayMode == Constant.PLAY_MODE_BY_SINGLE) {
				musicMode.setImageResource(R.drawable.btn_music_single_loop);
			} else if (nowPlayMode == Constant.PLAY_MODE_BY_RANDOM) {
				musicMode.setImageResource(R.drawable.btn_music_shuffle);
			} else {
				musicMode.setImageResource(R.drawable.btn_music_order_loop);
				nowPlayMode = 0;
			}
			musicPreference.savaPlayMode(context, nowPlayMode);
			musicPlayIntent.setAction(Constant.ACTION_SET_PLAYMODE);
			musicPlayIntent.putExtra("play_mode", nowPlayMode);
			sendBroadcast(musicPlayIntent);
			Log.d("MusicPlayActivity", "musicPreference mode :" + musicPreference.getPlayMode(context));
			break;
		case R.id.imgbt_share_music:
			sendWinInfoDiaLog();
			break;
		case R.id.share_button:
			shareBaiduSocial();
			break;
		case R.id.backmain_btn:
			startActivity(new Intent(context, MainActivity.class));
			overridePendingTransition(R.anim.actcome,R.anim.actout);
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化控件
	 * */

	public void initView() {
		viewPager = (ViewPager) findViewById(R.id.center_body_view_pager);
		musicTitle = (TextView) findViewById(R.id.music_txt_title);
		musicName = (TextView) findViewById(R.id.music_txt_album);
		musicArtist = (TextView) findViewById(R.id.music_txt_artist);
		musicMode = (ImageButton) findViewById(R.id.music_mode);
		musicSeekBar = (SeekBar) findViewById(R.id.music_seek);
		musicTimePlayed = (TextView) findViewById(R.id.music_time_played);
		musicTimeTotal = (TextView) findViewById(R.id.music_time_total);
		musicPre = (ImageButton) findViewById(R.id.music_button_prev);
		musicPlay = (ImageButton) findViewById(R.id.music_button_play);
		musicNext = (ImageButton) findViewById(R.id.music_button_next);
		shareMusic = (ImageButton) findViewById(R.id.imgbt_share_music);
		shareBtn = (Button) findViewById(R.id.share_button);
		backToMainBtn = (Button) findViewById(R.id.backmain_btn);
		pageViews = new ArrayList<View>();
		musicLocalLayout = new MusicPlayerLocalLayout(context);
		pageViews.add(inflater.inflate(R.layout.mp_album, null));
		pageViews.add(inflater.inflate(R.layout.mp_lrc, null));
		pageViews.add(musicLocalLayout);
	}

	/**
	 * 设置监听器
	 * */
	public void setupListener() {
		musicPre.setOnClickListener(this);
		musicPlay.setOnClickListener(this);
		musicNext.setOnClickListener(this);
		musicMode.setOnClickListener(this);
		shareMusic.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
		backToMainBtn.setOnClickListener(this);
	}

	/**
	 * 初始化pageData
	 * */
	public void initpageData() {
		if (viewPager.getChildCount() <= 0) {
			pagerAdapter = new MyPagerAdapter(pageViews);
		}
		viewPager.setAdapter(pagerAdapter);
	}

	/**
	 * 初始化播放模式
	 * */
	public void initPlayMode() {
		Log.v("MusicPlayActivity initPlayMode", "当前模式：" + nowPlayMode);
		if (nowPlayMode == Constant.PLAY_MODE_BY_ORDER) {
			musicMode.setImageResource(R.drawable.btn_music_order_loop);
		} else if (nowPlayMode == Constant.PLAY_MODE_BY_RANDOM) {
			musicMode.setImageResource(R.drawable.btn_music_shuffle);
		} else if (nowPlayMode == Constant.PLAY_MODE_BY_SINGLE) {
			musicMode.setImageResource(R.drawable.btn_music_single_loop);
		}
	}

	/**
	 * 初始化播放进度条
	 * */
	public void initPlayProgress() {
		// curMs = musicPreference.getMusicCurrentMs(context);
		curMs = MyApplication.mediaPlayer.getCurrentPosition();
		int seekBarProgress = curMs * 100 / totalMs;
		musicSeekBar.setProgress(seekBarProgress);
		musicTimePlayed.setText(StrTime.gettim(curMs));
	}

	/**
	 * 注册更新音乐转台的BroadCastReceiver
	 * */
	public void registerMusicInfoReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.ACTION_UPDATE);
		registerReceiver(musicInfoReceiver, intentFilter);
	}

	/**
	 * 启动音乐相关线程
	 * */
	public void executeThread() {
		isProgressThreadRunable = true;
		// 更新音乐进度线程
		musicInfoProgressThread = new ProgressThread();
		musicInfoProgressThread.start();
	}

	/**
	 * 更新进度条的ProgressThread
	 * */
	Thread musicInfoProgressThread;
	/**
	 * 是否循环执行此线程
	 * */
	boolean isProgressThreadRunable = true;
	/**
	 * 播放器当前播放位置
	 * */
	int curMs = 0;

	class ProgressThread extends Thread {

		@Override
		public void run() {
			while (isProgressThreadRunable) {
				if (MyApplication.mediaPlayer != null && MyApplication.mediaPlayer.isPlaying()) {
					curMs = MyApplication.mediaPlayer.getCurrentPosition();
					musicInfoHandler.sendEmptyMessage(Constant.UPDATE_SEEK_BAR);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			super.run();
		}

	}

	/**
	 * 处理音乐信息的总Handler
	 * */
	Handler musicInfoHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.UPDATE_SEEK_BAR:
				try {
					int seekBarProgress = curMs * 100 / totalMs;
					musicSeekBar.setProgress(seekBarProgress);
					musicTimePlayed.setText(StrTime.gettim(curMs));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 接收service传来的音乐播放信息的BoardCastReceiver
	 * */
	private MusicInfoReceiver musicInfoReceiver;
	int position;
	Music curMusic;
	int totalMs = 1;
	/**
	 * 记录当前播放状态
	 * */
	public boolean isPlaying = false;

	class MusicInfoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constant.ACTION_UPDATE)) {
				position = intent.getIntExtra("position", 0);
				curMusic = (Music) intent.getSerializableExtra("music");
				totalMs = intent.getIntExtra("totalms", 288888);
				// 设置播放音乐的信息
				musicTitle.setText(curMusic.getSinger() + "-" + curMusic.getAlbumName() + "-" + curMusic.getMusicName());
				musicName.setText(curMusic.getMusicName());
				musicArtist.setText(curMusic.getSinger());
				musicTimeTotal.setText(StrTime.getTime(String.valueOf(totalMs)));
				int seekBarProgress = curMs * 100 / totalMs;
				musicSeekBar.setProgress(seekBarProgress);
				// 设置当前播放状态
				if (MyApplication.mediaPlayer.isPlaying()) {
					musicPlay.setImageResource(R.drawable.btn_music_pause);
					isPlaying = true;
				} else {
					musicPlay.setImageResource(R.drawable.btn_music_play);
					isPlaying = false;
				}
			}
		}

	}

	/**
	 * 微信消息选择框
	 * */
	private static final int MMAlertSelect1 = 0;
	private static final int MMAlertSelect2 = 1;

	public void sendWinInfoDiaLog() {
		MMAlert.showAlert(this, getString(R.string.send_music), this.getResources().getStringArray(R.array.send_music_item), null, new MMAlert.OnAlertSelectId() {

			@Override
			public void onClick(int whichButton) {
				switch (whichButton) {
				case MMAlertSelect1:
					SendInfoWeiXin(false);
					break;
				case MMAlertSelect2:
					SendInfoWeiXin(true);
					break;
				default:
					break;
				}
			}

		});
	}

	/**
	 * 向微信发送分享
	 * */
	public void SendInfoWeiXin(boolean shareDirect) {
		String text = curMusic.getSinger() + "-" + curMusic.getAlbumName() + "-" + curMusic.getMusicName();
		// 初始化一个WXTextObject对象
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		// msg.title = "Will be ignored";
		msg.description = text;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
		req.message = msg;
		// req.scene = SendMessageToWX.Req.WXSceneSession;
		// req.scene = SendMessageToWX.Req.WXSceneTimeline;
		req.scene = shareDirect ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		Log.d("music", text);
		// 调用api接口发送数据到微信
		MyApplication.api.sendReq(req);
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	/**
	 * 百度社会化分享
	 * */
	final Handler handler = new Handler(Looper.getMainLooper());

	private void shareBaiduSocial() {
		ShareContent pageContent;
		pageContent = new ShareContent();
		String text = curMusic.getSinger() + "-" + curMusic.getAlbumName() + "-" + curMusic.getMusicName();
		pageContent.setContent("欢使用互动音乐播放器，我正在使用它分享：" + text);
		pageContent.setTitle("互动音乐播放器");
		pageContent.setUrl("http://hi.baidu.com/comsince");
		pageContent.setImageUrl("http://apps.bdimg.com/developer/static/04171450/developer/images/icon/terminal_adapter.png");
		// 必须加content否则无法弹出分享信息框
		MyApplication.socialShareUi.showShareMenu(this, pageContent, Utility.SHARE_BOX_STYLE, new ShareListener() {

			@Override
			public void onApiComplete(String responses) {
				final String msg = responses;
				handler.post(new Runnable() {
					@Override
					public void run() {
						Utility.showAlert(MusicPlayActivity.this, msg);
					}
				});
			}

			@Override
			public void onAuthComplete(Bundle values) {

			}

			@Override
			public void onError(BaiduShareException arg0) {
			}
		});
	}

}
