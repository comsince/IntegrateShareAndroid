package com.comsince.secret.util;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comsince.secret.R;

/**
 * 
 * @author track	
 * @version1.0
 */
public abstract class BaseContorl {
	 
	  public static void doShowToask(final  Context context,int message){
		   LayoutInflater inflater=LayoutInflater.from(context);
		   View toaskView=inflater.inflate(R.layout.toask_view, null);
		   Toast toast=Toast.makeText(context,null,Toast.LENGTH_SHORT);
		   toast.setView(toaskView);
		   ((TextView)toaskView.findViewById(R.id.toaskMessage)).setText(message);
		   toast.show();
	  }
	  public static void doShowSToask(final  Context context,int message){
		  adoShowToask(context,R.drawable.icon_success,context.getString(message));
	  }
	  public static void doShowEToask(final  Context context,int message){
		  adoShowToask(context,R.drawable.icon_error,context.getString(message));
	  }
	  public static void doShowSToask(final  Context context,String message){
		  adoShowToask(context,R.drawable.icon_success,message);
	  }
	  public static void doShowEToask(final  Context context,String message){
		  adoShowToask(context,R.drawable.icon_error,message);
	  }
	  public static void doShowHToask(final  Context context,int message){
		  adoShowToask(context,R.drawable.icon_hint,context.getString(message));
	  }
	  public static void doShowHToask(final  Context context,String message){
		  adoShowToask(context,R.drawable.icon_hint,message);
	  }
	  public static void adoShowToask(final  Context ui,int icon,String message){
		   LayoutInflater inflater=LayoutInflater.from(ui);
		   View toaskView=inflater.inflate(R.layout.toask_view, null);
		   ((ImageView)toaskView.findViewById(R.id.icon)).setBackgroundResource(icon);
		  // ((TextView)toaskView.findViewById(R.id.toaskMessage)).setCompoundDrawablesWithIntrinsicBounds(icon, 0,0 , 0);
		   Toast toast=Toast.makeText(ui,null,1000);
		   toast.setView(toaskView);
		   ((TextView)toaskView.findViewById(R.id.toaskMessage)).setText(message);
			toast.show();
	  }
	  public static void doShowToask(final  Context ui,int icon,int message){
		  adoShowToask(ui,icon,ui.getString(message));
	  }
	 
	  
	  
}
