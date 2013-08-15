package com.comsince.phonebook;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.comsince.phonebook.menu.Desktop;
import com.comsince.phonebook.menu.Desktop.onChangeViewListener;
import com.comsince.phonebook.menu.Friends;
import com.comsince.phonebook.menu.Home;
import com.comsince.phonebook.ui.base.FlipperLayout;
import com.comsince.phonebook.ui.base.FlipperLayout.OnOpenListener;
import com.comsince.phonebook.util.ViewUtil;

public class MainActivity extends Activity implements OnOpenListener{
	protected PhoneBookApplication phoneBookApplication;
	
	private FlipperLayout mRoot;
	
	private Desktop mDesktop;
	
	private Home mHome;
	
	private Friends mFriends;
	/**
	 * 当前显示的View的编号
	 */
	private int mViewPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在manifest中声明
		phoneBookApplication = (PhoneBookApplication) getApplication();
		
		mRoot = new FlipperLayout(this);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mRoot.setLayoutParams(params);
		/**
		 * 只能有一个默认显示的页面
		 * */
		mDesktop = new Desktop(this, this);
		mHome = new Home(this, this);
		mFriends = new Friends(this, phoneBookApplication);
		mRoot.addView(mDesktop.getView(), params);
		//mRoot.addView(mHome.getView(), params);
		mRoot.addView(mFriends.getView(),params);
		setContentView(mRoot);
		setListener();
	}
	
	public void setListener(){
		/**
		 * 默认打开好友界面
		 * */
		mFriends.setOnOpenListener(this);
		mDesktop.setOnChangeViewListener(new onChangeViewListener() {
			
			@Override
			public void onChangeView(int arg0) {
				mViewPosition = arg0;
				switch (arg0) {
				case ViewUtil.FRIENDS:
					mRoot.close(mFriends.getView());
					break;

				default:
					break;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

	
}
