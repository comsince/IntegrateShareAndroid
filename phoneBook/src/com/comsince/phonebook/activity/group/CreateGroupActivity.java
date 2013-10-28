package com.comsince.phonebook.activity.group;

import com.comsince.phonebook.R;
import com.comsince.phonebook.activity.AddInfoDialogActivity;
import com.comsince.phonebook.activity.PersonInfoActivity;
import com.comsince.phonebook.activity.dialog.FunctionSelectDialog;
import com.comsince.phonebook.constant.ActivityForResultUtil;
import com.comsince.phonebook.util.T;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateGroupActivity extends BaseCreateGroup {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.group_back:
			finish();
			break;
		case R.id.create_group_btn:
			break;
		case R.id.group_avatar_change:
			if(TextUtils.isEmpty(tvGroupTag.getText().toString().trim())){
				T.showLong(mContext, "请先填写群组标签");
			}else{
				functionSelection();
			}
			break;
		case R.id.group_name_editable_box:
			startToAddInfoDialog("群名称",tvGroupName,REQUEST_GROUP_NAME);
			break;
		case R.id.group_tag_editable_box:
			startToAddInfoDialog("群标签", tvGroupTag, REQUEST_GROUP_TAG	);
			break;
		case R.id.group_remark_editable_box:
			startToAddInfoDialog("群说明", tvGroupRemark, REQUEST_GROUP_REMARK);
			break;
		case R.id.group_reigon_editable_box:
			startToAddInfoDialog("群区域", tvGroupRegion, REQUEST_GROUP_REGION);
			break;
		}
	}

	@Override
	protected void initView() {
		RgroupName = (RelativeLayout) findViewById(R.id.group_name_editable_box);
		RgroupTag = (RelativeLayout) findViewById(R.id.group_tag_editable_box);
		RgroupRegion = (RelativeLayout) findViewById(R.id.group_reigon_editable_box);
		Rgroupremark = (RelativeLayout) findViewById(R.id.group_remark_editable_box);
		
		tvGroupName = (TextView) findViewById(R.id.group_name_editable_tv);
		tvGroupTag = (TextView) findViewById(R.id.group_tag_editable_tv);
		tvGroupRegion = (TextView) findViewById(R.id.group_reigon_editable_tv);
		tvGroupRemark = (TextView) findViewById(R.id.group_remark_editable_tv);
		
		btnBack = (Button) findViewById(R.id.group_back);
		btnCreateGroup = (Button) findViewById(R.id.create_group_btn);
		btnGroupAvatarChange = (Button) findViewById(R.id.group_avatar_change);
		groupCardAvatar = (ImageButton) findViewById(R.id.group_avatar);
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void setUpListener() {
		RgroupName.setOnClickListener(this);
		RgroupTag.setOnClickListener(this);
		RgroupRegion.setOnClickListener(this);
		Rgroupremark.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnCreateGroup.setOnClickListener(this);
		btnGroupAvatarChange.setOnClickListener(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == AddInfoDialogActivity.ADD_INFO_COMMIT){
			if(requestCode == REQUEST_GROUP_NAME){
				tvGroupName.setText(data.getAction());
			}else if(requestCode == REQUEST_GROUP_TAG){
				tvGroupTag.setText(data.getAction());
			}else if(requestCode == REQUEST_GROUP_REMARK){
				tvGroupRemark.setText(data.getAction());
			}else if(requestCode == REQUEST_GROUP_REGION){
				tvGroupRegion.setText(data.getAction());
			}
		}else if(requestCode == PersonInfoActivity.REQUEST_PERSON_AVATER){
			if(resultCode == FunctionSelectDialog.RESULT_FUNCTION_ONE){
				//从手机相册中上传
				upLoadFromAlbums();
			}else if(resultCode == FunctionSelectDialog.RESULT_FUNCTION_TWO){
				//拍照 上传
				takePhotoUpLoad();
			}
		}else if(requestCode == ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION){
			Uri uri = null;
			if (data == null) {
				Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
				return;
			}
			if (resultCode == RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					Toast.makeText(this, "SD不可用", Toast.LENGTH_SHORT).show();
					return;
				}
				uri = data.getData();
				startPhotoZoom(uri);
			} else {
				Toast.makeText(this, "照片获取失败", Toast.LENGTH_SHORT).show();
			}
		}else if(requestCode == ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CROP){
			if (data == null) {
				Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
				return;
			} else {
				saveCropPhoto(data);
			}
		}else if(requestCode == ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CAMERA){
			if (resultCode == RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					Toast.makeText(this, R.string.sd_noexit, Toast.LENGTH_SHORT).show();
					return;
				}
				startPhotoZoom(Uri.fromFile(cameraFile));
			} else {
				Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
