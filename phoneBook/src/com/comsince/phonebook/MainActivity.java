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
	
	private FlipperLayout mRoot;
	
	private Desktop mDesktop;
	
	private Home mHome;
	
	private Friends mFriends;

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
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
