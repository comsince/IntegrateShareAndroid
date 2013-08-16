package com.comsince.phonebook.menu;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.activity.FriendInfoActivity;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.Person;
import com.comsince.phonebook.entity.Persons;
import com.comsince.phonebook.ui.base.FlipperLayout.OnOpenListener;
import com.comsince.phonebook.ui.base.MyLetterListView;
import com.comsince.phonebook.ui.base.MyLetterListView.OnTouchingLetterChangedListener;
import com.comsince.phonebook.util.AndroidUtil;
import com.comsince.phonebook.util.DataUtil;
import com.comsince.phonebook.util.FileUtil;
import com.comsince.phonebook.util.HttpTool;
import com.comsince.phonebook.util.SimpleXmlReaderUtil;

public class Friends {
	private Context mContext;
	private PhoneBookApplication phoneBookApplication;
	private View mFriends;
	private ListView mDisplay;
	private EditText mSearch;
	private Button mRefreshFriend;
	private View mLoading;
	private UpdatePhoneBookInfoThread updatePhoneBookInfoThread;
	/**
	 * 字符索引表
	 * */
	private MyLetterListView mLetter;
	private Button mMenu;
	private FriendInfoAdapter friendInfoAdapter;
	private OnOpenListener mOnOpenListener;
	// 当前显示的好友数据
	private List<Person> mMyFriendsResults = new ArrayList<Person>();
	// 当前显示的好友的姓名的首字母的在列表中的位置
	private List<Integer> mMyFriendsPosition = new ArrayList<Integer>();
	// 当前显示的好友的姓名的首字母数据
	private List<String> mMyFriendsFirstName = new ArrayList<String>();

	public Friends(Context context, PhoneBookApplication application) {
		mContext = context;
		phoneBookApplication = application;
		mFriends = LayoutInflater.from(context).inflate(R.layout.friends, null);
		findViewById();
		setListener();
		init();
	}

	public void findViewById() {
		mDisplay = (ListView) mFriends.findViewById(R.id.friends_display);
		mSearch = (EditText) mFriends.findViewById(R.id.friends_search);
		mLetter = (MyLetterListView) mFriends.findViewById(R.id.friends_letter);
		mMenu = (Button) mFriends.findViewById(R.id.friends_menu);
		mRefreshFriend = (Button) mFriends.findViewById(R.id.friends_add);
		mLoading = mFriends.findViewById(R.id.loadinginfo);
		mLoading.setVisibility(View.GONE);
	}

