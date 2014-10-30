package com.comsince.secret.bean;

import java.io.Serializable;

public class UpdateInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int newlevel;
	private String appUrl;
	private String newVersion;
	private String updateMsg;
	public int getNewlevel() {
		return newlevel;
	}
	public void setNewlevel(int newlevel) {
		this.newlevel = newlevel;
	}
    
	public String getUpdateMsg() {
		return updateMsg;
	}
	public void setUpdateMsg(String updateMsg) {
		this.updateMsg = updateMsg;
	}
	public String getAppUrl() {
		return appUrl;
	}
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
	public String getNewVersion() {
		return newVersion;
	}
	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}
	
}
