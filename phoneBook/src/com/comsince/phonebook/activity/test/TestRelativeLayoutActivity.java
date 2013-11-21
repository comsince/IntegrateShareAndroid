package com.comsince.phonebook.activity.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.comsince.phonebook.R;

public class TestRelativeLayoutActivity extends Activity implements OnClickListener{
	private LinearLayout conversation_multichat_layout;
	private LinearLayout conversation_secretfile_layout;
	private LinearLayout conversation_watermark_layout;
	private LinearLayout conversation_rich_status_layout;
	private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.conversation_options_bar);
    	mContext = this;
    	initView();
    	setUpListener();
    }
    
    private void initView(){
    	conversation_multichat_layout = (LinearLayout) findViewById(R.id.conversation_multichat_layout);
    	conversation_secretfile_layout = (LinearLayout) findViewById(R.id.conversation_secretfile_layout);
    	conversation_watermark_layout = (LinearLayout) findViewById(R.id.conversation_watermark_layout);
    	conversation_rich_status_layout = (LinearLayout) findViewById(R.id.conversation_rich_status_layout);
    }
    
    private void setUpListener(){
    	conversation_multichat_layout.setOnClickListener(this);
    	conversation_secretfile_layout.setOnClickListener(this);
    	conversation_watermark_layout.setOnClickListener(this);
    	conversation_rich_status_layout.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.conversation_multichat_layout:
            Toast.makeText(mContext, "click conversation_multichat_layout", Toast.LENGTH_LONG).show();
			break;
		case R.id.conversation_secretfile_layout:
			Toast.makeText(mContext, "click conversation_secretfile_layout", Toast.LENGTH_LONG).show();
			break;
		case R.id.conversation_watermark_layout:
			Toast.makeText(mContext, "click conversation_watermark_layout", Toast.LENGTH_LONG).show();
			break;
		case R.id.conversation_rich_status_layout:
			Toast.makeText(mContext, "click conversation_rich_status_layout", Toast.LENGTH_LONG).show();
			break;
		}

	}
}
