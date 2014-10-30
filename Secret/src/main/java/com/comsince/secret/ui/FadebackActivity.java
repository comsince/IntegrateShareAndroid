package com.comsince.secret.ui;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.comsince.secret.R;
import com.comsince.secret.service.HandlerThreads;
import com.comsince.secret.util.BaseContorl;
import com.comsince.secret.util.SqlliteHander;


public class FadebackActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fadeback);
	   ((Button)findViewById(R.id.fadebackButton)).setOnClickListener(new OnClickListener(){
 
		@Override
		public void onClick(View arg0) {
			String msg = ((EditText)findViewById(R.id.fadeback_edit)).getText().toString().trim();
			if("".equals(msg))
			{
				BaseContorl.doShowHToask(FadebackActivity.this, R.string.fadeback_hint);
				return ;
			}
			msg = "#"+FadebackActivity.this.getString(R.string.app_name_version)+"#" +msg ;
			FadebackActivity.this.findViewById(R.id.PROGRESS_BAR).setVisibility(View.VISIBLE);
			((Button)findViewById(R.id.fadebackButton)).setEnabled(false);
			new HandlerThreads.SendFadeBackThread(handler,msg, SqlliteHander.getTnstantiation(FadebackActivity.this).queryUser().userId).start();
		}});
	   findViewById(R.id.LEFT_BUTTON).setVisibility(View.VISIBLE);
	  ((TextView)findViewById(R.id.TITLE_TEXT)).setText(R.string.title_fadeback);
	   ((Button)findViewById(R.id.LEFT_BUTTON)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				 
				FadebackActivity.this.finish();
			}});
	}
	
	Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			
			FadebackActivity.this.findViewById(R.id.PROGRESS_BAR).setVisibility(View.GONE);
			((Button)findViewById(R.id.fadebackButton)).setEnabled(true);
			if(msg.what == -1)
			{
				BaseContorl.doShowEToask(FadebackActivity.this, "反馈失败,请稍后再试吧^_^");
			}else
			{
				BaseContorl.doShowSToask(FadebackActivity.this, R.string.fadeBack_complte_tips);
				((EditText)findViewById(R.id.fadeback_edit)).setText("");
			}
			 
		}
};
}
