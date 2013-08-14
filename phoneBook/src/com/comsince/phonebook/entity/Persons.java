package com.comsince.phonebook.entity;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
@Root(strict=false, name="persons")
public class Persons {
	@ElementList(required = false,inline=true)
	private List<Person> persons;

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

}
