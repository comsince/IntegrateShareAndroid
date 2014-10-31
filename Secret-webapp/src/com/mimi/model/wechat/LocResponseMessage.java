package com.mimi.model.wechat;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="xml")
public class LocResponseMessage extends BaseMessage{
	
	@Element(name="Location_X")
	private String locationX;
	
	@Element(name="Location_Y")
	private String locationY;
	
	@Element(name="Scale")
	private String scale;
	
	@Element(name="Label")
	private String lable;

	public String getLocationX() {
		return locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}
	
	
}
