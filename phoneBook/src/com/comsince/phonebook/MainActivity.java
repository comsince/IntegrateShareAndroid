package com.comsince.phonebook;



import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.comsince.phonebook.asynctask.SendMsgAsyncTask;
import com.comsince.phonebook.asynctask.SendMsgAsyncTask.OnSendScuessListener;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.dbhelper.UserDB;
import com.comsince.phonebook.entity.User;
import com.comsince.phonebook.menu.Desktop;
import com.comsince.phonebook.menu.Desktop.onChangeViewListener;
import com.comsince.phonebook.menu.Friends;
import com.comsince.phonebook.menu.Home;
import com.comsince.phonebook.menu.MGroup;
import com.comsince.phonebook.menu.OnlineFriend;
import com.comsince.phonebook.preference.PhoneBookPreference;
import com.comsince.phonebook.receiver.PushMessageReceiver;
import com.comsince.phonebook.receiver.PushMessageReceiver.EventHandler;
import com.comsince.phonebook.ui.base.FlipperLayout;
import com.comsince.phonebook.ui.base.FlipperLayout.OnOpenListener;
import com.comsince.phonebook.util.BaiduPushUtil;
import com.comsince.phonebook.util.L;
import com.comsince.phonebook.util.T;
import com.comsince.phonebook.util.ViewUtil;
import com.google.gson.Gson;



public class MainActivity extends Activity implements OnOpenListener,EventHandler{
	protected PhoneBookApplication phoneBookApplication;
	private PhoneBookPreference phonebookPreference;
	
	private FlipperLayout mRoot;
	
	private Desktop mDesktop;
	
	private Home mHome;
	
	private Friends mFriends;
	
	private MGroup mGroup;
	/**在线好友页面**/
	private OnlineFriend mOnlineFriend;
	/**
	 * 当前显示的View的编号
	 */
	private int mViewPosition;
	
	private Context context;
	
	private UserDB mUserDB;
	//private MessageDB mMsgDB;
	private Gson mGson;
	/**发送消息**/
	private SendMsgAsyncTask task;
	
	/**
	 * 接收加入群组tag的广播
	 * */
	private AddTagBroadcastReceiber addTagReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		//百度云推送服务初始化api
	    initBaiduPushService();
		//在manifest中声明
		phoneBookApplication = (PhoneBookApplication) getApplication();
		phonebookPreference = PhoneBookApplication.phoneBookPreference;
		//建立并注册广播
		addTagReceiver = new AddTagBroadcastReceiber();
		IntentFilter tagFilter = new IntentFilter();
		tagFilter.addAction(Constant.ACTION_ADD_TAG);
		registerReceiver(addTagReceiver, tagFilter);
		
