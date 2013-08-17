package com.comsince.knowledge.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.auth.BCSSignCondition;
import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.request.GenerateUrlRequest;

public class BaiduCloudSaveUtil {
	
	private static final String PUT = "PUT";
	private static final int HTTP_CONNECT_TIMEOUT = 100000;
	private static final int HTTP_READ_TIMEOUT = 100000;
	
	static String accessKey = "9ab8af408f719b4961bbd16f9bac9f16";
	static String secretKey = "99fc6b05f8c44160e6bc7e1efe097e9a";
	static String host = "bcs.duapp.com";
	
	/**
	 * 向百度云中存储数据
	 * @param path 上传文件的路径
	 * @param url 上传的文件的网络地址
	 * */
	public static void putObject(String url,String path){
		URL  uRLObj;
		HttpURLConnection connection = null;
		try {
			uRLObj = new URL(url);
			connection = (HttpURLConnection) uRLObj.openConnection();
			//设置请求方法
			connection.setRequestMethod(PUT);
			//设置文件类型
			connection.setRequestProperty("Content-Type", "text/xml");
			//设置文件长度
			connection.setRequestProperty("Content-Length", ""+getFileLength(path));
			connection.setRequestProperty("charset", "utf-8");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			//设置超时时间
			connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
			connection.setReadTimeout(HTTP_READ_TIMEOUT);
			
			//发送请求
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			InputStream in = new FileInputStream(path);
			byte buffer[] = new byte[4 * 1024];
			int temp;
			while ((temp = in.read(buffer)) != -1) {
				wr.write(buffer, 0, temp);
			}
			wr.flush();
			wr.close();
			//获取相应
			InputStream responseIn = null;
			connection.connect();
			String w = connection.getResponseMessage();
			//connection.getInputStream(); 
			Log.d("download", "sdfsd");
			int s = connection.getContentLength();
			Log.i("download",String.valueOf(s));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(connection != null){
			connection.disconnect();
		}
	}
	
	public static Long getFileLength(String path){
		Long fileLength = null;
		try {
			FileInputStream in = new FileInputStream(path);
			File file = new File(path);
			fileLength =  file.length();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileLength;
	}
	/**
	 * @param  object BCS对象必须以斜线开头
	 * */
	public static String  generateUrl(String bucket, String object) {
		BCSCredentials credentials = new BCSCredentials(accessKey, secretKey);
		BaiduBCS baiduBCS = new BaiduBCS(credentials, host);
	    baiduBCS.setDefaultEncoding("UTF-8"); 
		GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest(HttpMethodName.PUT, bucket, object);
		generateUrlRequest.setBcsSignCondition(new BCSSignCondition());
		return baiduBCS.generateUrl(generateUrlRequest); 
	}

}
