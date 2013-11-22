package com.comsince.knowledge.view.dialog;

import com.comsince.knowledge.R;

import android.app.Dialog;
import android.content.Context;

public class StyleDialog extends Dialog {

	public StyleDialog(Context context) {
		super(context,R.style.NewDialog);
		setContentView(R.layout.layout_search_pic_lyric);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}
	
	public static Dialog getStyleDialog(Context context){
		Dialog dlg = new StyleDialog(context);
		return dlg;
	}

}
