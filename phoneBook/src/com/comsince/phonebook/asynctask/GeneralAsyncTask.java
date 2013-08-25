package com.comsince.phonebook.asynctask;

import java.io.File;
import java.io.InputStream;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.activity.GeneralLoadingActivity;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.preference.PhoneBookPreference;
import com.comsince.phonebook.util.AndroidUtil;
import com.comsince.phonebook.util.BaiduCloudSaveUtil;
import com.comsince.phonebook.util.DataUtil;
import com.comsince.phonebook.util.FileUtil;
import com.comsince.phonebook.util.SimpleXmlReaderUtil;

import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class GeneralAsyncTask extends AsyncTask<String, Void, Boolean> {
    private String loadingText;
    private int taskTag;
    private Context context;
    
	public GeneralAsyncTask(String loadingText, int taskTag, Context context) {
		this.context = context;
		this.loadingText = loadingText;
		this.taskTag = taskTag;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		//关闭进度框
		Log.d("text", String.valueOf(result));
		if(!result){
			Toast.makeText(context, "没有您的数据，请先编辑个人信息上传", Toast.LENGTH_SHORT).show();
		}
		context.sendBroadcast(new Intent(Constant.ACTION_FINISH));
	}

	@Override
	protected void onPreExecute() {
		Intent intent = new Intent();
		intent.putExtra("showText", loadingText);
		intent.setClass(context, GeneralLoadingActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected Boolean doInBackground(String... params) {
		Boolean flag = false;
		if(taskTag == Constant.TASK_UPLOAD){
			Log.d("text", String.valueOf(taskTag));
			//password在保持的时候就已经加密了
			String passWord = PhoneBookApplication.phoneBookPreference.getPassWord(context);
			String fileName = PhoneBookApplication.phoneBookPreference.getUserName(context)+"_"+passWord;
			String uploadURL = BaiduCloudSaveUtil.generateUrl(Constant.PHONE_BOOK_PATH, "/"+Constant.DIR_PERSON_INFO+"/"+fileName+".xml");
			String uploadXmlPath = AndroidUtil.getSDCardRoot()+Constant.PHONE_BOOK_PATH+File.separator+Constant.DIR_PERSON_INFO+File.separator+fileName+".xml";
			String responeMsg = BaiduCloudSaveUtil.putObject(uploadURL, uploadXmlPath);
			Log.d("text", uploadURL);
			Log.d("text", responeMsg);
			if(responeMsg.equals("OK")){
				flag = true;
			}else{
				flag = false;
			}
		}else if(taskTag == Constant.TASK_DOWNLOAD){
			String passWord = PhoneBookApplication.phoneBookPreference.getPassWord(context);
			String fileName = PhoneBookApplication.phoneBookPreference.getUserName(context)+"_"+passWord;
			//注意请求方式
			String downloadURL = BaiduCloudSaveUtil.generateUrlForGet(Constant.PHONE_BOOK_PATH, "/"+Constant.DIR_PERSON_INFO+"/"+fileName+".xml");
			InputStream in = BaiduCloudSaveUtil.getObject(downloadURL);
			if(in != null){
				flag = true;
				FileUtil.write2SDFromInput(Constant.PHONE_BOOK_PATH+"/"+Constant.DIR_PERSON_INFO, fileName+".xml", in);
			}else{
				flag = false;
			}
		}
		return flag;
	}

}
