package com.mimi.model;

import java.io.Serializable;

public class Matter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String matterId;
	private String userId;
	private String content;
	private String alias;
	private String timestamp;
	private String lastretime;
	private String type;
	private String fileType;
	private String file;
	private String vote;
	private int recount;
	public String getMatterId() {
		return matterId;
	}
	public void setMatterId(String matterId) {
		this.matterId = matterId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	 
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getVote() {
		return vote;
	}
	public void setVote(String vote) {
		this.vote = vote;
	}
	public int getRecount() {
		return recount;
	}
	public void setRecount(int recount) {
		this.recount = recount;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getLastretime() {
		return lastretime;
	}
	public void setLastretime(String lastretime) {
		this.lastretime = lastretime;
	}
	 
}
