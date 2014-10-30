package com.comsince.secret.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comsince.secret.R;
import com.comsince.secret.bean.Comment;
import com.comsince.secret.bean.Matter;
import com.comsince.secret.util.AppTools;


public class CommentListViewAdapter extends BaseAdapter {
       private Context context;
       ArrayList<Comment> commentList;
       Matter matter;
       boolean frist = true;
	   public CommentListViewAdapter(Context context,ArrayList<Comment> list,Matter m)
       {
    	   this.context = context;
    	   this.commentList = list;
    	   matter = m;
       }

        @Override
		public View getView(int index, View convertView, ViewGroup parent) {
        	View view = convertView;
        	final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
				holder = new ViewHolder();
				holder.commontText = ((TextView) view.findViewById(R.id.item_commentText));
				holder.minuteAgo = ((TextView) view.findViewById(R.id.minuteAgo));
				holder.alias = ((TextView) view.findViewById(R.id.alias));
				holder.rank = ((TextView) view.findViewById(R.id.rank));
				holder.image = (ImageView) view.findViewById(R.id.icon_louzhu);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
		         holder.commontText.setText(commentList.get(index).content);;
		         holder.rank.setText("#"+commentList.get(index).rank);;
		         holder.alias.setText(commentList.get(index).alias);;
		         holder.minuteAgo.setText(AppTools.howTimeAgo(context, Long.valueOf(commentList.get(index).timestamp)));
		         if(matter.userId.equals(commentList.get(index).userId))
				 {
		        	 holder.image.setVisibility(View.VISIBLE);
				 }
        	 
		    if(frist)
		    	view.setAnimation(AnimationUtils.loadAnimation(context, R.anim.appear));
		    frist = false;
            return view;
        }

		@Override
		public int getCount() {
			if(commentList == null)
			{
				return 0;
			}
			return commentList.size();
		}
		 @Override
		public Object getItem(int index) {
             // TODO Auto-generated method stub
             return commentList.get(index);
     }

     @Override
	public long getItemId(int arg0) {
             // TODO Auto-generated method stub
             return arg0;
     }

	 public void setDataList(ArrayList<Comment> list)
	 {
		 commentList = list;
	 }
	 
	 
	 private class ViewHolder {
			public TextView commontText;
			public TextView minuteAgo;
			public TextView alias;
			public TextView rank;
			public ImageView image;
		}

}