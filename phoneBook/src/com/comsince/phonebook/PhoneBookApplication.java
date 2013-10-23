package com.comsince.phonebook;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import com.comsince.phonebook.dbhelper.MessageDB;
import com.comsince.phonebook.dbhelper.UserDB;
import com.comsince.phonebook.entity.Group;
import com.comsince.phonebook.entity.Person;
import com.comsince.phonebook.preference.PhoneBookPreference;
import com.comsince.phonebook.util.PhotoUtil;
import com.comsince.phonebook.util.SimpleXmlReaderUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PhoneBookApplication extends Application {
    public static PhoneBookPreference phoneBookPreference;
    public static Context context;
    public static PhoneBookApplication phoneBookApplication;
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		phoneBookApplication = this;
		try {
			mAvatars = getAssets().list("avatar");
		} catch (IOException e) {
			e.printStackTrace();
		}
		phoneBookPreference = new PhoneBookPreference(context);
	}
	
	public synchronized static PhoneBookApplication getInstance() {
		return phoneBookApplication;
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
	 * 当前用户的群组信息
	 * */
	public List<Group> mGroupResult = new ArrayList<Group>();
	/**
	 * 广播分组
	 * */
	public List<String> tags = new ArrayList<String>();
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
	
	/**用户数据库**/
	private UserDB mUserDB;
	private Gson mGson;
	private MessageDB mMsgDB;
	private MediaPlayer mMediaPlayer;

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
	
	public synchronized UserDB getUserDB() {
		if (mUserDB == null)
			mUserDB = new UserDB(this);
		return mUserDB;
	}
	
	public synchronized MessageDB getMessageDB() {
		if (mMsgDB == null)
			mMsgDB = new MessageDB(this);
		return mMsgDB;
	}
	
	public synchronized Gson getGson() {
		if (mGson == null)
			// 不转换没有 @Expose 注解的字段
			mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
					.create();
		return mGson;
	}
	
	public synchronized MediaPlayer getMediaPlayer() {
		if (mMediaPlayer == null)
			mMediaPlayer = MediaPlayer.create(this, R.raw.office);
		return mMediaPlayer;
	}

}
