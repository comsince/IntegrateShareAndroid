package com.comsince.knowledge.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileUtil {
	static long fileLength;
    /**
     * @author comsince
     * @param directory  要壓縮的文件目錄
     * @param zip 壓縮的文件路徑
     * 
     * */
	public static final void zipDirectory(File directory, File zip) throws IOException {
		fileLength = zip.length();
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
		zip(directory, directory, zos);
		zos.close();
	}

	/**
	 * @author comsince
	 * @param directory
	 * @param base
	 * @param zos
	 * 
	 * */
	private static void zip(File directory, File base, ZipOutputStream zos) throws IOException {
		File[] files = directory.listFiles();
		byte[] buffer = new byte[8192];
		int read = 0;
		for (int i = 0, n = files.length; i < n; i++) {
			if (files[i].isDirectory()) {
				zip(files[i], base, zos);
			} else {
				FileInputStream in = new FileInputStream(files[i]);
				ZipEntry entry = new ZipEntry(files[i].getPath().substring(base.getPath().length() + 1));
				zos.putNextEntry(entry);
				while (-1 != (read = in.read(buffer))) {
					zos.write(buffer, 0, read);
				}
				in.close();
			}
		}
	}

}
