package com.comsince.secret.service;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.comsince.secret.R;
import com.comsince.secret.bean.Comment;
import com.comsince.secret.bean.Matter;
import com.comsince.secret.bean.Page;
import com.comsince.secret.bean.UpdateInfo;
import com.comsince.secret.bean.User;
import com.comsince.secret.common.Constant;
import com.comsince.secret.ui.NewVersionActivity;
import com.comsince.secret.util.AppTools;
import com.comsince.secret.util.BaseContorl;
import com.comsince.secret.util.SqlliteHander;


public  class HandlerThreads {

	 
	public static  class MatterListThread extends Thread{
		
	 
		Handler handler;
		String order;
		int page;
		public MatterListThread(Handler handler,int page,String order)
		{
			 
			this.handler = handler;
			this.page = page;
			this.order = order;
		}
		
		@Override
		public void run() {
			//WeiBoAPI api = new WeiBoAPI();
			ArrayList<Matter> list = null;
			Message message=new Message();
			try {
				list = API.getMatterList(page, order);
				
			} catch (Exception e) {
				e.printStackTrace();
				message.what = -1;
			}
            if(list==null || list.isEmpty())
			{
				page  --;
			}
			Bundle bundle=new Bundle();
			bundle.putSerializable("list", list==null?new ArrayList<Matter>():list );
			bundle.putString("order", order);
			bundle.putInt("page", page);
			message.setData(bundle);
			handler.sendMessage(message);
		}
	}
	
	public static  class SyncMyMatterThread extends Thread{
		
		 
		Context context;
		public SyncMyMatterThread(Context context)
		{
			 
			this.context = context;
		}
		
