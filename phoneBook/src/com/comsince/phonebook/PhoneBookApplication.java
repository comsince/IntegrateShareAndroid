package com.comsince.phonebook;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
		initFaceMap();
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
	/**表情页数**/
	public static final int NUM_PAGE = 6;
	private Map<String, Integer> mFaceMap = new LinkedHashMap<String, Integer>();
	public static int NUM = 20;// 每页20个表情,还有最后一个删除button
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
	
	public Map<String, Integer> getFaceMap() {
		if (!mFaceMap.isEmpty())
			return mFaceMap;
		return null;
	}
	
	
	/**
	 * 初始化表情对应关系
	 * **/
	private void initFaceMap() {
		mFaceMap.put("[呲牙]", R.drawable.f000);
		mFaceMap.put("[调皮]", R.drawable.f001);
		mFaceMap.put("[流汗]", R.drawable.f002);
		mFaceMap.put("[偷笑]", R.drawable.f003);
		mFaceMap.put("[再见]", R.drawable.f004);
		mFaceMap.put("[敲打]", R.drawable.f005);
		mFaceMap.put("[擦汗]", R.drawable.f006);
		mFaceMap.put("[猪头]", R.drawable.f007);
		mFaceMap.put("[玫瑰]", R.drawable.f008);
		mFaceMap.put("[流泪]", R.drawable.f009);
		mFaceMap.put("[大哭]", R.drawable.f010);
		mFaceMap.put("[嘘]", R.drawable.f011);
		mFaceMap.put("[酷]", R.drawable.f012);
		mFaceMap.put("[抓狂]", R.drawable.f013);
		mFaceMap.put("[委屈]", R.drawable.f014);
		mFaceMap.put("[便便]", R.drawable.f015);
		mFaceMap.put("[炸弹]", R.drawable.f016);
		mFaceMap.put("[菜刀]", R.drawable.f017);
		mFaceMap.put("[可爱]", R.drawable.f018);
		mFaceMap.put("[色]", R.drawable.f019);
		mFaceMap.put("[害羞]", R.drawable.f020);

		mFaceMap.put("[得意]", R.drawable.f021);
		mFaceMap.put("[吐]", R.drawable.f022);
		mFaceMap.put("[微笑]", R.drawable.f023);
		mFaceMap.put("[发怒]", R.drawable.f024);
		mFaceMap.put("[尴尬]", R.drawable.f025);
		mFaceMap.put("[惊恐]", R.drawable.f026);
		mFaceMap.put("[冷汗]", R.drawable.f027);
		mFaceMap.put("[爱心]", R.drawable.f028);
		mFaceMap.put("[示爱]", R.drawable.f029);
		mFaceMap.put("[白眼]", R.drawable.f030);
		mFaceMap.put("[傲慢]", R.drawable.f031);
		mFaceMap.put("[难过]", R.drawable.f032);
		mFaceMap.put("[惊讶]", R.drawable.f033);
		mFaceMap.put("[疑问]", R.drawable.f034);
		mFaceMap.put("[睡]", R.drawable.f035);
		mFaceMap.put("[亲亲]", R.drawable.f036);
		mFaceMap.put("[憨笑]", R.drawable.f037);
		mFaceMap.put("[爱情]", R.drawable.f038);
		mFaceMap.put("[衰]", R.drawable.f039);
		mFaceMap.put("[撇嘴]", R.drawable.f040);
		mFaceMap.put("[阴险]", R.drawable.f041);

		mFaceMap.put("[奋斗]", R.drawable.f042);
		mFaceMap.put("[发呆]", R.drawable.f043);
		mFaceMap.put("[右哼哼]", R.drawable.f044);
		mFaceMap.put("[拥抱]", R.drawable.f045);
		mFaceMap.put("[坏笑]", R.drawable.f046);
		mFaceMap.put("[飞吻]", R.drawable.f047);
		mFaceMap.put("[鄙视]", R.drawable.f048);
		mFaceMap.put("[晕]", R.drawable.f049);
		mFaceMap.put("[大兵]", R.drawable.f050);
		mFaceMap.put("[可怜]", R.drawable.f051);
		mFaceMap.put("[强]", R.drawable.f052);
		mFaceMap.put("[弱]", R.drawable.f053);
		mFaceMap.put("[握手]", R.drawable.f054);
		mFaceMap.put("[胜利]", R.drawable.f055);
		mFaceMap.put("[抱拳]", R.drawable.f056);
		mFaceMap.put("[凋谢]", R.drawable.f057);
		mFaceMap.put("[饭]", R.drawable.f058);
		mFaceMap.put("[蛋糕]", R.drawable.f059);
		mFaceMap.put("[西瓜]", R.drawable.f060);
		mFaceMap.put("[啤酒]", R.drawable.f061);
		mFaceMap.put("[飘虫]", R.drawable.f062);

		mFaceMap.put("[勾引]", R.drawable.f063);
		mFaceMap.put("[OK]", R.drawable.f064);
		mFaceMap.put("[爱你]", R.drawable.f065);
		mFaceMap.put("[咖啡]", R.drawable.f066);
		mFaceMap.put("[钱]", R.drawable.f067);
		mFaceMap.put("[月亮]", R.drawable.f068);
		mFaceMap.put("[美女]", R.drawable.f069);
		mFaceMap.put("[刀]", R.drawable.f070);
		mFaceMap.put("[发抖]", R.drawable.f071);
		mFaceMap.put("[差劲]", R.drawable.f072);
		mFaceMap.put("[拳头]", R.drawable.f073);
		mFaceMap.put("[心碎]", R.drawable.f074);
		mFaceMap.put("[太阳]", R.drawable.f075);
		mFaceMap.put("[礼物]", R.drawable.f076);
		mFaceMap.put("[足球]", R.drawable.f077);
		mFaceMap.put("[骷髅]", R.drawable.f078);
		mFaceMap.put("[挥手]", R.drawable.f079);
		mFaceMap.put("[闪电]", R.drawable.f080);
		mFaceMap.put("[饥饿]", R.drawable.f081);
		mFaceMap.put("[困]", R.drawable.f082);
		mFaceMap.put("[咒骂]", R.drawable.f083);

		mFaceMap.put("[折磨]", R.drawable.f084);
		mFaceMap.put("[抠鼻]", R.drawable.f085);
		mFaceMap.put("[鼓掌]", R.drawable.f086);
		mFaceMap.put("[糗大了]", R.drawable.f087);
		mFaceMap.put("[左哼哼]", R.drawable.f088);
		mFaceMap.put("[哈欠]", R.drawable.f089);
		mFaceMap.put("[快哭了]", R.drawable.f090);
		mFaceMap.put("[吓]", R.drawable.f091);
		mFaceMap.put("[篮球]", R.drawable.f092);
		mFaceMap.put("[乒乓球]", R.drawable.f093);
		mFaceMap.put("[NO]", R.drawable.f094);
		mFaceMap.put("[跳跳]", R.drawable.f095);
		mFaceMap.put("[怄火]", R.drawable.f096);
		mFaceMap.put("[转圈]", R.drawable.f097);
		mFaceMap.put("[磕头]", R.drawable.f098);
		mFaceMap.put("[回头]", R.drawable.f099);
		mFaceMap.put("[跳绳]", R.drawable.f100);
		mFaceMap.put("[激动]", R.drawable.f101);
		mFaceMap.put("[街舞]", R.drawable.f102);
		mFaceMap.put("[献吻]", R.drawable.f103);
		mFaceMap.put("[左太极]", R.drawable.f104);

		mFaceMap.put("[右太极]", R.drawable.f105);
		mFaceMap.put("[闭嘴]", R.drawable.f106);
	}

}
