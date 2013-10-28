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
import com.comsince.phonebook.activity.PersonInfoActivity;
import com.comsince.phonebook.activity.group.CreateGroupActivity;
import com.comsince.phonebook.adapter.MGroupAdapter;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.Group;
import com.comsince.phonebook.entity.Groups;
import com.comsince.phonebook.menu.MGroup;
import com.comsince.phonebook.util.AndroidUtil;
import com.comsince.phonebook.util.BaiduCloudSaveUtil;
import com.comsince.phonebook.util.FileUtil;
import com.comsince.phonebook.util.PhoneBookUtil;
import com.comsince.phonebook.util.SimpleXmlReaderUtil;
import com.comsince.phonebook.util.baidupush.BaiduPush;

public class GeneralAsyncTask extends AsyncTask<String, Void, Boolean> {
    private String loadingText;
    private int taskTag;
    private Context context;
    private Handler mGroupHandler;
    /**
     * 组标签
     * */
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
			Toast.makeText(context, loadingText+"任务失败", Toast.LENGTH_SHORT).show();
		}
		if(taskTag == Constant.TASK_UPLOAD){
			if(mGroupHandler != null){
				mGroupHandler.sendEmptyMessage(PersonInfoActivity.UPLOAD_PERSON_INFO_SUCCESS);
			}
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
			//password在保存的时候就已经加密了
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
		}else if(taskTag == Constant.TASK_UPLOAD_PRESON_AVATAR){
			String passWord = PhoneBookApplication.phoneBookPreference.getPassWord(context);
			String fileName = PhoneBookApplication.phoneBookPreference.getUserName(context)+"_"+passWord;
			String uploadURL = BaiduCloudSaveUtil.generateUrl(Constant.PHONE_BOOK_PATH, "/"+Constant.DIR_PERSON_AVATAR+"/"+fileName+".jpg");
			String uploadXmlPath = AndroidUtil.getSDCardRoot()+Constant.PHONE_BOOK_PATH+File.separator+Constant.DIR_PERSON_AVATAR+File.separator+fileName+".jpg";
			String responeMsg = BaiduCloudSaveUtil.putObject(uploadURL, uploadXmlPath);
			Log.d("text", uploadURL);
			Log.d("text", responeMsg);
			if(responeMsg.equals("OK")){
				flag = true;
			}else{
				flag = false;
			}
		}else if(taskTag == Constant.TASK_UPLOAD_GROUP_AVATAR){
			condition = params[0];
			String upLoadURL = BaiduCloudSaveUtil.generateUrl(Constant.PHONE_BOOK_PATH, "/"+Constant.DIR_GROUP_AVATAR+"/"+condition+".jpg");
			String uploadXmlPath = AndroidUtil.getSDCardRoot()+Constant.PHONE_BOOK_PATH+File.separator+Constant.DIR_GROUP_AVATAR+File.separator+condition+".jpg";
			String responeMsg = BaiduCloudSaveUtil.putObject(upLoadURL, uploadXmlPath);
			if(responeMsg.equals("OK")){
				flag = true;
			}else{
				flag = false;
			}
		}else if(taskTag == Constant.TASK_UPLOAD_GROUP_DETAIL){
			condition = params[0];
			String upLoadURL = BaiduCloudSaveUtil.generateUrl(Constant.PHONE_BOOK_PATH, "/"+condition+"/"+Constant.FILE_GROUP_INFO);
			String uploadXmlPath = AndroidUtil.getSDCardRoot()+Constant.PHONE_BOOK_PATH+File.separator+condition+File.separator+Constant.FILE_GROUP_INFO;
			String responeMsg = BaiduCloudSaveUtil.putObject(upLoadURL, uploadXmlPath);
			if(responeMsg.equals("OK")){
				flag = true;
				mGroupHandler.sendEmptyMessage(CreateGroupActivity.UPLOAD_GROUP_DETAIL_SUCCESS);
			}else{
				flag = false;
				mGroupHandler.sendEmptyMessage(CreateGroupActivity.UPLOAD_GROUP_DETAIL_FAIL);
			}
		}else if(taskTag == Constant.TASK_UPLOAD_GROUP_PERSON){
			condition = params[0];
			String upLoadURL = BaiduCloudSaveUtil.generateUrl(Constant.PHONE_BOOK_PATH, "/"+condition+"/"+Constant.FILE_GROUP_PERSON_XML);
			String uploadXmlPath = AndroidUtil.getSDCardRoot()+Constant.PHONE_BOOK_PATH+File.separator+condition+File.separator+Constant.FILE_GROUP_PERSON_XML;
			String responeMsg = BaiduCloudSaveUtil.putObject(upLoadURL, uploadXmlPath);
			if(responeMsg.equals("OK")){
				flag = true;
				mGroupHandler.sendEmptyMessage(CreateGroupActivity.UPLOAD_GROUP_PERSON_SUCCESS);
			}else{
				flag = false;
				mGroupHandler.sendEmptyMessage(CreateGroupActivity.UPLOAD_GROUP_PERSON_FAIL);
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
			String groupTag = params[0];
			String msg = "{\"title\":\""+title+"\",\"description\":\""+description+"\"}";
			BaiduPush.pushMsgToGroup(groupTag,msg);
			flag = true;
		}else if(taskTag == Constant.TASK_GET_GROUP_BY_TAG){
			flag = downLoadGroupInfo(condition);
		}else if(taskTag == Constant.TASK_DOWNLOAD_PERSON_GROUPPERSON){
			condition = params[0];
			String downloadURL = BaiduCloudSaveUtil.generateUrlForGet(Constant.PHONE_BOOK_PATH, "/"+condition+"/"+Constant.FILE_GROUP_PERSON_XML);
			InputStream in = BaiduCloudSaveUtil.getObject(downloadURL);
			if(in != null){
				flag = true;
				FileUtil.write2SDFromInput(Constant.PHONE_BOOK_PATH+"/"+condition, Constant.FILE_GROUP_PERSON_XML, in);
				mGroupHandler.sendEmptyMessage(Constant.DOWN_LOAD_GROUPPERSON_SUCCESS);
			}else{
				flag = false;
				mGroupHandler.sendEmptyMessage(Constant.DOWN_LOAD_GROUPPERSON_FAIL);
			}
		}else if(taskTag == Constant.TASK_UPLOAD_PERSON_GROUPPERSON){
			condition = params[0];
			String uploadURL = BaiduCloudSaveUtil.generateUrl(Constant.PHONE_BOOK_PATH, "/"+condition+"/"+Constant.FILE_GROUP_PERSON_XML);
			String uploadXmlPath = AndroidUtil.getSDCardRoot()+Constant.PHONE_BOOK_PATH+File.separator+condition+File.separator+Constant.FILE_GROUP_PERSON_XML;
			String responeMsg = BaiduCloudSaveUtil.putObject(uploadURL, uploadXmlPath);
			if(responeMsg.equals(Constant.SUCCESS_MSG)){
				flag = true;
				mGroupHandler.sendEmptyMessage(Constant.UPLAOD_GROUPPERSON_SUCCESS);
			}else{
				flag = false;
				mGroupHandler.sendEmptyMessage(Constant.UPLAOD_GROUPPERSON_SUCCESS);
			}
		}else if(taskTag == Constant.TASK_UPLOAD_PERSON_GROUPINFO){
			String uploadURL = BaiduCloudSaveUtil.generateUrl(Constant.PHONE_BOOK_PATH, "/"+Constant.DIR_PERSON_INFO+"/"+PhoneBookUtil.getPerosnGroupInfoFileName(context)+".xml");
			String uploadXmlPath = AndroidUtil.getSDCardRoot()+Constant.PHONE_BOOK_PATH+File.separator+Constant.DIR_PERSON_INFO+File.separator+PhoneBookUtil.getPerosnGroupInfoFileName(context)+".xml";
			String responeMsg = BaiduCloudSaveUtil.putObject(uploadURL, uploadXmlPath);
			if(responeMsg.equals(Constant.SUCCESS_MSG)){
				flag = true;
			}else{
				flag = false;
			}
		}else if(taskTag == Constant.TASK_DOWNLOAD_TARTGET_PERONINFO){
			condition = params[0];
			String fileName = condition.split("/")[2];
			String downloadURL = BaiduCloudSaveUtil.generateUrlForGet(Constant.PHONE_BOOK_PATH, condition);
			InputStream in = BaiduCloudSaveUtil.getObject(downloadURL);
			if(in != null){
				flag = true;
				FileUtil.write2SDFromInput(Constant.PHONE_BOOK_PATH+"/"+Constant.DIR_PERSON_INFO, fileName, in);
			}else{
				flag = false;
			}
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
