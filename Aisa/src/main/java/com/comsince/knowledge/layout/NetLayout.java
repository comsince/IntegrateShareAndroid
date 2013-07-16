package com.comsince.knowledge.layout;

import com.comsince.knowledge.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class NetLayout extends LinearLayout {
	View rootview;
	ListView localistview;
	LayoutInflater inflater;
	Context context;

	public NetLayout(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		rootview = inflater.inflate(R.layout.netmusiclist, this, true);
	}

}
