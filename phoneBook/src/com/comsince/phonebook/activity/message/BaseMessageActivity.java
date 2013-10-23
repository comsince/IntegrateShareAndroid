package com.comsince.phonebook.activity.message;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.comsince.phonebook.receiver.PushMessageReceiver.EventHandler;
import com.comsince.phonebook.view.extendlistview.MsgListView;
import com.google.gson.Gson;

public abstract class BaseMessageActivity extends Activity 
       implements TextWatcher,OnClickListener,EventHandler{
    /**基本组件**/
	protected MsgListView mMsgListView;
	protected TextView chatFriendName;
	protected Button backBtn;
	protected Button sendBtn;
	protected ImageButton faceBtn;
	protected EditText msgEt;
	protected Button aboutFriend;
	
	protected Context context;
	protected List<MessageItem>	msgList;
	protected ChatAdapter msgAdapter;
	
	protected PhoneBookPreference phoneBookPre;
	protected PhoneBookApplication phoneBookApplication;
	protected Gson mGson;
	
	//消息传递对象，即是发给谁
	protected User mFromUser;
	
	public static final int NEW_MESSAGE = 0x001;// 收到消息
	
	
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
	
	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == NEW_MESSAGE) {
				com.comsince.phonebook.entity.Message msgItem = (com.comsince.phonebook.entity.Message) msg.obj;
				String userId = msgItem.getUser_id();
				if (!userId.equals(mFromUser.getUserId()))// 如果不是当前正在聊天对象的消息，不处理
					return;
				//int headId = msgItem.getHead_id();
				int headId = 0;
				MessageItem item = new MessageItem(MessageItem.MESSAGE_TYPE_TEXT, msgItem.getNick(), System.currentTimeMillis(), msgItem.getMessage(), headId, true, 0);
				msgAdapter.upDateMsg(item);
				// mMsgDB.saveMsg(msgItem.getUser_id(), item);
				//RecentItem recentItem = new RecentItem(userId, headId, msgItem.getNick(), msgItem.getMessage(), 0, System.currentTimeMillis());
				//mRecentDB.saveRecent(recentItem);
			}
		}
	};
}
