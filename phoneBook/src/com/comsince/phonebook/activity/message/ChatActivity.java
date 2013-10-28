package com.comsince.phonebook.activity.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.adapter.ChatAdapter;
import com.comsince.phonebook.adapter.FacePageAdeapter;
import com.comsince.phonebook.asynctask.SendMsgAsyncTask;
import com.comsince.phonebook.entity.MessageItem;
import com.comsince.phonebook.entity.User;
import com.comsince.phonebook.receiver.PushMessageReceiver;
import com.comsince.phonebook.util.L;
import com.comsince.phonebook.util.PhoneBookUtil;
import com.comsince.phonebook.view.extendlistview.MsgListView;
import com.comsince.phonebook.view.viewpager.CirclePageIndicator;
import com.comsince.phonebook.view.viewpager.JazzyViewPager;
import com.comsince.phonebook.view.viewpager.JazzyViewPager.TransitionEffect;

public class ChatActivity extends BaseMessageActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		PushMessageReceiver.ehList.add(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		PushMessageReceiver.ehList.remove(this);// 移除监听
	}
	
	@Override
	public void onBackPressed() {
		if (params.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE || isFaceShow) {
			faceLinearLayout.setVisibility(View.GONE);
			isFaceShow = false;
		}else{
			finish();
		}
	}
	
	@Override
	protected void initViews() {
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		params = getWindow().getAttributes();
		chatFriendName = (TextView) findViewById(R.id.chat_friend_name);
		backBtn = (Button) findViewById(R.id.chat_back);
		mMsgListView = (MsgListView) findViewById(R.id.msg_listView);
		faceBtn = (ImageButton) findViewById(R.id.face_btn);
		msgEt = (EditText) findViewById(R.id.msg_et);
		sendBtn = (Button) findViewById(R.id.send_btn);
		aboutFriend = (Button) findViewById(R.id.about_friend);
		
		//底部表情输入
		faceLinearLayout = (LinearLayout) findViewById(R.id.face_ll);
		faceViewPager = (JazzyViewPager) findViewById(R.id.face_pager);
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
	}

	@Override
	protected void initEvents() {
		backBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		msgEt.addTextChangedListener(this);
		faceBtn.setOnClickListener(this);
		mMsgListView.setXListViewListener(this);
		msgEt.setOnTouchListener(this);
		mMsgListView.setOnTouchListener(this);
	}

	@Override
	protected void initData() {
		mFromUser = (User) getIntent().getSerializableExtra("user");
		chatFriendName.setText(mFromUser.getNick());
		//初始化消息加载页数
		MsgPagerNum = 0;
		mMsgDB = phoneBookApplication.getMessageDB();
		msgList = initMsgData();
		msgAdapter = new ChatAdapter(context, msgList);
		mMsgListView.setAdapter(msgAdapter);
		mMsgListView.setPullLoadEnable(false);
		//获取表情对应关系
		Set<String> keySet = PhoneBookApplication.getInstance().getFaceMap().keySet();
		keys = new ArrayList<String>();
		keys.addAll(keySet);
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
			if (!isFaceShow) {
				imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
				try {
					Thread.sleep(80);// 解决此时会黑一下屏幕的问题
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				faceLinearLayout.setVisibility(View.VISIBLE);
				isFaceShow = true;
			} else {
				faceLinearLayout.setVisibility(View.GONE);
				isFaceShow = false;
			}
			break;
			
		case R.id.send_btn:
			String msg = msgEt.getText().toString();
			MessageItem item = new MessageItem(MessageItem.MESSAGE_TYPE_TEXT,
					phoneBookPre.getUserName(), System.currentTimeMillis(), msg,
					0, false, 0,PhoneBookUtil.getCurrentUserAvatarName(context));
			L.i("发送消息： "+item.getName());
			msgAdapter.upDateMsg(item);
			mMsgListView.setSelection(msgAdapter.getCount() - 1);
			mMsgDB.saveMsg(mFromUser.getUserId(), item);
			msgEt.setText("");
			//发送消息
			com.comsince.phonebook.entity.Message msgItem = new com.comsince.phonebook.entity.Message(System.currentTimeMillis(), msg, "",PhoneBookUtil.getCurrentUserAvatarName(context));
			new SendMsgAsyncTask(mGson.toJson(msgItem), mFromUser.getUserId()).send();
			break;
	  }
	}

	@Override
	public void onMessage(com.comsince.phonebook.entity.Message message) {
		Message handlerMsg = handler.obtainMessage(NEW_MESSAGE);
		handlerMsg.obj = message;
		handler.sendMessage(handlerMsg);
	}

	@Override
	public void onBind(String method, int errorCode, String content) {
		
	}

	@Override
	public void onNotify(String title, String content) {
		
	}

	@Override
	public void onNetChange(boolean isNetConnected) {
		
	}

	@Override
	public void onNewFriend(User u) {
		
	}

	@Override
	public void onRefresh() {
		MsgPagerNum++;
		List<MessageItem> msgList = initMsgData();
		int position = msgAdapter.getCount();
		msgAdapter.refreshMsg(msgList);
		mMsgListView.stopRefresh();
		mMsgListView.setSelection(msgAdapter.getCount() - position - 1);
	}

	@Override
	public void onLoadMore() {
		
	}

	@Override
	protected void initFacePage() {
		List<View> lv = new ArrayList<View>();
		for (int i = 0; i < PhoneBookApplication.NUM_PAGE; ++i)
			lv.add(getGridView(i));
		FacePageAdeapter adapter = new FacePageAdeapter(lv, faceViewPager);
		faceViewPager.setAdapter(adapter);
		faceViewPager.setCurrentItem(currentPage);
		faceViewPager.setTransitionEffect(TransitionEffect.Standard);
		indicator.setViewPager(faceViewPager);
		adapter.notifyDataSetChanged();
		faceLinearLayout.setVisibility(View.GONE);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				currentPage = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.msg_listView:
			imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
			faceLinearLayout.setVisibility(View.GONE);
			isFaceShow = false;
			break;
		case R.id.msg_et:
			imm.showSoftInput(msgEt, 0);
			faceLinearLayout.setVisibility(View.GONE);
			isFaceShow = false;
			break;
		}
		return false;
	}

}
