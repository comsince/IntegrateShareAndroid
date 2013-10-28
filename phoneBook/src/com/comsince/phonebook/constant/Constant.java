package com.comsince.phonebook.constant;

public class Constant {
	//存储路径
	public static final String PHONE_BOOK_PATH = "phonebook";
	public static final String YC_ZG_PRIMARY_PRESON = "http://bcs.duapp.com/comsince/yc_zg_primary_person.xml";
    public static final String YC_ZG_PRIMARY_PRESON_FILE_NAME = "yc_zg_primary_person.xml";
    
    public static final int DOWN_LOAD_SUCCESS = 10000;
    public static final int DOWN_LOAD_FAIL = 10001;
    
    public static final int DOWN_LOAD_GROUPPERSON_SUCCESS =10002;
    public static final int DOWN_LOAD_GROUPPERSON_FAIL =10004;
    public static final int UPLAOD_GROUPPERSON_SUCCESS = 10003;
  //百度开放平台通讯录key
  	public static final String BAIDU_APP_KEY = "RWg6F0aIW2tIxjonhScWE8j8";
  	
  	//点击通知按钮是否需要更新当前数据
  	public static String IS_UPDATE_DATA_FROM_NOTIFICATION = "N";
  	
  	//群组标签
  	public static final String TITLE_GROUP ="YC_ZG_PRIMARY";
  	
  	//存放用户信息的主目录
  	public static final String MAIN_DIR_PHONE_BOOK = "phonebook";
  	public static final String DIR_PERSON_INFO = "personInfo";
  	public static final String DIR_LOGIN_INFO = "loginInfo";
  	public static final String FILE_GROUP_SUFFIX = "groupInfo";
  	public static final String FILE_GROUP_INFO = "groupDetail.xml";
  	public static final String FILE_GROUP_PERSON_INFO = "groupPerson";
  	public static final String FILE_GROUP_PERSON_XML = "groupPerson.xml";
  	//存放用户头像目录
  	public static final String DIR_PERSON_AVATAR = "personavatar";
  	//存放群组头像的目录
  	public static final String DIR_GROUP_AVATAR = "groupavatar";
  	//存放用户头像信息
  	public static final String DIR_PERSON_AVATER = "userAvater";
  	//loading activity 广播
  	public static final String ACTION_FINISH = "com.comsince.phonebook.loadingfinish";
  	public static final String ACTION_ADD_TAG = "com.comsince.phonebook.addtag";
  	
  	//general task type
  	public static final int TASK_UPLOAD = 30001;
  	public static final int TASK_UPLOAD_PRESON_AVATAR = 300010;
  	public static final int TASK_UPLOAD_GROUP_AVATAR = 300011;
  	public static final int TASK_DOWNLOAD = 30002;
  	public static final int TASK_DOWNLOAD_PERSON_GROUP_INFO = 30003;
  	public static final int TASK_SEND_NOTIFICATION_TO_GROUP = 30004;
  	public static final int TASK_GET_GROUP_BY_TAG = 30005;
  	public static final int TASK_UPLOAD_PERSON_GROUPINFO = 30006;
  	public static final int TASK_UPLOAD_PERSON_GROUPPERSON = 30007;
    public static final int TASK_DOWNLOAD_PERSON_GROUPPERSON = 30008;  	
    public static final int TASK_DOWNLOAD_TARTGET_PERONINFO = 30009;
  	//http msg
  	public static final String SUCCESS_MSG = "OK";
    
  	
  	//新朋友加入
  	public static final int NEW_MESSAGE = 0x000;// 有新消息
	public static final int NEW_FRIEND = 0x001;// 有好友加入
}
