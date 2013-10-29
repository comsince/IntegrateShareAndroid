package com.comsince.phonebook.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.asynctask.GeneralAsyncTask;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.Person;
import com.comsince.phonebook.entity.Phone;
import com.comsince.phonebook.util.AndroidUtil;
import com.comsince.phonebook.util.PhoneBookUtil;
import com.comsince.phonebook.view.smartimagview.SmartImageView;

public class FriendInfoActivity extends Activity {
	private Button mBack;
	private TextView mTitle;
	private Button mSubmit;
	private SmartImageView mAvatar;
	private Button mAvatarChange;
	private TextView mName;
	private TextView mSignature;
	private TextView mContactName,mContactAttribution,mContactNumber;
	private TextView mSex;
	private TextView mEmail;
	private TextView mBirthDay;
	private TextView mRegion;
	private TextView mMarriage;
	private TextView mTencentQQ;
	private TextView mMsn;
	
	private RelativeLayout mCall;
	private ImageButton mSendMsg;
	/**
	 * 用户资料
	 * */
	private Person mPerson;
	/**
	 * 用户网络资料相对路径
	 * */
	private String targetPersonInfoRelativePath;
	private String downLoadPersonName;
	private GeneralAsyncTask downLoadPersonInfo;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_person_info);
		context = this;
		downLoadPersonName = this.getIntent().getStringExtra("personName");
		targetPersonInfoRelativePath = this.getIntent().getStringExtra("tagerPersonInfo");
		findViewById();
		setListener();
		init();
		downLoadPersonInfo();
	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		initPersonAvatar();
		init();
	}



	public void findViewById(){
		mBack = (Button) findViewById(R.id.about_back);
		mTitle = (TextView) findViewById(R.id.about_title);
		mSubmit = (Button) findViewById(R.id.about_submit);
		mAvatar = (SmartImageView) findViewById(R.id.about_avatar);
		mAvatarChange = (Button) findViewById(R.id.about_avatar_change);
		mName = (TextView) findViewById(R.id.about_name);
		mSignature = (TextView) findViewById(R.id.about_signature);
		mContactName = (TextView) findViewById(R.id.detail_contact_name);
		mContactAttribution = (TextView) findViewById(R.id.detail_contact_attribution);
		mContactNumber = (TextView) findViewById(R.id.detail_contact_number);
        mSex = (TextView) findViewById(R.id.personal_card_sex_editable_tv);
		mEmail = (TextView) findViewById(R.id.personal_card_email_editable_tv);
		mBirthDay = (TextView) findViewById(R.id.personal_card_birthday_editable_tv);
		mMarriage = (TextView) findViewById(R.id.personal_card_marriage_editable_tv);
		mRegion = (TextView) findViewById(R.id.personal_card_reigon_editable_tv);
		mTencentQQ = (TextView) findViewById(R.id.personal_card_qq_editable_tv);
		mMsn = (TextView) findViewById(R.id.personal_card_msn_editable_tv);
		mCall = (RelativeLayout) findViewById(R.id.view_cdetail_call);
		mSendMsg = (ImageButton) findViewById(R.id.chatwith_contact);
	}
	
	public void setListener(){
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//调用Android系统API打电话
	            Uri uri = Uri.parse("tel:"+mContactNumber.getText().toString().trim());
	            Intent intent = new Intent(Intent.ACTION_CALL, uri);
	            startActivity(intent);
			}
		});
		mSendMsg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 //调用Android系统API发送短信
	            Uri uri = Uri.parse("smsto:"+mContactNumber.getText().toString().trim());
	            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
	            //intent.putExtra("sms_body", "android...");
	            startActivity(intent);
			}
		});
	}
	
	public void init(){
		mPerson = (Person) getIntent().getSerializableExtra("person");
		if(mPerson == null){
			mPerson = PhoneBookUtil.getPersonInfoByPath(targetPersonInfoRelativePath);
		}
		if(mPerson != null){
			if(!TextUtils.isEmpty(targetPersonInfoRelativePath)){
				mBack.setText("好友");
			}else{
				mBack.setText("好友");
			}
			mTitle.setText(mPerson.getName()+"个人资料");
			mSubmit.setText("提交");
			if(!TextUtils.isEmpty(mPerson.getName())){
				mName.setText(mPerson.getName());
			}
			if(!TextUtils.isEmpty(mPerson.getRemark())){
				mSignature.setText(mPerson.getRemark());
			}
			mContactName.setText("手机");
			if(mPerson.getPhonesList()!=null){
				Phone phone = mPerson.getPhonesList().get(0).getPhones().get(0);
				String attribution = phone.getAttribution();
				if(!TextUtils.isEmpty(attribution)){
					mContactAttribution.setText(attribution);
				}
				String contactNumber = phone.getNumber();
				if(!TextUtils.isEmpty(contactNumber)){
					mContactNumber.setText(contactNumber);
				}
			}
			if(!TextUtils.isEmpty(mPerson.getSex())){
				mSex.setText(mPerson.getSex());
			}
			if(!TextUtils.isEmpty(mPerson.getBirthDay())){
				mBirthDay.setText(mPerson.getBirthDay());
			}
			if(!TextUtils.isEmpty(mPerson.getReigon())){
				mRegion.setText(mPerson.getReigon());
			}
			if(!TextUtils.isEmpty(mPerson.getQq())){
				mTencentQQ.setText(mPerson.getQq());
			}
			if(!TextUtils.isEmpty(mPerson.getMarriage())){
				mMarriage.setText(mPerson.getMarriage());
			}
			if(!TextUtils.isEmpty(mPerson.getEmail())){
				mEmail.setText(mPerson.getEmail());
			}
			if(!TextUtils.isEmpty(mPerson.getMsn())){
				mMsn.setText(mPerson.getMsn());
			}
		}
	}
	
	
	public void downLoadPersonInfo(){
		if(AndroidUtil.getNetWorkStatus(context)&&!TextUtils.isEmpty(targetPersonInfoRelativePath)){
			String downInfo;
			if(TextUtils.isEmpty(downLoadPersonName)){
				downInfo = "正在下载个人信息...";
			}else{
				downInfo = "正在下载"+downLoadPersonName+"的信息...";
			}
			
			downLoadPersonInfo = new GeneralAsyncTask(downInfo, Constant.TASK_DOWNLOAD_TARTGET_PERONINFO, context);
			downLoadPersonInfo.execute(targetPersonInfoRelativePath);
		}
	}
	
	/**
	 * 加载个人头像
	 * **/
	public void initPersonAvatar(){
		if(!TextUtils.isEmpty(targetPersonInfoRelativePath)){
			String avatarName = PhoneBookUtil.getPersonAvatarNameByDetailInfoPath(targetPersonInfoRelativePath);
			Bitmap bitmap = PhoneBookApplication.getInstance().getAvatarByUserInfoExceptMe(avatarName);
			if(bitmap != null){
				mAvatar.setImageBitmap(bitmap);
			}else{
				mAvatar.setImageUrl(PhoneBookUtil.getJpgFileWebUrlByFileName(avatarName), avatarName);
			}
		}
	}

}
