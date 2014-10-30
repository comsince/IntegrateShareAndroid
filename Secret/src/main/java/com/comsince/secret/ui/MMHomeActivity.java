package com.comsince.secret.ui;


import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.comsince.secret.R;
import com.comsince.secret.adapter.MatterListViewAdapter;
import com.comsince.secret.adapter.ViewPaperAdapter;
import com.comsince.secret.bean.Matter;
import com.comsince.secret.component.SuperListView;
import com.comsince.secret.component.WindowMenu;
import com.comsince.secret.service.HandlerThreads;
import com.comsince.secret.util.BaseContorl;
import com.comsince.secret.util.GlobalExceptionListener;


@SuppressLint("HandlerLeak")
public class MMHomeActivity extends Activity implements OnClickListener,SuperListView.OnScrollPositionListener,OnItemClickListener,SuperListView.OnRefreshListener {
	SuperListView newlistView;
	SuperListView  hotlistView;
	SuperListView  piclistView;
	MatterListViewAdapter newListAdapter;
	MatterListViewAdapter hotListAdapter;
	MatterListViewAdapter picListAdapter;
	ImageView backTopButton;
	ArrayList<Matter> newDataList = new ArrayList<Matter>();
	ArrayList<Matter> hotDataList = new ArrayList<Matter>();
	ArrayList<Matter> picDataList = new ArrayList<Matter>();
	//View header;
	PopupWindow menuWindow;
	String order = "timestamp";
	int newpage = 1;
	int hotpage = 1;
	int picpage = 1;
	WebView bannerWebView;
	View loadingView;
	private ViewPager pager;
	List<View> pageViews;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		GlobalExceptionListener.getInstance().init(this);
		init();
		
