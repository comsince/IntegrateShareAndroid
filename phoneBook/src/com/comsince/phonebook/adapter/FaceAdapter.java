package com.comsince.phonebook.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FaceAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private int currentPage = 0;
	private Map<String, Integer> mFaceMap;
	private List<Integer> faceList = new ArrayList<Integer>();

	public FaceAdapter(Context context, int currentPage) {
		this.inflater = LayoutInflater.from(context);
		this.currentPage = currentPage;
		mFaceMap = PhoneBookApplication.getInstance().getFaceMap();
		initData();
	}

	private void initData() {
		for (Map.Entry<String, Integer> entry : mFaceMap.entrySet()) {
			faceList.add(entry.getValue());
		}
	}

	@Override
	public int getCount() {
		return PhoneBookApplication.NUM + 1;
	}

	@Override
	public Object getItem(int position) {
		return faceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.face, null, false);
			viewHolder.faceIV = (ImageView) convertView.findViewById(R.id.face_iv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (position == PhoneBookApplication.NUM) {
			viewHolder.faceIV.setImageResource(R.drawable.emotion_del_selector);
			viewHolder.faceIV.setBackgroundDrawable(null);
		} else {
			int count = PhoneBookApplication.NUM * currentPage + position;
			if (count < 107) {
				viewHolder.faceIV.setImageResource(faceList.get(count));
			} else {
				viewHolder.faceIV.setImageDrawable(null);
			}
		}
		return convertView;
	}

	public static class ViewHolder {
		ImageView faceIV;
	}
}
