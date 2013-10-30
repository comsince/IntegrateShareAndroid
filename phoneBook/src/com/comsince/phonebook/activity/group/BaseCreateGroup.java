package com.comsince.phonebook.activity.group;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.activity.AddInfoDialogActivity;
import com.comsince.phonebook.activity.PersonInfoActivity;
import com.comsince.phonebook.activity.dialog.FunctionSelectDialog;
import com.comsince.phonebook.asynctask.GeneralAsyncTask;
import com.comsince.phonebook.constant.ActivityForResultUtil;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.Group;
import com.comsince.phonebook.entity.GroupInfo;
import com.comsince.phonebook.entity.GroupPerson;
import com.comsince.phonebook.entity.GroupPersons;
import com.comsince.phonebook.entity.Groups;
import com.comsince.phonebook.util.PhoneBookUtil;
import com.comsince.phonebook.util.PhotoUtil;
import com.comsince.phonebook.util.SimpleXmlReaderUtil;

public abstract class BaseCreateGroup extends Activity implements OnClickListener{
	protected RelativeLayout RgroupName;
	protected RelativeLayout RgroupTag;
	protected RelativeLayout RgroupRegion;
	protected RelativeLayout Rgroupremark;
	
	protected TextView tvGroupName;
	protected TextView tvGroupTag;
	protected TextView tvGroupRegion;
	protected TextView tvGroupRemark;
	
	protected Button btnCreateGroup;
	protected Button btnBack;
	protected Button btnGroupAvatarChange;
	protected ImageButton groupCardAvatar;
	
	protected File cameraFile;	//保存拍照后的图片文件
	protected GroupInfo groupInfo;
	
	protected GeneralAsyncTask groupTask;
	protected Context mContext;
	protected SimpleXmlReaderUtil xmlUtil;
	
	public static final int  REQUEST_GROUP_NAME = 0;
	public static final int  REQUEST_GROUP_TAG = 1;
	public static final int  REQUEST_GROUP_REMARK = 2;
	public static final int  REQUEST_GROUP_REGION = 3;
	
	public static final int UPLOAD_GROUP_DETAIL_SUCCESS = 500001;
	public static final int UPLOAD_GROUP_DETAIL_FAIL = 500002;
	
