package com.comsince.phonebook.entity;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false, name = "phone")
public class Phone implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3952181297461648024L;
	@Element(required = false , name = "attribution")
	private String attribution;
	@Element(required = false , name = "number")
	private String number;

	public String getAttribution() {
		return attribution;
	}

	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
