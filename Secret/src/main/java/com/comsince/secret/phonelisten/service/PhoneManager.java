package com.comsince.secret.phonelisten.service;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import java.util.HashMap;

/**
 * Created by liaojinlong on 14-11-11.
 */
public class PhoneManager {

    /**
     * 获取用户的通话记录
     * */
    public static HashMap<String,String> getNewCallRecord(Context context){
        HashMap<String,String> map = new HashMap<String,String>();
        Cursor cursor =  context.getContentResolver().query(CallLog.Calls.CONTENT_URI, new String[] {
                        CallLog.Calls.NUMBER, CallLog.Calls.DURATION, CallLog.Calls.DATE }, "type=2", null,
                "date desc limit 1");

        if(cursor.moveToFirst()){
            map.put("number", cursor.getString(0));
            map.put("duration", cursor.getString(1));
            map.put("date", cursor.getString(2));
        }
        cursor.close();
        return map;
    }
}
