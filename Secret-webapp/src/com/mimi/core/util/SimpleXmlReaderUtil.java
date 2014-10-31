package com.mimi.core.util;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class SimpleXmlReaderUtil {
	
	private static SimpleXmlReaderUtil mInstance;
	private Serializer serializer;

	public SimpleXmlReaderUtil() {
		this.serializer = new Persister();
	}
	
	public static synchronized SimpleXmlReaderUtil getInstance(){
		if(mInstance == null){
			mInstance = new SimpleXmlReaderUtil();
		}
		return mInstance;
	}
	/**
	 * @author comsince
	 * @param xmlPath  将要解析的xml文件路径
	 * @param object   xml解析的文件对象
	 * @throws Exception 
	 * **/
	public <T> T readXml(String xmlPath,Class<? extends T> object ) throws Exception{
		File inputXml = new File(xmlPath);
		return serializer.read(object, inputXml);
		
	}
	/**
	 * @author comsince
	 * @param in 文件输入流
	 * @param object xml 解析文件对象
	 * */
	public <T> T readXmlFromInputStream(InputStream in,Class<? extends T> object ) throws Exception{
		return serializer.read(object, in);
	}
	
	/**
	 * @author ljlong-dev
	 * @param str
	 * @param object
	 * */
	public <T> T readXmlFromString(String str,Class<? extends T> object) throws Exception{
		return serializer.read(object, str);
	}
	
	
	/**
	 * @author comsince
	 * @param object  将要写入xml的对象
	 * @param xmlPath 将要写入xml的文件路径
	 * **/
	public void writeXml(Object object,String xmlPath){
	   File outputXmlFile = new File(xmlPath);
	   try {
		serializer.write(object, outputXmlFile);
		} catch (Exception e) {
			e.printStackTrace();
	  }
 		
	}
	
	public void writeXml(Object object,OutputStream out){
		try {
			serializer.write(object, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeXml(Object object,Writer writer){
		try {
			serializer.write(object, writer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
