package com.comsince.secret.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.JetPlayer;

/**
 * Created by liaojinlong on 14-6-24.
 */
public class BaseSharePreferenceHelper {

    public Context context;

    /**
     * 获取执行name的preference
     * @param name
     * **/
    public SharedPreferences getSharePerferenceByName(String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    /**
     * 根据value类型设置相应类型的put方法
     * @param key
     * @param value
     * **/
    public void putValueBykey(String preferenceName,String key,Object value){
        SharedPreferences sharedPreferences = getSharePerferenceByName(preferenceName);
        if(value instanceof Boolean){
            sharedPreferences.edit().putBoolean(key,(Boolean)value).commit();
        }else if(value instanceof String){
            sharedPreferences.edit().putString(key,(String)value).commit();
        }
    }

    /**
     * 根据key获取相应的value
     * @param key
     * @return String
     * */
    public String getValueBykey(String preferenceName,String key,String defaultValue){
        SharedPreferences sharedPreferences = getSharePerferenceByName(preferenceName);
        return sharedPreferences.getString(key,defaultValue);
    }

}
