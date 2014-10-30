package com.mimi.core.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

import com.mimi.model.Config;

public class DataMap {

	final static LinkedHashMap<String,String> provinceMap = new LinkedHashMap<String,String>();
	final static List<Config> cityMap = new ArrayList<Config>();
	final static LinkedHashMap<String,String> businessMap = new LinkedHashMap<String,String>();
	
	public static void loadProvinceMap()
	{
		SAXReader sax = new SAXReader();
		Document root ;
		try {
			root = sax.read(DataMap.class.getResourceAsStream("region.xml"));
			List<Node>  nodeList = root.selectNodes("/data/province");
			for(Node node : nodeList)
			{
				DefaultElement element = (DefaultElement) node;
				provinceMap.put(element.attributeValue("id"),element.attributeValue("name"));
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static  List<Config>  getCityList(String provinceId) throws DocumentException
	{
		cityMap.clear();
		SAXReader sax = new SAXReader();
		Document root = sax.read(DataMap.class.getResourceAsStream("region.xml"));
		Element element = (Element) root.selectSingleNode("/data/province[@id='"+provinceId+"']");
		for(Element node : (List<Element>)element.elements())
		{
			Config config = new Config();
			config.setKey(node.attributeValue("id"));
			config.setValue(node.attributeValue("name"));
			cityMap.add(config);
		}
		return cityMap;
	}
	
	public static void loadBusinessMap()
	{
		try {
			Properties properties= new Properties();
			properties.load(DataMap.class.getResourceAsStream("business.properties"));
			for(Object key :properties.keySet())
			{
				businessMap.put(key.toString(), properties.get(key).toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static HashMap<String, String> getProvinceMap() {
		if(provinceMap.size() == 0)
		{
			loadProvinceMap();
		}
		return provinceMap;
	}

	public static LinkedHashMap getBusinessMap() {
		if(businessMap.size() == 0)
		{
			loadBusinessMap();
		}
		return businessMap;
	}
	
}
