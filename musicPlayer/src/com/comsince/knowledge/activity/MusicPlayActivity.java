package com.comsince.knowledge.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.comsince.knowledge.R;
import com.comsince.knowledge.adapter.MyPagerAdapter;

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

	
	/**
	 * 装载滑动页面
	 * */
	private List<View> pageViews;
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
		initView();
		setupListener();
	}
	
	

	@Override
	protected void onResume() {
		super.onResume();
		initpageData();
	}



	@Override
	public void onClick(View arg0) {

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
		musicPre = (ImageButton) findViewById(R.id.music_button_prev);
		musicPlay = (ImageButton) findViewById(R.id.music_button_play);
		musicNext = (ImageButton) findViewById(R.id.music_button_next);
		pageViews = new ArrayList<View>();
		pageViews.add(inflater.inflate(R.layout.mp_album, null));
		pageViews.add(inflater.inflate(R.layout.mp_lrc, null));
		//pageViews.add(inflater.inflate(R.layout.musiclist, null));
	}

	/**
	 * 设置监听器
	 * */
	public void setupListener() {

	}

	/**
	 * 初始化pageData
	 * */
	public void initpageData() {
       if(viewPager.getChildCount()<=0){
    	   pagerAdapter = new MyPagerAdapter(pageViews);
       }
       viewPager.setAdapter(pagerAdapter);
	}

}
