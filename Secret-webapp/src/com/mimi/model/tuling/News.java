package com.mimi.model.tuling;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class News {
	/**
	 * 新闻标题
	 * */
	private String article;
	/**
	 * 新闻来源
	 * */
	private String source;
	/**
	 * 新闻图片
	 * */
	private String icon;
	/**
	 * 新闻详情地址
	 * */
	private String detailurl;
	
	
	public static News parse(String jsonString){
		 if (TextUtils.isEmpty(jsonString)) {
	            return null;
	        }

	        News news = null;
	        try {
	            JSONObject jsonObject = new JSONObject(jsonString);
	            news = parse(jsonObject);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	        return news;
	}
	
	public static News parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }
        News news = new News();
        news.article              = jsonObject.optString("article");
        news.source               = jsonObject.optString("source");
        news.icon                 = jsonObject.optString("icon");
        news.detailurl            = jsonObject.optString("detailurl");
        return news;
    }
	
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDetailurl() {
		return detailurl;
	}
	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}

	
}
