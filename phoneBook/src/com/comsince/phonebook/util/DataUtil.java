package com.comsince.phonebook.util;

import java.util.UUID;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class DataUtil {
	/**
	 * generate uuid.
	 * 
	 * @return
	 */
	public static String generateId() {
		String id = UUID.randomUUID().toString().toLowerCase();
		id = id.replace("-", "");
		return id.toString();
	}
	/**
	 * 判断字符是否null或是""
	 * **/
	public static boolean isEmpty(String str) {
		if (str != null) {
			int len = str.length();
			for (int x = 0; x < len; x++)
				if (str.charAt(x) > ' ')
					return false;

		}
		return true;
	}
	
	/**
	 * 获取字符的拼音
	 * 
	 * @param str
	 * @return
	 */
	public static String getStringPinYin(String str) {
		StringBuilder sb = new StringBuilder();
		String tempPinyin = null;
		for (int i = 0; i < str.length(); ++i) {
			tempPinyin = getCharacterPinYin(str.charAt(i));
			if (tempPinyin == null) {
				// 如果str.charAt(i)非汉字，则保持原样
				sb.append(str.charAt(i));
			} else {
				sb.append(tempPinyin);
			}
		}
		return sb.toString();
	}
	

	public static String getCharacterPinYin(char c) {
		HanyuPinyinOutputFormat format = null;
		format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		String[] pinyin = null;
		try {
			pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		// 如果c不是汉字，toHanyuPinyinStringArray会返回null
		if (pinyin == null)
			return null;
		// 只取一个发音，如果是多音字，仅取第一个发音
		return pinyin[0];
	}
}