	public void setListener() {
		mSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String searchChar = s.toString().toUpperCase();
				mMyFriendsResults.clear();
				mMyFriendsPosition.clear();
				mMyFriendsFirstName.clear();
				// 判断输入内容的长度
				if (searchChar.length() > 0) {
					// 判断是否是字母
					if (searchChar.matches("^[a-z,A-Z].*$")) {
						// 判断当前好友里是有存在这个字母,有的话则取出数据更新界面,否则直接更新界面
						if (phoneBookApplication.mMyFriendsGroupByFirstName.containsKey(searchChar)) {
							List<Person> results = phoneBookApplication.mMyFriendsGroupByFirstName.get(searchChar);
							mMyFriendsResults.addAll(results);
							mMyFriendsFirstName.add(searchChar);
							mMyFriendsPosition.add(0);
							friendInfoAdapter.notifyDataSetChanged();
						} else {
							friendInfoAdapter.notifyDataSetChanged();
						}
					} else {
						friendInfoAdapter.notifyDataSetChanged();
					}
				} else {
					// 输入框没内容时,获取全部好友并更新界面
					mMyFriendsResults.addAll(phoneBookApplication.mMyFriendsResults);
					mMyFriendsPosition.addAll(phoneBookApplication.mMyFriendsPosition);
					mMyFriendsFirstName.addAll(phoneBookApplication.mMyFriendsFirstName);
					friendInfoAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		
		mLetter.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				// 根据触摸的字母,跳转到响应位置
				if (phoneBookApplication.mMyFriendsFirstNamePosition.get(s) != null) {
					mDisplay.setSelection(phoneBookApplication.mMyFriendsFirstNamePosition.get(s));
				}
			}
		});
		mMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mOnOpenListener != null) {
					mOnOpenListener.open();
				}
			}
		});
		mRefreshFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                mLoading.setVisibility(View.VISIBLE);
                mDisplay.setVisibility(View.GONE);
                isUpdate = true;
        		updatePhoneBookInfoThread = new UpdatePhoneBookInfoThread();
        		updatePhoneBookInfoThread.start();
			}
		});
	}

	public void init() {
		getFriendInfo();
		friendInfoAdapter = new FriendInfoAdapter();
		mDisplay.setAdapter(friendInfoAdapter);
	}

	public View getView() {
		return mFriends;
	}

	/**
	 * 初始化好友信息
	 * 
	 * */
	public void getFriendInfo() {
		if (phoneBookApplication.mMyFriendsResults.isEmpty() || isUpdate) {
			//如果application中存储数据不为空，则进来先进行数据清理
			phoneBookApplication.mMyFriendsResults.clear();
			phoneBookApplication.mMyFriendsFirstName.clear();
			phoneBookApplication.mMyFriendsPosition.clear();
			phoneBookApplication.mMyFriendsGroupByFirstName.clear();
			//
			SimpleXmlReaderUtil simpleXmlReader = phoneBookApplication.simpleXmlReader;
			try {
				InputStream friendInfoInfromSDCard = FileUtil.getInputSteamFromFile(AndroidUtil.getSDCardRoot()+Constant.PHONE_BOOK_PATH+File.separator+Constant.YC_ZG_PRIMARY_PRESON_FILE_NAME);
				if(friendInfoInfromSDCard !=null){
					Log.i("download", "Loading sdcard info");
					mMyFriendsResults = simpleXmlReader.readXmlFromInputStream(friendInfoInfromSDCard, Persons.class).getPersons();
				}else{
					InputStream friendInfoIn = mContext.getAssets().open("personinfo/person.xml");
					mMyFriendsResults = simpleXmlReader.readXmlFromInputStream(friendInfoIn, Persons.class).getPersons();
				}
				for (Person person : mMyFriendsResults) {
					person.setId(DataUtil.generateId());
					person.setName_pinyin(DataUtil.getStringPinYin(person.getName()));
					if (!TextUtils.isEmpty(person.getName_pinyin())) {
						person.setName_first(person.getName_pinyin().substring(0, 1).toUpperCase());
					}
					phoneBookApplication.mMyFriendsResults.add(person);

					if (person.getName_first().matches("^[a-z,A-Z].*$")) {
						if (phoneBookApplication.mMyFriendsFirstName.contains(person.getName_first())) {
							phoneBookApplication.mMyFriendsGroupByFirstName.get(person.getName_first()).add(person);
						} else {
							phoneBookApplication.mMyFriendsFirstName.add(person.getName_first());
							List<Person> list = new ArrayList<Person>();
							list.add(person);
							phoneBookApplication.mMyFriendsGroupByFirstName.put(person.getName_first(), list);
						}
					} else {
						if (phoneBookApplication.mMyFriendsFirstName.contains("#")) {
							phoneBookApplication.mMyFriendsGroupByFirstName.get("#").add(person);
						} else {
							phoneBookApplication.mMyFriendsFirstName.add("#");
							List<Person> list = new ArrayList<Person>();
							list.add(person);
							phoneBookApplication.mMyFriendsGroupByFirstName.put("#", list);
						}
					}
				}
				Collections.sort(phoneBookApplication.mMyFriendsFirstName);
				int position = 0;
				for (int i = 0; i < phoneBookApplication.mMyFriendsFirstName.size(); i++) {
					phoneBookApplication.mMyFriendsFirstNamePosition.put(phoneBookApplication.mMyFriendsFirstName.get(i), position);
					phoneBookApplication.mMyFriendsPosition.add(position);
					position += phoneBookApplication.mMyFriendsGroupByFirstName.get(phoneBookApplication.mMyFriendsFirstName.get(i)).size();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mMyFriendsResults.clear();
		mMyFriendsResults.addAll(phoneBookApplication.mMyFriendsResults);
		mMyFriendsPosition.addAll(phoneBookApplication.mMyFriendsPosition);
		mMyFriendsFirstName.addAll(phoneBookApplication.mMyFriendsFirstName);
	}

	class FriendInfoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mMyFriendsResults.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.friend_item, null);
				holder = new ViewHolder();
				holder.alpha = (TextView) convertView.findViewById(R.id.friends_item_alpha);
				holder.alpha_line = (ImageView) convertView.findViewById(R.id.friends_item_alpha_line);
				holder.avatar = (ImageView) convertView.findViewById(R.id.friends_item_avatar);
				holder.name = (TextView) convertView.findViewById(R.id.friends_item_name);
				holder.arrow = (ImageView) convertView.findViewById(R.id.friends_item_arrow);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			int section = getSectionForPosition(position);
			final Person person = phoneBookApplication.mMyFriendsGroupByFirstName.get(mMyFriendsFirstName.get(section)).get(position - getPositionForSection(section));
			if (getPositionForSection(section) == position) {
				holder.alpha.setVisibility(View.VISIBLE);
				holder.alpha_line.setVisibility(View.VISIBLE);
				holder.alpha.setText(mMyFriendsFirstName.get(section));
			} else {
				holder.alpha.setVisibility(View.GONE);
				holder.alpha_line.setVisibility(View.GONE);
			}
			holder.name.setText(person.getName());
			if(person.getSex().equals("男")){
				holder.avatar.setImageBitmap(phoneBookApplication.getAvatar(1));
			}else{
				holder.avatar.setImageBitmap(phoneBookApplication.getAvatar(0));
			}
			holder.arrow.setVisibility(View.GONE);
            convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(mContext, FriendInfoActivity.class);
					person.getMarriage();
					intent.putExtra("person", person);
					mContext.startActivity(intent);
				}
			});
			return convertView;
		}

		public int getPositionForSection(int section) {
			if (section < 0 || section >= mMyFriendsFirstName.size()) {
				return -1;
			}
			return mMyFriendsPosition.get(section);
		}

		public int getSectionForPosition(int position) {
			if (position < 0 || position >= mMyFriendsResults.size()) {
				return -1;
			}
			int index = Arrays.binarySearch(mMyFriendsPosition.toArray(), position);
			return index >= 0 ? index : -index - 2;
		}

		class ViewHolder {
			TextView alpha;
			ImageView alpha_line;
			ImageView avatar;
			TextView name;
			ImageView arrow;
		}

	}
	/**
	 * 设置打开侧边栏的监听器，在主activity中调用
	 * */
	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}
	
	/**
	 * 更新通讯录列表线程
	 * */
	boolean isUpdate = false;
	class UpdatePhoneBookInfoThread extends Thread{

		@Override
		public void run() {
			while(isUpdate){
				try {
					InputStream in = HttpTool.getStream(Constant.YC_ZG_PRIMARY_PRESON, null, null, HttpTool.GET);
					//写入文件
				    File file = FileUtil.write2SDFromInput(Constant.PHONE_BOOK_PATH, Constant.YC_ZG_PRIMARY_PRESON_FILE_NAME, in);
				    if(file != null){
				    	getFriendInfo();
				    	updatePhoneBookHandler.sendEmptyMessage(Constant.DOWN_LOAD_SUCCESS);
				    }
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			super.run();
		}
		
	}
	
	Handler updatePhoneBookHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.DOWN_LOAD_SUCCESS:
				//getFriendInfo();
				isUpdate = false;
				mLoading.setVisibility(View.GONE);
				mDisplay.setVisibility(View.VISIBLE);
				friendInfoAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
		
	};

}
