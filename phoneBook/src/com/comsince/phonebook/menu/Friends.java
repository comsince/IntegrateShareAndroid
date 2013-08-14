package com.comsince.phonebook.menu;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.entity.Person;
import com.comsince.phonebook.entity.Persons;
import com.comsince.phonebook.ui.base.MyLetterListView;
import com.comsince.phonebook.ui.base.MyLetterListView.OnTouchingLetterChangedListener;
import com.comsince.phonebook.util.DataUtil;
import com.comsince.phonebook.util.SimpleXmlReaderUtil;

public class Friends {
	private Context mContext;
	private PhoneBookApplication phoneBookApplication;
	private View mFriends;
	private ListView mDisplay;
	private EditText mSearch;
	/**
	 * 字符索引表
	 * */
	private MyLetterListView mLetter;
	private FriendInfoAdapter friendInfoAdapter;
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
		if (phoneBookApplication.mMyFriendsResults.isEmpty()) {
			SimpleXmlReaderUtil simpleXmlReader = phoneBookApplication.simpleXmlReader;
			try {
				InputStream friendInfoIn = mContext.getAssets().open("personinfo/person.xml");
				mMyFriendsResults = simpleXmlReader.readXmlFromInputStream(friendInfoIn, Persons.class).getPersons();
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

}
