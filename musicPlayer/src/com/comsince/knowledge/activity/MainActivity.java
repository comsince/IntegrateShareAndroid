package com.comsince.knowledge.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.comsince.knowledge.MyApplication;
import com.comsince.knowledge.R;
import com.comsince.knowledge.adapter.LocalMusicListAdapter;
import com.comsince.knowledge.adapter.MyPagerAdapter;
import com.comsince.knowledge.layout.FavoriteLayout;
import com.comsince.knowledge.layout.LocalLayout;
import com.comsince.knowledge.layout.NetLayout;
import com.comsince.knowledge.service.MusicPlayerService;

public class MainActivity extends Activity {
	// 基本控件
	ViewPager viewPager;
	ImageButton localmusic, favoritemusic, netmusic;
	ImageView listShowAlbum;
	TextView musicName, musicTime;
	ImageButton preBtn, playBtn, nextBtn;
	Context context;
	// 装载滑动页面
	List<View> pageViews;
	LayoutInflater inflater;
	// 滑动布局
	FavoriteLayout favorLayout;
	LocalLayout localLayout;
	NetLayout netLayout;
	MyPagerAdapter pagerAdapter;

	private static String TAG = "Aisa";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.playermain);
		context = this;
		inflater = LayoutInflater.from(this);
		initView();
		//启动service
		startService(new Intent(context,MusicPlayerService.class));
	}

	@Override
	protected void onResume() {
		super.onResume();
		initpageData();
	}

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
		localmusic = (ImageButton) findViewById(R.id.localmusic);
		favoritemusic = (ImageButton) findViewById(R.id.favoritemusic);
		netmusic = (ImageButton) findViewById(R.id.netmusic);
		// 底部播放按钮
		listShowAlbum = (ImageView) findViewById(R.id.list_show_album);
		musicName = (TextView) findViewById(R.id.musicName);
		musicTime = (TextView) findViewById(R.id.musicTime);
		preBtn = (ImageButton) findViewById(R.id.prebtn);
		nextBtn = (ImageButton) findViewById(R.id.nextbtn);
		playBtn = (ImageButton) findViewById(R.id.playbtn);
		//设置监听器
		setupListener();
	}
	/**
	 * 设置监听器
	 * */
	public void setupListener(){
		localmusic.setOnClickListener(new MyMusicListener());
		favoritemusic.setOnClickListener(new LoveMusicListener());
		netmusic.setOnClickListener(new NetMusicListener());
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

}
