package com.comsince.secret.phonelisten.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

/**
 * Created by liaojinlong on 14-11-10.
 */
public class CallBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")){

        }else{

        }
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        switch (tManager.getCallState()){
            case TelephonyManager.CALL_STATE_RINGING:
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                break;
        }
    }
}
