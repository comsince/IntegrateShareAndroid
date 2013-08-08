package com.comsince.knowledge.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.comsince.knowledge.R;
import com.comsince.knowledge.entity.BaiduDevMusic;
import com.comsince.knowledge.entity.Music;

public class OnlineSearchResultAdapter extends BaseAdapter {
	private List<BaiduDevMusic> sounds;
	private LayoutInflater inflater;
	private ListView lvSounds;
	
	
	public OnlineSearchResultAdapter(Context context,List<BaiduDevMusic> sounds, ListView lvSounds) {
		this.inflater = LayoutInflater.from(context);
		this.sounds = sounds;
		this.lvSounds = lvSounds;
	}

    /**
     * 动态增加listview数据
     * */
	public void addMusic(BaiduDevMusic music) {
		if (sounds != null) {
			sounds.add(music);
			this.notifyDataSetChanged();
		}
	}
	
	/**
	 * 删除数据
	 * */
	public void deleteData(){
		if(sounds != null){
			sounds.clear();
		}
	}
	@Override
	public int getCount() {
		return sounds.size();
	}

	@Override
	public Object getItem(int position) {
		return sounds.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(sounds.get(position).getSong_id());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.ui_cloud_home_music_list_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.list_item_click_icon);
			holder.tvMusicName = (TextView) convertView.findViewById(R.id.cloud_music_item_title);
			holder.tvSinger = (TextView) convertView.findViewById(R.id.cloud_music_item_info);
			holder.musicAblum = (TextView) convertView.findViewById(R.id.cloud_music_item_ablum_info);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		BaiduDevMusic music = sounds.get(position);
		holder.tvMusicName.setText(music.getSong());
		holder.tvSinger.setText(music.getSinger());
		holder.musicAblum.setText(" - "+"<<"+music.getAlbum()+">>");
		return convertView;
	}
	class ViewHolder {
		ImageView icon;
		TextView tvMusicName;
		TextView tvSinger;
		TextView musicAblum;

	}

}
