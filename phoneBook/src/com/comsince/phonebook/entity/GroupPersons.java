package com.comsince.phonebook.entity;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
/**
 * 该群组的详细人员列表，包括是否允许其人员加入群组
 * */
@Root(strict = false, name = "groupPersons")
public class GroupPersons {
	@ElementList(required = false, inline = true)
	private List<GroupPerson> groupPersons;

	public List<GroupPerson> getGroupPersons() {
		return groupPersons;
	}

	public void setGroupPersons(List<GroupPerson> groupPersons) {
		this.groupPersons = groupPersons;
	}

}
