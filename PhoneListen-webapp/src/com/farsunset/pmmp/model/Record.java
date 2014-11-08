/**
 * @author 3979434
 */
package com.farsunset.pmmp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 */
@Entity
@Table(name = "t_pmmp_record")
public class Record implements Serializable {

	private static final long serialVersionUID = 4733464888738356502L;


	@Id
	@Column(name = "gid", length = 13)
	private String gid;

	@Column(name = "menumber", length = 16)
	private String menumber;

	@Column(name = "hename", length = 16)
	private String hename;

	@Column(name = "henumber", length = 16)
	private String henumber;
 
	@Column(name = "beginTime", length = 13)
	private String beginTime;
	
	@Column(name = "endTime", length = 13)
	private String endTime;
	
	@Column(name = "type", length = 1)
	private String type; 

	@Column(name = "status", length = 1)
	private String status;
	
	@Column(name = "content", length = 320)
	private String content;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	 

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMenumber() {
		return menumber;
	}

	public void setMenumber(String menumber) {
		this.menumber = menumber;
	}

	public String getHename() {
		return hename;
	}

	public void setHename(String hename) {
		this.hename = hename;
	}

	public String getHenumber() {
		return henumber;
	}

	public void setHenumber(String henumber) {
		this.henumber = henumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
}