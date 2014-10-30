package com.comsince.secret.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.comsince.secret.R;
import com.comsince.secret.bean.User;
import com.comsince.secret.service.API;
import com.comsince.secret.util.BaseContorl;
import com.comsince.secret.util.SqlliteHander;

public class UserRegisterActivity extends Activity{
	
	EditText alias;
	Button registerButton;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		alias = (EditText) this.findViewById(R.id.alias);
		registerButton = (Button) this.findViewById(R.id.registerButton);
		registerButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				 String name = alias.getText().toString().trim();
				 if(!"".equals(name))
				 {
					 
					 BaseContorl.doShowHToask(UserRegisterActivity.this, "正在注册.......");
					 User user = new User();
					 user.alias = name ;
					 user.imei = ((TelephonyManager) UserRegisterActivity.this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
					 new UserLgonThread(user).start();
					 UserRegisterActivity.this.findViewById(R.id.PROGRESS_BAR).setVisibility(View.VISIBLE);
					 UserRegisterActivity.this.findViewById(R.id.registerButton).setEnabled(false);
				 }
			}});
	}
	
	 Handler registerHandler = new Handler() {
			
			@Override
			public void handleMessage(Message msg) {
				
				UserRegisterActivity.this.findViewById(R.id.PROGRESS_BAR).setVisibility(View.GONE);
				UserRegisterActivity.this.findViewById(R.id.registerButton).setEnabled(true);
				if(msg.what == -1)
				{
					BaseContorl.doShowEToask(UserRegisterActivity.this, R.string.tip_network_busy);
					return;
				}
				if(msg.what == 1)
				{
					BaseContorl.doShowEToask(UserRegisterActivity.this, "["+alias+"]已经存在啦，换一个名字吧");
					return;
				}
				User u = (User) msg.getData().getSerializable("user");
				Intent intent = new Intent(UserRegisterActivity.this,MMHomeActivity.class);  
		        startActivity(intent);
			    UserRegisterActivity.this.finish();
				SqlliteHander.getTnstantiation(UserRegisterActivity.this).saveUser(u);
				BaseContorl.doShowSToask(UserRegisterActivity.this, "恭喜你,注册成功!");
			}
		};
		public   class UserLgonThread extends Thread{
		 
			User user;
			UserLgonThread(User u){
				user= u;
			}
			@Override
			public void run() {
				Message message=new Message();
				try {
					String code = API.userRegister(user);
					if( code.equals("1"))
					{
						message.what = 1;
					}else
					{
						user.userId = code;
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					message.what = -1;
				}
				Bundle bundle=new Bundle();
				bundle.putSerializable("user", user);
				message.setData(bundle);
				registerHandler.sendMessage(message);
			}
		} 
}
