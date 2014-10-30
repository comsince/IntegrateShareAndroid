package com.comsince.secret.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comsince.secret.R;
import com.comsince.secret.bean.Matter;
import com.comsince.secret.common.Constant;
import com.comsince.secret.util.AnimateFirstDisplayListener;
import com.comsince.secret.util.AppTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;


public class MatterListViewAdapter extends BaseAdapter {
       private Context context;
       ArrayList<Matter> dataList;
       boolean frist = true;
       DisplayImageOptions options;
       private ImageLoadingListener animateFirstListener;
       ImageLoader imageLoader = ImageLoader.getInstance();
	   public MatterListViewAdapter(Context context, ArrayList<Matter> data)
       {
    	   this.context = context;
    	   this.dataList = data;
    	   
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
    	  
       }

        @Override
		public View getView(int index, View convertView, ViewGroup parent) {
        	
	        	View view = convertView;
				final ViewHolder holder;
				if (convertView == null) {
					view = LayoutInflater.from(context).inflate(R.layout.item_matterlist, parent, false);
					holder = new ViewHolder();
					holder.matter = ((TextView) view.findViewById(R.id.matter));
					holder.recount = ((TextView) view.findViewById(R.id.recount));
					holder.alias = ((TextView) view.findViewById(R.id.alias));
					holder.minuteAgo = ((TextView) view.findViewById(R.id.minuteAgo));
					holder.image = (ImageView) view.findViewById(R.id.picView);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
 		         holder.matter.setText(dataList.get(index).content);;
 		         holder.recount.setText(String.valueOf(dataList.get(index).recount));;
 		         holder.alias.setText(dataList.get(index).alias);;
 		         holder.minuteAgo.setText(AppTools.howTimeAgo(context, Long.valueOf(dataList.get(index).timestamp)));
 		         
 		         if(dataList.get(index).file!=null)
 		         {
 		        	String url = Constant.SERVER_URL+"/"+dataList.get(index).file;
 		        	imageLoader.displayImage(url, holder.image, options, animateFirstListener);
 		        	holder.image.setVisibility(View.VISIBLE);
 		        	holder.matter.setVisibility(View.GONE);
 		         }else
 		         {
 		        	 
 		        	holder.image.setVisibility(View.GONE);
 		        	holder.matter.setVisibility(View.VISIBLE);
 		         }
 		        
 		        return view;
        }

		@Override
		public int getCount() {
			if(dataList == null)
			{
				return 0;
			}
			return dataList.size();
		}
		
	@Override
    public Matter getItem(int index) {
             // TODO Auto-generated method stub
             return dataList.get(index);
     }

     @Override
	public long getItemId(int arg0) {
             // TODO Auto-generated method stub
             return 0;
     }

	public ArrayList<Matter> getDataList() {
		return dataList;
	}

	public void setDataList(ArrayList<Matter> dataList) {
		this.dataList = dataList;
	}
	
	private class ViewHolder {
		public TextView matter;
		public TextView recount;
		public TextView alias;
		public TextView minuteAgo;
		public ImageView image;
	}
	
}