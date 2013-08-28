package com.comsince.phonebook.adapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.GroupPerson;
import com.comsince.phonebook.entity.Person;
import com.comsince.phonebook.entity.Persons;
import com.comsince.phonebook.util.AndroidUtil;
import com.comsince.phonebook.util.DataUtil;
import com.comsince.phonebook.util.FileUtil;
import com.comsince.phonebook.util.SimpleXmlReaderUtil;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GroupByLetterAdapter extends BaseAdapter {

	// 当前显示的好友数据
	private List<GroupPerson> mMyFriendsResults = new ArrayList<GroupPerson>();
	// 当前显示的好友的姓名的首字母的在列表中的位置
	private List<Integer> mMyFriendsPosition = new ArrayList<Integer>();
	// 当前显示的好友的姓名的首字母数据
	private List<String> mMyFriendsFirstName = new ArrayList<String>();
	/**
	 * 当前用户的好友的姓名首字母在列表中的位置
	 */
	public Map<String, Integer> mMyFriendsFirstNamePosition = new HashMap<String, Integer>();
	/**
	 * 当前用户的好友根据姓名首字母分组
	 */
	public Map<String, List<GroupPerson>> mMyFriendsGroupByFirstName = new HashMap<String, List<GroupPerson>>();

	@Override
	public int getCount() {
		return 0;
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
		return null;
	}

	/**
	 * 初始化好友信息
	 * 
	 * */
	public void getFriendInfo(List<GroupPerson> groupPersons) {
		try {
			for (GroupPerson groupPerson : groupPersons) {
				String pinyinName = DataUtil.getStringPinYin(groupPerson.getPersonRealName());
				String pinyinFirstUpperCase = pinyinName.substring(0, 1).toUpperCase();

				mMyFriendsResults.add(groupPerson);

				if (pinyinFirstUpperCase.matches("^[a-z,A-Z].*$")) {
					if (mMyFriendsFirstName.contains(pinyinFirstUpperCase)) {
						mMyFriendsGroupByFirstName.get(pinyinFirstUpperCase).add(groupPerson);
					} else {
						mMyFriendsFirstName.add(pinyinFirstUpperCase);
						List<GroupPerson> list = new ArrayList<GroupPerson>();
						list.add(groupPerson);
						mMyFriendsGroupByFirstName.put(pinyinFirstUpperCase, list);
					}
				} else {
					if (mMyFriendsFirstName.contains("#")) {
						mMyFriendsGroupByFirstName.get("#").add(groupPerson);
					} else {
						mMyFriendsFirstName.add("#");
						List<GroupPerson> list = new ArrayList<GroupPerson>();
						list.add(groupPerson);
						mMyFriendsGroupByFirstName.put("#", list);
					}
				}
			}
			Collections.sort(mMyFriendsFirstName);
			int position = 0;
			for (int i = 0; i < mMyFriendsFirstName.size(); i++) {
				mMyFriendsFirstNamePosition.put(mMyFriendsFirstName.get(i), position);
				mMyFriendsPosition.add(position);
				position += mMyFriendsGroupByFirstName.get(mMyFriendsFirstName.get(i)).size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
