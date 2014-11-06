package com.mimi.model.tuling;

import java.util.ArrayList;

import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PriviledgeList {
	public ArrayList<Priviledge> list;

    public static PriviledgeList parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        PriviledgeList priviledgeList = new PriviledgeList();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.optJSONArray("list");
            if (jsonArray != null && jsonArray.length() > 0) {
                int length = jsonArray.length();
                priviledgeList.list = new ArrayList<Priviledge>(length);
                for (int ix = 0; ix < length; ix++) {
                	priviledgeList.list.add(Priviledge.parse(jsonArray.optJSONObject(ix)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return priviledgeList;
    }
}
