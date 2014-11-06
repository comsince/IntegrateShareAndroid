package com.mimi.util;

public class TextUtils {
	
	public static boolean isEmpty(String str) {
		if (str != null) {
			int len = str.length();
			for (int x = 0; x < len; x++)
				if (str.charAt(x) > ' ')
					return false;

		}
		return true;
	}
}
