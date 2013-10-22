package com.comsince.phonebook.activity.message;

import com.comsince.phonebook.R;
import com.comsince.phonebook.view.extendlistview.MsgListView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChatActivity extends BaseMessageActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		chatFriendName = (TextView) findViewById(R.id.chat_friend_name);
		backBtn = (Button) findViewById(R.id.chat_back);
		mMsgListView = (MsgListView) findViewById(R.id.msg_listView);
		faceBtn = (ImageButton) findViewById(R.id.face_btn);
		msgEt = (EditText) findViewById(R.id.msg_et);
		sendBtn = (Button) findViewById(R.id.search_btn);
	}

	@Override
	protected void initEvents() {

	}

	@Override
	protected void initData() {
		
	}

}
