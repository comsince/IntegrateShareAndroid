package com.comsince.phonebook.menu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.activity.GroupFriendActivity;
import com.comsince.phonebook.activity.JoinGroupDialogActivity;
import com.comsince.phonebook.adapter.MGroupAdapter;
import com.comsince.phonebook.asynctask.GeneralAsyncTask;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.Group;
import com.comsince.phonebook.entity.Groups;
import com.comsince.phonebook.ui.base.FlipperLayout.OnOpenListener;
import com.comsince.phonebook.uikit.MMAlert;
import com.comsince.phonebook.util.AndroidUtil;

public class MGroup {
	private Context mContext;
	private PhoneBookApplication phoneBookApplication;
	private View mGroup;
	private Button mMenu;
	private ListView mDisplay;
	private EditText mSearch;
	private Button selection;
	
	private List<Group> mGroupResult = new ArrayList<Group>();
	private MGroupAdapter mGroupAdapter;
	
	private OnOpenListener mOnOpenListener;
	//通用asyncTask
	GeneralAsyncTask generalAsyncTask;
	
	private static final int MMAlertSelect_Get_Group  =  0;
	private static final int MMAlertSelect_Create_Group  =  1;
	private static final int MMAlertSelect_Join_Group  =  2;
	public static final int SUCCESS = 0;
	public static final int FAIL = 1;
	
	public MGroup(Context context,PhoneBookApplication application){
		this.mContext = context;
		this.phoneBookApplication = application;
		mGroup = LayoutInflater.from(context).inflate(R.layout.group, null);
		findViewById();
		setListener();
		initData();
	}
	
	public void findViewById(){
		mMenu = (Button) mGroup.findViewById(R.id.group_menu);
		mDisplay = (ListView) mGroup.findViewById(R.id.group_display);
		mSearch = (EditText) mGroup.findViewById(R.id.group_search);
		selection = (Button) mGroup.findViewById(R.id.group_add);
	}
	
	public void setListener(){
		mMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mOnOpenListener != null) {
					mOnOpenListener.open();
				}
			}
		});
		selection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectDialog();
			}
		});
        mDisplay.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent();
				intent.putExtra("groupTag", mGroupResult.get(position).getGroupTag());
				intent.setClass(mContext, GroupFriendActivity.class);
				((Activity)mContext).startActivity(intent);
			}
		});
	}
	
	public void initData(){
		getGroupData();
		mGroupAdapter = new MGroupAdapter(mContext, mGroupResult);
		mDisplay.setAdapter(mGroupAdapter);
	}
	/**
	 * 从本地获取分组信息
	 * */
	public void getGroupData(){
		String filePath = AndroidUtil.getSDCardRoot()+Constant.PHONE_BOOK_PATH+File.separator+Constant.DIR_PERSON_INFO+File.separator;
		String fileName = phoneBookApplication.phoneBookPreference.getUserName(mContext)+"_"+phoneBookApplication.phoneBookPreference.getPassWord(mContext)+"_"+Constant.FILE_GROUP_SUFFIX+".xml";
		String fileUrl = filePath + fileName;
		if(new File(fileUrl).exists()){
			try {
				mGroupResult = phoneBookApplication.simpleXmlReader.readXml(fileUrl, Groups.class).getGroups();
				phoneBookApplication.mGroupResult = mGroupResult;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 功能选项
	 * */
	public void selectDialog(){
		MMAlert.showAlert(mContext, mContext.getString(R.string.select_info), mContext.getResources().getStringArray(R.array.select_item_group), null, new MMAlert.OnAlertSelectId(){

			@Override
			public void onClick(int whichButton) {
				switch (whichButton) {
				case MMAlertSelect_Get_Group:
					/**
					 * 1,传递适配器对象
					 * 2,利用handler传递消息
					 * **/
					generalAsyncTask = new GeneralAsyncTask(mContext.getString(R.string.person_group_info_download), Constant.TASK_DOWNLOAD_PERSON_GROUP_INFO, mContext,groupHandler);
					generalAsyncTask.execute();
					break;
				case MMAlertSelect_Create_Group:
					
					break;
				case MMAlertSelect_Join_Group:
					joinGroup();
					break;
				default:
					break;
				}
			}
			
		});
	}
	
	public void joinGroup(){
		Intent intent = new Intent();
		intent.setClass(mContext, JoinGroupDialogActivity.class);
		((Activity)mContext).startActivity(intent);
	}
	
	Handler groupHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				getGroupData();
				mGroupAdapter.refreshData(mGroupResult);
				break;
			case FAIL:
				Toast.makeText(mContext, "下载分组失败，请重试", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
		
	};
	
	public void refreshGroupData(){
		getGroupData();
		mGroupAdapter.refreshData(mGroupResult);
	}
	
	/**
	 * 获取页面的view
	 * */
	public View getView(){
		return mGroup;
	}
	
	/**
	 * 设置打开侧边栏的监听器，在主activity中调用
	 * */
	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}
	
	
}
