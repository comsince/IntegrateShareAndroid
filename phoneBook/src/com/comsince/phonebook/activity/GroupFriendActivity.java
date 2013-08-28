package com.comsince.phonebook.activity;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.ui.base.MyLetterListView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class GroupFriendActivity extends Activity implements OnClickListener{
	private Context context;
	private PhoneBookApplication phoneBookApplication;
	private View mFriends;
	private ListView mDisplay;
	private EditText mSearch;
	private Button groupBtn;
	private Button functionBtn;
	private MyLetterListView mLetter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_friends);
		initView();
		setUpListener();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void initView(){
		mDisplay = (ListView) mFriends.findViewById(R.id.friends_display);
		mSearch = (EditText) mFriends.findViewById(R.id.friends_search);
		mLetter = (MyLetterListView) mFriends.findViewById(R.id.friends_letter);
		groupBtn = (Button) mFriends.findViewById(R.id.about_back);
		functionBtn = (Button) mFriends.findViewById(R.id.about_submit);
	}
	
	public void setUpListener(){
		groupBtn.setOnClickListener(this);
		functionBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_back:
			finish();
			break;
		case R.id.about_submit:
			break;
		default:
			break;
		}
	}
	
}
