package com.comsince.phonebook.adapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comsince.phonebook.R;
import com.comsince.phonebook.activity.FriendInfoActivity;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.GroupPerson;
import com.comsince.phonebook.entity.Person;
import com.comsince.phonebook.entity.Persons;
import com.comsince.phonebook.util.AndroidUtil;
import com.comsince.phonebook.util.DataUtil;
import com.comsince.phonebook.util.FileUtil;
import com.comsince.phonebook.util.SimpleXmlReaderUtil;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupByLetterAdapter extends BaseAdapter {
    private Context context;
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
	

	public GroupByLetterAdapter(Context context, List<GroupPerson> mMyFriendsResults) {
		this.context = context;
		this.mMyFriendsResults = mMyFriendsResults;
		//getFriendInfo(mMyFriendsResults);
	}
	
	public void refreshData(List<GroupPerson> groupPersonList){
		if(groupPersonList.size() > 0){
			mMyFriendsResults.clear();
			mMyFriendsResults = groupPersonList;
			getFriendInfo(mMyFriendsResults);
			this.notifyDataSetChanged();
		}
	}
    /**
     * 不要偷懒，少步骤，基本的初始变量一定不要忘记了
     * */
	@Override
	public int getCount() {
		return mMyFriendsResults.size();
	}

	@Override
	public Object getItem(int position) {
		return mMyFriendsResults.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.friend_item, null);
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
		final GroupPerson groupPerson = mMyFriendsGroupByFirstName.get(mMyFriendsFirstName.get(section)).get(position - getPositionForSection(section));
		if (getPositionForSection(section) == position) {
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha_line.setVisibility(View.VISIBLE);
			holder.alpha.setText(mMyFriendsFirstName.get(section));
		} else {
			holder.alpha.setVisibility(View.GONE);
			holder.alpha_line.setVisibility(View.GONE);
		}
		holder.name.setText(groupPerson.getPersonRealName());
		holder.avatar.setBackgroundResource(R.drawable.phonebook);
		holder.arrow.setVisibility(View.GONE);
        convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, FriendInfoActivity.class);
				intent.putExtra("tagerPersonInfo", groupPerson.getDetialInfoPath());
				String personName = groupPerson.getPersonRealName();
				if(!TextUtils.isEmpty(personName)){
					intent.putExtra("personName", personName);
				}
				context.startActivity(intent);
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
	

	/**
	 * 初始化好友信息
	 * 
	 * */
	public void getFriendInfo(List<GroupPerson> groupPersons) {
		try {
			mMyFriendsFirstName.clear();
			mMyFriendsPosition.clear();
			mMyFriendsGroupByFirstName.clear();
			mMyFriendsFirstNamePosition.clear();
			for (GroupPerson groupPerson : groupPersons) {
				String pinyinName = DataUtil.getStringPinYin(groupPerson.getPersonRealName());
				String pinyinFirstUpperCase = pinyinName.substring(0, 1).toUpperCase();

				//mMyFriendsResults.add(groupPerson);

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
