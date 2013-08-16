package com.comsince.phonebook.util;

import java.util.ArrayList;
import java.util.List;

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
}
