package com.comsince.phonebook.asynctask;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.activity.GeneralLoadingActivity;
import com.comsince.phonebook.adapter.MGroupAdapter;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.Group;
import com.comsince.phonebook.entity.Groups;
import com.comsince.phonebook.menu.MGroup;
import com.comsince.phonebook.util.AndroidUtil;
import com.comsince.phonebook.util.BaiduCloudSaveUtil;
import com.comsince.phonebook.util.FileUtil;
import com.comsince.phonebook.util.SimpleXmlReaderUtil;
import com.comsince.phonebook.util.baidupush.BaiduPush;

public class GeneralAsyncTask extends AsyncTask<String, Void, Boolean> {
    private String loadingText;
    private int taskTag;
    private Context context;
    private Handler mGroupHandler;
    private String condition;
    
	public GeneralAsyncTask(String loadingText, int taskTag, Context context) {
		this.context = context;
		this.loadingText = loadingText;
		this.taskTag = taskTag;
	}
	
	public GeneralAsyncTask(String loadingText, int taskTag, Context context,Handler mGroupHandler){
		this.context = context;
		this.loadingText = loadingText;
		this.taskTag = taskTag;
		this.mGroupHandler = mGroupHandler;
	}
	
	public GeneralAsyncTask(String loadingText, int taskTag, String condition,Context context) {
		this.context = context;
		this.loadingText = loadingText;
		this.taskTag = taskTag;
		this.condition = condition;
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
		}else if(taskTag == Constant.TASK_DOWNLOAD_PERSON_GROUP_INFO){
			SimpleXmlReaderUtil xmlUtils = new SimpleXmlReaderUtil();
			String passWord = PhoneBookApplication.phoneBookPreference.getPassWord(context);
			String fileName = PhoneBookApplication.phoneBookPreference.getUserName(context)+"_"+passWord+"_"+Constant.FILE_GROUP_SUFFIX;
			String downloadURL = BaiduCloudSaveUtil.generateUrlForGet(Constant.PHONE_BOOK_PATH, "/"+Constant.DIR_PERSON_INFO+"/"+fileName+".xml");
			InputStream in = BaiduCloudSaveUtil.getObject(downloadURL);
			if(in != null){
				flag = true;
				FileUtil.write2SDFromInput(Constant.PHONE_BOOK_PATH+"/"+Constant.DIR_PERSON_INFO, fileName+".xml", in);
				mGroupHandler.sendEmptyMessage(MGroup.SUCCESS);
			}else{
				flag = false;
				mGroupHandler.sendEmptyMessage(MGroup.FAIL);
			}
		}else if(taskTag == Constant.TASK_SEND_NOTIFICATION_TO_GROUP){
			String title = PhoneBookApplication.phoneBookPreference.getUserName(context);
	        String description = context.getString(R.string.push_messge_content);
			String groupTag = "YC_ZG_PRIMARY";
			String msg = "{\"title\":\""+title+"\",\"description\":\""+description+"\"}";
			BaiduPush.pushMsgToGroup(groupTag,msg);
			flag = true;
		}else if(taskTag == Constant.TASK_GET_GROUP_BY_TAG){
			flag = downLoadGroupInfo(condition);
		}
		return flag;
	}
	
	/**
	 * 下载分组信息
	 * */
	public Boolean downLoadGroupInfo(String condition){
		Boolean flag = false;
		String downloadURL = BaiduCloudSaveUtil.generateUrlForGet(Constant.PHONE_BOOK_PATH, "/"+condition+"/"+Constant.FILE_GROUP_INFO);
		InputStream in = BaiduCloudSaveUtil.getObject(downloadURL);
		if(in != null){
			flag = true;
			FileUtil.write2SDFromInput(Constant.PHONE_BOOK_PATH+"/"+condition, Constant.FILE_GROUP_INFO, in);
		}else{
			flag = false;
		}
		return flag;
	}

}
