package com.comsince.phonebook.entity;

/**
 * 消息内容模型，不同于交互消息模型，这个模型仅仅处理在界面显示的作用
 * **/
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
	private String avatarName; //用户头像名
	private int headImg;
	private boolean isComMeg = true;// 是否为收到的消息

	private int isNew;

	public MessageItem() {
	}

	public MessageItem(int msgType, String name, long date, String message, int headImg, boolean isComMeg, int isNew ,String avatarName) {
		super();
		this.msgType = msgType;
		this.name = name;
		this.time = date;
		this.message = message;
		this.headImg = headImg;
		this.isComMeg = isComMeg;
		this.isNew = isNew;
		this.avatarName = avatarName;
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

	public String getAvatarName() {
		return avatarName;
	}

	public void setAvatarName(String avatarName) {
		this.avatarName = avatarName;
	}

}
