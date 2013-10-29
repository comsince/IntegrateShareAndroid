package com.comsince.phonebook.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.Group;
import com.comsince.phonebook.entity.GroupPerson;
import com.comsince.phonebook.entity.GroupPersons;
import com.comsince.phonebook.entity.Groups;
import com.comsince.phonebook.entity.Person;

public class PhoneBookUtil {
	public static SimpleXmlReaderUtil xmlUtil = new SimpleXmlReaderUtil();
	/**
	 * 获取个人信息目录
	 * */
    public static String getPersonInfoPath(){
    	return AndroidUtil.getSDCardRoot() + Constant.MAIN_DIR_PHONE_BOOK + File.separator + Constant.DIR_PERSON_INFO + File.separator;
    }
    /**
     * 获取当前用户的分组信息
     * */
    public static List<Group> getCurrentUserGroup(Context context){
    	List<Group> groups = new ArrayList<Group>();
    	String userName = PhoneBookApplication.phoneBookPreference.getUserName(context);
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
    
    /**
     * 获取当前加入的分组的人员信息
     * */
	public static List<GroupPerson> getCurrentJoinGroupPersonInfo(Context context, String groupTag) {
		List<GroupPerson> groupPersons = new ArrayList<GroupPerson>();
		//多写了一个根路径
		String filePath = AndroidUtil.getSDCardRoot() + Constant.PHONE_BOOK_PATH + File.separator + groupTag + File.separator + Constant.FILE_GROUP_PERSON_XML;
		try {
			if(new File(filePath).exists()){
				groupPersons = xmlUtil.readXml(filePath, GroupPersons.class).getGroupPersons();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupPersons;
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
     * 获取个人分组信息的文件名
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
    
    /**
     * 向要加入的群组中登记个人信息
     * **/
     public static void writeGroupPersonToTargetGroup(String tag,GroupPersons groupPersons){
    	 xmlUtil.writeXml(groupPersons, Constant.PHONE_BOOK_PATH+File.separator+tag, Constant.FILE_GROUP_PERSON_INFO);
     }
    /**
     * 获取当前登录用户的个人信息的文件的路径
     * */
    public static String getCurrentPersonInfoFilePath(Context context){
    	String userName = PhoneBookApplication.phoneBookPreference.getUserName(context);
		String userPassword = PhoneBookApplication.phoneBookPreference.getPassWord(context);
		String fileName = userName+"_"+userPassword+".xml";
		return AndroidUtil.getSDCardRoot()+Constant.PHONE_BOOK_PATH+File.separator+Constant.DIR_PERSON_INFO+File.separator+fileName;
    }
    
    /**
     * 获取当前登录用户的个人信息的文件全名
     * 包括后缀名
     * */
    public static String getCurrentPersonFullFileName(Context context){
    	String userName = PhoneBookApplication.phoneBookPreference.getUserName(context);
		String userPassword = PhoneBookApplication.phoneBookPreference.getPassWord(context);
		String fileName = userName+"_"+userPassword+".xml";
		return fileName;
    }
    
    /**
     * 获取当前用户的detialInfoPath
     * */
    public static String getCurrentDetialInfoPath(Context context){
    	return File.separator+Constant.DIR_PERSON_INFO+File.separator+getCurrentPersonFullFileName(context);
    }
    
    /**
     * 获取当前登录用户的个人信息
     * */
    public static Person getCurrrentPersonInfo(Context context){
    	Person person = new Person();
		String filePath = getCurrentPersonInfoFilePath(context);
		if(new File(filePath).exists()){
			try {
				 person = xmlUtil.readXml(filePath, Person.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return person;
    }
    
    /**
     * 根据路劲获取其基本信息
     * @param relativePath 相对路径phonebook/
     * */
    public static Person getPersonInfoByPath(String relativePath){
    	Person person = new Person();
    	String filePath = AndroidUtil.getSDCardRoot() + Constant.PHONE_BOOK_PATH + relativePath;
    	if(new File(filePath).exists()){
    		try {
				person = xmlUtil.readXml(filePath, Person.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return person;
    }
    
    /**
     * 获取当前用户图片存储地址
     * **/
    public static String getCurrentUserAvatarFilePath(Context context){
    	String userName = PhoneBookApplication.phoneBookPreference.getUserName(context);
		String userPassword = PhoneBookApplication.phoneBookPreference.getPassWord(context);
    	String root = AndroidUtil.getSDCardRoot() + Constant.PHONE_BOOK_PATH +File.separator + Constant.DIR_PERSON_AVATAR +File.separator 
    			          + userName + "_" + userPassword + ".jpg";
    	return root;
    }
    
    /**
     * 获取头像的网络加密访问地址
     * **/
    public static String getCurrentUserAvatarWebUrl(Context context){
    	String passWord = PhoneBookApplication.phoneBookPreference.getPassWord(context);
		String fileName = PhoneBookApplication.phoneBookPreference.getUserName(context)+"_"+passWord;
		return BaiduCloudSaveUtil.generateUrlForGet(Constant.PHONE_BOOK_PATH, "/"+Constant.DIR_PERSON_AVATAR+"/"+fileName+".jpg");
    }
    
    /**
     * 根据文件名获取图片文件的访问链接
     * @param fileName 要访问的文件名，仅限jpg格式
     * **/
    public static String getJpgFileWebUrlByFileName(String fileName){
    	return BaiduCloudSaveUtil.generateUrlForGet(Constant.PHONE_BOOK_PATH, "/"+Constant.DIR_PERSON_AVATAR+"/"+fileName+".jpg");
    }
    
    /**
     * 获取群组的头像url
     * **/
    public static String getGroupAvatarWebUrl(String fileName){
    	return BaiduCloudSaveUtil.generateUrlForGet(Constant.PHONE_BOOK_PATH, "/"+Constant.DIR_GROUP_AVATAR+"/"+fileName+".jpg");
    }
    
    /**
     * 获取当前用户头像文件名，不包含后缀名
     * **/
    public static String getCurrentUserAvatarName(Context context){
    	String passWord = PhoneBookApplication.phoneBookPreference.getPassWord(context);
		String fileName = PhoneBookApplication.phoneBookPreference.getUserName(context)+"_"+passWord;
		return fileName;
    }
    
}
