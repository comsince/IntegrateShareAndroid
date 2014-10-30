package com.comsince.secret.ui;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.comsince.secret.R;
import com.comsince.secret.bean.UpdateInfo;
import com.comsince.secret.util.BaseContorl;
import com.comsince.secret.util.GlobalExceptionListener;


public class NewVersionActivity extends Activity {
	 UpdateInfo updateInfo ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		initView();
		GlobalExceptionListener.getInstance().init(this);
		((Button) findViewById(R.id.LEFT_BUTTON))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						NewVersionActivity.this.finish();
					}
				});
	}

	private void initView()
	{
		findViewById(R.id.LEFT_BUTTON).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.TITLE_TEXT)).setText(R.string.find_new_version);
		  updateInfo  = (UpdateInfo) this.getIntent().getExtras().getSerializable("updateInfo");
		 TextView newVersion = (TextView)this.findViewById(R.id.new_version_textview);
		 newVersion.setText(updateInfo.getNewVersion());
		 TextView atVersion = (TextView)this.findViewById(R.id.at_version_textview);
		 String nowVersionName = "";
		try {
			nowVersionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}
		 atVersion.setText(nowVersionName);
		 TextView update_msg = (TextView)this.findViewById(R.id.update_msg_textview);
		 if(updateInfo.getUpdateMsg() == null)
		 {
			 update_msg.setText(R.string.no_update_msg);
		 }else
		 {
			 update_msg.setText(updateInfo.getUpdateMsg().replaceAll("[|]", "\n"));
		 }
		 
		 Button download_buttion = (Button)this.findViewById(R.id.downLoadButton);
		 download_buttion.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				/*String[] arry =  updateInfo.getAppUrl().split("/");
				  String fileName = arry[arry.length-1];
				try{
					DownloadManager downloadManager = ((DownloadManager) NewVersionActivity.this.getSystemService(Activity.DOWNLOAD_SERVICE));
			        Request request = new Request(Uri.parse(updateInfo.getAppUrl()));
			        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
			        request.setShowRunningNotification(true);
			        request.setVisibleInDownloadsUi(true);
			        request.setTitle(NewVersionActivity.this.getString(R.string.app_name)+updateInfo.getNewVersion());
		            downloadManager.enqueue(request);
				}catch(Exception e)*/
				{
					    Uri url = Uri.parse(updateInfo.getAppUrl());  
					    Intent intent= new Intent(Intent.ACTION_VIEW, url);        
					    NewVersionActivity.this.startActivity(intent);
				}
				BaseContorl.doShowSToask(NewVersionActivity.this, R.string.downloading);
			}
			 
	     });
	}
	
}
