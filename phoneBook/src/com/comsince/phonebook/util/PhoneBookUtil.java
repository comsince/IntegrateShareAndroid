package com.comsince.phonebook.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.Group;
import com.comsince.phonebook.entity.Groups;

public class PhoneBookUtil {
	public static SimpleXmlReaderUtil xmlUtil = new SimpleXmlReaderUtil();
	/**
	 * 获取个人信息目录
	 * */
    public static String getPersonInfoPath(){
    	return AndroidUtil.getSDCardRoot() + Constant.MAIN_DIR_PHONE_BOOK + File.separator + Constant.DIR_PERSON_INFO + File.separator;
    }
    
    public static List<Group> getCurrentUserGroup(Context context){
    	List<Group> groups = new ArrayList<Group>();
    	String userName = PhoneBookApplication.phoneBookPreference.getPassWord(context);
		String userPassword = PhoneBookApplication.phoneBookPreference.getPassWord(context);
		String fileName = userName+"_"+userPassword+"_"+Constant.FILE_GROUP_SUFFIX+".xml";
		String filePath = AndroidUtil.getSDCardRoot()+Constant.PHONE_BOOK_PATH+File.separator+Constant.DIR_PERSON_INFO+File.separator+fileName;
		SimpleXmlReaderUtil xmlUtil = new SimpleXmlReaderUtil();
		try {
			groups = xmlUtil.readXml(filePath, Groups.class).getGroups();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return groups;
    }
    
    public static String getGroupInfoFullPath(Context context){
    	List<Group> groups = new ArrayList<Group>();
    	String userName = PhoneBookApplication.phoneBookPreference.getUserName(context);
		String userPassword = PhoneBookApplication.phoneBookPreference.getPassWord(context);
		String fileName = userName+"_"+userPassword+"_"+Constant.FILE_GROUP_SUFFIX+".xml";
		String filePath = AndroidUtil.getSDCardRoot()+Constant.PHONE_BOOK_PATH+File.separator+Constant.DIR_PERSON_INFO+File.separator+fileName;
		return filePath;
    }
    /**
     * 获取个人信息的文件名
     * @return fileName 不带后缀名
     * **/
    public static String getPerosnGroupInfoFileName(Context context){
    	String userName = PhoneBookApplication.phoneBookPreference.getUserName(context);
		String userPassword = PhoneBookApplication.phoneBookPreference.getPassWord(context);
		String fileName = userName+"_"+userPassword+"_"+Constant.FILE_GROUP_SUFFIX;
    	return fileName;
    }
    
    public static void writePersonGroupInfo(Groups groups ,String fileName){
    	
    	xmlUtil.writeXml(groups, Constant.PHONE_BOOK_PATH+File.separator+Constant.DIR_PERSON_INFO, fileName);
    }
}
