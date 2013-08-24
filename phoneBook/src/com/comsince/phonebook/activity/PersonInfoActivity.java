package com.comsince.phonebook.activity;

import com.comsince.phonebook.R;

import android.app.Activity;
import android.os.Bundle;
/**
 * 登陆用户个人信息页面，在这个页面中登陆用户需要填写自己的基本信息，并向服务器提交，只有
 * 提交个人信息的方能加入到群组中
 * */
public class PersonInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_edit_person_info);
	}

}
