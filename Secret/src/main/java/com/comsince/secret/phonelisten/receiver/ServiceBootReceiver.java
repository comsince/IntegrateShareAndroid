package com.comsince.secret.phonelisten.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.comsince.secret.phonelisten.service.PhoneListenService;

/**
 * Created by liaojinlong on 14-11-10.
 */
public class ServiceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent findService = new Intent(context, PhoneListenService.class);
        context.startService(findService);
    }
}
