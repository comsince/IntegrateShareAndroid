package com.comsince.secret.ui;


import java.util.ArrayList;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.comsince.secret.R;
import com.comsince.secret.adapter.CommentListViewAdapter;
import com.comsince.secret.bean.Comment;
import com.comsince.secret.bean.Matter;
import com.comsince.secret.bean.User;
import com.comsince.secret.common.Constant;
import com.comsince.secret.component.MMListView;
import com.comsince.secret.component.WindowComment;
import com.comsince.secret.service.HandlerThreads;
import com.comsince.secret.util.AnimateFirstDisplayListener;
import com.comsince.secret.util.AppTools;
import com.comsince.secret.util.BaseContorl;
import com.comsince.secret.util.GlobalExceptionListener;
import com.comsince.secret.util.SqlliteHander;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class MMPreviewActivity extends Activity implements OnClickListener{
	
	MMListView listView;
	Matter matter;
	ArrayList<Comment> dataList = new ArrayList<Comment>();
	ImageView backTopButton;
	Button reloadButton;
	int page = 1;
	CommentListViewAdapter listAdapter;
	PopupWindow commentWindow;
	 DisplayImageOptions options;
     private ImageLoadingListener animateFirstListener;
     ImageLoader imageLoader = ImageLoader.getInstance();
	@Override
	public void onCreate(Bundle bundle)
	{ 
		super.onCreate(bundle);
		setContentView(R.layout.activity_preview);
		initView(this.getIntent().getExtras());
		GlobalExceptionListener.getInstance().init(this);
	}
	
	private void initView(Bundle bundle)
	{
		matter = (Matter) bundle.getSerializable("matter");
		((TextView)this.findViewById(R.id.TITLE_TEXT)).setText(matter.alias);
		 View header = LayoutInflater.from(this).inflate(R.layout.header_matter_detail, null);
		((TextView)header.findViewById(R.id.alias)).setText(matter.alias);
		((TextView)header.findViewById(R.id.contextText)).setText(matter.content);
		((TextView)header.findViewById(R.id.minuteAgo)).setText(AppTools.howTimeAgo(this, Long.valueOf(matter.timestamp)));
		((TextView) header.findViewById(R.id.sun)).setText(String.valueOf(Integer.parseInt(matter.vote.substring(0, 4),16)));
	    ((TextView) header.findViewById(R.id.moon)).setText(String.valueOf(Integer.parseInt(matter.vote.substring(4, 8),16)));
	    ((TextView) header.findViewById(R.id.star)).setText(String.valueOf(Integer.parseInt(matter.vote.substring(8, 12),16)));
	    header.findViewById(R.id.sunBut).setOnClickListener(this);
	    header.findViewById(R.id.moonBut).setOnClickListener(this);
	    header.findViewById(R.id.starBut).setOnClickListener(this);
	    findViewById(R.id.publishButton).setOnClickListener(this);
	    findViewById(R.id.closeCommentButton).setOnClickListener(this);
	    
		backTopButton = (ImageView)this.findViewById(R.id.backTopButton);
		reloadButton = (Button)header.findViewById(R.id.reload_button);
		backTopButton.setOnClickListener(this);
		reloadButton.setOnClickListener(this);

		listAdapter = new CommentListViewAdapter(MMPreviewActivity.this,dataList,matter);
		listView = (MMListView) findViewById(R.id.commentListtView);
		listView.setHeaderView(header);
		header.setPadding(0, 0, 0, 0);
		listView.setAdapter(listAdapter);
		listView.setOnMoreListener(new MMListView.OnMoreListener(){

			@Override
			public void onShowNextPage() {
				new HandlerThreads.CommentListThread(moreHandler,matter.matterId,++page).start();
			}
			
		});
		listView.setOnScrollPositionListener(new MMListView.OnScrollPositionListener(){

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
			}});
	    ((TextView)listView.findViewById(R.id.commentCount)).setText("评论("+matter.recount+")");
		findViewById(R.id.LEFT_BUTTON).setVisibility(View.VISIBLE);
		((Button)findViewById(R.id.LEFT_BUTTON)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				MMPreviewActivity.this.finish();
		 }}); 
		if(matter.recount > 0)
		{
			new HandlerThreads.CommentListThread(commentHandler,matter.matterId,page).start();
		}else
		{
			findViewById(R.id.progressView).setVisibility(View.GONE);
		}
		
		commentWindow = new WindowComment(this,publishHandler,matter.matterId).getPopupWindow();
		((Button)findViewById(R.id.RIGHT_BUTTON)).setVisibility(View.VISIBLE);
		((Button)findViewById(R.id.RIGHT_BUTTON)).setBackgroundResource(R.drawable.button_comment);
		((Button)findViewById(R.id.RIGHT_BUTTON)).setOnClickListener(this);
		
	 
		 options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.image_empty)
			.showImageForEmptyUri(R.drawable.image_empty)
			.showImageOnFail(R.drawable.image_empty)
			.cacheInMemory()
			.cacheOnDisc()
			//.displayer(new RoundedBitmapDisplayer(20))
			.build();
 	   
 	   animateFirstListener = new AnimateFirstDisplayListener();
 	   
 	   imageLoader = ImageLoader.getInstance();
 	   
 	   
 	  if(matter.file!=null)
       {
      	 String url = Constant.SERVER_URL+"/"+matter.file;
      	imageLoader.displayImage(url, (ImageView) header.findViewById(R.id.picView), options, animateFirstListener);
      	header.findViewById(R.id.picView).setVisibility(View.VISIBLE);
       }
	}
 
	
	Handler commentHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			findViewById(R.id.progressView).setVisibility(View.GONE);
			if(msg.what == -1)
			{
				findViewById(R.id.networkBusyView).setVisibility(View.VISIBLE);
				return;
			}
			MediaPlayer.create(MMPreviewActivity.this, R.raw.refresh_full).start();
			dataList = (ArrayList<Comment>) msg.getData().getSerializable("list");
			//listView.setAdapter(listAdapter);
			listAdapter.setDataList(dataList);
			listAdapter.notifyDataSetChanged();
			page = msg.getData().getInt("page");
			int count = msg.getData().getInt("count");
			((TextView)listView.findViewById(R.id.commentCount)).setText("评论("+count+")");
		}
	};
 
		
    Handler moreHandler = new Handler() {
			
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 9||msg.what == -1)
				{
					listView.showMoreComplete();
				    BaseContorl.doShowEToask(MMPreviewActivity.this, R.string.tip_network_busy);
					return;
				}
				
				findViewById(R.id.progressView).setVisibility(View.GONE);
				ArrayList<Comment> temp  = (ArrayList<Comment>) msg.getData().getSerializable("list");
				if(temp!=null&&!temp.isEmpty())
				{
					dataList.addAll(temp);
					listAdapter.setDataList(dataList);
					listAdapter.notifyDataSetChanged();
					MediaPlayer.create(MMPreviewActivity.this, R.raw.refresh_full).start();
				}else
				{
					BaseContorl.doShowHToask(MMPreviewActivity.this,"没有更多的评论啦");
				}
				
				listView.showMoreComplete();
				page = msg.getData().getInt("page");
			}
		};

        Handler publishHandler = new Handler() {
				
				@Override
				public void handleMessage(Message msg) {
					
					findViewById(R.id.publishButton).setEnabled(true);
					MMPreviewActivity.this.findViewById(R.id.PROGRESS_BAR).setVisibility(View.GONE);
					MMPreviewActivity.this.findViewById(R.id.RIGHT_BUTTON).setVisibility(View.VISIBLE);
					if(msg.what == -1 )
					{
						 BaseContorl.doShowEToask(MMPreviewActivity.this,R.string.tip_network_busy);
					}else if(msg.what == 0)
					{
						
						  ((TextView)commentWindow.getContentView().findViewById(R.id.commentText)).setText("");
						   MediaPlayer.create(MMPreviewActivity.this, R.raw.refresh_full).start();
						   commentWindow.dismiss();  
						   
						   Comment comment = (Comment) msg.getData().getSerializable("comment");
						   if(dataList!=null &&!dataList.isEmpty()){
							   comment.rank = (Integer.parseInt(dataList.get(0).rank)+1)+"";
						   }else
						   {
							   comment.rank = "1";
						   }
						   
						   dataList.add(0, comment);
						   listAdapter.setDataList(dataList);
						   listAdapter.notifyDataSetChanged();
						   listView.setSelection(1);
						   
						   if(!comment.userId.equals(SqlliteHander.getTnstantiation(MMPreviewActivity.this).queryUser().userId))
						   {
							   SqlliteHander.getTnstantiation(MMPreviewActivity.this).saveFollowMatter(matter);
						   }
						   matter.recount = matter.recount+1;
						  ((TextView)listView.findViewById(R.id.commentCount)).setText("评论("+matter.recount+")");
						  dismssCommentPanel();
						  
						  ((EditText)findViewById(R.id.commentText)).setText(null);
					}
					else if(msg.what == 9)
					{
						 BaseContorl.doShowEToask(MMPreviewActivity.this,"服务器正忙，稍后再试吧");
					}
		
				}
			};
					
		 

		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
			   case R.id.backTopButton:
				   listView.setSelection(0);
				   break;
			   case R.id.reload_button:
				   findViewById(R.id.progressView).setVisibility(View.VISIBLE);
				   findViewById(R.id.networkBusyView).setVisibility(View.GONE);
				   new HandlerThreads.CommentListThread(commentHandler,matter.matterId,page).start();
				   break;
			  
			   case R.id.sunBut:
				   
				   new HandlerThreads.VoteThread(voteHandler,matter.matterId,1).start();
				   listView.findViewById(R.id.sunBut).setEnabled(false);
				   listView.findViewById(R.id.moonBut).setEnabled(false);
				   listView.findViewById(R.id.starBut).setEnabled(false);
				   startVoteAnim(R.drawable.f000);
				   ((TextView) listView.findViewById(R.id.sun)).setText(Integer.parseInt(((TextView) listView.findViewById(R.id.sun)).getText().toString())+1+"");
				  break;
			   case R.id.moonBut:
				   new HandlerThreads.VoteThread(voteHandler,matter.matterId,2).start();
				   listView.findViewById(R.id.sunBut).setEnabled(false);
				   listView.findViewById(R.id.moonBut).setEnabled(false);
				   listView.findViewById(R.id.starBut).setEnabled(false);
				   startVoteAnim(R.drawable.f009);
				  ((TextView) listView.findViewById(R.id.moon)).setText(Integer.parseInt(((TextView) listView.findViewById(R.id.moon)).getText().toString())+1+"");
				   break;
				   
			   case R.id.starBut:
				   new HandlerThreads.VoteThread(voteHandler,matter.matterId,3).start();
				   listView.findViewById(R.id.sunBut).setEnabled(false);
				   listView.findViewById(R.id.moonBut).setEnabled(false);
				   listView.findViewById(R.id.starBut).setEnabled(false);
				   startVoteAnim(R.drawable.f002);
				  ((TextView) listView.findViewById(R.id.star)).setText(Integer.parseInt(((TextView) listView.findViewById(R.id.star)).getText().toString())+1+"");
				   break;
				   
			   case R.id.RIGHT_BUTTON:
				   if(findViewById(R.id.publishCommentView).getVisibility()==View.VISIBLE)
				   {
					   dismssCommentPanel();
				   }else{
					   
				       findViewById(R.id.publishCommentView).setVisibility(View.VISIBLE);
				       findViewById(R.id.publishCommentView).startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom));
				   }
				   break;
			   case R.id.closeCommentButton:
				   dismssCommentPanel();
				   break;
			   case R.id.publishButton:
				   String commentText = ((EditText)findViewById(R.id.commentText)).getText().toString().trim();
					if(commentText.length() < 1)
					{
						BaseContorl.doShowHToask(this,"多说一点点吧!");
						return ;
					}
				   User user = SqlliteHander.getTnstantiation(this).queryUser();
				   Comment comment = new Comment();
				   comment.content=commentText;
				   comment.matterId=matter.matterId;
				   comment.alias=user.alias;
				   comment.userId = user.userId;
				   new HandlerThreads.SendCommentThread(publishHandler,comment).start();
				  // BaseContorl.doShowHToask(context,"正在回复......");
				   findViewById(R.id.publishButton).setEnabled(false);
				   findViewById(R.id.PROGRESS_BAR).setVisibility(View.VISIBLE);
				   findViewById(R.id.RIGHT_BUTTON).setVisibility(View.GONE);
				   break;
			}
		}	
		
		private void dismssCommentPanel()
		{
			
			Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_bottom);
			anim.setAnimationListener(new AnimationListener(){
				@Override
				public void onAnimationEnd(Animation arg0) {
					 findViewById(R.id.publishCommentView).setVisibility(View.GONE);
				}

				@Override
				public void onAnimationRepeat(Animation arg0) {}
				@Override
				public void onAnimationStart(Animation arg0) {}});
			findViewById(R.id.publishCommentView).startAnimation(anim);
		}
        Handler voteHandler = new Handler() {
			
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == -1)
				{
					 BaseContorl.doShowEToask(MMPreviewActivity.this,R.string.tip_network_busy);
				} 
					// BaseContorl.doShowSToask(MMPreviewActivity.this,"投票成功!");
	
			}
		};
		
		
		private void startVoteAnim(int icon)
		{
			
			  listView.findViewById(R.id.voteAnimView).setVisibility(View.VISIBLE);
			  ((TextView)listView.findViewById(R.id.voteAnimView)).setCompoundDrawablesWithIntrinsicBounds(icon, 0,0 , 0);
			   Animation anim = AnimationUtils.loadAnimation(this, R.anim.vote_anim);
				anim.setAnimationListener(new AnimationListener(){
					@Override
					public void onAnimationEnd(Animation arg0) {
						listView.findViewById(R.id.voteAnimView).setVisibility(View.GONE);
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {}
					@Override
					public void onAnimationStart(Animation arg0) {}});
				listView.findViewById(R.id.voteAnimView).startAnimation(anim);
			
		}
		
		
		@Override
		public boolean onKeyDown(int keyCode,KeyEvent event) {
			  if(keyCode ==KeyEvent.KEYCODE_BACK )
			  {
				  if(findViewById(R.id.publishCommentView).getVisibility()==View.VISIBLE)
				  {
					   dismssCommentPanel();
					   return false;
				  }
				  this.finish();
			  }
			  return false;
		}
}
