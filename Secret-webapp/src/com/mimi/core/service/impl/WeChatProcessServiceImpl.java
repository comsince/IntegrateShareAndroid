package com.mimi.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mimi.core.http.CustomHttpClient;
import com.mimi.core.service.WeChatProcessService;
import com.mimi.model.tuling.News;
import com.mimi.model.tuling.NewsList;
import com.mimi.model.wechat.resp.Article;
import com.mimi.model.wechat.resp.NewsMessage;
import com.mimi.model.wechat.resp.TextMessage;
import com.mimi.util.MessageUtil;

public class WeChatProcessServiceImpl implements WeChatProcessService {

	@Override
	public String processTextMsg(TextMessage textMsg) {
		String result = null;
		if(textMsg.getContent().trim().contains("新闻")){
			result = responseMutilPicMsg(textMsg);
		}
		return result;
	}

    private String responseMutilPicMsg(TextMessage textMsg){
    	String result = CustomHttpClient.getMessage(textMsg.getContent());
    	NewsList newsList = NewsList.parse(result);
    	List<Article> articles = new ArrayList<Article>();
    	for(int i=0;i<6;i++){
    		News news = newsList.newsList.get(i);
    		Article article = new Article();
    		article.setTitle(news.getArticle());
    		article.setPicUrl(news.getIcon());
    		article.setDescription(news.getSource());
    		article.setUrl(news.getDetailurl());
    		articles.add(article);
    	}
    	NewsMessage newsMsg = new NewsMessage();
    	newsMsg.setArticleCount(6);
    	newsMsg.setArticles(articles);
    	newsMsg.setCreateTime(new Date().getTime());
    	newsMsg.setFromUserName(textMsg.getFromUserName());
    	newsMsg.setToUserName(textMsg.getToUserName());
    	newsMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
    	return MessageUtil.newsMessageToXml(newsMsg);
    }

}
