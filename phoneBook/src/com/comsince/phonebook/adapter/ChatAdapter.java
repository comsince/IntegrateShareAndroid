package com.comsince.phonebook.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.comsince.phonebook.R;
import com.comsince.phonebook.entity.MessageItem;
import com.comsince.phonebook.util.TimeUtil;

public class ChatAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<MessageItem> mMsgList;

	public ChatAdapter(Context mContext, List<MessageItem> mMsgList) {
		this.mContext = mContext;
		this.mMsgList = mMsgList;
		mInflater = LayoutInflater.from(mContext);
	}
	
	public void upDateMsg(MessageItem msg) {
		mMsgList.add(msg);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mMsgList.size();
	}

	@Override
	public Object getItem(int position) {
		return mMsgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MessageItem item = mMsgList.get(position);
		boolean isComMsg = item.isComMeg();
		ViewHolder holder;
		if (convertView == null || convertView.getTag(R.drawable.ic_launcher + position) == null) {
			holder = new ViewHolder();
			if (isComMsg) {
				convertView = mInflater.inflate(R.layout.chat_item_left, null);
			} else {
				convertView = mInflater.inflate(R.layout.chat_item_right, null);
			}
			holder.head = (ImageView) convertView.findViewById(R.id.icon);
			holder.time = (TextView) convertView.findViewById(R.id.datetime);
			holder.msg = (TextView) convertView.findViewById(R.id.textView2);
			holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar1);
			convertView.setTag(R.drawable.ic_launcher + position);
		} else {
			holder = (ViewHolder) convertView.getTag(R.drawable.ic_launcher + position);
		}
		holder.time.setText(TimeUtil.getChatTime(item.getDate()));
		holder.time.setVisibility(View.VISIBLE);
		//holder.head.setBackgroundResource(PushApplication.heads[item.getHeadImg()]);
		if(isComMsg){
			holder.head.setBackgroundResource(R.drawable.phonebook);
		}else{
			holder.head.setBackgroundResource(R.drawable.chat_tool_camera);
		}
		/*if (!isComMsg && !mSpUtil.getShowHead()) {
			holder.head.setVisibility(View.GONE);
		}*/

		//holder.msg.setText(convertNormalStringToSpannableString(item.getMessage()), BufferType.SPANNABLE);
		holder.msg.setText(item.getMessage());
		holder.progressBar.setVisibility(View.GONE);
		holder.progressBar.setProgress(50);
		return convertView;
	}
	
	static class ViewHolder {
		ImageView head;
		TextView time;
		TextView msg;
		ImageView imageView;
		ProgressBar progressBar;
	}
	

}
