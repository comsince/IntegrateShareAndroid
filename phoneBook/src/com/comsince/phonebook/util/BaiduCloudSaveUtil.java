package com.comsince.phonebook.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.util.Log;

import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.auth.BCSSignCondition;
import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.request.GenerateUrlRequest;
import com.comsince.phonebook.constant.Constant;

public class BaiduCloudSaveUtil {
	
	private static final String PUT = "PUT";
	private static final String GET = "GET";
	private static final int HTTP_CONNECT_TIMEOUT = 100000;
	private static final int HTTP_READ_TIMEOUT = 100000;
	
	
	static String host = "bcs.duapp.com";
	
	/**
	 * 向百度云中存储数据
	 * @param path 上传文件的路径
	 * @param url 上传的文件的网络地址
	 * */
	public static String putObject(String url,String path){
		String respondMsg = null;
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
		    respondMsg = connection.getResponseMessage();
			//connection.getInputStream(); 
			Log.d("upload", "responeMsg");
			int s = connection.getContentLength();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(connection != null){
			connection.disconnect();
		}
		return respondMsg;
	}
	
	/**
	 * 从云上获取数据,注意不要关闭connection,如果关闭的就无法读取数据
	 * */
	public static InputStream getObject(String url){
		InputStream in = null;
		String responseMsg = null;
		URL  uRLObj;
		HttpURLConnection connection = null;
		try {
			uRLObj = new URL(url);
			connection = (HttpURLConnection) uRLObj.openConnection();
			//设置请求方法
			connection.setRequestMethod(GET);
			//设置文件类型
			connection.setRequestProperty("Content-Type", "text/xml");
			connection.setRequestProperty("charset", "utf-8");
			connection.setDoInput(true);
			//设置超时时间
			connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
			connection.setReadTimeout(HTTP_READ_TIMEOUT);
			
			responseMsg = connection.getResponseMessage();
			if(responseMsg.equals(Constant.SUCCESS_MSG)){
				in = connection.getInputStream();
			}else{
				in = connection.getInputStream();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
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
	/**
	 * 不同的請求方式，其url生成方式也不一樣，一定要設置請求方式，不能混淆
	 * */
	public static String  generateUrlForGet(String bucket, String object) {
		BCSCredentials credentials = new BCSCredentials(accessKey, secretKey);
		BaiduBCS baiduBCS = new BaiduBCS(credentials, host);
	    baiduBCS.setDefaultEncoding("UTF-8"); 
		GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest(HttpMethodName.GET, bucket, object);
		generateUrlRequest.setBcsSignCondition(new BCSSignCondition());
		return baiduBCS.generateUrl(generateUrlRequest); 
	}

}
