package com.comsince.knowledge.lrcutil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.util.Log;

public class BaiduLrc {
	public static String BAIDU_API_PATH = "http://box.zhangmen.baidu.com/x?op=12&count=1&title=";
	/**
	 * 参数： word: "歌曲名", //歌曲名 encodeURI format: "json", //返回数据格式，xml |
	 * json，默认xml
	 * */
	public static String BAIDU_DEV_API_PATH = "http://mp3.baidu.com/dev/api/?tn=getinfo&ct=0&ie=utf-8&word=<word>&format=<format>";
	/**
	 * 通过歌曲id下载歌词
	 * */
	public static String BAIDU_TING_API_PATH = "http://ting.baidu.com/data/music/links?songIds=<songIds>";
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

}
