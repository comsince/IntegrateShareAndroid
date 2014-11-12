package com.comsince.secret.phonelisten.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import com.comsince.secret.phonelisten.observer.PhoneListenObserver;

/**
 * Created by liaojinlong on 14-11-10.
 */
public class PhoneListenService extends Service{
    public static final String TAG = "PhoneListenService";

    private ContentObserver mObserver;
    // 地图定位帮助类
    //private LocationHelper mLocationHelper;


    @Override
    public void onCreate() {
        super.onCreate();
        addObserver();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void addObserver() {
        Log.i(TAG, "add observer. ");
        ContentResolver resolver = getContentResolver();
        Handler handler = new PhoneListenObserver.SMSHandler(this);
        mObserver = new PhoneListenObserver(resolver, handler);
        //注册短信监听器
        resolver.registerContentObserver(PhoneListenObserver.smsUri, true, mObserver);
        //注册联系人监听器
        resolver.registerContentObserver(ContactsContract.Contacts.CONTENT_URI,true,mObserver);
    }

    @Override
    public void onDestroy() {
        this.getContentResolver().unregisterContentObserver(mObserver);
        super.onDestroy();
    }
}
