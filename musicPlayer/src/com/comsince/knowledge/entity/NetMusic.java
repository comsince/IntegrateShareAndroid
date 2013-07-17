package com.comsince.knowledge.entity;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false, name = "music")
public class NetMusic {
	@Attribute(required = true)
	private String id;
	@Element(required = false)
	private String name;
	@Element(required = false)
	private String singer;
	@Element(required = false)
	private String author;
	@Element(required = false)
	private String composer;
	@Element(required = false)
	private String album;
	@Element(required = false)
	private String albumpic;
	@Element(required = false)
	private String musicpath;
	@Element(required = false)
	private String time;
	@Element(required = false)
	private String downcount;
	@Element(required = false)
	private String favcount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getAlbumpic() {
		return albumpic;
	}

	public void setAlbumpic(String albumpic) {
		this.albumpic = albumpic;
	}

	public String getMusicpath() {
		return musicpath;
	}

	public void setMusicpath(String musicpath) {
		this.musicpath = musicpath;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDowncount() {
		return downcount;
	}

	public void setDowncount(String downcount) {
		this.downcount = downcount;
	}

	public String getFavcount() {
		return favcount;
	}

	public void setFavcount(String favcount) {
		this.favcount = favcount;
	}

}
