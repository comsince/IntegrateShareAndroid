package com.comsince.phonebook.entity;

public class MessageItem {
	// Text
	public static final int MESSAGE_TYPE_TEXT = 1;
	// image
	public static final int MESSAGE_TYPE_IMG = 2;
	// file
	public static final int MESSAGE_TYPE_FILE = 3;

	private int msgType;
	private String name;// 消息来自
	private long time;// 消息日期
	private String message;// 消息内容
	private int headImg;
	private boolean isComMeg = true;// 是否为收到的消息

	private int isNew;

	public MessageItem() {
	}

	public MessageItem(int msgType, String name, long date, String message, int headImg, boolean isComMeg, int isNew) {
		super();
		this.msgType = msgType;
		this.name = name;
		this.time = date;
		this.message = message;
		this.headImg = headImg;
		this.isComMeg = isComMeg;
		this.isNew = isNew;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDate() {
		return time;
	}

	public void setDate(long date) {
		this.time = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getHeadImg() {
		return headImg;
	}

	public void setHeadImg(int headImg) {
		this.headImg = headImg;
	}

	public boolean isComMeg() {
		return isComMeg;
	}

	public void setComMeg(boolean isComMeg) {
		this.isComMeg = isComMeg;
	}

	public static int getMessageTypeText() {
		return MESSAGE_TYPE_TEXT;
	}

	public static int getMessageTypeImg() {
		return MESSAGE_TYPE_IMG;
	}

	public static int getMessageTypeFile() {
		return MESSAGE_TYPE_FILE;
	}

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

}
