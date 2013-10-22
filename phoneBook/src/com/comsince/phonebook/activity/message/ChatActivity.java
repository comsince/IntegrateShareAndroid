package com.comsince.phonebook.activity.message;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.comsince.phonebook.R;
import com.comsince.phonebook.adapter.ChatAdapter;
import com.comsince.phonebook.asynctask.SendMsgAsyncTask;
import com.comsince.phonebook.entity.Message;
import com.comsince.phonebook.entity.MessageItem;
import com.comsince.phonebook.entity.User;
import com.comsince.phonebook.util.L;
import com.comsince.phonebook.view.extendlistview.MsgListView;

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
		sendBtn = (Button) findViewById(R.id.send_btn);
	}

	@Override
	protected void initEvents() {
		backBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		msgEt.addTextChangedListener(this);
		faceBtn.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		mFromUser = (User) getIntent().getSerializableExtra("user");
		msgList = new ArrayList<MessageItem>();
		msgAdapter = new ChatAdapter(context, msgList);
		mMsgListView.setAdapter(msgAdapter);
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() > 0) {
			sendBtn.setEnabled(true);
		} else {
			sendBtn.setEnabled(false);
		}		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_back:
			finish();
			break;
			
		case R.id.face_btn:
			
			break;
			
		case R.id.send_btn:
			String msg = msgEt.getText().toString();
			MessageItem item = new MessageItem(MessageItem.MESSAGE_TYPE_TEXT,
					phoneBookPre.getUserName(), System.currentTimeMillis(), msg,
					0, false, 0);
			L.i("发送消息： "+item.getName());
			msgAdapter.upDateMsg(item);
			mMsgListView.setSelection(msgAdapter.getCount() - 1);
			//mMsgDB.saveMsg(mFromUser.getUserId(), item);
			msgEt.setText("");
			//发送消息
			Message msgItem = new Message(System.currentTimeMillis(), msg, "");
			new SendMsgAsyncTask(mGson.toJson(msgItem), mFromUser.getUserId()).send();
			break;
	  }
	}

}
