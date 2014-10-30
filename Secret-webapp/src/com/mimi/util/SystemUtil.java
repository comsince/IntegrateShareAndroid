package com.mimi.util;

import java.io.IOException;
import java.util.Random;


public class SystemUtil {
	 
		public static int  getVerifyCode() throws IOException
		{
			Random random = new Random();
			int code = Integer.MAX_VALUE - random.nextInt(Integer.MAX_VALUE);
			return code;
		}
		
		public static String encryptVerifyCode(int code )
		{
			code = 0x7fffffff-~code>>>4^0xff<<3;
			return MD5.getMD5Encoding(String.valueOf((code))).toUpperCase();
		}
		
		public static boolean isEmpty(Object obj)
		{
			if(null == obj)
				return true;
			if("".equals(obj.toString().trim()))
			{
				return true;
			}
			return false;
		}
	 
		public static String getSequenceId()
		{
			String mark = String.valueOf(System.currentTimeMillis());
			return mark;
		}
		
		public static String codeStyle(String content)
		{
			if(!isEmpty(content))
			{
				content = content.replaceAll("\\[java\\]", "<script type='syntaxhighlighter' class='brush:java'>");
				content= content.replaceAll("\\[/java\\]","</script>");
			}
			return content;
		}
}
