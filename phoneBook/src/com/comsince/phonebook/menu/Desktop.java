package com.comsince.phonebook.menu;

import com.comsince.phonebook.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Desktop {
	private Context mContext;
	private Activity mActivity;
	private ListView mDisplay;
	/**
	 * 桌面适配器
	 */
	private DesktopAdapter mAdapter;
	/**
	 * 当前界面的View
	 */
	private View mDesktop;

	public Desktop(Context context, Activity activity) {
		mContext = context;
		mActivity = activity;
		// 绑定布局到当前View
		mDesktop = LayoutInflater.from(context).inflate(R.layout.desktop, null);
		findViewById();
		setListener();
		init();
	}

	public void findViewById() {
		mDisplay = (ListView) mDesktop.findViewById(R.id.desktop_display);
	}

	public void setListener() {

	}

	public void init() {
		mAdapter = new DesktopAdapter(mContext);
		mDisplay.setAdapter(mAdapter);
	}

	/**
	 * 获取菜单界面
	 * 
	 * @return 菜单界面的View
	 */
	public View getView() {
		return mDesktop;
	}

	public class DesktopAdapter extends BaseAdapter {
		private Context mContext;
		private String[] mName = { "首页", "消息", "好友", "照片", "转帖", "礼物", "游戏", "附近" };

		public DesktopAdapter(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return 8;
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
		public View getView(int position, View convertView, ViewGroup parent) {
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
			return convertView;
		}

		class ViewHolder {
			LinearLayout layout;
			ImageView icon;
			TextView name;
		}

	}

}
