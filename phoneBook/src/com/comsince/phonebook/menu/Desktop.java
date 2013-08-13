package com.comsince.phonebook.menu;


import com.comsince.phonebook.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class Desktop {
	private Context mContext;
	private Activity mActivity;
	/**
	 * 当前界面的View
	 */
	private View mDesktop;
	
	public Desktop(Context context, Activity activity) {
		mContext = context;
		mActivity = activity;
		// 绑定布局到当前View
		mDesktop = LayoutInflater.from(context).inflate(R.layout.desktop, null);
		findViewById();
		setListener();
		init();
	}
	
	public void findViewById(){
		
	}
	
	public void setListener(){
		
	}
	
	public void init(){
		
	}
	
	/**
	 * 获取菜单界面
	 * 
	 * @return 菜单界面的View
	 */
	public View getView() {
		return mDesktop;
	}

}
