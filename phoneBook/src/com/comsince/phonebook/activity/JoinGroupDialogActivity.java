package com.comsince.phonebook.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comsince.phonebook.R;
import com.comsince.phonebook.asynctask.GeneralAsyncTask;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.Group;
import com.comsince.phonebook.entity.GroupInfo;
import com.comsince.phonebook.entity.Groups;
import com.comsince.phonebook.util.AndroidUtil;
import com.comsince.phonebook.util.PhoneBookUtil;
import com.comsince.phonebook.util.SimpleXmlReaderUtil;

public class JoinGroupDialogActivity extends Activity implements OnClickListener{
	private EditText searchContent;
	private ImageButton searchBtn;
	private ListView searchListResult;
	private TextView commit,cancel;
	
	private Context context;
	private GeneralAsyncTask getGroupTask;
	private GroupInfo groupInfo;
	private searchGroupAdapter searchAdapter;
	private SimpleXmlReaderUtil xmlUtil;
	private String groupTag = null;
	//群组标签
	private List<String> tags = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		xmlUtil = new SimpleXmlReaderUtil();
		setContentView(R.layout.search_group_dialog);
		initView();
		setUpListener();
		initData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getGroupInfoFromFile();
	}



	public void initView(){
		searchContent = (EditText) findViewById(R.id.group_search);
		searchBtn = (ImageButton) findViewById(R.id.search_btn);
		searchListResult = (ListView) findViewById(R.id.selectgroups_display);
		commit = (TextView) findViewById(R.id.add_info_commit);
		cancel = (TextView) findViewById(R.id.add_info_cancel);
	}
	
	public void setUpListener(){
		searchBtn.setOnClickListener(this);
		commit.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}
	
	public void initData(){
		groupInfo = new GroupInfo();
		searchAdapter = new searchGroupAdapter();
		searchListResult.setAdapter(searchAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_btn:
			searchGroup();
			break;
		case R.id.add_info_commit:
			commitData();
			break;
		case R.id.add_info_cancel:
			finish();
		default:
			break;
		}
	}
	
	public void searchGroup(){
		String searchCondition = searchContent.getText().toString().trim();
		if(TextUtils.isEmpty(searchCondition)){
			Toast.makeText(context, "请输入邀请码", Toast.LENGTH_SHORT).show();
		}else{
			groupTag = searchCondition;
			getGroupTask = new GeneralAsyncTask("正在查询，请稍后...",Constant.TASK_GET_GROUP_BY_TAG,searchCondition,context);
			getGroupTask.execute();
		}
	}
	
	public void commitData(){
		if(tags.size() == 0){
			Toast.makeText(context, "请选择要加入的群组", Toast.LENGTH_SHORT).show();
		}else{
			//建立个人已经加入的分组信息
			createPersonGroupInfo();
			Intent intent = new Intent(Constant.ACTION_ADD_TAG);
			intent.putExtra("tag", groupTag);
			sendBroadcast(intent);
			finish();
		}
	}
	
	public void createPersonGroupInfo(){
		List<Group> groupsList = new ArrayList<Group>();
		Group group = new Group();
		String groupName = groupInfo.getGroupName();
		if(!TextUtils.isEmpty(groupName)){
			group.setGroupName(groupName);
		}
		String groupTag = groupInfo.getGroupTag();
		if(!TextUtils.isEmpty(groupTag)){
			group.setGroupTag(groupTag);
		}
		groupsList = PhoneBookUtil.getCurrentUserGroup(context);
		if(groupsList.size()!= 0){
			if(!groupsList.contains(group)){
				groupsList.add(group);
			}
		}else{
			groupsList.add(group);
		}
		Groups groups = new Groups();
    	groups.setGroups(groupsList);
    	PhoneBookUtil.writePersonGroupInfo(groups, PhoneBookUtil.getPerosnGroupInfoFileName(context));
		
	}
	
    public void getGroupInfoFromFile(){
    	if(!TextUtils.isEmpty(groupTag)){
    		String filePath = AndroidUtil.getSDCardRoot()+Constant.PHONE_BOOK_PATH+File.separator+groupTag+File.separator+Constant.FILE_GROUP_INFO;
    		try {
				groupInfo = xmlUtil.readXml(filePath, GroupInfo.class);
				searchAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
	   
    }
	
	private class searchGroupAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			if(groupInfo.getGroupName() == null){
				return 0;
			}else{
				return 1;
			}
		}

		@Override
		public Object getItem(int arg0) {
			return groupInfo;
		}

		@Override
		public long getItemId(int position) {
			return 0;
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
			GroupInfo info = groupInfo;
			if(!TextUtils.isEmpty(info.getGroupName())){
				holder.avatar.setBackgroundResource(R.drawable.phonebook);
				holder.name.setText(info.getGroupName());
			}
            holder.style.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String tag = groupInfo.getGroupTag();
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
