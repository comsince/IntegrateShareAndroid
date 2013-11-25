package com.comsince.phonebook.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.entity.User;
import com.comsince.phonebook.util.L;
import com.comsince.phonebook.util.PhoneBookUtil;
import com.comsince.phonebook.view.smartimagview.SmartImageView;

public class OnlineFriendAdapter extends BaseAdapter{
	private List<User> mUser;
	private LayoutInflater inflater;
	private String nowCommingMsgUserId = "";
	private String nowCommingMs;
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
	
	public void refreshData(String userId,String msg){
		nowCommingMsgUserId = userId;
		nowCommingMs =  msg;
		notifyDataSetChanged();
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
		if(convertView == null || convertView.getTag(R.drawable.phonebook + position) == null){
			convertView = inflater.inflate(R.layout.contact_list_item_for_buddy, null);
			holder = new ViewHolder();
			holder.niceName = (TextView) convertView.findViewById(R.id.contact_list_item_name);
			holder.userId = (TextView) convertView.findViewById(R.id.cpntact_list_item_state);
			holder.avatarImage = (SmartImageView) convertView.findViewById(R.id.icon);
			//不要忘记设置tag
			convertView.setTag(R.drawable.phonebook + position,holder);
		}else {
			holder = (ViewHolder) convertView.getTag(R.drawable.phonebook + position);
		}
		User user = mUser.get(position);
		holder.niceName.setText(user.getNick());
		//holder.userId.setText(user.getUserId());
		holder.userId.setText(user.getMsg());
		if(user.getUserId().equals(PhoneBookApplication.getInstance().phoneBookPreference.getUserId())){
			String username = user.getUserAvatarName();
			holder.avatarImage.setImageBitmap(PhoneBookApplication.getInstance().getAvatarByUserInfo(username));
		}else{
			L.i("friendadapter"+user.getUserAvatarName());
			String userAvatarName = user.getUserAvatarName();
			Bitmap avatarBitmap = PhoneBookApplication.getInstance().getAvatarByUserInfoExceptMe(userAvatarName);
			L.i("friendadapter avatarBitmap: "+userAvatarName+avatarBitmap);
			if(avatarBitmap == null){
				holder.avatarImage.setImageUrl(PhoneBookUtil.getJpgFileWebUrlByFileName(user.getUserAvatarName()),userAvatarName);
			}else{
				holder.avatarImage.setImageBitmap(avatarBitmap);
			}
		}
		//holder.avatarImage.setImageBitmap(PhoneBookApplication.bitmap_s);
		String userId = user.getUserId();
		if(userId.equals(nowCommingMsgUserId)){
			holder.userId.setText(nowCommingMs);
		}
		
		return convertView;
	}
	
	class ViewHolder {
		TextView userId;
		TextView niceName;
		SmartImageView avatarImage;
	}
	
}
