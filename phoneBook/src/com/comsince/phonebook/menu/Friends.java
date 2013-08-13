package com.comsince.phonebook.menu;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


public class Friends {
	private Context mContext;
	private PhoneBookApplication phoneBookApplication;
	private View mFriends;
	public Friends(Context context, PhoneBookApplication application) {
		mContext = context;
		phoneBookApplication = application;
		mFriends = LayoutInflater.from(context).inflate(R.layout.friends, null);
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
		return mFriends;
	}

}
