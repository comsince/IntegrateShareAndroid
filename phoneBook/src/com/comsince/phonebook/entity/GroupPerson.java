package com.comsince.phonebook.entity;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false, name = "groupPerson")
public class GroupPerson {
	@Element(required = false)
	private String personAccount;
	@Element(required = false)
	private String personAccountPassword;
	@Element(required = false)
	private String personRealName;
	@Element(required = false)
	private String isAuthor;
	@Element(required = false)
	private String detialInfoPath;

	public String getPersonAccount() {
		return personAccount;
	}

	public void setPersonAccount(String personAccount) {
		this.personAccount = personAccount;
	}

	public String getPersonRealName() {
		return personRealName;
	}

	public void setPersonRealName(String personRealName) {
		this.personRealName = personRealName;
	}

	public String getIsAuthor() {
		return isAuthor;
	}

	public void setIsAuthor(String isAuthor) {
		this.isAuthor = isAuthor;
	}

	public String getDetialInfoPath() {
		return detialInfoPath;
	}

	public void setDetialInfoPath(String detialInfoPath) {
		this.detialInfoPath = detialInfoPath;
	}

	public String getPersonAccountPassword() {
		return personAccountPassword;
	}

	public void setPersonAccountPassword(String personAccountPassword) {
		this.personAccountPassword = personAccountPassword;
	}

}
