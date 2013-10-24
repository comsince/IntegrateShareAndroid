package com.comsince.phonebook.adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.entity.MessageItem;
import com.comsince.phonebook.util.TimeUtil;

public class ChatAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<MessageItem> mMsgList;
	
	public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

	public ChatAdapter(Context mContext, List<MessageItem> mMsgList) {
		this.mContext = mContext;
		this.mMsgList = mMsgList;
		mInflater = LayoutInflater.from(mContext);
	}
	
	public void upDateMsg(MessageItem msg) {
		mMsgList.add(msg);
		notifyDataSetChanged();
	}
	
	public void refreshMsg(List<MessageItem> msgItems){
		if(msgItems != null){
			mMsgList = msgItems;
		}
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
		if (convertView == null || convertView.getTag(R.drawable.phonebook + position) == null) {
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
			convertView.setTag(R.drawable.phonebook + position);
		} else {
			holder = (ViewHolder) convertView.getTag(R.drawable.phonebook + position);
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

		holder.msg.setText(convertNormalStringToSpannableString(item.getMessage()), BufferType.SPANNABLE);
		//holder.msg.setText(item.getMessage());
		holder.progressBar.setVisibility(View.GONE);
		holder.progressBar.setProgress(50);
		return convertView;
	}
	
	/**
	 * 另外一种方法解析表情
	 * 
	 * @param message
	 *            传入的需要处理的String
	 * @return
	 */
	private CharSequence convertNormalStringToSpannableString(String message) {
		String hackTxt;
		if (message.startsWith("[") && message.endsWith("]")) {
			hackTxt = message + " ";
		} else {
			hackTxt = message;
		}
		SpannableString value = SpannableString.valueOf(hackTxt);
		Matcher localMatcher = EMOTION_URL.matcher(value);
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			int k = localMatcher.start();
			int m = localMatcher.end();
			if (m - k < 8) {
				if (PhoneBookApplication.getInstance().getFaceMap().containsKey(str2)) {
					int face = PhoneBookApplication.getInstance().getFaceMap().get(str2);
					Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), face);
					if (bitmap != null) {
						ImageSpan localImageSpan = new ImageSpan(mContext, bitmap, ImageSpan.ALIGN_BASELINE);
						value.setSpan(localImageSpan, k, m, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}
		return value;
	}
	
	static class ViewHolder {
		ImageView head;
		TextView time;
		TextView msg;
		ImageView imageView;
		ProgressBar progressBar;
	}
	

}
