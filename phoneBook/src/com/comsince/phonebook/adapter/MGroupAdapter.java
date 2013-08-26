package com.comsince.phonebook.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comsince.phonebook.R;
import com.comsince.phonebook.entity.Group;

public class MGroupAdapter extends BaseAdapter {
	private List<Group> mGroupResult;
	private LayoutInflater inflater;
	public MGroupAdapter(Context context,List<Group> mGroupResult){
		inflater = LayoutInflater.from(context);
		if(mGroupResult != null){
			this.mGroupResult = mGroupResult;
		}
	}

	@Override
	public int getCount() {
		return mGroupResult.size();
	}

	@Override
	public Object getItem(int position) {
		return mGroupResult.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.friend_item, null);
			holder = new ViewHolder();
			holder.alpha = (TextView) convertView.findViewById(R.id.friends_item_alpha);
			holder.alpha_line = (ImageView) convertView.findViewById(R.id.friends_item_alpha_line);
			holder.avatar = (ImageView) convertView.findViewById(R.id.friends_item_avatar);
			holder.name = (TextView) convertView.findViewById(R.id.friends_item_name);
			holder.arrow = (ImageView) convertView.findViewById(R.id.friends_item_arrow);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.alpha.setVisibility(View.GONE);
		Group group = mGroupResult.get(position);
		holder.avatar.setBackgroundResource(R.drawable.phonebook);
		holder.name.setText(group.getGroupName());
		return convertView;
	}
	
	class ViewHolder {
		TextView alpha;
		ImageView alpha_line;
		ImageView avatar;
		TextView name;
		ImageView arrow;
	}

}
