package com.comsince.phonebook.entity;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(strict = false, name = "phones")
public class Phones implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5798302515297624222L;
	@ElementList(required = false, inline = true)
	private List<Phone> phones;

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

}
