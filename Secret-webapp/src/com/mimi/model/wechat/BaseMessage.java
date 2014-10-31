package com.mimi.model.wechat;

import org.simpleframework.xml.Element;

public class BaseMessage {
	@Element(name="ToUserName")
	private String toUserName;
	
	@Element(name="FromUserName")
	private String fromUserName;
	
	@Element(name="CreateTime")
	private Integer createTime;
	
	@Element(name="MsgType")
	private String msgType;
	
	@Element(name="MsgId")
	private Integer msgId;

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public Integer getMsgId() {
		return msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}
	
	
}
