package com.comsince.knowledge.entity;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
@Root(strict=false, name="reslist")
public class BaiduDevMusicList {
	@ElementList(required = false,inline=true)
	private List<BaiduDevMusic> baiduDevMusics;

	public List<BaiduDevMusic> getBaiduDevMusics() {
		return baiduDevMusics;
	}

	public void setBaiduDevMusics(List<BaiduDevMusic> baiduDevMusics) {
		this.baiduDevMusics = baiduDevMusics;
	}
	

}
