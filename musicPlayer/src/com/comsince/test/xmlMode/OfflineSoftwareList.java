package com.comsince.test.xmlMode;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

   @Root(strict=false, name="offlineSoftwareList")
   public class OfflineSoftwareList {
   	 
   	@Element(required=false)
   	private String softwareName;	   
   		   
   	@Element(required=false)
   	private String softwarePath;
   	
   	public void setSoftwareName(String inSoftwareName) {
   		softwareName = inSoftwareName;
   	}
   	   
   	public void setSoftwarePath(String inSoftwarePath) {
   		softwarePath = inSoftwarePath;
   	}

   	public String getSoftwareName() {
   		return softwareName;
   	}
   		   
   	public String getSoftwarePath() {
   		return softwarePath;
   	}	   
   	   
   }