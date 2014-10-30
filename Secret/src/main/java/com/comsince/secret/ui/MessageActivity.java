package com.comsince.secret.ui;



import android.app.Activity;
import android.os.Bundle;


public class MessageActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		/*super.onCreate(savedInstanceState);
		setContentView(R.layout.tip_clear_cache);
		GlobalExceptionListener.getInstance().init(this);
	   ((Button)findViewById(R.id.clearButton)).setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			new Thread(){
				public void run()
				{
					File dir = new File(Constant.CACHE_DIR);
					File[] files = dir.listFiles();
					for(File f:files)
					{
						f.delete();
					}
					dir.delete();
				}
			}.start();
			BaseContorl.doShowToask(MessageActivity.this, R.string.clear_ok);
			MessageActivity.this.finish();
		}});
	   
	   ((Button)findViewById(R.id.button_return)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				 
				MessageActivity.this.finish();
			}});
	}*/
	}
 
}
