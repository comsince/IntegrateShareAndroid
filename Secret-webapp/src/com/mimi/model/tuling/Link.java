package com.mimi.model.tuling;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Link {

	/**
	 * 链接提示信息
	 * */
	public String text;
	/**
	 * 链接地址
	 * */
	public String url;
	
	public static Link parse(String jsonString){
		 if (TextUtils.isEmpty(jsonString)) {
	            return null;
	        }

	        Link link = null;
	        try {
	            JSONObject jsonObject = new JSONObject(jsonString);
	            link = parse(jsonObject);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	        return link;
	}
	
	public static Link parse(JSONObject jsonObject) {
       if (null == jsonObject) {
           return null;
       }
       Link link = new Link();
       link.text              = jsonObject.optString("text");
       link.url               = jsonObject.optString("url");
       return link;
   }
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
