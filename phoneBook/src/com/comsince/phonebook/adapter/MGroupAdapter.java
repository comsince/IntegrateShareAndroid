package com.comsince.phonebook.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.entity.Group;
import com.comsince.phonebook.util.L;
import com.comsince.phonebook.util.PhoneBookUtil;
import com.comsince.phonebook.view.smartimagview.SmartImageView;

public class MGroupAdapter extends BaseAdapter {
	private List<Group> mGroupResult;
	private LayoutInflater inflater;
	private int nowCommingMsgPosition = -1;
	private String nowCommingMsgGroupTag = "";
	private int msgTag = 0;
	private ListView groupListView;
	public static final int NEW_MESSAGE = 1;
	public static final int CLEAR_MESSAGE = 2;
	private Map<String,Boolean> msgCommingMap = new HashMap<String,Boolean>();
	
	public MGroupAdapter(Context context,List<Group> mGroupResult){
		inflater = LayoutInflater.from(context);
		if(mGroupResult != null){
			this.mGroupResult = mGroupResult;
			//初始化所有的群组都无消息
			for(Group group : mGroupResult){
				msgCommingMap.put(group.getGroupTag(), false);
			}
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
		msgCommingMap.put(groupTag, true);
		notifyDataSetChanged();
	}
	
	/**
	 * 点击消除消息提示,新消息来临
	 * */
	public void refreshComingMsg(String groupTag,int msgtag){
		nowCommingMsgGroupTag = groupTag;
		msgTag = msgtag;
		if(msgTag == NEW_MESSAGE){
			msgCommingMap.put(groupTag, true);
		}else if(msgTag == CLEAR_MESSAGE){
			msgCommingMap.put(groupTag, false);
		}
		notifyDataSetChanged();
	}
	
	public void setListview(ListView listView){
		this.groupListView = listView;
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
			convertView.setTag(R.drawable.phonebook + position,holder);
			L.i("mgroupAdapter setposition :"+String.valueOf(position));
		} else {
			holder = (ViewHolder) convertView.getTag(R.drawable.phonebook + position);
			L.i("mgroupAdapter getposition :"+String.valueOf(position));
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
		
		int isVisable = holder.msgNew.getVisibility();
		L.i("mgroupAdapter isvisable :"+String.valueOf(isVisable));
		/*if(groupTag.equals(nowCommingMsgGroupTag) && View.GONE == isVisable && msgTag == NEW_MESSAGE){
			holder.msgNew.setVisibility(View.VISIBLE);
			msgTag = 0;
		}
		
		if(groupTag.equals(nowCommingMsgGroupTag) && msgTag == CLEAR_MESSAGE){
			holder.msgNew.setVisibility(View.GONE);
			msgTag = 0;
		}*/
		
		if(msgCommingMap.get(groupTag)){
			holder.msgNew.setVisibility(View.VISIBLE);
		}else{
			holder.msgNew.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	/**可见更新，仅当可见的时候更新view
	 * **/
	private void upDateView(){
		int i = 0;
		for(Group group : mGroupResult){
			if(group.getGroupTag().equals(nowCommingMsgGroupTag)){
				break;
			}
			i++;
		}
		groupListView.getChildAt(0);
		ViewHolder viewHolder = (ViewHolder) groupListView.getTag(R.drawable.phonebook + i);
		viewHolder.msgNew.setVisibility(View.VISIBLE);
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
