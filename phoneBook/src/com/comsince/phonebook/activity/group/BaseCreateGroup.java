package com.comsince.phonebook.activity.group;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comsince.phonebook.R;
import com.comsince.phonebook.activity.AddInfoDialogActivity;
import com.comsince.phonebook.activity.PersonInfoActivity;
import com.comsince.phonebook.activity.dialog.FunctionSelectDialog;
import com.comsince.phonebook.asynctask.GeneralAsyncTask;
import com.comsince.phonebook.constant.ActivityForResultUtil;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.GroupInfo;
import com.comsince.phonebook.util.PhotoUtil;

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
	
	public static final int  REQUEST_GROUP_NAME = 0;
	public static final int  REQUEST_GROUP_TAG = 1;
	public static final int  REQUEST_GROUP_REMARK = 2;
	public static final int  REQUEST_GROUP_REGION = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
		mContext = this;
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
    }
	
}
