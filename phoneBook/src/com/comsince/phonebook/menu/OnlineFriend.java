package com.comsince.phonebook.menu;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.activity.message.ChatActivity;
import com.comsince.phonebook.adapter.OnlineFriendAdapter;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.dbhelper.UserDB;
import com.comsince.phonebook.entity.User;
import com.comsince.phonebook.receiver.PushMessageReceiver;
import com.comsince.phonebook.receiver.PushMessageReceiver.EventHandler;
import com.comsince.phonebook.ui.base.FlipperLayout.OnOpenListener;
import com.comsince.phonebook.util.L;
import com.comsince.phonebook.util.T;
import com.comsince.phonebook.util.TimeUtil;
import com.comsince.phonebook.view.pulltorefreshlistview.RefreshListView;
import com.comsince.phonebook.view.pulltorefreshlistview.RefreshListView.OnCancelListener;
import com.comsince.phonebook.view.pulltorefreshlistview.RefreshListView.OnRefreshListener;
import com.comsince.phonebook.view.quickactionbar.QuickAction;
import com.comsince.phonebook.view.quickactionbar.QuickActionBar;
import com.comsince.phonebook.view.quickactionbar.QuickActionWidget;
import com.comsince.phonebook.view.quickactionbar.QuickActionWidget.OnQuickActionClickListener;

public class OnlineFriend implements OnRefreshListener,OnCancelListener,EventHandler,OnQuickActionClickListener{
	
	private Context mContext;
	private PhoneBookApplication phoneBookApplication;
	private View mOnlineFriend;
	private Button mMenu,mOperation;
	private RelativeLayout ReOperation;
	private RefreshListView mRefreshListView;
	private OnlineFriendAdapter mOnlineFriendAdapter;
	private List<User> mUser;
	private UserDB mUserDB;
	private QuickActionWidget mBar;
	private int onItemClickPosition;
	private boolean isoperationBarGone = true;
	
	public static final int NEW_MESSAGE = 0x001;// 收到消息
	
	private OnOpenListener mOnOpenListener;
	
	public OnlineFriend(Context context,PhoneBookApplication application){
		this.mContext = context;
		this.phoneBookApplication = application;
		mOnlineFriend = LayoutInflater.from(context).inflate(R.layout.fragment_onlinefriend, null);
		findViewById();
		setListener();
		initData();
	}
	
	private void findViewById(){
		mRefreshListView = (RefreshListView) mOnlineFriend.findViewById(R.id.online_friend_list);
		mMenu = (Button) mOnlineFriend.findViewById(R.id.online_friend_menu);
		mOperation = (Button) mOnlineFriend.findViewById(R.id.group_add);
		ReOperation = (RelativeLayout) mOnlineFriend.findViewById(R.id.conversation_options_page);
	}
	
