package com.comsince.phonebook.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.adapter.GroupByLetterAdapter;
import com.comsince.phonebook.asynctask.GeneralAsyncTask;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.GroupPerson;
import com.comsince.phonebook.ui.base.MyLetterListView;
import com.comsince.phonebook.util.AndroidUtil;
import com.comsince.phonebook.util.PhoneBookUtil;

public class GroupFriendActivity extends Activity implements OnClickListener{
	private Context context;
	private PhoneBookApplication phoneBookApplication;
	private ListView mDisplay;
	private EditText mSearch;
	private Button groupBtn;
	private Button functionBtn;
	private MyLetterListView mLetter;
	private GroupByLetterAdapter groupByLetterAdapter;
	private List<GroupPerson> groupPersonList = new ArrayList<GroupPerson>();
	private String groupTag;
	private GeneralAsyncTask downGroupPerson;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_friends);
		context = this;
		groupTag = this.getIntent().getStringExtra("groupTag");
		initView();
		setUpListener();
		initData();
		downLoadGroupPerson();
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(!AndroidUtil.getNetWorkStatus(context)){
			groupByLetterAdapter.refreshData(groupPersonList);
		}
	}
	
	public void initView(){
		mDisplay = (ListView) findViewById(R.id.friends_display);
		mSearch = (EditText) findViewById(R.id.friends_search);
		mLetter = (MyLetterListView) findViewById(R.id.friends_letter);
		groupBtn = (Button) findViewById(R.id.about_back);
		functionBtn = (Button) findViewById(R.id.about_submit);
	}
	
	public void setUpListener(){
		groupBtn.setOnClickListener(this);
		functionBtn.setOnClickListener(this);
	}
	
	public void initData(){
		//groupPersonList = PhoneBookUtil.getCurrentJoinGroupPersonInfo(context,groupTag);
		groupByLetterAdapter = new GroupByLetterAdapter(context, groupPersonList);
		mDisplay.setAdapter(groupByLetterAdapter);
	}
	
	public void downLoadGroupPerson(){
		if(AndroidUtil.getNetWorkStatus(context)){
			downGroupPerson = new GeneralAsyncTask("正在下载分组人员信息...", Constant.TASK_DOWNLOAD_PERSON_GROUPPERSON, context, downGroupPersonHandler);
			downGroupPerson.execute(groupTag);
		}else{
			groupPersonList = PhoneBookUtil.getCurrentJoinGroupPersonInfo(context,groupTag);
		}
		
	}
	
	public void updataGroupPerson(){
		groupPersonList = PhoneBookUtil.getCurrentJoinGroupPersonInfo(context,groupTag);
		groupByLetterAdapter.refreshData(groupPersonList);
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
	
	Handler downGroupPersonHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.DOWN_LOAD_GROUPPERSON_SUCCESS:
				updataGroupPerson();
				break;

			default:
				break;
			}
		}
		
	};
	
}
