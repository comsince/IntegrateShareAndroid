package com.comsince.phonebook;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.comsince.phonebook.entity.Person;
import com.comsince.phonebook.util.PhotoUtil;
import com.comsince.phonebook.util.SimpleXmlReaderUtil;

public class PhoneBookApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			mAvatars = getAssets().list("avatar");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 当前用户的好友数据
	 */
	public List<Person> mMyFriendsResults = new ArrayList<Person>();
	/**
	 * 当前用户的好友根据姓名首字母分组
	 */
	public Map<String, List<Person>> mMyFriendsGroupByFirstName = new HashMap<String, List<Person>>();
	/**
	 * 当前用户的好友的姓名首字母在列表中的位置
	 */
	public Map<String, Integer> mMyFriendsFirstNamePosition = new HashMap<String, Integer>();
	/**
	 * 当前用户的好友的姓名的首字母数据
	 */
	public List<String> mMyFriendsFirstName = new ArrayList<String>();
	/**
	 * 当前用户的好友的姓名的首字母的在列表中的位置
	 */
	public List<Integer> mMyFriendsPosition = new ArrayList<Integer>();
	/**
	 * 头像名称
	 */
	public String[] mAvatars;
	/**
	 * 读取xml的工具
	 * */
	public SimpleXmlReaderUtil simpleXmlReader = new SimpleXmlReaderUtil();

	/**
	 * 圆形头像缓存
	 */
	public HashMap<String, SoftReference<Bitmap>> mAvatarCache = new HashMap<String, SoftReference<Bitmap>>();

	/**
	 * 默认头像
	 */
	public Bitmap mDefault_Avatar;

	/**
	 * 根据编号获取用户圆形头像
	 */
	public Bitmap getAvatar(int position) {
		try {
			String avatarName = mAvatars[position];
			Bitmap bitmap = null;
			if (mAvatarCache.containsKey(avatarName)) {
				SoftReference<Bitmap> reference = mAvatarCache.get(avatarName);
				bitmap = reference.get();
				if (bitmap != null) {
					return bitmap;
				}
			}
			bitmap = PhotoUtil.toRoundCorner(BitmapFactory.decodeStream(getAssets().open("avatar/" + avatarName)), 15);
			mAvatarCache.put(avatarName, new SoftReference<Bitmap>(bitmap));
			return bitmap;
		} catch (Exception e) {
			return mDefault_Avatar;
		}
	}

}
