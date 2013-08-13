package com.comsince.phonebook;



import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;

import com.comsince.phonebook.menu.Desktop;
import com.comsince.phonebook.menu.Friends;
import com.comsince.phonebook.menu.Home;
import com.comsince.phonebook.ui.base.FlipperLayout;

public class MainActivity extends Activity {
	protected PhoneBookApplication phoneBookApplication;
	/**
	 * 当前显示内容的容器(继承于ViewGroup)
	 */
	private FlipperLayout mRoot;
	/**
	 * 菜单界面
	 */
	private Desktop mDesktop;
	/**
	 * 内容首页界面
	 */
	private Home mHome;
	/**
	 * 好友首页界面
	 */
	private Friends mFriends;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//要在mainfest中声明该application
		phoneBookApplication = (PhoneBookApplication) getApplication();
		/**
		 * 创建容器,并设置全屏大小
		 */
		mRoot = new FlipperLayout(this);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mRoot.setLayoutParams(params);
		/**
		 * 主页显示只有一个，不要装载多个
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
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
