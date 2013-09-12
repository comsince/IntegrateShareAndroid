package com.comsince.phonebook.activity.dialog;


import com.comsince.phonebook.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FunctionSelectDialog extends Activity {
	public final static int RESULT_FUNCTION_ONE = 100001;
	public final static int RESULT_FUNCTION_TWO = 100002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.functionselectlayout);
		String sfunction1 = this.getIntent().getStringExtra("sfunction1");
		String sfunction2 = this.getIntent().getStringExtra("sfunction2");
		TextView function1 = (TextView) findViewById(R.id.function1);
		TextView function2 = (TextView) findViewById(R.id.funtion2);
		RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.functionlayout1);
		RelativeLayout rl2 = (RelativeLayout) findViewById(R.id.functionlayout2);
		function1.setText(sfunction1);
		function2.setText(sfunction2);
		rl1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setResult(RESULT_FUNCTION_ONE);
				finish();
			}
			
		});
		rl2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setResult(RESULT_FUNCTION_TWO);
				finish();
			}
			
		});
		
	}
    
}
