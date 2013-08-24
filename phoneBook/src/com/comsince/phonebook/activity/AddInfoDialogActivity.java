package com.comsince.phonebook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comsince.phonebook.R;

public class AddInfoDialogActivity extends Activity implements OnClickListener {
	public static final int ADD_INFO_COMMIT = 0;
	public static final int ADD_INFO_CANCEL = 1;
	String dialogTitle;
    private TextView title;
    private EditText infoInput;
    private TextView addInfoCommit,addInfoCancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.addinfo_dialog);
	    dialogTitle = getIntent().getStringExtra("titleName");
	    initView();
	    setOnClickListener();
	    
	}
	public void initView(){
		title = (TextView) findViewById(R.id.add_info_title);
		if(!TextUtils.isEmpty(dialogTitle)){
			title.setText("请输入"+dialogTitle);
		}
		infoInput = (EditText) findViewById(R.id.add_info_input);
		addInfoCommit = (TextView) findViewById(R.id.add_info_commit);
		addInfoCancel = (TextView) findViewById(R.id.add_info_cancel);
	}
	public void setOnClickListener(){
		addInfoCommit.setOnClickListener(this);
		addInfoCancel.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_info_commit:
			commitData();
			break;
		case R.id.add_info_cancel:
			setResult(ADD_INFO_CANCEL);
			finish();
			break;
		default:
			break;
		}
	}
	
	public void commitData(){
		String infoinput = infoInput.getText().toString().trim();
		if(TextUtils.isEmpty(infoinput)){
			Toast.makeText(this, "请输入"+dialogTitle+"信息", Toast.LENGTH_SHORT).show();
		}else{
			setResult(ADD_INFO_COMMIT, (new Intent()).setAction(infoinput));
			finish();
		}
	}

}
