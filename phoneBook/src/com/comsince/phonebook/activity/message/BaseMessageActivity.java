package com.comsince.phonebook.activity.message;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.adapter.ChatAdapter;
import com.comsince.phonebook.adapter.FaceAdapter;
import com.comsince.phonebook.dbhelper.MessageDB;
import com.comsince.phonebook.entity.Group;
import com.comsince.phonebook.entity.MessageItem;
import com.comsince.phonebook.entity.User;
import com.comsince.phonebook.preference.PhoneBookPreference;
import com.comsince.phonebook.receiver.PushMessageReceiver.EventHandler;
import com.comsince.phonebook.view.extendlistview.MsgListView;
import com.comsince.phonebook.view.extendlistview.MsgListView.IXListViewListener;
import com.comsince.phonebook.view.viewpager.CirclePageIndicator;
import com.comsince.phonebook.view.viewpager.JazzyViewPager;
import com.google.gson.Gson;

public abstract class BaseMessageActivity extends Activity 
       implements TextWatcher,OnClickListener,EventHandler,IXListViewListener,OnTouchListener{
    /**基本组件**/
	protected MsgListView mMsgListView;
	protected TextView chatFriendName;
	protected Button backBtn;
	protected Button sendBtn;
	protected ImageButton faceBtn;
	protected EditText msgEt;
	protected Button aboutFriend;
	
	/**底部表情输入框**/
	protected LinearLayout faceLinearLayout;
	protected JazzyViewPager faceViewPager;
	protected CirclePageIndicator indicator;
	protected int currentPage = 0;
	protected List<String> keys;
	protected boolean isFaceShow = false;
	protected InputMethodManager imm;
	protected WindowManager.LayoutParams params;
	
	protected Context context;
	protected List<MessageItem>	msgList;
	protected ChatAdapter msgAdapter;
	
	protected PhoneBookPreference phoneBookPre;
	protected PhoneBookApplication phoneBookApplication;
	protected Gson mGson;
	
	protected Group group;
	
	//消息传递对象，即是发给谁
	protected User mFromUser;
	protected MessageDB mMsgDB;
	/**消息一次加载的页数**/
	protected static int MsgPagerNum;
	
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
		initFacePage();
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
	
	/**
	 * 初始化表情
	 * **/
	protected abstract void initFacePage();
	
	
	/**
	 * 加载消息历史，从数据库中读出
	 */
	protected List<MessageItem> initMsgData() {
		List<MessageItem> list = mMsgDB.getMsg(mFromUser.getUserId(),MsgPagerNum);
		List<MessageItem> msgList = new ArrayList<MessageItem>();// 消息对象数组
		if (list.size() > 0) {
			for (MessageItem entity : list) {
				if (entity.getName().equals("")) {
					entity.setName(mFromUser.getNick());
				}
				if (entity.getHeadImg() < 0) {
					entity.setHeadImg(mFromUser.getHeadIcon());
				}
				msgList.add(entity);
			}
		}
		return msgList;

	}
	
	protected List<MessageItem> initMsgData(String condition){
		List<MessageItem> list = mMsgDB.getMsg(condition,MsgPagerNum);
		List<MessageItem> msgList = new ArrayList<MessageItem>();// 消息对象数组
		if (list.size() > 0) {
			for (MessageItem entity : list) {
				msgList.add(entity);
			}
		}
		return msgList;
	}
	
	/**
	 * 获取表情翻页效果单个显示页面
	 * **/
	protected GridView getGridView(int i) {
		GridView gv = new GridView(this);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(1);
		gv.setVerticalSpacing(1);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new FaceAdapter(this, i));
		// gv.setOnTouchListener(forbidenScroll());
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == PhoneBookApplication.NUM) {// 删除键的位置
					int selection = msgEt.getSelectionStart();
					String text = msgEt.getText().toString();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							msgEt.getText().delete(start, end);
							return;
						}
						msgEt.getText().delete(selection - 1, selection);
					}
				} else {
					int count = currentPage * PhoneBookApplication.NUM + arg2;
					// 注释的部分，在EditText中显示字符串
					// String ori = msgEt.getText().toString();
					// int index = msgEt.getSelectionStart();
					// StringBuilder stringBuilder = new StringBuilder(ori);
					// stringBuilder.insert(index, keys.get(count));
					// msgEt.setText(stringBuilder.toString());
					// msgEt.setSelection(index + keys.get(count).length());

					// 下面这部分，在EditText中显示表情
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), (Integer) PhoneBookApplication.getInstance().getFaceMap().values().toArray()[count]);
					if (bitmap != null) {
						int rawHeigh = bitmap.getHeight();
						int rawWidth = bitmap.getHeight();
						int newHeight = 40;
						int newWidth = 40;
						// 计算缩放因子
						float heightScale = ((float) newHeight) / rawHeigh;
						float widthScale = ((float) newWidth) / rawWidth;
						// 新建立矩阵
						Matrix matrix = new Matrix();
						matrix.postScale(heightScale, widthScale);
						// 设置图片的旋转角度
						// matrix.postRotate(-30);
						// 设置图片的倾斜
						// matrix.postSkew(0.1f, 0.1f);
						// 将图片大小压缩
						// 压缩后图片的宽和高以及kB大小均会变化
						Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, rawWidth, rawHeigh, matrix, true);
						ImageSpan imageSpan = new ImageSpan(context, newBitmap);
						String emojiStr = keys.get(count);
						SpannableString spannableString = new SpannableString(emojiStr);
						spannableString.setSpan(imageSpan, emojiStr.indexOf('['), emojiStr.indexOf(']') + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						msgEt.append(spannableString);
					} else {
						String ori = msgEt.getText().toString();
						int index = msgEt.getSelectionStart();
						StringBuilder stringBuilder = new StringBuilder(ori);
						stringBuilder.insert(index, keys.get(count));
						msgEt.setText(stringBuilder.toString());
						msgEt.setSelection(index + keys.get(count).length());
					}
				}
			}
		});
		return gv;
	}
	
	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == NEW_MESSAGE) {
				if(mFromUser != null){
					com.comsince.phonebook.entity.Message msgItem = (com.comsince.phonebook.entity.Message) msg.obj;
					String userId = msgItem.getUser_id();
					if (!userId.equals(mFromUser.getUserId()))// 如果不是当前正在聊天对象的消息，不处理
						return;
					//int headId = msgItem.getHead_id();
					int headId = 0;
					MessageItem item = new MessageItem(MessageItem.MESSAGE_TYPE_TEXT, msgItem.getNick(), System.currentTimeMillis(), msgItem.getMessage(), headId, true, 0 ,msgItem.getAvatar_name());
					msgAdapter.upDateMsg(item);
					mMsgListView.setSelection(msgAdapter.getCount() - 1);
					mMsgDB.saveMsg(msgItem.getUser_id(), item);
					//RecentItem recentItem = new RecentItem(userId, headId, msgItem.getNick(), msgItem.getMessage(), 0, System.currentTimeMillis());
					//mRecentDB.saveRecent(recentItem);
				}
				
				if(group != null){
					com.comsince.phonebook.entity.Message msgItem = (com.comsince.phonebook.entity.Message) msg.obj;
					String userId = msgItem.getUser_id();
					//过滤自己发送的群消息
					if (userId.equals(PhoneBookApplication.getInstance().getPreference().getUserId()))
						return;
					int headId = 0;
					MessageItem item = new MessageItem(MessageItem.MESSAGE_TYPE_TEXT, msgItem.getNick(), System.currentTimeMillis(), msgItem.getMessage(), headId, true, 0 ,msgItem.getAvatar_name());
					msgAdapter.upDateMsg(item);
					mMsgListView.setSelection(msgAdapter.getCount() - 1);
					mMsgDB.saveMsg(group.getGroupTag(), item);
					//RecentItem recentItem = new RecentItem(userId, headId, msgItem.getNick(), msgItem.getMessage(), 0, System.currentTimeMillis());
					//mRecentDB.saveRecent(recentItem);
				}
			}
		}
	};
}
