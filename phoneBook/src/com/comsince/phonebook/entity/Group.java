package com.comsince.phonebook.entity;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 该人员加入的分组
 * */
@Root(strict = false, name = "group")
public class Group implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4408433059240361180L;
	@Element(required = false)
	private String groupTag;
	@Element(required = false)
	private String groupName;
	@Element(required = false)
	private String anthor;
	@Element(required = false)
	private String remark;
	public String getGroupTag() {
		return groupTag;
	}
	public void setGroupTag(String groupTag) {
		this.groupTag = groupTag;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getAnthor() {
		return anthor;
	}
	public void setAnthor(String anthor) {
		this.anthor = anthor;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}
