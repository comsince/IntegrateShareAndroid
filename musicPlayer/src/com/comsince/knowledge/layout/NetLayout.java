package com.comsince.knowledge.layout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.comsince.knowledge.R;
import com.comsince.knowledge.adapter.MusicOnlineAdapter;
import com.comsince.knowledge.entity.Music;
import com.comsince.knowledge.entity.NetMusic;
import com.comsince.knowledge.entity.NetMusicList;
import com.comsince.knowledge.utils.HttpTool;
import com.comsince.knowledge.utils.SimpleXmlReaderUtil;
import com.comsince.knowledge.view.dialog.StyleDialog;

public class NetLayout extends LinearLayout {
	View rootview;
	ListView netlistview;
	View loadingView;
	LayoutInflater inflater;
	List<Music> netMusics;
	Context context;
	MusicOnlineAdapter adapter;
	SimpleXmlReaderUtil simpleXmlReaderUtil = new SimpleXmlReaderUtil();
	private static String TAG = "NetLayout";

	public NetLayout(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		rootview = inflater.inflate(R.layout.netmusiclist, this, true);
		initView();
		initData();
		initListener();
	}

	public void initView() {
		netlistview = (ListView) rootview.findViewById(R.id.lvSounds);
		loadingView = rootview.findViewById(R.id.loadinginfo);
		netMusics = new ArrayList<Music>();
		/*try {
			loadingView.setVisibility(View.GONE);
			netMusics = generalNetMusics();
			Log.d(TAG, "音乐列表数目：" + netMusics.size());
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		adapter = new MusicOnlineAdapter(context, netMusics, netlistview);
		//设置adapter不要忘记了
		netlistview.setAdapter(adapter);
	}

	public void initData() {
		// 新建 一个线程去下载音乐xml数据
		new Thread() {
			@Override
			public void run() {
				// 获得xml文件的输入流
				try {
					//InputStream in = HttpTool.getStream(HttpTool.URI+"sounds.xml", null, null,HttpTool.GET);
					InputStream in = context.getAssets().open("conf/sounds.xml");
					NetMusicList netMusicList = simpleXmlReaderUtil.readXmlFromInputStream(in, NetMusicList.class);
					Log.d(TAG, "音乐列表：" + netMusicList.getNetMusics().size());
					for (NetMusic netMusic : netMusicList.getNetMusics()) {
						Music music = new Music();
						music.setId(Integer.parseInt(netMusic.getId()));
						music.setMusicName(netMusic.getName());
						music.setSinger(netMusic.getSinger());
						music.setAlbumName(netMusic.getAlbum());
						music.setMusicPath(netMusic.getMusicpath());
						music.setAlbumPath(netMusic.getAlbumpic());
						music.setTime(netMusic.getTime());
						if (handler != null) {
							Message msg = handler.obtainMessage();
							msg.what = 1;
							msg.obj = music;
							Log.d(TAG, "sendhandler" + music.getId());
							handler.sendMessage(msg);
						}
					}
				    if(handler != null){
				    	handler.sendEmptyMessage(0);
				    }
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}.start();
	}
	
	private void initListener(){
		netlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				StyleDialog.getStyleDialog(context).show();
			}
		});
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(context, "音乐地址xml解析完成", Toast.LENGTH_SHORT).show();
				loadingView.setVisibility(View.GONE);
				break;
			case 1:
				// 从xml中解析出一条music, 更新listView
				adapter.addMusic((Music) msg.obj);
				break;

			default:
				loadingView.setVisibility(View.GONE);
				break;
			}
		}

	};
	
	public List<Music> generalNetMusics() throws Exception{
		List<Music>  musics = new ArrayList<Music>();
		InputStream in = context.getAssets().open("conf/sounds.xml");
		NetMusicList netMusicList = simpleXmlReaderUtil.readXmlFromInputStream(in, NetMusicList.class);
		Log.d(TAG, "音乐列表：" + netMusicList.getNetMusics().size());
		for (NetMusic netMusic : netMusicList.getNetMusics()) {
			Music music = new Music();
			music.setId(Integer.parseInt(netMusic.getId()));
			music.setMusicName(netMusic.getName());
			music.setSinger(netMusic.getSinger());
			music.setAlbumName(netMusic.getAlbum());
			music.setMusicPath(netMusic.getMusicpath());
			music.setAlbumPath(netMusic.getAlbumpic());
			music.setTime(netMusic.getTime());
			musics.add(music);
		}
		return musics;
	}

}
