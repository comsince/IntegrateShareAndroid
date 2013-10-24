package com.comsince.phonebook.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import com.comsince.phonebook.constant.Constant;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class PhotoUtil {
	/**
	 * 将图片变为圆角
	 * 
	 * @param bitmap
	 *            原Bitmap图片
	 * @param pixels
	 *            图片圆角的弧度(单位:像素(px))
	 * @return 带有圆角的图片(Bitmap 类型)
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	
	/**
	 * 保存图片到SD卡
	 * 
	 * @param bitmap
	 *            图片的bitmap对象
	 * @param bitmapName 存放图片的文件名，没有后缀名，默认格式为jpg
	 * @return
	 */
	public static String savePhotoToSDCard(Bitmap bitmap,String bitmapName) {
		if (!AndroidUtil.isSdcardExist()) {
			return null;
		}
		FileOutputStream fileOutputStream = null;
		FileUtil.createEmptyDir(Constant.PHONE_BOOK_PATH + File.separator + Constant.DIR_PERSON_AVATAR);

		String fileName = bitmapName + ".jpg";
		String newFilePath = AndroidUtil.getSDCardRoot() + Constant.PHONE_BOOK_PATH + File.separator + 
				Constant.DIR_PERSON_AVATAR + File.separator  + File.separator + fileName;
		File file = FileUtil.createNewFile(newFilePath);
		if (file == null) {
			return null;
		}
		try {
			fileOutputStream = new FileOutputStream(newFilePath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
		} catch (FileNotFoundException e1) {
			return null;
		} finally {
			try {
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (IOException e) {
				return null;
			}
		}
		return newFilePath;
	}
}
