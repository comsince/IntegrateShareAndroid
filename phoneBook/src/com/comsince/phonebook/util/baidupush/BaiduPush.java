package com.comsince.phonebook.util.baidupush;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;



import android.util.Log;



public class BaiduPush {
	public final static String HTTP_METHOD_POST = "POST";
	public final static String HTTP_METHOD_GET = "GET";
	public final static String mUrl = "http://channel.api.duapp.com/rest/2.0/channel/";
	public static final String SEND_MSG_ERROR = "send_msg_error";
	
	private final static int HTTP_CONNECT_TIMEOUT = 10000;// 连接超时时间，10s
	private final static int HTTP_READ_TIMEOUT = 10000;// 读消息超时时间，10s
   /**
    * 给指定群组广播通知
    * */
	private final static String MSGKEY = "msgkey";
	public static String pushMsgToGroup(String tag,String message){
		RestApi ra = new RestApi(RestApi.METHOD_PUSH_MESSAGE);
		ra.put(RestApi._MESSAGE_TYPE, RestApi.MESSAGE_TYPE_NOTIFY);
		ra.put(RestApi._MESSAGES, message);
		ra.put(RestApi._MESSAGE_KEYS, MSGKEY);
		ra.put(RestApi._PUSH_TYPE, RestApi.PUSH_TYPE_TAG);
		ra.put(RestApi._TAG, tag);
		return PostHttpRequest(ra);
	}
	
	/**
	 * 给所有用户推送消息
	 * 
	 * @param message
	 * @return
	 */
	public static String PushMessage(String message) {
		RestApi ra = new RestApi(RestApi.METHOD_PUSH_MESSAGE);
		ra.put(RestApi._MESSAGE_TYPE, RestApi.MESSAGE_TYPE_MESSAGE);
		ra.put(RestApi._MESSAGES, message);
		ra.put(RestApi._MESSAGE_KEYS, MSGKEY);
		ra.put(RestApi._PUSH_TYPE, RestApi.PUSH_TYPE_ALL);
		return PostHttpRequest(ra);
	}
	
	/**
	 * 给指定用户推送消息
	 * 
	 * @param message
	 * @param userid
	 * @return
	 */
	public static String PushMessage(String message, String userid) {
		RestApi ra = new RestApi(RestApi.METHOD_PUSH_MESSAGE);
		ra.put(RestApi._MESSAGE_TYPE, RestApi.MESSAGE_TYPE_MESSAGE);
		ra.put(RestApi._MESSAGES, message);
		ra.put(RestApi._MESSAGE_KEYS, MSGKEY);
		ra.put(RestApi._PUSH_TYPE, RestApi.PUSH_TYPE_USER);
		ra.put(RestApi._USER_ID, userid);
		return PostHttpRequest(ra);
	}
	
	/**
	 * 执行Post请求前数据处理，加密之类
	 * 
	 * @param data
	 *            请求的数据
	 * @return
	 */
	public static String PostHttpRequest(RestApi data) {
		StringBuilder sb = new StringBuilder();
		String channel = data.remove(RestApi._CHANNEL_ID);
		if (channel == null)
			channel = "channel";
		try {
			data.put(RestApi._TIMESTAMP,Long.toString(System.currentTimeMillis() / 1000));
			data.remove(RestApi._SIGN);
			sb.append(HTTP_METHOD_POST);
			sb.append(mUrl);
			sb.append(channel);
			for (Map.Entry<String, String> i : data.entrySet()) {
				sb.append(i.getKey());
				sb.append('=');
				sb.append(i.getValue());
			}
			sb.append(RestApi.mSecretKey);
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(urlencode(sb.toString()).getBytes());
			byte[] md5 = md.digest();
			sb.setLength(0);
			for (byte b : md5)
				sb.append(String.format("%02x", b & 0xff));
			data.put(RestApi._SIGN, sb.toString());
			sb.setLength(0);
			for (Map.Entry<String, String> i : data.entrySet()) {
				sb.append(i.getKey());
				sb.append('=');
				sb.append(urlencode(i.getValue()));
				sb.append('&');
			}
			sb.setLength(sb.length() - 1);
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("test","PostHttpRequest Exception:" + e.getMessage());
			return SEND_MSG_ERROR;//消息发送失败，返回错误，执行重发
		}

		StringBuilder response = new StringBuilder();
		HttpRequest(mUrl + channel, sb.toString(), response);
		return response.toString();
	}
	
	/**
	 * 执行Post请求
	 * 
	 * @param url
	 *            基础url
	 * @param query
	 *            提交的数据
	 * @param out
	 *            服务器回复的字符串
	 * @return
	 */
	private static int HttpRequest(String url, String query, StringBuilder out) {
        int code = 0;
		URL urlobj;
		HttpURLConnection connection = null;

		try {
			urlobj = new URL(url);
			connection = (HttpURLConnection) urlobj.openConnection();
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection
					.setRequestProperty("Content-Length", "" + query.length());
			connection.setRequestProperty("charset", "utf-8");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
			connection.setReadTimeout(HTTP_READ_TIMEOUT);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(query.toString());
			wr.flush();
			wr.close();

			// Get Response
			code = connection.getResponseCode();
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;

			while ((line = rd.readLine()) != null) {
				out.append(line);
				out.append('\r');
			}
			rd.close();

		} catch (Exception e) {
			e.printStackTrace();
			Log.i("test","HttpRequest Exception:" + e.getMessage());
			out.append(SEND_MSG_ERROR);//消息发送失败，返回错误，执行重发
		}

		if (connection != null)
			connection.disconnect();

		return code;
	}
	
	/**
	 * url编码方式
	 * 
	 * @param str
	 *            指定编码方式，未指定默认为utf-8
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String urlencode(String str) throws UnsupportedEncodingException {
		String rc = URLEncoder.encode(str, "utf-8");
		return rc.replace("*", "%2A");
	}

}
