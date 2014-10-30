package com.comsince.secret.ui;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.comsince.secret.R;

public class AboutAcityvity extends Activity{
	 
	@Override
	public void onCreate(Bundle savedInstanceState)
	{  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		findViewById(R.id.LEFT_BUTTON).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.TITLE_TEXT)).setText(R.string.about_tips);
	   ((Button)findViewById(R.id.LEFT_BUTTON)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				 
				Intent intent = new Intent(AboutAcityvity.this,SettingCenterActivity.class);
				startActivity(intent);
				AboutAcityvity.this.finish();
			}});
	}
	
 
}
