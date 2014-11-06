package com.mimi.model.tuling;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Priviledge {
	
	public String name;
	public String icon;
	public String info;
    public String detailurl;
    
    public static Priviledge parse(String jsonString){
		 if (TextUtils.isEmpty(jsonString)) {
	            return null;
	        }

		 Priviledge priviledge = null;
	        try {
	            JSONObject jsonObject = new JSONObject(jsonString);
	            priviledge = parse(jsonObject);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	        return priviledge;
	}
	
	public static Priviledge parse(JSONObject jsonObject) {
      if (null == jsonObject) {
          return null;
      }
      Priviledge priviledge = new Priviledge();
      priviledge.name              = jsonObject.optString("name");
      priviledge.icon              = jsonObject.optString("icon");
      priviledge.info              = jsonObject.optString("info");
      priviledge.detailurl              = jsonObject.optString("detailurl");
      return priviledge;
  }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getDetailurl() {
		return detailurl;
	}
	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}
    
    
}
