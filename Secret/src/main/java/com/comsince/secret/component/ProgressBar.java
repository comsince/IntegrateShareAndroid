package com.comsince.secret.component;



import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.comsince.secret.R;

public class ProgressBar extends ImageView{
	
 
	public ProgressBar(Context paramContext) {
		super(paramContext);
		init(paramContext);
	}
	
	
	public ProgressBar(Context paramContext, AttributeSet paramAttributeSet)
	{
	    super(paramContext, paramAttributeSet);
	    init(paramContext);
	}
	

	public void init( final Context context)
	{
		 final AnimationDrawable ad = (AnimationDrawable) getResources().getDrawable(R.drawable.progressbar);
		 this.setBackgroundDrawable(ad);
		 this.post(new Runnable()  
		 {  
		             @Override  
		             public void run()  
		             {  
		            	 ad.start();  
		             }  
		 });  
	}
    
}
