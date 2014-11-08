/**
 * @author 3979434
 */
package com.farsunset.pmmp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.Gson;

/**
 * 
 */
@Entity
@Table(name = "t_pmmp_user")
public class User implements Serializable {

	private static final long serialVersionUID = 4733464888738356502L;


	@Id
	@Column(name = "account", length = 64)
	private String account;

	@Column(name = "password", length = 64)
	private String password;

	@Column(name = "name", length = 16)
	private String name;
	
 
	@Column(name = "mobile", length = 11)
	private String mobile;
	
	@Column(name = "gps")
	private String  gps;
	 
	@Column(name = "net")
	private String  net;
	 
	@Column(name = "milieu")
	private String  milieu;
	
	
	@Column(name = "duration",length=10)
	private String  duration;
	
	@Column(name = "createTime", length = 13)
	private String createTime;
	
	
	@Column(name = "power", length = 64)
	private String power; 

	
	
    
	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public String getNet() {
		return net;
	}

	public void setNet(String net) {
		this.net = net;
	}

	public String getMilieu() {
		return milieu;
	}

	public void setMilieu(String milieu) {
		this.milieu = milieu;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof User)) {
			return false;
		}

		final User obj = (User) o;
		if (account != null ? !account.equals(obj.account)
				: obj.account != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		Integer result = 0;
		result = 29 * result + (account != null ? account.hashCode() : 0);
		result = 29 * result + (password != null ? password.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this).replaceAll("\"", "'");
	}

}
