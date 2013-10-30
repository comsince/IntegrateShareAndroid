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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.activity.GroupFriendActivity;
import com.comsince.phonebook.activity.JoinGroupDialogActivity;
import com.comsince.phonebook.activity.group.CreateGroupActivity;
import com.comsince.phonebook.activity.message.ChatActivity;
import com.comsince.phonebook.adapter.MGroupAdapter;
import com.comsince.phonebook.asynctask.GeneralAsyncTask;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.Group;
import com.comsince.phonebook.entity.Groups;
import com.comsince.phonebook.entity.User;
import com.comsince.phonebook.receiver.PushMessageReceiver.EventHandler;
import com.comsince.phonebook.ui.base.FlipperLayout.OnOpenListener;
import com.comsince.phonebook.uikit.MMAlert;
import com.comsince.phonebook.util.AndroidUtil;
import com.comsince.phonebook.view.quickactionbar.QuickAction;
import com.comsince.phonebook.view.quickactionbar.QuickActionBar;
import com.comsince.phonebook.view.quickactionbar.QuickActionWidget;
import com.comsince.phonebook.view.quickactionbar.QuickActionWidget.OnQuickActionClickListener;

public class MGroup implements OnQuickActionClickListener{
	private Context mContext;
	private PhoneBookApplication phoneBookApplication;
	private View mGroup;
	private Button mMenu;
	private ListView mDisplay;
	private EditText mSearch;
	private Button selection;
	private QuickActionWidget mBar;
	
	private List<Group> mGroupResult = new ArrayList<Group>();
	private MGroupAdapter mGroupAdapter;
	
	private OnOpenListener mOnOpenListener;
	//通用asyncTask
	GeneralAsyncTask generalAsyncTask;
	
	private int onItemLongClickPosition;
	
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
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				//Intent intent = new Intent();
				//intent.putExtra("groupTag", mGroupResult.get(position).getGroupTag());
				//intent.setClass(mContext, GroupFriendActivity.class);
				//((Activity)mContext).startActivity(intent);
				onItemLongClickPosition = position;
				showChildQuickActionBar(view.findViewById(R.id.friends_item_avatar));
			}
		});
        mDisplay.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long arg3) {
				onItemLongClickPosition = position;
				showChildQuickActionBar(view.findViewById(R.id.friends_item_avatar));
				return false;
			}
		});
	}
	
	public void initData(){
		getGroupData();
		mGroupAdapter = new MGroupAdapter(mContext, mGroupResult);
		mGroupAdapter.setListview(mDisplay);
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
					createGroup();
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
	
	public void createGroup(){
		Intent intent = new Intent();
		intent.setClass(mContext, CreateGroupActivity.class);
		mContext.startActivity(intent);
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
	
	/**
	 * 初始化actionbar
	 * **/
	private void showChildQuickActionBar(View view) {
		mBar = new QuickActionBar(mContext);
		mBar.addQuickAction(new QuickAction(mContext, R.drawable.ic_action_share_pressed, R.string.open));
		mBar.addQuickAction(new QuickAction(mContext, R.drawable.ic_action_rename_pressed, R.string.groupchat));
		mBar.addQuickAction(new QuickAction(mContext, R.drawable.ic_action_move_pressed, R.string.about));
		mBar.addQuickAction(new QuickAction(mContext, R.drawable.ic_action_delete_pressed, R.string.delete));
		mBar.setOnQuickActionClickListener(this);
		mBar.show(view);
	}

	@Override
	public void onQuickActionClicked(QuickActionWidget widget, int position) {
		switch (position) {
		case 0:
			//打开该群组的人员列表
			goToFriendList();
			break;
		case 1:
			//群聊
			chatToGroup();
			break;
		case 2:
			break;
		case 3:
			break;

		}
	}
	
	/**
	 * 跳转到朋友列表
	 * */
	public void goToFriendList(){
		Intent intent = new Intent();
		intent.putExtra("groupTag", mGroupResult.get(onItemLongClickPosition).getGroupTag());
		intent.setClass(mContext, GroupFriendActivity.class);
		((Activity)mContext).startActivity(intent);
	}
	
	/**
	 *群聊天
	 * **/
	public void chatToGroup(){
		Intent intent = new Intent();
		intent.putExtra("group", mGroupResult.get(onItemLongClickPosition));
		//intent.putExtra("groupTag", mGroupResult.get(onItemLongClickPosition).getGroupTag());
		intent.setClass(mContext, ChatActivity.class);
		((Activity)mContext).startActivity(intent);
		mGroupAdapter.refreshComingMsg(mGroupResult.get(onItemLongClickPosition).getGroupTag(), MGroupAdapter.CLEAR_MESSAGE);
	}
	
	/**
	 * 更新群组消息来临状态
	 * **/
	public void refreshGroupMsgState(String groupTag){
		mGroupAdapter.refreshComingMsg(groupTag,MGroupAdapter.NEW_MESSAGE);
	}
	
}
