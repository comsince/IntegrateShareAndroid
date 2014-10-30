package com.comsince.secret.ui;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.comsince.secret.R;
import com.comsince.secret.adapter.MatterListViewAdapter;
import com.comsince.secret.adapter.ViewPaperAdapter;
import com.comsince.secret.bean.Matter;
import com.comsince.secret.component.MMListView;
import com.comsince.secret.util.AppTools;
import com.comsince.secret.util.GlobalExceptionListener;
import com.comsince.secret.util.SqlliteHander;


public class MMMyActivity extends Activity implements OnClickListener,OnItemClickListener,OnItemLongClickListener{
	MMListView matterListtView;
	MMListView  followListtView;
	View   draftView;
	MatterListViewAdapter matterListAdapter;
	MatterListViewAdapter followListAdapter;
	ImageView backTopButton;
	ArrayList<Matter> matterDataList;
	ArrayList<Matter> followDataList;
	PopupWindow publishWindow;
	PopupWindow menuWindow;
	private ViewPager pager;
	List<View> pageViews;
	int page = 1;
	PopupWindow deleteWindow; 
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		GlobalExceptionListener.getInstance().init(this);
		init();
	} 
	
	void init()
	{
		
		findViewById(R.id.btn_nav_my).setOnClickListener(this);
		findViewById(R.id.btn_nav_follow).setOnClickListener(this);
		findViewById(R.id.btn_nav_draft).setOnClickListener(this);
		((TextView)findViewById(R.id.TITLE_TEXT)).setText("我的秘密");
		View cells = LayoutInflater.from(this).inflate(R.layout.mypaperview_cells, null);
		pageViews = new ArrayList<View>();
		matterListtView = (MMListView) cells.findViewById(R.id.matterListtView);
		followListtView = (MMListView) cells.findViewById(R.id.followListtView);
		draftView = cells.findViewById(R.id.draftPanel);
		draftView.findViewById(R.id.draftContent).setOnClickListener(this);
		matterDataList = SqlliteHander.getTnstantiation(this).myMimiList();
		matterListAdapter = new  MatterListViewAdapter(this,matterDataList);
		matterListtView.setAdapter(matterListAdapter);
		matterListtView.setShowFooter(false);
		matterListtView.setOnItemLongClickListener(this);
		matterListtView.thatAll();
		
		followDataList = SqlliteHander.getTnstantiation(this).myFollowList();
		followListAdapter = new  MatterListViewAdapter(this,followDataList);
		followListtView.setAdapter(followListAdapter);
		followListtView.setShowFooter(false);
		followListtView.thatAll();
		pageViews.add(matterListtView);
		pageViews.add(followListtView);
		pageViews.add(draftView); 
		 
		pager  = (ViewPager)this.findViewById(R.id.view_pager);
		pager.removeAllViews();
		pager.setAdapter( new ViewPaperAdapter(pageViews));
		pager.setOnPageChangeListener(new OnPageChangeListener(){
			@Override
			public void onPageScrollStateChanged(int arg0) {}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override
			public void onPageSelected(int index) {
				 if(index==0)
				 {
					 navSelector(R.id.btn_nav_my);
					 
				 }
				 if(index==1)
				 {
					 navSelector(R.id.btn_nav_follow);
				 }
				 if(index==2)
				 { 
					 navSelector(R.id.btn_nav_draft);
				 }
				
			}});
		navSelector(R.id.btn_nav_my);
		pager.setOffscreenPageLimit(2);
		
        
		
		matterListtView.setOnItemClickListener(this);
		followListtView.setOnItemClickListener(this); 

		Matter m =  SqlliteHander.getTnstantiation(MMMyActivity.this).getDraft();
			if(m!=null)
			{
				draftView.findViewById(R.id.draftContent).setVisibility(View.VISIBLE);
				draftView.findViewById(R.id.saveTime).setVisibility(View.VISIBLE);
				draftView.findViewById(R.id.noDraftTip).setVisibility(View.GONE);
				((TextView)draftView.findViewById(R.id.draftContent)).setText(m.content);
				((TextView)draftView.findViewById(R.id.saveTime)).setText("保存于"+ AppTools.howTimeAgo(MMMyActivity.this, Long.valueOf(m.timestamp)));
			}
			
			findViewById(R.id.LEFT_BUTTON).setVisibility(View.VISIBLE);
			findViewById(R.id.LEFT_BUTTON).setOnClickListener(this);
	}

 
	 
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		   case R.id.btn_nav_my:
			   pager.setCurrentItem(0);
			   break;
		   case R.id.btn_nav_follow:
			   pager.setCurrentItem(1);
			   break;
           case R.id.btn_nav_draft:
			   pager.setCurrentItem(2);
			   break;
			     
		   case R.id.draftContent:
			   Intent intent = new Intent(MMMyActivity.this,MMPublishActivity.class);   
               intent.putExtra("draft", ((TextView)draftView.findViewById(R.id.draftContent)).getText()); 
        	   startActivity(intent);
			   break;
           case R.id.LEFT_BUTTON:
			   
			   
			   this.finish();
			   break;
		}
		
	}
	private void navSelector(int id)
	{
		switch(id)
		{
		   case R.id.btn_nav_my:
			  
			   findViewById(R.id.btn_nav_my).setSelected(true);
			   findViewById(R.id.btn_nav_follow).setSelected(false);
			   findViewById(R.id.btn_nav_draft).setSelected(false);
			   break;
		   case R.id.btn_nav_follow:
			   
			   
			   findViewById(R.id.btn_nav_follow).setSelected(true);
			   findViewById(R.id.btn_nav_my).setSelected(false);
			   findViewById(R.id.btn_nav_draft).setSelected(false);
			   break;
            case R.id.btn_nav_draft:
			   
			    
            	findViewById(R.id.btn_nav_draft).setSelected(true);
            	findViewById(R.id.btn_nav_my).setSelected(false);
 			   findViewById(R.id.btn_nav_follow).setSelected(false);
			   break;
			     
		}
	}
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event) {
		  if(keyCode ==KeyEvent.KEYCODE_BACK )
		  {
			  this.finish();
		  }
		  return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
		Matter m =(Matter) arg0.getAdapter().getItem(index);
		Intent intent = new Intent(this,MMPreviewActivity.class);   
        intent.putExtra("matter", m); 
 		startActivity(intent);
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, final View view, final int index,
			long arg3) {
		
		 View pupview = LayoutInflater.from(this).inflate( R.layout.pupwindow_delete, null);
		 Button  pop_remove= (Button) pupview.findViewById(R.id.delete);
		 pop_remove.setOnClickListener(new OnClickListener()
		 {
			@Override
			public void onClick(View arg0) {
				SqlliteHander.getTnstantiation(MMMyActivity.this).deleteMatter(matterDataList.get(index).matterId);
				Animation anim = AnimationUtils.loadAnimation(MMMyActivity.this, R.anim.disappear);
				anim.setAnimationListener(new AnimationListener(){
					@Override
					public void onAnimationEnd(Animation arg0) {
						matterDataList.remove(index);
						matterListAdapter.notifyDataSetChanged();
						MediaPlayer.create(MMMyActivity.this, R.raw.restore).start();
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {}
					@Override
					public void onAnimationStart(Animation arg0) {}});
			    view.startAnimation(anim);
			  
			    deleteWindow.dismiss();
			}
		 });
		 
		 deleteWindow = new PopupWindow(pupview,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);  
		 deleteWindow.setBackgroundDrawable(new BitmapDrawable());
		 deleteWindow.setAnimationStyle(R.style.windowSlideAnimation);
		 //deleteWindow.showAsDropDown(view, Math.abs(view.getWidth()-deleteWindow.getWidth()) / 2, 0);
		 deleteWindow.showAtLocation(view, Gravity.CENTER,0, view.getTop());
		 deleteWindow.setOutsideTouchable(true);  
		return false;
	}
}
