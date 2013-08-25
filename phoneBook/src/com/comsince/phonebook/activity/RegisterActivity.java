package com.comsince.phonebook.activity;

import java.io.File;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.Loginfo;
import com.comsince.phonebook.preference.PhoneBookPreference;
import com.comsince.phonebook.util.AndroidUtil;
import com.comsince.phonebook.util.BaiduCloudSaveUtil;
import com.comsince.phonebook.util.DataUtil;
import com.comsince.phonebook.util.SimpleXmlReaderUtil;

public class RegisterActivity extends Activity implements OnClickListener {
	public static final int REGISTER_SUCCESS = 100;
	public static final int REGISTER_FAIL = 101;
    private RelativeLayout register;
    private EditText userName,passWord,phone;
    private TextView title;
    private Button leftBtn,rightBtn;
    private TextView registerText;
    private ImageView loading;
    private Loginfo loginfo;
    private String username,password,phonenumber;
    private SimpleXmlReaderUtil simpleXmlReaderUtil;
    //Animation-list实现逐帧动画
    private AnimationDrawable animationDrawable;
    private static PhoneBookPreference phoneBookPreference;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		phoneBookPreference = PhoneBookApplication.phoneBookPreference;
		setContentView(R.layout.regist);
		findViewById();
		setupListener();
		initData();
	}
	
	public void findViewById(){
		userName = (EditText) findViewById(R.id.username);
		passWord = (EditText) findViewById(R.id.password);
		phone = (EditText) findViewById(R.id.phone);
		title = (TextView) findViewById(R.id.title);
		leftBtn = (Button) findViewById(R.id.title_btn_left);
		rightBtn = (Button) findViewById(R.id.title_btn_right);
		register = (RelativeLayout) findViewById(R.id.regist);
		registerText = (TextView) findViewById(R.id.regist_text);
		loading = (ImageView) findViewById(R.id.loading);
	}
	
	public void setupListener(){
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		register.setOnClickListener(this);
	}
	
	public void initData(){
		leftBtn.setVisibility(View.VISIBLE);
		loading.setVisibility(View.GONE);
		leftBtn.setText(R.string.login);
		rightBtn.setText(R.string.regist);
		title.setText(R.string.register_phonebook);
		loginfo = new Loginfo();
		simpleXmlReaderUtil = new SimpleXmlReaderUtil();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_btn_left:
			finish();
			break;
		case R.id.title_btn_right:
			finish();
			break;
		case R.id.regist:
			register();
			break;
		default:
			break;
		}
	}
	
	public void register(){
		username = userName.getText().toString().trim();
		password = passWord.getText().toString().trim();
		phonenumber = phone.getText().toString().trim();
		if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)||TextUtils.isEmpty(phonenumber)){
			Toast.makeText(this, "用户名或密码,手机号不能为空！", Toast.LENGTH_SHORT).show();
		}else{
			//保存數據到共享數據元
			phoneBookPreference.saveUserName(this, username);
			phoneBookPreference.savePassWord(this, DataUtil.md5(password));
			loading.setVisibility(View.VISIBLE);
			register.setClickable(false);
			//实现逐帧动画
			animationDrawable = (AnimationDrawable) loading.getBackground();
			animationDrawable.start();
			loginfo = new Loginfo();
			loginfo.setUsername(username);
			loginfo.setPassword(password);
			loginfo.setPhonenumber(phonenumber);
			new registerThread().start();
		}
	}
	
	class registerThread extends Thread{

		@Override
		public void run() {
			//将注册信息写入到本地
			simpleXmlReaderUtil.writeXml(loginfo, Constant.PHONE_BOOK_PATH+"/"+Constant.DIR_LOGIN_INFO,username);
			//将该注册用户信息上传到服务器
			String uploadURL = BaiduCloudSaveUtil.generateUrl(Constant.PHONE_BOOK_PATH, "/"+Constant.DIR_LOGIN_INFO+"/"+username+".xml");
			String uploadXmlPath = AndroidUtil.getSDCardRoot()+Constant.PHONE_BOOK_PATH+File.separator+Constant.DIR_LOGIN_INFO+File.separator+username+".xml";
			String responeMsg = BaiduCloudSaveUtil.putObject(uploadURL, uploadXmlPath);
			if(!TextUtils.isEmpty(responeMsg)&&responeMsg.equals("OK")){
				registerHandler.sendEmptyMessage(REGISTER_SUCCESS);
			}else{
				registerHandler.sendEmptyMessage(REGISTER_FAIL);
			}
			super.run();
		}
		
	}
	
	Handler registerHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REGISTER_SUCCESS:
				setResult(REGISTER_SUCCESS);
				finish();
				break;
			case REGISTER_FAIL:
				registerText.setVisibility(View.VISIBLE);
				loading.setVisibility(View.GONE);
				register.setClickable(true);
				Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
		
	};

}
