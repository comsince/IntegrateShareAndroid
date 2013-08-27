package com.comsince.phonebook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comsince.phonebook.MainActivity;
import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.preference.PhoneBookPreference;
import com.comsince.phonebook.util.DataUtil;

public class LoginActivity extends Activity implements OnClickListener{
    private EditText userName , userPassWord;
    private Button loginBtn;
    private TextView registration;
    private TextView title;
    
    private static PhoneBookPreference phoneBookPreference;
    
    public static final int LOGIN_REQUEST = 100001;
    public static final int REGISTER_REQUEST = 100002;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		phoneBookPreference = PhoneBookApplication.phoneBookPreference;
		setContentView(R.layout.login_activity);
		findViewById();
		setupListener();
	}
	public void findViewById(){
		userName = (EditText) findViewById(R.id.username);
		userPassWord = (EditText) findViewById(R.id.userpassword);
		loginBtn = (Button) findViewById(R.id.login_activity_login);
		registration = (TextView) findViewById(R.id.registration);
		title = (TextView) findViewById(R.id.title);
		title.setText(R.string.login);
	}
	/**
	 * 设置按钮的侦听器
	 * */
	public void setupListener(){
		loginBtn.setOnClickListener(this);
		registration.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_activity_login:
			//login();
			//跳过登录
			Intent intent = new Intent();
			intent.setClass(this, MainActivity.class);
			startActivity(intent);
			break;
		case R.id.registration:
			register();
			break;
		default:
			break;
		}
	}
	/**
	 * 登录
	 * */
	public void login(){
		String username = userName.getText().toString().trim();
		String password = userPassWord.getText().toString().trim();
		if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
			Toast.makeText(LoginActivity.this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
		}else{
			//保存數據到共享數據元
			phoneBookPreference.saveUserName(this, username);
			phoneBookPreference.savePassWord(this, DataUtil.md5(password));
			Intent intent  = new Intent();
			intent.putExtra("username", username);
			intent.putExtra("password", password);
			intent.setClass(this, LoadingActivity.class);
			startActivityForResult(intent, LOGIN_REQUEST);
		}
	}
	/*
	 * 注册
	 * **/
	public void register(){
		Intent intent = new Intent();
		intent.setClass(this, RegisterActivity.class);
		startActivityForResult(intent, REGISTER_REQUEST);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == LOGIN_REQUEST){
			if(resultCode == LoadingActivity.LOGIN_SUCCESS){
				Intent intent = new Intent();
				intent.setClass(this, MainActivity.class);
				startActivity(intent);
				this.finish();
			}else if(resultCode == LoadingActivity.LOGIN_Fail){
				Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
			}
		}else if(requestCode == REGISTER_REQUEST){
			if(resultCode == RegisterActivity.REGISTER_SUCCESS){
				Intent intent = new Intent();
				intent.setClass(this, MainActivity.class);
				startActivity(intent);
				this.finish();
			}else if(resultCode == RegisterActivity.REGISTER_FAIL){
				Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	 

}
