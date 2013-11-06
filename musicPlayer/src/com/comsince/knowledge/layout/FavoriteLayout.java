package com.comsince.knowledge.layout;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;
import com.comsince.knowledge.R;
import com.comsince.knowledge.adapter.OnlineSearchResultAdapter;
import com.comsince.knowledge.constant.Constant;
import com.comsince.knowledge.entity.BaiduDevMusic;
import com.comsince.knowledge.lrcutil.BaiduLrc;

public class FavoriteLayout extends RelativeLayout {
	private View rootView;
	private Context context;
	private LayoutInflater inflater;

	private EditText searchEt;
	private ImageButton searchBt;
	private ImageButton voiceSearchBt;
	private ListView searchResultList;
	private List<BaiduDevMusic> baiduDevMusics;
	private OnlineSearchResultAdapter onlineSearchResultAdapter;
	
	private BaiduASRDigitalDialog mVoiceDialog = null;
	
	private View loadingView;

	public FavoriteLayout(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		rootView = inflater.inflate(R.layout.ui_search_music_main, this, true);

		initView();
		initData();
		initListener();
	}

	public void initView() {
		searchEt = (EditText) rootView.findViewById(R.id.search_bar_et);
		searchBt = (ImageButton) rootView.findViewById(R.id.song_recognition_btn);
		voiceSearchBt = (ImageButton) rootView.findViewById(R.id.search_voice_btn);
		searchResultList = (ListView) rootView.findViewById(R.id.lvSounds);
		loadingView = rootView.findViewById(R.id.loadinginfo);
		loadingView.setVisibility(View.GONE);
	}

	public void initData() {
		baiduDevMusics = new ArrayList<BaiduDevMusic>();
        onlineSearchResultAdapter = new OnlineSearchResultAdapter(context, baiduDevMusics, searchResultList);
        searchResultList.setAdapter(onlineSearchResultAdapter);
	}

	public void initListener() {
		searchBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//清楚已有的数据
				onlineSearchResultAdapter.deleteData();
				//这个不能放到子线程中，只能在UI主线程中操作
				loadingView.setVisibility(View.VISIBLE);
				new UpdateSearchResultThread().start();
			}
		});
		
		voiceSearchBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mVoiceDialog != null) {
                    mVoiceDialog.dismiss();
                }
				initParams();
				mVoiceDialog.show();
			}
		});
		/*searchResultList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				MMAlert.showAlert(context, context.getString(R.string.down_info), context.getResources().getStringArray(R.array.download_music_item), null, new MMAlert.OnAlertSelectId(){

					@Override
					public void onClick(int whichButton) {
						
					}
					
				});
			}
		});*/
	}
	
	/**
	 * 初始化语音对话框参数
	 * **/
	private void initParams(){
		 Bundle params = new Bundle();
         params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, Constant.BAIDU_APP_KEY);
         params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, Constant.BAIDU_APP_SECRET);
         params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_BLUE_DEEPBG);
         mVoiceDialog = new BaiduASRDigitalDialog(context, params);
         mVoiceDialog.setDialogRecognitionListener(new DialogRecognitionListener() {
			
			@Override
			public void onResults(Bundle results) {
				ArrayList<String> rs = results != null ? results.getStringArrayList(RESULTS_RECOGNITION) : null;
                if (rs != null && rs.size() > 0) {
                	searchEt.setText(rs.get(0));
                	//清楚已有的数据
    				onlineSearchResultAdapter.deleteData();
    				//这个不能放到子线程中，只能在UI主线程中操作
    				loadingView.setVisibility(View.VISIBLE);
    				new UpdateSearchResultThread().start();
                }
			}
		});
	}
	
	class UpdateSearchResultThread extends Thread{

		@Override
		public void run() {
			// 从输入框中获取搜索字段
			String searchKey = searchEt.getText().toString().trim();
			if (!TextUtils.isEmpty(searchKey)) {
				// utf-8编码搜索字段
				String searchKeyToUtf = BaiduLrc.EncodeName(searchKey);
				// 调用mp3 api接口
				baiduDevMusics = BaiduLrc.getBaiduDevMusicListBySongName(searchKeyToUtf).getBaiduDevMusics();
				if(baiduDevMusics != null){
					for(BaiduDevMusic baiduDevMusic : baiduDevMusics){
						if (handler != null) {
							Message msg = handler.obtainMessage();
							msg.what = 1;
							msg.obj = baiduDevMusic;
							handler.sendMessage(msg);
							Log.e("F", baiduDevMusic.getSinger());
						}
					}
				}else{
					if(handler !=null){
						handler.sendEmptyMessage(0);
					}
				}
			}else{
				if(handler != null){
					handler.sendEmptyMessage(2);
				}
			}
		}
		
	}
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(context, "没有找到该歌曲，请重试!", Toast.LENGTH_SHORT).show();
				loadingView.setVisibility(View.GONE);
				break;
			case 1:
				// 从xml中解析出一条music, 更新listView
				loadingView.setVisibility(View.GONE);
				searchResultList.setVisibility(View.VISIBLE);
				onlineSearchResultAdapter.addMusic((BaiduDevMusic) msg.obj);
				break;
			case 2:
				Toast.makeText(context, "请输入关键字!", Toast.LENGTH_SHORT).show();
				loadingView.setVisibility(View.GONE);
			default:
				loadingView.setVisibility(View.GONE);
				break;
			}
		}

	};

	public ListView getSearchResultList() {
		return searchResultList;
	}

	public void setSearchResultList(ListView searchResultList) {
		this.searchResultList = searchResultList;
	}
	
	public BaiduASRDigitalDialog getVoiceDialog(){
		return mVoiceDialog;
	}

}
