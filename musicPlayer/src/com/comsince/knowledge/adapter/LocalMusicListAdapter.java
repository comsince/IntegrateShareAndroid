package com.comsince.knowledge.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.comsince.knowledge.R;
import com.comsince.knowledge.entity.Music;
import com.comsince.knowledge.utils.AlbumImageLoader;
import com.comsince.knowledge.utils.AlbumImageLoader.ImageCallback;
import com.comsince.knowledge.utils.StrTime;

public class LocalMusicListAdapter extends BaseAdapter {
	private List<Music> musicList;
	private LayoutInflater inflater;
	AlbumImageLoader albumImageLoader;
	ListView listView;

	public LocalMusicListAdapter(Context context, List<Music> musicList, ListView listView) {
		inflater = LayoutInflater.from(context);
		if (musicList != null) {
			this.musicList = musicList;
		} else {
			this.musicList = new ArrayList<Music>();
		}
		// 获取异步加载图片对象
		this.listView = listView;
		albumImageLoader = new AlbumImageLoader(context);
		Log.d("Tag", "constructor");
	}

	@Override
	public int getCount() {
		return musicList.size();
	}

	@Override
	public Object getItem(int position) {
		return musicList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return musicList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d("TAG", "coming here!");
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.play_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.ivAlbum = (ImageView) convertView.findViewById(R.id.ivAlbum);
			viewHolder.tvMusicName = (TextView) convertView.findViewById(R.id.tvMusicName);
			viewHolder.tvSinger = (TextView) convertView.findViewById(R.id.tvSinger);
			viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Music music = musicList.get(position);
		viewHolder.tvMusicName.setText(music.getMusicName());
		viewHolder.tvSinger.setText(music.getSinger());
		String durction = music.getTime();
		viewHolder.tvTime.setText(StrTime.getTime(durction));
		// viewHolder.ivAlbum.setBackgroundResource(R.drawable.default_bg_s);
		//为ivAlbum加tag
		viewHolder.ivAlbum.setTag(music.getAlbumkey());
		Log.d("LocalMusicListAdapter", "music albumkey: "+music.getAlbumkey());
		Bitmap bitmap = albumImageLoader.loadImage(music.getAlbumkey(), new ImageCallback() {

			@Override
			public void loadedImage(String albumkey, Bitmap bitmap) {
				ImageView iv = (ImageView) listView.findViewWithTag(albumkey);
				if (iv != null && bitmap != null) {
					iv.setImageBitmap(bitmap);
				}
			}
		});

		if (bitmap != null) {
			viewHolder.ivAlbum.setBackgroundResource(R.drawable.default_bg_s);
		}else{
			viewHolder.ivAlbum.setBackgroundResource(R.drawable.default_bg_s);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView ivAlbum;
		TextView tvMusicName;
		TextView tvSinger;
		TextView tvTime;
		CheckBox myCheckBoxBox;
	}

}
