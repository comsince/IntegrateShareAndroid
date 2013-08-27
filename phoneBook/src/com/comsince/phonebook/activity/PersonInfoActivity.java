package com.comsince.phonebook.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.asynctask.GeneralAsyncTask;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.Person;
import com.comsince.phonebook.entity.Phone;
import com.comsince.phonebook.entity.Phones;
import com.comsince.phonebook.preference.PhoneBookPreference;
import com.comsince.phonebook.uikit.MMAlert;
import com.comsince.phonebook.util.PhoneBookUtil;
import com.comsince.phonebook.util.SimpleXmlReaderUtil;

/**
 * 登陆用户个人信息页面，在这个页面中登陆用户需要填写自己的基本信息，并向服务器提交，只有 提交个人信息的方能加入到群组中
 * */
public class PersonInfoActivity extends Activity implements OnClickListener{
	private TextView personName;
	private TextView personPhone;
	private TextView personSex, personBirthDay, personRegion, personMarrige;
	private TextView personQQ, personEmail, PersonMSN;
	private Button btnBack , btnCommit;
	private TextView title;
	private RelativeLayout RpersonName;
	private RelativeLayout RpersonPhone;
	private RelativeLayout RpersonSex, RpersonBirthDay, RpersonRegion, RpersonMarrige;
	private RelativeLayout RpersonQQ, RpersonEmail, RPersonMSN;
	private static PhoneBookPreference phoneBookPreference;
	private Person person;
	private SimpleXmlReaderUtil xmlUtil;
	private Context context;
	//通用asyncTask
	GeneralAsyncTask generalAsyncTask;
	//发送群组标签
	ArrayList<String> tags = new ArrayList<String>();
	
	public static final int  REQUEST_PERSON_NAME = 0;
	public static final int  REQUEST_PHONE_NUMBER = 1;
	public static final int  REQUEST_PERSON_SEX = 2;
	public static final int  REQUEST_PERSON_BIRTHDAY = 3;
	public static final int  REQUEST_PERSON_REGION = 4;
	public static final int  REQUEST_PERSON_MARRIAGE = 5;
	public static final int  REQUEST_PERSON_QQ = 6;
	public static final int  REQUEST_PERSON_EMAIL = 7;
	public static final int  REQUEST_PERSON_MSN = 8;
	
	public static final int REQUEST_SELECT_GROUP = 9;
	
	private static final int MMAlertSelect_Save  =  0;
	private static final int MMAlertSelect_UpLoad  =  1;
	private static final int MMAlertSelect_Send  =  2;
	private static final int MMAlertSelect_DownLoad  =  3;
	
