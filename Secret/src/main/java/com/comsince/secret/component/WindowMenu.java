package com.comsince.secret.component;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.PopupWindow;

import com.comsince.secret.R;
import com.comsince.secret.ui.MMMyActivity;
import com.comsince.secret.ui.MMPublishActivity;
import com.comsince.secret.ui.SettingCenterActivity;


public class WindowMenu{

	Activity context;
	PopupWindow popupWindow;
	public WindowMenu(Activity c) {
		context =c;
		View pupview = LayoutInflater.from(context).inflate( R.layout.pupwindow_menu, null);
		 pupview.setFocusableInTouchMode(true);
		 ((Button) pupview.findViewById(R.id.exit_button)).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					/*popupWindow.dismiss();
					Intent intent = new Intent(context,UserRegisterActivity.class);  
					context.startActivity(intent);
					context.finish();*/
					int i=1/0;
				}});
		 ((Button) pupview.findViewById(R.id.menu_cell_setting)).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(context,SettingCenterActivity.class);
					context.startActivity(intent);
				}});
		 ((Button) pupview.findViewById(R.id.menu_cell_my)).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					Intent aintent = new Intent(context,MMMyActivity.class);
					context. startActivity(aintent);
				}});
		 ((Button) pupview.findViewById(R.id.menu_cell_publish)).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(context,MMPublishActivity.class);
					context.startActivity(intent);
				}});
		 
		 
		 pupview.setOnKeyListener(new OnKeyListener()
		 {

			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				 if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_MENU || event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
					   if(popupWindow.isShowing())
						{
							popupWindow.dismiss();
						}
                  return true;
                }
                return false;
			}
			 
		 });
		
		 popupWindow = new PopupWindow(pupview,android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT,true);  
		 popupWindow.setBackgroundDrawable(new BitmapDrawable());
		 popupWindow.setAnimationStyle(R.style.menuAnimation);
		 popupWindow.setOutsideTouchable(true);  
	}
	 
	public PopupWindow getPopupWindow()
	{
		return popupWindow;
	}
}
