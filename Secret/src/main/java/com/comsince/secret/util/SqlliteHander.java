package com.comsince.secret.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.comsince.secret.bean.Matter;
import com.comsince.secret.bean.User;
import com.comsince.secret.common.Constant;


public class SqlliteHander {
	static Context context;
	private static SqlliteHander hander;
	private final static String DATABASE = Constant.APP_NAME+".db";
	private SqlliteHander()
	{};
	
	public static SqlliteHander getTnstantiation(Context ctx){
		context = ctx;
		if(null==hander)
		{
			hander =  new SqlliteHander();
		}
		return hander;
	}
	
	public  void createTable()
	{
		 SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);  	
		 String USQL ="create table if not exists T_MIMI_USER (userId varchar(13) PRIMARY KEY,alias varchar(16),imei varchar(15));";
		 String MSQL ="create table if not exists T_MIMI_MATTER (matterId varchar(13) PRIMARY KEY,userId varchar(10),content varchar(2000),alias varchar(16),timestamp varchar(15),lastretime varchar(15),type varchar(1),recount varchar(10),file varchar(32),fileType varchar(1),vote varchar(20));";
		 String FSQL ="create table if not exists T_MIMI_FOLLOW (matterId varchar(13) PRIMARY KEY,userId varchar(10),content varchar(2000),alias varchar(16),timestamp varchar(15),lastretime varchar(15),type varchar(1),recount varchar(10),file varchar(32),fileType varchar(1),vote varchar(20));";
		 String DSQL ="create table if not exists T_MIMI_DRAFT (content varchar(2000),timestamp varchar(15));";
		 db.execSQL(USQL);  
		 db.execSQL(MSQL);
		 db.execSQL(FSQL); 
		 db.execSQL(DSQL); 
		 db.close();
	}
	
	public  void saveUser(User obj)
	{
		deleteUser();
		SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);  
		db.execSQL("INSERT INTO T_MIMI_USER(userId,alias,imei) VALUES (?,?,?)", new String[]{obj.userId,obj.alias,obj.imei});
		db.close();
	}
	public  void saveDraft(String content)
	{
		SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);  
		db.execSQL("INSERT INTO T_MIMI_DRAFT(content,timestamp) VALUES (?,?)", new String[]{content,String.valueOf(System.currentTimeMillis())});
		db.close();
	}
	public Matter getDraft()
	{
		Matter m= null;
		SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);  
		 Cursor c = db.rawQuery("SELECT * FROM T_MIMI_DRAFT", null);  
	        while (c.moveToNext()) {  
	        	m = new Matter();
	        	m.content=c.getString(c.getColumnIndex("content"));
	        	m.timestamp = c.getString(c.getColumnIndex("timestamp"));
	        }  
	        c.close(); 
	        db.close();
	        
	        return m;
	}
	public  void saveMatter(Matter matter)
	{
		SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);  
		db.execSQL("INSERT INTO T_MIMI_MATTER(matterId,userId,content,alias,timestamp,lastretime,type,recount,file,fileType,vote) VALUES (?,?,?,?,?,?,?,?,?,?,?)",new String[]{matter.matterId,matter.userId,matter.content,matter.alias,matter.timestamp,matter.lastretime,matter.type,String.valueOf(matter.recount),matter.file,matter.fileType,matter.vote});
		db.close();
	}
	public  void saveFollowMatter(Matter matter)
	{
		SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);  
		Cursor c = db.rawQuery("SELECT * FROM T_MIMI_FOLLOW where matterId = ?", new String[]{matter.matterId});  
	    while (c.moveToNext()) {  
	        	 return ;
	    }  
		db.execSQL("INSERT INTO T_MIMI_FOLLOW(matterId,userId,content,alias,timestamp,lastretime,type,recount,file,fileType,vote) VALUES (?,?,?,?,?,?,?,?,?,?,?)",new String[]{matter.matterId,matter.userId,matter.content,matter.alias,matter.timestamp,matter.lastretime,matter.type,String.valueOf(matter.recount),matter.file,matter.fileType,matter.vote});
		c.close();
		db.close();
	}
	public  void syncMatter(List<Matter> list)
	{
		
		SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);  
		db.execSQL("delete from T_MIMI_MATTER");
		for(Matter matter:list)
		{
			db.execSQL("INSERT INTO T_MIMI_MATTER(matterId,userId,content,alias,timestamp,lastretime,type,recount,file,fileType,vote) VALUES (?,?,?,?,?,?,?,?,?,?,?)",new String[]{matter.matterId,matter.userId,matter.content,matter.alias,matter.timestamp,matter.lastretime,matter.type,String.valueOf(matter.recount),matter.file,matter.fileType,matter.vote});
		}
		db.close();
	}
	
	public  void syncFollow(List<Matter> list)
	{
		
		SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);  
		db.execSQL("delete from T_MIMI_FOLLOW");
		for(Matter matter:list)
		{
			db.execSQL("INSERT INTO T_MIMI_FOLLOW(matterId,userId,content,alias,timestamp,lastretime,type,recount,file,fileType,vote) VALUES (?,?,?,?,?,?,?,?,?,?,?)",new String[]{matter.matterId,matter.userId,matter.content,matter.alias,matter.timestamp,matter.lastretime,matter.type,String.valueOf(matter.recount),matter.file,matter.fileType,matter.vote});
		}
		db.close();
	}
	
	public  void deleteUser()
	{
		 SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);  	
		 db.execSQL("delete from T_MIMI_USER", new Object[]{});
		 db.close();
	}
	public  void deleteAllMatter()
	{
		 SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);  	
		 db.execSQL("delete from T_MIMI_MATTER", new Object[]{});
		 db.close();
	}
	public  void deleteMatter(String matterId)
	{
		 SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);  	
		 db.execSQL("delete from T_MIMI_MATTER where matterId =?", new Object[]{matterId});
		 db.close();
	}
	
	public  User queryUser()
	{
		 User info = null;
		 SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);  	
		 Cursor c = db.rawQuery("SELECT * FROM T_MIMI_USER", null);  
	        while (c.moveToNext()) {  
	        	info=new User();
	        	info.userId = c.getString(c.getColumnIndex("userId"));
	        	info.alias = c.getString(c.getColumnIndex("alias"));
	        	info.imei = c.getString(c.getColumnIndex("imei"));
	        	break;
	        }  
	        c.close(); 
	        db.close();
		return info;
	}
	public  ArrayList<Matter> myMimiList()
	{   
		 ArrayList<Matter> list = new  ArrayList<Matter>();
		 SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);  	
		 Cursor c = db.rawQuery("SELECT * FROM T_MIMI_MATTER order by timestamp desc", null);  
	        while (c.moveToNext()) {  
	        	Matter m=new Matter();
	        	m.matterId = c.getString(c.getColumnIndex("matterId"));
	        	m.timestamp = c.getString(c.getColumnIndex("timestamp"));
	        	m.content = c.getString(c.getColumnIndex("content"));
	        	m.alias = c.getString(c.getColumnIndex("alias"));
	        	m.userId = c.getString(c.getColumnIndex("userId"));
	        	
	        	m.lastretime = c.getString(c.getColumnIndex("lastretime"));
	        	m.type = c.getString(c.getColumnIndex("type"));
	        	m.recount = Integer.parseInt(c.getString(c.getColumnIndex("recount")));
	        	m.file = c.getString(c.getColumnIndex("file"));
	        	m.fileType = c.getString(c.getColumnIndex("fileType"));
	        	m.vote = c.getString(c.getColumnIndex("vote"));
	        	list.add(m);
	        }  
	        c.close(); 
	        db.close();
	        return list;
	}
	
	public   ArrayList<Matter> myFollowList()
	{
		 ArrayList<Matter> list = new  ArrayList<Matter>();
		 SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);  	
		 Cursor c = db.rawQuery("SELECT * FROM T_MIMI_FOLLOW order by timestamp desc", null);  
	        while (c.moveToNext()) {  
	        	Matter m=new Matter();
	        	m.matterId = c.getString(c.getColumnIndex("matterId"));
	        	m.userId = c.getString(c.getColumnIndex("userId"));
	        	m.timestamp = c.getString(c.getColumnIndex("timestamp"));
	        	m.content = c.getString(c.getColumnIndex("content"));
	        	m.alias = c.getString(c.getColumnIndex("alias"));
	        	
	        	m.lastretime = c.getString(c.getColumnIndex("lastretime"));
	        	m.type = c.getString(c.getColumnIndex("type"));
	        	m.recount = Integer.parseInt(c.getString(c.getColumnIndex("recount")));
	        	m.file = c.getString(c.getColumnIndex("file"));
	        	m.fileType = c.getString(c.getColumnIndex("fileType"));
	        	m.vote = c.getString(c.getColumnIndex("vote"));
	        	list.add(m);
	        }  
	        c.close(); 
	        db.close();
	        return list;
	}
}
