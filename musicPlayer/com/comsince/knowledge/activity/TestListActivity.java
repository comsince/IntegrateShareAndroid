package com.comsince.knowledge.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.comsince.knowledge.MyApplication;
import com.comsince.knowledge.R;
import com.comsince.knowledge.adapter.LocalMusicListAdapter;

public class TestListActivity extends Activity {
	private ListView listview;
	private LocalMusicListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		listview = (ListView) findViewById(R.id.lvSoundsTest);
		adapter = new LocalMusicListAdapter(this, MyApplication.musics);
		listview.setAdapter(adapter);
	}
	

}
