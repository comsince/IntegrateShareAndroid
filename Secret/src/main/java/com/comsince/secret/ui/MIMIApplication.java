/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.comsince.secret.ui;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.util.Log;

import com.comsince.secret.bean.User;
import com.comsince.secret.common.Constant;
import com.comsince.secret.phonelisten.service.PhoneListenService;
import com.comsince.secret.util.SqlliteHander;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.HashMap;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class MIMIApplication extends Application {

    public static HashMap<String,String > contactMap = new HashMap<String,String >();

	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		if (Constant.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		super.onCreate();
        File dirs = new File(Constant.CallLogContants.CACHE_DIR);
        if (!dirs.exists()) {
            dirs.mkdir();
        }
        //读取联系人信息，启动手机监听服务
        loadContacts();
        Context context = this.getApplicationContext();
        Intent findService = new Intent(context, PhoneListenService.class);
        context.startService(findService);

		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them, 
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.enableLogging() // Not necessary in common
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

    public void  loadContacts()
    {
        if(!contactMap.isEmpty()){
            return ;
        }
        Log.w("CallBroadcastReceiver", "----------------开始读取联系人.........");
        Cursor cursor = this.getApplicationContext().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String hasPhone = cursor .getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (hasPhone.compareTo("1") == 0) {
                Cursor phones = getApplicationContext().getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = " + contactId, null, null);
                while (phones.moveToNext()) {
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactMap.put(phoneNumber, phoneName); // 多个号码如何处理
                }
                phones.close();
            }
        }
        cursor.close();
        Log.w("CallBroadcastReceiver","----------------读取联系人完成........."+contactMap.size());
    }

    public String  getLoginUserName(){
        User u = SqlliteHander.getTnstantiation(getApplicationContext()).queryUser();
        return u.alias;
    }
}