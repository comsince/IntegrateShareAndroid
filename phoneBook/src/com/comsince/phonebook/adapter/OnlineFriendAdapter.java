package com.comsince.phonebook.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.comsince.phonebook.R;
import com.comsince.phonebook.entity.User;

public class OnlineFriendAdapter extends BaseAdapter{
	private List<User> mUser;
	private LayoutInflater inflater;
	public OnlineFriendAdapter(Context context,List<User> users){
		inflater = LayoutInflater.from(context);
		if(users != null){
			this.mUser = users;
		}
	}
	
	public void refreshData(List<User> users){
		if(users != null){
			this.mUser = users;
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return mUser.size();
	}

	@Override
	public Object getItem(int position) {
		return mUser.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.contact_list_item_for_buddy, null);
			holder = new ViewHolder();
			holder.niceName = (TextView) convertView.findViewById(R.id.contact_list_item_name);
			holder.userId = (TextView) convertView.findViewById(R.id.cpntact_list_item_state);
			//不要忘记设置tag
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		User user = mUser.get(position);
		holder.niceName.setText(user.getNick());
		holder.userId.setText(user.getUserId());
		return convertView;
	}
	
	class ViewHolder {
		TextView userId;
		TextView niceName;
	}

}
