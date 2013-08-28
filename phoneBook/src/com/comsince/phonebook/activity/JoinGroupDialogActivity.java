package com.comsince.phonebook.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.asynctask.GeneralAsyncTask;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.Group;
import com.comsince.phonebook.entity.GroupInfo;
import com.comsince.phonebook.entity.GroupPerson;
import com.comsince.phonebook.entity.GroupPersons;
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
			//更新当前分组的人员信息
			downLoadGroupPerson();
			
			Intent intent = new Intent(Constant.ACTION_ADD_TAG);
			intent.putExtra("tag", groupTag);
			sendBroadcast(intent);
			finish();
		}
	}
	/**
	 * 更新当前分组的人员信息
	 * */
	public void downLoadGroupPerson(){
		getGroupTask = new GeneralAsyncTask("更新当前分组的人员信息", Constant.TASK_DOWNLOAD_PERSON_GROUPPERSON, context, groupHandler);
		getGroupTask.execute(groupTag);
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
	
	/**
	 * 将个人信息加入到你所要加入的群组中
	 * */
	public void createGroupPerson(){
		GroupPersons groupPersons = new GroupPersons();
		List<GroupPerson> groupPersonList = new ArrayList<GroupPerson>();
		GroupPerson groupPerson = new GroupPerson();
		String personAccount = PhoneBookApplication.phoneBookPreference.getUserName(context);
		groupPerson.setPersonAccount(personAccount);
		String personMd5password = PhoneBookApplication.phoneBookPreference.getPassWord(context);
		groupPerson.setPersonAccountPassword(personMd5password);
		groupPerson.setIsAuthor("Y");
		//设置个人信息的相对路径名
		groupPerson.setDetialInfoPath(PhoneBookUtil.getCurrentDetialInfoPath(context));
		String personRealName = PhoneBookUtil.getCurrrentPersonInfo(context).getName();
		if(!TextUtils.isEmpty(personRealName)){
			groupPerson.setPersonRealName(personRealName);
		}
		//获取当前最新的分组人员信息
		groupPersonList = PhoneBookUtil.getCurrentJoinGroupPersonInfo(context, groupTag);
		String username = PhoneBookApplication.phoneBookPreference.getUserName(context);
		String md5password = PhoneBookApplication.phoneBookPreference.getPassWord(context);
		if(groupPersonList.size() != 0){
			int i = 0;
			int size = groupPersonList.size();
			for(GroupPerson gp : groupPersonList){
				if(gp.getPersonAccount().equals(username)&&gp.getPersonAccountPassword().equals(md5password)){
					break;
				}
				i++;
			}
			if(i == size){
				groupPersonList.add(groupPerson);
			}
		}else{
			groupPersonList.add(groupPerson);
		}
		groupPersons.setGroupPersons(groupPersonList);
		PhoneBookUtil.writeGroupPersonToTargetGroup(groupTag, groupPersons);
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
    
    /**
     * 上传分组人员信息
     * */
    public void upLoadGroupPerson(){
    	getGroupTask = new GeneralAsyncTask("正在上传分组人员信息...",Constant.TASK_UPLOAD_PERSON_GROUPPERSON, context, groupHandler);
    	getGroupTask.execute(groupTag);
    }
    
    /**
     * 上传个人的分组信息
     * */
    public void upLoadPersonGroupInfo(){
    	getGroupTask = new GeneralAsyncTask("正在上传个人分组信息...", Constant.TASK_UPLOAD_PERSON_GROUPINFO, context, groupHandler);
    	getGroupTask.execute();
    }
    
    private Handler groupHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.DOWN_LOAD_GROUPPERSON_SUCCESS:
				//将个人信息加入群组
				createGroupPerson();
				//上传个人群组信息，与个人加入群组的信息
				upLoadGroupPerson();
				break;
			case Constant.UPLAOD_GROUPPERSON_SUCCESS:
				//不管成功与否都要在上出个人分组信息
				upLoadPersonGroupInfo();
				break;
			case Constant.DOWN_LOAD_GROUPPERSON_FAIL:
				//将个人信息加入群组
				createGroupPerson();
				upLoadGroupPerson();
				break;
			default:
				break;
			}
		}
    	
    };
	
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
