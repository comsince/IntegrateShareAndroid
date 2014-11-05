package com.mimi.model.wechat;

import org.simpleframework.xml.Element;

/**
 * notice： 通过simpleframework解析出来的带有空格的字符，注意在比较的时候留意
 * **/
public class BaseMessage {
	@Element(name="URL",required=false)
	private String url;
	
	@Element(name="ToUserName",required=false)
	private String toUserName;
	
	@Element(name="FromUserName",required=false)
	private String fromUserName;
	
	@Element(name="CreateTime",required=false)
	private String createTime;
	
	@Element(name="MsgType",required=false)
	private String msgType;
	
	@Element(name="MsgId",required=false)
	private String msgId;

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getToUserName() {
		return toUserName.trim();
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName.trim();
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getMsgType() {
		return msgType.trim();
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMsgId() {
		return msgId.trim();
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@Override
	public String toString() {
		return "URL--"+url+"--ToUserName--"+toUserName+"--FromUserName--"+fromUserName+"--CreateTime--"+createTime
				+"--MsgType--"+msgType+"--MsgId--"+msgId;
	}
	
	
}
