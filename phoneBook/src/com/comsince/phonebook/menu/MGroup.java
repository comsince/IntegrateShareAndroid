package com.comsince.phonebook.menu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.adapter.MGroupAdapter;
import com.comsince.phonebook.constant.Constant;
import com.comsince.phonebook.entity.Group;
import com.comsince.phonebook.entity.Groups;
import com.comsince.phonebook.ui.base.FlipperLayout.OnOpenListener;
import com.comsince.phonebook.util.AndroidUtil;

public class MGroup {
	private Context mContext;
	private PhoneBookApplication phoneBookApplication;
	private View mGroup;
	private Button mMenu;
	private ListView mDisplay;
	private EditText mSearch;
	private Button selection;
	
	private List<Group> mGroupResult = new ArrayList<Group>();
	private MGroupAdapter mGroupAdapter;
	
	private OnOpenListener mOnOpenListener;
	
	public MGroup(Context context,PhoneBookApplication application){
		this.mContext = context;
		this.phoneBookApplication = application;
		mGroup = LayoutInflater.from(context).inflate(R.layout.group, null);
		findViewById();
		setListener();
		initData();
	}
	
	public void findViewById(){
		mMenu = (Button) mGroup.findViewById(R.id.group_menu);
		mDisplay = (ListView) mGroup.findViewById(R.id.group_display);
		mSearch = (EditText) mGroup.findViewById(R.id.group_search);
		selection = (Button) mGroup.findViewById(R.id.group_add);
	}
	
	public void setListener(){
		mMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mOnOpenListener != null) {
					mOnOpenListener.open();
				}
			}
		});
	}
	
	public void initData(){
		getGroupData();
		mGroupAdapter = new MGroupAdapter(mContext, mGroupResult);
		mDisplay.setAdapter(mGroupAdapter);
	}
	/**
	 * 从本地获取分组信息
	 * */
	public void getGroupData(){
		String filePath = AndroidUtil.getSDCardRoot()+Constant.DIR_PERSON_INFO+File.separator;
		String fileName = phoneBookApplication.phoneBookPreference.getUserName(mContext)+"_"+phoneBookApplication.phoneBookPreference.getPassWord(mContext)+"_"+Constant.FILE_GROUP_SUFFIX+".xml";
		String fileUrl = filePath + fileName;
		if(new File(fileUrl).exists()){
			try {
				mGroupResult = phoneBookApplication.simpleXmlReader.readXml(fileUrl, Groups.class).getGroups();
				phoneBookApplication.mGroupResult = mGroupResult;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public View getView(){
		return mGroup;
	}
	
	/**
	 * 设置打开侧边栏的监听器，在主activity中调用
	 * */
	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}
	
	
}
