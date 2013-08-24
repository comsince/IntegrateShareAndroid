package com.comsince.phonebook.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class PhoneBookPreference {
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
}
