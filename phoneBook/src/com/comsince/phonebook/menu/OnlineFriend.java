package com.comsince.phonebook.menu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.adapter.OnlineFriendAdapter;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.User;
import com.comsince.phonebook.ui.base.FlipperLayout.OnOpenListener;
import com.comsince.phonebook.view.pulltorefreshlistview.RefreshListView;
import com.comsince.phonebook.view.pulltorefreshlistview.RefreshListView.OnCancelListener;
import com.comsince.phonebook.view.pulltorefreshlistview.RefreshListView.OnRefreshListener;

public class OnlineFriend implements OnRefreshListener,OnCancelListener{
	
	private Context mContext;
	private PhoneBookApplication phoneBookApplication;
	private View mOnlineFriend;
	private Button mMenu;
	private RefreshListView mRefreshListView;
	private OnlineFriendAdapter mOnlineFriendAdapter;
	private List<User> mUser;
	
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
		mRefreshListView.setOnCancelListener(this);
		mRefreshListView.setOnRefreshListener(this);
	}
	
	private void initData(){
		mUser = new ArrayList<User>();
		mOnlineFriendAdapter = new OnlineFriendAdapter(mContext, mUser);
		mRefreshListView.setAdapter(mOnlineFriendAdapter);
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

	/**获取在线好友界面*/
	public View getView(){
		return mOnlineFriend;
	}
	
	
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
}