	private void setListener(){
		mMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnOpenListener != null) {
					mOnOpenListener.open();
				}
			}
		});
		
		mOperation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isoperationBarGone){
					ReOperation.setVisibility(View.VISIBLE);
					isoperationBarGone = false;
				}else{
					ReOperation.setVisibility(View.GONE);
					isoperationBarGone = true;
				}
			}
		});
		
		mRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long args) {
				if(position != 0){
					Intent toChatIntent = new Intent(mContext,ChatActivity.class);
					toChatIntent.putExtra("user", mUser.get(position-1));
					mContext.startActivity(toChatIntent);
				}
			}
		});
		
		mRefreshListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long arg3) {
				if(position != 0){
					onItemClickPosition = position;
					//T.showLong(mContext, String.valueOf(onItemClickPosition));
					showChildQuickActionBar(view.findViewById(R.id.icon));
				}
				return false;
			}
		});
		mRefreshListView.setOnCancelListener(this);
		mRefreshListView.setOnRefreshListener(this);
	}
	
	private void initData(){
		//PushMessageReceiver.ehList.add(this);
		mUserDB = phoneBookApplication.getUserDB();
		mUser = mUserDB.getUser();
		mOnlineFriendAdapter = new OnlineFriendAdapter(mContext, mUser);
		mRefreshListView.setAdapter(mOnlineFriendAdapter);
		//测试
		for(User user : mUser){
			L.i(user.toString());
		}
	}
	
	public void invilidateListView(){
		if(mRefreshListView != null){
			mRefreshListView.invalidateViews();
		}
	}
	
	/**
	 * 初始化actionbar
	 * **/
	private void showChildQuickActionBar(View view) {
		mBar = new QuickActionBar(mContext);
		mBar.addQuickAction(new QuickAction(mContext, R.drawable.ic_action_share_pressed, R.string.open));
		mBar.addQuickAction(new QuickAction(mContext, R.drawable.ic_action_rename_pressed, R.string.rename));
		mBar.addQuickAction(new QuickAction(mContext, R.drawable.ic_action_move_pressed, R.string.move));
		mBar.addQuickAction(new QuickAction(mContext, R.drawable.ic_action_delete_pressed, R.string.delete));
		mBar.setOnQuickActionClickListener(this);
		mBar.show(view);
	}
	
	/**
	 * 刷新好友列表
	 * **/
	public void refreshOnLineFriendData(User user){
		int i = 0;
		for(User u : mUser){
		   if(u.getUserId().equals(user.getUserId())){
			   break;
		   }	
		   i++;
		}
		if(i == mUser.size()){
			mUser.add(user);
		}
		mOnlineFriendAdapter.refreshData(mUser);
	}
	
	public void refreshOnLineFriendData(){
		mUser = mUserDB.getUser();
		mOnlineFriendAdapter.refreshData(mUser);
	}
	
	/**
	 * 更新用户消息
	 * **/
	public void refreshOnLineFriendData(String userId,String userMsg){
		mOnlineFriendAdapter.refreshData(userId, userMsg);
	}

	/**获取在线好友界面*/
	public View getView(){
		return mOnlineFriend;
	}
	
	/**
	 * 更新来临的消息
	 * **/
	
	/**
	 * 设置打开侧边栏的监听器，在主activity中调用
	 * */
	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}

	@Override
	public void onCancel() {
		mRefreshListView.onRefreshComplete();
	}

	@Override
	public void onRefresh() {
		PushManager.stopWork(mContext);
		PushManager.startWork(mContext, PushConstants.LOGIN_TYPE_API_KEY, Constant.BAIDU_APP_KEY);
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {

				}
				return null;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				mRefreshListView.onRefreshComplete();
			}
		}.execute();
	}

	@Override
	public void onMessage(com.comsince.phonebook.entity.Message message) {
		/*Message handlerMsg = handler.obtainMessage(NEW_MESSAGE);
		handlerMsg.obj = message;
		handler.sendMessage(handlerMsg);*/
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
	
	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == NEW_MESSAGE) {
				com.comsince.phonebook.entity.Message msgItem = (com.comsince.phonebook.entity.Message) msg.obj;
				String userId = msgItem.getUser_id();
				if(!msgItem.getTag().equals(Constant.ONETOONE)){
					return;
				}
				//查找需要更新消息的用户
				for(User user : mUser){
					if(user.getUserId().equals(userId)){
						user.setMsg("["+TimeUtil.getChatTime(System.currentTimeMillis())+"] "+msgItem.getMessage());
						break;
					}
				}
				//刷新adapter
				mOnlineFriendAdapter.refreshData(mUser);
			}
		}
	};

	@Override
	public void onQuickActionClicked(QuickActionWidget widget, int position) {
		User u = mUser.get(onItemClickPosition-1);
		if (u != null)
			switch (position) {
			case 0:
				//mMsgDB.clearNewCount(u.getUserId());// 新消息置空
				Intent toChatIntent = new Intent(mContext,ChatActivity.class);
				toChatIntent.putExtra("user", u);
				mContext.startActivity(toChatIntent);
				break;
			case 1:
				T.showShort(mContext, "rename");
				break;
			case 2:
				T.showShort(mContext, "move");
				break;
			case 3:
				mUserDB.delUser(u);
				refreshOnLineFriendData();
				//mRecentDB.delRecent(u.getUserId());
				//((MainActivity) getActivity()).upDateList();
				T.showShort(mContext, "删除成功！");
				break;
			default:
				break;
			}
	}
	
}
