package com.comsince.phonebook.util;

import java.io.File;

import com.comsince.phonebook.constant.Constant;

public class PhoneBookUtil {
	
	/**
	 * 获取个人信息目录
	 * */
    public static String getPersonInfoPath(){
    	return AndroidUtil.getSDCardRoot() + Constant.MAIN_DIR_PHONE_BOOK + File.separator + Constant.DIR_PERSON_INFO + File.separator;
    }
}
