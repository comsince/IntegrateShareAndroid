package com.comsince.knowledge.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.comsince.knowledge.R;

public class StyleDialog extends Dialog implements android.view.View.OnClickListener{
	public StyleDialogListener listener;
	private TextView rename_create;
	private TextView rename_cancel;
	
	public interface StyleDialogListener{
		public void commit();
	}
	
	public void setListener(StyleDialogListener listener) {
		this.listener = listener;
	}

	public StyleDialog(Context context) {
		super(context,R.style.NewDialog);
		setContentView(R.layout.layout_search_pic_lyric);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		initView();
		setUpListener();
	}
	
	private void initView(){
		rename_create = (TextView) findViewById(R.id.rename_create);
		rename_cancel = (TextView) findViewById(R.id.rename_cancel);
	}
	
	private void setUpListener(){
		rename_cancel.setOnClickListener(this);
		rename_create.setOnClickListener(this);
	}
	
	public static StyleDialog getStyleDialog(Context context){
		StyleDialog dlg = new StyleDialog(context);
		return dlg;
	}

	@Override
	public void onClick(View v) {
      
		switch (v.getId()) {
		case R.id.rename_cancel:
			dismiss();
			break;
		case R.id.rename_create:
			if(listener != null){
				listener.commit();
			}
			dismiss();
			break;
		default:
			break;
		}
	}

}