	public static final int UPLOAD_GROUP_PERSON_SUCCESS = 500003;
	public static final int UPLOAD_GROUP_PERSON_FAIL = 500004;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
		mContext = this;
		xmlUtil = new SimpleXmlReaderUtil();
		initView();
		initData();
		setUpListener();
	}
	
	protected abstract void initView();
	
	protected abstract void initData();
	
	protected abstract void setUpListener();
	
	/**
	 * 跳转到增加信息dialog中进行信息填写
	 * */
	public void startToAddInfoDialog(String titleName,TextView v,int requestCode){
		Intent intent = new Intent();
		String viewData = v.getText().toString().trim();
		if(!TextUtils.isEmpty(viewData)){
			intent.putExtra("viewData", viewData);
		}
		intent.putExtra("titleName", titleName);
		intent.setClass(this, AddInfoDialogActivity.class);
		startActivityForResult(intent, requestCode);
	}
	
	/**
	 * 弹出功能选择框
	 * */
	public void functionSelection(){
		Intent intent = new Intent();
		intent.putExtra("sfunction1", "从手机相册中选择");
		intent.putExtra("sfunction2", "拍照上传");
		intent.setClass(this, FunctionSelectDialog.class);
		startActivityForResult(intent, PersonInfoActivity.REQUEST_PERSON_AVATER);
	}
	
	/**
	 * 从手机相册上传
	 * */
	public void upLoadFromAlbums(){
		Intent intent = null;
		intent = new Intent(Intent.ACTION_PICK, null); 
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");  
		startActivityForResult(intent, ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION);
	}
	

	/**
	 * 拍照上传
	 * */
	public void takePhotoUpLoad(){
		Intent intent = null;
		intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File fileDir;
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			fileDir = new File(Environment.getExternalStorageDirectory()+File.separator+Constant.MAIN_DIR_PHONE_BOOK+File.separator+Constant.DIR_PERSON_AVATER); 
			if(!fileDir.exists()){  
				fileDir.mkdirs();  
			}  
		}else{
			Toast.makeText(this, R.string.sd_noexit, Toast.LENGTH_SHORT).show();
			return;
		}
		cameraFile = new File(fileDir.getAbsoluteFile()+"/"+System.currentTimeMillis()+".jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
		startActivityForResult(intent, ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CAMERA);
	}
	
	/**
	 * 系统裁剪照片
	 * 
	 * @param uri
	 */
	protected void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("scale", true);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent,ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CROP);
	}
	
	/**
	 * 保存裁剪的照片
	 * 
	 * @param data
	 */
	protected void saveCropPhoto(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			upLoadGroupAvatar(bitmap);
			//保存图片
			bitmap = PhotoUtil.toRoundCorner(bitmap, 15);
			if (bitmap != null) {
				groupCardAvatar.setImageBitmap(bitmap);
			}
			//上传群组头像
		} else {
			Toast.makeText(this, "获取裁剪照片错误", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 上传群组头像
	 * **/
	protected void upLoadGroupAvatar(Bitmap bitmap){
		String groupTag = tvGroupTag.getText().toString().trim();
		if(!TextUtils.isEmpty(groupTag)){
			PhotoUtil.savePhotoToSDCard(bitmap, groupTag, Constant.DIR_GROUP_AVATAR);
			groupTask = new GeneralAsyncTask(mContext.getString(R.string.group_info_avatar_upload), Constant.TASK_UPLOAD_GROUP_AVATAR, mContext);
			groupTask.execute(groupTag);
		}
	}
	
	/**
	 * 创建群组说明信息groupDetail.xml
	 * **/
    protected void sumitGroupInfo(){
    	groupInfo = new GroupInfo();
    	String groupName = tvGroupName.getText().toString().trim();
    	if(!TextUtils.isEmpty(groupName)){
    		groupInfo.setGroupName(groupName);
    	}
    	String groupTag = tvGroupTag.getText().toString().trim();
    	if(!TextUtils.isEmpty(groupTag)){
    		groupInfo.setGroupTag(groupTag);
    		String groupAvatar = File.separator + Constant.DIR_GROUP_AVATAR + File.separator + groupTag +".jpg";
    		groupInfo.setGroupAvatar(groupAvatar);
    	}
    	String groupRegion = tvGroupRegion.getText().toString().trim();
    	if(!TextUtils.isEmpty(groupRegion)){
    		groupInfo.setGroupRegion(groupRegion);
    	}
    	String groupRemark = tvGroupRemark.getText().toString().trim();
    	if(!TextUtils.isEmpty(groupRemark)){
    		groupInfo.setRemark(groupRemark);
    	}
    	String groupPath = Constant.MAIN_DIR_PHONE_BOOK +"/"+groupTag;
    	xmlUtil.writeXml(groupInfo, groupPath, Constant.FILE_GROUP_INFO_NO_SUFFIX);
    }
    
    /**
     * 初始化群组原始人员信息
     * **/
    
    protected void sumitGroupPerson(){
    	GroupPersons groupPersons = new GroupPersons();
		List<GroupPerson> groupPersonList = new ArrayList<GroupPerson>();
		GroupPerson groupPerson = new GroupPerson();
		String personAccount = PhoneBookApplication.phoneBookPreference.getUserName(mContext);
		groupPerson.setPersonAccount(personAccount);
		String personMd5password = PhoneBookApplication.phoneBookPreference.getPassWord(mContext);
		groupPerson.setPersonAccountPassword(personMd5password);
		groupPerson.setIsAuthor("Y");
		//设置个人信息的相对路径名
		groupPerson.setDetialInfoPath(PhoneBookUtil.getCurrentDetialInfoPath(mContext));
		String personRealName = PhoneBookUtil.getCurrrentPersonInfo(mContext).getName();
		if(!TextUtils.isEmpty(personRealName)){
			groupPerson.setPersonRealName(personRealName);
		}
		//获取当前最新的分组人员信息
		groupPersonList = PhoneBookUtil.getCurrentJoinGroupPersonInfo(mContext, groupInfo.getGroupTag());
		String username = PhoneBookApplication.phoneBookPreference.getUserName(mContext);
		String md5password = PhoneBookApplication.phoneBookPreference.getPassWord(mContext);
		if(groupPersonList.size() != 0){
			int i = 0;
			int size = groupPersonList.size();
			for(GroupPerson gp : groupPersonList){
				if(gp.getPersonAccount().equals(username)&&gp.getPersonAccountPassword().equals(md5password)){
					if(!TextUtils.isEmpty(personRealName)){
						gp.setPersonRealName(personRealName);
					}
					break;
				}
				i++;
			}
			if(i == size){
				groupPersonList.add(groupPerson);
			}
		}else{
			groupPersonList.add(groupPerson);
		}
		groupPersons.setGroupPersons(groupPersonList);
		PhoneBookUtil.writeGroupPersonToTargetGroup(groupInfo.getGroupTag(), groupPersons);
    }
    
    
    /**
     * 更新个人的分组信息
     * **/
    protected void submitPersonGroup(){
    	List<Group> groupsList = new ArrayList<Group>();
		Group group = new Group();
		String groupName = groupInfo.getGroupName();
		if(!TextUtils.isEmpty(groupName)){
			group.setGroupName(groupName);
		}
		String groupTag = groupInfo.getGroupTag();
		if(!TextUtils.isEmpty(groupTag)){
			group.setGroupTag(groupTag);
		}
		groupsList = PhoneBookUtil.getCurrentUserGroup(mContext);
		if(groupsList.size()!= 0){
			int i = 0;
			int size = groupsList.size();
			for(Group gr : groupsList){
				if(gr.getGroupName().equals(groupName) && gr.getGroupTag().equals(groupTag)){
					break;
				}
				if(gr.getGroupTag().equals(groupTag)){
					break;
				}
				i++;
			}
			if(i == size){
				groupsList.add(group);
			}
		}else{
			groupsList.add(group);
		}
		Groups groups = new Groups();
    	groups.setGroups(groupsList);
    	PhoneBookUtil.writePersonGroupInfo(groups, PhoneBookUtil.getPerosnGroupInfoFileName(mContext));
    }
    
    protected void createGroup(){
    	sumitGroupInfo();
    	sumitGroupPerson();
    	submitPersonGroup();
    }
    
    /**
     * 注册群组广播
     * **/
    protected void registerGroupTag(){
    	Intent intent = new Intent(Constant.ACTION_ADD_TAG);
		intent.putExtra("tag", groupInfo.getGroupTag());
		sendBroadcast(intent);
    }
    
    protected void createGroupUpLoadTask(String showtext,int taskTag,String param){
    	groupTask = new GeneralAsyncTask(showtext, taskTag, mContext,createGroupHandler);
    	if(param != null){
    		groupTask.execute(param);
    	}else{
    		groupTask.execute();
    	}
    	
    }
    
    
    protected Handler createGroupHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPLOAD_GROUP_DETAIL_SUCCESS:
				createGroupUpLoadTask(mContext.getString(R.string.group_info_person_upload), Constant.TASK_UPLOAD_GROUP_PERSON,groupInfo.getGroupTag());
				break;
			case UPLOAD_GROUP_DETAIL_FAIL:
				createGroupUpLoadTask(mContext.getString(R.string.group_info_person_upload), Constant.TASK_UPLOAD_GROUP_PERSON,groupInfo.getGroupTag());
				break;
			case UPLOAD_GROUP_PERSON_SUCCESS:
				createGroupUpLoadTask(mContext.getString(R.string.group_info_person_group_upload), Constant.TASK_UPLOAD_PERSON_GROUPINFO,null);
				break;
			case UPLOAD_GROUP_PERSON_FAIL:
				createGroupUpLoadTask(mContext.getString(R.string.group_info_person_group_upload), Constant.TASK_UPLOAD_PERSON_GROUPINFO,null);
				break;

			}
		}
    	
    };
	
}
