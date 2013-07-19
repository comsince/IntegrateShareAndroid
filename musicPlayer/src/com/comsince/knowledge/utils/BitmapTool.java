package com.comsince.knowledge.utils;

import java.io.FileInputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapTool {
	/**
	 * 根据专辑 albumkey 获取专辑图片
	 * @param context
	 * @param albumkey
	 * @return
	 */
	public static Bitmap getbitBmBykey(Context context,String albumkey){
		String path=MusicDataUtil.getAlbumArtPath(context, albumkey);
		return getbitmap(path);
	}
	/**
	 * 根据专辑路径获取专辑图片
	 * @param albumpath
	 * @return
	 */
	public static Bitmap getbitmap(String albumpath){
		FileInputStream is = null;
		try {
			is = new FileInputStream(albumpath);
			return BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				if (is!=null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	} 

}
