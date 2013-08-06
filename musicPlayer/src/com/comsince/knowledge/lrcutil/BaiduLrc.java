package com.comsince.knowledge.lrcutil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.comsince.knowledge.entity.BaiduDevMusic;
import com.comsince.knowledge.entity.BaiduDevMusicList;
import com.comsince.knowledge.utils.HttpTool;
import com.comsince.knowledge.utils.SimpleXmlReaderUtil;

public class BaiduLrc {
	public static String BAIDU_API_PATH = "http://box.zhangmen.baidu.com/x?op=12&count=1&title=";
	/**
	 * 参数： word: "歌曲名", //歌曲名 encodeURI format: "json", //返回数据格式，xml |
	 * json，默认xml
	 * */
	public static String BAIDU_DEV_API_PATH = "http://mp3.baidu.com/dev/api/?tn=getinfo&ct=0&ie=utf-8&word=<word>&format=xml";
	/**
	 * 通过歌曲id下载歌词
	 * */
	public static String BAIDU_TING_API_PATH = "http://ting.baidu.com/data/music/links?songIds=<songIds>";
	/**
	 * lrc base
	 * */
	public static String BAIDU_TING_LRC_BASE =  "http://ting.baidu.com";
	/**
	 * 百度歌词地址
	 * */
	public static String BAIDU_LRC_PATH = "http://box.zhangmen.baidu.com/bdlrc/";

	public static String getMusic(String musicname, String singername) {
		Log.i("lrc", "--" + BAIDU_API_PATH + EncodeName(musicname) + "$$" + EncodeName(singername) + "$$$$");
		return BAIDU_API_PATH + EncodeName(musicname) + "$$" + EncodeName(singername) + "$$$$";
	}

	public static String getLrcPath(String lrcid) {
		int id = Integer.parseInt(lrcid);
		Log.i("lrc", lrcid + "---/100");
		return BAIDU_LRC_PATH + id / 100 + "/" + id + ".lrc";
	}

	/**
	 * 歌曲或者歌手名称 中间有空格的时候 必须要进行转码
	 * 
	 * @param name
	 * @return
	 */
	public static String EncodeName(String name) {
		String[] names = name.split(" ");
		if (names.length > 1) {// 说明是名字中间有空格
			try {
				return URLEncoder.encode(name.trim(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return name;
			}
		} else {
			return name;
		}
	}
	
    /**
     * 根据歌曲名返回该歌曲的列表，包含不同歌手的演唱版本
     * @param songName 歌曲名
     * @return  BaiduDevMusicList 歌曲列表
     * */
	public static BaiduDevMusicList getBaiduDevMusicListBySongName(String songName){
		BaiduDevMusicList musics = new BaiduDevMusicList();
		 SimpleXmlReaderUtil simpleXmlReaderUtil = new SimpleXmlReaderUtil();
		 String decPath = BAIDU_DEV_API_PATH.replace("<word>", EncodeName(songName));
		 try {
			InputStream in = HttpTool.getStream(decPath, null, null, HttpTool.GET);
			musics = simpleXmlReaderUtil.readXmlFromInputStream(in, BaiduDevMusicList.class);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return musics;
		
	}
	
	/**
	 * 根据歌手名过滤出该音乐列表，获取song_id
	 * @param singer  艺术家
	 * @param baiduDevMusicList 
	 * */
	public static String getSongIdBySinger(String singer,BaiduDevMusicList baiduDevMusicList){
		String songId = null;
		for(BaiduDevMusic baiduMusic : baiduDevMusicList.getBaiduDevMusics()){
			if(singer.equals(baiduMusic.getSinger())){
				songId = baiduMusic.getSong_id();
				break;
			}
		}
		return songId;
	}
	
	/**
	 * 根据歌曲id，返回歌词下载地址
	 * */
	public static String  getLrcAddressBySongId(String songId){
		String lrcUrl = null;
		String songInfoUrl = BAIDU_TING_API_PATH.replace("<songIds>", EncodeName(songId));
		try {
			String songInfoJsonStr = HttpTool.getString(songInfoUrl, null, null, HttpTool.GET);
			JSONArray songListObj = new JSONObject(songInfoJsonStr).getJSONObject("data").getJSONArray("songList");
			String lrcLink = ((JSONObject) songListObj.get(0)).getString("lrcLink");
			lrcUrl = BAIDU_TING_LRC_BASE + lrcLink;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return lrcUrl;
	}
	

}