		mRoot = new FlipperLayout(this);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mRoot.setLayoutParams(params);
		/**
		 * 只能有一个默认显示的页面
		 * */
		mDesktop = new Desktop(this, this);
		mHome = new Home(this, this);
		mGroup = new MGroup(this, phoneBookApplication);
		//mFriends = new Friends(this, phoneBookApplication);
		mRoot.addView(mDesktop.getView(), params);
		//mRoot.addView(mHome.getView(), params);
		//mRoot.addView(mFriends.getView(),params);
		mRoot.addView(mGroup.getView(),params);
		setContentView(mRoot);
		setListener();
		initData();
		Log.i("download", "notification !");
	}
	
	public void setListener(){
		/**
		 * 默认打开好友界面
		 * 增加菜单响应过程
		 * 1.destop中增加时间监听
		 * 2.在mainActivity中覆盖onChangeView
		 * 3.非主显示页面要初始化监听setOnOpenListener
		 * */
		//mFriends.setOnOpenListener(this);
		mGroup.setOnOpenListener(this);
		mDesktop.setOnChangeViewListener(new onChangeViewListener() {
			
			@Override
			public void onChangeView(int arg0) {
				mViewPosition = arg0;
				switch (arg0) {
				case ViewUtil.FRIENDS:
					if(mFriends == null){
						mFriends = new Friends(context, phoneBookApplication);
						mFriends.setOnOpenListener(MainActivity.this);
					}
					mRoot.close(mFriends.getView());
					break;
				case ViewUtil.GROUPS:
					mRoot.close(mGroup.getView());
					mGroup.refreshGroupData();
					break;
					
				case ViewUtil.ONLINE_PERSON:
					if(mOnlineFriend == null){
						mOnlineFriend = new OnlineFriend(context, phoneBookApplication);
						mOnlineFriend.setOnOpenListener(MainActivity.this);
					}
					mRoot.close(mOnlineFriend.getView());
					break;

				default:
					break;
				}
			}
		});
	}
	
	private void initData(){
		mUserDB = phoneBookApplication.getUserDB();
		mGson = phoneBookApplication.getGson();
		//回调监听,用户第一次登陆时回调用
		PushMessageReceiver.ehList.add(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGroup.refreshGroupData();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(addTagReceiver);
		super.onDestroy();
	}

	/**
	 * 实现接口以便进行侧边栏界面弹出
	 * */
	@Override
	public void open() {
		if (mRoot.getScreenState() == FlipperLayout.SCREEN_STATE_CLOSE) {
			mRoot.open();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if (mRoot.getScreenState() == FlipperLayout.SCREEN_STATE_CLOSE) {
				mRoot.open();
			}else{
				ToQuitTheApp();
			}
			return false;
		}else{
			return super.onKeyDown(keyCode, event);
		}
		
	}
	
	/**
	 * 退出
	 * */ 
	public boolean isExit = false;
	private void ToQuitTheApp() {
		if (isExit) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			//结束当前程序
			/*finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);*/
		} else {
			isExit = true;
			Toast.makeText(MainActivity.this, "再按一次退出通讯录", Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 3000);// 3秒后发送消息
		}
	}

	// 创建Handler对象，用来处理消息
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// 处理消息
			super.handleMessage(msg);
			isExit = false;
		}
	};
	
	/**
	 * 初始化百度云推送服务
	 * */
	public void initBaiduPushService(){
		PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, Constant.BAIDU_APP_KEY);
		//设置标签
		List<String> tags = BaiduPushUtil.getTagFromFile(context);
		PushManager.setTags(this, tags);
	}
	
	class AddTagBroadcastReceiber extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Constant.ACTION_ADD_TAG)){
				String tag = intent.getStringExtra("tag");
				if(!phoneBookApplication.tags.contains(tag)){
					phoneBookApplication.tags.add(tag);
					PushManager.setTags(context, phoneBookApplication.tags);
				}
			}
		}
		
	}

	@Override
	public void onMessage(com.comsince.phonebook.entity.Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBind(String method, int errorCode, String content) {
		if (errorCode == 0) {// 如果绑定账号成功，由于第一次运行，给同一tag的人推送一条新人消息
			User u = new User(phonebookPreference.getUserId(), phonebookPreference.getChannelId(),
					phonebookPreference.getUserName(), 0, 0);
			mUserDB.addUser(u);// 把自己添加到数据库
			//组装将要发送给其他在线用户的消息
		com.comsince.phonebook.entity.Message msgItem = new com.comsince.phonebook.entity.Message(System.currentTimeMillis(), "hi", "phoneBook");
			task = new SendMsgAsyncTask(mGson.toJson(msgItem), "");
			task.setOnSendScuessListener(new OnSendScuessListener() {

				@Override
				public void sendScuess() {
					
				}
			});
			task.send();
		}
	}

	@Override
	public void onNotify(String title, String content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetChange(boolean isNetConnected) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewFriend(User u) {
		Message handlerMsg = handler.obtainMessage(Constant.NEW_FRIEND);
		handlerMsg.obj = u;
		handler.sendMessage(handlerMsg);
		L.i(u.toString());
	}
	
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.NEW_FRIEND:
				User u = (User) msg.obj;
				if (mOnlineFriend == null){
					mOnlineFriend = new OnlineFriend(context, phoneBookApplication);
					mOnlineFriend.setOnOpenListener(MainActivity.this);
				}
				mOnlineFriend.refreshOnLineFriendData();// 更新
				T.showShort(phoneBookApplication, "好友列表已更新!");
				break;
			case Constant.NEW_MESSAGE:
				/*// String message = (String) msg.obj;
				com.way.bean.Message msgItem = (com.way.bean.Message) msg.obj;
				String userId = msgItem.getUser_id();
				String nick = msgItem.getNick();
				String content = msgItem.getMessage();
				int headId = msgItem.getHead_id();
				// try {
				// headId = Integer
				// .parseInt(JsonUtil.getFromUserHead(message));
				// } catch (Exception e) {
				// L.e("head is not integer  " + e);
				// }
				if (mUserDB.selectInfo(userId) == null) {// 如果不存在此好友，则添加到数据库
					User user = new User(userId, msgItem.getChannel_id(), nick,
							headId, 0);
					mUserDB.addUser(user);
					mLeftFragment = (LeftFragment) getSupportFragmentManager()
							.findFragmentById(R.id.main_left_fragment);
					mLeftFragment.updateAdapter();// 更新一下好友列表
				}
				// TODO Auto-generated method stub
				MessageItem item = new MessageItem(
						MessageItem.MESSAGE_TYPE_TEXT, nick,
						System.currentTimeMillis(), content, headId, true, 1);
				mMsgDB.saveMsg(userId, item);
				// 保存到最近会话列表
				RecentItem recentItem = new RecentItem(userId, headId, nick,
						content, 0, System.currentTimeMillis());
				mRecentDB.saveRecent(recentItem);
				mAdapter.addFirst(recentItem);
				T.showShort(mApplication, nick + ":" + content);*/
				break;
			default:
				break;
			}
		}
	};

	
}
