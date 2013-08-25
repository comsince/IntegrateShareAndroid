package com.comsince.phonebook.activity;

import com.comsince.phonebook.R;
import com.comsince.phonebook.constant.Constant;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class GeneralLoadingActivity extends Activity {
	private TextView loadingText;
	private ImageView loadingView;
	private String showText;
	//Animation-list实现逐帧动画
    private AnimationDrawable animationDrawable;
    private Context mContext;
    private LoadingReceiver loadingReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadingReceiver = new LoadingReceiver();
		IntentFilter loadingFilter = new IntentFilter();
		loadingFilter.addAction(Constant.ACTION_FINISH);
		//注册广播接收器
		registerReceiver(loadingReceiver, loadingFilter);
		mContext = this;
		showText = this.getIntent().getStringExtra("showText");
		setContentView(R.layout.loading_dialog_layout);
		initView();
		initAnimation();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return false;
		}else{
			return super.onKeyDown(keyCode, event);
		}
		
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(loadingReceiver);
		super.onDestroy();
	}

	public void initView(){
		loadingText = (TextView) findViewById(R.id.loading_dialog_text);
		loadingText.setText(showText);
		loadingView = (ImageView) findViewById(R.id.loading_flower);
	}
	public void initAnimation(){
		animationDrawable = (AnimationDrawable) loadingView.getBackground();
		animationDrawable.start();
	}
	
	private class LoadingReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Constant.ACTION_FINISH)){
				((Activity) mContext).finish();
			}
		}
		
	}
	
}
