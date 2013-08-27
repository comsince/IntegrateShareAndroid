package com.comsince.phonebook.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.Group;
import com.comsince.phonebook.entity.Groups;

public class BaiduPushUtil {
	public static List<String> getTagsList(String originalText) {

		List<String> tags = new ArrayList<String>();
		int indexOfComma = originalText.indexOf(',');
		String tag;
		while (indexOfComma != -1) {
			tag = originalText.substring(0, indexOfComma);
			tags.add(tag);

			originalText = originalText.substring(indexOfComma + 1);
			indexOfComma = originalText.indexOf(',');
		}

		tags.add(originalText);
		return tags;
	}
	public static List<String> getTagFromFile(Context context){
		List<String> tags = new ArrayList<String>();
		String userName = PhoneBookApplication.phoneBookPreference.getPassWord(context);
		String userPassword = PhoneBookApplication.phoneBookPreference.getPassWord(context);
		String fileName = userName+"_"+userPassword+"_"+Constant.FILE_GROUP_SUFFIX+".xml";
		String filePath = AndroidUtil.getSDCardRoot()+Constant.PHONE_BOOK_PATH+File.separator+Constant.DIR_PERSON_INFO+File.separator+fileName;
		SimpleXmlReaderUtil xmlUtil = new SimpleXmlReaderUtil();
		try {
			List<Group> groups = xmlUtil.readXml(filePath, Groups.class).getGroups();
			if(groups != null){
				for(Group group : groups){
					String tag = group.getGroupTag();
					tags.add(tag);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tags;
	}
}
