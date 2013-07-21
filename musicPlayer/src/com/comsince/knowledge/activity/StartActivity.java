package com.comsince.knowledge.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.comsince.knowledge.R;

/**
 * 启动页面
 * 
 * @author comsince
 * 
 * */
public class StartActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置全屏显示,布局文件中已经设置了全屏显示
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.start);
		ImageView startImg = (ImageView) findViewById(R.id.startpage);
		// 设置启动界面动画
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo);
		// 开始imageview设置动画效果
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				new Handler().postDelayed(new Runnable(){
					@Override
					public void run(){
						//Intent intent = new Intent (StartActivity.this,LoginActivity.class);
						//直接跳到主界面
						Intent intent = new Intent (StartActivity.this,MainActivity.class);
						startActivity(intent);			
						StartActivity.this.finish();
					}
				}, 1000);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {

			}

		});
		startImg.startAnimation(animation);

	}
   
	
}
