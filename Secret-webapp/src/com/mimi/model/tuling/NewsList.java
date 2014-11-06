package com.mimi.model.tuling;

import java.util.ArrayList;

import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsList {
	public ArrayList<News> newsList;

    public static NewsList parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        NewsList newsList = new NewsList();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String code = jsonObject.getString("code");
            JSONArray jsonArray = jsonObject.optJSONArray("list");
            if (jsonArray != null && jsonArray.length() > 0) {
                int length = jsonArray.length();
                newsList.newsList = new ArrayList<News>(length);
                for (int ix = 0; ix < length; ix++) {
                	newsList.newsList.add(News.parse(jsonArray.optJSONObject(ix)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }

}
