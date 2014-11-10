package com.comsince.secret.phonelisten.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by liaojinlong on 14-11-10.
 */
public class PhoneListenService extends Service{

    // 地图定位帮助类
    //private LocationHelper mLocationHelper;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
