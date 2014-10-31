package com.mimi.model.wechat;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="xml")
public class PicResponseMessage extends BaseMessage {
	
	@Element(name = "PicUrl")
	private String picUrl;
	
	@Element(name="MediaId")
	private String mediaId;

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	
}
