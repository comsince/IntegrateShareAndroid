package com.comsince.knowledge.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.comsince.knowledge.R;

public class LoginActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	}
	/**
	 * 登录返回
	 * */
	public void loginback(View v){
		this.finish();
	}
	/**
	 * 登录
	 * */
	public void login(View v){
		Intent intent  = new Intent();
		intent.setClass(this, LoadingActivity.class);
		startActivity(intent);
		//LoginActivity.this.finish();
	}
	

}
