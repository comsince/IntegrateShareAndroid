package com.comsince.test.xmlMode;


import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(strict=false, name="impda")
public class Impda {
	   
   @Element(required=false)
   private String impdaLanServer;

   @Element(required=false)
   private String impdaWanServer;
   
   @Element(required=false)
   private String pdaIsRegister;

   @Element(required=false)
   private String pdaId;
    
   @ElementList(required=false, name="offlineSoftwareLists")
   private List<OfflineSoftwareList> offlineSoftwareLists; 

   public void setOfflineSoftwareLists(List<OfflineSoftwareList> inOfflineSoftwareLists) {
       this.offlineSoftwareLists = inOfflineSoftwareLists;
   }     

   public List<OfflineSoftwareList> getOfflineSoftwareLists() {
       return offlineSoftwareLists;
   }    
   
   public void setValue(String inString, String value) {
		if (inString == "impdaLanServer"){
			impdaLanServer = value;
		}
		if (inString == "impdaWanServer"){
			impdaWanServer = value;
		}
		if (inString == "pdaIsRegister"){
			pdaIsRegister = value;
		}
		if (inString == "pdaId"){
			pdaId = value;
		}
   }   

   public String getValue(String inString) {
		if (inString == "impdaLanServer"){
			return impdaLanServer;
		}
		if (inString == "impdaWanServer"){
			return impdaWanServer;
		}
		if (inString == "pdaIsRegister"){
			return pdaIsRegister;
		}
		if (inString == "pdaId"){
			return pdaId;
		}
		return null;
   }   
   
   
}