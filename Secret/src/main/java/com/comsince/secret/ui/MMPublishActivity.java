package com.comsince.secret.ui;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.comsince.secret.R;
import com.comsince.secret.bean.Matter;
import com.comsince.secret.bean.User;
import com.comsince.secret.common.Constant;
import com.comsince.secret.service.HandlerThreads;
import com.comsince.secret.util.BaseContorl;
import com.comsince.secret.util.SqlliteHander;


public class MMPublishActivity extends Activity implements OnClickListener{
	
	File imageFile;
	String fileType="1";
	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		setContentView(R.layout.activity_publish);
		findViewById(R.id.publishButton).setOnClickListener(this);
		
		((Button)findViewById(R.id.RIGHT_BUTTON)).setBackgroundResource(R.drawable.button_save);
		this.findViewById(R.id.RIGHT_BUTTON).setOnClickListener(this);
		
		String draft = this.getIntent().getStringExtra("draft");
		if(null!=draft)
		{
			((EditText)findViewById(R.id.matterContent)).setText(draft);
		}
		
		findViewById(R.id.LEFT_BUTTON).setVisibility(View.VISIBLE);
		findViewById(R.id.RIGHT_BUTTON).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.TITLE_TEXT)).setText("述说秘密");
		this.findViewById(R.id.LEFT_BUTTON).setOnClickListener(this);
		this.findViewById(R.id.imageButton).setOnClickListener(this);
		this.findViewById(R.id.imagePreview).setOnClickListener(this);
		
	}
	
    Handler publishHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			
			
			findViewById(R.id.publishButton).setEnabled(true);
			MMPublishActivity.this.findViewById(R.id.PROGRESS_BAR).setVisibility(View.GONE);
			MMPublishActivity.this.findViewById(R.id.RIGHT_BUTTON).setVisibility(View.VISIBLE);
			if(msg.what == -1)
			{
				BaseContorl.doShowEToask(MMPublishActivity.this, R.string.tip_network_busy);
				return;
			}
			MediaPlayer.create(MMPublishActivity.this, R.raw.refresh_full).start();
			Matter matter = (Matter) msg.getData().getSerializable("matter");
		    BaseContorl.doShowSToask(MMPublishActivity.this,"发布成功!");
		    SqlliteHander.getTnstantiation(MMPublishActivity.this).saveMatter(matter);
		    Intent intent = new Intent(MMPublishActivity.this,MMPreviewActivity.class);   
            intent.putExtra("matter", matter); 
     		startActivity(intent);
     		MMPublishActivity.this.finish();
     		
	     	if(imageFile!=null)
	   		{
	   			 imageFile.delete();
	   		}
		}
	};
	
	 @Override  
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        if (resultCode == RESULT_OK) {  
	            Uri uri = data.getData();
	            Log.e("uri", uri.toString());  
	            ContentResolver cr = this.getContentResolver();  
	            try {  
	                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
	                
	                bitmap = compressImage(bitmap);
	                
	                savaBitmapToSDCard(bitmap);
	                
	                /* 将Bitmap设定到ImageView */  
	                ((ImageView) findViewById(R.id.imagePreview)).setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath())); 
	                
	                findViewById(R.id.imageBox).setVisibility(View.VISIBLE);
	            } catch (Exception e) {  
	                Log.e("Exception", e.getMessage(),e);  
	            }  
	        }  
	        super.onActivityResult(requestCode, resultCode, data);  
	    }  
	 
	 
	 private Bitmap compressImage(Bitmap bitmap ) {  
	      
		    if(bitmap.getHeight() > 600)
		    {
		    	bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth()*600/bitmap.getHeight(), 600);
		    }
		    return bitmap;
		 
		}
	 
	 
	 public  void savaBitmapToSDCard(Bitmap bitmap){
		 if(imageFile!=null)
		 {
			 imageFile.delete();
		 }
		 imageFile = new File(Constant.IMAGE_DIR+"/"+System.currentTimeMillis());
	     FileOutputStream fOut = null;
	     try {
	    	 imageFile.createNewFile();
	         fOut = new FileOutputStream(imageFile);
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
	          bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);//把Bitmap对象解析成流
	     try {
	         fOut.flush();
	         fOut.close();
	     } catch (IOException e) {
	         e.printStackTrace();
	     }
	 }
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event) {
		  if(keyCode ==KeyEvent.KEYCODE_BACK )
		  {
			//  ((RadioButton) this.getParent().findViewById(R.id.radio_home)).setChecked(true);
			  this.finish();
		  }
		  return false;
	}


	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		   case R.id.publishButton:
			   String content = ((EditText)findViewById(R.id.matterContent)).getText().toString().trim();
				if(  content.length() < 1)
				{
					BaseContorl.doShowHToask(MMPublishActivity.this,"多说一点点吧!");
					return ;
				}
				Matter matter = new Matter();
				matter.content = content;
				User user = SqlliteHander.getTnstantiation(MMPublishActivity.this).queryUser();
				matter.userId = user.userId;
				matter.alias = user.alias;
				if(imageFile!=null && imageFile.exists())
				{
					matter.fileType = fileType;
					matter.file = imageFile.getAbsolutePath();
				}
				new HandlerThreads.PublishMatterThread(publishHandler,matter).start();
				BaseContorl.doShowHToask(MMPublishActivity.this,"正在发布......");
				
				findViewById(R.id.publishButton).setEnabled(false);
				MMPublishActivity.this.findViewById(R.id.PROGRESS_BAR).setVisibility(View.VISIBLE);
				MMPublishActivity.this.findViewById(R.id.RIGHT_BUTTON).setVisibility(View.GONE);
			   break;
		   case R.id.RIGHT_BUTTON:
			   String content1 = ((EditText)findViewById(R.id.matterContent)).getText().toString().trim();
				if(content1.length() ==0)
				{
					BaseContorl.doShowHToask(MMPublishActivity.this,"没有需要保存的内容!");
					return ;
				}
			   SqlliteHander.getTnstantiation(MMPublishActivity.this).saveDraft(content1);
			   BaseContorl.doShowSToask(MMPublishActivity.this,"保存成功!"); 
			   break;
			   
		   case R.id.LEFT_BUTTON:
			   String content2 = ((EditText)findViewById(R.id.matterContent)).getText().toString().trim();
				if(content2.length() >0)
				{
					SqlliteHander.getTnstantiation(MMPublishActivity.this).saveDraft(content2);
					 BaseContorl.doShowSToask(MMPublishActivity.this,"已保存草稿!"); 
				}
			  
			   MMPublishActivity.this.finish();
			   break;
		   case R.id.imageButton:
			   Intent intent = new Intent();  
               /* 开启Pictures画面Type设定为image */  
               intent.setType("image/*");
               intent.addCategory(Intent.CATEGORY_OPENABLE);
               /* 使用Intent.ACTION_GET_CONTENT这个Action */  
               intent.setAction(Intent.ACTION_GET_CONTENT);   
               /* 取得相片后返回本画面 */  
               startActivityForResult(intent, 1); 
			   break;
			   
			   
		   case R.id.imagePreview:
			   findViewById(R.id.imageBox).setVisibility(View.GONE);
			   imageFile.delete();
			   imageFile = null;
			   break;
		}
		
	}
}