		@Override
		public void run() {
			try {
				ArrayList<Matter>  list = API.syncMyMatter(SqlliteHander.getTnstantiation(context).queryUser().userId);
				SqlliteHander.getTnstantiation(context).syncMatter(list);
				list = API.followMatterList(SqlliteHander.getTnstantiation(context).queryUser().userId);
				SqlliteHander.getTnstantiation(context).syncFollow(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
		
 
	//---------------------------------------------------------------------------------------------------------------
	
	public static class SendFadeBackThread extends Thread{
		
		String msg ;
		String userId;
		Handler handler;
		public SendFadeBackThread(Handler h,String m,String userId)
		{
			msg = m;
			this.userId = userId;
			this.handler = h;
		}
		@Override
		public void run() {
			 Message message=new Message();
			try{
			 API.sendFadeBack(msg,userId); 
			} catch (Exception e) {
				message.what = -1;
				e.printStackTrace();
			}
			handler.sendMessage(message);
		}
		
     }
	

	
	
	
	//---------------------------------------------------------------------------------------------------------------
	
//---------------------------------------------------------------------------------------------------------------
	
	public static class VoteThread extends Thread{
		
		int index ;
		Handler handler;
		String matterId;
		public VoteThread(Handler handler,String matterId,int i)
		{
			this.index= i;
			this.matterId = matterId;
			this.handler = handler;
		}
		@Override
		public void run() {
			
			Message msg=new Message();
			try {
				API.vote(matterId,index); 
			} catch (Exception e) {
				e.printStackTrace();
				msg.what = -1;
			}
			Bundle bundle=new Bundle();
			//bundle.putString("vote", "33");
			//bundle.putString("f", index);
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
     }
	

	
	
	
	//---------------------------------------------------------------------------------------------------------------
	
	
	public static class SendCommentThread extends Thread{
		
		Handler handler;
		Comment comment;
		public SendCommentThread(Handler handler,Comment c)
		{
			 
			this.handler = handler;
			this.comment = c;
		}
		@Override
		public void run() {
			
			 
			Message message=new Message();
			try {
				String code = API.publishComment(comment);
				message.what =Integer.parseInt(code);
			} catch (Exception e) {
				e.printStackTrace();
				message.what = -1;
			}
			Bundle bundle=new Bundle();
			comment.timestamp=String.valueOf(System.currentTimeMillis());
			bundle.putSerializable("comment", comment);
			message.setData(bundle);
			handler.sendMessage(message);
			//new NotifyQueryThread().start();
		}
     }
	

	
	
	
	//---------------------------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------------------------
	
	
	
	public static class PublishMatterThread extends Thread{
		
		Handler handler;
		Matter matter;
		public PublishMatterThread(Handler handler,Matter m)
		{
			 
			this.handler = handler;
			this.matter = m;
		}
		@Override
		public void run() {
			
			 
			Message message=new Message();
			try {
				matter = API.publishMatter(matter);
				
			} catch (Exception e) {
				e.printStackTrace();
				message.what = -1;
			}
			Bundle bundle=new Bundle();
			bundle.putSerializable("matter", matter);
			message.setData(bundle);
			handler.sendMessage(message);
		}
     }
	

	
	
	
	//---------------------------------------------------------------------------------------------------------------

	public static  class CommentListThread extends Thread{
		
		 
		Handler handler;
		String matterId;
		int page;
		public CommentListThread(Handler handler,String id,int p)
		{
			 page =p;
			 matterId = id;
			this.handler = handler;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public void run() {
			//WeiBoAPI api = new WeiBoAPI();
			ArrayList<Comment> list = null ;
			Page p = new Page();
			Message message=new Message();
			try {
				 p = API.getCommentList(matterId,page);
				 list = (ArrayList<Comment>) p.getDataList();
				if(list==null||list.isEmpty())
				{
					page --;
				}
			} catch (Exception e) {
				e.printStackTrace();
				message.what = -1;
			}
			
			Bundle bundle=new Bundle();
			bundle.putSerializable("list", list==null?new ArrayList<Comment>():list );
			bundle.putSerializable("page", page);
			bundle.putSerializable("count", p.getCount());
			message.setData(bundle);
			handler.sendMessage(message);
			//new NotifyQueryThread().start();
		}
	}
	//---------------------------------------------------------------------------------------------------------------

	//---------------------------------------------------------------------------------------------------------------

	public static  class ModifyAliasThread extends Thread{
		
		 
		Handler handler;
		User user;
		public ModifyAliasThread(Handler handler,User user)
		{
			this.user = user;
			this.handler = handler;
		}
		
		@Override
		public void run() {
			 
			Message message=new Message();
			try {
				 API.modifyAlias(user);
				
			} catch (Exception e) {
				e.printStackTrace();
				message.what = -1;
			}
			
			Bundle bundle=new Bundle();
			message.setData(bundle);
			handler.sendMessage(message);
			//new NotifyQueryThread().start();
		}
	}
	//---------------------------------------------------------------------------------------------------------------		
		
		

      public  static class CheckUpdateThread extends Thread{
		
	       
	     	Activity activity;
	     	public CheckUpdateThread(Activity a)
	     	{
	     		this.activity = a;
	     	}
			@Override
			public void run() {
					
				    Message message=new Message();
					UpdateInfo updateInfo = null;
					try {
						updateInfo = API.updateInfo();
					} catch (Exception e) {
						e.printStackTrace();
						message.what = -1;
					} 
					Bundle bundle=new Bundle();
					bundle.putSerializable("updateInfo", updateInfo);
					message.setData(bundle);
					handler.sendMessage(message);
			}
			Handler handler = new Handler() {
					
					@Override
					public void handleMessage(Message msg) {
						
						final UpdateInfo updateInfo=(UpdateInfo) msg.getData().getSerializable("updateInfo");
						int versionCode = 1;
						try {
						 versionCode = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
						} catch (NameNotFoundException e) {
							e.printStackTrace();
						}
						if(null==updateInfo || updateInfo.getNewlevel()<=versionCode)
						{
							BaseContorl.doShowSToask(activity, R.string.no_new_verson_tips);
						}
						else
						{
							Intent intent = new Intent(activity,NewVersionActivity.class);
					        intent.putExtra("updateInfo", updateInfo); 
					        activity.startActivity(intent);
						} 
					}
				};
				
		 }
		
	  
  	
  	//---------------------------------------------------------------------------------------------------------------

  		
  		
      public  static class NotifyAppStartThread extends Thread{
  		
    	    Activity activity;
	     	public NotifyAppStartThread(Activity a)
	     	{
	     		this.activity = a;
	     	}
			@Override
			public void run() {
					
				try {
					TelephonyManager telephonemanage = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
					AppTools.getResponseRelsut(Constant.SERVER_URL + "" + telephonemanage.getDeviceId());
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		 }	
		

      public static class WeChatRequestThread extends Thread{

          @Override
          public void run() {
              String result = null;
              try {
                  StringBuffer sb = new StringBuffer();
                  sb.append("<xml>");
                  sb.append("<ToUserName>comsince</ToUserName>");
                  sb.append("<FromUserName>liaojinlong</FromUserName>");
                  sb.append("<CreateTime>1348831860</CreateTime>");
                  sb.append("<MsgType>text</MsgType>");
                  sb.append("<Content>请求最新的秘密</Content>");
                  sb.append("<MsgId>123456789</MsgId>");
                  sb.append("</xml>");
                  result = API.getWechatValidateMsg(sb.toString());
                  Log.i("wechat","wechat msg: "+result);
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
  	 
      
}
