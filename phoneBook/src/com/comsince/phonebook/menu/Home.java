package com.comsince.phonebook.menu;


import com.comsince.phonebook.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class Home {
	private Context mContext;
	private Activity mActivity;
	private View mHome;

	public Home(Context context, Activity activity) {
		mContext = context;
		mActivity = activity;
		mHome = LayoutInflater.from(context).inflate(R.layout.home, null);
		findViewById();
		setListener();
		init();
	}

	public void findViewById() {

	}

	public void setListener() {

	}

	public void init() {

	}

	public View getView() {
		return mHome;
	}

}
