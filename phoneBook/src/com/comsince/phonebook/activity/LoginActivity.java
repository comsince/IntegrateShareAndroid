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
import com.comsince.phonebook.R;

public class LoginActivity extends Activity implements OnClickListener{
    private EditText userName , userPassWord;
    private Button loginBtn;
    private TextView registration;
    
    public static final int LOGIN_REQUEST = 100001;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		findViewById();
		setupListener();
	}
	public void findViewById(){
		userName = (EditText) findViewById(R.id.username);
		userPassWord = (EditText) findViewById(R.id.userpassword);
		loginBtn = (Button) findViewById(R.id.login_activity_login);
	}
	/**
	 * 设置按钮的侦听器
	 * */
	public void setupListener(){
		loginBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_activity_login:
			login();
			break;
		case R.id.registration:
			break;
		default:
			break;
		}
	}
	
	public void login(){
		String username = userName.getText().toString().trim();
		String password = userPassWord.getText().toString().trim();
		if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
			Toast.makeText(LoginActivity.this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
		}else{
			Intent intent  = new Intent();
			intent.putExtra("username", username);
			intent.putExtra("password", password);
			intent.setClass(this, LoadingActivity.class);
			startActivityForResult(intent, LOGIN_REQUEST);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == LOGIN_REQUEST){
			if(resultCode == LoadingActivity.LOGIN_SUCCESS){
				this.finish();
				Intent intent = new Intent();
				intent.setClass(this, MainActivity.class);
				startActivity(intent);
			}else if(resultCode == LoadingActivity.LOGIN_Fail){
				Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	 

}
