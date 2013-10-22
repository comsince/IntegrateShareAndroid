package com.comsince.phonebook.activity.message;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.comsince.phonebook.R;
import com.comsince.phonebook.view.extendlistview.MsgListView;

public abstract class BaseMessageActivity extends Activity {
    /**基本组件**/
	protected MsgListView mMsgListView;
	protected TextView chatFriendName;
	protected Button backBtn;
	protected Button sendBtn;
	protected ImageButton faceBtn;
	protected EditText msgEt;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		initViews();
		initEvents();
		initData();
	}
	
	/**
	 * 初始化视图 
	 */
	protected abstract void initViews();

	/** 
	 * 初始化事件 
	 */
	protected abstract void initEvents();
	
	/**
	 * 初始化数据
	 * **/
	protected abstract void initData();
}
