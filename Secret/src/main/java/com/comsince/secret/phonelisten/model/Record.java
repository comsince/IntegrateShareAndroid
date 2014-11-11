/**
 * @author 3979434
 */
package com.comsince.secret.phonelisten.model;

import java.io.Serializable;
 
 
public class Record implements Serializable {

	public static final long serialVersionUID = 4733464888738356502L;

	public String gid;

	public String menumber;

	public String hename;

	public String henumber;
 
	public String beginTime;
	
	public String endTime;
	
	public String type; 

	public String status;
	
	public String content;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Record{" +
                "gid='" + gid + '\'' +
                ", menumber='" + menumber + '\'' +
                ", hename='" + hename + '\'' +
                ", henumber='" + henumber + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}