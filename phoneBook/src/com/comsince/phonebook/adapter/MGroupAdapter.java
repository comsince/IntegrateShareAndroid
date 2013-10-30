package com.comsince.phonebook.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.entity.Group;
import com.comsince.phonebook.util.PhoneBookUtil;
import com.comsince.phonebook.view.smartimagview.SmartImageView;

public class MGroupAdapter extends BaseAdapter {
	private List<Group> mGroupResult;
	private LayoutInflater inflater;
	private int nowCommingMsgPosition;
	private String nowCommingMsgGroupTag;
	public MGroupAdapter(Context context,List<Group> mGroupResult){
		inflater = LayoutInflater.from(context);
		if(mGroupResult != null){
			this.mGroupResult = mGroupResult;
		}
	}
	
	/**
	 * refresh data
	 * */
	public void refreshData(List<Group> groups){
		mGroupResult.clear();
		if(groups != null){
			mGroupResult = groups;
			this.notifyDataSetChanged();
		}
	}
	
	/**
	 * 新消息来临更新adapter
	 * **/
	public void refreshComingMsg(int position){
		nowCommingMsgPosition = position;
		notifyDataSetChanged();
	}
	
	public void refreshComingMsg(String groupTag){
		nowCommingMsgGroupTag = groupTag;
		notifyDataSetChanged();
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
		if (convertView == null || convertView.getTag(R.drawable.phonebook + position) == null) {
			convertView = inflater.inflate(R.layout.friend_item, null);
			holder = new ViewHolder();
			holder.alpha = (TextView) convertView.findViewById(R.id.friends_item_alpha);
			holder.alpha_line = (ImageView) convertView.findViewById(R.id.friends_item_alpha_line);
			holder.avatar = (SmartImageView) convertView.findViewById(R.id.friends_item_avatar);
			holder.name = (TextView) convertView.findViewById(R.id.friends_item_name);
			holder.arrow = (ImageView) convertView.findViewById(R.id.friends_item_arrow);
			holder.msgNew = (ImageView) convertView.findViewById(R.id.group_message_new);
			convertView.setTag(R.drawable.phonebook + position);
		} else {
			holder = (ViewHolder) convertView.getTag(R.drawable.phonebook + position);
		}
		holder.alpha.setVisibility(View.GONE);
		Group group = mGroupResult.get(position);
		String groupTag = group.getGroupTag();
		if(true){
			Bitmap bitmap = PhoneBookApplication.getInstance().getAvatarByUserInfoExceptMe(groupTag);
			if(bitmap != null){
				holder.avatar.setImageBitmap(bitmap);
			}else{
				holder.avatar.setImageUrl(PhoneBookUtil.getGroupAvatarWebUrl(groupTag), groupTag);
			}
		}
		holder.name.setText(group.getGroupName() + " [邀请码]："+groupTag);
		
		if(position == nowCommingMsgPosition){
			holder.msgNew.setVisibility(View.VISIBLE);
		}
		
		if(groupTag.equals(nowCommingMsgGroupTag)){
			holder.msgNew.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
	
	class ViewHolder {
		TextView alpha;
		ImageView alpha_line;
		SmartImageView avatar;
		TextView name;
		ImageView arrow;
		ImageView msgNew;
	}

}
