package com.comsince.phonebook.entity;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
@Root(strict=false, name="person")
public class Person implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Element(required = false)
	private String id;
	@Element(required = false)
	private String name;
	@Element(required = false)
	private String qq;
	@Element(required = false)
	private String marriage;
	@Element(required = false)
	private String reigon;
	@Element(required = false)
	private String remark;
	@ElementList(required = false,inline=true)
	private List<Phones> phonesList;
	/**
	 * 好友性别
	 * */
	@Element(required = false)
    private String sex;
	
	/**
	 * 好友的姓名拼音
	 */
	private String name_pinyin;
	/**
	 * 好友姓名的首字母
	 */
	private String name_first;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}

	public String getReigon() {
		return reigon;
	}

	public void setReigon(String reigon) {
		this.reigon = reigon;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	

	public List<Phones> getPhonesList() {
		return phonesList;
	}

	public void setPhonesList(List<Phones> phonesList) {
		this.phonesList = phonesList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName_pinyin() {
		return name_pinyin;
	}

	public void setName_pinyin(String name_pinyin) {
		this.name_pinyin = name_pinyin;
	}

	public String getName_first() {
		return name_first;
	}

	public void setName_first(String name_first) {
		this.name_first = name_first;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
