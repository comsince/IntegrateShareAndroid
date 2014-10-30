package com.comsince.secret.util;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;



public class GlobalExceptionListener implements UncaughtExceptionHandler {
    private static GlobalExceptionListener INSTANCE = new GlobalExceptionListener();
    private Activity activity;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private GlobalExceptionListener() {
    }

    public static GlobalExceptionListener getInstance() {
		return INSTANCE;
    }

    public void init(Activity ctx) {
    	activity = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}

       
    }

   
    private boolean handleException(Throwable ex) {
        ex.printStackTrace();
       // Intent indexIntent = new Intent();
		//indexIntent.setClass(activity, IndexUI.class);
		//activity.startActivity(indexIntent);
		activity.finish();
        return true;
    }
}