package com.comsince.secret.bean;

import java.io.Serializable;

public class Comment implements Serializable{
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	public String commentId;
	public String matterId;
	public String userId;
	public String content;
	public String alias;
    public String rank;
	public String timestamp;
	 
}
