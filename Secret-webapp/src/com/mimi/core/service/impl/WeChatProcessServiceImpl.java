package com.mimi.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mimi.core.http.CustomHttpClient;
import com.mimi.core.service.WeChatProcessService;
import com.mimi.model.tuling.Link;
import com.mimi.model.tuling.Priviledge;
import com.mimi.model.tuling.PriviledgeList;
import com.mimi.model.tuling.MessageCode;
import com.mimi.model.tuling.News;
import com.mimi.model.tuling.NewsList;
import com.mimi.model.tuling.Text;
import com.mimi.model.wechat.req.EventMessage;
import com.mimi.model.wechat.req.ReqTextMessage;
import com.mimi.model.wechat.resp.Article;
import com.mimi.model.wechat.resp.NewsMessage;
import com.mimi.model.wechat.resp.TextMessage;
import com.mimi.util.MessageUtil;

public class WeChatProcessServiceImpl implements WeChatProcessService {

	@Override
	public String processTextMsg(ReqTextMessage reqTextMsg) {
		String result = null;
		int code = MessageCode.SPECIAL;
		String responseJsonStr = null;
		if(reqTextMsg.getContent().trim().equals("秘密")){
			code = MessageCode.SPECIAL;
		}else if(reqTextMsg.getContent().trim().equals("?") || reqTextMsg.getContent().trim().equals("帮助")
				||reqTextMsg.getContent().trim().equals("help")){
			code = MessageCode.HELP;
		}else{
			responseJsonStr = CustomHttpClient.getMessage(reqTextMsg.getContent());
			code = MessageUtil.getTulingStatusCode(responseJsonStr);
		}
		System.out.println("code--"+code+"--"+reqTextMsg.getContent().trim());
		switch (code) {
		case MessageCode.SPECIAL:
			result = null;
			break;
		case MessageCode.NEWS:
			result = responseTulingNewsMsg(reqTextMsg,responseJsonStr);
			break;
		case MessageCode.TEXT:
			result = responseTulingTextMsg(reqTextMsg,responseJsonStr);
			break;
		case MessageCode.PRIVILEDGE:
			result = responseTulingPriviledgeMsg(reqTextMsg,responseJsonStr);
			break;
		case MessageCode.STORY:
			break;
		case MessageCode.WEBSITE:
			result = responseTulingWebsiteMsg(reqTextMsg,responseJsonStr);
			break;
		case MessageCode.HELP:
			result = responseHelpMsg(reqTextMsg);
			break;
		default:
			break;
		}
		return result;
	}
	
	@Override
	public String processFollowMsg(EventMessage eventMsg) {
		ReqTextMessage reqTextMsg = new ReqTextMessage();
		reqTextMsg.setToUserName(eventMsg.getToUserName());
		reqTextMsg.setFromUserName(eventMsg.getFromUserName());
		reqTextMsg.setCreateTime(new Date().getTime());
		return responseHelpMsg(reqTextMsg);
	}

	/**
	 * 图灵机器人取得实时新闻信息
	 * 回复多图文消息
	 * **/
    private String responseTulingNewsMsg(ReqTextMessage reqTextMsg,String jsonStr){
    	NewsList newsList = NewsList.parse(jsonStr);
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
    	NewsMessage newsMsg = responseNewsMsg(reqTextMsg,articles,6);
    	return MessageUtil.newsMessageToXml(newsMsg);
    }
    
    /**
     * 图灵文字聊天机器人
     * 回复纯文本信息，回复格式为微信文本格式
     * **/
    private String responseTulingTextMsg(ReqTextMessage reqTextMsg,String jsonStr){
    	Text robotText = Text.parse(jsonStr);
    	TextMessage textMessage = responseTextMsg(reqTextMsg);
		textMessage.setContent(robotText.text);
		return MessageUtil.textMessageToXml(textMessage);
    }
    
    /**
     * 帮助信息
     * */
    private String responseHelpMsg(ReqTextMessage reqTextMsg){
    	TextMessage textMessage = responseTextMsg(reqTextMsg);
    	StringBuffer buffer = new StringBuffer();
		buffer.append("您好，我是comsince，你可以像下面这样问我：").append("\n\n");
		buffer.append("1  天气预报-->输入珠海天气").append("\n");
		buffer.append("2  公交查询-->输入珠海公交10A").append("\n");
		buffer.append("3  聊天唠嗑-->输入我爱你老婆").append("\n");
		buffer.append("4  实时新闻-->输入娱乐新闻").append("\n");
		buffer.append("5  菜谱查询-->输入辣子鸡").append("\n");
		buffer.append("6  更多功能等待你的发现").append("\n\n");
		buffer.append("回复“?”显示此帮助菜单");
		textMessage.setContent(buffer.toString());
		return MessageUtil.textMessageToXml(textMessage);
    }
    
    /**
     * 图灵优惠信息
     * 回复格式为，图文消息格式
     * */
    private String responseTulingPriviledgeMsg(ReqTextMessage reqTextMsg,String jsonStr){
    	PriviledgeList priviledgeList = PriviledgeList.parse(jsonStr);
    	List<Article> articles = new ArrayList<Article>();
    	for(int i=0;i<6;i++){
    		Priviledge priviledge = priviledgeList.list.get(i);
    		Article article = new Article();
    		article.setTitle(priviledge.getName());
    		article.setPicUrl(priviledge.getIcon());
    		article.setDescription(priviledge.getInfo());
    		article.setUrl(priviledge.getDetailurl());
    		articles.add(article);
    	}
    	NewsMessage newsMsg = responseNewsMsg(reqTextMsg,articles,6);
    	return MessageUtil.newsMessageToXml(newsMsg);
    }
    
    /**
     * 图灵网站链接消息
     * 回复格式：文本格式
     * */
    private String responseTulingWebsiteMsg(ReqTextMessage reqTextMsg,String jsonStr){
    	Link link = Link.parse(jsonStr);
    	TextMessage textMessage = responseTextMsg(reqTextMsg);
	    textMessage.setContent(link.text+link.url);
		return MessageUtil.textMessageToXml(textMessage);
    }
    
    /**
     * 微信通用文本信息格式构建类,预留content随后填充
     * @param textMessage
     * @return TextMessage 这里的消息类型为响应的文本对象类型
     * **/
    @Override
    public TextMessage responseTextMsg(ReqTextMessage reqTextMsg){
    	TextMessage textMessage = new TextMessage();
		textMessage.setToUserName(reqTextMsg.getToUserName());
		textMessage.setFromUserName(reqTextMsg.getFromUserName());
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		return textMessage;
    }
    
    /**
     * 微信通用图文消息格式构建类
     * @param textMsg
     * @param articles
     * @param articleCount
     * */
    private NewsMessage responseNewsMsg(ReqTextMessage reqTextMsg,List<Article> articles,int articleCount){
    	NewsMessage newsMsg = new NewsMessage();
    	newsMsg.setArticleCount(articleCount);
    	newsMsg.setArticles(articles);
    	newsMsg.setCreateTime(new Date().getTime());
    	newsMsg.setFromUserName(reqTextMsg.getFromUserName());
    	newsMsg.setToUserName(reqTextMsg.getToUserName());
    	newsMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
    	return newsMsg;
    }

	
    
    

}
