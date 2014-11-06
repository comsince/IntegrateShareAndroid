package com.mimi.model.tuling;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Text {
 
	public String text;
	
	public static Text parse(String jsonString){
		 if (TextUtils.isEmpty(jsonString)) {
	            return null;
	        }
	        Text tt = null;
	        try {
	            JSONObject jsonObject = new JSONObject(jsonString);
	            tt = parse(jsonObject);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	        return tt;
	}
	
	public static Text parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }
        Text tt = new Text();
        tt.text              = jsonObject.optString("text");
        return tt;
    }

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
