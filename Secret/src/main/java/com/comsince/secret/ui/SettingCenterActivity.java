package com.comsince.secret.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.comsince.secret.R;
import com.comsince.secret.bean.User;
import com.comsince.secret.service.HandlerThreads;
import com.comsince.secret.util.BaseContorl;
import com.comsince.secret.util.SqlliteHander;


public class SettingCenterActivity extends Activity implements OnClickListener{
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_center_view);
		findViewById(R.id.modifAliasBut).setOnClickListener(this);
		//findViewById(R.id.setting_apprecomm).setOnClickListener(this);
		findViewById(R.id.setting_updata).setOnClickListener(this);
		findViewById(R.id.setting_feedback).setOnClickListener(this);
		findViewById(R.id.setting_about).setOnClickListener(this);
	//	findViewById(R.id.setting_share).setOnClickListener(this);
		findViewById(R.id.LEFT_BUTTON).setOnClickListener(this);
		findViewById(R.id.LEFT_BUTTON).setVisibility(View.VISIBLE);
		((TextView)this.findViewById(R.id.TITLE_TEXT)).setText("设置中心");
		User u = SqlliteHander.getTnstantiation(this).queryUser();
		((TextView)this.findViewById(R.id.alias)).setText(u.alias);
        findViewById(R.id.setting_wechat).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent ;
		 switch(v.getId())
		 {
		    case R.id.modifAliasBut:
		       
		    	final User  u = SqlliteHander.getTnstantiation(SettingCenterActivity.this).queryUser();
		    	String alias = ((TextView)SettingCenterActivity.this.findViewById(R.id.alias)).getText().toString();
 		        u.alias = alias;
		        new HandlerThreads.ModifyAliasThread(new Handler(){
		        	
		        	@Override
					public void handleMessage(Message msg) {
		        		if(msg.what==0)
		        		{
		        			 BaseContorl.doShowSToask(SettingCenterActivity.this, "修改成功");
		     		        SqlliteHander.getTnstantiation(SettingCenterActivity.this).saveUser(u);
		        		}
		        	}
		        }, u).start();
		    	break;
		     
		  /*  case R.id.setting_apprecomm:
		    	break;*/
		    case R.id.setting_feedback:
		        intent = new Intent(this,FadebackActivity.class);
				startActivity(intent);
		    	break;
		    case R.id.setting_updata:
		    	
		        BaseContorl.doShowToask(this, R.string.check_update_tips);
		    	new HandlerThreads.CheckUpdateThread(this).start();
		    	break;
		    case R.id.setting_about:
		    	intent = new Intent(this,AboutAcityvity.class);
				startActivity(intent);
		    	break;
             case R.id.setting_wechat:
                 new HandlerThreads.WeChatRequestThread().start();
                 break;
		    case R.id.LEFT_BUTTON:
		    	this.finish();
		    	break;
		 }
		
	}
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event) {
		  if(keyCode ==KeyEvent.KEYCODE_BACK )
		  {
			//  ((RadioButton) this.getParent().findViewById(R.id.radio_home)).setChecked(true);
			  this.finish();
		  }
		  return false;
	}
 
}
