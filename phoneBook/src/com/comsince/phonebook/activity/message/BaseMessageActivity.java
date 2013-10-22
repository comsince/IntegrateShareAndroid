package com.comsince.phonebook.activity.message;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.adapter.ChatAdapter;
import com.comsince.phonebook.entity.MessageItem;
import com.comsince.phonebook.entity.User;
import com.comsince.phonebook.preference.PhoneBookPreference;
import com.comsince.phonebook.view.extendlistview.MsgListView;
import com.google.gson.Gson;

public abstract class BaseMessageActivity extends Activity 
       implements TextWatcher,OnClickListener{
    /**基本组件**/
	protected MsgListView mMsgListView;
	protected TextView chatFriendName;
	protected Button backBtn;
	protected Button sendBtn;
	protected ImageButton faceBtn;
	protected EditText msgEt;
	
	protected Context context;
	protected List<MessageItem>	msgList;
	protected ChatAdapter msgAdapter;
	
	protected PhoneBookPreference phoneBookPre;
	protected PhoneBookApplication phoneBookApplication;
	protected Gson mGson;
	
	//消息传递对象，即是发给谁
	protected User mFromUser;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		context = this;
		phoneBookApplication = (PhoneBookApplication) getApplication();
		phoneBookPre = PhoneBookApplication.phoneBookPreference;
		mGson = phoneBookApplication.getGson();
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
