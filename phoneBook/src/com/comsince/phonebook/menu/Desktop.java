package com.comsince.phonebook.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comsince.phonebook.R;
import com.comsince.phonebook.activity.PersonInfoActivity;
import com.comsince.phonebook.util.ViewUtil;

public class Desktop {
	private Context mContext;
	private Activity mActivity;
	private ListView mDisplay;
    private RelativeLayout personInfo;
	private DesktopAdapter mAdapter;

	private View mDesktop;
	
	/**
	 * 接口对象,用来修改显示的View
	 */
	private onChangeViewListener mOnChangeViewListener;

	public Desktop(Context context, Activity activity) {
		mContext = context;
		mActivity = activity;

		mDesktop = LayoutInflater.from(context).inflate(R.layout.desktop, null);
		findViewById();
		setListener();
		init();
	}

	public void findViewById() {
		mDisplay = (ListView) mDesktop.findViewById(R.id.desktop_display);
		//点击该空间进入个人信息修改界面
		personInfo = (RelativeLayout) mDesktop.findViewById(R.id.desktop_top_layout);
	}

	public void setListener() {
        personInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, PersonInfoActivity.class);
				mContext.startActivity(intent);
			}
		});
	}

	public void init() {
		mAdapter = new DesktopAdapter(mContext);
		mDisplay.setAdapter(mAdapter);
	}

	public View getView() {
		return mDesktop;
	}
	
	/**
	 * 界面修改方法
	 * 
	 * @param onChangeViewListener
	 */
	public void setOnChangeViewListener(onChangeViewListener onChangeViewListener) {
		mOnChangeViewListener = onChangeViewListener;
	}
	
	/**
	 * 切换显示界面的接口
	 * 
	 * @author rendongwei
	 * 
	 */
	public interface onChangeViewListener {
		public abstract void onChangeView(int arg0);
	}

	public class DesktopAdapter extends BaseAdapter {
		private Context mContext;
		private String[] mName = { "好友", "消息" };
		private int[] mIcon = {R.drawable.sidebar_icon_friends, R.drawable.sidebar_icon_news };
		private int[] mIconPressed = { R.drawable.sidebar_icon_friends_pressed ,R.drawable.sidebar_icon_news_pressed };
		private int mChoose = 0;

		public DesktopAdapter(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return mName.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.desktop_item, null);
				holder = new ViewHolder();
				holder.layout = (LinearLayout) convertView.findViewById(R.id.desktop_item_layout);
				holder.icon = (ImageView) convertView.findViewById(R.id.desktop_item_icon);
				holder.name = (TextView) convertView.findViewById(R.id.desktop_item_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText(mName[position]);
			if (position == mChoose) {
				holder.name.setTextColor(Color.parseColor("#ffffffff"));
				holder.icon.setImageResource(mIconPressed[position]);
				holder.layout.setBackgroundColor(Color.parseColor("#20000000"));
			} else {
				holder.name.setTextColor(Color.parseColor("#7fffffff"));
				holder.icon.setImageResource(mIcon[position]);
				holder.layout.setBackgroundResource(Color.parseColor("#00000000"));
			}
			/**
			 * 设置监听器
			 * */
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mOnChangeViewListener != null){
						switch (position) {
						case ViewUtil.FRIENDS:
							mOnChangeViewListener.onChangeView(ViewUtil.FRIENDS);
							break;
						case ViewUtil.MESSAGE:
							mOnChangeViewListener.onChangeView(ViewUtil.MESSAGE);
							break;
						default:
							break;
						}
						mChoose = position;
						notifyDataSetChanged();
					}
				}
			});
			return convertView;
		}

		class ViewHolder {
			LinearLayout layout;
			ImageView icon;
			TextView name;
		}

	}

}