	private boolean isUpdateData = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		phoneBookPreference = PhoneBookApplication.phoneBookPreference;
		xmlUtil = new SimpleXmlReaderUtil();
		setContentView(R.layout.activity_layout_edit_person_info);
		initView();
		setUpListener();
		//如果放在resume中每次弹出的对话框都会重新调用此函数重新加载本地数据，导致无法显示修改的数据
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(isUpdateData){
			initData();
			isUpdateData = false;
		}
	}

	public void initView() {
		RpersonName = (RelativeLayout) findViewById(R.id.personal_card_name_box);
		RpersonPhone = (RelativeLayout) findViewById(R.id.personal_card_phone_box);
		RpersonSex = (RelativeLayout) findViewById(R.id.personal_card_sex_editable_box);
		RpersonBirthDay = (RelativeLayout) findViewById(R.id.personal_card_birthday_editable_box);
		RpersonRegion = (RelativeLayout) findViewById(R.id.personal_card_reigon_editable_box);
		RpersonMarrige = (RelativeLayout) findViewById(R.id.personal_card_marriage_editable_box);
		RpersonQQ = (RelativeLayout) findViewById(R.id.personal_card_qq_editable_box);
		RpersonEmail = (RelativeLayout) findViewById(R.id.personal_card_email_editable_box);
		RPersonMSN = (RelativeLayout) findViewById(R.id.personal_card_msn_editable_box);
		//textview
		personName = (TextView) findViewById(R.id.personal_card_name);
		personPhone = (TextView) findViewById(R.id.personal_card_phone);
		personSex = (TextView) findViewById(R.id.personal_card_sex_editable_tv);
		personBirthDay = (TextView) findViewById(R.id.personal_card_birthday_editable_tv);
		personRegion = (TextView) findViewById(R.id.personal_card_reigon_editable_tv);
		personMarrige = (TextView) findViewById(R.id.personal_card_marriage_editable_tv);
		personQQ = (TextView) findViewById(R.id.personal_card_qq_editable_tv);
		personEmail = (TextView) findViewById(R.id.personal_card_email_editable_tv);
		PersonMSN = (TextView) findViewById(R.id.personal_card_msn_editable_tv);
		title = (TextView) findViewById(R.id.about_title);
		title.setText("个人资料");
		btnBack = (Button) findViewById(R.id.about_back);
		btnCommit = (Button) findViewById(R.id.about_submit);
	}

	/**
	 * 初始化数据
	 * */
	public void initData() {
		// 先从本地加载文件
		String personInfoDir = PhoneBookUtil.getPersonInfoPath();
		String personInfoPath = personInfoDir + phoneBookPreference.getUserName(this) + "_" + phoneBookPreference.getPassWord(this) + ".xml";
		try {
			if(new File(personInfoPath).exists()){
				InputStream personIn = new FileInputStream(personInfoPath);
				person = xmlUtil.readXmlFromInputStream(personIn, Person.class);
				loadData();
			}else{
				Toast.makeText(this, "你还没有建立个人资料，或者你可以点击刷新从网络获取您的资料", Toast.LENGTH_SHORT).show();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setUpListener() {
        title.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
        RpersonName.setOnClickListener(this);
        RpersonPhone.setOnClickListener(this);
        RpersonSex.setOnClickListener(this);
		RpersonBirthDay.setOnClickListener(this);
		RpersonRegion.setOnClickListener(this);
		RpersonMarrige.setOnClickListener(this);
		RpersonQQ.setOnClickListener(this);
		RpersonEmail.setOnClickListener(this);
		RPersonMSN.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_back:
			finish();
			break;
		case R.id.about_submit:
			selectDialog();
			break;
		case R.id.about_title:
			break;
		case R.id.personal_card_name_box:
			startToAddInfoDialog("姓名",personName,REQUEST_PERSON_NAME);
			break;
		case R.id.personal_card_phone_box:
			startToAddInfoDialog("电话",personPhone,REQUEST_PHONE_NUMBER);
			break;
		case R.id.personal_card_sex_editable_box:
			startToAddInfoDialog("性别",personSex,REQUEST_PERSON_SEX);
			break;
		case R.id.personal_card_birthday_editable_box:
			startToAddInfoDialog("生日",personBirthDay,REQUEST_PERSON_BIRTHDAY);
			break;
		case R.id.personal_card_reigon_editable_box:
			startToAddInfoDialog("区域",personRegion,REQUEST_PERSON_REGION);
			break;
		case R.id.personal_card_marriage_editable_box:
			startToAddInfoDialog("婚姻",personMarrige,REQUEST_PERSON_MARRIAGE);
			break;
		case R.id.personal_card_qq_editable_box:
			startToAddInfoDialog("QQ",personQQ,REQUEST_PERSON_QQ);
			break;
		case R.id.personal_card_email_editable_box:
			startToAddInfoDialog("邮箱",personEmail,REQUEST_PERSON_EMAIL);
			break;
		case R.id.personal_card_msn_editable_box:
			startToAddInfoDialog("MSN",PersonMSN,REQUEST_PERSON_MSN);
			break;
		default:
			break;
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == AddInfoDialogActivity.ADD_INFO_COMMIT){
			if(requestCode == REQUEST_PERSON_NAME){
			    personName.setText(data.getAction());
			}else if(requestCode == REQUEST_PHONE_NUMBER){
				personPhone.setText(data.getAction());
			}else if(requestCode == REQUEST_PERSON_SEX){
				personSex.setText(data.getAction());
			}else if(requestCode == REQUEST_PERSON_BIRTHDAY){
				personBirthDay.setText(data.getAction());
			}else if(requestCode == REQUEST_PERSON_REGION){
				personRegion.setText(data.getAction());
			}else if(requestCode == REQUEST_PERSON_MARRIAGE){
				personMarrige.setText(data.getAction());
			}else if(requestCode == REQUEST_PERSON_QQ){
				personQQ.setText(data.getAction());
			}else if(requestCode == REQUEST_PERSON_EMAIL){
				personEmail.setText(data.getAction());
			}else if(requestCode == REQUEST_PERSON_MSN){
				PersonMSN.setText(data.getAction());
			}
		}else if(resultCode == SelectGroupDialogActivity.RESULT_SELECT_GROUP_SUCCESS){
			if(requestCode == REQUEST_SELECT_GROUP){
				tags = data.getStringArrayListExtra("tags");
				Log.i("test", "ssss");
				generalAsyncTask = new GeneralAsyncTask(context.getString(R.string.send_notification_to_group), Constant.TASK_SEND_NOTIFICATION_TO_GROUP, context);
				generalAsyncTask.execute(tags.get(0));
			}
		}
		
	}

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
	 *加载个人资料到控件中
	 * */
	public void loadData(){
		String realName = person.getName();
		if(!TextUtils.isEmpty(realName)){
			personName.setText(realName);
		}
		int phoneSize = person.getPhonesList().size();
		if(phoneSize > 0){
			String phoneNumber = person.getPhonesList().get(0).getPhones().get(0).getNumber();
			if(!TextUtils.isEmpty(phoneNumber)){
				personPhone.setText(phoneNumber);
			}
		}
		String sex = person.getSex();
		if(!TextUtils.isEmpty(sex)){
			personSex.setText(sex);
		}
		String birthDay = person.getBirthDay();
		if(!TextUtils.isEmpty(birthDay)){
			personBirthDay.setText(birthDay);
		}
		String region = person.getReigon();
		if(!TextUtils.isEmpty(region)){
			personRegion.setText(region);
		}
		String marriage = person.getMarriage();
		if(!TextUtils.isEmpty(marriage)){
			personMarrige.setText(marriage);
		}
		String qq = person.getQq();
		if(!TextUtils.isEmpty(qq)){
			personQQ.setText(qq);
		}
		String email = person.getEmail();
		if(!TextUtils.isEmpty(email)){
			personEmail.setText(email);
		}
		String msn = person.getMsn();
		if(!TextUtils.isEmpty(msn)){
			PersonMSN.setText(msn);
		}
	}
	
	/**
	 * 获取数据准备提交数据
	 * 1、将数据保存到本地
	 * 2、将数据上传到服务器
	 * **/
	public void subMitData(){
		person = new Person();
		String realName = personName.getText().toString().trim();
		if(!TextUtils.isEmpty(realName)){
			person.setName(realName);
		}
		String phoneNumber = personPhone.getText().toString().trim();
		if(!TextUtils.isEmpty(phoneNumber)){
			Phone phone = new Phone();
			List<Phone> phoneList = new ArrayList<Phone>();
			Phones phones = new Phones();
			List<Phones> phonesList = new ArrayList<Phones>();
			phone.setNumber(phoneNumber);
			phoneList.add(phone);
			phones.setPhones(phoneList);
			phonesList.add(phones);
			person.setPhonesList(phonesList);
		}
		String birthDay = personBirthDay.getText().toString().trim();
		if(!TextUtils.isEmpty(birthDay)){
			person.setBirthDay(birthDay);
		}
		String sex = personSex.getText().toString().trim();
		if(!TextUtils.isEmpty(sex)){
			person.setSex(sex);
		}
		String region = personRegion.getText().toString().trim();
		if(!TextUtils.isEmpty(region)){
			person.setReigon(region);
		}
		String marriage = personMarrige.getText().toString().trim();
		if(!TextUtils.isEmpty(marriage)){
			person.setMarriage(marriage);
		}
		String qq = personQQ.getText().toString().trim();
		if(!TextUtils.isEmpty(qq)){
			person.setQq(qq);
		}
		String email = personEmail.getText().toString().trim();
		if(!TextUtils.isEmpty(email)){
			person.setEmail(email);
		}
		String msn = PersonMSN.getText().toString().trim();
		if(!TextUtils.isEmpty(msn)){
			person.setMsn(msn);
		}
		
		String personInfoDir = Constant.MAIN_DIR_PHONE_BOOK +"/"+Constant.DIR_PERSON_INFO;
		String personInfoName = phoneBookPreference.getUserName(this) + "_" + phoneBookPreference.getPassWord(this);
		xmlUtil.writeXml(person,personInfoDir, personInfoName);
		Toast.makeText(this, "保存本地成功", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 弹出功能选择框
	 * */
	public void selectDialog(){
		MMAlert.showAlert(context, context.getString(R.string.select_info), context.getResources().getStringArray(R.array.select_item), null, new MMAlert.OnAlertSelectId(){

			@Override
			public void onClick(int whichButton) {
				switch (whichButton) {
				case MMAlertSelect_Save:
					subMitData();
					break;
				case MMAlertSelect_UpLoad:
					subMitData();
					//上传数据
					generalAsyncTask = new GeneralAsyncTask(context.getString(R.string.person_info_upload), Constant.TASK_UPLOAD, context);
					generalAsyncTask.execute();
					break;
				case MMAlertSelect_Send:
					Intent intent = new Intent();
					intent.setClass(context, SelectGroupDialogActivity.class);
					startActivityForResult(intent, REQUEST_SELECT_GROUP);
					break;
				case MMAlertSelect_DownLoad:
					//设置当下载数据成功，更新当前页面
					isUpdateData = true;
					generalAsyncTask = new GeneralAsyncTask(context.getString(R.string.person_info_download), Constant.TASK_DOWNLOAD, context);
					generalAsyncTask.execute();
					break;
				default:
					break;
				}
			}
			
		});
	}

}
