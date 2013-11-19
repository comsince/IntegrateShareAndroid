package com.comsince.knowledge.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comsince.knowledge.R;

public class SearchLrcDialog extends Dialog implements android.view.View.OnClickListener{
	private Context mContext;
	private EditText edMusicName,edArtist;
	private TextView tvCommit,tvCancel;
	private searchLrcDialogListeners listener;
	private String musicName;
	private String artist;
	
	public interface searchLrcDialogListeners{
		public void commit(String musicName,String artist);
	}
	
	public void setSearchLrcDialogListender(searchLrcDialogListeners searchLrcListener){
		this.listener = searchLrcListener;
	}

	public SearchLrcDialog(Context context) {
		super(context,R.style.Theme_Light_FullScreenDialogAct);
		mContext = context;
		setContentView(R.layout.searchinfo_dialog);
		initViews();
		initEvents();
		setCancelable(true);
		setCanceledOnTouchOutside(false);
	}
	
	public SearchLrcDialog(Context context,String musicName,String artist) {
		super(context,R.style.Theme_Light_FullScreenDialogAct);
		mContext = context;
		this.musicName = musicName;
		this.artist = artist;
		setContentView(R.layout.searchinfo_dialog);
		initViews();
		initEvents();
		setCancelable(true);
		setCanceledOnTouchOutside(false);
	}
	
	private void initViews(){
		edMusicName = (EditText) findViewById(R.id.musicname_info_input);
		edArtist = (EditText) findViewById(R.id.artist_info_input);
		tvCommit = (TextView) findViewById(R.id.search_lrc_info_commit);
		tvCancel = (TextView) findViewById(R.id.search_lrc_info_cancel);
		if(!TextUtils.isEmpty(musicName)){
			edMusicName.setText(musicName);
		}
		if(!TextUtils.isEmpty(artist)){
			edArtist.setText(artist);
		}
	}
	
	private void initEvents(){
		tvCommit.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
	}
	
	private void commit(){
		String musicname = edMusicName.getText().toString().trim();
		String artist = edArtist.getText().toString().trim();
		if(!TextUtils.isEmpty(artist)&&!TextUtils.isEmpty(musicname)){
			if(listener != null){
				listener.commit(musicname, artist);
			}
		}else{
			Toast.makeText(mContext, "请确认歌曲名与歌手已经填写", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_lrc_info_commit:
			commit();
			break;
		case R.id.search_lrc_info_cancel:
			dismiss();
			break;

		}
	}
	

}
