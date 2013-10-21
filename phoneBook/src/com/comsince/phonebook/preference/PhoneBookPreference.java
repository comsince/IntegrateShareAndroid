package com.comsince.phonebook.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class PhoneBookPreference {
   public static final String MESSAGE_SOUND_KEY = "message_sound";
   SharedPreferences phoneBookPreference;
   public PhoneBookPreference(Context context){
	   phoneBookPreference = context.getSharedPreferences("phoneBook", Context.MODE_PRIVATE);
   }
   
   /**
    * 保存登陸用戶名
    * */
   public void saveUserName(Context context,String userName){
	   phoneBookPreference.edit().putString("userName", userName).commit();
   }
   public String getUserName(Context context){
	   return phoneBookPreference.getString("userName", "admin");
   }
   public String getUserName(){
	   return phoneBookPreference.getString("userName", "admin");
   }
   /**
    * 保存用戶手機號
    * */
   public void savePhoneNumber(Context context,String phoneNumber){
	   phoneBookPreference.edit().putString("phoneNumber", phoneNumber).commit();
   }
   public String getPhoneNumber(Context context){
	  return phoneBookPreference.getString("phoneNumber", "12345678900");
   }
   /**
    * 保存密码
    * */
   public void savePassWord(Context context,String passWord){
	   phoneBookPreference.edit().putString("passWord", passWord).commit();
   }
   public String getPassWord(Context context){
	   return phoneBookPreference.getString("passWord", "admin");
   }
   
   /**
    * set appid
    * */
   public void saveAppId(String appId){
	   phoneBookPreference.edit().putString("appId", appId).commit();
   }
   
   public String getAppId(){
	   return phoneBookPreference.getString("appId", "appId");
   }
   
   /**
    * 保存channelId
    * **/
   public void saveChannelId(String channelId){
	   phoneBookPreference.edit().putString("channelId", channelId).commit();
   }
   
   public String getChannelId(){
	   return phoneBookPreference.getString("channelId", "channelId");
   }
   /**
    * 
    * **/
   public void saveUserId(String userId){
	   phoneBookPreference.edit().putString("userId", userId).commit();
   }  
   
   public String getUserId(){
	   return phoneBookPreference.getString("userId", "userId");
   }
   
    // 新消息是否有声音
	public boolean getMsgSound() {
		return phoneBookPreference.getBoolean(MESSAGE_SOUND_KEY, true);
	}
   
}