		new HandlerThreads.SyncMyMatterThread(this).start();
	} 
	
	void init()
	{
		
	    backTopButton = (ImageView)this.findViewById(R.id.backTopButton);
		backTopButton.setOnClickListener(this);
	   /* header = LayoutInflater.from(this).inflate(R.layout.header_matter_home, null);
		header.findViewById(R.id.oderHotButton).setOnClickListener(this);
		header.findViewById(R.id.oderNewButton).setOnClickListener(this);*/
		findViewById(R.id.reload_button).setOnClickListener(this);
		findViewById(R.id.RIGHT_BUTTON).setOnClickListener(this);
		
		pageViews = new ArrayList<View>();
		View cells = LayoutInflater.from(this).inflate(R.layout.paperview_cells, null);
		newlistView = (SuperListView) cells.findViewById(R.id.newMatterListtView);
		hotlistView =(SuperListView) cells.findViewById(R.id.findMatterListtView);
		piclistView =(SuperListView) cells.findViewById(R.id.picMatterListtView);
		pageViews.add(newlistView);
		pageViews.add(hotlistView);
		pageViews.add(piclistView);
		pager  = (ViewPager)this.findViewById(R.id.view_pager);
		pager.removeAllViews();
		pager.setAdapter( new ViewPaperAdapter(pageViews));
		pager.setOnPageChangeListener(new OnPageChangeListener(){
			@Override
			public void onPageScrollStateChanged(int arg0) {backTopButtonBlur();}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {backTopButtonBlur();}
			@Override
			public void onPageSelected(int index) {
				 if(index==0)
				 {
					 onClick(R.id.btn_nav_new,false);
				 }
				 if(index==1)
				 {
					 onClick(R.id.btn_nav_hot,false);
				 }
				 if(index==2)
				 {
					 onClick(R.id.btn_nav_pic,false);
				 }
				
			}});
		
		onClick(R.id.btn_nav_new,false);
		pager.setOffscreenPageLimit(3);
		findViewById(R.id.btn_nav_new).setOnClickListener(this);
		findViewById(R.id.btn_nav_hot).setOnClickListener(this);
		findViewById(R.id.btn_nav_pic).setOnClickListener(this);

	//	listView.set
		//listView.setHeaderView(header);
		
		newListAdapter = new MatterListViewAdapter(MMHomeActivity.this,newDataList);
		newlistView.setAdapter(newListAdapter);
		newlistView.setOnRefreshListener(this);		
		
		hotListAdapter = new MatterListViewAdapter(MMHomeActivity.this,hotDataList);
		hotlistView.setAdapter(hotListAdapter);
		hotlistView.setOnRefreshListener(this);
		
		picListAdapter = new MatterListViewAdapter(MMHomeActivity.this,picDataList);
		piclistView.setAdapter(picListAdapter);
		piclistView.setOnRefreshListener(this);
		
		
		hotlistView.setOnScrollPositionListener(this);
		hotlistView.setOnItemClickListener(this);
		
		newlistView.setOnScrollPositionListener(this);
		newlistView.setOnItemClickListener(this);
	   
		piclistView.setOnScrollPositionListener(this);
		piclistView.setOnItemClickListener(this);
		
		
	    order = "recount";
	    hotlistView.doRefresh();
	    order = "pic";
	    piclistView.doRefresh();
	    order = "timestamp";
	    newlistView.doRefresh();
	   
	    //findViewById(R.id.progressView).setVisibility(View.VISIBLE);
	    
	    findViewById(R.id.RIGHT_BUTTON).setVisibility(View.VISIBLE);
	    ((TextView)findViewById(R.id.TITLE_TEXT)).setText("秘密大厅");
	    ((Button)findViewById(R.id.RIGHT_BUTTON)).setBackgroundResource(R.drawable.button_setting);
		((Button)findViewById(R.id.RIGHT_BUTTON)).setOnClickListener(this);
		
		menuWindow = new WindowMenu(this).getPopupWindow();

	}

	Handler matterHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			findViewById(R.id.progressView).setVisibility(View.GONE);
			String torder = msg.getData().getString("order");
			if(msg.what == -1)
			{
				findViewById(R.id.networkBusyView).setVisibility(View.VISIBLE);
				if(torder.equals("timestamp"))
				{
					newlistView.refreshComplete();
				}
				if(torder.equals("recount"))
				{
					hotlistView.refreshComplete();
				}
				
				if(torder.equals("pic"))
				{
					piclistView.refreshComplete();
				}
				return;
			}
			
			ArrayList<Matter> templist = (ArrayList<Matter>) msg.getData().getSerializable("list");
            if(templist != null && templist.size() == 0){
                findViewById(R.id.networkBusyView).setVisibility(View.VISIBLE);
                if(torder.equals("timestamp"))
                {
                    newlistView.refreshComplete();
                }
                if(torder.equals("recount"))
                {
                    hotlistView.refreshComplete();
                }

                if(torder.equals("pic"))
                {
                    piclistView.refreshComplete();
                }
                return;
            }
			if(torder.equals("timestamp"))
			{
				newlistView.refreshComplete();
				newListAdapter.getDataList().clear();
				newListAdapter.getDataList().addAll(templist);
				newListAdapter.notifyDataSetChanged();
				newpage=1;
			}
			if(torder.equals("recount"))
			{
				hotlistView.refreshComplete();
				hotListAdapter.getDataList().clear();
				hotListAdapter.getDataList().addAll(templist);
				hotListAdapter.notifyDataSetChanged();
				hotpage=1;
			}
			
			if(torder.equals("pic"))
			{
				piclistView.refreshComplete();
				picListAdapter.getDataList().clear();
				picListAdapter.getDataList().addAll(templist);
				picListAdapter.notifyDataSetChanged();
				picpage=1;
			}
			MediaPlayer.create(MMHomeActivity.this, R.raw.refresh_full).start();
		}
	};
    
	Handler moreHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			
			String torder = msg.getData().getString("order");
			findViewById(R.id.progressView).setVisibility(View.GONE);
			if(msg.what != 0)
			{
				if(torder.equals("timestamp"))
				{
					newlistView.showMoreComplete(false);
				}
				if(torder.equals("recount"))
				{
					hotlistView.showMoreComplete(false);
				}
				
				if(torder.equals("pic"))
				{
					piclistView.showMoreComplete(false);
				}
			    BaseContorl.doShowEToask(MMHomeActivity.this, R.string.tip_network_busy);
				return;
			}
			MediaPlayer.create(MMHomeActivity.this, R.raw.refresh_full).start();
			ArrayList<Matter> temp = (ArrayList<Matter>) msg.getData().getSerializable("list");
			if(temp!=null&&!temp.isEmpty())
			{
				if(torder.equals("timestamp"))
				{
					newListAdapter.getDataList().addAll(temp);
					newListAdapter.notifyDataSetChanged();
					newlistView.showMoreComplete(true);
					newpage = msg.getData().getInt("page");
				}
				if(torder.equals("recount"))
				{
					hotListAdapter.getDataList().addAll(temp);
					hotListAdapter.notifyDataSetChanged();
					hotlistView.showMoreComplete(true);
					hotpage = msg.getData().getInt("page");
				}
				
				if(torder.equals("pic"))
				{
					picListAdapter.getDataList().addAll(temp);
					picListAdapter.notifyDataSetChanged();
					piclistView.showMoreComplete(true);
					picpage = msg.getData().getInt("page");
				}
				
			}else
			{
				//BaseContorl.doShowHToask(MMHomeActivity.this,"已经是最后一页了");
                if(torder.equals("timestamp"))
                {
                    newlistView.showMoreComplete(false);
                }
                if(torder.equals("recount"))
                {
                    hotlistView.showMoreComplete(false);
                }

                if(torder.equals("pic"))
                {
                    piclistView.showMoreComplete(false);
                }
			}
			//listView.showMoreComplete();
			
			
			
		}
	};
	
	
	 
  
	
	
	
	@Override
	public void onClick(View v) {
		
		onClick(v.getId(),true);
	}
	
   public void onClick(int id,boolean flag)
   {
	   switch(id)
		{
		   case R.id.btn_nav_hot:
			   order = "recount";
			   hotpage = 1;
			   findViewById(R.id.btn_nav_hot).setSelected(true);
			   findViewById(R.id.btn_nav_new).setSelected(false);
			   findViewById(R.id.btn_nav_pic).setSelected(false);
			   if(flag){
				   pager.setCurrentItem(1);
			   }
			   break;
		   case R.id.btn_nav_new:
			   order = "timestamp";
			   newpage = 1;
			   findViewById(R.id.btn_nav_new).setSelected(true);
			   findViewById(R.id.btn_nav_hot).setSelected(false);
			   findViewById(R.id.btn_nav_pic).setSelected(false);
			   if(flag){
				   pager.setCurrentItem(0);
			   }
			   break;
		   case R.id.btn_nav_pic:
			   order = "pic";
			   picpage = 1;
			   findViewById(R.id.btn_nav_pic).setSelected(true);
			   findViewById(R.id.btn_nav_hot).setSelected(false);
			   findViewById(R.id.btn_nav_new).setSelected(false);
			   if(flag){
				   pager.setCurrentItem(2);
			   }
			   break;
		   case R.id.reload_button:
			   findViewById(R.id.progressView).setVisibility(View.VISIBLE);
			   findViewById(R.id.networkBusyView).setVisibility(View.GONE);
			   break;
			     
		   case R.id.backTopButton:
			   if(pager.getCurrentItem() == 0)
				{
				   newlistView.setSelection(0);
				}
				if(pager.getCurrentItem() == 1)
				{
					hotlistView.setSelection(0);
				}
			   break;
		   case R.id.RIGHT_BUTTON:
			    Intent intent = new Intent(MMHomeActivity.this,SettingCenterActivity.class);   
		 		startActivity(intent);
			   
			   break;
		}
	   
   }
 
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
		Matter m =null;
		if(pager.getCurrentItem() == 0)
		{
			m = newListAdapter.getItem(index-1);
		}
		if(pager.getCurrentItem() == 1)
		{
			m = hotListAdapter.getItem(index-1);
		}
		if(pager.getCurrentItem() == 2)
		{
			m = picListAdapter.getItem(index-1);
		}
		Intent intent = new Intent(MMHomeActivity.this,MMPreviewActivity.class);   
        intent.putExtra("matter", m); 
 		startActivity(intent);
	}

	@Override
	public void onResume() {
		if(menuWindow!=null &&menuWindow.isShowing())
		{
			menuWindow.dismiss();
		}
		super.onResume();
	}
	
	@Override
	public void showBackTopButton() {
		//backTopButton.setVisibility(View.VISIBLE);
	}
	@Override
	public void hideBackTopButton() {
		//backTopButton.setVisibility(View.GONE);
	}
	@Override
	public void backTopButtonFocuse() {
		backTopButton.setPressed(true);
	}
	@Override
	public void backTopButtonBlur() {
		backTopButton.setPressed(false);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 
		if(!menuWindow.isShowing())
		{
			menuWindow.showAtLocation(findViewById(R.id.view_pager), Gravity.BOTTOM, 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		}else
		{
			menuWindow.dismiss();
		}
	 return false;
}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new HandlerThreads.MatterListThread(matterHandler, 1,order).start();
	}

	@Override
	public void onShowNextPage() {
		if(order.equals("timestamp"))
		{
			new HandlerThreads.MatterListThread(moreHandler,  ++newpage,order).start();
		}
		if(order.equals("recount"))
		{
			new HandlerThreads.MatterListThread(moreHandler,  ++hotpage,order).start();
		}
		
		if(order.equals("pic"))
		{
			new HandlerThreads.MatterListThread(moreHandler,  ++picpage,order).start();
		}
		
	}
}
