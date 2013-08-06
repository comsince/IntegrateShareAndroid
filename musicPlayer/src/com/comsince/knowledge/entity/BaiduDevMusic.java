package com.comsince.knowledge.entity;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
@Root(strict = false, name = "res")
public class BaiduDevMusic {
	//注意这些都是element 不是attrbuite
	@Element(required = false)
	private String song;
	@Element(required = false)
	private String song_id;
	@Element(required = false)
	private String singer;
	@Element(required = false)
	private String album;
	@Element(required = false)
	private String singerPicLarge;
	@Element(required = false)
	private String singerPicSmall;
	@Element(required = false)
	private String albumPicLarge;
	@Element(required = false)
	private String albumPicSmall;

	public String getSong() {
		return song;
	}

	public void setSong(String song) {
		this.song = song;
	}

	public String getSong_id() {
		return song_id;
	}

	public void setSong_id(String song_id) {
		this.song_id = song_id;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getSingerPicLarge() {
		return singerPicLarge;
	}

	public void setSingerPicLarge(String singerPicLarge) {
		this.singerPicLarge = singerPicLarge;
	}

	public String getSingerPicSmall() {
		return singerPicSmall;
	}

	public void setSingerPicSmall(String singerPicSmall) {
		this.singerPicSmall = singerPicSmall;
	}

	public String getAlbumPicLarge() {
		return albumPicLarge;
	}

	public void setAlbumPicLarge(String albumPicLarge) {
		this.albumPicLarge = albumPicLarge;
	}

	public String getAlbumPicSmall() {
		return albumPicSmall;
	}

	public void setAlbumPicSmall(String albumPicSmall) {
		this.albumPicSmall = albumPicSmall;
	}

}
