package com.comsince.phonebook.activity;


import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.comsince.phonebook.R;
import com.comsince.phonebook.entity.Person;
import com.comsince.phonebook.entity.Phone;

public class FriendInfoActivity extends Activity {
	private Button mBack;
	private TextView mTitle;
	private Button mSubmit;
	private ImageButton mAvatar;
	private Button mAvatarChange;
	private TextView mName;
	private TextView mSignature;
	private TextView mContactName,mContactAttribution,mContactNumber;
	private TextView mEmail;
	private TextView mBirthDay;
	private TextView mRegion;
	private TextView mMarriage;
	private TextView mTencentQQ;
	private TextView mMsn;
	/**
	 * 用户资料
	 * */
	private Person mPerson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_person_info);
		findViewById();
		setListener();
		init();
	}
	
	public void findViewById(){
		mBack = (Button) findViewById(R.id.about_back);
		mTitle = (TextView) findViewById(R.id.about_title);
		mSubmit = (Button) findViewById(R.id.about_submit);
		mAvatar = (ImageButton) findViewById(R.id.about_avatar);
		mAvatarChange = (Button) findViewById(R.id.about_avatar_change);
		mName = (TextView) findViewById(R.id.about_name);
		mSignature = (TextView) findViewById(R.id.about_signature);
		mContactName = (TextView) findViewById(R.id.detail_contact_name);
		mContactAttribution = (TextView) findViewById(R.id.detail_contact_attribution);
		mContactNumber = (TextView) findViewById(R.id.detail_contact_number);
		mEmail = (TextView) findViewById(R.id.personal_card_email_editable_tv);
		mBirthDay = (TextView) findViewById(R.id.personal_card_birthday_editable_tv);
		mMarriage = (TextView) findViewById(R.id.personal_card_marriage_editable_tv);
		mRegion = (TextView) findViewById(R.id.personal_card_reigon_editable_tv);
		mTencentQQ = (TextView) findViewById(R.id.personal_card_qq_editable_tv);
		mMsn = (TextView) findViewById(R.id.personal_card_msn_editable_tv);
	}
	
	public void setListener(){
		
	}
	
	public void init(){
		mPerson = (Person) getIntent().getSerializableExtra("person");
		if(mPerson != null){
			mBack.setText("好友");
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
			if(!TextUtils.isEmpty(mPerson.getReigon())){
				mRegion.setText(mPerson.getReigon());
			}
			if(!TextUtils.isEmpty(mPerson.getQq())){
				mTencentQQ.setText(mPerson.getQq());
			}
			if(!TextUtils.isEmpty(mPerson.getMarriage())){
				mMarriage.setText(mPerson.getMarriage());
			}
		}
	}

}
