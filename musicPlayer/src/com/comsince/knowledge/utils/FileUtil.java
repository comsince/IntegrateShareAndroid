package com.comsince.knowledge.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.comsince.knowledge.constant.Constant;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FileUtil {
	/**
	 * 将输入流复制到指定的文件路径中
	 * 
	 * @author comsince
	 * @param is
	 *            指定输入流
	 * @param fileName
	 *            要复制到的文件路径，此完整的路径名
	 * 
	 */
	public static void writeToFile(InputStream is, String fileName) {

		try {
			File outfile = new File(fileName);
			if (!outfile.exists()) {
				outfile.createNewFile();
			}

			OutputStream os = new FileOutputStream(fileName);
			byte[] data = new byte[is.available()];
			is.read(data);
			os.write(data);
			is.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从assets目录中读取文件
	 * 
	 * @author comsince
	 * @param filePath
	 *            该文件为assets目录下的具体路径
	 * @param context
	 *            该activity指定的上下文context
	 * @return InputStream 返回该文件的输入流，将该输入流读入内存，相对于文件而言
	 * */
	public static InputStream readFileFromAssets(Context context, String filePath) {
		InputStream is = null;
		try {
			is = context.getAssets().open(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;

	}

	/**
	 * 在SD卡上指定目录下创建文件
	 * 
	 * @author comsince
	 * @param fileName
	 *            要创建的文件名
	 * @param dir
	 *            要创建的文件所在的目录：相对于sdcardRoot的路径
	 * @throws IOException
	 */
	public static File createFileInSDCard(String fileName, String dir) throws IOException {
		File file = new File(AndroidUtil.getSDCardRoot() + dir + File.separator + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @author comsince
	 * @param dir
	 *            目录名
	 */
	public static File creatSDDir(String dir) {
		File dirFile = new File(AndroidUtil.getSDCardRoot() + dir + File.separator);
		System.out.println(dirFile.mkdirs());
		return dirFile;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 * 
	 * @author Comsince
	 * @param fileName
	 *            文件名
	 * @param path
	 *            文件所在的路径
	 * @return true 文件存在 false 文件不存在
	 */
	public static boolean isFileExist(String fileName, String path) {
		File file = new File(AndroidUtil.getSDCardRoot() + path + File.separator + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中，当path不存在时，创建目录
	 * 
	 * @author comsince
	 * @param path
	 *            文件路径
	 * @param fileName
	 *            文件名
	 * @param input
	 *            输入流
	 * 
	 */
	public static File write2SDFromInput(String path, String fileName, InputStream input) {

		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = createFileInSDCard(fileName, path);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int temp;
			while ((temp = input.read(buffer)) != -1) {
				output.write(buffer, 0, temp);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	/**
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void readData(InputStream in, OutputStream out) throws IOException {
		if (in != null && out != null) {
			BufferedInputStream bis = new BufferedInputStream(in);
			BufferedOutputStream bos = new BufferedOutputStream(out);
			int total = 0;
			int len = -1;
			byte[] bytes = new byte[1024];
			while ((len = bis.read(bytes)) != -1) {
				bos.write(bytes, 0, len);
				bos.flush();
				total += len;
				Log.i("download", "" + total);
			}
			bos.close();
			out.close();
			bis.close();
			in.close();
		}
	}
	
	
   /**
    * @param in
	 * @param out
	 * @throws IOException
    */
	public static void save(InputStream in, String path)  throws IOException {
		if (in != null && path != null) {
			File file = new File(path);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(path);
			readData(in, out);
		}
	}
	/**
	 * @param in
	 * @param out
	 * @param handler
	 * @param fileLength
	 * */
	public static void save(InputStream in,String path,Handler handler,long fileLength) throws IOException{
		if(in !=null && path!=null){
			File file = new File(path);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(path);
			readData(in, out,handler,fileLength);
		}
	}
	/**
	 * @param in
	 * @param out
	 * @param handler
	 * @throws IOException
	 */
	public static void readData(InputStream in,OutputStream out,Handler handler,long fileLength) throws IOException{
		if(in!=null && out!=null){
			BufferedInputStream bis = new BufferedInputStream(in);
			BufferedOutputStream bos = new BufferedOutputStream(out);
			int len = -1;
			int total = 0;
			int percent = 0;
			byte[] bytes = new byte[1024];
			int loadedLength = 0;//已下载的长度（kb）
			while((len = bis.read(bytes))!=-1){
				bos.write(bytes,0,len);
				total += len;
				percent = (int) (1.0 * total / fileLength) * 100;
				Message msg = handler.obtainMessage(Constant.MSG_PROGRESS);
				msg.arg1 = percent;
				Log.i("download", String.valueOf(total)+"/"+fileLength);
				Log.i("download", String.valueOf(percent));
				handler.sendMessage(msg);
				/*loadedLength++;//每下载1kb，该值+1
				//如果已下载长度是200的整数倍，则发送消息回主线程
				if(loadedLength%12==0 && handler!=null){
					Message msg = handler.obtainMessage(Constant.MSG_PROGRESS);
					msg.arg1 = loadedLength;
					if (fileLength>0&&loadedLength<fileLength) {
						handler.sendMessage(msg);
					}
				}*/
				bos.flush();
			}
			bos.close();
			out.close();
			bis.close();
			in.close();
		}
	}

}
