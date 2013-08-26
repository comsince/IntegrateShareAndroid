package com.comsince.phonebook.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.entity.Group;

public class SelectGroupDialogActivity extends Activity implements OnClickListener {
	private Context context;
	private PhoneBookApplication phoneBookApplication;
	private List<Group> dGroupResult = new ArrayList<Group>();

	private TextView commit, cancel;
	private ListView dDisplay;
	private GroupAdapter groupAdapter;
	
	public static int RESULT_SELECT_GROUP_SUCCESS = 10001;
	
	//群组标签
	private List<String> tags = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		phoneBookApplication = (PhoneBookApplication) getApplication();
		setContentView(R.layout.select_group_dialog);
		initView();
		setUpListener();
		initData();
	}

	public void initView() {
		commit = (TextView) findViewById(R.id.add_info_commit);
		cancel = (TextView) findViewById(R.id.add_info_cancel);
		dDisplay = (ListView) findViewById(R.id.selectgroups_display);
	}

	public void setUpListener() {
		commit.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	public void initData() {
		dGroupResult = phoneBookApplication.mGroupResult;
		groupAdapter = new GroupAdapter();
		dDisplay.setAdapter(groupAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_info_commit:
            comitData();
			break;
		case R.id.add_info_cancel:
			finish();
			break;

		default:
			break;
		}
	}
	
	public void comitData(){
		if(tags.size() == 0){
			Toast.makeText(context, "请选择要发送的群组", Toast.LENGTH_SHORT).show();
		}else{
			Intent intent = new Intent();
			intent.putStringArrayListExtra("tags", (ArrayList<String>) tags);
			setResult(RESULT_SELECT_GROUP_SUCCESS, intent);
			finish();
		}
	}

	private class GroupAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dGroupResult.size();
		}

		@Override
		public Object getItem(int position) {
			return dGroupResult.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.selectfriends_activity_item, null);
				holder = new ViewHolder();
				holder.alpha = (TextView) convertView.findViewById(R.id.selectfriends_item_alpha);
				holder.alpha_line = (ImageView) convertView.findViewById(R.id.selectfriends_item_alpha_line);
				holder.avatar = (ImageView) convertView.findViewById(R.id.selectfriends_item_avatar);
				holder.name = (TextView) convertView.findViewById(R.id.selectfriends_item_name);
				holder.status = (ImageView) convertView.findViewById(R.id.selectfriends_item_status);
				holder.style = (CheckBox) convertView.findViewById(R.id.selectfriends_item_style);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.alpha.setVisibility(View.GONE);
			holder.status.setVisibility(View.GONE);
			final Group group = dGroupResult.get(position);
			holder.avatar.setBackgroundResource(R.drawable.phonebook);
			holder.name.setText(group.getGroupName());
			holder.style.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String tag = group.getGroupTag();
					if(tags.contains(tag)){
						tags.remove(tag);
					}else{
						tags.add(tag);
					}
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView alpha;
			ImageView alpha_line;
			ImageView avatar;
			TextView name;
			ImageView status;
			CheckBox style;
		}

	}
}
