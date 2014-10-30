package com.comsince.secret.ui;




import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;

import com.comsince.secret.R;
import com.comsince.secret.bean.User;
import com.comsince.secret.common.Constant;
import com.comsince.secret.service.API;
import com.comsince.secret.util.SqlliteHander;


public class SplanshActivity extends Activity{
	
	boolean initComplete = false;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init_view);
		new Thread(){
			@Override
			public void run()
			{
				SqlliteHander.getTnstantiation(SplanshActivity.this).createTable();
				checkUserInfo();
			}
		}.start();
		
		File dir = new File(Constant.IMAGE_DIR);
		if(!dir.exists())
		{
			dir.mkdir();
		}
	}
	
	private void checkUserInfo()
	{
		User info = SqlliteHander.getTnstantiation(this).queryUser();
		if(info == null)
		{
			new CheckUserThread().start();
			
		}else
		{
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent intent = new Intent(this,MMHomeActivity.class);  
			startActivity(intent);
			this.finish();
		}
 		
 	}
   Handler checkHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			 
			User u = (User) msg.getData().getSerializable("user");
			if(u==null)
			{
				Intent intent = new Intent(SplanshActivity.this,UserRegisterActivity.class);
				startActivity(intent);
				SplanshActivity.this.finish();
			}else
			{
				SqlliteHander.getTnstantiation(SplanshActivity.this).saveUser(u);
				Intent intent = new Intent(SplanshActivity.this,MMHomeActivity.class);  
			    startActivity(intent);
				SplanshActivity.this.finish();
				
			}
		}
	};
	public   class CheckUserThread extends Thread{
	 
		@Override
		public void run() {
			User u = null;
			Message message=new Message();
			String imei = ((TelephonyManager) SplanshActivity.this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
			try {
				u = API.getUserByIMEI(imei);
			} catch (Exception e) {
				e.printStackTrace();
				message.what = -1;
			}
			
			Bundle bundle=new Bundle();
			bundle.putSerializable("user", u);
			message.setData(bundle);
			checkHandler.sendMessage(message);
		}
	}
	
}
