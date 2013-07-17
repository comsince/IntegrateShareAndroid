package com.comsince.knowledge.utils;

import java.io.File;
import java.io.InputStream;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class SimpleXmlReaderUtil {
	
	private Serializer serializer;

	public SimpleXmlReaderUtil() {
		this.serializer = new Persister();
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
	

}
