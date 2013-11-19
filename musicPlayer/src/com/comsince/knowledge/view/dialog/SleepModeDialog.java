package com.comsince.knowledge.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comsince.knowledge.R;

public class SleepModeDialog extends Dialog implements android.view.View.OnClickListener{
	private Context mContext;
	private EditText editTime;
	private TextView tvCommit,tvCancel;
	private SleepModeDialogListener dialogListener;
	
	public interface SleepModeDialogListener{
		public void commit(String time);
	}
	
	public void setSleepModeDialogListener(SleepModeDialogListener listener){
		this.dialogListener = listener;
	}

	public SleepModeDialog(Context context) {
		super(context,R.style.Theme_Light_FullScreenDialogAct);
		mContext = context;
		setContentView(R.layout.addinfo_dialog);
		initViews();
		initEvents();
		setCancelable(true);
		setCanceledOnTouchOutside(false);
	}
	
	public SleepModeDialog(Context context,SleepModeDialogListener listener) {
		super(context);
		mContext = context;
		dialogListener = listener;
		setContentView(R.layout.addinfo_dialog);
		initViews();
		initEvents();
		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}
	
	private void initViews(){
		editTime = (EditText) findViewById(R.id.add_info_input);
		tvCommit = (TextView) findViewById(R.id.add_info_commit);
		tvCancel = (TextView) findViewById(R.id.add_info_cancel);
	}
	
	private void initEvents(){
		tvCommit.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
	}
	
	private void commit(){
		String time = editTime.getText().toString().trim();
		if(TextUtils.isEmpty(time)){
			Toast.makeText(mContext, "请输入睡眠时间", Toast.LENGTH_SHORT).show();
		}else if(dialogListener != null){
			dialogListener.commit(time);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_info_commit:
			commit();
			break;
		case R.id.add_info_cancel:
			SleepModeDialog.this.dismiss();
			break;
		}
	}

}
