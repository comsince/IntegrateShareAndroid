package com.comsince.knowledge.layout;

import com.comsince.knowledge.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FavoriteLayout extends RelativeLayout {
	private View rootView;
	private Context context;
	private LayoutInflater inflater;

	public FavoriteLayout(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		rootView = inflater.inflate(R.layout.favoritegroup, this, true);
	}

}
