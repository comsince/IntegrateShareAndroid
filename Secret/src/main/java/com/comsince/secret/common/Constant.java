package com.comsince.secret.common;

import android.os.Environment;

  


/**
 * 
 * @author track
 * @version 1.0
 */
public interface Constant { 
		public static final String CODE_SUCCESS = "0";
		
		public static final String ORDER_NEW = "timestamp";
		public static final String ORDER_RECOUNT = "recount";
		
		public static final String ORDER_PIC = "pic";
		
		public static final String FILE_TYPE_PIC = "1";
		
		public static final String WEIBO_TYPE_2 ="2";
		public static final int VALID_FAILED =408;
		public static final int ID_NO_EXIST =0;
		public static final int SUCCESS_FULL = 1;
		public static final int STATE_RECEIVED = 3;
		public static final int STATE_ON_PASSAGE = 0;
		public static final String FORMAT_TYPE = "UTF-8";
		public static final  String APP_NAME="MIMI";
		public static final  StringBuffer draft= new StringBuffer();
		//public static final String SERVER_URL ="http://mimiapi.sinaapp.com";
		public static final String SERVER_URL ="http://172.16.9.242:8080/Secret-webapp";
		//public static final String SERVER_URL ="http://124.237.121.75:8989/mimiapi";
		//public static final String SERVER_URL ="http://comsincephonebook.sinaapp.com";
		public static final  String IMAGE_DIR=Environment.getExternalStorageDirectory().getPath()+"/mimiFielCache";
		
		public static class Config {
			public static final boolean DEVELOPER_MODE = false;
		}
		
}
