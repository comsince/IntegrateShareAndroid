package com.ichliebephone.c2dm;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AndroidC2DMDemo extends Activity {
    /** Called when the activity is first created. */
	private static final String TAG = "AndroidC2DMDemo";
	public static final String SENDER_ID = "android.c2dm.demo@gmail.com"; //使用C2DM服务的用户账户
	public static final String MESSAGE_KEY_ONE = "msg";   //和服务器商量好的接收消息的键值key
	//update
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //
        Log.v(TAG, "Start");
        if(C2DMRegistration.getRegistrationID(this).equals("")){
	        //如果本地没有保存registration_id值，则向C2DM服务器注册
        	Log.v(TAG, "Register C2DM");
	        C2DMRegistration.register(this, SENDER_ID);
        }else{
        	//否则说明已经向C2DM服务器注册过了
        	Log.v(TAG, "C2DM registered");
        }
    }
    //测试主干恢复
    //河滨分支
    

}
