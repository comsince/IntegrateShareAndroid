package com.comsince.phonebook.entity;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 分组的基本信息,当管理员创建群组时，需要填写该群组的基本信息
 * 其他该组成员就会根据跟组的Tag找到这组信息，选择是否加入该组
 * */
@Root(strict = false, name = "groupInfo")
public class GroupInfo {
	@Element(required = false)
	private String adminAccount;
	@Element(required = false)
	private String adminRealName;
	@Element(required = false)
	private String groupTag;
	@Element(required = false)
	private String remark;
	@Element(required = false)
	private String groupName;

	public String getAdminAccount() {
		return adminAccount;
	}

	public void setAdminAccount(String adminAccount) {
		this.adminAccount = adminAccount;
	}

	public String getAdminRealName() {
		return adminRealName;
	}

	public void setAdminRealName(String adminRealName) {
		this.adminRealName = adminRealName;
	}

	public String getGroupTag() {
		return groupTag;
	}

	public void setGroupTag(String groupTag) {
		this.groupTag = groupTag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
