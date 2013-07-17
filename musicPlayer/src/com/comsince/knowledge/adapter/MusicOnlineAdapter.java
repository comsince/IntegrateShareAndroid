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
import com.comsince.knowledge.entity.Music;

public class MusicOnlineAdapter extends BaseAdapter {
	private List<Music> sounds;
	private LayoutInflater inflater;
	private ListView lvSounds;
	
	
	public MusicOnlineAdapter(Context context,List<Music> sounds, ListView lvSounds) {
		this.inflater = LayoutInflater.from(context);
		this.sounds = sounds;
		this.lvSounds = lvSounds;
	}

    /**
     * 动态增加listview数据
     * */
	public void addMusic(Music music) {
		if (sounds != null) {
			sounds.add(music);
			this.notifyDataSetChanged();
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
		return sounds.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.net_play_list_item, null);
			holder = new ViewHolder();
			holder.ivAlbum = (ImageView) convertView.findViewById(R.id.ivAlbum);
			holder.tvMusicName = (TextView) convertView.findViewById(R.id.musicName);
			holder.tvSinger = (TextView) convertView.findViewById(R.id.singer);
			holder.tvLoaded = (TextView) convertView.findViewById(R.id.isdownLoad);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Music music = sounds.get(position);
		holder.tvMusicName.setText(music.getMusicName());
		holder.tvSinger.setText(music.getSinger());
		return convertView;
	}
	class ViewHolder {
		ImageView ivAlbum;
		TextView tvMusicName;
		TextView tvSinger;
		TextView tvLoaded;

	}

}
