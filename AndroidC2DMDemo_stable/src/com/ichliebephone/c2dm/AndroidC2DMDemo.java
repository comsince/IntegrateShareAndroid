package com.ichliebephone.c2dm;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ichliebephone.c2dm.util.AndroidUtils;

public class AndroidC2DMDemo extends Activity {
    /** Called when the activity is first created. */
	private static final String TAG = "AndroidC2DMDemo";
	public static final String SENDER_ID = "android.c2dm.demo@gmail.com"; //使用C2DM服务的用户账户
	public static final String MESSAGE_KEY_ONE = "msg";   //和服务器商量好的接收消息的键值key
	
	private Context context;
	private Button btnRegister;
	private Button btnSend;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnSend = (Button) findViewById(R.id.btn_send);
        
        Log.v(TAG, "Start");
        //向C2DM服务器注册
        //C2DMessaging.register(this, SENDER_ID);
        btnRegister.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                AndroidUtils.bindMPush(getApplicationContext());
            }
        });
        btnSend.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent("com.meizu.c2dm.intent.RECEIVE");
                intent.putExtra("name", "test");
                context.sendBroadcast(intent);
            }
        });
    }
    
    
}