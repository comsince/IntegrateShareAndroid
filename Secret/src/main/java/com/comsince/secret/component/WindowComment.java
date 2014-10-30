package com.comsince.secret.component;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.comsince.secret.R;
import com.comsince.secret.bean.Comment;
import com.comsince.secret.bean.User;
import com.comsince.secret.service.HandlerThreads;
import com.comsince.secret.util.BaseContorl;
import com.comsince.secret.util.SqlliteHander;


public class WindowComment {

	Activity context;
	PopupWindow popupWindow;
	Handler publishHandler;
    String matterId;
	public WindowComment(Activity c, Handler handler,String id) {
		context = c;
		publishHandler = handler;
		matterId = id;
		final View pupview = LayoutInflater.from(context).inflate(
				R.layout.window_comment, null);
		pupview.setFocusableInTouchMode(true);
		pupview.findViewById(R.id.publishButton).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						   String content = ((TextView)pupview.findViewById(R.id.commentText)).getText().toString().trim();
							if(content.length() < 10)
							{
								BaseContorl.doShowHToask(context, "多说一点点吧!");
								return ;
							}
						   User user = SqlliteHander.getTnstantiation(context).queryUser();
						   Comment comment = new Comment();
						   comment.content=content;
						   comment.matterId=matterId;
						   comment.alias=user.alias;
						   comment.userId = user.userId;
						   new HandlerThreads.SendCommentThread(publishHandler,comment).start();
						   BaseContorl.doShowHToask(context,"正在回复......");
						   pupview.findViewById(R.id.publishButton).setEnabled(false);
						   context.findViewById(R.id.PROGRESS_BAR).setVisibility(View.VISIBLE);
						   context.findViewById(R.id.RIGHT_BUTTON).setVisibility(View.GONE);
					}
				});

		(pupview.findViewById(R.id.windowbg))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						pupview.findViewById(R.id.publishButton).setEnabled(true);
						context.findViewById(R.id.PROGRESS_BAR).setVisibility(View.GONE);
						context.findViewById(R.id.RIGHT_BUTTON).setVisibility(View.VISIBLE);
						popupWindow.dismiss();
					}
				});

		popupWindow = new PopupWindow(pupview, android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT, true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	    popupWindow.setAnimationStyle(R.style.menuAnimation);
		// 让导航菜单可以得到焦点
		popupWindow.setFocusable(true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
	}

	public PopupWindow getPopupWindow() {
		return popupWindow;
	}
}
