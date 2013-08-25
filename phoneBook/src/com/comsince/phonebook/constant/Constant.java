package com.comsince.phonebook.constant;

public class Constant {
	//存储路径
	public static final String PHONE_BOOK_PATH = "phonebook";
	public static final String YC_ZG_PRIMARY_PRESON = "http://bcs.duapp.com/comsince/yc_zg_primary_person.xml";
    public static final String YC_ZG_PRIMARY_PRESON_FILE_NAME = "yc_zg_primary_person.xml";
    
    public static final int DOWN_LOAD_SUCCESS = 10000;
    public static final int DOWN_LOAD_FAIL = 10001;
    
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
  	
  	//loading activity 广播
  	public static final String ACTION_FINISH = "com.comsince.phonebook.loadingfinish";
  	
  	//general task type
  	public static final int TASK_UPLOAD = 30001;
  	public static final int TASK_DOWNLOAD = 30002;
  	
  	//http msg
  	public static final String SUCCESS_MSG = "OK";
    
}
