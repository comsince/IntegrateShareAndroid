package com.comsince.knowledge.entity;

import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(strict=false, name="sounds")
public class NetMusicList {
	@ElementList(required = false,inline=true)
	private List<NetMusic> netMusics;

	public List<NetMusic> getNetMusics() {
		return netMusics;
	}

	public void setNetMusics(List<NetMusic> netMusics) {
		this.netMusics = netMusics;
	}

}
